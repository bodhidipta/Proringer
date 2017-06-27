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
import android.support.v4.util.ArrayMap;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.Spinner;

import com.android.llc.proringer.R;
import com.android.llc.proringer.adapter.SpinnerAdapter;
import com.android.llc.proringer.appconstant.ProConstant;
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
import java.util.LinkedList;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

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
    private Spinner sq_ft, graage, prototype, basement, lot_size, age_of_roof, age_of_ac, age_of_furance, age_water_heater, age_window;
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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home_reminder, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        sq_ft = (Spinner) view.findViewById(R.id.sq_ft);
        graage = (Spinner) view.findViewById(R.id.graage);
        prototype = (Spinner) view.findViewById(R.id.prototype);
        basement = (Spinner) view.findViewById(R.id.basement);
        lot_size = (Spinner) view.findViewById(R.id.lot_size);
        age_of_roof = (Spinner) view.findViewById(R.id.age_of_roof);
        age_of_ac = (Spinner) view.findViewById(R.id.age_of_ac);
        age_of_furance = (Spinner) view.findViewById(R.id.age_of_furance);
        age_water_heater = (Spinner) view.findViewById(R.id.age_water_heater);
        age_window = (Spinner) view.findViewById(R.id.age_window);

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
                            ArrayList<String> sqftvalue = new ArrayList<String>();
                            ArrayList<String> sqftid = new ArrayList<String>();
                            sqftvalue.add("Select SQ. FT.");
                            sqftid.add("0");
                            for (int i = 0; i < jsonObject.getJSONArray("square_footage").length(); i++) {
                                sqftvalue.add(jsonObject.getJSONArray("square_footage").getJSONObject(i).getString("value"));
                                sqftid.add(jsonObject.getJSONArray("square_footage").getJSONObject(i).getString("id"));
                            }
                            idList.put("square_footage", sqftid);
                            valueList.put("square_footage", sqftvalue);

                            SpinnerAdapter square_footage_ADP = new SpinnerAdapter(getActivity(), 0, sqftvalue);
                            sq_ft.setAdapter(square_footage_ADP);
                            square_footage_ADP.setDropDownViewResource(R.layout.actionmenuspiner);
                            sq_ft.setDropDownVerticalOffset(100);

                            /**
                             garage
                             */
                            ArrayList<String> garagevalue = new ArrayList<String>();
                            ArrayList<String> garageid = new ArrayList<String>();
                            garagevalue.add("Select Garage");
                            garageid.add("0");
                            for (int i = 0; i < jsonObject.getJSONArray("garage").length(); i++) {
                                garagevalue.add(jsonObject.getJSONArray("garage").getJSONObject(i).getString("value"));
                                garageid.add(jsonObject.getJSONArray("garage").getJSONObject(i).getString("id"));
                            }
                            idList.put("garage", garageid);
                            valueList.put("garage", garagevalue);

                            SpinnerAdapter garage_ADP = new SpinnerAdapter(getActivity(), 0, garagevalue);
                            graage.setAdapter(garage_ADP);
                            garage_ADP.setDropDownViewResource(R.layout.actionmenuspiner);
                            graage.setDropDownVerticalOffset(100);

                            /**
                             basement
                             */
                            ArrayList<String> basementvalue = new ArrayList<String>();
                            ArrayList<String> basementid = new ArrayList<String>();
                            basementvalue.add("Select Basement");
                            basementid.add("0");
                            for (int i = 0; i < jsonObject.getJSONArray("basement").length(); i++) {
                                basementvalue.add(jsonObject.getJSONArray("basement").getJSONObject(i).getString("value"));
                                basementid.add(jsonObject.getJSONArray("basement").getJSONObject(i).getString("id"));
                            }
                            idList.put("basement", basementid);
                            valueList.put("basement", basementvalue);

                            SpinnerAdapter basement_ADP = new SpinnerAdapter(getActivity(), 0, basementvalue);
                            basement.setAdapter(basement_ADP);
                            basement_ADP.setDropDownViewResource(R.layout.actionmenuspiner);
                            basement.setDropDownVerticalOffset(100);

                            /**
                             lot_size
                             */
                            ArrayList<String> lot_sizevalue = new ArrayList<String>();
                            ArrayList<String> lot_sizeid = new ArrayList<String>();
                            lot_sizevalue.add("Select Size");
                            lot_sizeid.add("0");
                            for (int i = 0; i < jsonObject.getJSONArray("lot_size").length(); i++) {
                                lot_sizevalue.add(jsonObject.getJSONArray("lot_size").getJSONObject(i).getString("value"));
                                lot_sizeid.add(jsonObject.getJSONArray("lot_size").getJSONObject(i).getString("id"));
                            }
                            idList.put("lot_size", lot_sizeid);
                            valueList.put("lot_size", lot_sizevalue);

                            SpinnerAdapter lot_size_ADP = new SpinnerAdapter(getActivity(), 0, lot_sizevalue);
                            lot_size.setAdapter(lot_size_ADP);
                            lot_size_ADP.setDropDownViewResource(R.layout.actionmenuspiner);
                            lot_size.setDropDownVerticalOffset(100);

                            /**
                             age_of_roof
                             */
                            ArrayList<String> age_of_roofvalue = new ArrayList<String>();
                            ArrayList<String> age_of_roofid = new ArrayList<String>();
                            age_of_roofvalue.add("Select Age");
                            age_of_roofid.add("0");
                            for (int i = 0; i < jsonObject.getJSONArray("age_of_roof").length(); i++) {
                                age_of_roofvalue.add(jsonObject.getJSONArray("age_of_roof").getJSONObject(i).getString("value"));
                                age_of_roofid.add(jsonObject.getJSONArray("age_of_roof").getJSONObject(i).getString("id"));
                            }
                            idList.put("age_of_roof", age_of_roofid);
                            valueList.put("age_of_roof", age_of_roofvalue);

                            SpinnerAdapter age_of_roof_ADP = new SpinnerAdapter(getActivity(), 0, age_of_roofvalue);
                            age_of_roof.setAdapter(age_of_roof_ADP);
                            age_of_roof_ADP.setDropDownViewResource(R.layout.actionmenuspiner);
                            age_of_roof.setDropDownVerticalOffset(100);

                            /**
                             age_of_ac
                             */
                            ArrayList<String> age_of_acvalue = new ArrayList<String>();
                            ArrayList<String> age_of_acid = new ArrayList<String>();
                            age_of_acvalue.add("Select Age");
                            age_of_acid.add("0");
                            for (int i = 0; i < jsonObject.getJSONArray("age_of_ac").length(); i++) {
                                age_of_acvalue.add(jsonObject.getJSONArray("age_of_ac").getJSONObject(i).getString("value"));
                                age_of_acid.add(jsonObject.getJSONArray("age_of_ac").getJSONObject(i).getString("id"));
                            }
                            idList.put("age_of_ac", age_of_acid);
                            valueList.put("age_of_ac", age_of_acvalue);

                            SpinnerAdapter age_of_ac_ADP = new SpinnerAdapter(getActivity(), 0, age_of_acvalue);
                            age_of_ac.setAdapter(age_of_ac_ADP);
                            age_of_ac_ADP.setDropDownViewResource(R.layout.actionmenuspiner);
                            age_of_ac.setDropDownVerticalOffset(100);
                            /**
                             age_of_furnance
                             */
                            ArrayList<String> age_of_furnancevalue = new ArrayList<String>();
                            ArrayList<String> age_of_furnanceid = new ArrayList<String>();
                            age_of_furnancevalue.add("Select Age");
                            age_of_furnanceid.add("0");
                            for (int i = 0; i < jsonObject.getJSONArray("age_of_furnance").length(); i++) {
                                age_of_furnancevalue.add(jsonObject.getJSONArray("age_of_furnance").getJSONObject(i).getString("value"));
                                age_of_furnanceid.add(jsonObject.getJSONArray("age_of_furnance").getJSONObject(i).getString("id"));
                            }
                            idList.put("age_of_furnance", age_of_furnanceid);
                            valueList.put("age_of_furnance", age_of_furnancevalue);

                            SpinnerAdapter age_of_furnance_ADP = new SpinnerAdapter(getActivity(), 0, age_of_furnancevalue);
                            age_of_furance.setAdapter(age_of_furnance_ADP);
                            age_of_furnance_ADP.setDropDownViewResource(R.layout.actionmenuspiner);
                            age_of_furance.setDropDownVerticalOffset(100);

                            /**
                             age_of_water_heater
                             */
                            ArrayList<String> age_of_water_heatervalue = new ArrayList<String>();
                            ArrayList<String> age_of_water_heaterid = new ArrayList<String>();
                            age_of_water_heatervalue.add("Select Age");
                            age_of_water_heaterid.add("0");
                            for (int i = 0; i < jsonObject.getJSONArray("age_of_water_heater").length(); i++) {
                                age_of_water_heatervalue.add(jsonObject.getJSONArray("age_of_water_heater").getJSONObject(i).getString("value"));
                                age_of_water_heaterid.add(jsonObject.getJSONArray("age_of_water_heater").getJSONObject(i).getString("id"));
                            }
                            idList.put("age_of_water_heater", age_of_water_heaterid);
                            valueList.put("age_of_water_heater", age_of_water_heatervalue);

                            SpinnerAdapter age_of_water_heater_ADP = new SpinnerAdapter(getActivity(), 0, age_of_water_heatervalue);
                            age_water_heater.setAdapter(age_of_water_heater_ADP);
                            age_of_water_heater_ADP.setDropDownViewResource(R.layout.actionmenuspiner);
                            age_water_heater.setDropDownVerticalOffset(100);
                            /**
                             age_of_windows
                             */
                            ArrayList<String> age_of_windowsvalue = new ArrayList<String>();
                            ArrayList<String> age_of_windowsid = new ArrayList<String>();
                            age_of_windowsvalue.add("Select Age");
                            age_of_windowsid.add("0");
                            for (int i = 0; i < jsonObject.getJSONArray("age_of_windows").length(); i++) {
                                age_of_windowsvalue.add(jsonObject.getJSONArray("age_of_windows").getJSONObject(i).getString("value"));
                                age_of_windowsid.add(jsonObject.getJSONArray("age_of_windows").getJSONObject(i).getString("id"));
                            }
                            idList.put("age_of_windows", age_of_windowsid);
                            valueList.put("age_of_windows", age_of_windowsvalue);

                            SpinnerAdapter age_of_windows_ADP = new SpinnerAdapter(getActivity(), 0, age_of_windowsvalue);
                            age_window.setAdapter(age_of_windows_ADP);
                            age_of_windows_ADP.setDropDownViewResource(R.layout.actionmenuspiner);
                            age_window.setDropDownVerticalOffset(100);
                            /**
                             property_type
                             */
                            ArrayList<String> property_typevalue = new ArrayList<String>();
                            ArrayList<String> property_typeid = new ArrayList<String>();
                            property_typevalue.add("Select Property");
                            property_typeid.add("0");
                            for (int i = 0; i < jsonObject.getJSONArray("property_type").length(); i++) {
                                property_typevalue.add(jsonObject.getJSONArray("property_type").getJSONObject(i).getString("value"));
                                property_typeid.add(jsonObject.getJSONArray("property_type").getJSONObject(i).getString("id"));
                            }
                            idList.put("property_type", property_typeid);
                            valueList.put("property_type", property_typevalue);

                            SpinnerAdapter prototype_ADP = new SpinnerAdapter(getActivity(), 0, property_typevalue);
                            prototype.setAdapter(prototype_ADP);
                            prototype_ADP.setDropDownViewResource(R.layout.actionmenuspiner);
                            prototype.setDropDownVerticalOffset(100);


                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        setOptions();


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

                            sq_ft.setSelection(valueList.get("square_footage").indexOf(options.getJSONObject("square_footage").getString("value")));
                            graage.setSelection(valueList.get("garage").indexOf(options.getJSONObject("garage").getString("value")));
                            prototype.setSelection(valueList.get("property_type").indexOf(options.getJSONObject("property_type").getString("value")));
                            basement.setSelection(valueList.get("basement").indexOf(options.getJSONObject("basement").getString("value")));
                            lot_size.setSelection(valueList.get("lot_size").indexOf(options.getJSONObject("lot_size").getString("value")));
                            age_of_roof.setSelection(valueList.get("age_of_roof").indexOf(options.getJSONObject("roof_age").getString("value")));
                            age_of_ac.setSelection(valueList.get("age_of_ac").indexOf(options.getJSONObject("a/c_age").getString("value")));
                            age_of_furance.setSelection(valueList.get("age_of_furnance").indexOf(options.getJSONObject("furnance_age").getString("value")));
                            age_water_heater.setSelection(valueList.get("age_of_water_heater").indexOf(options.getJSONObject("water_heater_age").getString("value")));
                            age_window.setSelection(valueList.get("age_of_windows").indexOf(options.getJSONObject("window_age").getString("value")));
                            ac_filter.setText(options.getString("filter_change"));

                            last_water_heater_flush.setText(options.getString("water_heater_flush"));
                            last_chimney_sweep.setText(options.getString("chimney_sweep"));

                            last_test_for_radon_gas.setText(options.getString("randon_gas"));
                            Smoke_detector_battery.setText(options.getString("smoke_detector"));
                            co_detector_battery.setText(options.getString("co_detector"));
                            last_gutter_clean.setText(options.getString("gutter_clean"));

                            if (!options.getString("property_picture").equals(""))
                                Glide.with(getActivity()).load(options.getString("property_picture")).centerCrop().into(property_image);

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
                idList.get("square_footage").get(sq_ft.getSelectedItemPosition()),
                idList.get("garage").get(graage.getSelectedItemPosition()),
                idList.get("property_type").get(prototype.getSelectedItemPosition()),
                idList.get("basement").get(basement.getSelectedItemPosition()),
                idList.get("lot_size").get(lot_size.getSelectedItemPosition()),
                idList.get("age_of_roof").get(age_of_roof.getSelectedItemPosition()),
                idList.get("age_of_ac").get(age_of_ac.getSelectedItemPosition()),
                idList.get("age_of_furnance").get(age_of_furance.getSelectedItemPosition()),
                ac_filter.getText().toString().trim(),
                idList.get("age_of_water_heater").get(age_water_heater.getSelectedItemPosition()),
                idList.get("age_of_windows").get(age_window.getSelectedItemPosition()),
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
