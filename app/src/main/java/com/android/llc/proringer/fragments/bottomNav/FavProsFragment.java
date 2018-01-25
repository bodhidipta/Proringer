package com.android.llc.proringer.fragments.bottomNav;

import android.content.Context;
import android.content.DialogInterface;
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
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.llc.proringer.R;
import com.android.llc.proringer.activities.LandScreenActivity;
import com.android.llc.proringer.adapter.SearchFavoriteListAdapter;
import com.android.llc.proringer.adapter.SearchProListAdapter;
import com.android.llc.proringer.appconstant.ProApplication;
import com.android.llc.proringer.database.DatabaseHandler;
import com.android.llc.proringer.fragments.drawerNav.SearchLocalProFragment;
import com.android.llc.proringer.helper.CustomAlert;
import com.android.llc.proringer.helper.MyCustomAlertListener;
import com.android.llc.proringer.helper.MyLoader;
import com.android.llc.proringer.helper.ProServiceApiHelper;
import com.android.llc.proringer.utils.Logger;
import com.android.llc.proringer.viewsmod.edittext.ProRegularEditText;
import com.android.llc.proringer.viewsmod.textview.ProRegularTextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by bodhidipta on 12/06/17.
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

public class FavProsFragment extends Fragment implements MyCustomAlertListener {
    private RecyclerView pros_list;
    MyLoader myLoader = null;
    SearchFavoriteListAdapter searchFavoriteListAdapter;
    LinearLayout LLMain, LLNetworkDisconnection;
    String category_search = "";
    ImageView img_clear;
    ProRegularEditText edt_search;
    TextWatcher mySearchTextWatcher;
    private InputMethodManager keyboard;
    ProRegularTextView tv_empty_show;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fav_pro, container, false);
    }

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        LLMain = (LinearLayout) view.findViewById(R.id.LLMain);
        LLNetworkDisconnection = (LinearLayout) view.findViewById(R.id.LLNetworkDisconnection);

        keyboard = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);

        img_clear = (ImageView) view.findViewById(R.id.img_clear);
        img_clear.setVisibility(View.GONE);

        edt_search = (ProRegularEditText) view.findViewById(R.id.edt_search);

        view.findViewById(R.id.tv_empty_show).setVisibility(View.GONE);

        pros_list = (RecyclerView) view.findViewById(R.id.pros_list);
        pros_list.setLayoutManager(new LinearLayoutManager((LandScreenActivity) getActivity()));

        tv_empty_show = (ProRegularTextView) view.findViewById(R.id.tv_empty_show);
        tv_empty_show.setVisibility(View.GONE);

        myLoader = new MyLoader(getActivity());

