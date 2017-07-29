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
import android.widget.TextView;

import com.android.llc.proringer.R;
import com.android.llc.proringer.adapter.SearchFavoriteListAdapter;
import com.android.llc.proringer.adapter.SearchProListAdapter;
import com.android.llc.proringer.appconstant.ProApplication;
import com.android.llc.proringer.helper.ProServiceApiHelper;

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

public class SearchLocalPro extends Fragment {
    private RecyclerView pros_list;
    String category_search="";
    String zip_search="";
    ProgressDialog pgDialog;
    SearchProListAdapter searchProListAdapter;
    JSONArray info_array;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search_pros, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        pros_list = (RecyclerView) view.findViewById(R.id.pros_list);
        pros_list.setLayoutManager(new LinearLayoutManager(getActivity()));

        setListAdapter();

    }

    public interface onOptionSelected {
        void onItemPassed(int position, String value,String addorDelete);
    }


    public void setListAdapter(){
        ProServiceApiHelper.getInstance(getActivity()).getProsListingAPI(new ProServiceApiHelper.getApiProcessCallback() {
            @Override
            public void onStart() {
                pgDialog = new ProgressDialog(getActivity());
                pgDialog.setTitle("FavPros");
                pgDialog.setCancelable(false);
                pgDialog.setMessage("Getting FavPros list. Please wait.");
                pgDialog.show();
            }

            @Override
            public void onComplete(String message) {
                if (pgDialog != null && pgDialog.isShowing())
                    pgDialog.dismiss();

                try {
                    JSONObject jsonObject = new JSONObject(message);

                    if (jsonObject.has("info_array")) {

                        info_array=jsonObject.getJSONArray("info_array");

                        if(searchProListAdapter==null){

                            searchProListAdapter=new SearchProListAdapter(getActivity(), info_array, new onOptionSelected() {
                                @Override
                                public void onItemPassed(int position, String value, String addorDelete) {
                                    if (addorDelete.trim().equals("Y")){
                                        deleteFavPro(value);
                                    }
                                    else {
                                        addFavPro(value);
                                    }
                                }
                            });
                            pros_list.setAdapter(searchProListAdapter);
                        }
                        else {
                            searchProListAdapter.notifyDataSetChanged();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(String error) {
                if (pgDialog != null && pgDialog.isShowing())
                    pgDialog.dismiss();
            }
        },ProApplication.getInstance().getUserId(),category_search,zip_search);
    }

    public void deleteFavPro(final String pros_id){

        TextView title = new TextView(getActivity());
        title.setText("Are you sure you want to remove from favorites?");
//                title.setBackgroundResource(R.drawable.gradient);
        title.setPadding(10, 10, 10, 10);
        title.setGravity(Gravity.CENTER);
        title.setTextColor(getActivity().getResources().getColor(R.color.colorTextBlack));
        title.setTextSize(18);

        new AlertDialog.Builder(getActivity())
                .setCustomTitle(title)

                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        ///////////delete from favorite list
                        try {
                            ProServiceApiHelper.getInstance(getActivity()).favouriteProAddDelete(new ProServiceApiHelper.getApiProcessCallback() {
                                                                                                @Override
                                                                                                public void onStart() {

                                                                                                    pgDialog.setTitle("Delete Favorite pros");
                                                                                                    pgDialog.setMessage("It's deleting.Please wait....");
                                                                                                    pgDialog.setCancelable(false);
                                                                                                    pgDialog.show();

                                                                                                }

                                                                                                @Override
                                                                                                public void onComplete(String message) {

                                                                                                    if (pgDialog != null && pgDialog.isShowing())
                                                                                                        pgDialog.dismiss();

                                                                                                    new AlertDialog.Builder(getActivity())
                                                                                                            .setTitle("Delete Favorite pros")
                                                                                                            .setMessage("" + message)
                                                                                                            .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                                                                                                @Override
                                                                                                                public void onClick(DialogInterface dialog, int which) {
                                                                                                                    dialog.dismiss();
                                                                                                                    setListAdapter();
                                                                                                                }
                                                                                                            })
                                                                                                            .setCancelable(false)
                                                                                                            .show();
                                                                                                }

                                                                                                @Override
                                                                                                public void onError(String error) {
                                                                                                    if (pgDialog != null && pgDialog.isShowing())
                                                                                                        pgDialog.dismiss();

                                                                                                    new AlertDialog.Builder(getActivity())
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

    public void addFavPro(final  String pros_id){
        try {
            ProServiceApiHelper.getInstance(getActivity()).favouriteProAddDelete(new ProServiceApiHelper.getApiProcessCallback() {
                                                                                @Override
                                                                                public void onStart() {

                                                                                    pgDialog.setTitle("Add Favorite pros");
                                                                                    pgDialog.setMessage("It's adding.Please wait....");
                                                                                    pgDialog.setCancelable(false);
                                                                                    pgDialog.show();

                                                                                }

                                                                                @Override
                                                                                public void onComplete(String message) {

                                                                                    if (pgDialog != null && pgDialog.isShowing())
                                                                                        pgDialog.dismiss();


                                                                                    new AlertDialog.Builder(getActivity())
                                                                                            .setTitle("Add Favorite pros")
                                                                                            .setMessage("" + message)
                                                                                            .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                                                                                @Override
                                                                                                public void onClick(DialogInterface dialog, int which) {
                                                                                                    dialog.dismiss();
                                                                                                    setListAdapter();

                                                                                                }
                                                                                            })
                                                                                            .setCancelable(false)
                                                                                            .show();
                                                                                }

                                                                                @Override
                                                                                public void onError(String error) {
                                                                                    if (pgDialog != null && pgDialog.isShowing())
                                                                                        pgDialog.dismiss();

                                                                                    new AlertDialog.Builder(getActivity())
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
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
