package com.android.llc.proringer.fragments.bottomNav;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import com.android.llc.proringer.R;
import com.android.llc.proringer.activities.LandScreenActivity;
import com.android.llc.proringer.adapter.ProjectListingAdapter;
import com.android.llc.proringer.appconstant.ProApplication;
import com.android.llc.proringer.helper.CustomAlert;
import com.android.llc.proringer.helper.MyCustomAlertListener;
import com.android.llc.proringer.helper.MyLoader;
import com.android.llc.proringer.helper.ProServiceApiHelper;
import com.android.llc.proringer.pojo.ProjectPostedData;

import java.util.List;

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

public class MyProjectsFragment extends Fragment implements MyCustomAlertListener{
    MyLoader myLoader=null;
    RecyclerView project_list;
    LinearLayout no_project_available, LLNetworkDisconnection;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.my_projects, container, false);
    }

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        no_project_available = (LinearLayout) view.findViewById(R.id.no_project_available);
        LLNetworkDisconnection = (LinearLayout) view.findViewById(R.id.LLNetworkDisconnection);
        project_list = (RecyclerView) view.findViewById(R.id.project_list);
        project_list.setLayoutManager(new LinearLayoutManager((LandScreenActivity) getActivity()));

        myLoader=new MyLoader(getActivity());

        loadList();
    }

    public void loadList() {
        project_list.setVisibility(View.VISIBLE);
        LLNetworkDisconnection.setVisibility(View.GONE);

        ProServiceApiHelper.getInstance((LandScreenActivity) getActivity()).getMyProjectList(new ProServiceApiHelper.projectListCallback() {
            @Override
            public void onStart() {
                myLoader.showLoader();
            }

            @Override
            public void onComplete(final List projectList) {

                if (myLoader != null && myLoader.isMyLoaderShowing())
                    myLoader.dismissLoader();

                if (projectList != null && projectList.size() > 0)
                    project_list.setAdapter(new ProjectListingAdapter((LandScreenActivity) getActivity(), projectList, new onOptionSelected() {
                        @Override
                        public void onItemPassed(int position, String value) {
                            ProApplication.getInstance().setDataSelected((ProjectPostedData) projectList.get(position));
                            if (ProApplication.getInstance().getDataSelected().getProject_status().equalsIgnoreCase("A")
                                    || ProApplication.getInstance().getDataSelected().getProject_status().equalsIgnoreCase("Y")) {
                                ((LandScreenActivity) getActivity()).transactMyProjectsDetails();
                            }

                        }
                    }));
                else
                    no_project_available.setVisibility(View.VISIBLE);
            }

            @Override
            public void onError(String error) {
                if (myLoader != null && myLoader.isMyLoaderShowing())
                    myLoader.dismissLoader();

                if (error.equalsIgnoreCase(getResources().getString(R.string.no_internet_connection_found_Please_check_your_internet_connection))) {
                    project_list.setVisibility(View.GONE);
                    LLNetworkDisconnection.setVisibility(View.VISIBLE);
                }


                CustomAlert customAlert = new CustomAlert(getActivity(), "Load Error", "" + error,MyProjectsFragment.this);
                customAlert.getListenerRetryCancelFromNormalAlert("retry","abort",1);
            }
        });

    }

    @Override
    public void callbackForAlert(String result, int i) {

        if (result.equalsIgnoreCase("retry")&&i==1) {
            loadList();
        }
    }

    public interface onOptionSelected {
        void onItemPassed(int position, String value);
    }
}
