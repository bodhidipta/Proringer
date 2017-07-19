package com.android.llc.proringer.fragments.bottomNav;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.llc.proringer.R;
import com.android.llc.proringer.activities.LandScreenActivity;
import com.android.llc.proringer.adapter.ProjectListingAdapter;
import com.android.llc.proringer.appconstant.ProApplication;
import com.android.llc.proringer.helper.ProServiceApiHelper;
import com.android.llc.proringer.pojo.ProjectPostedData;
import com.android.llc.proringer.utils.Logger;

import java.util.LinkedList;
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

public class MyProjects extends Fragment {
    ProgressDialog dialog;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.my_projects, container, false);
    }

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final RecyclerView project_list = (RecyclerView) view.findViewById(R.id.project_list);
        project_list.setLayoutManager(new LinearLayoutManager(getActivity()));

        ProServiceApiHelper.getInstance(getActivity()).getMyProjectList(new ProServiceApiHelper.projectListCallback() {
            @Override
            public void onStart() {
                dialog = new ProgressDialog(getActivity());
                dialog.setTitle("My Projects");
                dialog.setCancelable(false);
                dialog.setMessage("Getting MyProject list. Please wait.");
                dialog.show();
            }

            @Override
            public void onComplete(final List projectList) {

                if (dialog != null && dialog.isShowing())
                    dialog.dismiss();

                if (projectList != null && projectList.size() > 0)
                    project_list.setAdapter(new ProjectListingAdapter(getActivity(), projectList, new onOptionSelected() {
                        @Override
                        public void onItemPassed(int position, String value) {
                            ProApplication.getInstance().setDataSelected((ProjectPostedData) projectList.get(position));
                            ((LandScreenActivity) getActivity()).transactMyProjectsDetails();

                        }
                    }));
                else
                    view.findViewById(R.id.no_project_available).setVisibility(View.VISIBLE);
            }

            @Override
            public void onError(String error) {
                if (dialog != null && dialog.isShowing())
                    dialog.dismiss();
                new AlertDialog.Builder(getActivity())
                        .setTitle("MyProjects Error")
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

    }

    public interface onOptionSelected {
        void onItemPassed(int position, String value);
    }


}
