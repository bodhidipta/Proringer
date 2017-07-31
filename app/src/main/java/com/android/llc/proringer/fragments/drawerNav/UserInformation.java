package com.android.llc.proringer.fragments.drawerNav;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.PopupWindow;
import com.android.llc.proringer.R;
import com.android.llc.proringer.activities.LandScreenActivity;
import com.android.llc.proringer.adapter.PlaceCustomListAdapterDialog;
import com.android.llc.proringer.appconstant.ProApplication;
import com.android.llc.proringer.database.DatabaseHandler;
import com.android.llc.proringer.helper.ProServiceApiHelper;
import com.android.llc.proringer.utils.Logger;
import com.android.llc.proringer.viewsmod.edittext.ProLightEditText;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

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

public class UserInformation extends Fragment {
    private ProLightEditText first_name, last_name, contact, address, zip_code, city, state;
    private boolean pause_condition=false;
    private ProgressDialog pgDialog = null;
    private static PopupWindow popupWindow;
    private boolean checkToShowAfterSearach = false;
    private PlaceCustomListAdapterDialog placeCustomListAdapterDialog;


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

        address.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus)
                    address.setText("");
            }
        });


        zip_code = (ProLightEditText) view.findViewById(R.id.zip_code);
        zip_code.setEnabled(false);
        zip_code.setClickable(false);

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
        ProServiceApiHelper.getInstance((LandScreenActivity)getActivity()).updateUserInformation(
                new ProServiceApiHelper.getApiProcessCallback() {
                    @Override
                    public void onStart() {
                        pgDialog = new ProgressDialog((LandScreenActivity)getActivity());
                        pgDialog.setTitle("User Information");
                        pgDialog.setMessage("Updating user information. Please wait..");
                        pgDialog.setCancelable(false);
                        pgDialog.show();
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
                        new AlertDialog.Builder((LandScreenActivity)getActivity())
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
        DatabaseHandler.getInstance((LandScreenActivity)getActivity()).getUserInfo(
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
                            address.addTextChangedListener(new TextWatcher() {
                                @Override
                                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                                }

                                @Override
                                public void onTextChanged(CharSequence s, int start, int before, int count) {

                                }

                                @Override
                                public void afterTextChanged(Editable s) {

                                    if (!s.toString().trim().equals("")) {
                                        if(!pause_condition) {
                                            if (checkToShowAfterSearach == false) {
                                                createGooglePlaceList(address, s.toString().trim());
                                            } else {
                                                checkToShowAfterSearach = false;
                                            }
                                        }
                                    }
                                }
                            });
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
    public void createGooglePlaceList(final View view, String place) {
        ProServiceApiHelper.getInstance((LandScreenActivity)getActivity()).getSearchCountriesByPlacesFilter(new ProServiceApiHelper.onSearchPlacesNameCallback() {

            @Override
            public void onComplete(ArrayList<String> listdata) {
                showDialog(view,listdata);
            }

            @Override
            public void onError(String error) {

            }

            @Override
            public void onStartFetch() {

            }
        },place);
    }

    private void showDialog(View v, ArrayList<String> stringArrayList) {

        if (popupWindow == null) {
            popupWindow = new PopupWindow((LandScreenActivity)getActivity());
            // Removes default background.
            popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

            View dialogView = ((LandScreenActivity)getActivity()).getLayoutInflater().inflate(R.layout.dialog_show_place, null);

            RecyclerView rcv_ = (RecyclerView) dialogView.findViewById(R.id.rcv_);
            rcv_.setLayoutManager(new LinearLayoutManager((LandScreenActivity)getActivity()));

            placeCustomListAdapterDialog = new PlaceCustomListAdapterDialog((LandScreenActivity)getActivity(), stringArrayList, new onOptionSelected() {
                @Override
                public void onItemPassed(int position, ArrayList<String> stringArrayList) {
                        checkToShowAfterSearach = true;
                        address.setText(stringArrayList.get(position));

                        InputMethodManager imm = (InputMethodManager) ((LandScreenActivity)getActivity()).getSystemService(Activity.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(address.getWindowToken(), 0);

                        ProServiceApiHelper.getInstance((LandScreenActivity)getActivity()).getZipLocationStateAPI(new ProServiceApiHelper.getApiProcessCallback() {
                            @Override
                            public void onStart() {

                            }

                            @Override
                            public void onComplete(String message) {
                                try {
                                    JSONObject mainRes = new JSONObject(message);

                                    if (mainRes.getString("status").equalsIgnoreCase("OK") &&
                                            mainRes.has("results") &&
                                            mainRes.getJSONArray("results").length() > 0) {

                                        JSONArray results = mainRes.getJSONArray("results");

                                        JSONObject innerIncer = results.getJSONObject(0);

                                            /**
                                             * loop through address component
                                             * for country and state
                                             */
                                            if (innerIncer.has("address_components") &&
                                                    innerIncer.getJSONArray("address_components").length() > 0) {

                                                Logger.printMessage("address_components",""+innerIncer.getJSONArray("address_components"));

                                                JSONArray address_components = innerIncer.getJSONArray("address_components");

                                                for (int j = 0; j < address_components.length(); j++) {

                                                    if (address_components.getJSONObject(j).has("types") &&
                                                            address_components.getJSONObject(j).getJSONArray("types").length() > 0
                                                            ) {

                                                        JSONArray types = address_components.getJSONObject(j).getJSONArray("types");

                                                        for (int k = 0; k < types.length(); k++) {
                                                            if (types.getString(k).equals("administrative_area_level_2")) {
                                                                city.setText(address_components.getJSONObject(j).getString("short_name"));
                                                            }
                                                            if (types.getString(k).equals("administrative_area_level_1")) {
                                                                state.setText(address_components.getJSONObject(j).getString("short_name"));
                                                            }
                                                            if (types.getString(k).equals("postal_code")) {
                                                                zip_code.setText(address_components.getJSONObject(j).getString("short_name"));
                                                            }
                                                            else {
                                                                zip_code.setText("");
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                            @Override
                            public void onError(String error) {

                            }
                        },address.getText().toString().trim());
                    popupWindow.dismiss();
                }
            });

            rcv_.setAdapter(placeCustomListAdapterDialog);
            // some other visual settings
            popupWindow.setFocusable(false);
            popupWindow.setWidth(WindowManager.LayoutParams.MATCH_PARENT);
            popupWindow.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);

            // set the list view as pop up window content
            popupWindow.setContentView(dialogView);
            popupWindow.showAsDropDown(v, -5, 0);


        } else {
            popupWindow.showAsDropDown(v, -5, 0);
            if (placeCustomListAdapterDialog!=null) {
                placeCustomListAdapterDialog.setRefresh(stringArrayList);
            }
        }
    }

    public interface onOptionSelected {
        void onItemPassed(int position,ArrayList<String> stringArrayList);
    }

    @Override
    public void onResume() {
        if (popupWindow!=null){
            pause_condition=false;
        }
        super.onResume();
    }

    public void closeMyPopUpWindow(){
        if (popupWindow!=null){
            popupWindow.dismiss();
            popupWindow=null;
            pause_condition=true;
        }
    }
}
