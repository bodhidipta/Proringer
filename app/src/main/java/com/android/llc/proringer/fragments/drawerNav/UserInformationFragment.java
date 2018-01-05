package com.android.llc.proringer.fragments.drawerNav;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.android.llc.proringer.R;
import com.android.llc.proringer.activities.LandScreenActivity;
import com.android.llc.proringer.activities.LocationFinder;
import com.android.llc.proringer.adapter.PlaceCustomListAdapterDialog;
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

public class UserInformationFragment extends Fragment implements MyCustomAlertListener {
    private ProLightEditText first_name, last_name, contact, zip_code, city, state;
    ProRegularTextView tv_search_by_location;
    //    ProLightEditText address;
    PopupWindow popupWindow;
    MyLoader myLoader = null;
    ImageView Erase;
    boolean checkToShowAfterSearach = false;
    PlaceCustomListAdapterDialog placeCustomListAdapterDialog;
    int textLength = 0;

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

        contact.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String text = contact.getText().toString();
                textLength = contact.getText().length();

                if (text.endsWith("-") || text.endsWith(" ") || text.endsWith(" "))
                    return;

                if (textLength == 1) {
                    if (!text.contains("(")) {
                        contact.setText(new StringBuilder(text).insert(text.length() - 1, "(").toString());
                        contact.setSelection(contact.getText().length());
                    }

                } else if (textLength == 5) {

                    if (!text.contains(")")) {
                        contact.setText(new StringBuilder(text).insert(text.length() - 1, ")").toString());
                        contact.setSelection(contact.getText().length());
                    }

                } else if (textLength == 6) {
                    contact.setText(new StringBuilder(text).insert(text.length() - 1, " ").toString());
                    contact.setSelection(contact.getText().length());

                } else if (textLength == 10) {
                    if (!text.contains("-")) {
                        contact.setText(new StringBuilder(text).insert(text.length() - 1, "-").toString());
                        contact.setSelection(contact.getText().length());
                    }
                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void afterTextChanged(Editable s) {
            }
        });

        myLoader = new MyLoader(getActivity());
//        address= (ProLightEditText) view.findViewById(R.id.address);

        tv_search_by_location = (ProRegularTextView) view.findViewById(R.id.tv_search_by_location);
        tv_search_by_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(getActivity(), LocationFinder.class);
                startActivityForResult(i, 1);
            }
        });

        //  Erase= (ImageView) view.findViewById(R.id.Erase);

//        Erase.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                address.setText("");
//            }
//        });

