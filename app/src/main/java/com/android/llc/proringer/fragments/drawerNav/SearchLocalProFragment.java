package com.android.llc.proringer.fragments.drawerNav;

import android.app.ProgressDialog;
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

public class SearchLocalProFragment extends Fragment {
    private RecyclerView pros_list;
    String category_search = "";
    String zip_search = "";
    ProgressDialog pgDialog1;
    ProgressDialog pgDialog2;
    ProgressDialog pgDialog3;
    SearchProListAdapter searchProListAdapter;
    LinearLayout LLMain,LLNetworkDisconnection;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search_pros, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        pros_list = (RecyclerView) view.findViewById(R.id.pros_list);
        pros_list.setLayoutManager(new LinearLayoutManager((LandScreenActivity)getActivity()));


        LLMain= (LinearLayout) view.findViewById(R.id.LLMain);
        LLNetworkDisconnection= (LinearLayout) view.findViewById(R.id.LLNetworkDisconnection);

        loadList();

    }

    public interface onOptionSelected {
        void onItemPassed(String value, String addorDelete);
    }


    public void loadList() {

        LLMain.setVisibility(View.VISIBLE);
        LLNetworkDisconnection.setVisibility(View.GONE);

        ProServiceApiHelper.getInstance((LandScreenActivity)getActivity()).getProsListingAPI(new ProServiceApiHelper.getApiProcessCallback() {
            @Override
            public void onStart() {
                pgDialog1 = new ProgressDialog((LandScreenActivity)getActivity());
                pgDialog1.setTitle("Local Pros");
                pgDialog1.setCancelable(false);
                pgDialog1.setMessage("Getting local pros list.Please wait...");
                pgDialog1.show();
            }

            @Override
            public void onComplete(String message) {
                if (pgDialog1 != null && pgDialog1.isShowing())
                    pgDialog1.dismiss();

                try {
                    JSONObject jsonObject = new JSONObject(message);

                    if (jsonObject.has("info_array")) {

                        JSONArray info_array = jsonObject.getJSONArray("info_array");

                        if (searchProListAdapter == null) {

                            searchProListAdapter = new SearchProListAdapter((LandScreenActivity)getActivity(), info_array, new onOptionSelected() {
                                @Override
                                public void onItemPassed(String value, String addorDelete) {
                                    Logger.printMessage("value",":--"+value);
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
                if (pgDialog1 != null && pgDialog1.isShowing())
                    pgDialog1.dismiss();


                if(error.equalsIgnoreCase("No internet connection found. Please check your internet connection.")){
                    LLMain.setVisibility(View.GONE);
                    LLNetworkDisconnection.setVisibility(View.VISIBLE);
                }


                new AlertDialog.Builder(getActivity())
                        .setTitle("Load Error")
                        .setMessage("" + error)
                        .setPositiveButton("retry", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                loadList();

                            }
                        })
                        .setNegativeButton("abort", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).show();
            }
        }, ProApplication.getInstance().getUserId(), category_search, zip_search);
    }

    public void deleteFavPro(final String pros_id) {

        TextView title = new TextView((LandScreenActivity)getActivity());
        title.setText("Are you sure you want to remove from favorites?");
//                title.setBackgroundResource(R.drawable.gradient);
        title.setPadding(10, 10, 10, 10);
        title.setGravity(Gravity.CENTER);
        title.setTextColor(((LandScreenActivity)getActivity()).getResources().getColor(R.color.colorTextBlack));
        title.setTextSize(18);

        new AlertDialog.Builder((LandScreenActivity)getActivity())
                .setCustomTitle(title)

                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        ///////////delete from favorite list
                        try {
                            ProServiceApiHelper.getInstance((LandScreenActivity)getActivity()).favouriteProAddDelete(new ProServiceApiHelper.getApiProcessCallback() {
                                                                                                     @Override
                                                                                                     public void onStart() {
                                                                                                         pgDialog2 = new ProgressDialog((LandScreenActivity)getActivity());
                                                                                                         pgDialog2.setTitle("Delete Favorite pros");
                                                                                                         pgDialog2.setMessage("project is deleting.Please wait....");
                                                                                                         pgDialog2.setCancelable(false);
                                                                                                         pgDialog2.show();

                                                                                                     }

                                                                                                     @Override
                                                                                                     public void onComplete(String message) {

                                                                                                         if (pgDialog2 != null && pgDialog2.isShowing())
                                                                                                             pgDialog2.dismiss();

                                                                                                         new AlertDialog.Builder((LandScreenActivity)getActivity())
                                                                                                                 .setTitle("Delete Favorite pros")
                                                                                                                 .setMessage("" + message)
                                                                                                                 .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                                                                                                     @Override
                                                                                                                     public void onClick(DialogInterface dialog, int which) {
                                                                                                                         dialog.dismiss();
                                                                                                                         loadList();
                                                                                                                     }
                                                                                                                 })
                                                                                                                 .setCancelable(false)
                                                                                                                 .show();
                                                                                                     }

                                                                                                     @Override
                                                                                                     public void onError(String error) {
                                                                                                         if (pgDialog2 != null && pgDialog2.isShowing())
                                                                                                             pgDialog2.dismiss();

                                                                                                         new AlertDialog.Builder((LandScreenActivity)getActivity())
                                                                                                                 .setTitle("Delete Fav pros")
                                                                                                                 .setMessage("" + error)
                                                                                                                 .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                                                                                                     @Override
                                                                                                                     public void onClick(DialogInterface dialog, int which) {
                                                                                                                         dialog.dismiss();
                                                                                                                     }
                                                                                                                 })
                                                                                                                 .setCancelable(false)
                                                                                                                 .show();
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
            ProServiceApiHelper.getInstance((LandScreenActivity)getActivity()).favouriteProAddDelete(new ProServiceApiHelper.getApiProcessCallback() {
                                                                                     @Override
                                                                                     public void onStart() {
                                                                                         pgDialog3 = new ProgressDialog((LandScreenActivity)getActivity());
                                                                                         pgDialog3.setTitle("Add Favorite pros");
                                                                                         pgDialog3.setMessage("A project is adding.Please wait....");
                                                                                         pgDialog3.setCancelable(false);
                                                                                         pgDialog3.show();

                                                                                     }

                                                                                     @Override
                                                                                     public void onComplete(String message) {

                                                                                         if (pgDialog3 != null && pgDialog3.isShowing())
                                                                                             pgDialog3.dismiss();


                                                                                         new AlertDialog.Builder((LandScreenActivity)getActivity())
                                                                                                 .setTitle("Add Favorite pros")
                                                                                                 .setMessage("" + message)
                                                                                                 .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                                                                                     @Override
                                                                                                     public void onClick(DialogInterface dialog, int which) {
                                                                                                         dialog.dismiss();
                                                                                                         loadList();

                                                                                                     }
                                                                                                 })
                                                                                                 .setCancelable(false)
                                                                                                 .show();
                                                                                     }

                                                                                     @Override
                                                                                     public void onError(String error) {
                                                                                         if (pgDialog3 != null && pgDialog3.isShowing())
                                                                                             pgDialog3.dismiss();

                                                                                         new AlertDialog.Builder((LandScreenActivity)getActivity())
                                                                                                 .setTitle("Add Fav pros")
                                                                                                 .setMessage("" + error)
                                                                                                 .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                                                                                     @Override
                                                                                                     public void onClick(DialogInterface dialog, int which) {
                                                                                                         dialog.dismiss();
                                                                                                     }
                                                                                                 })
                                                                                                 .setCancelable(false)
                                                                                                 .show();

                                                                                     }
                                                                                 },
                    ProApplication.getInstance().getUserId(),
                    pros_id
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
