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
import com.android.llc.proringer.activities.PostProjectActivity;
import com.android.llc.proringer.adapter.PostProjectServiceAndOtherListAdapter;
import com.android.llc.proringer.helper.ProServiceApiHelper;
import com.android.llc.proringer.pojo.ProCategoryData;

import java.util.LinkedList;

/**
 * Created by su on 7/13/17.
 */

public class ServiceAndOtherListFragment extends Fragment {
    private ProgressDialog pgDialog;
    private RecyclerView service_list;
    private PostProjectServiceAndOtherListAdapter adapter;
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

        if (((PostProjectActivity) getActivity()).isForth)
            selectService(((PostProjectActivity) getActivity()).selectedCategory.getId());
        else {
            step = 5;
            performBack();
        }
    }


    public void selectService(String id) {
        ProServiceApiHelper.getInstance((PostProjectActivity) getActivity()).getServiceList(new ProServiceApiHelper.onProCategoryListener() {
            @Override
            public void onComplete(LinkedList<ProCategoryData> listdata) {
                if (pgDialog != null && pgDialog.isShowing())
                    pgDialog.dismiss();

                ((PostProjectActivity) (PostProjectActivity) getActivity()).serviceListing = listdata;
                initAdapter(listdata);

            }

            @Override
            public void onError(String error) {
                if (pgDialog != null && pgDialog.isShowing())
                    pgDialog.dismiss();

                new AlertDialog.Builder((PostProjectActivity) getActivity())
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
                pgDialog = new ProgressDialog((PostProjectActivity) getActivity());
                pgDialog.setTitle("Preparing service");
                pgDialog.setMessage("Getting service list.Please wait...");
                pgDialog.setCancelable(false);
                pgDialog.show();

            }
        }, id);
    }

    private void initAdapter(LinkedList<ProCategoryData> itemData) {
        adapter = new PostProjectServiceAndOtherListAdapter((PostProjectActivity) getActivity(),
                itemData, PostProjectServiceAndOtherListAdapter.TYPE_LIST,
                new PostProjectServiceAndOtherListAdapter.onClickItem() {
                    @Override
                    public void onSelectItemClick(int position, ProCategoryData data) {
                        ((PostProjectActivity) getActivity()).isForth = true;
                        if (step == 0) {
                            step++;

                            ((PostProjectActivity) getActivity()).selectedService = data;
                            ((PostProjectActivity) getActivity()).increaseStep();

                            ((PostProjectActivity) getActivity()).selectedServiceList = new LinkedList<>();
                            ProCategoryData data1 = new ProCategoryData("", "", "Repair", "");
                            ProCategoryData data2 = new ProCategoryData("", "", "Installation", "");
                            ProCategoryData data3 = new ProCategoryData("", "", "Others", "");
                            ((PostProjectActivity) getActivity()).selectedServiceList.add(data1);
                            ((PostProjectActivity) getActivity()).selectedServiceList.add(data2);
                            ((PostProjectActivity) getActivity()).selectedServiceList.add(data3);

                            adapter.updateList(((PostProjectActivity) getActivity()).selectedServiceList);

                        } else if (step == 1) {
                            step++;
                            ((PostProjectActivity) getActivity()).service_look_type = data.getCategory_name();
                            ((PostProjectActivity) getActivity()).increaseStep();

                            ((PostProjectActivity) getActivity()).service_look_typeList = new LinkedList<>();
                            ProCategoryData data1 = new ProCategoryData("4", "", "Single Family Home", "");
                            ProCategoryData data2 = new ProCategoryData("5", "", "Condominium", "");
                            ProCategoryData data3 = new ProCategoryData("6", "", "Townhome", "");
                            ProCategoryData data4 = new ProCategoryData("7", "", "Multi-Family", "");
                            ProCategoryData data5 = new ProCategoryData("1", "", "Commercial", "");
                            ((PostProjectActivity) getActivity()).service_look_typeList.add(data1);
                            ((PostProjectActivity) getActivity()).service_look_typeList.add(data2);
                            ((PostProjectActivity) getActivity()).service_look_typeList.add(data3);
                            ((PostProjectActivity) getActivity()).service_look_typeList.add(data4);
                            ((PostProjectActivity) getActivity()).service_look_typeList.add(data5);

                            adapter.updateList(((PostProjectActivity) getActivity()).service_look_typeList);
                        } else if (step == 2) {
                            step++;

                            ((PostProjectActivity) getActivity()).property_type = data.getId();
                            ((PostProjectActivity) getActivity()).increaseStep();

                            ((PostProjectActivity) getActivity()).property_typeList = new LinkedList<>();
                            ProCategoryData data1 = new ProCategoryData("", "", "Ready to hire", "");
                            ProCategoryData data2 = new ProCategoryData("", "", "Planning and Budgeting", "");
                            ((PostProjectActivity) getActivity()).property_typeList.add(data1);
                            ((PostProjectActivity) getActivity()).property_typeList.add(data2);
                            adapter.updateList(((PostProjectActivity) getActivity()).property_typeList);

                        } else if (step == 3) {
                            step++;
                            ((PostProjectActivity) getActivity()).project_stage = data.getCategory_name();
                            ((PostProjectActivity) getActivity()).increaseStep();

                            ((PostProjectActivity) getActivity()).project_stageList = new LinkedList<>();
                            ProCategoryData data1 = new ProCategoryData("1", "", "Timing Is Flexible", "");
                            ProCategoryData data2 = new ProCategoryData("2", "", "Within 1 Week", "");
                            ProCategoryData data3 = new ProCategoryData("3", "", "1-2 Week", "");
                            ProCategoryData data4 = new ProCategoryData("4", "", "More Than 2 Weeks", "");
                            ProCategoryData data5 = new ProCategoryData("5", "", "Emergency", "");
                            ((PostProjectActivity) getActivity()).project_stageList.add(data1);
                            ((PostProjectActivity) getActivity()).project_stageList.add(data2);
                            ((PostProjectActivity) getActivity()).project_stageList.add(data3);
                            ((PostProjectActivity) getActivity()).project_stageList.add(data4);
                            ((PostProjectActivity) getActivity()).project_stageList.add(data5);

                            adapter.updateList(((PostProjectActivity) getActivity()).project_stageList
                            );
                        } else if (step == 4) {
                            step++;
                            ((PostProjectActivity) getActivity()).timeframe_id = data.getId();
                            ((PostProjectActivity) getActivity()).increaseStep();


                            /**
                             * fragment calling
                             */
                            ((PostProjectActivity) getActivity()).changeFragmentNext(3);

                        }
                    }
                });
        service_list.setAdapter(adapter);
    }

    public void performBack() {
        switch (step) {
            case 1:
                adapter.updateList(((PostProjectActivity) getActivity()).serviceListing);
                step--;
                break;
            case 2:
                adapter.updateList(((PostProjectActivity) getActivity()).selectedServiceList);
                step--;
                break;
            case 3:
                adapter.updateList(((PostProjectActivity) getActivity()).service_look_typeList);
                step--;
                break;
            case 4:
                adapter.updateList(((PostProjectActivity) getActivity()).property_typeList);
                step--;
                break;
            case 5:
                initAdapter(((PostProjectActivity) getActivity()).project_stageList);
                step--;
                break;
        }
    }
}
