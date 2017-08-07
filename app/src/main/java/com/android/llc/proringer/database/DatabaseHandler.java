package com.android.llc.proringer.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import com.android.llc.proringer.utils.Logger;

/**
 * Created by bodhidipta on 24/06/17.
 * <!-- * Copyright (c) 2017, The Proringer-->
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

public class DatabaseHandler extends ProDatabase {
    private static DatabaseHandler ourInstance = null;
    private Context mContext = null;
    private final String TAG = "proDB";

    public static DatabaseHandler getInstance(Context context) {
        return new DatabaseHandler(context);
    }

    private DatabaseHandler(Context context) {
        super(context);
        mContext = context;
    }

    public void insertIntoDatabase(String date, String userId, String dataList, onCompleteProcess listener) {
        SQLiteDatabase db = null;
        try {
            db = this.getReadableDatabase();
            Cursor cursor = db.query(
                    DatabaseConstant.TABLE_USER_INFO,
                    null,
                    DatabaseConstant.USER_ID + "=?",
                    new String[]{userId},
                    null,
                    null,
                    null
            );

            if (cursor != null && cursor.getCount() > 0) {
                cursor.close();
                db.close();

                ContentValues contentValues = new ContentValues();
                contentValues.put(DatabaseConstant.SYNC_DATE, "" + date);
                contentValues.put(DatabaseConstant.USERDATA, "" + dataList);

                db = this.getWritableDatabase();
                int res = db.update(DatabaseConstant.TABLE_USER_INFO, contentValues, DatabaseConstant.USER_ID + "=?", new String[]{userId});
                Logger.printMessage(TAG, "result update " + res);

                db.close();
                listener.onSuccess();
            } else {
                cursor.close();
                db.close();
                ContentValues contentValues = new ContentValues();
                contentValues.put(DatabaseConstant.USER_ID, "" + userId);
                contentValues.put(DatabaseConstant.SYNC_DATE, "" + date);
                contentValues.put(DatabaseConstant.USERDATA, "" + dataList);
                db = this.getWritableDatabase();
                long res = db.insert(DatabaseConstant.TABLE_USER_INFO, null, contentValues);
                Logger.printMessage(TAG, "result insert " + res);
                db.close();
                listener.onSuccess();
            }

        } catch (SQLiteException lqit) {
            lqit.printStackTrace();
            if (db != null && db.isOpen())
                db.close();
            listener.onError(lqit.getMessage());

        }
    }

    public void getUserInfo(String userId, onQueryCompleteListener callback) {
        SQLiteDatabase db = null;
        try {
            db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery("SELECT " + DatabaseConstant.USERDATA + " FROM " + DatabaseConstant.TABLE_USER_INFO + " WHERE " + DatabaseConstant.USER_ID + "=?", new String[]{userId});

            if (cursor != null && cursor.getCount() > 0) {
                cursor.moveToFirst();
                String returnString = cursor.getString(cursor.getColumnIndex(DatabaseConstant.USERDATA));
                cursor.close();
                db.close();
                callback.onSuccess(returnString);
            } else {
                cursor.close();
                db.close();
                callback.onError("No data found");
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (db != null && db.isOpen())
                db.close();

            callback.onError(e.getMessage());
        }
    }


    /**
     * insertHomeScheduleOptions inserts home schedule option list into the db
     * if any data already exists , update will take place.
     *
     * @param jsonString json response from APi.
     */
    public void insertHomeScheduleOptions(String jsonString, onCompleteProcess callback) {
        SQLiteDatabase db = null;
        try {
            db = this.getReadableDatabase();

            Cursor cursor = db.query(DatabaseConstant.TABLE_CATEGORY_INFO, null, null, null, null, null, null);
            if (cursor != null && cursor.getCount() > 0) {
                /*
                Data already exists so update will taken place
                 */

                cursor.close();
                db.close();
                db = this.getWritableDatabase();

                ContentValues values = new ContentValues();
                values.put(DatabaseConstant.HOME_SCHEDULAR, jsonString);
                int res = db.update(DatabaseConstant.TABLE_CATEGORY_INFO, values, null, null);
                db.close();

                if (res > 0)
                    callback.onSuccess();
                else
                    callback.onError("Database error occur.");

            } else {
                /*
                Insert will taken place
                 */
                cursor.close();
                db.close();
                db = this.getWritableDatabase();

                ContentValues values = new ContentValues();
                values.put(DatabaseConstant.HOME_SCHEDULAR, jsonString);
                long res = db.insert(DatabaseConstant.TABLE_CATEGORY_INFO, null, values);
                db.close();
                if (res > 0)
                    callback.onSuccess();
                else
                    callback.onError("Database error occur.");

            }
        } catch (Exception e) {
            e.printStackTrace();
            callback.onError("Database error occur.");
        }
    }

    public interface onCompleteProcess {
        void onSuccess();

        void onError(String s);
    }

    public interface onQueryCompleteListener {
        void onSuccess(String... s);

        void onError(String s);
    }

}