//        address.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//
//                if (!s.toString().trim().equals("")) {
//                    if (checkToShowAfterSearach == false) {
//                        createGooglePlaceList(address, s.toString());
//                    } else {
//                        checkToShowAfterSearach = false;
//                    }
//                    Erase.setVisibility(View.VISIBLE);
//                }
//                else {
//                    Erase.setVisibility(View.GONE);
//                }
//            }
//        });


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

        if (first_name.getText().toString().trim().equals(""))
        {
            first_name.setError("Enter First Name");
            first_name.setFocusable(true);
        }
        else {
            first_name.setError(null);//removes error
            first_name.clearFocus();
            if (last_name.getText().toString().trim().equals(""))
            {
                last_name.setError("Enter Last Name");
                last_name.setFocusable(true);
            }
            else {
                last_name.setError(null);//removes error
                last_name.clearFocus();
                if (contact.getText().toString().trim().equals(""))
                {
                    contact.setError("Enter Phone Number");
                    contact.setFocusable(true);
                }
                else {

                    contact.setError(null);//removes error
                    contact.clearFocus();

                    if (contact.getText().toString().trim().length()<14)
                    {
                        contact.setError("Enter Correct Phone Number");
                        contact.setFocusable(true);
                    }
                    else {

                        contact.setError(null);//removes error
                        contact.clearFocus();

                        if (tv_search_by_location.getText().toString().trim().equals(""))
                        {
                            tv_search_by_location.setError("Choose Address");
                            tv_search_by_location.setFocusable(true);
                        }
                        else {
                            if (zip_code.getText().toString().trim().equals("")||city.getText().toString().trim().equals("")||state.getText().toString().trim().equals("")) {
                                CustomAlert customAlert = new CustomAlert(getActivity(), "Updating Error", "Please Choose the correct address which will contains zip code , city, state", UserInformationFragment.this);
                                customAlert.createNormalAlert("ok", 1);
                            } else {
                                ProServiceApiHelper.getInstance((LandScreenActivity) getActivity()).updateUserInformationAPI(
                                        new ProServiceApiHelper.getApiProcessCallback() {
                                            @Override
                                            public void onStart() {
                                                myLoader.showLoader();
                                            }

                                            @Override
                                            public void onComplete(String message) {
                                                if (myLoader != null && myLoader.isMyLoaderShowing())
                                                    myLoader.dismissLoader();
                                                Toast.makeText(getActivity(), "User information updated successfully", Toast.LENGTH_SHORT).show();
                                            }

                                            @Override
                                            public void onError(String error) {
                                                if (myLoader != null && myLoader.isMyLoaderShowing())
                                                    myLoader.dismissLoader();

                                                CustomAlert customAlert = new CustomAlert(getActivity(), "Error updating information", "" + error, UserInformationFragment.this);
                                                customAlert.getListenerRetryCancelFromNormalAlert("retry", "abort", 1);
                                            }
                                        },
                                        first_name.getText().toString().trim(),
                                        last_name.getText().toString().trim(),
                                        tv_search_by_location.getText().toString().trim(),
//                    address.getText().toString().trim(),
                                        city.getText().toString().trim(),
                                        "USA",
                                        state.getText().toString().trim(),
                                        zip_code.getText().toString().trim(),
                                        contact.getText().toString().trim(),
                                        "",
                                        "",
                                        "");
                            }
                        }
                    }
                }
            }
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
//                            address.setText(innerObj.getString("address") + "");
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

                    if (!extras.getString("selectedPlace").equals("")) {
                        tv_search_by_location.setText(extras.getString("selectedPlace").substring(0, extras.getString("selectedPlace").indexOf(",")));
//                        address.setText(extras.getString("selectedPlace").substring(0, extras.getString("selectedPlace").indexOf(",")));
                    }
                    zip_code.setText(extras.getString("zip"));
                    city.setText(extras.getString("city"));
                    state.setText(extras.getString("state"));
                }

            }
        }
    }

    @Override
    public void callbackForAlert(String result, int i) {
        if (result.equalsIgnoreCase("retry") && i == 1) {
            updateUserInformation();
        }
    }

    public void createGooglePlaceList(final View view, String place) {
        ProServiceApiHelper.getInstance(getActivity()).getLocationListUsingGoogleAPI(place, new ProServiceApiHelper.getApiProcessCallback() {
            @Override
            public void onStart() {
            }

            @Override
            public void onComplete(String message) {
                try {
                    JSONObject jsonObject = new JSONObject(message);
                    JSONArray jsonArray = jsonObject.getJSONArray("predictions");
                    //showDialog(view, jsonArray);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Logger.printMessage("message", "" + message);
            }

            @Override
            public void onError(String error) {
            }
        });
    }

//    private void showDialog(View v, JSONArray PredictionsJsonArray) {
//
//        if (popupWindow == null) {
//            popupWindow = new PopupWindow(getActivity());
//            // Closes the popup window when touch outside.
//            popupWindow.setOutsideTouchable(true);
//            popupWindow.setFocusable(false);
//            // Removes default background.
//            popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//
//            View dailogView = getActivity().getLayoutInflater().inflate(R.layout.dialog_show_place, null);
//
//            RecyclerView rcv_ = (RecyclerView) dailogView.findViewById(R.id.rcv_);
//            rcv_.setLayoutManager(new LinearLayoutManager(getActivity()));
//
//            placeCustomListAdapterDialog = new PlaceCustomListAdapterDialog(getActivity(), PredictionsJsonArray, new onOptionSelected() {
//                @Override
//                public void onItemPassed(int position, JSONObject value) {
//                    try {
//                        checkToShowAfterSearach = true;
////                        address.setText(value.getString("description"));
//
//                        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
////                        imm.hideSoftInputFromWindow(address.getWindowToken(), 0);
//
//                        ProServiceApiHelper.getInstance(getActivity()).getZipLocationStateAPI(new ProServiceApiHelper.getApiProcessCallback() {
//                            @Override
//                            public void onStart() {
//
//                            }
//
//                            @Override
//                            public void onComplete(String message) {
//                                try {
//                                    JSONObject mainRes = new JSONObject(message);
//
//                                    if (mainRes.getString("status").equalsIgnoreCase("OK") &&
//                                            mainRes.has("results") &&
//                                            mainRes.getJSONArray("results").length() > 0) {
//
//                                        JSONArray results = mainRes.getJSONArray("results");
//
//                                        JSONObject innerIncer = results.getJSONObject(0);
//
//                                        /**
//                                         * loop through address component
//                                         * for country and state
//                                         */
//                                        if (innerIncer.has("address_components") &&
//                                                innerIncer.getJSONArray("address_components").length() > 0) {
//
//                                            Logger.printMessage("address_components",""+innerIncer.getJSONArray("address_components"));
//
//                                            JSONArray address_components = innerIncer.getJSONArray("address_components");
//
//                                            for (int j = 0; j < address_components.length(); j++) {
//
//                                                if (address_components.getJSONObject(j).has("types") &&
//                                                        address_components.getJSONObject(j).getJSONArray("types").length() > 0
//                                                        ) {
//
//                                                    JSONArray types = address_components.getJSONObject(j).getJSONArray("types");
//
//                                                    for (int k = 0; k < types.length(); k++) {
//                                                        if (types.getString(k).equals("administrative_area_level_2")) {
//                                                            city.setText(address_components.getJSONObject(j).getString("short_name"));
//                                                        }
//                                                        if (types.getString(k).equals("administrative_area_level_1")) {
//                                                            state.setText(address_components.getJSONObject(j).getString("short_name"));
//                                                        }
//                                                        if (types.getString(k).equals("postal_code")) {
//                                                            zip_code.setText(address_components.getJSONObject(j).getString("short_name"));
//                                                        }
//                                                        else {
//                                                            zip_code.setText("");
//                                                        }
//                                                    }
//                                                }
//                                            }
//                                        }
//                                    }
//
//                                } catch (JSONException e) {
//                                    e.printStackTrace();
//                                }
//                            }
//                            @Override
//                            public void onError(String error) {
//
//                            }
//                        },address.getText().toString().trim()
//                        );
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                    popupWindow.dismiss();
//                }
//            });
//
//            rcv_.setAdapter(placeCustomListAdapterDialog);
//            // some other visual settings
//            popupWindow.setFocusable(false);
//            popupWindow.setWidth(WindowManager.LayoutParams.MATCH_PARENT);
//            popupWindow.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
//
//            // set the list view as pop up window content
//            popupWindow.setContentView(dailogView);
//            popupWindow.showAsDropDown(v, -5, 0);
//
//
//        } else {
//            popupWindow.showAsDropDown(v, -5, 0);
//            placeCustomListAdapterDialog.setRefresh(PredictionsJsonArray);
//        }
//    }

    public interface onOptionSelected {
        void onItemPassed(int position, JSONObject value);
    }
}
