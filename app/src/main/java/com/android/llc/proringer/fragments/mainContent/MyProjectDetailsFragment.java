package com.android.llc.proringer.fragments.mainContent;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.llc.proringer.R;
import com.android.llc.proringer.activities.AddEditProsActivity;
import com.android.llc.proringer.activities.LandScreenActivity;
import com.android.llc.proringer.appconstant.ProApplication;
import com.android.llc.proringer.appconstant.ProConstant;
import com.android.llc.proringer.fragments.postProject.PostProjectRegistrationAndFinalizeFragment;
import com.android.llc.proringer.helper.CustomAlert;
import com.android.llc.proringer.helper.MyCustomAlertListener;
import com.android.llc.proringer.helper.MyLoader;
import com.android.llc.proringer.helper.ProServiceApiHelper;
import com.android.llc.proringer.utils.Logger;
import com.android.llc.proringer.viewsmod.textview.ProRegularTextView;
import com.android.llc.proringer.viewsmod.textview.ProSemiBoldTextView;
import com.bumptech.glide.Glide;

/**
 * Created by su on 7/17/17.
 */

public class MyProjectDetailsFragment extends Fragment implements MyCustomAlertListener{
    MyLoader myLoader = null;
    ProRegularTextView tv_posted_in, tv_project, tv_service, tv_type, tv_property, tv_status, tv_start, img_description;
    ImageView img_project;
    LinearLayout LL_Active;
    ProSemiBoldTextView tv_accepted_review, tv_edit,tv_renew;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.my_project_detailed_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tv_posted_in = (ProRegularTextView) view.findViewById(R.id.tv_posted_in);
        tv_project = (ProRegularTextView) view.findViewById(R.id.tv_project);
        tv_service = (ProRegularTextView) view.findViewById(R.id.tv_service);
        tv_type = (ProRegularTextView) view.findViewById(R.id.tv_type);
        tv_property = (ProRegularTextView) view.findViewById(R.id.tv_property);
        tv_status = (ProRegularTextView) view.findViewById(R.id.tv_status);
//        ProRegularTextView tv_type2 = (ProRegularTextView) view.findViewById(R.id.tv_type2);
        tv_start = (ProRegularTextView) view.findViewById(R.id.tv_start);
        img_description = (ProRegularTextView) view.findViewById(R.id.img_description);
        img_project = (ImageView) view.findViewById(R.id.img_project);
        LL_Active = (LinearLayout) view.findViewById(R.id.LL_Active);
        tv_accepted_review = (ProSemiBoldTextView) view.findViewById(R.id.tv_accepted_review);
        tv_edit = (ProSemiBoldTextView) view.findViewById(R.id.tv_edit);
        tv_renew = (ProSemiBoldTextView) view.findViewById(R.id.tv_renew);
        tv_posted_in.setText(ProApplication.getInstance().getDataSelected().getDate_time());



        tv_project.setText(ProApplication.getInstance().getDataSelected().getProject_category_name());
        final String project = ProApplication.getInstance().getDataSelected().getProject_category_name();
        tv_service.setText(ProApplication.getInstance().getDataSelected().getProject_category_service_name());
        tv_type.setText(ProApplication.getInstance().getDataSelected().getProject_service_looktype());
        tv_property.setText(ProApplication.getInstance().getDataSelected().getProperty_type_name());
        tv_status.setText(ProApplication.getInstance().getDataSelected().getProject_stage());
        tv_start.setText(ProApplication.getInstance().getDataSelected().getProject_timeframe_name());
        img_description.setText(ProApplication.getInstance().getDataSelected().getProject_name());


        ProConstant.projectid = ProApplication.getInstance().getDataSelected().getId();
        ProConstant.service = ProApplication.getInstance().getDataSelected().getProject_category_service_name();
        ProConstant.Project_name = (ProApplication.getInstance().getDataSelected().getProject_category_name());
        ProConstant.projectzip = ProApplication.getInstance().getDataSelected().getZip();

