package com.android.llc.proringer.activities;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.android.llc.proringer.R;
import com.android.llc.proringer.appconstant.ProApplication;
import com.android.llc.proringer.fragments.postProject.CateGoryList;
import com.android.llc.proringer.fragments.postProject.PostProjectContainDescription;
import com.android.llc.proringer.fragments.postProject.PostProjectRegistrationandFinalize;
import com.android.llc.proringer.fragments.postProject.PostProjectSelectImage;
import com.android.llc.proringer.fragments.postProject.SearchLocation;
import com.android.llc.proringer.fragments.postProject.ServiceAndOtherList;
import com.android.llc.proringer.helper.ProServiceApiHelper;
import com.android.llc.proringer.pojo.AddressData;
import com.android.llc.proringer.pojo.ProCategoryData;
import com.android.llc.proringer.utils.Logger;
import com.android.llc.proringer.viewsmod.textview.ProRegularTextView;


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

    public FragmentManager fragmentManager = null;

    private ProgressBar progress_posting;
    private ProRegularTextView selected_service_category, selected_service_property;
    private LinearLayout content_post_form_submit;
    private ProgressDialog pgDialog = null;

    private int step = 0;
    private String selectedId = "", step1Option = "", serviceId = "",
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
    private static final int REQUEST_IMAGE_CAPTURE = 5;
    private static final int PICK_IMAGE = 3;
    public String mCurrentPhotoPath = "";
    private ImageView image_pager;
    public String project_description_text = "";
    public AddressData selectedAddressData = null;
    private Object lock = new Object();
    private InputMethodManager keyboard;

    public String first_name, last_name, email, password, confirm_password;

    /**
     * @param savedInstanceState
     */


    public ProCategoryData selectedCategory;
    public ProCategoryData selectedService;
    public String service_look_type;
    public String property_type;
    public String project_stage;
    public String timeframe_id;
    private int progressStep = 0;

    LinearLayout title_lebel_container;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_project_get_started);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        keyboard = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        findViewById(R.id.home).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        title_lebel_container = (LinearLayout) findViewById(R.id.title_lebel_container);

        header_icon = (ImageView) findViewById(R.id.header_icon);
        header_text = (ProRegularTextView) findViewById(R.id.header_text);

        progress_posting = (ProgressBar) findViewById(R.id.progress_posting);
        selected_service_category = (ProRegularTextView) findViewById(R.id.selected_service_category);
        selected_service_property = (ProRegularTextView) findViewById(R.id.selected_service_property);

        content_post_form_submit = (LinearLayout) findViewById(R.id.content_post_form_submit);


        image_pager = (ImageView) findViewById(R.id.image_pager);


        if (ProApplication.getInstance().equals("")) {
            progress_posting.setMax(10);
        } else {
            progress_posting.setMax(9);
        }

        fragmentManager = getSupportFragmentManager();

        changeFragmentNext(1);


        ///////////////////KLMNGBKLMBVKMLBNVKLMBNKLMBNMKLMNBBNKLHMNBMK,L
//        findViewById(R.id.add_photo).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(ActivityPostProject.this, PermissionController.class);
//                intent.setAction(PermissionController.ACTION_READ_STORAGE_PERMISSION);
//                startActivityForResult(intent, 200);
//            }
//        });


//        ProServiceApiHelper.getInstance(ActivityPostProject.this).getCategoryList(new ProServiceApiHelper.onProCategoryListener() {
//            @Override
//            public void onComplete(LinkedList<ProCategoryData> listdata) {
//
//                if (pgDialog != null && pgDialog.isShowing())
//                    pgDialog.dismiss();
//                progress_posting.setProgress(step);
//
//                gridAdapter = new PostProjectGridAdapter(ActivityPostProject.this, listdata, new PostProjectGridAdapter.onClickItem() {
//                    @Override
//                    public void onSelectItemClick(int position, ProCategoryData data) {
//                        if (step == 0) {
//                            step1Option = data.getCategory_name();
//                            selectedId = data.getId();
//                            selectService(data.getId());
//                        }
//                    }
//                });
//                pro_service_listing.setAdapter(gridAdapter);
//
//            }
//
//            @Override
//            public void onError(String error) {
//                if (pgDialog != null && pgDialog.isShowing())
//                    pgDialog.dismiss();
//
//                new AlertDialog.Builder(ActivityPostProject.this)
//                        .setTitle("Error")
//                        .setMessage("" + error)
//                        .setCancelable(false)
//                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                dialog.dismiss();
//                            }
//                        })
//                        .show();
//
//            }
//
//            @Override
//            public void onStartFetch() {
//                pgDialog = new ProgressDialog(ActivityPostProject.this);
//                pgDialog.setTitle("Preparing category");
//                pgDialog.setMessage("Please wait while preparing category list.");
//                pgDialog.setCancelable(false);
//                pgDialog.show();
//
//            }
//        });

