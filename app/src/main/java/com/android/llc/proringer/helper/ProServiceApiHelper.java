package com.android.llc.proringer.helper;

import android.content.Context;
import android.os.AsyncTask;

import com.android.llc.proringer.pojo.ProCategoryData;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.LinkedList;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
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
    private onProCategoryListener listener;
    private final String categoryAPI = "http://esolz.co.in/lab6/proringer_latest/app_categorylist";
    private String serviceAPI = "http://esolz.co.in/lab6/proringer_latest/app_catrgoryservice_list";

    public static ProServiceApiHelper getInstance(Context context) {
        if (instance == null)
            instance = new ProServiceApiHelper();
        mcontext = context;
        return instance;
    }

    private ProServiceApiHelper() {
    }

    public void getCategoryList(final onProCategoryListener callback, String... params) {
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
    }

    public void getServiceList(final onProCategoryListener callback, final String... params) {
        new AsyncTask<String, Void, LinkedList>() {
            String exception = "";
            LinkedList<ProCategoryData> categoryList = null;
            String apiSer="";
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
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    public interface onProCategoryListener {
        void onComplete(LinkedList<ProCategoryData> listdata);

        void onError(String error);

        void onStartFetch();
    }


}
