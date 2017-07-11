package com.android.llc.proringer.activities;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import com.android.llc.proringer.R;
import com.android.llc.proringer.adapter.LocationListAdapter;
import com.android.llc.proringer.adapter.PostProjectGridAdapter;
import com.android.llc.proringer.adapter.PostProjectListAdapter;
import com.android.llc.proringer.adapter.SearchProListAdapter;
import com.android.llc.proringer.appconstant.ProApplication;
import com.android.llc.proringer.appconstant.ProConstant;
import com.android.llc.proringer.helper.ProServiceApiHelper;
import com.android.llc.proringer.pojo.AddressData;
import com.android.llc.proringer.pojo.ProCategoryData;
import com.android.llc.proringer.utils.ImageTakerActivityCamera;
import com.android.llc.proringer.utils.Logger;
import com.android.llc.proringer.utils.PermissionController;
import com.android.llc.proringer.viewsmod.edittext.ProLightEditText;
import com.android.llc.proringer.viewsmod.edittext.ProRegularEditText;
import com.android.llc.proringer.viewsmod.textview.ProRegularTextView;
import com.android.llc.proringer.viewsmod.textview.ProSemiBoldTextView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;

import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

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
    private RecyclerView pro_service_listing, location_list;
    private LinearLayout content_post_form_submit, container_registration, container_project_description;
    private RelativeLayout container_location, container_add_photoes;
    private ProgressDialog pgDialog = null;
    private PostProjectListAdapter adapter = null;
    private PostProjectGridAdapter gridAdapter = null;
    private boolean zipSearchGoing = false;
    private LinkedList<ProCategoryData> serviceListing = null;
    private int step = 0;
    private String selectedId = "", step1Option = "", serviceId = "", service_look_type = "", property_type = "", project_stage = "", timeframe_id = "",
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
    private String mCurrentPhotoPath = "";
    private ImageView image_pager;
    private ProRegularEditText project_description_text, zip_code_text;
    public AddressData selectedAddressData = null;
    private Object lock = new Object();
    private InputMethodManager keyboard;
    private LocationListAdapter zip_search_adapter = null;
    private ProLightEditText first_name, last_name, email, password, confirm_password, zip_code;


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
        keyboard = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
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

        location_list = (RecyclerView) findViewById(R.id.location_list);
        location_list.setLayoutManager(new LinearLayoutManager(ActivityPostProject.this));

        pro_service_listing = (RecyclerView) findViewById(R.id.pro_service_listing);
        pro_service_listing.setLayoutManager(new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL));

        content_post_form_submit = (LinearLayout) findViewById(R.id.content_post_form_submit);
        container_registration = (LinearLayout) findViewById(R.id.container_registration);
        container_project_description = (LinearLayout) findViewById(R.id.container_project_description);
        container_location = (RelativeLayout) findViewById(R.id.container_location);
        container_add_photoes = (RelativeLayout) findViewById(R.id.container_add_photoes);

        first_name = (ProLightEditText) findViewById(R.id.first_name);
        last_name = (ProLightEditText) findViewById(R.id.last_name);
        email = (ProLightEditText) findViewById(R.id.email);
        password = (ProLightEditText) findViewById(R.id.password);
        confirm_password = (ProLightEditText) findViewById(R.id.confirm_password);
        zip_code = (ProLightEditText) findViewById(R.id.zip_code);


        image_pager = (ImageView) findViewById(R.id.image_pager);
        project_description_text = (ProRegularEditText) findViewById(R.id.project_description_text);
        zip_code_text = (ProRegularEditText) findViewById(R.id.zip_code_text);


        if (ProApplication.getInstance().equals("")) {
            progress_posting.setMax(10);
        } else {
            progress_posting.setMax(9);
        }

        findViewById(R.id.add_photo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActivityPostProject.this, PermissionController.class);
                intent.setAction(PermissionController.ACTION_READ_STORAGE_PERMISSION);
                startActivityForResult(intent, 200);
            }
        });


        zip_code_text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                synchronized (lock) {
                    searchLocationWithZip(s.toString());
                }

            }
        });

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
                if (project_description_text.getText().toString().trim().equals("")) {
                    project_description_text.setError("Please enter project description.");
                } else {
                    step++;
                    progress_posting.setProgress(step);
                    selected_service_property.setText("" + step8option);
                    container_location.setVisibility(View.VISIBLE);
                }

            }
        });
        findViewById(R.id.continue_location_section).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedAddressData == null) {
                    zip_code_text.setError("Please enter a valid zip code to continue.");
                } else {
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

            }
        });
        findViewById(R.id.register).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateRegistration()) {

                }

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
                            serviceId = data.getId();
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
                            service_look_type = data.getCategory_name();
                            progress_posting.setProgress(step);
                            selected_service_property.setText("" + step3option);
                            LinkedList<ProCategoryData> dataList = new LinkedList<>();
                            ProCategoryData data1 = new ProCategoryData("4", "", "Single Family Home", "");
                            ProCategoryData data2 = new ProCategoryData("5", "", "Condominium", "");
                            ProCategoryData data3 = new ProCategoryData("6", "", "Townhome", "");
                            ProCategoryData data4 = new ProCategoryData("7", "", "Multi-Family", "");
                            ProCategoryData data5 = new ProCategoryData("1", "", "Commercial", "");
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

                            property_type = data.getId();

                            LinkedList<ProCategoryData> dataList = new LinkedList<>();
                            ProCategoryData data1 = new ProCategoryData("", "", "Ready to hire", "");
                            ProCategoryData data2 = new ProCategoryData("", "", "Planning and Budgeting", "");
                            dataList.add(data1);
                            dataList.add(data2);
                            adapter.updateList(dataList);
                        } else if (step == 4) {
                            step++;
                            project_stage = data.getCategory_name();

                            progress_posting.setProgress(step);
                            selected_service_property.setText("" + step5option);
                            LinkedList<ProCategoryData> dataList = new LinkedList<>();
                            ProCategoryData data1 = new ProCategoryData("1", "", "Timing Is Flexible", "");
                            ProCategoryData data2 = new ProCategoryData("2", "", "Within 1 Week", "");
                            ProCategoryData data3 = new ProCategoryData("3", "", "1-2 Week", "");
                            ProCategoryData data4 = new ProCategoryData("4", "", "More Than 2 Weeks", "");
                            ProCategoryData data5 = new ProCategoryData("5", "", "Emergency", "");
                            dataList.add(data1);
                            dataList.add(data2);
                            dataList.add(data3);
                            dataList.add(data4);
                            dataList.add(data5);

                            adapter.updateList(dataList);
                        } else if (step == 5) {
                            step++;
                            timeframe_id = data.getId();
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
            if (!ProApplication.getInstance().equals("")) {
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


    private void showImagePickerOption() {
        new AlertDialog.Builder(ActivityPostProject.this)
                .setCancelable(true)
                .setTitle("Property image")
                .setMessage("please choose image source type.")
                .setPositiveButton("Gallery", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        Intent intent = new Intent(Intent.ACTION_PICK);
                        if (intent.resolveActivity(getPackageManager()) != null) {
                            intent.setType("image/*");
                            startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);

                        }
                    }
                })
                .setNegativeButton("Camera", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        ProConstant.cameraRequested = true;
                        startActivityForResult(new Intent(ActivityPostProject.this, ImageTakerActivityCamera.class), REQUEST_IMAGE_CAPTURE);
                    }
                })
                .create()
                .show();
    }

    @Override
    public void onActivityResult(final int requestCode, int resultCode, final Intent data) {
        try {
            Logger.printMessage("resultCode", "requestCode " + requestCode + " &b resultcode :: " + resultCode);
            if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (data != null) {
                            mCurrentPhotoPath = data.getExtras().get("data").toString();
                            Logger.printMessage("image****", "" + mCurrentPhotoPath);
                            Glide.with(ActivityPostProject.this).load("file://" + mCurrentPhotoPath).into(new GlideDrawableImageViewTarget(image_pager) {
                                @Override
                                public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> animation) {
                                    super.onResourceReady(resource, animation);
                                }
                            });
                        }
                    }
                }, 800);
            } else if (requestCode == PICK_IMAGE && resultCode == RESULT_OK) {
                Logger.printMessage("image****", "" + data.getData());
                try {
                    Uri uri = data.getData();
                    File dataFile = new File(getRealPathFromURI(uri));
                    if (!dataFile.exists())
                        Logger.printMessage("image****", "data file does not exists");
                    mCurrentPhotoPath = dataFile.getAbsolutePath();
                    Glide.with(ActivityPostProject.this).load(uri).fitCenter().into(new GlideDrawableImageViewTarget(image_pager) {
                        /**
                         * {@inheritDoc}
                         * If no {@link GlideAnimation} is given or if the animation does not set the
                         * {@link Drawable} on the view, the drawable is set using
                         * {@link ImageView#setImageDrawable(Drawable)}.
                         *
                         * @param resource  {@inheritDoc}
                         * @param animation {@inheritDoc}
                         */
                        @Override
                        public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> animation) {
                            super.onResourceReady(resource, animation);
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else if (requestCode == 200 && resultCode == RESULT_OK) {
                showImagePickerOption();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    //file uri to real location in filesystem
    public String getRealPathFromURI(Uri contentURI) {
        Cursor cursor = ActivityPostProject.this.getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) {
            // Source is Dropbox or other similar local file path
            return contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            return cursor.getString(idx);
        }
    }


    private void searchLocationWithZip(String key) {
        ProServiceApiHelper.getInstance(ActivityPostProject.this).getSearchArea(key, new ProServiceApiHelper.onSearchZipCallback() {
            @Override
            public void onComplete(List<AddressData> listdata) {
                zipSearchGoing = false;
                if (zip_search_adapter == null) {
                    zip_search_adapter = new LocationListAdapter(ActivityPostProject.this, listdata, new LocationListAdapter.onItemelcted() {
                        @Override
                        public void onSelect(int pos, AddressData data) {
                            selectedAddressData = data;
                        }
                    });

                    location_list.setAdapter(zip_search_adapter);
                } else {
                    zip_search_adapter.updateData(listdata);
                }
            }

            @Override
            public void onError(String error) {

            }

            @Override
            public void onStartFetch() {

            }
        });
    }
    private boolean validateRegistration() {
        if (first_name.getText().toString().trim().equals("")) {
            first_name.setError("Please enter First name.");
            return false;
        } else {
            if (last_name.getText().toString().trim().equals("")) {
                last_name.setError("Please enter Last name.");
                return false;
            } else {
                if (email.getText().toString().trim().equals("")) {
                    email.setError("Please enter Email.");
                    return false;

                } else {
                    if (Patterns.EMAIL_ADDRESS.matcher(email.getText().toString().trim()).matches()) {
                        if (password.getText().toString().trim().equals("")) {
                            password.setError("Please enter Password.");
                            return false;

                        } else {
                            if (confirm_password.getText().toString().trim().equals("")) {
                                confirm_password.setError("Please enter Confirm password.");
                                return false;

                            } else {

                                if (confirm_password.getText().toString().trim().equals(password.getText().toString().trim())) {

                                    if (confirm_password.getText().toString().trim().length() > 6 &&
                                            confirm_password.getText().toString().trim().matches(".*[^0-9].*")) {
                                        if (zip_code.getText().toString().trim().equals("")) {
                                            zip_code.setError("Please enter Zip.");
                                            return false;
                                        } else {
                                            completePostProject();
                                            return true;
                                        }
                                    } else {

                                        if (confirm_password.getText().toString().trim().length() < 6) {
                                            confirm_password.setError("Password should contains at least 6 character.");
                                        } else if (!confirm_password.getText().toString().trim().matches(".*[^0-9].*")) {
                                            confirm_password.setError("Password should combination of Alphanumeric and Number.");
                                        }
                                        return false;
                                    }


                                } else {
                                    confirm_password.setError("Password and Confirm password does not matched.");
                                    return false;
                                }
                            }
                        }
                    } else {
                        email.setError("Please enter valid Email address.");
                        return false;

                    }
                }
            }
        }
    }

    private void completePostProject() {
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
                        try {

                            keyboard.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }


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
                project_description_text.getText().toString().trim(),
                selectedAddressData.getZip_code(),
                selectedAddressData.getCity(),
                selectedAddressData.getState_code(),
                selectedAddressData.getCountry_code(),
                selectedAddressData.getLatitude() + "",
                selectedAddressData.getLongitude() + "",
                first_name.getText().toString().trim(),
                last_name.getText().toString().trim(),
                email.getText().toString().trim(),
                confirm_password.getText().toString().trim()
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

    private void showErrorDialog(String title, String message) {
        new AlertDialog.Builder(ActivityPostProject.this)
                .setTitle("" + title)
                .setMessage("" + message)
                .setCancelable(false)
                .setPositiveButton("retry", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        validateRegistration();
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


}
