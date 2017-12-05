package com.android.llc.proringer.fragments.bottomNav;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.llc.proringer.R;
import com.android.llc.proringer.activities.LandScreenActivity;
import com.android.llc.proringer.adapter.ProjectMessageAdapter;
import com.android.llc.proringer.appconstant.ProApplication;
import com.android.llc.proringer.helper.MyLoader;
import com.android.llc.proringer.helper.ProServiceApiHelper;
import com.android.llc.proringer.pojo.ProjectMessage;
import com.android.llc.proringer.utils.Logger;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

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
 */

public class MessageFragment extends Fragment {
    private RecyclerView project_message_list;
    private ProjectMessageAdapter projectMessageAdapter;
    MyLoader myLoader = null;
    ArrayList<ProjectMessage> projectMessageArrayList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.messages, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        projectMessageArrayList = new ArrayList<>();
        myLoader = new MyLoader(getActivity());

        project_message_list = (RecyclerView) view.findViewById(R.id.message_list);
        project_message_list.setLayoutManager(new LinearLayoutManager((LandScreenActivity) getActivity()));
        loadList();

    }

    public void loadList() {

        ProServiceApiHelper.getInstance((LandScreenActivity) getActivity()).getUserMessageList(new ProServiceApiHelper.getApiProcessCallback() {
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


                        for (int i = 0; i < jsonObject.getJSONArray("info_array").length(); i++) {
                            ProjectMessage projectMessage = new ProjectMessage();
                            projectMessage.setProj_id(jsonObject.getJSONArray("info_array").getJSONObject(i).getString("proj_id"));
                            projectMessage.setProj_image(jsonObject.getJSONArray("info_array").getJSONObject(i).getString("proj_image"));
                            projectMessage.setProj_name(jsonObject.getJSONArray("info_array").getJSONObject(i).getString("proj_name"));
                            projectMessage.setStatus(jsonObject.getJSONArray("info_array").getJSONObject(i).getString("status"));
                            projectMessage.setProject_date(jsonObject.getJSONArray("info_array").getJSONObject(i).getString("project_date"));
                            projectMessage.setNo_of_pros_user(jsonObject.getJSONArray("info_array").getJSONObject(i).getInt("no_of_pro_user"));
                            projectMessageArrayList.add(projectMessage);
                        }


                        JSONArray info_array = jsonObject.getJSONArray("info_array");


                        Logger.printMessage("info_array", "" + info_array);

                        if (projectMessageAdapter == null) {
                            Logger.printMessage("projectMessageAdapter", "null");
                            projectMessageAdapter = new ProjectMessageAdapter((LandScreenActivity) getActivity(), projectMessageArrayList, new onOptionSelected() {
                                @Override
                                public void onItemPassed(int position, String project_id) {
                                    ((LandScreenActivity) getActivity()).toggleToolBar(true);
                                    ((LandScreenActivity) getActivity()).transactProjectMessaging(project_id);
                                }
                            });

                            project_message_list.setAdapter(projectMessageAdapter);
                        } else {
                            Logger.printMessage("projectMessageAdapter", "not null");
                            //projectMessageAdapter.refreshData(info_array);

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
        }, ProApplication.getInstance().getUserId(), "");

    }

    public interface onOptionSelected {
        void onItemPassed(int position, String value);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

    }
}
