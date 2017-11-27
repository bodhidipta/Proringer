package com.android.llc.proringer.fragments.drawerNav;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.android.llc.proringer.R;
import com.android.llc.proringer.activities.LandScreenActivity;
import com.android.llc.proringer.adapter.ProCategoryListAdapter;
import com.android.llc.proringer.adapter.SearchProListAdapter;
import com.android.llc.proringer.appconstant.ProApplication;
import com.android.llc.proringer.database.DatabaseHandler;
import com.android.llc.proringer.helper.CustomAlert;
import com.android.llc.proringer.helper.MyCustomAlertListener;
import com.android.llc.proringer.helper.MyLoader;
import com.android.llc.proringer.helper.ProServiceApiHelper;
import com.android.llc.proringer.pojo.ProCategoryData;
import com.android.llc.proringer.utils.Logger;
import com.android.llc.proringer.viewsmod.edittext.ProRegularEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedList;

/**
 * Created by bodhidipta on 21/06/17.
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

public class SearchLocalProFragment extends Fragment implements MyCustomAlertListener {
    private RecyclerView pros_list;
    String category_search = "";
    MyLoader myLoader = null;
    ProRegularEditText edt_search;
    SearchProListAdapter searchProListAdapter;
    LinearLayout LLMain, LLNetworkDisconnection;
    TextWatcher mySearchTextWatcher;
    PopupWindow popupWindow;
    ImageView img_clear;
    ProCategoryListAdapter proCategoryListAdapter;
    private InputMethodManager keyboard;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_search_pros, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        pros_list = (RecyclerView) view.findViewById(R.id.pros_list);
        pros_list.setLayoutManager(new LinearLayoutManager((LandScreenActivity) getActivity()));

        LLMain = (LinearLayout) view.findViewById(R.id.LLMain);
        LLNetworkDisconnection = (LinearLayout) view.findViewById(R.id.LLNetworkDisconnection);
        edt_search = (ProRegularEditText) view.findViewById(R.id.edt_search);
        myLoader = new MyLoader(getActivity());
        img_clear = (ImageView) view.findViewById(R.id.img_clear);
        img_clear.setVisibility(View.GONE);

        keyboard = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);

        img_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edt_search.setText("");
                category_search = "";
                loadList();
                closeKeypad();
            }
        });


        edt_search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                    Logger.printMessage("search_category", edt_search.getText().toString());
                    closeKeypad();
                    loadList();
                } else if ((event != null && (actionId == KeyEvent.KEYCODE_DEL))) {
                    if (edt_search.getText().toString().equals("")) {
                        Logger.printMessage("search_category", edt_search.getText().toString());
                        closeKeypad();
                        loadList();
                    }
                }
                return false;
            }
        });


//        edt_search.setOnKeyListener(new View.OnKeyListener() {
//            public boolean onKey(View v, int keyCode, KeyEvent event) {
//                if (
//                        (event.getAction() == KeyEvent.ACTION_DOWN)
//                                || (keyCode == KeyEvent.KEYCODE_ENTER)) {
//                    Logger.printMessage("search_category", edt_search.getText().toString());
//                    closeKeypad();
//                    loadList();
//                    return true;
//                }
//                return false;
//            }
//        });

        mySearchTextWatcher = new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // your logic here
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // your logic here
                category_search = s.toString().trim();
                if (category_search.length() > 0) {
                    img_clear.setVisibility(View.VISIBLE);
                } else {
                    img_clear.setVisibility(View.GONE);
                }
//                if (category_search.length() == 0) {
//                    closeKeypad();
//                    loadList();
//                }
                //loadCategoryList();
            }
        };

        edt_search.setText("");
        category_search = "";
        edt_search.addTextChangedListener(mySearchTextWatcher);

        if (((LandScreenActivity) getActivity()).local_pros_search_zip.trim().equals("")) {
            plotUserInformation();
        } else {
            loadList();
        }
    }

    @Override
    public void callbackForAlert(String result, int i) {
        if (result.equalsIgnoreCase("retry") && i == 1) {
            plotUserInformation();
        } else if (result.equalsIgnoreCase("ok") && i == 1) {
            plotUserInformation();
        } else if (result.equalsIgnoreCase("ok") && i == 3) {
            plotUserInformation();
        }
//        if (result.equalsIgnoreCase("retry") && i==2){
//            loadCategoryList();
//        }
    }

    public interface onOptionSelected {
        void onItemPassed(String value, String addorDelete);
    }

    public interface onOptionSelectedCategory {
        void onItemPassed(int position, ProCategoryData proCategoryData);
    }

    public void loadList() {

        LLMain.setVisibility(View.VISIBLE);
        LLNetworkDisconnection.setVisibility(View.GONE);

        ProServiceApiHelper.getInstance((LandScreenActivity) getActivity()).getProsListingAPI(new ProServiceApiHelper.getApiProcessCallback() {
            @Override
            public void onStart() {
                myLoader.showLoader();
            }

            @Override
            public void onComplete(String message) {
                if (myLoader != null && myLoader.isMyLoaderShowing())
                    myLoader.dismissLoader();

                try {
                    JSONObject jsonObject = new JSONObject(message);

                    if (jsonObject.has("info_array")) {

                        JSONArray info_array = jsonObject.getJSONArray("info_array");
                        Logger.printMessage("info_array", "" + info_array);

                        if (searchProListAdapter == null) {
                            Logger.printMessage("searchProListAdapter", "null");
                            searchProListAdapter = new SearchProListAdapter(getActivity(), info_array, new onOptionSelected() {
                                @Override
                                public void onItemPassed(String value, String addorDelete) {
                                    Logger.printMessage("value", ":--" + value);
                                    if (addorDelete.trim().equals("Y")) {
                                        deleteFavPro(value);
                                    } else {
                                        addFavPro(value);
                                    }
                                }
                            });
                            pros_list.setAdapter(searchProListAdapter);
                        } else {
                            Logger.printMessage("searchProListAdapter", "not null");
                            searchProListAdapter.refreshData(info_array);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(String error) {
                if (myLoader != null && myLoader.isMyLoaderShowing())
                    myLoader.dismissLoader();

                Logger.printMessage("error", error);

                if (error.equalsIgnoreCase(getActivity().getResources().getString(R.string.no_internet_connection_found_Please_check_your_internet_connection))) {
                    LLMain.setVisibility(View.GONE);
                    LLNetworkDisconnection.setVisibility(View.VISIBLE);
                }
                if (searchProListAdapter != null) {
                    Logger.printMessage("searchProListAdapter", "not null");

                    JSONArray jarr_info_array = new JSONArray();
                    searchProListAdapter.refreshData(jarr_info_array);
                }

                CustomAlert customAlert = new CustomAlert(getActivity(), "Load Error", "" + error, SearchLocalProFragment.this);
                customAlert.getListenerRetryCancelFromNormalAlert("retry", "abort", 1);

            }
        }, ProApplication.getInstance().getUserId(), category_search, ((LandScreenActivity) getActivity()).local_pros_search_zip);
    }

    public void deleteFavPro(final String pros_id) {

        TextView title = new TextView((LandScreenActivity) getActivity());
        title.setText("Are you sure you want to remove from favorites?");
//                title.setBackgroundResource(R.drawable.gradient);
        title.setPadding(10, 10, 10, 10);
        title.setGravity(Gravity.CENTER);
        title.setTextColor(getActivity().getResources().getColor(R.color.colorTextBlack));
        title.setTextSize(18);

        new AlertDialog.Builder((LandScreenActivity) getActivity())
                .setCustomTitle(title)

                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        ///////////delete from favorite list
                        try {
                            ProServiceApiHelper.getInstance((LandScreenActivity) getActivity()).favouriteProAddDelete(new ProServiceApiHelper.getApiProcessCallback() {
                                                                                                                          @Override
                                                                                                                          public void onStart() {
                                                                                                                              myLoader.showLoader();
                                                                                                                          }

                                                                                                                          @Override
                                                                                                                          public void onComplete(String message) {

                                                                                                                              if (myLoader != null && myLoader.isMyLoaderShowing())
                                                                                                                                  myLoader.dismissLoader();

                                                                                                                              CustomAlert customAlert = new CustomAlert(getActivity(), "Delete Favorite pros", "" + message, SearchLocalProFragment.this);
                                                                                                                              customAlert.createNormalAlert("ok", 1);
                                                                                                                          }

                                                                                                                          @Override
                                                                                                                          public void onError(String error) {
                                                                                                                              if (myLoader != null && myLoader.isMyLoaderShowing())
                                                                                                                                  myLoader.dismissLoader();

                                                                                                                              CustomAlert customAlert = new CustomAlert(getActivity(), "Delete Favorite pros", "" + error, SearchLocalProFragment.this);
                                                                                                                              customAlert.createNormalAlert("ok", 2);
                                                                                                                          }
                                                                                                                      },
                                    ProApplication.getInstance().getUserId(),
                                    pros_id
                            );
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                })
                .setCancelable(false)
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .show();
    }

    public void addFavPro(final String pros_id) {
        try {
            ProServiceApiHelper.getInstance((LandScreenActivity) getActivity()).favouriteProAddDelete(new ProServiceApiHelper.getApiProcessCallback() {
                                                                                                          @Override
                                                                                                          public void onStart() {
                                                                                                              myLoader.showLoader();

                                                                                                          }

                                                                                                          @Override
                                                                                                          public void onComplete(String message) {

                                                                                                              if (myLoader != null && myLoader.isMyLoaderShowing())
                                                                                                                  myLoader.dismissLoader();


                                                                                                              CustomAlert customAlert = new CustomAlert(getActivity(), "Add Favorite pros", "" + message, SearchLocalProFragment.this);
                                                                                                              customAlert.createNormalAlert("ok", 3);
                                                                                                          }

                                                                                                          @Override
                                                                                                          public void onError(String error) {
                                                                                                              if (myLoader != null && myLoader.isMyLoaderShowing())
                                                                                                                  myLoader.dismissLoader();

                                                                                                              CustomAlert customAlert = new CustomAlert(getActivity(), "Add Favorite pros", "" + error, SearchLocalProFragment.this);
                                                                                                              customAlert.createNormalAlert("ok", 4);
                                                                                                          }
                                                                                                      },
                    ProApplication.getInstance().getUserId(),
                    pros_id
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
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

                            Logger.printMessage("zipCode", "zipCode:-" + innerObj.getString("zipcode"));

                            if (innerObj.getString("zipcode").trim().equals("")) {
                                ((LandScreenActivity) getActivity()).local_pros_search_zip = "";
                                loadList();
                            } else {
                                ((LandScreenActivity) getActivity()).local_pros_search_zip = innerObj.getString("zipcode");
                                loadList();
                            }
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

    private void showDialog(View v, LinkedList<ProCategoryData> listdata) {

        if (popupWindow == null) {
            popupWindow = new PopupWindow(getActivity());
            // Closes the popup window when touch outside.
            popupWindow.setOutsideTouchable(true);
            popupWindow.setFocusable(false);
            // Removes default background.
            popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

            View dailogView = getActivity().getLayoutInflater().inflate(R.layout.dialog_show_category, null);

            RecyclerView rcv_ = (RecyclerView) dailogView.findViewById(R.id.rcv_);
            rcv_.setLayoutManager(new LinearLayoutManager(getActivity()));

            proCategoryListAdapter = new ProCategoryListAdapter(getActivity(), listdata, new onOptionSelectedCategory() {

                @Override
                public void onItemPassed(int position, ProCategoryData proCategoryData) {
                    Logger.printMessage("position-->" + position, "value-->" + proCategoryData.getCategory_name());

                    popupWindow.dismiss();
                }
            });

            rcv_.setAdapter(proCategoryListAdapter);
            // some other visual settings
            popupWindow.setFocusable(false);
            popupWindow.setWidth(WindowManager.LayoutParams.MATCH_PARENT);
            popupWindow.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);

            // set the list view as pop up window content
            popupWindow.setContentView(dailogView);
            popupWindow.showAsDropDown(v, -5, 0);


        } else {
            popupWindow.showAsDropDown(v, -5, 0);
            proCategoryListAdapter.setRefresh(listdata);
        }
    }

    public void closeKeypad() {
        try {
            keyboard.hideSoftInputFromWindow(getActivity().getWindow().getDecorView().getWindowToken(), 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}
