package com.android.llc.proringer.fragments.bottomNav;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import com.android.llc.proringer.R;
import com.android.llc.proringer.activities.LandScreenActivity;
import com.android.llc.proringer.adapter.PostProjectCategoryGridAdapter;
import com.android.llc.proringer.adapter.PostProjectServiceAndOtherListAdapter;
import com.android.llc.proringer.appconstant.ProApplication;
import com.android.llc.proringer.helper.CustomAlert;
import com.android.llc.proringer.helper.MyCustomAlertListener;
import com.android.llc.proringer.helper.MyLoader;
import com.android.llc.proringer.helper.ProServiceApiHelper;
import com.android.llc.proringer.pojo.ProCategoryData;
import com.android.llc.proringer.utils.Logger;
import com.android.llc.proringer.viewsmod.textview.ProRegularTextView;

import java.util.LinkedList;

/**
 * Created by bodhidipta on 15/06/17.
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

public class PostProjectFragment extends Fragment implements MyCustomAlertListener {
    private ProgressBar progress_posting;
    private ProRegularTextView selected_service_category, service_request_category, selected_service_property;
    private RecyclerView pro_service_listing;
    private LinearLayout content_post_form_submit, container_registration, container_project_description;
    private RelativeLayout container_location;
    MyLoader myLoader = null;
    private PostProjectServiceAndOtherListAdapter adapter = null;
    private PostProjectCategoryGridAdapter gridAdapter = null;
    private LinkedList<ProCategoryData> serviceListing = null;
    private int step = 0;
    private String selectedId = "", step1Option = "",
            step2option = "What type of work best describe this project?",
            step3option = "What type of property will this be for?",
            step4option = "What is the current status for this project?",
            step5option = "When would you like to start?",
            step6option = "Add a photo(Optional) ",
            step7option = "Add details about the project",
            step8option = "Where is the project located",
            step9option = "Lets get acquainted",
            step10option = "WE SENT YOUR PROJECT TO THE PROS!";

    LinearLayout LLMain, LLNetworkDisconnection;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.post_project, container, false);
    }

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        LLMain = (LinearLayout) view.findViewById(R.id.LLMain);
        LLNetworkDisconnection = (LinearLayout) view.findViewById(R.id.LLNetworkDisconnection);

        myLoader=new MyLoader(getActivity());

        progress_posting = (ProgressBar) view.findViewById(R.id.progress_posting);
        selected_service_category = (ProRegularTextView) view.findViewById(R.id.selected_service_category);
        service_request_category = (ProRegularTextView) view.findViewById(R.id.service_request_category);
        selected_service_property = (ProRegularTextView) view.findViewById(R.id.selected_service_property);

        pro_service_listing = (RecyclerView) view.findViewById(R.id.pro_service_listing);
        pro_service_listing.setLayoutManager(new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL));

        content_post_form_submit = (LinearLayout) view.findViewById(R.id.content_post_form_submit);
        container_registration = (LinearLayout) view.findViewById(R.id.container_registration);
        container_project_description = (LinearLayout) view.findViewById(R.id.container_project_description);
        container_location = (RelativeLayout) view.findViewById(R.id.container_location);

        if (ProApplication.getInstance().equals("")) {
            progress_posting.setMax(10);
        } else {
            progress_posting.setMax(9);
        }

        listPostProject();

        view.findViewById(R.id.continue_image_section).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                step++;
                progress_posting.setProgress(step);
                selected_service_property.setText("" + step7option);
                container_project_description.setVisibility(View.VISIBLE);

            }
        });
        view.findViewById(R.id.continue_project_describing).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                step++;
                progress_posting.setProgress(step);
                selected_service_property.setText("" + step8option);
                container_location.setVisibility(View.VISIBLE);
            }
        });
        view.findViewById(R.id.continue_location_section).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ProApplication.getInstance().getUserId().equals("")) {
                    step++;
                    progress_posting.setProgress(step);
                    selected_service_property.setText("" + step9option);
                    /**
                     * IF no user is login then visible registration process or visible last part
                     */
                    container_registration.setVisibility(View.VISIBLE);
                } else {

                    step++;
                    progress_posting.setProgress(step);
                    selected_service_property.setText("" + step10option);
                    content_post_form_submit.setVisibility(View.VISIBLE);
                    Logger.printMessage("@steps", "" + step);
                }

            }
        });
        view.findViewById(R.id.register).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                step++;
                progress_posting.setProgress(step);
                selected_service_property.setText("" + step10option);
                content_post_form_submit.setVisibility(View.VISIBLE);

                /**
                 * Post it ..
                 cat_id = category id(from category list api)
                 service_id = category service id (from category service list api)
                 service_look_type( static content list)
                 property_type = property id(from category property list api)
                 project_stage( static content list)
                 timeframe_id = project timeframe id (from category timeframe list api)
                 project_image
                 project_details
                 project_zipcode
                 city
                 state = state_code(from state table)
                 country = country_code(from country table)
                 latitude
                 longitude
                 f_name
                 l_name
                 email_id
                 password
                 */
            }
        });
        view.findViewById(R.id.close_project).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((LandScreenActivity) getActivity()).toggleToolBar(false);
                ((LandScreenActivity) getActivity()).redirectToDashBoard();

            }
        });
    }

    public void listPostProject() {

        LLMain.setVisibility(View.VISIBLE);
        LLNetworkDisconnection.setVisibility(View.GONE);

        ProServiceApiHelper.getInstance((LandScreenActivity) getActivity()).getCategoryList(new ProServiceApiHelper.onProCategoryListener() {
            @Override
            public void onStartFetch() {
                myLoader.showLoader();
            }

            @Override
            public void onComplete(LinkedList<ProCategoryData> listdata) {
                if (myLoader != null && myLoader.isMyLoaderShowing())
                    myLoader.dismissLoader();
                progress_posting.setProgress(step);
                gridAdapter = new PostProjectCategoryGridAdapter((LandScreenActivity) getActivity(), listdata, new PostProjectCategoryGridAdapter.onClickItem() {
                    @Override
                    public void onSelectItemClick(int position, ProCategoryData data) {
                        if (step == 0) {
                            step1Option = data.getCategory_name();
                            selectedId = data.getId();
                            selectService(data.getId());
                        }
                    }
                });
                pro_service_listing.setAdapter(gridAdapter);
            }

            @Override
            public void onError(String error) {
                if (myLoader != null && myLoader.isMyLoaderShowing())
                    myLoader.dismissLoader();
                if (error.equalsIgnoreCase(getActivity().getResources().getString(R.string.no_internet_connection_found_Please_check_your_internet_connection))) {
                    LLMain.setVisibility(View.GONE);
                    LLNetworkDisconnection.setVisibility(View.VISIBLE);
                }


                CustomAlert customAlert = new CustomAlert(getActivity(), "Load Error", "" + error, PostProjectFragment.this);
                customAlert.getListenerRetryCancelFromNormalAlert("retry","abort",1);
            }
        });
    }

    private void selectService(String id) {
        ProServiceApiHelper.getInstance((LandScreenActivity) getActivity()).getServiceList(new ProServiceApiHelper.onProCategoryListener() {
            @Override
            public void onComplete(LinkedList<ProCategoryData> listdata) {
                if (myLoader != null && myLoader.isMyLoaderShowing())
                    myLoader.dismissLoader();
                serviceListing = listdata;
                ((LandScreenActivity) getActivity()).toggleToolBar(true);
                pro_service_listing.setLayoutManager(new LinearLayoutManager(getActivity()));
                step++;
                progress_posting.setProgress(step);


                adapter = new PostProjectServiceAndOtherListAdapter((LandScreenActivity) getActivity(), listdata, PostProjectServiceAndOtherListAdapter.TYPE_LIST, new PostProjectServiceAndOtherListAdapter.onClickItem() {
                    @Override
                    public void onSelectItemClick(int position, ProCategoryData data) {
                        if (step == 1) {
                            step++;
                            progress_posting.setProgress(step);
                            LinkedList<ProCategoryData> dataList = new LinkedList<>();
                            ProCategoryData data1 = new ProCategoryData("", "", "Repair", "");
                            ProCategoryData data2 = new ProCategoryData("", "", "Installation", "");
                            ProCategoryData data3 = new ProCategoryData("", "", "Others", "");
                            dataList.add(data1);
                            dataList.add(data2);
                            dataList.add(data3);

                            adapter.updateList(dataList);
                            selected_service_category.setVisibility(View.VISIBLE);
                            selected_service_category.setText(step1Option);
                            selected_service_property.setVisibility(View.VISIBLE);
                            selected_service_property.setText("" + step2option);


                        } else if (step == 2) {
                            step++;
                            progress_posting.setProgress(step);
                            selected_service_property.setText("" + step3option);
                            LinkedList<ProCategoryData> dataList = new LinkedList<>();
                            ProCategoryData data1 = new ProCategoryData("", "", "Single Family Home", "");
                            ProCategoryData data2 = new ProCategoryData("", "", "Condominium", "");
                            ProCategoryData data3 = new ProCategoryData("", "", "Townhome", "");
                            ProCategoryData data4 = new ProCategoryData("", "", "Multi-Family", "");
                            ProCategoryData data5 = new ProCategoryData("", "", "Commercial", "");
                            dataList.add(data1);
                            dataList.add(data2);
                            dataList.add(data3);
                            dataList.add(data4);
                            dataList.add(data5);

                            adapter.updateList(dataList);
                        } else if (step == 3) {
                            step++;
                            progress_posting.setProgress(step);
                            selected_service_property.setText("" + step4option);

                            LinkedList<ProCategoryData> dataList = new LinkedList<>();
                            ProCategoryData data1 = new ProCategoryData("", "", "Ready to hire", "");
                            ProCategoryData data2 = new ProCategoryData("", "", "Planning and Budgeting", "");
                            dataList.add(data1);
                            dataList.add(data2);
                            adapter.updateList(dataList);
                        } else if (step == 4) {
                            step++;
                            progress_posting.setProgress(step);
                            selected_service_property.setText("" + step5option);
                            LinkedList<ProCategoryData> dataList = new LinkedList<>();
                            ProCategoryData data1 = new ProCategoryData("", "", "Timing Is Flexible", "");
                            ProCategoryData data2 = new ProCategoryData("", "", "Within 1 Week", "");
                            ProCategoryData data3 = new ProCategoryData("", "", "1-2 Week", "");
                            ProCategoryData data4 = new ProCategoryData("", "", "More Than 2 Weeks", "");
                            ProCategoryData data5 = new ProCategoryData("", "", "Emergency", "");
                            dataList.add(data1);
                            dataList.add(data2);
                            dataList.add(data3);
                            dataList.add(data4);
                            dataList.add(data5);

                            adapter.updateList(dataList);
                        } else if (step == 5) {
                            step++;
                            progress_posting.setProgress(step);
                            selected_service_property.setText("" + step6option);
                            pro_service_listing.setVisibility(View.GONE);

                        }
                    }
                });
                pro_service_listing.setAdapter(adapter);
                service_request_category.setVisibility(View.VISIBLE);
            }

            @Override
            public void onError(String error) {
                if (myLoader != null && myLoader.isMyLoaderShowing())
                    myLoader.dismissLoader();

                CustomAlert customAlert = new CustomAlert(getActivity(), "Contact Us", "" + error, PostProjectFragment.this);
                customAlert.createNormalAlert("ok",1);
            }

            @Override
            public void onStartFetch() {
                myLoader.showLoader();
            }
        }, id);
    }

    public void performBack() {
        if (step < 7) {
            pro_service_listing.setVisibility(View.VISIBLE);
        } else {
            pro_service_listing.setVisibility(View.GONE);
        }
        Logger.printMessage("backAt", "" + step);

        if (step == 1) {
            step = 0;
            progress_posting.setProgress(step);
            pro_service_listing.setLayoutManager(new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL));
            pro_service_listing.setAdapter(gridAdapter);
            selected_service_category.setVisibility(View.GONE);
            service_request_category.setVisibility(View.GONE);
            selected_service_property.setVisibility(View.GONE);
            ((LandScreenActivity) getActivity()).toggleToolBar(false);

        } else if (step == 2) {


            Logger.printMessage("backAt", "selectedId -->" + selectedId);
            adapter.updateList(serviceListing);
            step--;
            progress_posting.setProgress(step);
        } else if (step == 3) {
            step--;
            progress_posting.setProgress(step);
            LinkedList<ProCategoryData> dataList = new LinkedList<>();
            ProCategoryData data1 = new ProCategoryData("", "", "Repair", "");
            ProCategoryData data2 = new ProCategoryData("", "", "Installation", "");
            ProCategoryData data3 = new ProCategoryData("", "", "Others", "");
            dataList.add(data1);
            dataList.add(data2);
            dataList.add(data3);

            adapter.updateList(dataList);
            selected_service_category.setVisibility(View.VISIBLE);
            selected_service_category.setText(step1Option);
            selected_service_property.setVisibility(View.VISIBLE);
            selected_service_property.setText("" + step2option);
        } else if (step == 4) {
            step--;
            progress_posting.setProgress(step);
            selected_service_property.setText("" + step3option);
            LinkedList<ProCategoryData> dataList = new LinkedList<>();
            ProCategoryData data1 = new ProCategoryData("", "", "Single Family Home", "");
            ProCategoryData data2 = new ProCategoryData("", "", "Condominium", "");
            ProCategoryData data3 = new ProCategoryData("", "", "Townhome", "");
            ProCategoryData data4 = new ProCategoryData("", "", "Multi-Family", "");
            ProCategoryData data5 = new ProCategoryData("", "", "Commercial", "");
            dataList.add(data1);
            dataList.add(data2);
            dataList.add(data3);
            dataList.add(data4);
            dataList.add(data5);

            adapter.updateList(dataList);
        } else if (step == 5) {
            step--;
            progress_posting.setProgress(step);
            selected_service_property.setText("" + step4option);

            LinkedList<ProCategoryData> dataList = new LinkedList<>();
            ProCategoryData data1 = new ProCategoryData("", "", "Ready to hire", "");
            ProCategoryData data2 = new ProCategoryData("", "", "Planning and Budgeting", "");
            dataList.add(data1);
            dataList.add(data2);
            adapter.updateList(dataList);
        } else if (step == 6) {
            step--;
            progress_posting.setProgress(step);
            selected_service_property.setText("" + step5option);
            LinkedList<ProCategoryData> dataList = new LinkedList<>();
            ProCategoryData data1 = new ProCategoryData("", "", "Timing Is Flexible", "");
            ProCategoryData data2 = new ProCategoryData("", "", "Within 1 Week", "");
            ProCategoryData data3 = new ProCategoryData("", "", "1-2 Week", "");
            ProCategoryData data4 = new ProCategoryData("", "", "More Than 2 Weeks", "");
            ProCategoryData data5 = new ProCategoryData("", "", "Emergency", "");
            dataList.add(data1);
            dataList.add(data2);
            dataList.add(data3);
            dataList.add(data4);
            dataList.add(data5);

            adapter.updateList(dataList);

        } else if (step == 7) {
            step--;
            progress_posting.setProgress(step);
            selected_service_property.setText("" + step6option);
            container_project_description.setVisibility(View.GONE);
        } else if (step == 8) {
            step--;
            progress_posting.setProgress(step);
            selected_service_property.setText("" + step7option);
            container_location.setVisibility(View.GONE);
        } else if (step == 9) {
            if (ProApplication.getInstance().equals("")) {
                step--;
                progress_posting.setProgress(step);
                selected_service_property.setText("" + step8option);
                container_registration.setVisibility(View.GONE);
            } else {
                step--;
                progress_posting.setProgress(step);

                selected_service_property.setText("" + step9option);
                content_post_form_submit.setVisibility(View.GONE);
            }

        } else if (step == 10) {
            step--;
            progress_posting.setProgress(step);

            selected_service_property.setText("" + step9option);
            content_post_form_submit.setVisibility(View.GONE);

        }
    }

    @Override
    public void callbackForAlert(String result, int i) {
        if (result.equalsIgnoreCase("retry") && i==1){
            listPostProject();
        }
    }
}
