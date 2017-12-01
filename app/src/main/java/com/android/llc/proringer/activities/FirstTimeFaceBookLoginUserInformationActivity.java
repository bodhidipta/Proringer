package com.android.llc.proringer.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupWindow;

import com.android.llc.proringer.R;
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
 * Created by su on 11/16/17.
 */

public class FirstTimeFaceBookLoginUserInformationActivity extends AppCompatActivity implements MyCustomAlertListener {
    private ProLightEditText first_name, last_name, contact, zip_code, city, state;
    ProRegularTextView tv_search_by_location;
    //    ProLightEditText address;
    PopupWindow popupWindow;
    MyLoader myLoader = null;
    ImageView Erase;
    boolean checkToShowAfterSearach = false;
    PlaceCustomListAdapterDialog placeCustomListAdapterDialog;
    int textLength = 0;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.first_time_facebook_user_login_information);

        first_name = (ProLightEditText) findViewById(R.id.first_name);
        last_name = (ProLightEditText) findViewById(R.id.last_name);
        contact = (ProLightEditText) findViewById(R.id.contact);

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

        myLoader = new MyLoader(FirstTimeFaceBookLoginUserInformationActivity.this);
//        address= (ProLightEditText) view.findViewById(R.id.address);

        tv_search_by_location = (ProRegularTextView) findViewById(R.id.tv_search_by_location);
        tv_search_by_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(FirstTimeFaceBookLoginUserInformationActivity.this, LocationFinder.class);
                startActivityForResult(i, 1);
            }
        });


        zip_code = (ProLightEditText) findViewById(R.id.zip_code);

        city = (ProLightEditText) findViewById(R.id.city);
        city.setEnabled(false);
        city.setClickable(false);

        state = (ProLightEditText) findViewById(R.id.state);
        state.setEnabled(false);
        state.setClickable(false);

        String firstTimeFbDetails = ProApplication.getInstance().getFbFirstTimeJson();

        Logger.printMessage("@firstTimeFBLogin", firstTimeFbDetails);
        try {
            JSONObject mainObject = new JSONObject(firstTimeFbDetails);
            JSONObject info_arr = mainObject.getJSONObject("info_array");
            first_name.setText(info_arr.getString("first_name") + "");
            last_name.setText(info_arr.getString("last_name") + "");
//                            address.setText(innerObj.getString("address") + "");
            zip_code.setText(info_arr.getString("zipcode") + "");


        } catch (JSONException jse) {
            jse.printStackTrace();
        }

        findViewById(R.id.save_ifo).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        validationCheckAndSubmit();
                    }
                }
        );


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
    }

    private void updateUserInformation() {

        if (!zip_code.getText().toString().trim().equals("")) {
            ProServiceApiHelper.getInstance(FirstTimeFaceBookLoginUserInformationActivity.this).updateUserInformationFirstTime(
                    new ProServiceApiHelper.getApiProcessCallback() {
                        @Override
                        public void onStart() {
                            myLoader.showLoader();
                        }

                        @Override
                        public void onComplete(String message) {
                            if (myLoader != null && myLoader.isMyLoaderShowing())
                                myLoader.dismissLoader();

                            ProServiceApiHelper.getInstance(FirstTimeFaceBookLoginUserInformationActivity.this).getUserInformation(new ProServiceApiHelper.getApiProcessCallback() {
                                @Override
                                public void onStart() {

                                }

                                @Override
                                public void onComplete(String message) {
                                    ProApplication.getInstance().setFBUserStatus(0);
                                    Intent intent = new Intent(FirstTimeFaceBookLoginUserInformationActivity.this, LandScreenActivity.class);
                                    ProApplication.getInstance().go_to = "dashboard";
                                    startActivity(intent);
                                    finish();
                                }

                                @Override
                                public void onError(String error) {

                                }
                            });
                        }

                        @Override
                        public void onError(String error) {
                            if (myLoader != null && myLoader.isMyLoaderShowing())
                                myLoader.dismissLoader();

                            CustomAlert customAlert = new CustomAlert(FirstTimeFaceBookLoginUserInformationActivity.this, "Error updating information", "" + error, FirstTimeFaceBookLoginUserInformationActivity.this);
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
        } else {
            CustomAlert customAlert = new CustomAlert(FirstTimeFaceBookLoginUserInformationActivity.this, "Updating Error", "Please Choose the correct address which will contains zip code", FirstTimeFaceBookLoginUserInformationActivity.this);
            customAlert.createNormalAlert("ok", 1);
        }
    }

    private void validationCheckAndSubmit() {

        first_name.setError(null);//removes error
        first_name.clearFocus();

        last_name.setError(null);//removes error
        last_name.clearFocus();

        contact.setError(null);//removes error
        contact.clearFocus();

        tv_search_by_location.setError(null);//removes error
        tv_search_by_location.clearFocus();

        city.setError(null);//removes error
        city.clearFocus();

        state.setError(null);//removes error
        state.clearFocus();

        zip_code.setError(null);//removes error
        zip_code.clearFocus();

        if (first_name.getText().toString().equals("")) {
            first_name.requestFocus();
            first_name.setError("Please Enter First Name");
        } else {
            if (last_name.getText().toString().equals("")) {
                last_name.requestFocus();
                last_name.setError("Please Enter Last Name");
            } else {
                if (contact.getText().toString().equals("")) {
                    contact.requestFocus();
                    contact.setError("Please Enter Phone Number");
                } else {
                    if (tv_search_by_location.getText().toString().equals("")) {
                        tv_search_by_location.requestFocus();
                        tv_search_by_location.setError("Please Enter Address");
                    } else {
                        if (city.getText().toString().equals("")) {
                            city.requestFocus();
                            city.setError("Please Enter City");
                        } else {
                            if (state.getText().toString().equals("")) {
                                state.requestFocus();
                                state.setError("Please Enter State");
                            } else {
                                if (zip_code.getText().toString().equals("")) {
                                    zip_code.requestFocus();
                                    zip_code.setError("Please Enter Zip Code");
                                } else {
                                    updateUserInformation();
                                }
                            }
                        }
                    }
                }
            }
        }


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
        ProServiceApiHelper.getInstance(FirstTimeFaceBookLoginUserInformationActivity.this).getLocationListUsingGoogleAPI(place, new ProServiceApiHelper.getApiProcessCallback() {
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


    public interface onOptionSelected {
        void onItemPassed(int position, JSONObject value);
    }
}
