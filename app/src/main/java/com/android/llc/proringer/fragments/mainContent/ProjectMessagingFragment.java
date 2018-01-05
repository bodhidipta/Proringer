package com.android.llc.proringer.fragments.mainContent;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.android.llc.proringer.R;
import com.android.llc.proringer.activities.IndividualMessageActivity;
import com.android.llc.proringer.activities.LandScreenActivity;
import com.android.llc.proringer.adapter.ProjectDetailedMessageAdapter;
import com.android.llc.proringer.appconstant.ProApplication;
import com.android.llc.proringer.helper.MyLoader;
import com.android.llc.proringer.helper.ProServiceApiHelper;
import com.android.llc.proringer.helper.SimpleDividerItemDecoration;
import com.android.llc.proringer.pojo.SetGetProjectMessageDetailsData;
import com.android.llc.proringer.utils.Logger;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by bodhidipta on 13/06/17.
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

public class ProjectMessagingFragment extends Fragment {
    RelativeLayout detailed_project_search;
    RecyclerView message_list;
    ArrayList<SetGetProjectMessageDetailsData> projectMessageDetailsArrayList;
    ProjectDetailedMessageAdapter projectDetailedMessageAdapter;
    String mypojectid="";
    MyLoader myLoader = null;
    JSONArray info_array;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mypojectid = getArguments().getString("project_id");
        return inflater.inflate(R.layout.project_detailed_messaging, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        myLoader = new MyLoader(getActivity());
        projectMessageDetailsArrayList = new ArrayList<>();

        detailed_project_search = (RelativeLayout) view.findViewById(R.id.detailed_project_search);

        message_list = (RecyclerView) view.findViewById(R.id.message_list);
        message_list.setLayoutManager(new LinearLayoutManager((LandScreenActivity) getActivity()));
        message_list.addItemDecoration(new SimpleDividerItemDecoration(getActivity()));

    }

    @Override
    public void onResume() {
        super.onResume();
        loadList();
    }

    //    @Override
//    protected void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
//
//        // Only if you need to restore open/close state when
//        // the orientation is changed
//        if (projectDetailedMessageAdapter != null) {
//            projectDetailedMessageAdapter.saveStates(outState);
//        }
//    }
//
//    @Override
//    protected void onRestoreInstanceState(Bundle savedInstanceState) {
//        super.onRestoreInstanceState(savedInstanceState);
//
//        // Only if you need to restore open/close state when
//        // the orientation is changed
//        if (projectDetailedMessageAdapter != null) {
//            projectDetailedMessageAdapter.restoreStates(savedInstanceState);
//        }
//    }

    public void loadList() {

        projectMessageDetailsArrayList.clear();

        ProServiceApiHelper.getInstance((LandScreenActivity) getActivity()).getUserMessageListAPI(new ProServiceApiHelper.getApiProcessCallback() {
            @Override
            public void onStart() {
                myLoader.showLoader();
            }

            @Override
            public void onComplete(String message) {
                if (myLoader != null && myLoader.isMyLoaderShowing())
                    myLoader.dismissLoader();
                try {

                    info_array= new JSONObject(message).getJSONArray("info_array");

                    if (info_array.getJSONObject(0).has("all_pro_user_list")) {

                        for (int i = 0; i < info_array.getJSONObject(0).getJSONArray("all_pro_user_list").length(); i++) {

                            SetGetProjectMessageDetailsData setGetProjectMessageDetailsData = new SetGetProjectMessageDetailsData();

                            setGetProjectMessageDetailsData.setId(info_array.getJSONObject(0).getJSONArray("all_pro_user_list").getJSONObject(i).getString("id"));
                            setGetProjectMessageDetailsData.setProject_id(info_array.getJSONObject(0).getJSONArray("all_pro_user_list").getJSONObject(i).getString("project_id"));
                            setGetProjectMessageDetailsData.setHomeowner_id(info_array.getJSONObject(0).getJSONArray("all_pro_user_list").getJSONObject(i).getString("homeowner_id"));
                            setGetProjectMessageDetailsData.setPro_id(info_array.getJSONObject(0).getJSONArray("all_pro_user_list").getJSONObject(i).getString("pro_id"));
                            setGetProjectMessageDetailsData.setPro_img(info_array.getJSONObject(0).getJSONArray("all_pro_user_list").getJSONObject(i).getString("pro_img"));
                            setGetProjectMessageDetailsData.setPro_rating(info_array.getJSONObject(0).getJSONArray("all_pro_user_list").getJSONObject(i).getString("pro_rating"));
                            setGetProjectMessageDetailsData.setPro_time_status(info_array.getJSONObject(0).getJSONArray("all_pro_user_list").getJSONObject(i).getString("pro_time_status"));
                            setGetProjectMessageDetailsData.setPro_user_name(info_array.getJSONObject(0).getJSONArray("all_pro_user_list").getJSONObject(i).getString("pro_user_name"));
                            setGetProjectMessageDetailsData.setPro_com_nm(info_array.getJSONObject(0).getJSONArray("all_pro_user_list").getJSONObject(i).getString("pro_com_nm"));
                            setGetProjectMessageDetailsData.setNo_of_msg(info_array.getJSONObject(0).getJSONArray("all_pro_user_list").getJSONObject(i).getInt("no_of_msg"));
                            setGetProjectMessageDetailsData.setMessage_info(info_array.getJSONObject(0).getJSONArray("all_pro_user_list").getJSONObject(i).getString("message_info"));
                            setGetProjectMessageDetailsData.setRead_status(info_array.getJSONObject(0).getJSONArray("all_pro_user_list").getJSONObject(i).getInt("read_status"));
                            setGetProjectMessageDetailsData.setMessage_list(info_array.getJSONObject(0).getJSONArray("all_pro_user_list").getJSONObject(i).getJSONArray("message_list"));

                            projectMessageDetailsArrayList.add(setGetProjectMessageDetailsData);
                        }
                        Logger.printMessage("all_pro_user_list", "" + info_array.getJSONObject(0).getJSONArray("all_pro_user_list"));

                        if (projectDetailedMessageAdapter == null) {
                            Logger.printMessage("projectDetailedMessageAdapter", "not null");
                            projectDetailedMessageAdapter = new ProjectDetailedMessageAdapter((LandScreenActivity) getActivity(), projectMessageDetailsArrayList, new onOptionSelected() {
                                @Override
                                public void onItemPassed(int position, String value) {
                                    //startActivity(new Intent((LandScreenActivity) getActivity(), IndividualMessageActivity.class));
                                    Intent intent= new Intent(getActivity(),IndividualMessageActivity.class);
                                    try {
                                        intent.putExtra("infoarry", info_array.getJSONObject(0).getJSONArray("all_pro_user_list").getJSONObject(position).toString());
                                        startActivity(intent);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                            message_list.setAdapter(projectDetailedMessageAdapter);

                        } else {
                            Logger.printMessage("projectDetailedMessageAdapter", "not null");
                            projectDetailedMessageAdapter.notifyDataSetChanged();
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
            }
        }, ProApplication.getInstance().getUserId(), mypojectid);

    }

    public interface onOptionSelected {
        void onItemPassed(int position, String value);
    }
}