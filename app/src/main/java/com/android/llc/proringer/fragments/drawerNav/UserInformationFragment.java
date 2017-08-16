package com.android.llc.proringer.fragments.drawerNav;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.android.llc.proringer.R;
import com.android.llc.proringer.activities.ContactUsActivity;
import com.android.llc.proringer.activities.LandScreenActivity;
import com.android.llc.proringer.activities.TakeGooglePlacePredictionActivity;
import com.android.llc.proringer.appconstant.ProApplication;
import com.android.llc.proringer.database.DatabaseHandler;
import com.android.llc.proringer.helper.CustomAlert;
import com.android.llc.proringer.helper.MyCustomAlertListener;
import com.android.llc.proringer.helper.MyLoader;
import com.android.llc.proringer.helper.ProServiceApiHelper;
import com.android.llc.proringer.utils.Logger;
import com.android.llc.proringer.viewsmod.edittext.ProLightEditText;
import com.android.llc.proringer.viewsmod.textview.ProRegularTextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by bodhidipta on 22/06/17.
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

public class UserInformationFragment extends Fragment implements MyCustomAlertListener{
    private ProLightEditText first_name, last_name, contact, zip_code, city, state;
    ProRegularTextView tv_search_by_location;
    RelativeLayout RLSearchByLocation;
    MyLoader myLoader = null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_user_information, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        first_name = (ProLightEditText) view.findViewById(R.id.first_name);
        last_name = (ProLightEditText) view.findViewById(R.id.last_name);
        contact = (ProLightEditText) view.findViewById(R.id.contact);

        myLoader=new MyLoader(getActivity());

        RLSearchByLocation = (RelativeLayout) view.findViewById(R.id.RLSearchByLocation);
        tv_search_by_location = (ProRegularTextView) view.findViewById(R.id.tv_search_by_location);

        RLSearchByLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(getActivity(), TakeGooglePlacePredictionActivity.class);
                startActivityForResult(i, 1);
            }
        });


        zip_code = (ProLightEditText) view.findViewById(R.id.zip_code);
//        zip_code.setEnabled(false);
//        zip_code.setClickable(false);

        city = (ProLightEditText) view.findViewById(R.id.city);
        city.setEnabled(false);
        city.setClickable(false);

        state = (ProLightEditText) view.findViewById(R.id.state);
        state.setEnabled(false);
        state.setClickable(false);


        plotUserInformation();

        view.findViewById(R.id.save_ifo).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        updateUserInformation();
                    }
                }
        );
    }

    private void updateUserInformation() {

        if (!zip_code.getText().toString().trim().equals("")) {
            ProServiceApiHelper.getInstance((LandScreenActivity) getActivity()).updateUserInformation(
                    new ProServiceApiHelper.getApiProcessCallback() {
                        @Override
                        public void onStart() {
                            myLoader.showLoader();
                        }

                        @Override
                        public void onComplete(String message) {
                            if (myLoader != null && myLoader.isMyLoaderShowing())
                                myLoader.dismissLoader();
                        }

                        @Override
                        public void onError(String error) {
                            if (myLoader != null && myLoader.isMyLoaderShowing())
                                myLoader.dismissLoader();

                            CustomAlert customAlert = new CustomAlert(getActivity(), "Error updating information", "" + error, UserInformationFragment.this);
                            customAlert.getListenerRetryCancelFromNormalAlert("retry","abort",1);
                        }
                    },
                    first_name.getText().toString().trim(),
                    last_name.getText().toString().trim(),
                    tv_search_by_location.getText().toString().trim(),
                    city.getText().toString().trim(),
                    "USA",
                    state.getText().toString().trim(),
                    zip_code.getText().toString().trim(),
                    contact.getText().toString().trim(),
                    "",
                    "",
                    "");
        } else {

            CustomAlert customAlert = new CustomAlert(getActivity(), "Updating Error", "Please Choose the correct address which will contains zip code", UserInformationFragment.this);
            customAlert.createNormalAlert("ok",1);
        }
    }


    private void plotUserInformation() {
        DatabaseHandler.getInstance((LandScreenActivity) getActivity()).getUserInfo(
                ProApplication.getInstance().getUserId(),
                new DatabaseHandler.onQueryCompleteListener() {
                    @Override
                    public void onSuccess(String... s) {

                        Logger.printMessage("database", "Yes");
                        Logger.printMessage("success", "s:--" + s);
                        /**
                         * User data already found in database
                         */

                        Logger.printMessage("@dashBoard", "on database data exists");
                        try {
                            JSONObject mainObject = new JSONObject(s[0]);
                            JSONArray info_arr = mainObject.getJSONArray("info_array");
                            JSONObject innerObj = info_arr.getJSONObject(0);
                            first_name.setText(innerObj.getString("f_name") + "");
                            last_name.setText(innerObj.getString("l_name") + "");
                            tv_search_by_location.setText(innerObj.getString("address") + "");

                            city.setText(innerObj.getString("city") + "");
                            state.setText(innerObj.getString("state") + "");
                            zip_code.setText(innerObj.getString("zipcode") + "");
                            contact.setText(innerObj.getString("ph_no") + "");

                        } catch (JSONException jse) {
                            jse.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(String s) {
                        /**
                         * No user data found on database or something went wrong
                         */
                        Logger.printMessage("@dashBoard", "on database data not exists:--" + s);

                    }
                });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {

                Bundle extras = data.getBundleExtra("data");
                if (extras != null) {

                    Logger.printMessage("selectedPlace", "--->" + extras.getString("selectedPlace"));
                    Logger.printMessage("zip", "--->" + extras.getString("zip"));
                    Logger.printMessage("city", "--->" + extras.getString("city"));
                    Logger.printMessage("state", "--->" + extras.getString("state"));

                    tv_search_by_location.setText(extras.getString("selectedPlace"));
                    zip_code.setText(extras.getString("zip"));
                    city.setText(extras.getString("city"));
                    state.setText(extras.getString("state"));
                }

            }
        }
    }

    @Override
    public void callbackForAlert(String result, int i) {
        if (result.equalsIgnoreCase("retry") && i==1){
            updateUserInformation();
        }
    }
}
