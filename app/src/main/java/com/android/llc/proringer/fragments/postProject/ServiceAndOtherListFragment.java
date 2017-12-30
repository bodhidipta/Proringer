package com.android.llc.proringer.fragments.postProject;

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
import com.android.llc.proringer.helper.MyLoader;
import com.android.llc.proringer.helper.ProServiceApiHelper;
import com.android.llc.proringer.pojo.SetGetProCategoryData;
import com.android.llc.proringer.utils.Logger;

import java.util.LinkedList;

/**
 * Created by su on 7/13/17.
 */

public class ServiceAndOtherListFragment extends Fragment {
    private MyLoader myLoader=null;
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

        myLoader=new MyLoader(getActivity());

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
            public void onComplete(LinkedList<SetGetProCategoryData> listdata) {
                if (myLoader != null && myLoader.isMyLoaderShowing())
                    myLoader.dismissLoader();

                ((PostProjectActivity) (PostProjectActivity) getActivity()).serviceListing = listdata;
                initAdapter(listdata);

            }

            @Override
            public void onError(String error) {
                if (myLoader != null && myLoader.isMyLoaderShowing())
                    myLoader.dismissLoader();

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
                myLoader.showLoader();
            }
        }, id);
    }

    private void initAdapter(LinkedList<SetGetProCategoryData> itemData) {
        adapter = new PostProjectServiceAndOtherListAdapter((PostProjectActivity) getActivity(),
                itemData, PostProjectServiceAndOtherListAdapter.TYPE_LIST,
                new PostProjectServiceAndOtherListAdapter.onClickItem() {
                    @Override
                    public void onSelectItemClick(int position, SetGetProCategoryData data) {
                        ((PostProjectActivity) getActivity()).isForth = true;
                        if (step == 0) {
                            step++;

                            Logger.printMessage("change_me","change_me");
                            ((PostProjectActivity) getActivity()).selectedService = data;
                            ((PostProjectActivity) getActivity()).increaseStep();

                            ((PostProjectActivity) getActivity()).selectedServiceList = new LinkedList<>();
                            SetGetProCategoryData data1 = new SetGetProCategoryData("", "", "Repair", "");
                            SetGetProCategoryData data2 = new SetGetProCategoryData("", "", "Installation", "");
                            SetGetProCategoryData data3 = new SetGetProCategoryData("", "", "Others", "");
                            ((PostProjectActivity) getActivity()).selectedServiceList.add(data1);
                            ((PostProjectActivity) getActivity()).selectedServiceList.add(data2);
                            ((PostProjectActivity) getActivity()).selectedServiceList.add(data3);

                            adapter.updateList(((PostProjectActivity) getActivity()).selectedServiceList);

                        } else if (step == 1) {
                            step++;
                            ((PostProjectActivity) getActivity()).service_look_type = data.getCategory_name();
                            ((PostProjectActivity) getActivity()).increaseStep();

                            ((PostProjectActivity) getActivity()).service_look_typeList = new LinkedList<>();
                            SetGetProCategoryData data1 = new SetGetProCategoryData("4", "", "Single Family Home", "");
                            SetGetProCategoryData data2 = new SetGetProCategoryData("5", "", "Condominium", "");
                            SetGetProCategoryData data3 = new SetGetProCategoryData("6", "", "Townhome", "");
                            SetGetProCategoryData data4 = new SetGetProCategoryData("7", "", "Multi-Family", "");
                            SetGetProCategoryData data5 = new SetGetProCategoryData("1", "", "Commercial", "");
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
                            SetGetProCategoryData data1 = new SetGetProCategoryData("", "", "Ready to hire", "");
                            SetGetProCategoryData data2 = new SetGetProCategoryData("", "", "Planning and Budgeting", "");
                            ((PostProjectActivity) getActivity()).property_typeList.add(data1);
                            ((PostProjectActivity) getActivity()).property_typeList.add(data2);
                            adapter.updateList(((PostProjectActivity) getActivity()).property_typeList);

                        } else if (step == 3) {
                            step++;
                            ((PostProjectActivity) getActivity()).project_stage = data.getCategory_name();
                            ((PostProjectActivity) getActivity()).increaseStep();

                            ((PostProjectActivity) getActivity()).project_stageList = new LinkedList<>();
                            SetGetProCategoryData data1 = new SetGetProCategoryData("1", "", "Timing Is Flexible", "");
                            SetGetProCategoryData data2 = new SetGetProCategoryData("2", "", "Within 1 Week", "");
                            SetGetProCategoryData data3 = new SetGetProCategoryData("3", "", "1-2 Week", "");
                            SetGetProCategoryData data4 = new SetGetProCategoryData("4", "", "More Than 2 Weeks", "");
                            SetGetProCategoryData data5 = new SetGetProCategoryData("5", "", "Emergency", "");
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
