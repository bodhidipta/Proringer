package com.android.llc.proringer.helper;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;

import com.android.llc.proringer.R;
import com.android.llc.proringer.appconstant.ProApplication;
import com.android.llc.proringer.database.DatabaseHandler;
import com.android.llc.proringer.pojo.SetGetAddressData;
import com.android.llc.proringer.pojo.SetGetProCategoryData;
import com.android.llc.proringer.pojo.SetGetProjectPostedData;
import com.android.llc.proringer.utils.ImageCompressor;
import com.android.llc.proringer.utils.Logger;
import com.android.llc.proringer.utils.NetworkUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


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

    private String currentLat = "";
    private String currentLng = "";

//    private String BaseUrl = "http://esolz.co.in/lab6/proringer_latest/";
    private String BaseUrl = "http://111.93.181.155/lab6/proringer_latest/";

    private String categoryAPI = BaseUrl + "app_categorylist";
    private String serviceAPI = BaseUrl + "app_catrgoryservice_list";
    private String registrationAPI = BaseUrl + "app_signup";
    private String loginAPI = BaseUrl + "app_homeowner_login";
    private String forgetPasswordAPI = BaseUrl + "app_forgot_password";
    private String resetPasswordAPI = BaseUrl + "app_resetpassword";
    private String getUserInformationAPI = BaseUrl + "app_userinformation_list";
    private String updateUserInformationAPI = BaseUrl + "app_userinfo_edit";
    private String changeUserEmailAPI = BaseUrl + "app_change_email";
    private String changeUserPasswordAPI = BaseUrl + "app_change_password";
    private String getNotificationDetailsAPI = BaseUrl + "app_notification_list";
    private String updateNotificationDetailsAPI = BaseUrl + "app_notification_save";
    private String homeSchedulerOptionListAPI = BaseUrl + "app_homescheduler_option";
    private String homeSchedulerValuesListAPI = BaseUrl + "app_homescheduler_list?user_id=";
    private String updateHomeSchedularOptionAPI = BaseUrl + "app_homescheduler_save";
    private String inviteFriendsAPI = BaseUrl + "app_invite_friend";
    private String postProjectAPI = BaseUrl + "app_project_post";
    private String myProjectListAPI = BaseUrl + "app_homeowner_myproject?user_id=";
    private String contactUsAPI = BaseUrl + "app_contact_us";
    private String myProjectDeleteAPI = BaseUrl + "app_myproject_delete";
    private String myProjectDetailsAPI = BaseUrl + "app_myproject_details?user_id=";
    private String myProjectRenewAPI = BaseUrl + "project_renew";
    private String favoriteProsListAPI = BaseUrl + "app_favourite_pros?user_id=";
    private String favoriteProsDeleteAPI = BaseUrl + "app_favourite_pros_delete";
    private String faqInformationAPI = BaseUrl + "app_faq";
    private String termsOfUseAPI = BaseUrl + "app_term";
    private String privacyPolicyAPI = BaseUrl + "app_privacy_policy";
    private String prosListingAPI = BaseUrl + "app_serch_result_project?user_id=";
    private String favouriteProAdddeleteAPI = BaseUrl + "app_favourite_pro_adddelete";
    private String homeownerDashboardAPI = BaseUrl + "app_homeowner_dashboard";
    private String prosIndividualListingAPI = BaseUrl + "app_pro_individual_listing";
    private String prosAddReviewAPI = BaseUrl + "app_homeowner_addreview";
    private String prosIndividualPortfolioImageAPI = BaseUrl + "app_individual_portfolio_image";
    private String prosAllReviewAPI = BaseUrl + "app_homeowner_allreview";
    private String prosReportReviewAPI = BaseUrl + "app_homeowner_reportreview";
    private String profileImageAPI = BaseUrl + "app_homeowner_profileimg";
    private String contactProAPI = BaseUrl + "contact_pro";
    private String editprojectAPI = BaseUrl + "app_project_edit";
    private String loginFBAPI = BaseUrl + "app_facebook_login";
    private String messageListAPI = BaseUrl + "app_project_message";
    private String messageDeleteAPI = BaseUrl + "app_project_message_deleted";
    private String messagesendAPI = BaseUrl + "app_project_msg_send";
    private String usersDeviceUpdateAPI = BaseUrl + "users_device_update";

    public static ProServiceApiHelper getInstance(Context context) {
        if (instance == null)
            instance = new ProServiceApiHelper();
        mcontext = context;
        return instance;
    }

    private ProServiceApiHelper() {
    }

    public void setCurrentLatLng(String currentLat, String currentLng) {
        this.currentLat = currentLat;
        this.currentLng = currentLng;
    }

    public String[] getCurrentLatLng() {
        String str[] = {
                currentLat, currentLng
        };
        return str;
    }

    /**
     * Get category list for post a project or search a pro
     *
     * @param callback
     * @param params
     */
    public void getCategoryListAPI(final onProCategoryListener callback, String... params) {

        if (NetworkUtil.getInstance().isNetworkAvailable(mcontext)) {
            new AsyncTask<String, Void, LinkedList>() {
                String exception = "";
                LinkedList<SetGetProCategoryData> categoryList = null;

                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                    callback.onStartFetch();
                    categoryList = new LinkedList<SetGetProCategoryData>();
                }

                @Override
                protected LinkedList doInBackground(String... params) {
                    try {
                        OkHttpClient client = new OkHttpClient.Builder().connectTimeout(6000, TimeUnit.MILLISECONDS).retryOnConnectionFailure(true).build();

                        Logger.printMessage("categoryAPI", "" + categoryAPI);

                        Request request = new Request.Builder()
                                .get()
                                .url(categoryAPI)
                                .build();

                        Response response = client.newCall(request).execute();
                        String responseString = response.body().string();
                        JSONObject mainObject = new JSONObject(responseString);

                        if (mainObject.getBoolean("response")) {
                            JSONArray lsitingArray = mainObject.getJSONArray("info_array");
                            for (int i = 0; i < lsitingArray.length(); i++) {
                                SetGetProCategoryData data = new SetGetProCategoryData(
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
            callback.onError(mcontext.getResources().getString(R.string.no_internet_connection_found_Please_check_your_internet_connection));
        }
    }

    /**
     * Get services list for a category when posting a project
     *
     * @param callback
     * @param params
     */
    public void getServiceListAPI(final onProCategoryListener callback, final String... params) {

        if (NetworkUtil.getInstance().isNetworkAvailable(mcontext)) {
            new AsyncTask<String, Void, LinkedList>() {
                String exception = "";
                LinkedList<SetGetProCategoryData> categoryList = null;
                String apiSer = "";

                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                    callback.onStartFetch();
                    apiSer = serviceAPI + "?parent_category=" + params[0];

                    Logger.printMessage("apiSer", "" + apiSer);

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
                        String responseString = response.body().string();
                        JSONObject mainObject = new JSONObject(responseString);

                        if (mainObject.getBoolean("response")) {
                            JSONArray lsitingArray = mainObject.getJSONArray("info_array");
                            for (int i = 0; i < lsitingArray.length(); i++) {
                                SetGetProCategoryData data = new SetGetProCategoryData(
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
            callback.onError(mcontext.getResources().getString(R.string.no_internet_connection_found_Please_check_your_internet_connection));
        }
    }

    /**
     * For registration of user
     *
     * @param callback
     * @param params
     */

    public void getUserRegisteredAPI(final getApiProcessCallback callback, String... params) {
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

                        Logger.printMessage("f_name", ":-" + params[0]);
                        Logger.printMessage("l_name", ":-" + params[1]);
                        Logger.printMessage("email_id", ":-" + params[2]);
                        Logger.printMessage("password", ":-" + params[3]);
                        Logger.printMessage("zipcode", ":-" + params[4]);
                        Logger.printMessage("registrationAPI", registrationAPI);


                        OkHttpClient client = new OkHttpClient.Builder().connectTimeout(6000, TimeUnit.MILLISECONDS).retryOnConnectionFailure(true).build();

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
            callback.onError(mcontext.getResources().getString(R.string.no_internet_connection_found_Please_check_your_internet_connection));
        }
    }

    /**
     * For login of user
     *
     * @param callback
     * @param params
     */

    public void getUserLoggedInAPI(final getApiProcessCallback callback, String... params) {
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
                                .add("user_type", "H")
                                .build();

                        Logger.printMessage("email", ":-" + params[0]);
                        Logger.printMessage("password", ":-" + params[1]);
                        Logger.printMessage("device_type", ":-" + "a");
                        Logger.printMessage("user_type", ":-" + "H");
                        Logger.printMessage("loginAPI", loginAPI);


                        OkHttpClient client = new OkHttpClient.Builder().connectTimeout(6000, TimeUnit.MILLISECONDS).retryOnConnectionFailure(true).build();

                        Request request = new Request.Builder()
                                .url(loginAPI)
                                .post(body)
                                .build();

                        Response response = client.newCall(request).execute();
                        String response_string = response.body().string();
                        Logger.printMessage("LogIN", loginAPI + "\n" + params[0] + "/" + params[1] + "\n" + response_string);
                        JSONObject mainResponseObj = new JSONObject(response_string);
                        if (mainResponseObj.getBoolean("response")) {
                            exception = "";
                            JSONObject jsonInfo = mainResponseObj.getJSONObject("info_array");
                            ProApplication.getInstance().setUserPreference(jsonInfo.getString("user_id"), jsonInfo.getString("user_type"), jsonInfo.getString("first_name"), jsonInfo.getString("last_name"), jsonInfo.getString("zipcode"));
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
            callback.onError(mcontext.getResources().getString(R.string.no_internet_connection_found_Please_check_your_internet_connection));
        }
    }


    /**
     * For Facebook login of user
     *
     * @param callback
     * @param params
     */

    public void getUserLoggedInFacebookAPI(final getApiProcessCallback callback, String... params) {
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
                                .add("email", params[2])
                                .add("fb_id", params[3])
                                .add("device_type", "A")
                                .build();

                        Logger.printMessage("f_name", ":-" + params[0]);
                        Logger.printMessage("l_name", ":-" + params[1]);
                        Logger.printMessage("email", ":-" + params[2]);
                        Logger.printMessage("fb_id", ":-" + params[3]);
                        Logger.printMessage("device_type", ":- A");
                        Logger.printMessage("loginFBAPI", loginFBAPI);


                        OkHttpClient client = new OkHttpClient.Builder().connectTimeout(6000, TimeUnit.MILLISECONDS).retryOnConnectionFailure(true).build();

                        Request request = new Request.Builder()
                                .url(loginFBAPI)
                                .post(body)
                                .build();

                        Response response = client.newCall(request).execute();
                        String response_string = response.body().string();

                        Logger.printMessage("loginFBAPI", loginFBAPI + "\n"
                                + params[0] + "\n"
                                + params[1] + "\n"
                                + params[2] + "\n"
                                + params[3] + "\n"
                                + "A" + "\n"
                                + response_string);

                        JSONObject mainResponseObj = new JSONObject(response_string);
                        if (mainResponseObj.getBoolean("response")) {
                            exception = "";
                            JSONObject jsonInfo = mainResponseObj.getJSONObject("info_array");

                            ProApplication.getInstance().setFbFirstTimeJson(response_string);
                            ProApplication.getInstance().setFBUserStatus(mainResponseObj.getInt("user_status"));
                            ProApplication.getInstance().setUserPreference(jsonInfo.getString("user_id"), jsonInfo.getString("user_type"), jsonInfo.getString("first_name"), jsonInfo.getString("last_name"), jsonInfo.getString("zipcode"));

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
                        getUserInformationAPI(callback);
                    } else {
                        callback.onError(s);
                    }
                }
            }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, params);
        } else {
            callback.onError(mcontext.getResources().getString(R.string.no_internet_connection_found_Please_check_your_internet_connection));
        }
    }


    /**
     * Forget password request..
     *
     * @param email
     * @param callback
     */
    public void forgetPasswordAPI(final String email, final getApiProcessCallback callback) {
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
                        OkHttpClient okHttpClient = new OkHttpClient.Builder().connectTimeout(6000, TimeUnit.MILLISECONDS).retryOnConnectionFailure(true).build();
                        RequestBody requestBody = new FormBody.Builder()
                                .add("user_email", params[0]).build();

                        Logger.printMessage("user_email", ":-" + params[0]);
                        Logger.printMessage("forgetPasswordAPI", forgetPasswordAPI);

                        Request request = new Request.Builder()
                                .url(forgetPasswordAPI)
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
            callback.onError(mcontext.getResources().getString(R.string.no_internet_connection_found_Please_check_your_internet_connection));
    }

    /**
     * reset password sent to mail..
     *
     * @param request_code
     * @param password
     * @param callback
     */
    public void resetPasswordAPI(final String request_code, String password, final getApiProcessCallback callback) {
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
                        OkHttpClient okHttpClient = new OkHttpClient.Builder().connectTimeout(6000, TimeUnit.MILLISECONDS).retryOnConnectionFailure(true).build();
                        RequestBody requestBody = new FormBody.Builder()
                                .add("user_reset_code", params[0])
                                .add("new_password", params[1]).build();

                        Logger.printMessage("user_reset_code", ":-" + params[0]);
                        Logger.printMessage("new_password", ":-" + params[1]);
                        Logger.printMessage("resetPasswordAPI", resetPasswordAPI);

                        Request request = new Request.Builder()
                                .url(resetPasswordAPI)
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
            callback.onError(mcontext.getResources().getString(R.string.no_internet_connection_found_Please_check_your_internet_connection));
    }

    /**
     * get User information
     *
     * @param callback
     */
    public void getUserInformationAPI(final getApiProcessCallback callback) {
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
                        OkHttpClient okHttpClient = new OkHttpClient.Builder().connectTimeout(6000, TimeUnit.MILLISECONDS).retryOnConnectionFailure(true).build();
                        String userIfoAPI = getUserInformationAPI + "?user_id=" + ProApplication.getInstance().getUserId();

                        Logger.printMessage("userInfo", ProApplication.getInstance().getUserId());
                        Logger.printMessage("userIfoAPI", "" + userIfoAPI);

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
                            String dateToday = new SimpleDateFormat("dd/MM/yyyy").format(new Date());
                            DatabaseHandler.getInstance(mcontext).insertIntoDatabase(
                                    dateToday,
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
            callback.onError(mcontext.getResources().getString(R.string.no_internet_connection_found_Please_check_your_internet_connection));
    }

    /**
     * Update user information and save on local db
     *
     * @param callback
     * @param params
     */
    public void updateUserInformationAPI(final getApiProcessCallback callback, String... params) {
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
                        OkHttpClient okHttpClient = new OkHttpClient.Builder().connectTimeout(6000, TimeUnit.MILLISECONDS).retryOnConnectionFailure(true).build();

                        Logger.printMessage("user_id", ":-" + ProApplication.getInstance().getUserId());
                        Logger.printMessage("f_name", ":-" + params[0]);
                        Logger.printMessage("l_name", ":-" + params[1]);
                        Logger.printMessage("address", ":-" + params[2]);
                        Logger.printMessage("city", ":-" + params[3]);
                        Logger.printMessage("country", ":-" + params[4]);
                        Logger.printMessage("state", ":-" + params[5]);
                        Logger.printMessage("zipcode", ":-" + params[6]);
                        Logger.printMessage("phone", ":-" + params[7]);
                        Logger.printMessage("details", ":-" + params[8]);
                        Logger.printMessage("latitude", ":-" + params[9]);
                        Logger.printMessage("longitude", ":-" + params[10]);
                        Logger.printMessage("updateUserInformationAPI", updateUserInformationAPI);


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
                                .url(updateUserInformationAPI)
                                .post(requestBody)
                                .build();
                        Response response = okHttpClient.newCall(request).execute();
                        String responseString = response.body().string();
                        Logger.printMessage("UpdateUserInfo", responseString);
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
                        Logger.printMessage("successfullyUpdateMSQL", "YES");
                        try {
                            callback.onComplete(s);
                            getUserInformationAPI(callback);
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
            callback.onError(mcontext.getResources().getString(R.string.no_internet_connection_found_Please_check_your_internet_connection));
    }

    /**
     * Update user email
     *
     * @param callback
     * @param params
     */
    public void updateUserEmailAPI(final getApiProcessCallback callback, String... params) {
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
                        OkHttpClient client = new OkHttpClient.Builder().connectTimeout(6000, TimeUnit.MILLISECONDS).retryOnConnectionFailure(true).build();
                        RequestBody requestBody = new FormBody.Builder()
                                .add("user_id", ProApplication.getInstance().getUserId())
                                .add("emailid", params[0])
                                .add("conf_emailid", params[1])
                                .add("user_type", "H")
                                .build();

                        Logger.printMessage("user_id", ":-" + ProApplication.getInstance().getUserId());
                        Logger.printMessage("emailid", ":-" + params[0]);
                        Logger.printMessage("conf_emailid", ":-" + params[1]);
                        Logger.printMessage("user_type", ":-" + "H");
                        Logger.printMessage("changeUserEmailAPI", changeUserEmailAPI);


                        Request request = new Request.Builder()
                                .post(requestBody)
                                .url(changeUserEmailAPI)
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
            }.executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, params);
        } else {
            callback.onError(mcontext.getResources().getString(R.string.no_internet_connection_found_Please_check_your_internet_connection));
        }
    }

    /**
     * change or update user password
     *
     * @param callback
     * @param params
     */
    public void updateUserPasswordAPI(final getApiProcessCallback callback, String... params) {
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
                        OkHttpClient client = new OkHttpClient.Builder().connectTimeout(6000, TimeUnit.MILLISECONDS).retryOnConnectionFailure(true).build();
                        RequestBody requestBody = new FormBody.Builder()
                                .add("user_id", ProApplication.getInstance().getUserId())
                                .add("curr_pass", params[0])
                                .add("new_pass", params[1])
                                .add("conf_pass", params[2])
                                .add("user_type", "H")
                                .build();

                        Logger.printMessage("user_id", ":-" + ProApplication.getInstance().getUserId());
                        Logger.printMessage("curr_pass", ":-" + params[0]);
                        Logger.printMessage("new_pass", ":-" + params[1]);
                        Logger.printMessage("conf_pass", ":-" + params[2]);
                        Logger.printMessage("user_type", "H");
                        Logger.printMessage("changeUserPasswordAPI", changeUserPasswordAPI);

                        Request request = new Request.Builder()
                                .post(requestBody)
                                .url(changeUserPasswordAPI)
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
            }.executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, params);
        } else {
            callback.onError(mcontext.getResources().getString(R.string.no_internet_connection_found_Please_check_your_internet_connection));
        }

    }

    /**
     * get notification status
     *
     * @param callback
     */
    public void getUserNotificationAPI(final getApiProcessCallback callback) {
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
                        OkHttpClient client = new OkHttpClient.Builder().connectTimeout(6000, TimeUnit.MILLISECONDS).retryOnConnectionFailure(true).build();

                        String notificationAPI = getNotificationDetailsAPI + "?user_id=" + ProApplication.getInstance().getUserId();
                        Request request = new Request.Builder()
                                .get()
                                .url(notificationAPI)
                                .build();

                        Logger.printMessage("notificationAPI", notificationAPI);

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
            }.executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);
        } else {
            callback.onError(mcontext.getResources().getString(R.string.no_internet_connection_found_Please_check_your_internet_connection));
        }
    }

    /**
     * Update notification status
     *
     * @param callback
     * @param params
     */
    public void updateUserNotificationAPI(final getApiProcessCallback callback, String... params) {
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
                        OkHttpClient client = new OkHttpClient.Builder().connectTimeout(6000, TimeUnit.MILLISECONDS).retryOnConnectionFailure(true).build();

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

                        Logger.printMessage("user_id", ":-" + ProApplication.getInstance().getUserId());
                        Logger.printMessage("email_newsletter", ":-" + params[0]);
                        Logger.printMessage("email_chat_msg", ":-" + params[1]);
                        Logger.printMessage("email_tips_article", ":-" + params[2]);
                        Logger.printMessage("email_project_replies", ":-" + params[3]);
                        Logger.printMessage("mobile_newsletter", ":-" + params[4]);
                        Logger.printMessage("mobile_chat_msg", ":-" + params[5]);
                        Logger.printMessage("mobile_article", ":-" + params[6]);
                        Logger.printMessage("mobile_project_replies", ":-" + params[7]);
                        Logger.printMessage("updateNotificationDetailsAPI", updateNotificationDetailsAPI);


                        Request request = new Request.Builder()
                                .post(requestBody)
                                .url(updateNotificationDetailsAPI)
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
            }.executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, params);
        } else {
            callback.onError(mcontext.getResources().getString(R.string.no_internet_connection_found_Please_check_your_internet_connection));
        }

    }

    /**
     * Get Home Scheduler options..
     *
     * @param callback
     */
    public void getHomeSchedularOptionListAPI(final getApiProcessCallback callback) {
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
                        OkHttpClient client = new OkHttpClient.Builder().connectTimeout(6000, TimeUnit.MILLISECONDS).retryOnConnectionFailure(true).build();

                        Logger.printMessage("homeSchedulerOptionList", "" + homeSchedulerOptionListAPI);

                        Request request = new Request.Builder()
                                .get()
                                .url(homeSchedulerOptionListAPI)
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
            }.executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);
        } else {
            callback.onError(mcontext.getResources().getString(R.string.no_internet_connection_found_Please_check_your_internet_connection));
        }

    }

    /**
     * Get options previously seted fro home scheduler
     *
     * @param callback
     */

    public void getHomeSchedularValuesListAPI(final getApiProcessCallback callback) {
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
                        OkHttpClient client = new OkHttpClient.Builder().connectTimeout(6000, TimeUnit.MILLISECONDS).retryOnConnectionFailure(true).build();

                        String ApiOption = homeSchedulerValuesListAPI + "" + ProApplication.getInstance().getUserId();
                        Logger.printMessage("home_svhe", "" + ApiOption);

                        Request request = new Request.Builder()
                                .get()
                                .url(ApiOption)
                                .build();

                        Response response = client.newCall(request).execute();
                        String responseString = response.body().string();
                        Logger.printMessage("home_svhe", responseString);
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
            }.executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);
        } else {
            callback.onError(mcontext.getResources().getString(R.string.no_internet_connection_found_Please_check_your_internet_connection));
        }

    }

    /**
     * Update Home scheduler options
     *
     * @param callback
     * @param params
     */
    public void updateHomeSchedularOptionsAPI(final getApiProcessCallback callback, String... params) {
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
                                return e.getMessage();
                            }
                        }

                        Logger.printMessage("user_id", "-->" + ProApplication.getInstance().getUserId());
                        Logger.printMessage("year_build", "-->" + params[0]);
                        Logger.printMessage("footage", "-->" + params[1]);
                        Logger.printMessage("garage", "-->" + params[2]);
                        Logger.printMessage("property_type", "-->" + params[3]);
                        Logger.printMessage("basement", "-->" + params[4]);
                        Logger.printMessage("size", "-->" + params[5]);
                        Logger.printMessage("roof_age", "-->" + params[6]);
                        Logger.printMessage("ac_age", "-->" + params[7]);
                        Logger.printMessage("furnance_age", "-->" + params[8]);
                        Logger.printMessage("filter_change", "-->" + params[9]);
                        Logger.printMessage("water_heater_age", "-->" + params[10]);
                        Logger.printMessage("window_age", "-->" + params[11]);
                        Logger.printMessage("water_heater_flush", "-->" + params[12]);
                        Logger.printMessage("chimney_sweep", "-->" + params[13]);
                        Logger.printMessage("last_radon_gas", "-->" + params[14]);
                        Logger.printMessage("smoke_detector", "-->" + params[15]);
                        Logger.printMessage("co_detector", "-->" + params[16]);
                        Logger.printMessage("gutter_clean", "-->" + params[17]);


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
                            Logger.printMessage("photo_param", "-->" + upload_temp.getAbsolutePath());
                            Logger.printMessage("*****", "" + upload_temp.getAbsolutePath());
                            requestBody.addFormDataPart("property_image", upload_temp.getName() + "", RequestBody.create(MEDIA_TYPE_PNG, upload_temp));
                        }


                        Logger.printMessage("@logUrl", updateHomeSchedularOptionAPI + "?user_id=" + ProApplication.getInstance().getUserId());
                        Request request = new Request.Builder()
                                .post(requestBody.build())
                                .addHeader("content-type", "multipart/form-data")
                                .url(updateHomeSchedularOptionAPI)
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
            }.executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, params);
        } else {
            callback.onError(mcontext.getResources().getString(R.string.no_internet_connection_found_Please_check_your_internet_connection));
        }

    }

    /**
     * Invite Friends
     *
     * @param callback
     * @param params
     */
    public void inviteFriendsAPI(final getApiProcessCallback callback, String... params) {
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
                        OkHttpClient client = new OkHttpClient.Builder().connectTimeout(6000, TimeUnit.MILLISECONDS).retryOnConnectionFailure(true).build();

                        RequestBody requestBody = new FormBody.Builder()
                                .add("user_id", ProApplication.getInstance().getUserId())
                                .add("first_name", params[0])
                                .add("last_name", params[1])
                                .add("email", params[2])
                                .add("conf_emailid", params[3])
                                .build();

                        Logger.printMessage("user_id", ":-" + ProApplication.getInstance().getUserId());
                        Logger.printMessage("first_name", ":-" + params[0]);
                        Logger.printMessage("last_name", ":-" + params[1]);
                        Logger.printMessage("email", ":-" + params[2]);
                        Logger.printMessage("conf_emailid", ":-" + params[3]);
                        Logger.printMessage("inviteFriendsAPI", inviteFriendsAPI);

                        Request request = new Request.Builder()
                                .post(requestBody)
                                .url(inviteFriendsAPI)
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
            }.executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, params);

        } else {
            callback.onError(mcontext.getResources().getString(R.string.no_internet_connection_found_Please_check_your_internet_connection));
        }

    }

    /**
     * Post Project
     *
     * @param callback
     * @param params
     */

    public void postProjectAPI(final getApiProcessCallback callback, String... params) {
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
                    Logger.printMessage("@user_id", "user_id:" + ProApplication.getInstance().getUserId());
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
                                return e.getMessage();
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
                                .addFormDataPart("city", params[9])
                                .addFormDataPart("state", params[10])
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
                        } else {
                            requestBody.addFormDataPart("project_image", "");

                        }

                        Request request = new Request.Builder()
                                .post(requestBody.build())
                                .url(postProjectAPI)
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
            }.

                    executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, params);

        } else {
            callback.onError(mcontext.getResources().getString(R.string.no_internet_connection_found_Please_check_your_internet_connection));
        }

    }

    /**
     * get MyProject details
     *
     * @param callback
     * @param params
     */
    public void getMyProjectDetailsAPI(final getApiProcessCallback callback, String... params) {
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

                        String ApiOption = myProjectDetailsAPI + "" + ProApplication.getInstance().getUserId() + "&project_id=" + params[0];
                        Logger.printMessage("myProjectdetailsAPI", ApiOption);

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
            }.executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, params);

        } else {
            callback.onError(mcontext.getResources().getString(R.string.no_internet_connection_found_Please_check_your_internet_connection));
        }

    }


    /**
     * get MyProject List
     *
     * @param callback
     */
    public void getMyProjectListAPI(final projectListCallback callback) {
        if (NetworkUtil.getInstance().isNetworkAvailable(mcontext)) {
            new AsyncTask<String, Void, String>() {
                String exception = "";
                LinkedList<SetGetProjectPostedData> linkedList = null;

                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                    linkedList = new LinkedList<SetGetProjectPostedData>();
                    callback.onStart();
                }

                @Override
                protected String doInBackground(String... params) {
                    try {
                        OkHttpClient client = new OkHttpClient.Builder().connectTimeout(6000, TimeUnit.MILLISECONDS).retryOnConnectionFailure(true).build();

                        String ApiOption = myProjectListAPI + "" + ProApplication.getInstance().getUserId();
                        Logger.printMessage("home_myProjectList", "" + ApiOption);

                        Request request = new Request.Builder()
                                .get()
                                .url(ApiOption)
                                .build();

                        Response response = client.newCall(request).execute();
                        String responseString = response.body().string();
                        Logger.printMessage("home_svhe", "" + responseString);
                        JSONObject jsonObject = new JSONObject(responseString);
                        if (jsonObject.getBoolean("response")) {
                            if (jsonObject.has("info_array")) {
                                JSONArray info_array = jsonObject.getJSONArray("info_array");
                                if (info_array.length() > 0) {
                                    for (int i = 0; i < info_array.length(); i++) {
                                        SetGetProjectPostedData data = new SetGetProjectPostedData(
                                                info_array.getJSONObject(i).getString("id"),
                                                info_array.getJSONObject(i).getString("project_name"),
                                                info_array.getJSONObject(i).getJSONObject("property_type").getString("property_type_id"),
                                                info_array.getJSONObject(i).getJSONObject("property_type").getString("property_type_name"),
                                                info_array.getJSONObject(i).getString("city"),
                                                info_array.getJSONObject(i).getString("zip"),
                                                info_array.getJSONObject(i).getJSONObject("project_category").getString("category_id"),
                                                info_array.getJSONObject(i).getJSONObject("project_category").getString("category_name"),
                                                info_array.getJSONObject(i).getJSONObject("project_category_service").getString("service_id"),
                                                info_array.getJSONObject(i).getJSONObject("project_category_service").getString("service name"),
                                                info_array.getJSONObject(i).getJSONObject("country").getString("country_id"),
                                                info_array.getJSONObject(i).getJSONObject("country").getString("country_code"),
                                                info_array.getJSONObject(i).getJSONObject("state").getString("state_id"),
                                                info_array.getJSONObject(i).getJSONObject("state").getString("state_code"),
                                                info_array.getJSONObject(i).getString("project_image"),
                                                info_array.getJSONObject(i).getString("date_time"),
                                                info_array.getJSONObject(i).getString("last_edit"),
                                                info_array.getJSONObject(i).getJSONObject("project_timeframe").getString("timeframe_id"),
                                                info_array.getJSONObject(i).getJSONObject("project_timeframe").getString("timeframe_name"),
                                                info_array.getJSONObject(i).getString("project_service_looktype"),
                                                info_array.getJSONObject(i).getString("project_stage"),
                                                info_array.getJSONObject(i).getString("project_status"),
                                                info_array.getJSONObject(i).getString("project_response"),
                                                info_array.getJSONObject(i).getString("project_award"),
                                                info_array.getJSONObject(i).getString("latitude"),
                                                info_array.getJSONObject(i).getString("longitude"),
                                                info_array.getJSONObject(i).getString("new_user_status")
                                        );

                                        linkedList.add(data);
                                    }
                                }
                            }
                            return responseString;
                        } else {
                            exception = jsonObject.getString("message");
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
                        callback.onComplete(linkedList);
                    } else {
                        callback.onError(s);
                    }
                }
            }.executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);
        } else {
            callback.onError(mcontext.getResources().getString(R.string.no_internet_connection_found_Please_check_your_internet_connection));
        }
    }


    /**
     * FaqActivity Information
     *
     * @param callback
     */
    public void getFaqInformationAPI(final faqCallback callback) {
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
                        OkHttpClient client = new OkHttpClient.Builder().connectTimeout(6000, TimeUnit.MILLISECONDS).retryOnConnectionFailure(true).build();

                        String ApiOption = faqInformationAPI;
                        Logger.printMessage("faqInformation", ApiOption);

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
            }.executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);
        } else {
            callback.onError(mcontext.getResources().getString(R.string.no_internet_connection_found_Please_check_your_internet_connection));
        }
    }


    /**
     * Terms Of Use Information
     *
     * @param callback
     */
    public void getTermsOfUseInformationAPI(final faqCallback callback) {
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
                        OkHttpClient client = new OkHttpClient.Builder().connectTimeout(6000, TimeUnit.MILLISECONDS).retryOnConnectionFailure(true).build();

                        String ApiOption = termsOfUseAPI;
                        Logger.printMessage("termsOfUseAPI", ApiOption);

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
            }.executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);
        } else {
            callback.onError(mcontext.getResources().getString(R.string.no_internet_connection_found_Please_check_your_internet_connection));
        }
    }


    /**
     * Privacy Policy Information
     *
     * @param callback
     */
    public void getPrivacyPolicyInformationAPI(final faqCallback callback) {
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
                        OkHttpClient client = new OkHttpClient.Builder().connectTimeout(6000, TimeUnit.MILLISECONDS).retryOnConnectionFailure(true).build();

                        String ApiOption = privacyPolicyAPI;
                        Logger.printMessage("privacyPolicyAPI", ApiOption);

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
            }.executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);
        } else {
            callback.onError(mcontext.getResources().getString(R.string.no_internet_connection_found_Please_check_your_internet_connection));
        }
    }


    /**
     * Contact us
     *
     * @param callback
     * @param params
     */
    public void contactUsAPI(final getApiProcessCallback callback, String... params) {
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
                        OkHttpClient client = new OkHttpClient.Builder().connectTimeout(6000, TimeUnit.MILLISECONDS).retryOnConnectionFailure(true).build();

                        RequestBody requestBody = new FormBody.Builder()
                                .add("fname", params[0])
                                .add("lname", params[1])
                                .add("email", params[2])
                                .add("phonenumber", params[3])
                                .add("contact_info", params[4])
                                .build();

                        Logger.printMessage("fname", ":-" + params[0]);
                        Logger.printMessage("lname", ":-" + params[1]);
                        Logger.printMessage("email", ":-" + params[2]);
                        Logger.printMessage("phonenumber", ":-" + params[3]);
                        Logger.printMessage("contact_info", ":-" + params[4]);

                        Logger.printMessage("contactUsAPI", contactUsAPI);


                        Request request = new Request.Builder()
                                .post(requestBody)
                                .url(contactUsAPI)
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
            }.executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, params);

        } else {
            callback.onError(mcontext.getResources().getString(R.string.no_internet_connection_found_Please_check_your_internet_connection));
        }

    }


    /**
     * delete my project
     *
     * @param callback
     * @param params
     */
    public void deleteMyProjectAPI(final getApiProcessCallback callback, String... params) {
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
                        OkHttpClient client = new OkHttpClient.Builder().connectTimeout(6000, TimeUnit.MILLISECONDS).retryOnConnectionFailure(true).build();

                        Logger.printMessage("user_id", ":-" + params[0]);
                        Logger.printMessage("project_id", ":-" + params[1]);
                        Logger.printMessage("myProjectDeleteAPI", myProjectDeleteAPI);

                        RequestBody requestBody = new FormBody.Builder()
                                .add("user_id", params[0])
                                .add("project_id", params[1])
                                .build();

                        Request request = new Request.Builder()
                                .post(requestBody)
                                .url(myProjectDeleteAPI)
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
            }.executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, params);

        } else {
            callback.onError(mcontext.getResources().getString(R.string.no_internet_connection_found_Please_check_your_internet_connection));
        }
    }


    /**
     * get favorite Pro list
     *
     * @param callback
     */
    public void getUserFavoriteProsListAPI(final getApiProcessCallback callback, String... params) {
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

                        Logger.printMessage("user_id", ":-" + params[0]);
                        Logger.printMessage("category_search", ":-" + params[1]);
                        Logger.printMessage("zip_search", ":-" + params[2]);

                        String FavProListAPI = favoriteProsListAPI + params[0] + "&category_search=" + params[1] + "&zip_search=" + params[2];

                        OkHttpClient client = new OkHttpClient.Builder().connectTimeout(6000, TimeUnit.MILLISECONDS).retryOnConnectionFailure(true).build();


                        Logger.printMessage("favoriteProsListAPI", FavProListAPI);
                        Request request = new Request.Builder()
                                .get()
                                .url(FavProListAPI)
                                .build();

                        Response response = client.newCall(request).execute();
                        String responseString = response.body().string();
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
            }.executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, params);
        } else {
            callback.onError(mcontext.getResources().getString(R.string.no_internet_connection_found_Please_check_your_internet_connection));
        }
    }


    /**
     * delete my project
     *
     * @param callback
     * @param params
     */
    public void deleteFavoriteProAPI(final getApiProcessCallback callback, String... params) {
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
                        OkHttpClient client = new OkHttpClient.Builder().connectTimeout(6000, TimeUnit.MILLISECONDS).retryOnConnectionFailure(true).build();

                        RequestBody requestBody = new FormBody.Builder()
                                .add("user_id", params[0])
                                .add("favourite_id", params[1])
                                .build();

                        Logger.printMessage("user_id", ":--" + params[0]);
                        Logger.printMessage("favourite_id", ":--" + params[1]);
                        Logger.printMessage("favoriteProsDeleteAPI", favoriteProsDeleteAPI);

                        Request request = new Request.Builder()
                                .post(requestBody)
                                .url(favoriteProsDeleteAPI)
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
            }.executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, params);

        } else {
            callback.onError(mcontext.getResources().getString(R.string.no_internet_connection_found_Please_check_your_internet_connection));
        }
    }






    /**
     * renew my project
     *
     * @param callback
     * @param params
     */
    public void renewProAPI(final getApiProcessCallback callback, String... params) {
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
                        OkHttpClient client = new OkHttpClient.Builder().connectTimeout(6000, TimeUnit.MILLISECONDS).retryOnConnectionFailure(true).build();

                        RequestBody requestBody = new FormBody.Builder()
                                .add("user_id", params[0])
                                .add("p_id", params[1])
                                .build();

                        Logger.printMessage("user_id", ":--" + params[0]);
                        Logger.printMessage("p_id", ":--" + params[1]);
                        Logger.printMessage("favoriteProsDeleteAPI", myProjectRenewAPI);

                        Request request = new Request.Builder()
                                .post(requestBody)
                                .url(myProjectRenewAPI)
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
            }.executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, params);

        } else {
            callback.onError(mcontext.getResources().getString(R.string.no_internet_connection_found_Please_check_your_internet_connection));
        }
    }


    /**
     * Search area by google API
     *
     * @param callback
     * @param params
     */

    public void getSearchAreaAPI(final onSearchZipCallback callback, String... params) {
        if (NetworkUtil.getInstance().isNetworkAvailable(mcontext)) {
            new AsyncTask<String, Void, String>() {
                String exception = "";
                List<SetGetAddressData> addressList;

                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                    callback.onStartFetch();
                }

                @Override
                protected String doInBackground(String... params) {
                    OkHttpClient client = new OkHttpClient.Builder().connectTimeout(6000, TimeUnit.MILLISECONDS).retryOnConnectionFailure(true).build();

                    try {
                        String searchLocalProject = "https://maps.googleapis.com/maps/api/geocode/json?address=" + params[0] + "&key=AIzaSyDoLuAdSE7M9SzeIht7-Bm-WrUjnDQBofg&language=en";
                        Logger.printMessage("searchLocationAPI", "" + searchLocalProject);
                        Request request = new Request.Builder()
                                .url(searchLocalProject)
                                .build();

                        Response response = client.newCall(request).execute();
                        String responseString = response.body().string();

                        JSONObject mainRes = new JSONObject(responseString);

                        if (mainRes.getString("status").equalsIgnoreCase("OK") &&
                                mainRes.has("results") &&
                                mainRes.getJSONArray("results").length() > 0) {

                            addressList = new ArrayList<SetGetAddressData>();
                            JSONArray results = mainRes.getJSONArray("results");

                            for (int i = 0; i < results.length(); i++) {

                                JSONObject outerJsonObj = results.getJSONObject(i);
                                if (outerJsonObj.getJSONArray("types").toString().contains("postal_code")) {

                                    String city = "";
                                    String country = "";
                                    String state_code = "";

                                    /**
                                     * loop through address component
                                     * for country and state
                                     */
                                    if (outerJsonObj.has("address_components") &&
                                            outerJsonObj.getJSONArray("address_components").length() > 0) {

                                        JSONArray address_components = outerJsonObj.getJSONArray("address_components");

                                        for (int j = 0; j < address_components.length(); j++) {

                                            if (address_components.getJSONObject(j).has("types") &&
                                                    address_components.getJSONObject(j).getJSONArray("types").length() > 0
                                                    ) {

                                                JSONArray types = address_components.getJSONObject(j).getJSONArray("types");

                                                for (int k = 0; k < types.length(); k++) {
                                                    if (types.getString(k).equals("administrative_area_level_2")) {
                                                        city = address_components.getJSONObject(j).getString("short_name");
                                                    }

                                                    if (types.getString(k).equals("administrative_area_level_1")) {
                                                        state_code = address_components.getJSONObject(j).getString("short_name");
                                                    }

                                                    if (types.getString(k).equals("country")) {
                                                        country = address_components.getJSONObject(j).getString("short_name");
                                                    }
                                                }
                                            }
                                        }
                                        if (country.equals("CA") || country.equals("US")) {
                                            SetGetAddressData data = new SetGetAddressData(
                                                    outerJsonObj.getString("formatted_address"),
                                                    state_code,
                                                    country,
                                                    params[0]
                                            );
                                            data.setCity(city);
                                            data.setLatitude(outerJsonObj.getJSONObject("geometry").getJSONObject("location").getString("lat"));
                                            data.setLongitude(outerJsonObj.getJSONObject("geometry").getJSONObject("location").getString("lng"));
                                            addressList.add(data);
                                        } else {
                                            exception = "This Zip code does not belongs to the USA or CANADA";
                                        }
                                    }
                                } else {
                                    exception = "Please enter valid postal code.";
                                }
                            }
                        }

                        Logger.printMessage("location", "" + responseString);
                        return responseString;
                    } catch (Exception e) {
                        e.printStackTrace();
                        exception = "Some error occured while searching entered zip. Please search again.";
                        return exception;
                    }
                }

                @Override
                protected void onPostExecute(String s) {
                    super.onPostExecute(s);
                    if (exception.equals("")) {
                        if (addressList != null && addressList.size() > 0)
                            callback.onComplete(addressList);
                    } else {
                        callback.onError(exception);
                    }
                }
            }.executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, params);

        } else {
            callback.onError(mcontext.getResources().getString(R.string.no_internet_connection_found_Please_check_your_internet_connection));
        }
    }

    /**
     * get zip code  using google api
     *
     * @param callback
     */
    public void getZipCodeUsingGoogleAPI(final getApiProcessCallback callback) {
        if (NetworkUtil.getInstance().isNetworkAvailable(mcontext)) {
            new AsyncTask<String, Void, String>() {
                ;
                String exception = "";

                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                    callback.onStart();
                }

                @Override
                protected String doInBackground(String... params) {
                    try {
                        OkHttpClient client = new OkHttpClient.Builder().connectTimeout(6000, TimeUnit.MILLISECONDS).retryOnConnectionFailure(true).build();

                        String geocodenotiAPI = "https://maps.googleapis.com/maps/api/geocode/json?latlng=" + ProServiceApiHelper.getInstance(mcontext).getCurrentLatLng()[0] + "," + ProServiceApiHelper.getInstance(mcontext).getCurrentLatLng()[1]
                                + "&key=AIzaSyDoLuAdSE7M9SzeIht7-Bm-WrUjnDQBofg&language=en";
                        Logger.printMessage("geocode", geocodenotiAPI);
                        Request request = new Request.Builder()
                                .get()
                                .url(geocodenotiAPI)
                                .build();

                        Response response = client.newCall(request).execute();
                        String responseString = response.body().string();
                        JSONObject jsonObject = new JSONObject(responseString);
                        // Logger.printMessage("jsonObject",""+jsonObject);
                        if (jsonObject.getString("status").equalsIgnoreCase("OK")) {
                            return responseString;
                        } else {
                            exception = jsonObject.getString("error_message");
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
            }.executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);
        } else {
            callback.onError(mcontext.getResources().getString(R.string.no_internet_connection_found_Please_check_your_internet_connection));
        }
    }

    /**
     * get Location List code  using google api
     *
     * @param param
     * @param callback
     */

    public void getLocationListUsingGoogleAPI(String param, final getApiProcessCallback callback) {

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
                    OkHttpClient client = new OkHttpClient.Builder().connectTimeout(6000, TimeUnit.MILLISECONDS).retryOnConnectionFailure(true).build();

                    try {
                        String searchLocalProject = "https://maps.googleapis.com/maps/api/place/autocomplete/json?input=" + params[0] + "&key=AIzaSyDoLuAdSE7M9SzeIht7-Bm-WrUjnDQBofg&language=en";
                        Logger.printMessage("searchLocationAPI", "" + searchLocalProject);
                        Request request = new Request.Builder()
                                .url(searchLocalProject)
                                .build();

                        Response response = client.newCall(request).execute();
                        String responseString = response.body().string();

                        JSONObject mainRes = new JSONObject(responseString);

                        if (mainRes.getString("status").equalsIgnoreCase("OK")) {
                            return responseString;
                        } else {
                            exception = mainRes.getString("status");
                            return exception;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        exception = "Something error. Please search again.";
                    }
                    return null;
                }

                @Override
                protected void onPostExecute(String s) {
                    super.onPostExecute(s);
                    if (exception.equals("")) {
                        callback.onComplete(s);
                    } else {
                        callback.onError(exception);
                    }
                }
            }.executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, param);

        } else {
            callback.onError("No internet connection found. Please check your internet connection.");
        }
    }


    public void getSearchCountriesByPlacesFilterAPI(final onSearchPlacesNameCallback callback, String... params) {
        if (NetworkUtil.getInstance().isNetworkAvailable(mcontext)) {
            new AsyncTask<String, Void, String>() {
                String exception = "";
                ArrayList<String> addressList;

                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                    callback.onStartFetch();
                }

                @Override
                protected String doInBackground(String... params) {
                    OkHttpClient client = new OkHttpClient.Builder().connectTimeout(6000, TimeUnit.MILLISECONDS).retryOnConnectionFailure(true).build();

                    try {
                        String searchLocalProject = "https://maps.googleapis.com/maps/api/place/autocomplete/json?input=" + params[0] + "&key=AIzaSyDoLuAdSE7M9SzeIht7-Bm-WrUjnDQBofg&language=en";
                        Logger.printMessage("searchLocationAPI", "" + searchLocalProject);
                        Request request = new Request.Builder()
                                .url(searchLocalProject)
                                .build();

                        Response response = client.newCall(request).execute();
                        String responseString = response.body().string();

                        JSONObject mainRes = new JSONObject(responseString);

                        if (mainRes.getString("status").equalsIgnoreCase("OK") &&
                                mainRes.has("predictions") &&
                                mainRes.getJSONArray("predictions").length() > 0) {

                            addressList = new ArrayList<String>();
                            JSONArray predictions = mainRes.getJSONArray("predictions");

                            for (int i = 0; i < predictions.length(); i++) {

                                JSONObject innerIncer = predictions.getJSONObject(i);

                                if (innerIncer.has("terms") &&
                                        innerIncer.getJSONArray("terms").length() > 0) {

                                    /**
                                     * loop through address component
                                     * for country and state
                                     */

                                    JSONArray terms = innerIncer.getJSONArray("terms");

                                    for (int j = 0; j < terms.length(); j++) {
                                        if (terms.getJSONObject(j).getString("value").contains("United States") ||
                                                terms.getJSONObject(j).getString("value").contains("Canada")
                                                ||terms.getJSONObject(j).getString("value").contains("USA")
                                                ||terms.getJSONObject(j).getString("value").contains("US")
                                                ||terms.getJSONObject(j).getString("value").contains("CA")
                                                ) {
                                            Logger.printMessage("description", "" + innerIncer.getString("description"));
                                            addressList.add(innerIncer.getString("description"));
                                            break;
                                        }
                                    }
                                }
                            }
                        }

                        return responseString;
                    } catch (Exception e) {
                        e.printStackTrace();
                        exception = "Some error occured while searching entered zip. Please search again.";
                        return exception;
                    }
                }

                @Override
                protected void onPostExecute(String s) {
                    super.onPostExecute(s);
                    Logger.printMessage("location", s);
                    if (exception.equals("")) {
                        if (addressList != null && addressList.size() > 0)
                            callback.onComplete(addressList);
                    } else {
                        callback.onError(exception);
                    }
                }
            }.executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, params);

        } else {
            callback.onError(mcontext.getResources().getString(R.string.no_internet_connection_found_Please_check_your_internet_connection));
        }
    }


    /**
     * get Zip city location name State List code  using google api
     *
     * @param callback
     */

    public void getZipLocationStateAPI(final getApiProcessCallback callback, String... params) {

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
                    OkHttpClient client = new OkHttpClient.Builder().connectTimeout(6000, TimeUnit.MILLISECONDS).retryOnConnectionFailure(true).build();

                    try {
                        String query = URLEncoder.encode(params[0], "utf-8");
                        String searchLocalProject = "https://maps.googleapis.com/maps/api/geocode/json?address=" + query + "&key=AIzaSyDoLuAdSE7M9SzeIht7-Bm-WrUjnDQBofg&language=en";
                        Logger.printMessage("searchLocationAPI", "" + searchLocalProject);
                        Request request = new Request.Builder()
                                .url(searchLocalProject)
                                .build();

                        Response response = client.newCall(request).execute();
                        String responseString = response.body().string();

                        JSONObject mainRes = new JSONObject(responseString);

                        if (mainRes.getString("status").equalsIgnoreCase("OK")) {
                            return responseString;
                        } else {
                            exception = mainRes.getString("status");
                            return exception;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        exception = "Something error. Please search again.";
                        return exception;
                    }
                }

                @Override
                protected void onPostExecute(String s) {
                    super.onPostExecute(s);
                    if (exception.equals("")) {
                        callback.onComplete(s);
                    } else {
                        callback.onError(exception);
                    }
                }
            }.executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, params);

        } else {
            callback.onError(mcontext.getResources().getString(R.string.no_internet_connection_found_Please_check_your_internet_connection));
        }
    }


    /**
     * get Location List code  using google api
     *
     * @param callback
     * @param params
     */

    public void getProsListingAPI(final getApiProcessCallback callback, String... params) {
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
//                        OkHttpClient client = new OkHttpClient.Builder().connectTimeout(6000, TimeUnit.MILLISECONDS).retryOnConnectionFailure(true).build();


                        Logger.printMessage("user_id", ":-" + params[0]);
                        Logger.printMessage("category_search", ":-" + params[1]);
                        Logger.printMessage("zip_search", ":-" + params[2]);
                        Logger.printMessage("prosListingAPI", prosListingAPI + params[0] + "&category_search=" + params[1] + "&zip_search=" + params[2]);

//                        Request request = new Request.Builder()
//                                .get()
//                                .url(prosListingAPI + params[0] + "&category_search=" + params[1] + "&zip_search=" + params[2])
//                                .build();

//                        Response response = client.newCall(request).execute();

                        URL mUrl = new URL(prosListingAPI + params[0] + "&category_search=" + params[1] + "&zip_search=" + params[2]);
                        HttpURLConnection httpConnection = (HttpURLConnection) mUrl.openConnection();
                        httpConnection.setRequestMethod("GET");
                        httpConnection.setRequestProperty("Content-length", "0");
                        httpConnection.setUseCaches(true);
                        httpConnection.setAllowUserInteraction(true);
                        httpConnection.setConnectTimeout(160000);
                        httpConnection.setReadTimeout(160000);

                        httpConnection.connect();

                        int responseCode = httpConnection.getResponseCode();
                        StringBuilder sb = new StringBuilder();
                        if (responseCode == HttpURLConnection.HTTP_OK) {
                            BufferedReader br = new BufferedReader(new InputStreamReader(httpConnection.getInputStream()));

                            String line;
                            while ((line = br.readLine()) != null) {
                                sb.append(line + "\n");
                            }
                            br.close();
                        }


                        String responseString = sb.toString();

                        Logger.printMessage("prosListingAPI", "" + responseString);
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
            }.executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, params);

        } else {
            callback.onError(mcontext.getResources().getString(R.string.no_internet_connection_found_Please_check_your_internet_connection));
        }
    }

    /**
     * app favourite pro add or delete
     *
     * @param callback
     * @param params
     */
    public void favouriteProAddDeleteAPI(final getApiProcessCallback callback, String... params) {

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
                        OkHttpClient okHttpClient = new OkHttpClient.Builder().connectTimeout(6000, TimeUnit.MILLISECONDS).retryOnConnectionFailure(true).build();

                        Logger.printMessage("user_id", ":-" + params[0]);
                        Logger.printMessage("pro_id", ":-" + params[1]);
                        Logger.printMessage("updateUserInformationAPI", favouriteProAdddeleteAPI);

                        RequestBody requestBody = new FormBody.Builder()
                                .add("user_id", params[0])
                                .add("pro_id", params[1])
                                .build();

                        Request request = new Request.Builder()
                                .url(favouriteProAdddeleteAPI)
                                .post(requestBody)
                                .build();
                        Response response = okHttpClient.newCall(request).execute();
                        String responseString = response.body().string();
                        Logger.printMessage("UpdateUserInfo", responseString);
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
                        } catch (Exception io) {
                            callback.onError(io.getMessage());
                        }
                    } else {
                        callback.onError(s);
                    }
                }
            }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, params);
        } else
            callback.onError(mcontext.getResources().getString(R.string.no_internet_connection_found_Please_check_your_internet_connection));
    }


    /**
     * get DashBoard details
     *
     * @param callback
     */
    public void getDashBoardDetailsAPI(final getApiProcessCallback callback) {
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

                        String dashAPI = homeownerDashboardAPI + "?user_id=" + ProApplication.getInstance().getUserId();
                        Request request = new Request.Builder()
                                .get()
                                .url(dashAPI)
                                .build();

                        Logger.printMessage("homeownerDashboardAPI", dashAPI);

                        Response response = client.newCall(request).execute();
                        String responseString = response.body().string();
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
            }.executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);
        } else {
            callback.onError(mcontext.getResources().getString(R.string.no_internet_connection_found_Please_check_your_internet_connection));
        }
    }


    /**
     * get pro_individual_listing
     *
     * @param callback
     * @param params
     */
    public void getProsIndividualListingAPI(final getApiProcessCallback callback, String... params) {

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
                        OkHttpClient okHttpClient = new OkHttpClient.Builder().connectTimeout(6000, TimeUnit.MILLISECONDS).retryOnConnectionFailure(true).build();

                        Logger.printMessage("user_id", ":-" + params[0]);
                        Logger.printMessage("pro_id", ":-" + params[1]);


                        String prosIndividualListAPI = prosIndividualListingAPI + "?" + "user_id=" + params[0] + "&pro_id=" + params[1];

                        Logger.printMessage("prosIndividualListAPI", prosIndividualListAPI);

                        Request request = new Request.Builder()
                                .get()
                                .url(prosIndividualListAPI)
                                .build();


                        Response response = okHttpClient.newCall(request).execute();
                        String responseString = response.body().string();

                        Logger.printMessage("prosIndividualDetails", responseString);

                        JSONObject responseJsonObj = new JSONObject(responseString);
                        if (responseJsonObj.getBoolean("response")) {
                            return responseString;
                        } else {
                            exception = responseJsonObj.getString("message");
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
                        } catch (Exception io) {
                            callback.onError(io.getMessage());
                        }
                    } else {
                        callback.onError(s);
                    }
                }
            }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, params);
        } else
            callback.onError(mcontext.getResources().getString(R.string.no_internet_connection_found_Please_check_your_internet_connection));
    }


    /**
     * For add review of user
     *
     * @param callback
     * @param params
     */

    public void prosAddReviewAPI(final getApiProcessCallback callback, String... params) {
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
                                .add("user_id", params[0])
                                .add("pro_id", params[1])
                                .add("review_rate", params[2])
                                .add("review_message", params[3])
                                .build();

                        Logger.printMessage("user_id", ":-" + params[0]);
                        Logger.printMessage("pro_id", ":-" + params[1]);
                        Logger.printMessage("review_rate", ":-" + params[2]);
                        Logger.printMessage("review_message", ":-" + params[3]);
                        Logger.printMessage("prosAddReview", prosAddReviewAPI);


                        OkHttpClient client = new OkHttpClient.Builder().connectTimeout(6000, TimeUnit.MILLISECONDS).retryOnConnectionFailure(true).build();

                        Request request = new Request.Builder()
                                .url(prosAddReviewAPI)
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
            callback.onError(mcontext.getResources().getString(R.string.no_internet_connection_found_Please_check_your_internet_connection));
        }
    }


    /**
     * get all review listing of pros
     *
     * @param callback
     * @param params
     */
    public void getProsAllReviewAPI(final getApiProcessCallback callback, String... params) {

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
                        OkHttpClient okHttpClient = new OkHttpClient.Builder().connectTimeout(6000, TimeUnit.MILLISECONDS).retryOnConnectionFailure(true).build();

                        Logger.printMessage("user_id", ":-" + params[0]);
                        Logger.printMessage("pro_id", ":-" + params[1]);
                        Logger.printMessage("start_from", ":-" + params[2]);
                        Logger.printMessage("perpage", ":-" + params[3]);

                        Logger.printMessage("prosAllReviewAPI", prosAllReviewAPI + "?" + "user_id=" + params[0] + "&pro_id=" + params[1] + "&start_from=" + params[2] + "&perpage=" + params[3]);

                        Request request = new Request.Builder()
                                .get()
                                .url(prosAllReviewAPI + "?" + "user_id=" + params[0] + "&pro_id=" + params[1] + "&start_from=" + params[2] + "&perpage=" + params[3])
                                .build();

                        Response response = okHttpClient.newCall(request).execute();
                        String responseString = response.body().string();

                        Logger.printMessage("prosAllReviewAPIDetails", responseString);

                        JSONObject responseJsonObj = new JSONObject(responseString);
                        if (responseJsonObj.getBoolean("response")) {
                            return responseString;
                        } else {
                            exception = responseJsonObj.getString("message");
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
                        } catch (Exception io) {
                            callback.onError(io.getMessage());
                        }
                    } else {
                        callback.onError(s);
                    }
                }
            }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, params);
        } else
            callback.onError(mcontext.getResources().getString(R.string.no_internet_connection_found_Please_check_your_internet_connection));
    }


    /**
     * get portfolio image listing
     *
     * @param callback
     * @param params
     */
    public void getProIndividualPortfolioImageAPI(final getApiProcessCallback callback, String... params) {

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
                        OkHttpClient okHttpClient = new OkHttpClient.Builder().connectTimeout(6000, TimeUnit.MILLISECONDS).retryOnConnectionFailure(true).build();

                        Logger.printMessage("portfolio_id", ":-" + params[0]);

                        Logger.printMessage("prosIndividualPortfolioImageAPI", prosIndividualPortfolioImageAPI + "?" + "portfolio_id=" + params[0]);

                        Request request = new Request.Builder()
                                .get()
                                .url(prosIndividualPortfolioImageAPI + "?" + "portfolio_id=" + params[0])
                                .build();

                        Response response = okHttpClient.newCall(request).execute();
                        String responseString = response.body().string();

                        Logger.printMessage("proIndividualPortfolioImageList", responseString);

                        JSONObject responseJsonObj = new JSONObject(responseString);
                        if (responseJsonObj.getBoolean("response")) {
                            return responseString;
                        } else {
                            exception = responseJsonObj.getString("message");
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
                        } catch (Exception io) {
                            callback.onError(io.getMessage());
                        }
                    } else {
                        callback.onError(s);
                    }
                }
            }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, params);
        } else
            callback.onError(mcontext.getResources().getString(R.string.no_internet_connection_found_Please_check_your_internet_connection));
    }


    /**
     * For add report review of user
     *
     * @param callback
     * @param params
     */

    public void addReviewReportAbuseAPI(final getApiProcessCallback callback, String... params) {
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
                                .add("user_id", params[0])
                                .add("review_report_id", params[1])
                                .add("report_comment", params[2])
                                .build();

                        Logger.printMessage("user_id", ":-" + params[0]);
                        Logger.printMessage("review_report_id", ":-" + params[1]);
                        Logger.printMessage("report_comment", ":-" + params[2]);
                        Logger.printMessage("prosReportReviewAPI", prosReportReviewAPI);


                        OkHttpClient client = new OkHttpClient.Builder().connectTimeout(6000, TimeUnit.MILLISECONDS).retryOnConnectionFailure(true).build();

                        Request request = new Request.Builder()
                                .url(prosReportReviewAPI)
                                .post(body)
                                .build();

                        Response resposne = client.newCall(request).execute();
                        String response_string = resposne.body().string();
                        JSONObject mainResponseObj = new JSONObject(response_string);

                        Logger.printMessage("reportReviewResponseObj", "" + mainResponseObj);

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
            callback.onError(mcontext.getResources().getString(R.string.no_internet_connection_found_Please_check_your_internet_connection));
        }
    }


    /**
     * UpLoad Profile Image
     *
     * @param callback
     * @param params
     */

    public void upLoadProfileImageAPI(final getApiProcessCallback callback, String... params) {
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
                        if (!params[1].equals("")) {
                            try {
                                Bitmap bmp = ImageCompressor.with(mcontext).compressBitmap(params[1]);
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
                                return e.getMessage();
                            }
                        }
                        final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/*");

                        MultipartBody.Builder requestBody = new MultipartBody.Builder()
                                .setType(MultipartBody.FORM)
                                .addFormDataPart("user_id", params[0]);
                        if (upload_temp != null) {
                            Logger.printMessage("*****", "" + upload_temp.getAbsolutePath());
                            requestBody.addFormDataPart("profile_image", upload_temp.getName() + "", RequestBody.create(MEDIA_TYPE_PNG, upload_temp));
                        } else {
                            requestBody.addFormDataPart("profile_image", "");

                        }

                        Request request = new Request.Builder()
                                .post(requestBody.build())
                                .url(profileImageAPI)
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
            }.executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, params);

        } else {
            callback.onError(mcontext.getResources().getString(R.string.no_internet_connection_found_Please_check_your_internet_connection));
        }

    }


    /**
     * contact pro ..
     *
     * @param callback
     * @param params
     */
    public void contactProAPI(final getApiProcessCallback callback, String... params) {
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
                        OkHttpClient okHttpClient = new OkHttpClient.Builder().connectTimeout(6000, TimeUnit.MILLISECONDS).retryOnConnectionFailure(true).build();
                        RequestBody requestBody = new FormBody.Builder()
                                .add("user_id", params[0])
                                .add("user_type", params[1])
                                .add("prosUserId", params[2])
                                .add("pros_contact_service", params[3])
                                .add("con_description", params[4])
                                .add("additional_msg", params[5])
                                .build();

                        Logger.printMessage("user_id", ":-" + params[0]);
                        Logger.printMessage("user_type", ":-" + params[1]);
                        Logger.printMessage("prosUserId", ":-" + params[2]);
                        Logger.printMessage("pros_contact_service", ":-" + params[3]);
                        Logger.printMessage("con_description", ":-" + params[4]);
                        Logger.printMessage("additional_msg", ":-" + Integer.parseInt(params[5]));

                        Logger.printMessage("contactProAPI", contactProAPI);

                        Request request = new Request.Builder()
                                .url(contactProAPI)
                                .post(requestBody)
                                .build();
                        Response response = okHttpClient.newCall(request).execute();
                        String responseString = response.body().string();
                        Logger.printMessage("responseString", responseString);
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
            }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, params);
        } else
            callback.onError(mcontext.getResources().getString(R.string.no_internet_connection_found_Please_check_your_internet_connection));
    }


    /**
     * Post Project
     *
     * @param callback
     * @param params
     */
    public void seteditProjectAPI(final getApiProcessCallback callback, String... params) {
        if (NetworkUtil.getInstance().isNetworkAvailable(mcontext)) {


            new AsyncTask<String, Void, String>() {
                File upload_temp_PIC;
                String exception = "";

                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                    callback.onStart();
                }


                @Override
                protected String doInBackground(String... params) {
                    OkHttpClient client = new OkHttpClient.Builder().connectTimeout(6000, TimeUnit.MILLISECONDS).retryOnConnectionFailure(true).build();
                    //Logger.printMessage("@user_id", "user_id:" + ProApplication.getInstance().getUserId());
                    try {
                        if (!params[8].equals("")) {
                            try {
                                Bitmap bmp = ImageCompressor.with(mcontext).compressBitmap(params[9]);
                                Logger.printMessage("*****", "%% Bitmap size:: " + (bmp.getByteCount() / 1024) + " kb");
                                upload_temp_PIC = new File(mcontext.getCacheDir(), "" + System.currentTimeMillis() + ".png");
                                upload_temp_PIC.createNewFile();
                                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                                bmp.compress(Bitmap.CompressFormat.PNG, 0, bos);
                                byte[] bitmapdata = bos.toByteArray();

                                FileOutputStream fos = new FileOutputStream(upload_temp_PIC);
                                fos.write(bitmapdata);
                                fos.flush();
                                fos.close();

                            } catch (Exception e) {
                                e.printStackTrace();
                                return e.getMessage();
                            }

                        }
                        final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/*");
                        MultipartBody.Builder requestBody = new MultipartBody.Builder()
                                .setType(MultipartBody.FORM)
                                .addFormDataPart("user_id", params[0])
                                .addFormDataPart("project_id", params[1])
                                .addFormDataPart("project_zipcode", params[2])
                                .addFormDataPart("city", params[3])
                                .addFormDataPart("state", params[4])
                                .addFormDataPart("country", params[5])
                                .addFormDataPart("latitude", params[6])
                                .addFormDataPart("longitude", params[7])
                                .addFormDataPart("project_details", params[8]);

                        if (upload_temp_PIC != null) {
                            Logger.printMessage("*****", "" + upload_temp_PIC.getAbsolutePath());
                            requestBody.addFormDataPart("project_image", upload_temp_PIC.getName() + "", RequestBody.create(MEDIA_TYPE_PNG, upload_temp_PIC));
                        } else {
                            requestBody.addFormDataPart("project_image", "");

                        }

                        Request request = new Request.Builder()
                                .post(requestBody.build())
                                .url(editprojectAPI)
                                .build();

                        Response response = client.newCall(request).execute();
                        String responseString = response.body().string();

                        Logger.printMessage("home_edit_response", "" + responseString);
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
            }.executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, params);

        } else {
            callback.onError(mcontext.getResources().getString(R.string.no_internet_connection_found_Please_check_your_internet_connection));
        }
    }


    public void updateUserInformationFirstTimeAPI(final getApiProcessCallback callback, String... params) {
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
                        OkHttpClient okHttpClient = new OkHttpClient.Builder().connectTimeout(6000, TimeUnit.MILLISECONDS).retryOnConnectionFailure(true).build();

                        Logger.printMessage("user_id", ":-" + ProApplication.getInstance().getUserId());
                        Logger.printMessage("f_name", ":-" + params[0]);
                        Logger.printMessage("l_name", ":-" + params[1]);
                        Logger.printMessage("address", ":-" + params[2]);
                        Logger.printMessage("city", ":-" + params[3]);
                        Logger.printMessage("country", ":-" + params[4]);
                        Logger.printMessage("state", ":-" + params[5]);
                        Logger.printMessage("zipcode", ":-" + params[6]);
                        Logger.printMessage("phone", ":-" + params[7]);
                        Logger.printMessage("details", ":-" + params[8]);
                        Logger.printMessage("latitude", ":-" + params[9]);
                        Logger.printMessage("longitude", ":-" + params[10]);
                        Logger.printMessage("updateUserInformationAPI", updateUserInformationAPI);


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
                                .url(updateUserInformationAPI)
                                .post(requestBody)
                                .build();
                        Response response = okHttpClient.newCall(request).execute();
                        String responseString = response.body().string();
                        Logger.printMessage("UpdateUserInfo", responseString);
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
                        Logger.printMessage("successfullyUpdateMSQL", "YES");
                        try {
                            callback.onComplete(s);
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
            callback.onError(mcontext.getResources().getString(R.string.no_internet_connection_found_Please_check_your_internet_connection));
    }


    /**
     * get message  list
     *
     * @param callback
     */
    public void getUserMessageListAPI(final getApiProcessCallback callback, String... params) {
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

                        Logger.printMessage("user_id", ":-" + params[0]);
                        Logger.printMessage("project_id", ":-" + params[1]);
                        Logger.printMessage("list_search", ":-" + params[2]);
                        Logger.printMessage("pro_user_id", ":-" + params[3]);

                        String MessageListAPI = messageListAPI + "?user_id=" + params[0] + "&project_id=" + params[1]+"&list_search="+params[2]+"&pro_user_id="+params[3];

                        OkHttpClient client = new OkHttpClient.Builder().connectTimeout(6000, TimeUnit.MILLISECONDS).retryOnConnectionFailure(true).build();


                        Logger.printMessage("MessageListAPI", MessageListAPI);
                        Request request = new Request.Builder()
                                .get()
                                .url(MessageListAPI)
                                .build();

                        Response response = client.newCall(request).execute();
                        String responseString = response.body().string();
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
            }.executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, params);
        } else {
            callback.onError(mcontext.getResources().getString(R.string.no_internet_connection_found_Please_check_your_internet_connection));
        }
    }


    /**
     * delete message  list
     *
     * @param callback
     */
    public void deleteMessageListAPI(final getApiProcessCallback callback, String... params) {
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

                        Logger.printMessage("user_id", ":-" + params[0]);
                        Logger.printMessage("project_id", ":-" + params[1]);
                        Logger.printMessage("pro_user_id", ":-" + params[2]);

                        String MessageDeleteAPI = messageDeleteAPI + "?user_id=" + params[0] + "&project_id=" + params[1] + "&pro_user_id=" + params[2];

                        OkHttpClient client = new OkHttpClient.Builder().connectTimeout(6000, TimeUnit.MILLISECONDS).retryOnConnectionFailure(true).build();


                        Logger.printMessage("messageDeleteAPI", MessageDeleteAPI);
                        Request request = new Request.Builder()
                                .get()
                                .url(MessageDeleteAPI)
                                .build();

                        Response response = client.newCall(request).execute();
                        String responseString = response.body().string();
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
            }.executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, params);
        } else {
            callback.onError(mcontext.getResources().getString(R.string.no_internet_connection_found_Please_check_your_internet_connection));
        }
    }


    /**
     * send message
     *
     * @param callback
     */
    public void sendMessageAPI(final getApiProcessCallback callback, String... params) {

        if (NetworkUtil.getInstance().isNetworkAvailable(mcontext)) {
            new AsyncTask<String, Void, String>() {
                File upload_temp;
                String exception = "";

                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                    callback.onStart();
                }

                @Override
                protected String doInBackground(String... params) {
                    try {
                        OkHttpClient okHttpClient = new OkHttpClient.Builder().connectTimeout(6000, TimeUnit.MILLISECONDS).retryOnConnectionFailure(true).build();

                        Logger.printMessage("messagesendAPI", ":-" +messagesendAPI);
                        Logger.printMessage("user_id", ":-" + ProApplication.getInstance().getUserId());
                        Logger.printMessage("project_id", ":-" + params[0]);
                        Logger.printMessage("pro_id", ":-" + params[1]);
                        Logger.printMessage("message", ":-" + params[2]);
                        Logger.printMessage("file", ":-" + params[3]);

                        if (!params[3].equals("")) {
                            try {
                                Bitmap bmp = ImageCompressor.with(mcontext).compressBitmap(params[3]);
                                Logger.printMessage("*****", "%% Bitmap size:: " + (bmp.getByteCount() / 1024) + " kb");
                                upload_temp = new File(mcontext.getCacheDir(), "" + System.currentTimeMillis() + ".png");
                                upload_temp.createNewFile();
                                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                                bmp.compress(Bitmap.CompressFormat.PNG, 100, bos);
                                byte[] bitmapdata = bos.toByteArray();

                                FileOutputStream fos = new FileOutputStream(upload_temp);
                                fos.write(bitmapdata);
                                fos.flush();
                                fos.close();

                            } catch (Exception e) {
                                e.printStackTrace();
                                return e.getMessage();
                            }
                        }
                        final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/*");

                        MultipartBody.Builder requestBody = new MultipartBody.Builder()
                                .setType(MultipartBody.FORM)
                                .addFormDataPart("user_id", ProApplication.getInstance().getUserId())
                                .addFormDataPart("project_id", params[0])
                                .addFormDataPart("pro_id", params[1])
                                .addFormDataPart("message", params[2]);
                        if (upload_temp != null) {
                            Logger.printMessage("*****", "" + upload_temp.getAbsolutePath());
                            requestBody.addFormDataPart("attach_file", upload_temp.getName() + "", RequestBody.create(MEDIA_TYPE_PNG, upload_temp));
                        } else {
                            requestBody.addFormDataPart("attach_file", "");
                        }


                        Request request = new Request.Builder()
                                .url(messagesendAPI)
                                .post(requestBody.build())
                                .build();

                        Response response = okHttpClient.newCall(request).execute();

                        String responseString = response.body().string();
                        Logger.printMessage("responseInfo", responseString);
                        JSONObject respo = new JSONObject(responseString);
                        Logger.printMessage("response-->", String.valueOf(respo.getBoolean("response")));

                        if (respo.getBoolean("response")) {
                            Logger.printMessage("message-->", respo.getString("message"));
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
                protected void onPostExecute(String s) {
                    super.onPostExecute(s);
                    if (exception.equals("")) {
                        Logger.printMessage("successfullyUpdate", "YES");
                        try {
                            callback.onComplete(s);
                        } catch (Exception io) {
                            io.printStackTrace();
                            callback.onError(io.getMessage());
                        }
                    } else {
                        callback.onError(s);
                    }
                }
            }.executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, params);

        } else {
            callback.onError(mcontext.getResources().getString(R.string.no_internet_connection_found_Please_check_your_internet_connection));
        }

    }



    /**
     * For login of user
     *
     * @param callback
     * @param params
     */

    public void setUserDeviceAPI(final getApiProcessCallback callback, String... params) {
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
                                .add("user_id", params[0])
                                .add("device_token", params[1])
                                .build();

                        Logger.printMessage("user_id", "-->" + params[0]);
                        Logger.printMessage("device_token", "-->" + params[1]);
                        Logger.printMessage("usersDeviceUpdateAPI","-->"+ usersDeviceUpdateAPI);


                        OkHttpClient client = new OkHttpClient.Builder().connectTimeout(6000, TimeUnit.MILLISECONDS).retryOnConnectionFailure(true).build();

                        Request request = new Request.Builder()
                                .url(usersDeviceUpdateAPI)
                                .post(body)
                                .build();

                        Response response = client.newCall(request).execute();
                        String response_string = response.body().string();

                        Logger.printMessage("usersDeviceUpdateAPI", usersDeviceUpdateAPI + "\n" + params[0] + "/" + params[1] + "\n" + response_string);
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
            callback.onError(mcontext.getResources().getString(R.string.no_internet_connection_found_Please_check_your_internet_connection));
        }
    }



    /**
     * Interface used to get call back for getServiceList and getCategoryList
     */
    public interface onProCategoryListener {
        void onComplete(LinkedList<SetGetProCategoryData> listdata);

        void onError(String error);

        void onStartFetch();
    }

    /**
     * Interface used to get call back for search locationList
     */
    public interface onSearchPlacesNameCallback {
        void onComplete(ArrayList<String> listdata);

        void onError(String error);

        void onStartFetch();
    }

    /**
     * Interface used to get call back for search locationList
     */
    public interface onSearchZipCallback {
        void onComplete(List<SetGetAddressData> listdata);

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

    /**
     * Interface used to get call back for PostedProject list(MyProject)
     */
    public interface projectListCallback {
        void onStart();

        void onComplete(List projectList);

        void onError(String error);
    }


    /**
     * Interface used to get call back for faq
     */
    public interface faqCallback {
        void onStart();

        void onComplete(String s);

        void onError(String error);
    }
}