//        findViewById(R.id.continue_image_section).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                step++;
//                progress_posting.setProgress(step);
//                selected_service_property.setText("" + step7option);
//                container_project_description.setVisibility(View.VISIBLE);
//
//            }
//        });


//        findViewById(R.id.close_project).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                /**
//                 * on close redirect flow to get started screen
//                 */
//                finish();
//            }
//        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            performBack();
        return super.onOptionsItemSelected(item);
    }


    public void performBack() {
        if (progressStep == 1 || progressStep == 2 || progressStep == 3 || progressStep == 4 || progressStep == 5) {
            decreaseStep();
        } else if (progressStep==6||progressStep==7||progressStep==8){
            closeKeypad();
            if (getFragmentManager().getBackStackEntryCount() > 0) {
                getFragmentManager().popBackStack();
                decreaseStep();
            }
        }
        else if(progressStep==0){
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        performBack();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }


    private void showErrorDialog(String title, String message) {
        new AlertDialog.Builder(ActivityPostProject.this)
                .setTitle("" + title)
                .setMessage("" + message)
                .setCancelable(false)
                .setPositiveButton("retry", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("abort", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();
    }

    public void completePostProject() {
        ProServiceApiHelper.getInstance(ActivityPostProject.this).postProject(
                new ProServiceApiHelper.getApiProcessCallback() {
                    @Override
                    public void onStart() {
                        pgDialog = new ProgressDialog(ActivityPostProject.this);
                        pgDialog.setTitle("Post Project");
                        pgDialog.setMessage("Please wait ..");
                        pgDialog.setCancelable(false);
                        pgDialog.show();

                    }

                    @Override
                    public void onComplete(String message) {
                        if (pgDialog != null && pgDialog.isShowing())
                            pgDialog.dismiss();

                        step++;
                        progress_posting.setProgress(step);
                        selected_service_property.setText("" + step10option);
                        content_post_form_submit.setVisibility(View.VISIBLE);
                        findViewById(R.id.back_header).setVisibility(View.GONE);

                        closeKeypad();

                    }

                    @Override
                    public void onError(String error) {
                        if (pgDialog != null && pgDialog.isShowing())
                            pgDialog.dismiss();
                        showErrorDialog("Project post error", "" + error);
                    }
                },
                selectedId,
                serviceId,
                service_look_type,
                property_type,
                project_stage,
                timeframe_id,
                mCurrentPhotoPath,
                project_description_text,
                selectedAddressData.getZip_code(),
                selectedAddressData.getCity(),
                selectedAddressData.getState_code(),
                selectedAddressData.getCountry_code(),
                selectedAddressData.getLatitude() + "",
                selectedAddressData.getLongitude() + "",
                first_name,
                last_name,
                email,
                confirm_password
        );
        /**
         * .add("cat_id", params[0])
         .add("service_id", params[1])
         .add("service_look_type", params[2])
         .add("property_type", params[3])
         .add("project_stage", params[4])
         .add("timeframe_id", params[5])
         .add("project_image", params[6])
         .add("project_details", params[7])
         .add("project_zipcode", params[8])
         .add("city", params[9])
         .add("state", params[10])
         .add("country", params[11])
         .add("latitude", params[12])
         .add("longitude", params[13])
         .add("f_name", params[14])
         .add("l_name", params[15])
         .add("email_id", params[16])
         .add("password", params[17])
         */
    }

    public void setHeaderCategory() {
        header_icon.setVisibility(View.GONE);
        header_text.setVisibility(View.VISIBLE);
        header_text.setText(selectedCategory.getCategory_name());
    }

    public void changeFragmentNext(int next) {
        switch (next) {
            case 1:

                FragmentTransaction transaction1 = fragmentManager.beginTransaction();
                transaction1.add(R.id.container_fragment, new CateGoryList(), "" + CateGoryList.class.getCanonicalName());
                transaction1.addToBackStack("" + CateGoryList.class.getCanonicalName());
                transaction1.commit();
                Logger.printMessage("Tag_frg", "" + getSupportFragmentManager().getBackStackEntryCount());

                break;

            case 2:

                FragmentTransaction transaction2 = fragmentManager.beginTransaction();
                transaction2.add(R.id.container_fragment, new ServiceAndOtherList(), "" + ServiceAndOtherList.class.getCanonicalName());
                transaction2.addToBackStack("" + ServiceAndOtherList.class.getCanonicalName());
                transaction2.commit();
                Logger.printMessage("Tag_frg", "" + getSupportFragmentManager().getBackStackEntryCount());

                break;

            case 3:

                FragmentTransaction transaction3 = fragmentManager.beginTransaction();
                transaction3.add(R.id.container_fragment, new PostProjectSelectImage(), "" + PostProjectSelectImage.class.getCanonicalName());
                transaction3.addToBackStack("" + PostProjectSelectImage.class.getCanonicalName());
                transaction3.commit();
                Logger.printMessage("Tag_frg", "" + getSupportFragmentManager().getBackStackEntryCount());


                break;


            case 4:
                FragmentTransaction transaction4 = fragmentManager.beginTransaction();
                transaction4.add(R.id.container_fragment, new PostProjectContainDescription(), "" + PostProjectContainDescription.class.getCanonicalName());
                transaction4.addToBackStack("" + PostProjectContainDescription.class.getCanonicalName());
                transaction4.commit();
                Logger.printMessage("Tag_frg", "" + getSupportFragmentManager().getBackStackEntryCount());

                break;


            case 5:

                FragmentTransaction transaction5 = fragmentManager.beginTransaction();
                transaction5.add(R.id.container_fragment, new SearchLocation(), "" + SearchLocation.class.getCanonicalName());
                transaction5.addToBackStack("" + SearchLocation.class.getCanonicalName());
                transaction5.commit();
                Logger.printMessage("Tag_frg", "" + getSupportFragmentManager().getBackStackEntryCount());

                break;

            case 6:

                FragmentTransaction transaction6 = fragmentManager.beginTransaction();
                transaction6.add(R.id.container_fragment, new PostProjectRegistrationandFinalize(), "" + PostProjectRegistrationandFinalize.class.getCanonicalName());
                transaction6.addToBackStack("" + PostProjectRegistrationandFinalize.class.getCanonicalName());
                transaction6.commit();
                Logger.printMessage("Tag_frg", "" + getSupportFragmentManager().getBackStackEntryCount());

                break;
        }
    }

    public void increaseStep() {
        progressStep++;
        progress_posting.setProgress(progressStep);

        if (progressStep == 1) {
            selected_service_category.setVisibility(View.VISIBLE);
            selected_service_category.setText(selectedCategory.getCategory_name());
            selected_service_property.setVisibility(View.VISIBLE);
            selected_service_property.setText("" + step2option);
        } else if (progressStep == 2) {
            selected_service_property.setText("" + step3option);
        } else if (progressStep == 3) {
            selected_service_property.setText("" + step4option);
        } else if (progressStep == 4) {
            selected_service_property.setText("" + step5option);

        } else if (progressStep == 5) {
            selected_service_property.setText("" + step6option);
        } else if (progressStep == 6) {
            selected_service_property.setText("" + step7option);
        } else if (progressStep == 7) {
            selected_service_property.setText("" + step8option);
        } else if (progressStep == 8) {
            selected_service_property.setText("" + step9option);
        } else if (progressStep == 9) {
            selected_service_property.setText("" + step10option);
        }
    }

    public void decreaseStep() {
        progressStep--;
        progress_posting.setProgress(progressStep);

        if (progressStep == 0) {
            selected_service_category.setVisibility(View.GONE);
            selected_service_category.setText(selectedCategory.getCategory_name());
            selected_service_property.setVisibility(View.GONE);
        } else if (progressStep == 1) {
            selected_service_property.setText("" + step2option);
        } else if (progressStep == 2) {
            selected_service_property.setText("" + step3option);
        } else if (progressStep == 3) {
            selected_service_property.setText("" + step4option);
        } else if (progressStep == 4) {
            selected_service_property.setText("" + step5option);
        } else if (progressStep == 5) {
            selected_service_property.setText("" + step6option);
        } else if (progressStep == 6) {
            selected_service_property.setText("" + step7option);
        } else if (progressStep == 7) {
            selected_service_property.setText("" + step8option);
        } else if (progressStep == 8) {
            selected_service_property.setText("" + step9option);
        }
    }

    public void closeKeypad(){
        try {
            keyboard.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
