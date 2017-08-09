package com.android.llc.proringer.fragments.bottomNav;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.android.llc.proringer.R;
import com.android.llc.proringer.activities.LandScreenActivity;
import com.android.llc.proringer.helper.ProServiceApiHelper;
import com.android.llc.proringer.viewsmod.BottomNav;
import com.android.llc.proringer.viewsmod.NavigationHandler;
import com.android.llc.proringer.viewsmod.textview.ProLightTextView;
import com.android.llc.proringer.viewsmod.textview.ProRegularTextView;
import com.bumptech.glide.Glide;
import com.koushiklibrary.JSONPerser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by bodhidipta on 10/06/17.
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

public class DashBoardFragment extends Fragment {
    ImageView profile_pic;
    ProRegularTextView tv_name,tv_active_projects,tv_favorite_pros;
    ProLightTextView tv_address;
    ProgressDialog pgDialog;

    LinearLayout LLNetworkDisconnection;
    NestedScrollView nested_scroll_main;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.content_land_screen, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        LLNetworkDisconnection= (LinearLayout) view.findViewById(R.id.LLNetworkDisconnection);
        nested_scroll_main= (NestedScrollView) view.findViewById(R.id.nested_scroll_main);

        profile_pic = (ImageView) view.findViewById(R.id.profile_pic);
        tv_name = (ProRegularTextView) view.findViewById(R.id.tv_name);
        tv_active_projects = (ProRegularTextView) view.findViewById(R.id.tv_active_projects);
        tv_favorite_pros = (ProRegularTextView) view.findViewById(R.id.tv_favorite_pros);
        tv_address = (ProLightTextView) view.findViewById(R.id.tv_address);

        view.findViewById(R.id.post_project).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((LandScreenActivity) getActivity()).performPostProject();
            }
        });

        plotUserInformation();

        view.findViewById(R.id.userInformation).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((LandScreenActivity)getActivity()).bottomNavInstance.highLightSelected(BottomNav.CREATE_PROJECT);  // Well CREATE_PROJECT will reset bottom nav bar to nothing selected !!!
                NavigationHandler.getInstance().highlightTag(NavigationHandler.USER_INFORMATION);

            }
        });
        view.findViewById(R.id.login_settings).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((LandScreenActivity)getActivity()).bottomNavInstance.highLightSelected(BottomNav.CREATE_PROJECT);
                NavigationHandler.getInstance().highlightTag(NavigationHandler.LOGIN_SETTINGS);
            }
        });
        view.findViewById(R.id.notification).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((LandScreenActivity)getActivity()).bottomNavInstance.highLightSelected(BottomNav.CREATE_PROJECT);
                NavigationHandler.getInstance().highlightTag(NavigationHandler.NOTIFICATION);
            }
        });
        view.findViewById(R.id.home_schedule).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((LandScreenActivity)getActivity()).bottomNavInstance.highLightSelected(BottomNav.CREATE_PROJECT);
                NavigationHandler.getInstance().highlightTag(NavigationHandler.HOME_SCHEDUL);
            }
        });
        view.findViewById(R.id.invite_a_friend).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((LandScreenActivity)getActivity()).bottomNavInstance.highLightSelected(BottomNav.CREATE_PROJECT);
                NavigationHandler.getInstance().highlightTag(NavigationHandler.INVITE_FRIEND);
            }
        });
    }

    private void plotUserInformation() {

        nested_scroll_main.setVisibility(View.VISIBLE);
        LLNetworkDisconnection.setVisibility(View.GONE);

        ProServiceApiHelper.getInstance((LandScreenActivity)getActivity()).getDashBoardDetails(new ProServiceApiHelper.getApiProcessCallback() {
            @Override
            public void onStart() {

                pgDialog = new ProgressDialog((LandScreenActivity)getActivity());
                pgDialog.setTitle("My Dashboard");
                pgDialog.setMessage("My Dashboard page loading.Please wait...");
                pgDialog.setCancelable(false);
                pgDialog.show();
            }

            @Override
            public void onComplete(String message) {
                try {
                    JSONObject jsonObject=new JSONObject(message);
                    JSONArray jsonInfoArray=jsonObject.getJSONArray("info_array");

                    if (!jsonInfoArray.getJSONObject(0).getString("profile_img").equals(""))
                        Glide.with((LandScreenActivity)getActivity()).load(jsonInfoArray.getJSONObject(0).getString("profile_img")).centerCrop().into(profile_pic);

                    tv_name.setText(jsonInfoArray.getJSONObject(0).getString("user_name"));

                    tv_address.setText(jsonInfoArray.getJSONObject(0).getString("city")
                            +","+jsonInfoArray.getJSONObject(0).getString("state_code")
                            +" "+jsonInfoArray.getJSONObject(0).getString("zipcode"));

                    tv_active_projects.setText(jsonInfoArray.getJSONObject(0).getString("total_active_project"));
                    tv_favorite_pros.setText(jsonInfoArray.getJSONObject(0).getString("total_fav_pro"));

                    getUpdateUserData();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(String error) {
                if (pgDialog != null && pgDialog.isShowing())
                    pgDialog.dismiss();



                if(error.equalsIgnoreCase("No internet connection found. Please check your internet connection.")){
                    nested_scroll_main.setVisibility(View.GONE);
                    LLNetworkDisconnection.setVisibility(View.VISIBLE);
                }



                new AlertDialog.Builder(getActivity())
                        .setTitle("Load Error")
                        .setMessage("" + error)
                        .setPositiveButton("retry", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                plotUserInformation();

                            }
                        })
                        .setNegativeButton("abort", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).show();
            }
        });
    }

    private void getUpdateUserData() {

        ProServiceApiHelper.getInstance(getActivity()).getUserInformation(new ProServiceApiHelper.getApiProcessCallback() {
            @Override
            public void onStart() {
            }

            @Override
            public void onComplete(String message) {
                if (pgDialog != null && pgDialog.isShowing())
                    pgDialog.dismiss();
            }

            @Override
            public void onError(String error) {
                if (pgDialog != null && pgDialog.isShowing())
                    pgDialog.dismiss();
            }
        });
    }

}
