package com.android.llc.proringer.database;

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

public class DatabaseConstant {


    public static final int DATABASE_VERSION = 4; // table name


    /**
     * User details table info space
     */

    public static final String TABLE_USER_INFO = "table_user_info"; // table name
    /*
    Attributes for TABLE_USER_INFO
     */
    public static final String USER_ID = "user_id";
    public static final String SYNC_DATE = "date";
    public static final String USERDATA = "data";

    public static final String CREATE_USER_INFO_TABLE = "CREATE TABLE " + TABLE_USER_INFO
            + " (id INTEGER PRIMARY KEY,"
            + USER_ID + " TEXT,"
            + SYNC_DATE + " TEXT,"
            + USERDATA + " TEXT)"; // create table query


    /**
     * Table category info space ,
     * post project category list, post project service list, Home schedule options are backed up
     */
    public static final String TABLE_CATEGORY_INFO = "table_category_info"; // table name
    /*
    Attributes for TABLE_CATEGORY_INFO
     */
    public static final String CATEGORY_LISTING = "category_listing";//category listing
    public static final String SERVICE_LISTING = "service_listing";//service listing
    public static final String HOME_SCHEDULAR = "home_schedule";// Home schedule list

    public static final String CREATE_CATEGORY_INFO_TABLE = "CREATE TABLE " + TABLE_CATEGORY_INFO
            + " (id INTEGER PRIMARY KEY,"
            + CATEGORY_LISTING + " TEXT,"
            + SERVICE_LISTING + " TEXT,"
            + HOME_SCHEDULAR + " TEXT)"; // create table query


}
