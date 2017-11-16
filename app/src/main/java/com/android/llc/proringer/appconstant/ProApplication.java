package com.android.llc.proringer.appconstant;

import android.app.Application;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;

import com.android.llc.proringer.pojo.NotificationData;
import com.android.llc.proringer.pojo.ProjectPostedData;
import com.crashlytics.android.Crashlytics;
import io.fabric.sdk.android.Fabric;

/**
 * Created by bodhidipta on 09/06/17.
 * <!-- * Copyright (c) 2017, Proringer-->
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
 * -->
 */

public class ProApplication extends Application {
    private static ProApplication instance = null;
    private SharedPreferences userPreference = null;
    private SharedPreferences userFbFirstTimeStatus = null;
    private SharedPreferences notificationPreference = null;
    private ProjectPostedData dataSelected = null;

    public String go_to="";

    public static ProApplication getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());
        instance = this;
        userPreference = getSharedPreferences("USER_PREFERENCE", MODE_PRIVATE);
        notificationPreference = getSharedPreferences("NOTIFICATION_PREFERENCE", MODE_PRIVATE);
        userFbFirstTimeStatus=getSharedPreferences("USER_STATUS", MODE_PRIVATE);
    }




    public void setFBUserStatus(int a){
        userFbFirstTimeStatus.edit().clear().apply();
        userFbFirstTimeStatus.edit().putInt("user_status",a).apply();
    }
    public void clearFBUserStatus() {
        userPreference.edit().clear().apply();
    }
    public int getFBUserStatus(){
        return userFbFirstTimeStatus.getInt("user_status",0);
    }




    public void setUserPreference(String user_id,
                                  String user_type,
                                  String first_name,
                                  String last_name,
                                  String zip_code) {
        userPreference.edit().clear().apply();
        userPreference.edit().putString("UserId", user_id).apply();
        userPreference.edit().putString("UserType", user_type).apply();
        userPreference.edit().putString("UserFirstName", first_name).apply();
        userPreference.edit().putString("UserLastName", last_name).apply();
        userPreference.edit().putString("ZipCode", zip_code).apply();
    }

    public void setNotificationPreference(String... params) {
        notificationPreference.edit().clear().apply();
        notificationPreference.edit()
                .putString("email_newsletter", params[0])
                .putString("email_chat_msg", params[1])
                .putString("email_tips_article", params[2])
                .putString("email_project_replies", params[3])
                .putString("mobile_newsletter", params[4])
                .putString("mobile_chat_msg", params[5])
                .putString("mobile_tips_article", params[6])
                .putString("mobile_project_replies", params[7])
                .apply();

    }

    public NotificationData getUserNotification() {
        return new NotificationData(
                notificationPreference.getString("email_newsletter", "FALSE"),
                notificationPreference.getString("email_chat_msg", "FALSE"),
                notificationPreference.getString("email_tips_article", "FALSE"),
                notificationPreference.getString("email_project_replies", "FALSE"),
                notificationPreference.getString("mobile_newsletter", "FALSE"),
                notificationPreference.getString("mobile_chat_msg", "FALSE"),
                notificationPreference.getString("mobile_tips_article", "FALSE"),
                notificationPreference.getString("mobile_project_replies", "FALSE")
        );
    }

    public String getUserId() {
        return userPreference.getString("UserId", "");
    }

    public String getZipCode() {
        return userPreference.getString("ZipCode", "");
    }

    public String getUserType() {
        return userPreference.getString("UserType", "");
    }

    public String getUserFirstName() {
        return userPreference.getString("UserFirstName", "");
    }

    public String getUserEmail() {
        return userPreference.getString("userEmail", "");
    }

    public void setUserEmail(String email) {
        userPreference.edit().putString("userEmail", email).apply();
    }

    public void logOut() {
        userPreference.edit().clear().apply();
    }

    public ProjectPostedData getDataSelected() {
        return dataSelected;
    }

    public void setDataSelected(ProjectPostedData dataSelected) {
        this.dataSelected = dataSelected;
    }


    public static void SetRatingColor(LayerDrawable stars) {
        stars.getDrawable(2).setColorFilter(Color.parseColor("#FFf1592a"),
                PorterDuff.Mode.SRC_ATOP); // for filled stars
        stars.getDrawable(1).setColorFilter(Color.parseColor("#FFf1592a"),
                PorterDuff.Mode.SRC_ATOP); // for half filled stars
        stars.getDrawable(0).setColorFilter(Color.parseColor("#FFd6d7db"),
                PorterDuff.Mode.SRC_ATOP); // for empty stars
    }
}