        Logger.printMessage("projectid", ProConstant.projectid);
        Logger.printMessage("service_name", ProConstant.service);
        Logger.printMessage("Project_name", ProConstant.Project_name);
        Logger.printMessage("city", ProApplication.getInstance().getDataSelected().getCity());
        Logger.printMessage("Zip", ProConstant.projectzip);
        Logger.printMessage("count", ProApplication.getInstance().getDataSelected().getCountry_code());
        Logger.printMessage("State", ProApplication.getInstance().getDataSelected().getState_code());
        Logger.printMessage("latlong", ProApplication.getInstance().getDataSelected().getLatitude());
        Logger.printMessage("longitude", ProApplication.getInstance().getDataSelected().getLatitude());


        myLoader = new MyLoader(getActivity());

        if (!ProApplication.getInstance().getDataSelected().getProject_image().equals(""))
            Glide.with((LandScreenActivity) getActivity()).load(ProApplication.getInstance().getDataSelected().getProject_image())
//                    .centerCrop()
                    .into(img_project);

        Logger.printMessage("project_status", "" + ProApplication.getInstance().getDataSelected().getProject_status());

        if (ProApplication.getInstance().getDataSelected().getProject_status().equalsIgnoreCase("Y")) {
            LL_Active.setVisibility(View.VISIBLE);
            tv_accepted_review.setVisibility(View.GONE);
        } else if (ProApplication.getInstance().getDataSelected().getProject_status().equalsIgnoreCase("A")) {
            LL_Active.setVisibility(View.GONE);
            tv_accepted_review.setVisibility(View.VISIBLE);
        } else if (ProApplication.getInstance().getDataSelected().getProject_status().equalsIgnoreCase("D")) {
            LL_Active.setVisibility(View.GONE);
            tv_accepted_review.setVisibility(View.GONE);
        } else {
            LL_Active.setVisibility(View.GONE);
            tv_accepted_review.setVisibility(View.GONE);
        }
        tv_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//
                Intent i = new Intent(getActivity(), AddEditProsActivity.class);
                i.putExtra("Project", project);
                startActivity(i);
            }
        });

        tv_renew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CustomAlert customAlert = new CustomAlert(getActivity(), "Renew", "Are you sure you want to renew this project posting?", MyProjectDetailsFragment.this);
                customAlert.getListenerRetryCancelFromNormalAlert("ok","cancel",1);

            }
        });



        view.findViewById(R.id.tv_delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                TextView title = new TextView((LandScreenActivity) getActivity());
                title.setText("Are you sure you want to close and remove this project?");
//                title.setBackgroundResource(R.drawable.gradient);
                title.setPadding(10, 10, 10, 10);
                title.setGravity(Gravity.CENTER);
                title.setTextColor(getActivity().getResources().getColor(R.color.colorTextBlack));
                title.setTextSize(18);

                new AlertDialog.Builder((LandScreenActivity) getActivity())
                        .setCustomTitle(title)
                        .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                ///////////delete from favorite list
                                delete();
                            }
                        })
                        .setCancelable(false)
                        .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        })
                        .show();
            }
        });
