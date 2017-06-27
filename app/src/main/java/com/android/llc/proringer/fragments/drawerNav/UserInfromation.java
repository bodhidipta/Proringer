package com.android.llc.proringer.fragments.drawerNav;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.llc.proringer.R;
import com.android.llc.proringer.appconstant.ProApplication;
import com.android.llc.proringer.database.DatabaseHandler;
import com.android.llc.proringer.helper.ProServiceApiHelper;
import com.android.llc.proringer.utils.Logger;
import com.android.llc.proringer.viewsmod.edittext.ProLightEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

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

public class UserInfromation extends Fragment {
    private ProLightEditText first_name, last_name, contact, address, zip_code, city, state;
    private ProgressDialog pgDia = null;

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
        address = (ProLightEditText) view.findViewById(R.id.address);
        zip_code = (ProLightEditText) view.findViewById(R.id.zip_code);
        city = (ProLightEditText) view.findViewById(R.id.city);
        state = (ProLightEditText) view.findViewById(R.id.state);
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
        ProServiceApiHelper.getInstance(getActivity()).updateUserInformation(
                new ProServiceApiHelper.getApiProcessCallback() {
                    @Override
                    public void onStart() {
                        pgDia = new ProgressDialog(getActivity());
                        pgDia.setTitle("User Information");
                        pgDia.setMessage("Updating user information. Please wait..");
                        pgDia.setCancelable(false);
                        pgDia.show();
                    }

                    @Override
                    public void onComplete(String message) {
                        if (pgDia != null && pgDia.isShowing())
                            pgDia.dismiss();
                    }

                    @Override
                    public void onError(String error) {
                        if (pgDia != null && pgDia.isShowing())
                            pgDia.dismiss();
                        new AlertDialog.Builder(getActivity())
                                .setTitle("Error updating information")
                                .setMessage("" + error)
                                .setPositiveButton("retry", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                        updateUserInformation();
                                    }
                                })
                                .setNegativeButton("abort", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                })
                                .show();

                    }
                },
                first_name.getText().toString().trim(),
                last_name.getText().toString().trim(),
                address.getText().toString().trim(),
                city.getText().toString().trim(),
                "USA",
                state.getText().toString().trim(),
                zip_code.getText().toString().trim(),
                contact.getText().toString().trim(),
                "",
                "",
                "");
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
                            first_name.setText(innerObj.getString("f_name") + "");
                            last_name.setText(innerObj.getString("l_name") + "");
                            address.setText(innerObj.getString("address") + "");
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
                        Logger.printMessage("@dashBoard", "on database data not exists");

                    }
                });
    }
}
