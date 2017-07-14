package com.android.llc.proringer.fragments.postProject;

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
import com.android.llc.proringer.activities.ActivityPostProject;
import com.android.llc.proringer.adapter.PostProjectListAdapter;
import com.android.llc.proringer.helper.ProServiceApiHelper;
import com.android.llc.proringer.pojo.ProCategoryData;

import java.util.LinkedList;

/**
 * Created by su on 7/13/17.
 */

public class ServiceAndOtherList extends Fragment {
    private ProgressDialog pgDialog;
    private RecyclerView service_list;
    private PostProjectListAdapter adapter;
    public int step = 0;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.content_servicelisting_and_other, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        service_list = (RecyclerView) view.findViewById(R.id.service_list);
        service_list.setLayoutManager(new LinearLayoutManager(getActivity()));

        if (((ActivityPostProject) getActivity()).isForth)
            selectService(((ActivityPostProject) getActivity()).selectedCategory.getId());
        else {
            step = 5;
            performBack();
        }
    }


    public void selectService(String id) {
        ProServiceApiHelper.getInstance(getActivity()).getServiceList(new ProServiceApiHelper.onProCategoryListener() {
            @Override
            public void onComplete(LinkedList<ProCategoryData> listdata) {
                if (pgDialog != null && pgDialog.isShowing())
                    pgDialog.dismiss();

                ((ActivityPostProject) getActivity()).serviceListing = listdata;
                initAdapter(listdata);

            }

            @Override
            public void onError(String error) {
                if (pgDialog != null && pgDialog.isShowing())
                    pgDialog.dismiss();

                new AlertDialog.Builder(getActivity())
                        .setTitle("Error")
                        .setMessage("" + error)
                        .setCancelable(false)
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .show();

            }

            @Override
            public void onStartFetch() {
                pgDialog = new ProgressDialog(getActivity());
                pgDialog.setTitle("Preparing category");
                pgDialog.setMessage("Please wait while preparing category list.");
                pgDialog.setCancelable(false);
                pgDialog.show();

            }
        }, id);
    }

    private void initAdapter(LinkedList<ProCategoryData> itemData) {
        adapter = new PostProjectListAdapter(getActivity(),
                itemData, PostProjectListAdapter.TYPE_LIST,
                new PostProjectListAdapter.onClickItem() {
                    @Override
                    public void onSelectItemClick(int position, ProCategoryData data) {
                        ((ActivityPostProject) getActivity()).isForth=true;
                        if (step == 0) {
                            step++;

                            ((ActivityPostProject) getActivity()).selectedService = data;
                            ((ActivityPostProject) getActivity()).increaseStep();

                            ((ActivityPostProject) getActivity()).selectedServiceList = new LinkedList<>();
                            ProCategoryData data1 = new ProCategoryData("", "", "Repair", "");
                            ProCategoryData data2 = new ProCategoryData("", "", "Installation", "");
                            ProCategoryData data3 = new ProCategoryData("", "", "Others", "");
                            ((ActivityPostProject) getActivity()).selectedServiceList.add(data1);
                            ((ActivityPostProject) getActivity()).selectedServiceList.add(data2);
                            ((ActivityPostProject) getActivity()).selectedServiceList.add(data3);

                            adapter.updateList(((ActivityPostProject) getActivity()).selectedServiceList);

                        } else if (step == 1) {
                            step++;
                            ((ActivityPostProject) getActivity()).service_look_type = data.getCategory_name();
                            ((ActivityPostProject) getActivity()).increaseStep();

                            ((ActivityPostProject) getActivity()).service_look_typeList = new LinkedList<>();
                            ProCategoryData data1 = new ProCategoryData("4", "", "Single Family Home", "");
                            ProCategoryData data2 = new ProCategoryData("5", "", "Condominium", "");
                            ProCategoryData data3 = new ProCategoryData("6", "", "Townhome", "");
                            ProCategoryData data4 = new ProCategoryData("7", "", "Multi-Family", "");
                            ProCategoryData data5 = new ProCategoryData("1", "", "Commercial", "");
                            ((ActivityPostProject) getActivity()).service_look_typeList.add(data1);
                            ((ActivityPostProject) getActivity()).service_look_typeList.add(data2);
                            ((ActivityPostProject) getActivity()).service_look_typeList.add(data3);
                            ((ActivityPostProject) getActivity()).service_look_typeList.add(data4);
                            ((ActivityPostProject) getActivity()).service_look_typeList.add(data5);

                            adapter.updateList(((ActivityPostProject) getActivity()).service_look_typeList);
                        } else if (step == 2) {
                            step++;

                            ((ActivityPostProject) getActivity()).property_type = data.getId();
                            ((ActivityPostProject) getActivity()).increaseStep();

                            ((ActivityPostProject) getActivity()).property_typeList = new LinkedList<>();
                            ProCategoryData data1 = new ProCategoryData("", "", "Ready to hire", "");
                            ProCategoryData data2 = new ProCategoryData("", "", "Planning and Budgeting", "");
                            ((ActivityPostProject) getActivity()).property_typeList.add(data1);
                            ((ActivityPostProject) getActivity()).property_typeList.add(data2);
                            adapter.updateList(((ActivityPostProject) getActivity()).property_typeList);

                        } else if (step == 3) {
                            step++;
                            ((ActivityPostProject) getActivity()).project_stage = data.getCategory_name();
                            ((ActivityPostProject) getActivity()).increaseStep();

                            ((ActivityPostProject) getActivity()).project_stageList = new LinkedList<>();
                            ProCategoryData data1 = new ProCategoryData("1", "", "Timing Is Flexible", "");
                            ProCategoryData data2 = new ProCategoryData("2", "", "Within 1 Week", "");
                            ProCategoryData data3 = new ProCategoryData("3", "", "1-2 Week", "");
                            ProCategoryData data4 = new ProCategoryData("4", "", "More Than 2 Weeks", "");
                            ProCategoryData data5 = new ProCategoryData("5", "", "Emergency", "");
                            ((ActivityPostProject) getActivity()).project_stageList.add(data1);
                            ((ActivityPostProject) getActivity()).project_stageList.add(data2);
                            ((ActivityPostProject) getActivity()).project_stageList.add(data3);
                            ((ActivityPostProject) getActivity()).project_stageList.add(data4);
                            ((ActivityPostProject) getActivity()).project_stageList.add(data5);

                            adapter.updateList(((ActivityPostProject) getActivity()).project_stageList
                            );
                        } else if (step == 4) {
                            step++;
                            ((ActivityPostProject) getActivity()).timeframe_id = data.getId();
                            ((ActivityPostProject) getActivity()).increaseStep();


                            /**
                             * fragment calling
                             */
                            ((ActivityPostProject) getActivity()).changeFragmentNext(3);

                        }
                    }
                });
        service_list.setAdapter(adapter);
    }

    public void performBack() {
        switch (step) {
            case 1:
                adapter.updateList(((ActivityPostProject) getActivity()).serviceListing);
                step--;
                break;
            case 2:
                adapter.updateList(((ActivityPostProject) getActivity()).selectedServiceList);
                step--;
                break;
            case 3:
                adapter.updateList(((ActivityPostProject) getActivity()).service_look_typeList);
                step--;
                break;
            case 4:
                adapter.updateList(((ActivityPostProject) getActivity()).property_typeList);
                step--;
                break;
            case 5:
                initAdapter(((ActivityPostProject) getActivity()).project_stageList);
                step--;
                break;
        }
    }
}