//        ProServiceApiHelper.getInstance(getActivity()).getMyProjectDetails(new ProServiceApiHelper.getApiProcessCallback() {
//            @Override
//            public void onStart() {
//                dialog = new ProgressDialog(getActivity());
//                dialog.setTitle("My Projects");
//                dialog.setCancelable(false);
//                dialog.setMessage("Getting MyProject details. Please wait.");
//                dialog.show();
//            }
//
//            @Override
//            public void onComplete(String message) {
//                if (dialog != null && dialog.isShowing())
//                    dialog.dismiss();
//
//                try {
//                    JSONObject jsonObject = new JSONObject(message);
//
//                    if (jsonObject.has("info_array")) {
//
//                        JSONArray info_array = jsonObject.getJSONArray("info_array");
//                    }
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//
//            @Override
//            public void onError(String error) {
//                if (dialog != null && dialog.isShowing())
//                    dialog.dismiss();
//
//                new AlertDialog.Builder(getActivity())
//                        .setTitle("MyProjectsFragment Error")
//                        .setMessage("" + error)
//                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialogInterface, int i) {
//                                dialogInterface.dismiss();
//                            }
//                        })
//                        .show();
//            }
//        }, ProApplication.getInstance().getDataSelected().getId());
    }

    public void delete() {
        ProServiceApiHelper.getInstance((LandScreenActivity) getActivity()).deleteMyProjectAPI(new ProServiceApiHelper.getApiProcessCallback() {
                                                                                                @Override
                                                                                                public void onStart() {
                                                                                                    myLoader.showLoader();

                                                                                                }

                                                                                                @Override
                                                                                                public void onComplete(String message) {

//                                                                          itemList.remove(position);
//                                                                          notifyItemRemoved(position);

                                                                                                    if (myLoader != null && myLoader.isMyLoaderShowing())
                                                                                                        myLoader.dismissLoader();


                                                                                                    new AlertDialog.Builder((LandScreenActivity) getActivity())
                                                                                                            .setTitle("Project deleted")
                                                                                                            .setMessage("" + message)
                                                                                                            .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                                                                                                @Override
                                                                                                                public void onClick(DialogInterface dialog, int which) {
                                                                                                                    dialog.dismiss();
                                                                                                                    ((LandScreenActivity) getActivity()).toggleToolBar(false);
                                                                                                                    ((LandScreenActivity) getActivity()).transactMyProjects();
                                                                                                                }
                                                                                                            })
                                                                                                            .setCancelable(false)
                                                                                                            .show();
                                                                                                }

                                                                                                @Override
                                                                                                public void onError(String error) {
                                                                                                    if (myLoader != null && myLoader.isMyLoaderShowing())
                                                                                                        myLoader.dismissLoader();

                                                                                                    new AlertDialog.Builder((LandScreenActivity) getActivity())
                                                                                                            .setTitle("project deleting Error")
                                                                                                            .setMessage("" + error)
                                                                                                            .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                                                                                                @Override
                                                                                                                public void onClick(DialogInterface dialog, int which) {
                                                                                                                    dialog.dismiss();
                                                                                                                }
                                                                                                            })
                                                                                                            .setCancelable(false)
                                                                                                            .show();

                                                                                                }
                                                                                            },
                ProApplication.getInstance().getUserId(),
                ProApplication.getInstance().getDataSelected().getId());
    }
    public void renew() {
        ProServiceApiHelper.getInstance((LandScreenActivity) getActivity()).renewProAPI(new ProServiceApiHelper.getApiProcessCallback() {
                                                                                                @Override
                                                                                                public void onStart() {
                                                                                                    myLoader.showLoader();

                                                                                                }

                                                                                                @Override
                                                                                                public void onComplete(String message) {

//                                                                          itemList.remove(position);
//                                                                          notifyItemRemoved(position);

                                                                                                    if (myLoader != null && myLoader.isMyLoaderShowing())
                                                                                                        myLoader.dismissLoader();


                                                                                                    new AlertDialog.Builder((LandScreenActivity) getActivity())
                                                                                                            .setTitle("project posting renew")
                                                                                                            .setMessage("" + message)
                                                                                                            .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                                                                                                @Override
                                                                                                                public void onClick(DialogInterface dialog, int which) {
                                                                                                                    dialog.dismiss();
                                                                                                                }
                                                                                                            })
                                                                                                            .setCancelable(false)
                                                                                                            .show();
                                                                                                }

                                                                                                @Override
                                                                                                public void onError(String error) {
                                                                                                    if (myLoader != null && myLoader.isMyLoaderShowing())
                                                                                                        myLoader.dismissLoader();

                                                                                                    new AlertDialog.Builder((LandScreenActivity) getActivity())
                                                                                                            .setTitle("project posting renew Error")
                                                                                                            .setMessage("" + error)
                                                                                                            .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                                                                                                @Override
                                                                                                                public void onClick(DialogInterface dialog, int which) {
                                                                                                                    dialog.dismiss();
                                                                                                                }
                                                                                                            })
                                                                                                            .setCancelable(false)
                                                                                                            .show();

                                                                                                }
                                                                                            },
                ProApplication.getInstance().getUserId(),
                ProApplication.getInstance().getDataSelected().getId());
    }

    @Override
    public void callbackForAlert(String result, int i) {
        if (result.equalsIgnoreCase("ok")&&i==1) {
            renew();
        }
    }
}
