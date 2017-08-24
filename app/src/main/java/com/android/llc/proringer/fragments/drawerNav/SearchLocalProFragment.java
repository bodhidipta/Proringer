package com.android.llc.proringer.fragments.drawerNav;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.llc.proringer.R;
import com.android.llc.proringer.activities.LandScreenActivity;
import com.android.llc.proringer.adapter.SearchProListAdapter;
import com.android.llc.proringer.appconstant.ProApplication;
import com.android.llc.proringer.database.DatabaseHandler;
import com.android.llc.proringer.helper.CustomAlert;
import com.android.llc.proringer.helper.MyCustomAlertListener;
import com.android.llc.proringer.helper.MyLoader;
import com.android.llc.proringer.helper.ProServiceApiHelper;
import com.android.llc.proringer.utils.Logger;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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

    SearchProListAdapter searchProListAdapter;
    LinearLayout LLMain, LLNetworkDisconnection;

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

        myLoader = new MyLoader(getActivity());
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
    }

    public interface onOptionSelected {
        void onItemPassed(String value, String addorDelete);
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

                        if (searchProListAdapter == null) {

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


                if (error.equalsIgnoreCase(getActivity().getResources().getString(R.string.no_internet_connection_found_Please_check_your_internet_connection))) {
                    LLMain.setVisibility(View.GONE);
                    LLNetworkDisconnection.setVisibility(View.VISIBLE);
                }

                CustomAlert customAlert = new CustomAlert(getActivity(), "Load Error", "" + error, SearchLocalProFragment.this);
                customAlert.getListenerRetryCancelFromNormalAlert("retry", "abort", 1);

            }
        }, ProApplication.getInstance().getUserId(), category_search);
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
                                ProServiceApiHelper.getInstance(getActivity()).setSearchZip("");
                                loadList();
                            } else {
                                ProServiceApiHelper.getInstance(getActivity()).setSearchZip(innerObj.getString("zipcode"));
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

    @Override
    public void onResume() {
        super.onResume();
        plotUserInformation();
    }
}
