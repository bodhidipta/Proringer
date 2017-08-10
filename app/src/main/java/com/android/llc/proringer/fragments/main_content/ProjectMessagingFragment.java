package com.android.llc.proringer.fragments.main_content;

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
import com.android.llc.proringer.helper.onItemClick;
import com.android.llc.proringer.pojo.ProjectMessageDetails;

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
    ArrayList<ProjectMessageDetails> projectMessageDetailsArrayList;
    ProjectDetailedMessageAdapter projectDetailedMessageAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.project_detailed_messaging, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        projectMessageDetailsArrayList = new ArrayList<>();

        for (int i = 0; i < 15; i++) {
            ProjectMessageDetails projectMessageDetails = new ProjectMessageDetails();
            projectMessageDetails.setIsOpen(false);
            projectMessageDetails.setTagName("view " + i);
            projectMessageDetailsArrayList.add(projectMessageDetails);
        }

        detailed_project_search = (RelativeLayout) view.findViewById(R.id.detailed_project_search);

        message_list = (RecyclerView) view.findViewById(R.id.message_list);
        message_list.setLayoutManager(new LinearLayoutManager((LandScreenActivity) getActivity()));
        projectDetailedMessageAdapter = new ProjectDetailedMessageAdapter((LandScreenActivity) getActivity(), projectMessageDetailsArrayList, new onItemClick() {
            @Override
            public void onItemClick(int position) {
                startActivity(new Intent((LandScreenActivity) getActivity(), IndividualMessageActivity.class));
            }
        });
        message_list.setAdapter(projectDetailedMessageAdapter);
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
}
