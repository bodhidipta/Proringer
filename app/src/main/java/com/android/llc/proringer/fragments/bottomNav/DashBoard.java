package com.android.llc.proringer.fragments.bottomNav;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.android.llc.proringer.R;
import com.android.llc.proringer.activities.LandScreenActivity;
import com.android.llc.proringer.appconstant.ProApplication;
import com.android.llc.proringer.database.DatabaseHandler;
import com.android.llc.proringer.helper.ProServiceApiHelper;
import com.android.llc.proringer.utils.Logger;
import com.android.llc.proringer.viewsmod.BottomNav;
import com.android.llc.proringer.viewsmod.NavigationHandler;
import com.android.llc.proringer.viewsmod.textview.ProLightTextView;
import com.android.llc.proringer.viewsmod.textview.ProRegularTextView;

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

public class DashBoard extends Fragment {
    ImageView profile_pic;
    ProRegularTextView name;
    ProLightTextView address;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.content_land_screen, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        profile_pic = (ImageView) view.findViewById(R.id.profile_pic);
        name = (ProRegularTextView) view.findViewById(R.id.name);
        address = (ProLightTextView) view.findViewById(R.id.address);

        view.findViewById(R.id.post_project).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((LandScreenActivity) getActivity()).performPostProject();
            }
        });

        getUpdateUserData();
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


    private void getUpdateUserData() {
        ProServiceApiHelper.getInstance(getActivity()).getUserInformation(new ProServiceApiHelper.getApiProcessCallback() {
            @Override
            public void onStart() {

            }

            @Override
            public void onComplete(String message) {

            }

            @Override
            public void onError(String error) {

            }
        });
    }


    private void plotUserInformation() {
        DatabaseHandler.getInstance(getActivity()).getUserInfo(
                ProApplication.getInstance().getUserId(),
                new DatabaseHandler.onQueryCompleteListener() {
                    @Override
                    public void onSuccess(String... s) {
                        /**
                         * User data already found in database
                         */

                        Logger.printMessage("@dashBoard", "on database data exists");
                        try {
                            JSONObject mainObject = new JSONObject(s[0]);
                            JSONArray info_arr = mainObject.getJSONArray("info_array");
                            JSONObject innerObj = info_arr.getJSONObject(0);
                            name.setText(innerObj.getString("f_name") + " ");
                           // name.append(innerObj.getString("l_name") + "");

                            address.setText(innerObj.getString("city") + "," + innerObj.getString("state") + "," + innerObj.getString("zipcode"));


                        } catch (JSONException jse) {
                            jse.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(String s) {
                        /**
                         * No user data found on database or something went wrong
                         */
                        Logger.printMessage("@dashBoard", "on database data not exists");

                    }
                });
    }

}
