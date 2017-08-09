package com.android.llc.proringer.fragments.bottomNav;

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
import android.widget.TextView;

import com.android.llc.proringer.R;
import com.android.llc.proringer.activities.LandScreenActivity;
import com.android.llc.proringer.adapter.SearchFavoriteListAdapter;
import com.android.llc.proringer.appconstant.ProApplication;
import com.android.llc.proringer.helper.ProServiceApiHelper;

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

public class FavProsFragment extends Fragment {
    private RecyclerView pros_list;
    ProgressDialog pgDialog;
    SearchFavoriteListAdapter searchFavoriteListAdapter;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fav_pro, container, false);
    }

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        view.findViewById(R.id.tv_empty_show).setVisibility(View.GONE);

        pros_list = (RecyclerView) view.findViewById(R.id.pros_list);
        pros_list.setLayoutManager(new LinearLayoutManager((LandScreenActivity)getActivity()));


        ProServiceApiHelper.getInstance((LandScreenActivity)getActivity()).getUserFavoriteProsList(new ProServiceApiHelper.getApiProcessCallback() {
            @Override
            public void onStart() {
                pgDialog = new ProgressDialog(getActivity());
                pgDialog.setTitle("Favorite Pros");
                pgDialog.setCancelable(false);
                pgDialog.setMessage("Getting Favorite Pros list.Please wait...");
                pgDialog.show();
            }

            @Override
            public void onComplete(String message) {
                if (pgDialog != null && pgDialog.isShowing())
                    pgDialog.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(message);

                    if (jsonObject.has("info_array")) {

                        JSONArray info_array=jsonObject.getJSONArray("info_array");

                        searchFavoriteListAdapter=new SearchFavoriteListAdapter((LandScreenActivity)getActivity(), info_array, new onOptionSelected() {
                            @Override
                            public void onItemPassed(int position, String value) {
                                DeleteFavPro(value,position);
                            }
                        });
                        pros_list.setAdapter(searchFavoriteListAdapter);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(String error) {
                if (pgDialog != null && pgDialog.isShowing())
                    pgDialog.dismiss();


                if(error.equalsIgnoreCase("No internet connection found. Please check your internet connection.")){
                    view.findViewById(R.id.LLMain).setVisibility(View.GONE);
                    view.findViewById(R.id.LLNetworkDisconnection).setVisibility(View.VISIBLE);
                }

                new AlertDialog.Builder(getActivity())
                        .setTitle("Favorite Pros Load Error")
                        .setMessage("" + error)
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        })
                        .show();

            }
        });

        view.findViewById(R.id.find_local_pros).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((LandScreenActivity)(LandScreenActivity)getActivity()).transactSearchLocalPros();
            }
        });
    }

    public interface onOptionSelected {
        void onItemPassed(int position, String value);
    }

    public void DeleteFavPro(final String pros_id, final int pos){

        TextView title = new TextView(getActivity());
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
                            ProServiceApiHelper.getInstance((LandScreenActivity)getActivity()).deleteFavoritePro(new ProServiceApiHelper.getApiProcessCallback() {
                                                                                            @Override
                                                                                            public void onStart() {

                                                                                                pgDialog.setTitle("Delete Favorite pros");
                                                                                                pgDialog.setMessage("Favorite pros deleting.Please wait...");
                                                                                                pgDialog.setCancelable(false);
                                                                                                pgDialog.show();

                                                                                            }

                                                                                            @Override
                                                                                            public void onComplete(String message) {

                                                                                                searchFavoriteListAdapter.notifyMe(pos);

                                                                                                if (pgDialog != null && pgDialog.isShowing())
                                                                                                    pgDialog.dismiss();


                                                                                                new AlertDialog.Builder(getActivity())
                                                                                                        .setTitle("Delete Favorite pros")
                                                                                                        .setMessage("" + message)
                                                                                                        .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                                                                                            @Override
                                                                                                            public void onClick(DialogInterface dialog, int which) {
                                                                                                                dialog.dismiss();
                                                                                                            }
                                                                                                        })
                                                                                                        .setCancelable(false)
                                                                                                        .show();
                                                                                            }

                                                                                            @Override
                                                                                            public void onError(String error) {
                                                                                                if (pgDialog != null && pgDialog.isShowing())
                                                                                                    pgDialog.dismiss();

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
                        }
                        catch (Exception e)
                        {
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
}
