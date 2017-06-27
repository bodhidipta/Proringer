package com.android.llc.proringer.activities;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.android.llc.proringer.R;
import com.android.llc.proringer.adapter.PostProjectGridAdapter;
import com.android.llc.proringer.adapter.PostProjectListAdapter;
import com.android.llc.proringer.appconstant.ProApplication;
import com.android.llc.proringer.helper.ProServiceApiHelper;
import com.android.llc.proringer.pojo.ProCategoryData;
import com.android.llc.proringer.utils.Logger;
import com.android.llc.proringer.viewsmod.textview.ProRegularTextView;

import java.util.LinkedList;

/**
 * Created by bodhidipta on 20/06/17.
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

public class ActivityPostProject extends AppCompatActivity {
    private ProgressBar progress_posting;
    private ProRegularTextView selected_service_category, service_request_category, pro_request_category, selected_service_property;
    private RecyclerView pro_service_listing;
    private LinearLayout  content_post_form_submit,container_registration, container_project_description;
    private RelativeLayout container_location, container_add_photoes;
    private ProgressDialog pgDialog = null;
    private PostProjectListAdapter adapter = null;
    private PostProjectGridAdapter gridAdapter = null;
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
    private ImageView header_icon = null;
    private ProRegularTextView header_text = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_project_get_started);
        findViewById(R.id.back_header).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performBack();
            }
        });
        findViewById(R.id.home).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        header_icon = (ImageView) findViewById(R.id.header_icon);
        header_text = (ProRegularTextView) findViewById(R.id.header_text);

        progress_posting = (ProgressBar) findViewById(R.id.progress_posting);
        selected_service_category = (ProRegularTextView) findViewById(R.id.selected_service_category);
        service_request_category = (ProRegularTextView) findViewById(R.id.service_request_category);
        pro_request_category = (ProRegularTextView) findViewById(R.id.pro_request_category);
        selected_service_property = (ProRegularTextView) findViewById(R.id.selected_service_property);

        pro_service_listing = (RecyclerView) findViewById(R.id.pro_service_listing);
        pro_service_listing.setLayoutManager(new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL));

        content_post_form_submit = (LinearLayout) findViewById(R.id.content_post_form_submit);
        container_registration = (LinearLayout) findViewById(R.id.container_registration);
        container_project_description = (LinearLayout) findViewById(R.id.container_project_description);
        container_location = (RelativeLayout) findViewById(R.id.container_location);
        container_add_photoes = (RelativeLayout) findViewById(R.id.container_add_photoes);

        if (ProApplication.getInstance().equals("")) {
            progress_posting.setMax(10);
        } else {
            progress_posting.setMax(9);
        }


        ProServiceApiHelper.getInstance(ActivityPostProject.this).getCategoryList(new ProServiceApiHelper.onProCategoryListener() {
            @Override
            public void onComplete(LinkedList<ProCategoryData> listdata) {
                if (pgDialog != null && pgDialog.isShowing())
                    pgDialog.dismiss();
                progress_posting.setProgress(step);
                gridAdapter = new PostProjectGridAdapter(ActivityPostProject.this, listdata, new PostProjectGridAdapter.onClickItem() {
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
                if (pgDialog != null && pgDialog.isShowing())
                    pgDialog.dismiss();

                new AlertDialog.Builder(ActivityPostProject.this)
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
                pgDialog = new ProgressDialog(ActivityPostProject.this);
                pgDialog.setTitle("Preparing category");
                pgDialog.setMessage("Please wait while preparing category list.");
                pgDialog.setCancelable(false);
                pgDialog.show();

            }
        });

        findViewById(R.id.continue_image_section).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                step++;
                progress_posting.setProgress(step);
                selected_service_property.setText("" + step7option);
                container_project_description.setVisibility(View.VISIBLE);

            }
        });
        findViewById(R.id.continue_project_describing).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                step++;
                progress_posting.setProgress(step);
                selected_service_property.setText("" + step8option);
                container_location.setVisibility(View.VISIBLE);
            }
        });
        findViewById(R.id.continue_location_section).setOnClickListener(new View.OnClickListener() {
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
                    findViewById(R.id.back_header).setVisibility(View.GONE);
                    step++;
                    progress_posting.setProgress(step);
                    selected_service_property.setText("" + step10option);
                    content_post_form_submit.setVisibility(View.VISIBLE);
                    Logger.printMessage("@steps", "" + step);
                }
            }
        });
        findViewById(R.id.register).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                step++;
                progress_posting.setProgress(step);
                selected_service_property.setText("" + step10option);
                content_post_form_submit.setVisibility(View.VISIBLE);
                findViewById(R.id.back_header).setVisibility(View.GONE);
            }
        });
        findViewById(R.id.close_project).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /**
                 * on close redirect flow to get started screen
                 */
                finish();
            }
        });
    }

    private void selectService(String id) {
        ProServiceApiHelper.getInstance(ActivityPostProject.this).getServiceList(new ProServiceApiHelper.onProCategoryListener() {
            @Override
            public void onComplete(LinkedList<ProCategoryData> listdata) {
                if (pgDialog != null && pgDialog.isShowing())
                    pgDialog.dismiss();
                serviceListing = listdata;

                header_icon.setVisibility(View.GONE);
                header_text.setVisibility(View.VISIBLE);
                header_text.setText(step1Option);
//                ((LandScreenActivity) ActivityPostProject.this).toggleToolBar(true);
                pro_service_listing.setLayoutManager(new LinearLayoutManager(ActivityPostProject.this));
                step++;
                progress_posting.setProgress(step);


                adapter = new PostProjectListAdapter(ActivityPostProject.this, listdata, PostProjectListAdapter.TYPE_LIST, new PostProjectListAdapter.onClickItem() {
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
                if (pgDialog != null && pgDialog.isShowing())
                    pgDialog.dismiss();

                new AlertDialog.Builder(ActivityPostProject.this)
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
                pgDialog = new ProgressDialog(ActivityPostProject.this);
                pgDialog.setTitle("Preparing category");
                pgDialog.setMessage("Please wait while preparing category list.");
                pgDialog.setCancelable(false);
                pgDialog.show();

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
//            ((LandScreenActivity) ActivityPostProject.this).toggleToolBar(false);
            header_text.setVisibility(View.GONE);
            header_icon.setVisibility(View.VISIBLE);

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

        } else if (step == 0) {
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        performBack();
    }
}
