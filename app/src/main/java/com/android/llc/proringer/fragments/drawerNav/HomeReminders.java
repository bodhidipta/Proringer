package com.android.llc.proringer.fragments.drawerNav;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import com.android.llc.proringer.R;
import com.android.llc.proringer.adapter.SpinnerAdapter;
import com.android.llc.proringer.appconstant.ProConstant;
import com.android.llc.proringer.helper.BottomSheetGlobalList;
import com.android.llc.proringer.helper.ProServiceApiHelper;
import com.android.llc.proringer.utils.ImageTakerActivityCamera;
import com.android.llc.proringer.utils.Logger;
import com.android.llc.proringer.utils.PermissionController;
import com.android.llc.proringer.viewsmod.textview.ProRegularTextView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;

import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import static android.app.Activity.RESULT_OK;


/**
 * Created by bodhidipta on 22/06/17.
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

public class HomeReminders extends Fragment {

    private HashMap<String, ArrayList<String>> idList;
    private HashMap<String, ArrayList<String>> valueList;
    private ProgressDialog pgDial = null;
    private ProRegularTextView year_build, ac_filter, last_water_heater_flush,
            last_chimney_sweep, last_test_for_radon_gas, Smoke_detector_battery,
            co_detector_battery, last_gutter_clean;

    private ProRegularTextView add_propert_image, update_data;
    private ImageView property_image;
    private static final int REQUEST_IMAGE_CAPTURE = 5;
    private static final int PICK_IMAGE = 3;
    String mCurrentPhotoPath = "";

    String selected_sq_ft_id="0",selected_graage_id="0",selected_prototype_id="0",selected_basement_id="0",selected_lot_size_id="0",
            selected_age_of_roof_id="0",selected_age_of_ac_id="0",selected_age_of_furance_id="0",selected_age_water_heater_id="0",selected_age_window_id="0";

    RelativeLayout RLsq_ft,RLGraage,RLPrototype,RLBasement,RLLot_size,RLAge_of_roof,RLAge_of_ac,RLAge_of_furance,RLAge_water_heater,RLAge_window;
    ProRegularTextView tv_sq_ft,tv_graage,tv_prototype,tv_basement,tv_lot_size,tv_age_of_roof,tv_age_of_ac,tv_age_of_furance,tv_age_water_heater,tv_age_window;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home_reminder, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        RLsq_ft = (RelativeLayout) view.findViewById(R.id.RLsq_ft);
        RLGraage = (RelativeLayout) view.findViewById(R.id.RLGraage);
        RLPrototype = (RelativeLayout) view.findViewById(R.id.RLPrototype);
        RLBasement = (RelativeLayout) view.findViewById(R.id.RLBasement);
        RLLot_size = (RelativeLayout) view.findViewById(R.id.RLLot_size);
        RLAge_of_roof = (RelativeLayout) view.findViewById(R.id.RLAge_of_roof);
        RLAge_of_ac = (RelativeLayout) view.findViewById(R.id.RLAge_of_ac);
        RLAge_of_furance = (RelativeLayout) view.findViewById(R.id.RLAge_of_furance);
        RLAge_water_heater = (RelativeLayout) view.findViewById(R.id.RLAge_water_heater);
        RLAge_window = (RelativeLayout) view.findViewById(R.id.RLAge_window);

        tv_sq_ft= (ProRegularTextView) view.findViewById(R.id.tv_sq_ft);
        tv_graage= (ProRegularTextView) view.findViewById(R.id.tv_graage);
        tv_prototype= (ProRegularTextView) view.findViewById(R.id.tv_prototype);
        tv_basement= (ProRegularTextView) view.findViewById(R.id.tv_basement);
        tv_lot_size= (ProRegularTextView) view.findViewById(R.id.tv_lot_size);
        tv_age_of_roof= (ProRegularTextView) view.findViewById(R.id.tv_age_of_roof);
        tv_age_of_ac= (ProRegularTextView) view.findViewById(R.id.tv_age_of_ac);
        tv_age_of_furance= (ProRegularTextView) view.findViewById(R.id.tv_age_of_furance);
        tv_age_water_heater= (ProRegularTextView) view.findViewById(R.id.tv_age_water_heater);
        tv_age_window= (ProRegularTextView) view.findViewById(R.id.tv_age_window);


        year_build = (ProRegularTextView) view.findViewById(R.id.year_build);
        ac_filter = (ProRegularTextView) view.findViewById(R.id.ac_filter);
        last_water_heater_flush = (ProRegularTextView) view.findViewById(R.id.last_water_heater_flush);
        last_chimney_sweep = (ProRegularTextView) view.findViewById(R.id.last_chimney_sweep);
        last_test_for_radon_gas = (ProRegularTextView) view.findViewById(R.id.last_test_for_radon_gas);
        Smoke_detector_battery = (ProRegularTextView) view.findViewById(R.id.Smoke_detector_battery);
        co_detector_battery = (ProRegularTextView) view.findViewById(R.id.co_detector_battery);
        last_gutter_clean = (ProRegularTextView) view.findViewById(R.id.last_gutter_clean);

        add_propert_image = (ProRegularTextView) view.findViewById(R.id.add_propert_image);
        update_data = (ProRegularTextView) view.findViewById(R.id.update_data);

        property_image = (ImageView) view.findViewById(R.id.property_image);

        add_propert_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), PermissionController.class);
                intent.setAction(PermissionController.ACTION_READ_STORAGE_PERMISSION);
                startActivityForResult(intent, 200);
            }
        });
        getOptionList();

        update_data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateOptions();
            }
        });

        year_build.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View dialoglayout = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_date_picker, null);
                final DatePicker dPicker = (DatePicker) dialoglayout.findViewById(R.id.date_picker);
                dPicker.setMaxDate(Calendar.getInstance().getTimeInMillis());
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());


                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        try {
                            Date date = new SimpleDateFormat("dd/MM/yyyy").parse("" + dPicker.getDayOfMonth() + "/" + (dPicker.getMonth() + 1) + "/" + dPicker.getYear());
                            year_build.setText(new SimpleDateFormat("yyyy").format(date));


                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        // info_field_mowing_ermin.setText("" + getString(R.string.no_date));

                        year_build.setText("");

                    }
                });
                builder.setView(dialoglayout);
                builder.show();
            }
        });

        ac_filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View dialoglayout = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_date_picker, null);
                final DatePicker dPicker = (DatePicker) dialoglayout.findViewById(R.id.date_picker);
                dPicker.setMaxDate(Calendar.getInstance().getTimeInMillis());
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());


                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        try {
                            Date date = new SimpleDateFormat("dd/MM/yyyy").parse("" + dPicker.getDayOfMonth() + "/" + (dPicker.getMonth() + 1) + "/" + dPicker.getYear());
                            ac_filter.setText(new SimpleDateFormat("dd/MM/yyyy").format(date));

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        // info_field_mowing_ermin.setText("" + getString(R.string.no_date));

                        ac_filter.setText("");

                    }
                });
                builder.setView(dialoglayout);
                builder.show();
            }
        });

        last_water_heater_flush.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View dialoglayout = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_date_picker, null);
                final DatePicker dPicker = (DatePicker) dialoglayout.findViewById(R.id.date_picker);
                dPicker.setMaxDate(Calendar.getInstance().getTimeInMillis());
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());


                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        try {
                            Date date = new SimpleDateFormat("dd/MM/yyyy").parse("" + dPicker.getDayOfMonth() + "/" + (dPicker.getMonth() + 1) + "/" + dPicker.getYear());
                            last_water_heater_flush.setText(new SimpleDateFormat("dd/MM/yyyy").format(date));

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        // info_field_mowing_ermin.setText("" + getString(R.string.no_date));

                        last_water_heater_flush.setText("");

                    }
                });
                builder.setView(dialoglayout);
                builder.show();
            }
        });
        last_chimney_sweep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View dialoglayout = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_date_picker, null);
                final DatePicker dPicker = (DatePicker) dialoglayout.findViewById(R.id.date_picker);
                dPicker.setMaxDate(Calendar.getInstance().getTimeInMillis());
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());


                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        try {
                            Date date = new SimpleDateFormat("dd/MM/yyyy").parse("" + dPicker.getDayOfMonth() + "/" + (dPicker.getMonth() + 1) + "/" + dPicker.getYear());
                            last_chimney_sweep.setText(new SimpleDateFormat("dd/MM/yyyy").format(date));

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        // info_field_mowing_ermin.setText("" + getString(R.string.no_date));

                        last_chimney_sweep.setText("");

                    }
                });
                builder.setView(dialoglayout);
                builder.show();
            }
        });
        last_test_for_radon_gas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View dialoglayout = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_date_picker, null);
                final DatePicker dPicker = (DatePicker) dialoglayout.findViewById(R.id.date_picker);
                dPicker.setMaxDate(Calendar.getInstance().getTimeInMillis());
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());


                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        try {
                            Date date = new SimpleDateFormat("dd/MM/yyyy").parse("" + dPicker.getDayOfMonth() + "/" + (dPicker.getMonth() + 1) + "/" + dPicker.getYear());
                            last_test_for_radon_gas.setText(new SimpleDateFormat("dd/MM/yyyy").format(date));

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        // info_field_mowing_ermin.setText("" + getString(R.string.no_date));

                        last_test_for_radon_gas.setText("");

                    }
                });
                builder.setView(dialoglayout);
                builder.show();
            }
        });
        Smoke_detector_battery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View dialoglayout = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_date_picker, null);
                final DatePicker dPicker = (DatePicker) dialoglayout.findViewById(R.id.date_picker);
                dPicker.setMaxDate(Calendar.getInstance().getTimeInMillis());
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());


                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        try {
                            Date date = new SimpleDateFormat("dd/MM/yyyy").parse("" + dPicker.getDayOfMonth() + "/" + (dPicker.getMonth() + 1) + "/" + dPicker.getYear());
                            Smoke_detector_battery.setText(new SimpleDateFormat("dd/MM/yyyy").format(date));

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        // info_field_mowing_ermin.setText("" + getString(R.string.no_date));

                        Smoke_detector_battery.setText("");

                    }
                });
                builder.setView(dialoglayout);
                builder.show();
            }
        });
        co_detector_battery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View dialoglayout = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_date_picker, null);
                final DatePicker dPicker = (DatePicker) dialoglayout.findViewById(R.id.date_picker);
                dPicker.setMaxDate(Calendar.getInstance().getTimeInMillis());
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());


                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        try {
                            Date date = new SimpleDateFormat("dd/MM/yyyy").parse("" + dPicker.getDayOfMonth() + "/" + (dPicker.getMonth() + 1) + "/" + dPicker.getYear());
                            co_detector_battery.setText(new SimpleDateFormat("dd/MM/yyyy").format(date));

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        // info_field_mowing_ermin.setText("" + getString(R.string.no_date));

                        co_detector_battery.setText("");

                    }
                });
                builder.setView(dialoglayout);
                builder.show();
            }
        });
        last_gutter_clean.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View dialoglayout = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_date_picker, null);
                final DatePicker dPicker = (DatePicker) dialoglayout.findViewById(R.id.date_picker);
                dPicker.setMaxDate(Calendar.getInstance().getTimeInMillis());
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());


                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        try {
                            Date date = new SimpleDateFormat("dd/MM/yyyy").parse("" + dPicker.getDayOfMonth() + "/" + (dPicker.getMonth() + 1) + "/" + dPicker.getYear());
                            last_gutter_clean.setText(new SimpleDateFormat("dd/MM/yyyy").format(date));

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        // info_field_mowing_ermin.setText("" + getString(R.string.no_date));

                        last_gutter_clean.setText("");

                    }
                });
                builder.setView(dialoglayout);
                builder.show();
            }
        });


    }

    private void getOptionList() {
        ProServiceApiHelper.getInstance(getActivity()).getHomeSchedularOptionList(
                new ProServiceApiHelper.getApiProcessCallback() {
                    @Override
                    public void onStart() {
                        pgDial = new ProgressDialog(getActivity());
                        pgDial.setTitle("Home Schedule");
                        pgDial.setMessage("Getting option data for Home Schedule. Please wait.");
                        pgDial.setCancelable(false);
                        pgDial.show();
                    }

                    @Override
                    public void onComplete(String message) {

                        try {
                            idList = new HashMap<String, ArrayList<String>>();
                            valueList = new HashMap<String, ArrayList<String>>();

                            JSONObject jsonObject = new JSONObject(message);
                            /**
                             square_footage
                             */
                            final ArrayList<String> sqftvalue = new ArrayList<String>();
                            ArrayList<String> sqftid = new ArrayList<String>();
                            sqftvalue.add("Select SQ. FT.");
                            sqftid.add("0");
                            for (int i = 0; i < jsonObject.getJSONArray("square_footage").length(); i++) {
                                sqftvalue.add(jsonObject.getJSONArray("square_footage").getJSONObject(i).getString("value").trim());
                                sqftid.add(jsonObject.getJSONArray("square_footage").getJSONObject(i).getString("id").trim());
                            }
                            idList.put("square_footage", sqftid);
                            valueList.put("square_footage", sqftvalue);

                            RLsq_ft.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                    BottomSheetGlobalList.getInstatnce(getActivity()).showSiteSelectionDialog(sqftvalue, new BottomSheetGlobalList.onOptionSelected() {
                                        @Override
                                        public void onItemPassed(int position, String value) {
                                            tv_sq_ft.setText(value);
                                            Logger.printMessage("value",""+value);
                                            Logger.printMessage("id",""+idList.get("square_footage").get(position));
                                            selected_sq_ft_id=idList.get("square_footage").get(position);
                                        }
                                    });

                                }
                            });

                            /**
                             garage
                             */
                            final ArrayList<String> garagevalue = new ArrayList<String>();
                            ArrayList<String> garageid = new ArrayList<String>();
                            garagevalue.add("Select Garage");
                            garageid.add("0");
                            for (int i = 0; i < jsonObject.getJSONArray("garage").length(); i++) {
                                garagevalue.add(jsonObject.getJSONArray("garage").getJSONObject(i).getString("value").trim());
                                garageid.add(jsonObject.getJSONArray("garage").getJSONObject(i).getString("id").trim());
                            }
                            idList.put("garage", garageid);
                            valueList.put("garage", garagevalue);

                            RLGraage.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    BottomSheetGlobalList.getInstatnce(getActivity()).showSiteSelectionDialog(garagevalue, new BottomSheetGlobalList.onOptionSelected() {
                                        @Override
                                        public void onItemPassed(int position, String value) {
                                            tv_graage.setText(value);
                                            Logger.printMessage("value",""+value);
                                            Logger.printMessage("id",""+idList.get("garage").get(position));
                                            selected_graage_id=idList.get("garage").get(position);
                                        }
                                    });
                                }
                            });

                            /**
                             basement
                             */
                            final ArrayList<String> basementvalue = new ArrayList<String>();
                            ArrayList<String> basementid = new ArrayList<String>();
                            basementvalue.add("Select Basement");
                            basementid.add("0");
                            for (int i = 0; i < jsonObject.getJSONArray("basement").length(); i++) {
                                basementvalue.add(jsonObject.getJSONArray("basement").getJSONObject(i).getString("value").trim());
                                basementid.add(jsonObject.getJSONArray("basement").getJSONObject(i).getString("id").trim());
                            }
                            idList.put("basement", basementid);
                            valueList.put("basement", basementvalue);

                            RLBasement.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    BottomSheetGlobalList.getInstatnce(getActivity()).showSiteSelectionDialog(basementvalue, new BottomSheetGlobalList.onOptionSelected() {
                                        @Override
                                        public void onItemPassed(int position, String value) {
                                            tv_basement.setText(value);
                                            Logger.printMessage("value",""+value);
                                            Logger.printMessage("id",""+idList.get("basement").get(position));
                                            selected_basement_id=idList.get("basement").get(position);
                                        }
                                    });
                                }
                            });

                            /**
                             lot_size
                             */
                            final ArrayList<String> lot_sizevalue = new ArrayList<String>();
                            ArrayList<String> lot_sizeid = new ArrayList<String>();
                            lot_sizevalue.add("Select Size");
                            lot_sizeid.add("0");
                            for (int i = 0; i < jsonObject.getJSONArray("lot_size").length(); i++) {
                                lot_sizevalue.add(jsonObject.getJSONArray("lot_size").getJSONObject(i).getString("value").trim());
                                lot_sizeid.add(jsonObject.getJSONArray("lot_size").getJSONObject(i).getString("id").trim());
                            }
                            idList.put("lot_size", lot_sizeid);
                            valueList.put("lot_size", lot_sizevalue);

                            RLLot_size.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    BottomSheetGlobalList.getInstatnce(getActivity()).showSiteSelectionDialog(lot_sizevalue, new BottomSheetGlobalList.onOptionSelected() {
                                        @Override
                                        public void onItemPassed(int position, String value) {
                                            tv_lot_size.setText(value);
                                            Logger.printMessage("value",""+value);
                                            Logger.printMessage("id",""+idList.get("lot_size").get(position));
                                            selected_lot_size_id=idList.get("lot_size").get(position);
                                        }
                                    });
                                }
                            });

                            /**
                             age_of_roof
                             */
                            final ArrayList<String> age_of_roofvalue = new ArrayList<String>();
                            ArrayList<String> age_of_roofid = new ArrayList<String>();
                            age_of_roofvalue.add("Select Age");
                            age_of_roofid.add("0");
                            for (int i = 0; i < jsonObject.getJSONArray("age_of_roof").length(); i++) {
                                age_of_roofvalue.add(jsonObject.getJSONArray("age_of_roof").getJSONObject(i).getString("value").trim());
                                age_of_roofid.add(jsonObject.getJSONArray("age_of_roof").getJSONObject(i).getString("id").trim());
                            }
                            idList.put("age_of_roof", age_of_roofid);
                            valueList.put("age_of_roof", age_of_roofvalue);

                            RLAge_of_roof.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    BottomSheetGlobalList.getInstatnce(getActivity()).showSiteSelectionDialog(age_of_roofvalue, new BottomSheetGlobalList.onOptionSelected() {
                                        @Override
                                        public void onItemPassed(int position, String value) {
                                            tv_age_of_roof.setText(value);
                                            Logger.printMessage("value",""+value);
                                            Logger.printMessage("id",""+idList.get("age_of_roof").get(position));
                                            selected_age_of_roof_id=idList.get("age_of_roof").get(position);
                                        }
                                    });
                                }
                            });

                            /**
                             age_of_ac
                             */
                            final ArrayList<String> age_of_acvalue = new ArrayList<String>();
                            ArrayList<String> age_of_acid = new ArrayList<String>();
                            age_of_acvalue.add("Select Age");
                            age_of_acid.add("0");
                            for (int i = 0; i < jsonObject.getJSONArray("age_of_ac").length(); i++) {
                                age_of_acvalue.add(jsonObject.getJSONArray("age_of_ac").getJSONObject(i).getString("value").trim());
                                age_of_acid.add(jsonObject.getJSONArray("age_of_ac").getJSONObject(i).getString("id").trim());
                            }
                            idList.put("age_of_ac", age_of_acid);
                            valueList.put("age_of_ac", age_of_acvalue);

                            RLAge_of_ac.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                    BottomSheetGlobalList.getInstatnce(getActivity()).showSiteSelectionDialog(age_of_acvalue, new BottomSheetGlobalList.onOptionSelected() {
                                        @Override
                                        public void onItemPassed(int position, String value) {
                                            tv_age_of_ac.setText(value);
                                            Logger.printMessage("value",""+value);
                                            Logger.printMessage("id",""+idList.get("age_of_ac").get(position));
                                        }
                                    });
                                }
                            });


                            /**
                             age_of_furnance
                             */
                            final ArrayList<String> age_of_furnancevalue = new ArrayList<String>();
                            ArrayList<String> age_of_furnanceid = new ArrayList<String>();
                            age_of_furnancevalue.add("Select Age");
                            age_of_furnanceid.add("0");
                            for (int i = 0; i < jsonObject.getJSONArray("age_of_furnance").length(); i++) {
                                age_of_furnancevalue.add(jsonObject.getJSONArray("age_of_furnance").getJSONObject(i).getString("value").trim());
                                age_of_furnanceid.add(jsonObject.getJSONArray("age_of_furnance").getJSONObject(i).getString("id").trim());
                            }
                            idList.put("age_of_furnance", age_of_furnanceid);
                            valueList.put("age_of_furnance", age_of_furnancevalue);

                            RLAge_of_furance.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    BottomSheetGlobalList.getInstatnce(getActivity()).showSiteSelectionDialog(age_of_furnancevalue, new BottomSheetGlobalList.onOptionSelected() {
                                        @Override
                                        public void onItemPassed(int position, String value) {
                                            tv_age_of_furance.setText(value);
                                            Logger.printMessage("value",""+value);
                                            Logger.printMessage("id",""+idList.get("age_of_furnance").get(position));
                                            selected_age_of_furance_id=idList.get("age_of_furnance").get(position);
                                        }
                                    });
                                }
                            });

                            /**
                             age_of_water_heater
                             */
                            final ArrayList<String> age_of_water_heatervalue = new ArrayList<String>();
                            ArrayList<String> age_of_water_heaterid = new ArrayList<String>();
                            age_of_water_heatervalue.add("Select Age");
                            age_of_water_heaterid.add("0");
                            for (int i = 0; i < jsonObject.getJSONArray("age_of_water_heater").length(); i++) {
                                age_of_water_heatervalue.add(jsonObject.getJSONArray("age_of_water_heater").getJSONObject(i).getString("value").trim());
                                age_of_water_heaterid.add(jsonObject.getJSONArray("age_of_water_heater").getJSONObject(i).getString("id").trim());
                            }
                            idList.put("age_of_water_heater", age_of_water_heaterid);
                            valueList.put("age_of_water_heater", age_of_water_heatervalue);


                            /**
                             * ******************************** Missing value
                             */

                            RLAge_water_heater.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    BottomSheetGlobalList.getInstatnce(getActivity()).showSiteSelectionDialog(age_of_water_heatervalue, new BottomSheetGlobalList.onOptionSelected() {
                                        @Override
                                        public void onItemPassed(int position, String value) {
                                            tv_age_water_heater.setText(value);
                                            Logger.printMessage("value",""+value);
                                            Logger.printMessage("id",""+idList.get("age_of_water_heater").get(position));
                                            selected_age_water_heater_id=idList.get("age_of_water_heater").get(position);
                                        }
                                    });
                                }
                            });

                            /**
                             age_of_windows
                             */
                            final ArrayList<String> age_of_windowsvalue = new ArrayList<String>();
                            ArrayList<String> age_of_windowsid = new ArrayList<String>();
                            age_of_windowsvalue.add("Select Age");
                            age_of_windowsid.add("0");
                            for (int i = 0; i < jsonObject.getJSONArray("age_of_windows").length(); i++) {
                                age_of_windowsvalue.add(jsonObject.getJSONArray("age_of_windows").getJSONObject(i).getString("value").trim());
                                age_of_windowsid.add(jsonObject.getJSONArray("age_of_windows").getJSONObject(i).getString("id").trim());
                            }
                            idList.put("age_of_windows", age_of_windowsid);
                            valueList.put("age_of_windows", age_of_windowsvalue);

                            RLAge_window.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    BottomSheetGlobalList.getInstatnce(getActivity()).showSiteSelectionDialog(age_of_windowsvalue, new BottomSheetGlobalList.onOptionSelected() {
                                        @Override
                                        public void onItemPassed(int position, String value) {
                                            tv_age_window.setText(value);
                                            Logger.printMessage("value",""+value);
                                            Logger.printMessage("id",""+idList.get("age_of_windows").get(position));
                                            selected_age_window_id=idList.get("age_of_windows").get(position);
                                        }
                                    });
                                }
                            });

                            /**
                             property_type
                             */
                            final ArrayList<String> property_typevalue = new ArrayList<String>();
                            ArrayList<String> property_typeid = new ArrayList<String>();
                            property_typevalue.add("Select Property");
                            property_typeid.add("0");
                            for (int i = 0; i < jsonObject.getJSONArray("property_type").length(); i++) {
                                property_typevalue.add(jsonObject.getJSONArray("property_type").getJSONObject(i).getString("value").trim());
                                property_typeid.add(jsonObject.getJSONArray("property_type").getJSONObject(i).getString("id").trim());
                            }
                            idList.put("property_type", property_typeid);
                            valueList.put("property_type", property_typevalue);

                            RLPrototype.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    BottomSheetGlobalList.getInstatnce(getActivity()).showSiteSelectionDialog(property_typevalue, new BottomSheetGlobalList.onOptionSelected() {
                                        @Override
                                        public void onItemPassed(int position, String value) {
                                            tv_prototype.setText(value);
                                            Logger.printMessage("value",""+value);
                                            Logger.printMessage("id",""+idList.get("property_type").get(position));
                                            selected_prototype_id=idList.get("property_type").get(position);
                                        }
                                    });
                                }
                            });

                            setOptions();

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(String error) {
                        if (pgDial != null && pgDial.isShowing())
                            pgDial.dismiss();
                        new AlertDialog.Builder(getActivity())
                                .setTitle("Home Schedule Option Error")
                                .setMessage("" + error)
                                .setPositiveButton("retry", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                        getOptionList();

                                    }
                                })
                                .setNegativeButton("abort", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                    }
                });
    }

    private void setOptions() {
        ProServiceApiHelper.getInstance(getActivity()).getHomeSchedularValuesList(
                new ProServiceApiHelper.getApiProcessCallback() {
                    @Override
                    public void onStart() {

                    }
                    @Override
                    public void onComplete(String message) {
                        try {
                            JSONObject mainres = new JSONObject(message);
                            JSONObject options = mainres.getJSONObject("info_array");

                            year_build.setText(options.getString("year_built"));

//                            tv_sq_ft.setText(valueList.get("square_footage").indexOf(options.getJSONObject("square_footage").getString("value").trim()));
                            tv_sq_ft.setText(options.getJSONObject("square_footage").getString("value").trim());
                            selected_sq_ft_id=options.getJSONObject("square_footage").getString("id").trim();
//                            tv_graage.setText(valueList.get("garage").indexOf(options.getJSONObject("garage").getString("value").trim()));
                            tv_graage.setText(options.getJSONObject("garage").getString("value").trim());
                            selected_graage_id=options.getJSONObject("garage").getString("id").trim();
//                            tv_prototype.setText(valueList.get("property_type").indexOf(options.getJSONObject("property_type").getString("value").trim()));
                            tv_prototype.setText(options.getJSONObject("property_type").getString("value").trim());
                            selected_prototype_id=options.getJSONObject("property_type").getString("id").trim();
//                            tv_basement.setText(valueList.get("basement").indexOf(options.getJSONObject("basement").getString("value").trim()));
                            tv_basement.setText(options.getJSONObject("basement").getString("value").trim());
                            selected_basement_id=options.getJSONObject("basement").getString("id").trim();
//                            tv_lot_size.setText(valueList.get("lot_size").indexOf(options.getJSONObject("lot_size").getString("value").trim()));
                            tv_lot_size.setText(options.getJSONObject("lot_size").getString("value").trim());
                            selected_lot_size_id=options.getJSONObject("lot_size").getString("id").trim();
//                            tv_age_of_roof.setText(valueList.get("age_of_roof").indexOf(options.getJSONObject("roof_age").getString("value").trim()));
                            tv_age_of_roof.setText(options.getJSONObject("roof_age").getString("value").trim());
                            selected_age_of_roof_id=options.getJSONObject("roof_age").getString("id").trim();
//                            tv_age_of_ac.setText(valueList.get("age_of_ac").indexOf(options.getJSONObject("a/c_age").getString("value").trim()));
                            tv_age_of_ac.setText(options.getJSONObject("a/c_age").getString("value").trim());
                            selected_age_of_ac_id=options.getJSONObject("a/c_age").getString("id").trim();
//                            tv_age_of_furance.setText(valueList.get("age_of_furnance").indexOf(options.getJSONObject("furnance_age").getString("value").trim()));
                            tv_age_of_furance.setText(options.getJSONObject("furnance_age").getString("value").trim());
                            selected_age_of_furance_id=options.getJSONObject("furnance_age").getString("id").trim();
//                            tv_age_water_heater.setText(valueList.get("age_of_water_heater").indexOf(options.getJSONObject("water_heater_age").getString("value").trim()));
                            tv_age_water_heater.setText(options.getJSONObject("water_heater_age").getString("value").trim());
                            selected_age_water_heater_id=options.getJSONObject("water_heater_age").getString("id").trim();
//                            tv_age_window.setText(valueList.get("age_of_windows").indexOf(options.getJSONObject("window_age").getString("value").trim()));
                            tv_age_window.setText(options.getJSONObject("window_age").getString("value").trim());
                            selected_age_window_id=options.getJSONObject("window_age").getString("id").trim();



                            ac_filter.setText(options.getString("filter_change").trim());

                            last_water_heater_flush.setText(options.getString("water_heater_flush").trim());
                            last_chimney_sweep.setText(options.getString("chimney_sweep").trim());

                            last_test_for_radon_gas.setText(options.getString("randon_gas").trim());
                            Smoke_detector_battery.setText(options.getString("smoke_detector").trim());
                            co_detector_battery.setText(options.getString("co_detector").trim());
                            last_gutter_clean.setText(options.getString("gutter_clean").trim());

                            if (!options.getString("property_picture").trim().equals(""))
                                Glide.with(getActivity()).load(options.getString("property_picture").trim()).centerCrop().into(property_image);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        if (pgDial != null && pgDial.isShowing())
                            pgDial.dismiss();
                    }

                    @Override
                    public void onError(String error) {
                        if (pgDial != null && pgDial.isShowing())
                            pgDial.dismiss();
                    }
                });
    }

    private void updateOptions() {
        ProServiceApiHelper.getInstance(getActivity()).updateHomeSchedularOptions(
                new ProServiceApiHelper.getApiProcessCallback() {
                    @Override
                    public void onStart() {
                        pgDial = new ProgressDialog(getActivity());
                        pgDial.setTitle("Home Reminder");
                        pgDial.setMessage("Updating details. Please wait.");
                        pgDial.setCancelable(false);
                        pgDial.show();
                    }

                    @Override
                    public void onComplete(String message) {
                        if (pgDial != null && pgDial.isShowing())
                            pgDial.dismiss();
                        new AlertDialog.Builder(getActivity())
                                .setTitle("Update Home Scheduler")
                                .setMessage("Successfully updated Home scheduler options.")
                                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                })
                                .show();

                    }

                    @Override
                    public void onError(String error) {
                        if (pgDial != null && pgDial.isShowing())
                            pgDial.dismiss();
                        new AlertDialog.Builder(getActivity())
                                .setTitle("Update Error")
                                .setMessage("" + error)
                                .setPositiveButton("retry", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                        updateOptions();
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
                },
                /*
                  .addFormDataPart("user_id", ProApplication.getInstance().getUserId())
                                .addFormDataPart("year_build", "" + params[0])
                                .addFormDataPart("footage", "" + params[1])
                                .addFormDataPart("garage", "" + params[2])
                                .addFormDataPart("property_type", "" + params[3])
                                .addFormDataPart("basement", "" + params[4])
                                .addFormDataPart("size", "" + params[5])
                                .addFormDataPart("roof_age", "" + params[6])
                                .addFormDataPart("ac_age", "" + params[7])
                                .addFormDataPart("furnance_age", "" + params[8])
                                .addFormDataPart("filter_change", "" + params[9])
                                .addFormDataPart("water_heater_age", "" + params[10])
                                .addFormDataPart("window_age", "" + params[11])
                                .addFormDataPart("water_heater_flush", "" + params[12])
                                .addFormDataPart("chimney_sweep", "" + params[13])
                                .addFormDataPart("last_radon_gas", "" + params[14])
                                .addFormDataPart("smoke_detector", "" + params[15])
                                .addFormDataPart("co_detector", "" + params[16])
                                .addFormDataPart("gutter_clean", "" + params[17]);
                 */




                year_build.getText().toString().trim(),
                selected_sq_ft_id,
                selected_graage_id,
                selected_prototype_id,
                selected_basement_id,
                selected_lot_size_id,
                selected_age_of_roof_id,
                selected_age_of_ac_id,
                selected_age_of_furance_id,
                ac_filter.getText().toString().trim(),
                selected_age_water_heater_id,
                selected_age_window_id,
                last_water_heater_flush.getText().toString().trim(),
                last_chimney_sweep.getText().toString().trim(),
                last_test_for_radon_gas.getText().toString().trim(),
                Smoke_detector_battery.getText().toString().trim(),
                co_detector_battery.getText().toString().trim(),
                last_gutter_clean.getText().toString().trim(),
                mCurrentPhotoPath

        );
    }


    private void showImagePickerOption() {
        new AlertDialog.Builder(getActivity())
                .setCancelable(true)
                .setTitle("Property image")
                .setMessage("please choose image source type.")
                .setPositiveButton("Gallery", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        Intent intent = new Intent(Intent.ACTION_PICK);
                        if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
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
                        startActivityForResult(new Intent(getActivity(), ImageTakerActivityCamera.class), REQUEST_IMAGE_CAPTURE);
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
                            Glide.with(getActivity()).load("file://" + mCurrentPhotoPath).into(new GlideDrawableImageViewTarget(property_image) {
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
                    Glide.with(getActivity()).load(uri).fitCenter().into(new GlideDrawableImageViewTarget(property_image) {
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

    @Override
    public void setInitialSavedState(SavedState state) {
        super.setInitialSavedState(state);
    }


    //file uri to real location in filesystem
    public String getRealPathFromURI(Uri contentURI) {
        Cursor cursor = getActivity().getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) {
            // Source is Dropbox or other similar local file path
            return contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            return cursor.getString(idx);
        }
    }

}