//        view.findViewById(R.id.find_local_pros).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                ((LandScreenActivity) (LandScreenActivity) getActivity()).transactSearchLocalPros();
//            }
//        });

        img_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edt_search.setText("");
                category_search = "";
                loadList();
                closeKeypad();
            }
        });

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
//                if(category_search.length()==0){
//                    closeKeypad();
//                    loadList();
//                }
                //loadCategoryList();
            }
        };


        edt_search.setText("");
        category_search = "";
        edt_search.addTextChangedListener(mySearchTextWatcher);

        edt_search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                    Logger.printMessage("search_category", edt_search.getText().toString());
                    closeKeypad();
                    loadListSearch();
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

        if (((LandScreenActivity) getActivity()).local_pros_search_zip.trim().equals("")) {
            plotUserInformation();
        } else {
            loadList();
        }
    }

    public void loadList() {

        LLMain.setVisibility(View.VISIBLE);
        LLNetworkDisconnection.setVisibility(View.GONE);

        ProServiceApiHelper.getInstance((LandScreenActivity) getActivity()).getUserFavoriteProsListAPI(new ProServiceApiHelper.getApiProcessCallback() {
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

                    pros_list.setVisibility(View.VISIBLE);
                    tv_empty_show.setVisibility(View.GONE);

                    if (jsonObject.has("info_array")) {

                        JSONArray info_array = jsonObject.getJSONArray("info_array");


                        Logger.printMessage("info_array", "" + info_array);

                        if (searchFavoriteListAdapter == null) {
                            Logger.printMessage("favSearchProListAdapter", "null");
                            searchFavoriteListAdapter = new SearchFavoriteListAdapter((LandScreenActivity) getActivity(), info_array, new onOptionSelected() {
                                @Override
                                public void onItemPassed(int position, String value) {
                                    DeleteFavPro(value, position);
                                }
                            });
                            pros_list.setAdapter(searchFavoriteListAdapter);
                        } else {
                            Logger.printMessage("searchProListAdapter", "not null");
                            searchFavoriteListAdapter.refreshData(info_array);
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


                if (error.equalsIgnoreCase(getActivity().getResources().getString(R.string.no_internet_connection_found_Please_check_your_internet_connection))) {
                    LLMain.setVisibility(View.GONE);
                    LLNetworkDisconnection.setVisibility(View.VISIBLE);
                }

//                if (searchFavoriteListAdapter != null) {
                    Logger.printMessage("searchProListAdapter", "not null");

                    pros_list.setVisibility(View.GONE);
                    tv_empty_show.setVisibility(View.VISIBLE);
//
//                    JSONArray jarr_info_array = new JSONArray();
//                    searchFavoriteListAdapter.refreshData(jarr_info_array);
//                }

//                CustomAlert customAlert = new CustomAlert(getActivity(), "Load Error", "" + error, FavProsFragment.this);
//                customAlert.getListenerRetryCancelFromNormalAlert("retry", "abort", 1);
            }
        }, ProApplication.getInstance().getUserId(), category_search, ((LandScreenActivity) getActivity()).local_pros_search_zip);
    }

    public void loadListSearch() {

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

                        if (searchFavoriteListAdapter == null) {
                            Logger.printMessage("favSearchProListAdapter", "null");
                            searchFavoriteListAdapter = new SearchFavoriteListAdapter((LandScreenActivity) getActivity(), info_array, new onOptionSelected() {
                                @Override
                                public void onItemPassed(int position, String value) {
                                    DeleteFavPro(value, position);
                                }
                            });
                            pros_list.setAdapter(searchFavoriteListAdapter);
                        } else {
                            Logger.printMessage("searchProListAdapter", "not null");
                            searchFavoriteListAdapter.refreshData(info_array);
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


                if (error.equalsIgnoreCase(getActivity().getResources().getString(R.string.no_internet_connection_found_Please_check_your_internet_connection))) {
                    LLMain.setVisibility(View.GONE);
                    LLNetworkDisconnection.setVisibility(View.VISIBLE);
                }

                if (searchFavoriteListAdapter != null) {
                    Logger.printMessage("searchProListAdapter", "not null in error call");


                    JSONArray jarr_info_array = new JSONArray();
                    searchFavoriteListAdapter.refreshData(jarr_info_array);
                }


                CustomAlert customAlert = new CustomAlert(getActivity(), "Load Error", "" + error, FavProsFragment.this);
                customAlert.getListenerRetryCancelFromNormalAlert("retry", "abort", 1);
            }
        }, ProApplication.getInstance().getUserId(), category_search, ((LandScreenActivity) getActivity()).local_pros_search_zip);
    }

    @Override
    public void callbackForAlert(String result, int i) {
        if (result.equalsIgnoreCase("retry") && i == 1) {
            loadList();
        }
    }

    public interface onOptionSelected {
        void onItemPassed(int position, String value);
    }

    public void DeleteFavPro(final String pros_id, final int pos) {

        TextView title = new TextView(getActivity());
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
                            ProServiceApiHelper.getInstance((LandScreenActivity) getActivity()).deleteFavoriteProAPI(new ProServiceApiHelper.getApiProcessCallback() {
                                                                                                                      @Override
                                                                                                                      public void onStart() {
                                                                                                                          myLoader.showLoader();
                                                                                                                      }

                                                                                                                      @Override
                                                                                                                      public void onComplete(String message) {

                                                                                                                          searchFavoriteListAdapter.notifyMe(pos);

                                                                                                                          if (myLoader != null && myLoader.isMyLoaderShowing())
                                                                                                                              myLoader.dismissLoader();

                                                                                                                          CustomAlert customAlert = new CustomAlert(getActivity(), "Delete Favorite pros", "" + message, FavProsFragment.this);
                                                                                                                          customAlert.createNormalAlert("ok", 1);

                                                                                                                      }

                                                                                                                      @Override
                                                                                                                      public void onError(String error) {
                                                                                                                          if (myLoader != null && myLoader.isMyLoaderShowing())
                                                                                                                              myLoader.dismissLoader();


                                                                                                                          CustomAlert customAlert = new CustomAlert(getActivity(), "Delete Favorite pros", "" + error, FavProsFragment.this);
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

    public void closeKeypad() {
        try {
            keyboard.hideSoftInputFromWindow(getActivity().getWindow().getDecorView().getWindowToken(), 0);
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
}
