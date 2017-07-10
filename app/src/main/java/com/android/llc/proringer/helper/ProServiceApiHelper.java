package com.android.llc.proringer.helper;

import android.content.Context;
import android.database.sqlite.SQLiteException;
import android.graphics.Bitmap;
import android.os.AsyncTask;

import com.android.llc.proringer.appconstant.ProApplication;
import com.android.llc.proringer.database.DatabaseHandler;
import com.android.llc.proringer.pojo.ProCategoryData;
import com.android.llc.proringer.utils.ImageCompressor;
import com.android.llc.proringer.utils.Logger;
import com.android.llc.proringer.utils.NetworkUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.concurrent.TimeUnit;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.BufferedSink;


/**
 * Created by bodhidipta on 15/06/17.
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

public class ProServiceApiHelper {
    private static ProServiceApiHelper instance = null;
    private static Context mcontext;
    private onProCategoryListener listener;
    private final String categoryAPI = "http://esolz.co.in/lab6/proringer_latest/app_categorylist";
    private String serviceAPI = "http://esolz.co.in/lab6/proringer_latest/app_catrgoryservice_list";
    private String registrationAPI = "http://esolz.co.in/lab6/proringer_latest/app_signup";
    private String loginAPi = "http://esolz.co.in/lab6/proringer_latest/app_control/user_login";
    private String forgetPaswword = "http://esolz.co.in/lab6/proringer_latest/app_forgot_password";
    private String resetPaswword = "http://esolz.co.in/lab6/proringer_latest/app_resetpassword";
    private String getUserInformation = "http://esolz.co.in/lab6/proringer_latest/app_userinformation_list";
    private String updateUserInformation = "http://esolz.co.in/lab6/proringer_latest/app_userinfo_edit";
    private String changeUserEmail = "http://esolz.co.in/lab6/proringer_latest/app_change_email";
    private String changeUserPassword = "http://esolz.co.in/lab6/proringer_latest/app_change_password";
    private String getNotificationDetails = "http://esolz.co.in/lab6/proringer_latest/app_notification_list";
    private String updateNotificationDetails = "http://esolz.co.in/lab6/proringer_latest/app_notification_save";
    private String homeSchedulerOptionList = "http://esolz.co.in/lab6/proringer_latest/app_homescheduler_option";
    private String homeSchedulerValuesList = "http://esolz.co.in/lab6/proringer_latest/app_homescheduler_list?user_id=";
    private String updateHomeSchedularOption = "http://esolz.co.in/lab6/proringer_latest/app_homescheduler_save";
    private String inviteFriends = "http://esolz.co.in/lab6/proringer_latest/app_invite_friend";
    private String postProjectApi = "http://esolz.co.in/lab6/proringer_latest/app_project_post";

    public static ProServiceApiHelper getInstance(Context context) {
        if (instance == null)
            instance = new ProServiceApiHelper();
        mcontext = context;
        return instance;
    }

    private ProServiceApiHelper() {
    }

    /**
     * Get category list for post a project or search a pro
     *
     * @param callback
     * @param params
     */
    public void getCategoryList(final onProCategoryListener callback, String... params) {

        if (NetworkUtil.getInstance().isNetworkAvailable(mcontext)) {
            new AsyncTask<String, Void, LinkedList>() {
                String exception = "";
                LinkedList<ProCategoryData> categoryList = null;

                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                    callback.onStartFetch();
                    categoryList = new LinkedList<ProCategoryData>();
                }

                @Override
                protected LinkedList doInBackground(String... params) {
                    try {
                        OkHttpClient client = new OkHttpClient.Builder().connectTimeout(6000, TimeUnit.MILLISECONDS).retryOnConnectionFailure(true).build();
                        Request request = new Request.Builder()
                                .get()
                                .url(categoryAPI)
                                .build();

                        Response response = client.newCall(request).execute();
                        String responsetring = response.body().string();
                        JSONObject mainObject = new JSONObject(responsetring);

                        if (mainObject.getBoolean("response")) {
                            JSONArray lsitingArray = mainObject.getJSONArray("info_array");
                            for (int i = 0; i < lsitingArray.length(); i++) {
                                ProCategoryData data = new ProCategoryData(
                                        lsitingArray.getJSONObject(i).getString("id"),
                                        lsitingArray.getJSONObject(i).getString("parent_id"),
                                        lsitingArray.getJSONObject(i).getString("category_name"),
                                        lsitingArray.getJSONObject(i).getString("category_image")
                                );
                                categoryList.add(data);
                            }
                        } else {
                            exception = mainObject.getString("message");
                        }

                        return categoryList;
                    } catch (Exception e) {
                        e.printStackTrace();
                        exception = e.getMessage();
                        return null;
                    }

                }

                @Override
                protected void onPostExecute(LinkedList linkedList) {
                    super.onPostExecute(linkedList);
                    if (linkedList == null) {
                        callback.onError(exception);
                    } else if (exception.equals("")) {
                        callback.onComplete(linkedList);
                    } else {
                        callback.onError(exception);
                    }
                }
            }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } else {
            callback.onError("No internet connection found. Please check your internet connection.");
        }
    }

    /**
     * Get services list for a category when posting a project
     *
     * @param callback
     * @param params
     */
    public void getServiceList(final onProCategoryListener callback, final String... params) {

        if (NetworkUtil.getInstance().isNetworkAvailable(mcontext)) {
            new AsyncTask<String, Void, LinkedList>() {
                String exception = "";
                LinkedList<ProCategoryData> categoryList = null;
                String apiSer = "";

                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                    callback.onStartFetch();
                    apiSer = serviceAPI + "?parent_category=" + params[0];
                    categoryList = new LinkedList<>();
                }

                @Override
                protected LinkedList doInBackground(String... params) {
                    try {
                        OkHttpClient client = new OkHttpClient.Builder().connectTimeout(6000, TimeUnit.MILLISECONDS).retryOnConnectionFailure(true).build();
                        Request request = new Request.Builder()
                                .get()
                                .url(apiSer)
                                .build();

                        Response response = client.newCall(request).execute();
                        String responsetring = response.body().string();
                        JSONObject mainObject = new JSONObject(responsetring);

                        if (mainObject.getBoolean("response")) {
                            JSONArray lsitingArray = mainObject.getJSONArray("info_array");
                            for (int i = 0; i < lsitingArray.length(); i++) {
                                ProCategoryData data = new ProCategoryData(
                                        lsitingArray.getJSONObject(i).getString("id"),
                                        "",
                                        lsitingArray.getJSONObject(i).getString("category_name"),
                                        ""
                                );
                                categoryList.add(data);
                            }
                        } else {
                            exception = mainObject.getString("message");
                        }

                        return categoryList;
                    } catch (Exception e) {
                        e.printStackTrace();
                        exception = e.getMessage();
                        return null;
                    }

                }

                @Override
                protected void onPostExecute(LinkedList linkedList) {
                    super.onPostExecute(linkedList);
                    if (linkedList == null) {
                        callback.onError(exception);
                    } else if (exception.equals("")) {
                        callback.onComplete(linkedList);
                    } else {
                        callback.onError(exception);
                    }
                }
            }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, params);
        } else {
            callback.onError("No internet connection found. Please check your internet connection.");
        }
    }

    /**
     * For registration of user
     *
     * @param callback
     * @param params
     */

    public void getUserRegistered(final getApiProcessCallback callback, String... params) {
        if (NetworkUtil.getInstance().isNetworkAvailable(mcontext)) {
            new AsyncTask<String, Void, String>() {
                String exception = "";

                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                    callback.onStart();
                }

                @Override
                protected String doInBackground(String... params) {
                    try {

                        RequestBody body = new FormBody.Builder()
                                .add("f_name", params[0])
                                .add("l_name", params[1])
                                .add("email_id", params[2])
                                .add("password", params[3])
                                .add("zipcode", params[4])
                                .build();


                        OkHttpClient client = new OkHttpClient.Builder().connectTimeout(2000, TimeUnit.MILLISECONDS).retryOnConnectionFailure(true).build();

                        Request request = new Request.Builder()
                                .url(registrationAPI)
                                .post(body)
                                .build();

                        Response resposne = client.newCall(request).execute();
                        String response_string = resposne.body().string();
                        JSONObject mainResponseObj = new JSONObject(response_string);
                        if (mainResponseObj.getBoolean("response")) {
                            exception = "";
                            return mainResponseObj.getString("message");
                        } else {
                            exception = "true";
                            return mainResponseObj.getString("message");
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                        exception = e.getMessage();
                        return exception;
                    }

                }

                @Override
                protected void onPostExecute(String s) {
                    super.onPostExecute(s);
                    if (exception.equals("")) {
                        callback.onComplete(s);
                    } else {
                        callback.onError(s);
                    }
                }
            }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, params);
        } else {
            callback.onError("No internet connection found. Please check your internet connection.");
        }
    }

    /**
     * For login of user
     *
     * @param callback
     * @param params
     */

    public void getUserLoggedIn(final getApiProcessCallback callback, String... params) {
        if (NetworkUtil.getInstance().isNetworkAvailable(mcontext)) {
            new AsyncTask<String, Void, String>() {
                String exception = "";

                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                    callback.onStart();
                }

                @Override
                protected String doInBackground(String... params) {
                    try {

                        RequestBody body = new FormBody.Builder()
                                .add("email", params[0])
                                .add("password", params[1])
                                .add("device_type", "a")
                                .build();


                        OkHttpClient client = new OkHttpClient.Builder().connectTimeout(2000, TimeUnit.MILLISECONDS).retryOnConnectionFailure(true).build();

                        Request request = new Request.Builder()
                                .url(loginAPi)
                                .post(body)
                                .build();

                        Response resposne = client.newCall(request).execute();
                        String response_string = resposne.body().string();
                        Logger.printMessage("LogIN", loginAPi + "\n" + params[0] + "/" + params[1] + "\n" + response_string);
                        JSONObject mainResponseObj = new JSONObject(response_string);
                        if (mainResponseObj.getBoolean("response")) {
                            exception = "";
                            JSONObject jsonInfo = mainResponseObj.getJSONObject("info_array");
                            ProApplication.getInstance().setUserPreference(jsonInfo.getString("user_id"), jsonInfo.getString("user_type"), jsonInfo.getString("first_name"), jsonInfo.getString("last_name"));
                            return mainResponseObj.getString("message");
                        } else {
                            exception = "true";
                            return mainResponseObj.getString("message");
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                        exception = e.getMessage();
                        return exception;
                    }

                }

                @Override
                protected void onPostExecute(String s) {
                    super.onPostExecute(s);
                    if (exception.equals("")) {
                        callback.onComplete(s);
                    } else {
                        callback.onError(s);
                    }
                }
            }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, params);
        } else {
            callback.onError("No internet connection found. Please check your internet connection.");
        }
    }


    /**
     * Forget password request..
     *
     * @param email
     * @param callback
     */
    public void forgetPassword(final String email, final getApiProcessCallback callback) {
        if (NetworkUtil.getInstance().isNetworkAvailable(mcontext)) {
            new AsyncTask<String, Void, String>() {
                String exception = "";

                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                    callback.onStart();

                }

                @Override
                protected String doInBackground(String... params) {
                    try {
                        OkHttpClient okHttpClient = new OkHttpClient.Builder().connectTimeout(2000, TimeUnit.MILLISECONDS).retryOnConnectionFailure(true).build();
                        RequestBody requestBody = new FormBody.Builder()
                                .add("user_email", params[0]).build();
                        Request request = new Request.Builder()
                                .url(forgetPaswword)
                                .post(requestBody)
                                .build();
                        Response response = okHttpClient.newCall(request).execute();
                        String responseString = response.body().string();
                        Logger.printMessage("forgetPassw", responseString);
                        JSONObject mainresposne = new JSONObject(responseString);
                        if (mainresposne.getBoolean("response")) {
                            return mainresposne.getString("message");
                        } else {
                            exception = mainresposne.getString("message");
                            return exception;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        exception = e.getMessage();
                        return exception;
                    }

                }

                @Override
                protected void onPostExecute(String s) {
                    super.onPostExecute(s);
                    if (exception.equals("")) {
                        callback.onComplete(s);
                    } else {
                        callback.onError(s);
                    }
                }
            }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, email);
        } else
            callback.onError("No internet connection found. Please check your internet connection.");
    }

    /**
     * reset password sent to mail..
     *
     * @param request_code
     * @param password
     * @param callback
     */
    public void resetPassword(final String request_code, String password, final getApiProcessCallback callback) {
        if (NetworkUtil.getInstance().isNetworkAvailable(mcontext)) {
            new AsyncTask<String, Void, String>() {
                String exception = "";

                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                    callback.onStart();

                }

                @Override
                protected String doInBackground(String... params) {
                    try {
                        OkHttpClient okHttpClient = new OkHttpClient.Builder().connectTimeout(2000, TimeUnit.MILLISECONDS).retryOnConnectionFailure(true).build();
                        RequestBody requestBody = new FormBody.Builder()
                                .add("user_reset_code", params[0])
                                .add("new_password", params[0]).build();
                        Request request = new Request.Builder()
                                .url(resetPaswword)
                                .post(requestBody)
                                .build();
                        Response response = okHttpClient.newCall(request).execute();
                        String responseString = response.body().string();
                        Logger.printMessage("forgetPassw", responseString);
                        JSONObject mainresposne = new JSONObject(responseString);
                        if (mainresposne.getBoolean("response")) {
                            return mainresposne.getString("message");
                        } else {
                            exception = mainresposne.getString("message");
                            return exception;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        exception = e.getMessage();
                        return exception;
                    }

                }

                @Override
                protected void onPostExecute(String s) {
                    super.onPostExecute(s);
                    if (exception.equals("")) {
                        callback.onComplete(s);
                    } else {
                        callback.onError(s);
                    }
                }
            }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, request_code, password);
        } else
            callback.onError("No internet connection found. Please check your internet connection.");
    }

    /**
     * get User information
     *
     * @param callback
     */
    public void getUserInformation(final getApiProcessCallback callback) {
        if (NetworkUtil.getInstance().isNetworkAvailable(mcontext)) {
            new AsyncTask<String, Void, String>() {
                String exception = "";

                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                    callback.onStart();

                }

                @Override
                protected String doInBackground(String... params) {
                    try {
                        OkHttpClient okHttpClient = new OkHttpClient.Builder().connectTimeout(2000, TimeUnit.MILLISECONDS).retryOnConnectionFailure(true).build();
                        String userIfoAPI = getUserInformation + "?user_id=" + ProApplication.getInstance().getUserId();
                        Logger.printMessage("userInfo", ProApplication.getInstance().getUserId());

                        Request request = new Request.Builder()
                                .url(userIfoAPI)
                                .get()
                                .build();
                        Response response = okHttpClient.newCall(request).execute();
                        String responseString = response.body().string();
                        Logger.printMessage("userInfo", responseString);

                        return responseString;
                    } catch (Exception e) {
                        e.printStackTrace();
                        exception = e.getMessage();
                        return exception;
                    }

                }

                @Override
                protected void onPostExecute(final String s) {
                    super.onPostExecute(s);
                    if (exception.equals("")) {
                        try {
                            String datetody = new SimpleDateFormat("dd/MM/yyyy").format(new Date());
                            DatabaseHandler.getInstance(mcontext).insertIntoDatbase(
                                    datetody,
                                    ProApplication.getInstance().getUserId(),
                                    s,
                                    new DatabaseHandler.onCompleteProcess() {
                                        @Override
                                        public void onSuccess() {
                                            callback.onComplete(s);
                                        }

                                        @Override
                                        public void onError(String err) {
                                            callback.onError(err);
                                        }
                                    });
                        } catch (Exception io) {
                            io.printStackTrace();
                            callback.onError(io.getMessage());
                        }


                    } else {
                        callback.onError(s);
                    }
                }
            }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } else
            callback.onError("No internet connection found. Please check your internet connection.");
    }

    /**
     * Update useer information and save on local db
     *
     * @param callback
     * @param params
     */
    public void updateUserInformation(final getApiProcessCallback callback, String... params) {
        if (NetworkUtil.getInstance().isNetworkAvailable(mcontext)) {
            new AsyncTask<String, Void, String>() {
                String exception = "";

                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                    callback.onStart();

                }

                @Override
                protected String doInBackground(String... params) {
                    try {
                        OkHttpClient okHttpClient = new OkHttpClient.Builder().connectTimeout(2000, TimeUnit.MILLISECONDS).retryOnConnectionFailure(true).build();
                        Logger.printMessage("userInfo", ProApplication.getInstance().getUserId());

                        RequestBody requestBody = new FormBody.Builder()
                                .add("user_id", ProApplication.getInstance().getUserId())
                                .add("f_name", params[0])
                                .add("l_name", params[1])
                                .add("address", params[2])
                                .add("city", params[3])
                                .add("country", params[4])
                                .add("state", params[5])
                                .add("zipcode", params[6])
                                .add("phone", params[7])
                                .add("details", params[8])
                                .add("latitude", params[9])
                                .add("longitude", params[10])
                                .build();

                        Request request = new Request.Builder()
                                .url(updateUserInformation)
                                .post(requestBody)
                                .build();
                        Response response = okHttpClient.newCall(request).execute();
                        String responseString = response.body().string();
                        Logger.printMessage("userInfo", responseString);
                        JSONObject respo = new JSONObject(responseString);
                        if (respo.getBoolean("response")) {
                            return respo.getString("message");
                        } else {
                            exception = respo.getString("message");
                            return exception;
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                        exception = e.getMessage();
                        return exception;
                    }

                }

                @Override
                protected void onPostExecute(final String s) {
                    super.onPostExecute(s);
                    if (exception.equals("")) {
                        try {
                            callback.onComplete(s);
                            getUserInformation(callback);
                        } catch (Exception io) {
                            io.printStackTrace();
                            callback.onError(io.getMessage());
                        }

                    } else {
                        callback.onError(s);
                    }
                }
            }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, params);
        } else
            callback.onError("No internet connection found. Please check your internet connection.");
    }

    /**
     * Update user email
     *
     * @param callback
     * @param params
     */
    public void updateUserEmail(final getApiProcessCallback callback, String... params) {
        if (NetworkUtil.getInstance().isNetworkAvailable(mcontext)) {
            new AsyncTask<String, Void, String>() {
                String exception = "";

                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                }

                @Override
                protected String doInBackground(String... params) {
                    try {
                        OkHttpClient client = new OkHttpClient.Builder().connectTimeout(2000, TimeUnit.MILLISECONDS).retryOnConnectionFailure(true).build();
                        RequestBody requestBody = new FormBody.Builder()
                                .add("user_id", ProApplication.getInstance().getUserId())
                                .add("emailid", params[0])
                                .add("conf_emailid", params[1])
                                .add("user_type", "H")
                                .build();
                        Request request = new Request.Builder()
                                .post(requestBody)
                                .url(changeUserEmail)
                                .build();
                        Response response = client.newCall(request).execute();
                        String responseString = response.body().string();
                        JSONObject jsonObject = new JSONObject(responseString);
                        if (jsonObject.getBoolean("response")) {
                            return jsonObject.getString("message");
                        } else {
                            exception = jsonObject.getString("message");
                            return exception;
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                        exception = e.getMessage();
                    }
                    return null;
                }

                @Override
                protected void onPostExecute(String s) {
                    super.onPostExecute(s);
                    if (exception.equals("")) {
                        callback.onComplete(s);
                    } else {
                        callback.onError(s);
                    }
                }
            }.executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, params);
        } else {
            callback.onError("No internet connection found. Please check your internet connection.");
        }

    }

    /**
     * change or update user password
     *
     * @param callback
     * @param params
     */
    public void updateUserPassword(final getApiProcessCallback callback, String... params) {
        if (NetworkUtil.getInstance().isNetworkAvailable(mcontext)) {
            new AsyncTask<String, Void, String>() {
                String exception = "";

                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                }

                @Override
                protected String doInBackground(String... params) {
                    try {
                        OkHttpClient client = new OkHttpClient.Builder().connectTimeout(2000, TimeUnit.MILLISECONDS).retryOnConnectionFailure(true).build();
                        RequestBody requestBody = new FormBody.Builder()
                                .add("user_id", ProApplication.getInstance().getUserId())
                                .add("curr_pass", params[0])
                                .add("new_pass", params[1])
                                .add("conf_pass", params[2])
                                .add("user_type", "H")
                                .build();
                        Request request = new Request.Builder()
                                .post(requestBody)
                                .url(changeUserPassword)
                                .build();
                        Response response = client.newCall(request).execute();
                        String responseString = response.body().string();
                        JSONObject jsonObject = new JSONObject(responseString);
                        if (jsonObject.getBoolean("response")) {
                            return jsonObject.getString("message");
                        } else {
                            exception = jsonObject.getString("message");
                            return exception;
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                        exception = e.getMessage();
                    }
                    return null;
                }

                @Override
                protected void onPostExecute(String s) {
                    super.onPostExecute(s);
                    if (exception.equals("")) {
                        callback.onComplete(s);
                    } else {
                        callback.onError(s);
                    }
                }
            }.executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, params);
        } else {
            callback.onError("No internet connection found. Please check your internet connection.");
        }

    }

    /**
     * get notification status
     *
     * @param callback
     */
    public void getUserNotification(final getApiProcessCallback callback) {
        if (NetworkUtil.getInstance().isNetworkAvailable(mcontext)) {
            new AsyncTask<String, Void, String>() {
                String exception = "";

                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                }

                @Override
                protected String doInBackground(String... params) {
                    try {
                        OkHttpClient client = new OkHttpClient.Builder().connectTimeout(2000, TimeUnit.MILLISECONDS).retryOnConnectionFailure(true).build();

                        String notiAPI = getNotificationDetails + "?user_id=" + ProApplication.getInstance().getUserId();
                        Request request = new Request.Builder()
                                .get()
                                .url(notiAPI)
                                .build();

                        Response response = client.newCall(request).execute();
                        String responseString = response.body().string();
                        JSONObject jsonObject = new JSONObject(responseString);
                        if (jsonObject.getBoolean("response")) {

                            JSONObject jsonrespnse = new JSONObject(responseString);
                            JSONArray infoJsonArr = jsonrespnse.getJSONArray("info_array");
                            JSONObject innerObject = infoJsonArr.getJSONObject(0);
                            JSONObject emailObj = innerObject.getJSONObject("Email");
                            JSONObject mobileObj = innerObject.getJSONObject("Mobile");
                            ProApplication.getInstance().setNotificationPreference(
                                    emailObj.getString("newsletter"),
                                    emailObj.getString("chat_msg"),
                                    emailObj.getString("tips_article"),
                                    emailObj.getString("project_replies"),
                                    mobileObj.getString("newsletter"),
                                    mobileObj.getString("chat_msg"),
                                    mobileObj.getString("tips_article"),
                                    mobileObj.getString("project_replies"));

                            return jsonObject.getString("message");
                        } else {
                            exception = jsonObject.getString("message");
                            return exception;
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                        exception = e.getMessage();
                    }
                    return null;
                }

                @Override
                protected void onPostExecute(String s) {
                    super.onPostExecute(s);
                    if (exception.equals("")) {
                        callback.onComplete(s);
                    } else {
                        callback.onError(s);
                    }
                }
            }.executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);
        } else {
            callback.onError("No internet connection found. Please check your internet connection.");
        }

    }

    /**
     * Update notification status
     *
     * @param callback
     * @param params
     */
    public void updateUserNotification(final getApiProcessCallback callback, String... params) {
        if (NetworkUtil.getInstance().isNetworkAvailable(mcontext)) {
            new AsyncTask<String, Void, String>() {
                String exception = "";

                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                }

                @Override
                protected String doInBackground(String... params) {
                    try {
                        OkHttpClient client = new OkHttpClient.Builder().connectTimeout(2000, TimeUnit.MILLISECONDS).retryOnConnectionFailure(true).build();

                        RequestBody requestBody = new FormBody.Builder()
                                .add("user_id", ProApplication.getInstance().getUserId())
                                .add("email_newsletter", params[0])
                                .add("email_chat_msg", params[1])
                                .add("email_tips_article", params[2])
                                .add("email_project_replies", params[3])
                                .add("mobile_newsletter", params[4])
                                .add("mobile_chat_msg", params[5])
                                .add("mobile_article", params[6])
                                .add("mobile_project_replies", params[7])
                                .build();


                        Request request = new Request.Builder()
                                .post(requestBody)
                                .url(updateNotificationDetails)
                                .build();

                        Response response = client.newCall(request).execute();
                        String responseString = response.body().string();
                        JSONObject jsonObject = new JSONObject(responseString);
                        if (jsonObject.getBoolean("response")) {
                            return jsonObject.getString("message");
                        } else {
                            exception = jsonObject.getString("message");
                            return exception;
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                        exception = e.getMessage();
                    }
                    return null;
                }

                @Override
                protected void onPostExecute(String s) {
                    super.onPostExecute(s);
                    if (exception.equals("")) {
                        callback.onComplete(s);
                    } else {
                        callback.onError(s);
                    }
                }
            }.executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, params);
        } else {
            callback.onError("No internet connection found. Please check your internet connection.");
        }

    }

    /**
     * Get Home Schedular options..
     *
     * @param callback
     */
    public void getHomeSchedularOptionList(final getApiProcessCallback callback) {
        if (NetworkUtil.getInstance().isNetworkAvailable(mcontext)) {
            new AsyncTask<String, Void, String>() {
                String exception = "";

                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                }

                @Override
                protected String doInBackground(String... params) {
                    try {
                        OkHttpClient client = new OkHttpClient.Builder().connectTimeout(2000, TimeUnit.MILLISECONDS).retryOnConnectionFailure(true).build();

                        Request request = new Request.Builder()
                                .get()
                                .url(homeSchedulerOptionList)
                                .build();

                        Response response = client.newCall(request).execute();
                        String responseString = response.body().string();
                        Logger.printMessage("home_svhe", "" + responseString);
                        JSONObject jsonObject = new JSONObject(responseString);
                        if (jsonObject.getBoolean("response")) {
                            return responseString;
                        } else {
                            exception = jsonObject.getString("message");
                            return exception;
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                        exception = e.getMessage();
                    }
                    return null;
                }

                @Override
                protected void onPostExecute(String s) {
                    super.onPostExecute(s);
                    if (exception.equals("")) {
                        callback.onComplete(s);
                    } else {
                        callback.onError(s);
                    }
                }
            }.executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);
        } else {
            callback.onError("No internet connection found. Please check your internet connection.");
        }

    }

    /**
     * Get options previously seted fro home schedular
     *
     * @param callback
     */

    public void getHomeSchedularValuesList(final getApiProcessCallback callback) {
        if (NetworkUtil.getInstance().isNetworkAvailable(mcontext)) {
            new AsyncTask<String, Void, String>() {
                String exception = "";

                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                }

                @Override
                protected String doInBackground(String... params) {
                    try {
                        OkHttpClient client = new OkHttpClient.Builder().connectTimeout(2000, TimeUnit.MILLISECONDS).retryOnConnectionFailure(true).build();

                        String ApiOption = homeSchedulerValuesList + "" + ProApplication.getInstance().getUserId();
                        Logger.printMessage("home_svhe", "" + ApiOption);

                        Request request = new Request.Builder()
                                .get()
                                .url(ApiOption)
                                .build();

                        Response response = client.newCall(request).execute();
                        String responseString = response.body().string();
                        Logger.printMessage("home_svhe", "" + responseString);
                        JSONObject jsonObject = new JSONObject(responseString);
                        if (jsonObject.getBoolean("response")) {
                            return responseString;
                        } else {
                            exception = jsonObject.getString("message");
                            return exception;
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                        exception = e.getMessage();
                    }
                    return null;
                }

                @Override
                protected void onPostExecute(String s) {
                    super.onPostExecute(s);
                    if (exception.equals("")) {
                        callback.onComplete(s);
                    } else {
                        callback.onError(s);
                    }
                }
            }.executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);
        } else {
            callback.onError("No internet connection found. Please check your internet connection.");
        }

    }

    /**
     * Update Home schedular options
     *
     * @param callback
     * @param params
     */
    public void updateHomeSchedularOptions(final getApiProcessCallback callback, String... params) {
        if (NetworkUtil.getInstance().isNetworkAvailable(mcontext)) {
            new AsyncTask<String, Void, String>() {
                String exception = "";
                File upload_temp;

                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                }

                @Override
                protected String doInBackground(String... params) {
                    try {
                        OkHttpClient client = new OkHttpClient.Builder().connectTimeout(6000, TimeUnit.MILLISECONDS).retryOnConnectionFailure(true).build();
                        if (!params[18].equals("")) {
                            try {
                                Bitmap bmp = ImageCompressor.with(mcontext).compressBitmap(params[18]);
                                Logger.printMessage("*****", "%% Bitmap size:: " + (bmp.getByteCount() / 1024) + " kb");
                                upload_temp = new File(mcontext.getCacheDir(), "" + System.currentTimeMillis() + ".png");
                                upload_temp.createNewFile();
                                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                                bmp.compress(Bitmap.CompressFormat.PNG, 0, bos);
                                byte[] bitmapdata = bos.toByteArray();

                                FileOutputStream fos = new FileOutputStream(upload_temp);
                                fos.write(bitmapdata);
                                fos.flush();
                                fos.close();

                            } catch (Exception e) {
                                e.printStackTrace();
                                return null;
                            }

                        }
                        final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/*");
                        MultipartBody.Builder requestBody = new MultipartBody.Builder()
                                .setType(MultipartBody.FORM)
                                .addFormDataPart("user_id", ProApplication.getInstance().getUserId())
                                .addFormDataPart("year_build", "" + params[0])
                                .addFormDataPart("footage", "" + params[1])
                                .addFormDataPart("garage", "" + params[2])
                                .addFormDataPart("property_type", "" + params[3])
                                .addFormDataPart("basement", "" + params[4])
                                .addFormDataPart("size", "" + params[5])
                                .addFormDataPart("roof_age", "" + params[6])
                                .addFormDataPart("ac_age", "" + params[7])
                                .addFormDataPart("furnance_age", "" + params[8])
                                .addFormDataPart("filter_change", "" + params[9])
                                .addFormDataPart("water_heater_age", "" + params[10])
                                .addFormDataPart("window_age", "" + params[11])
                                .addFormDataPart("water_heater_flush", "" + params[12])
                                .addFormDataPart("chimney_sweep", "" + params[13])
                                .addFormDataPart("last_radon_gas", "" + params[14])
                                .addFormDataPart("smoke_detector", "" + params[15])
                                .addFormDataPart("co_detector", "" + params[16])
                                .addFormDataPart("gutter_clean", "" + params[17]);
                        if (upload_temp != null) {
                            Logger.printMessage("*****", "" + upload_temp.getAbsolutePath());
                            requestBody.addFormDataPart("property_image", upload_temp.getName() + "", RequestBody.create(MEDIA_TYPE_PNG, upload_temp));
                        }


                        Logger.printMessage("@logUrl", updateHomeSchedularOption + "?user_id=" + ProApplication.getInstance().getUserId());
                        Request request = new Request.Builder()
                                .post(requestBody.build())
                                .addHeader("content-type", "multipart/form-data")
                                .url(updateHomeSchedularOption)
                                .build();

                        Response response = client.newCall(request).execute();
                        String responseString = response.body().string();

                        Logger.printMessage("home_svhe", "" + responseString);
                        JSONObject jsonObject = new JSONObject(responseString);
                        if (jsonObject.getBoolean("response")) {
                            return responseString;
                        } else {
                            exception = jsonObject.getString("message");
                            return exception;
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                        exception = e.getMessage();
                    }
                    return null;
                }

                @Override
                protected void onPostExecute(String s) {
                    super.onPostExecute(s);
                    if (exception.equals("")) {
                        callback.onComplete(s);
                    } else {
                        callback.onError(s);
                    }
                }
            }.executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, params);
        } else {
            callback.onError("No internet connection found. Please check your internet connection.");
        }

    }

    public void inviteFriends(final getApiProcessCallback callback, String... params) {
        if (NetworkUtil.getInstance().isNetworkAvailable(mcontext)) {
            new AsyncTask<String, Void, String>() {
                String exception = "";


                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                }

                @Override
                protected String doInBackground(String... params) {
                    try {
                        OkHttpClient client = new OkHttpClient.Builder().connectTimeout(6000, TimeUnit.MILLISECONDS).retryOnConnectionFailure(true).build();

                        RequestBody requestBody = new FormBody.Builder()
                                .add("user_id", ProApplication.getInstance().getUserId())
                                .add("first_name", params[0])
                                .add("last_name", params[1])
                                .add("email", params[2])
                                .add("conf_emailid", params[3])
                                .build();

                        Request request = new Request.Builder()
                                .post(requestBody)
                                .url(inviteFriends)
                                .build();

                        Response response = client.newCall(request).execute();
                        String responseString = response.body().string();

                        Logger.printMessage("home_svhe", "" + responseString);
                        JSONObject jsonObject = new JSONObject(responseString);
                        if (jsonObject.getBoolean("response")) {
                            return jsonObject.getString("message");
                        } else {
                            exception = jsonObject.getString("message");
                            return exception;
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                        exception = e.getMessage();
                    }
                    return null;
                }

                @Override
                protected void onPostExecute(String s) {
                    super.onPostExecute(s);
                    if (exception.equals("")) {
                        callback.onComplete(s);
                    } else {
                        callback.onError(s);
                    }
                }
            }.executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, params);

        } else {
            callback.onError("No internet connection found. Please check your internet connection.");
        }

    }

    public void postProject(final getApiProcessCallback callback, String... params) {
        if (NetworkUtil.getInstance().isNetworkAvailable(mcontext)) {
            new AsyncTask<String, Void, String>() {
                String exception = "";
                File upload_temp;

                @Override
                protected void onPreExecute() {

                    super.onPreExecute();
                    callback.onStart();
                }

                @Override
                protected String doInBackground(String... params) {
                        OkHttpClient client = new OkHttpClient.Builder().connectTimeout(6000, TimeUnit.MILLISECONDS).retryOnConnectionFailure(true).build();

                        try {
                            if (!params[6].equals("")) {
                                try {
                                    Bitmap bmp = ImageCompressor.with(mcontext).compressBitmap(params[6]);
                                    Logger.printMessage("*****", "%% Bitmap size:: " + (bmp.getByteCount() / 1024) + " kb");
                                    upload_temp = new File(mcontext.getCacheDir(), "" + System.currentTimeMillis() + ".png");
                                    upload_temp.createNewFile();
                                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                                    bmp.compress(Bitmap.CompressFormat.PNG, 0, bos);
                                    byte[] bitmapdata = bos.toByteArray();

                                    FileOutputStream fos = new FileOutputStream(upload_temp);
                                    fos.write(bitmapdata);
                                    fos.flush();
                                    fos.close();

                                } catch (Exception e) {
                                    e.printStackTrace();
                                    return null;
                                }

                            }
                            final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/*");
                            MultipartBody.Builder requestBody = new MultipartBody.Builder()
                                    .setType(MultipartBody.FORM)
                                    .addFormDataPart("user_id", ProApplication.getInstance().getUserId())
                                    .addFormDataPart("user_type", "H")
                                    .addFormDataPart("cat_id", params[0])
                                    .addFormDataPart("service_id", params[1])
                                    .addFormDataPart("service_look_type", params[2])
                                    .addFormDataPart("property_type", params[3])
                                    .addFormDataPart("project_stage", params[4])
                                    .addFormDataPart("timeframe_id", params[5])
                                    .addFormDataPart("project_details", params[7])
                                    .addFormDataPart("project_zipcode", params[8])
                                    .addFormDataPart("city","")
                                    .addFormDataPart("state", "")
                                    .addFormDataPart("country", params[11])
                                    .addFormDataPart("latitude", params[12])
                                    .addFormDataPart("longitude", params[13])
                                    .addFormDataPart("f_name", params[14])
                                    .addFormDataPart("l_name", params[15])
                                    .addFormDataPart("email_id", params[16])
                                    .addFormDataPart("password", params[17]);
                            if (upload_temp != null) {
                                Logger.printMessage("*****", "" + upload_temp.getAbsolutePath());
                                requestBody.addFormDataPart("project_image", upload_temp.getName() + "", RequestBody.create(MEDIA_TYPE_PNG, upload_temp));
                            }else{
                                requestBody.addFormDataPart("project_image", "");

                            }

                            Request request = new Request.Builder()
                                    .post(requestBody.build())
                                    .url(postProjectApi)
                                    .build();

                            Response response = client.newCall(request).execute();
                            String responseString = response.body().string();

                            Logger.printMessage("home_svhe", "" + responseString);
                            JSONObject jsonObject = new JSONObject(responseString);
                            if (jsonObject.getBoolean("response")) {
                                return jsonObject.getString("message");
                            } else {
                                exception = jsonObject.getString("message");
                                return exception;
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                            exception = e.getMessage();
                        }
                        return null;
                    }

                    @Override
                    protected void onPostExecute (String s){
                        super.onPostExecute(s);
                        if (exception.equals("")) {
                            callback.onComplete(s);
                        } else {
                            callback.onError(s);
                        }
                    }
                }.

                executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, params);

            } else{
                callback.onError("No internet connection found. Please check your internet connection.");
            }

        }

        /**
         * Interface used to get call back for getServiceList and getCategoryList
         */
        public interface onProCategoryListener {
            void onComplete(LinkedList<ProCategoryData> listdata);

            void onError(String error);

            void onStartFetch();
        }

        /**
         * Interface used to get call back for Normal API execution
         */
        public interface getApiProcessCallback {
            void onStart();

            void onComplete(String message);

            void onError(String error);
        }

    }
