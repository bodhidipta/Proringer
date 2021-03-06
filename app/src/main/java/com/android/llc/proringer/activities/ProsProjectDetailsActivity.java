package com.android.llc.proringer.activities;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.llc.proringer.ImagePinching.graphics.ImageViewTouch;
import com.android.llc.proringer.ImagePinching.graphics.ImageViewTouchBase;
import com.android.llc.proringer.R;
import com.android.llc.proringer.adapter.ProDetailsServiceAreaAdapter;
import com.android.llc.proringer.adapter.ProDetailsServiceAreaDialogAdapter;
import com.android.llc.proringer.adapter.ProDetailsServiceDialogAdapter;
import com.android.llc.proringer.adapter.ProsDetailsBusinessHourAdapter;
import com.android.llc.proringer.adapter.ProsDetailsImageAdapter;
import com.android.llc.proringer.adapter.ProsDetailsLicenseAdapter;
import com.android.llc.proringer.adapter.ProsDetailsServiceAdapter;
import com.android.llc.proringer.appconstant.ProApplication;
import com.android.llc.proringer.helper.CustomAlert;
import com.android.llc.proringer.helper.MyCustomAlertListener;
import com.android.llc.proringer.helper.MyLoader;
import com.android.llc.proringer.helper.ProServiceApiHelper;
import com.android.llc.proringer.helper.ShowMyDialog;
import com.android.llc.proringer.utils.Logger;
import com.android.llc.proringer.utils.MethodsUtils;
import com.android.llc.proringer.viewsmod.textview.ProRegularTextView;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by su on 7/12/17.
 */

public class ProsProjectDetailsActivity extends AppCompatActivity implements MyCustomAlertListener {
    ImageView img_back, img_top, img_profile, img_achievements;
    String pros_id = "", pros_company_name = "";
    RecyclerView rcv_service, rcv_business_hour, rcv_service_area, rcv_license, rcv_project_gallery;
    JSONObject jsonObject = null;
    MyLoader myLoader = null;

    ProsDetailsServiceAdapter proDetailsService;
    ProsDetailsBusinessHourAdapter prosDetailsBusinessHourAdapter;
    ProsDetailsLicenseAdapter prosDetailsLicenseAdapter;
    ProsDetailsImageAdapter prosDetailsImageAdapter;

    JSONArray services;
    String contact_info_status = "";

    JSONObject infoArrayJsonObject = null;
    JSONObject infoJsonObject = null;

    RelativeLayout RLCollapsingImage;
    LinearLayout LLNetworkDisconnection;
    NestedScrollView nested_scroll_main;

    CardView cardview_contact_pro;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_details_pro);

        RLCollapsingImage = (RelativeLayout) findViewById(R.id.RLCollapsingImage);
        nested_scroll_main = (NestedScrollView) findViewById(R.id.nested_scroll_main);
        LLNetworkDisconnection = (LinearLayout) findViewById(R.id.LLNetworkDisconnection);

        img_back = (ImageView) findViewById(R.id.img_back);
        img_top = (ImageView) findViewById(R.id.img_top);
        img_profile = (ImageView) findViewById(R.id.img_profile);
        img_achievements = (ImageView) findViewById(R.id.img_achievements);

        cardview_contact_pro = (CardView) findViewById(R.id.cardview_contact_pro);

        rcv_service = (RecyclerView) findViewById(R.id.rcv_service);
        rcv_service.setLayoutManager(new GridLayoutManager(ProsProjectDetailsActivity.this, 2));

        rcv_business_hour = (RecyclerView) findViewById(R.id.rcv_business_hour);
        rcv_business_hour.setLayoutManager(new LinearLayoutManager(ProsProjectDetailsActivity.this));

        rcv_service_area = (RecyclerView) findViewById(R.id.rcv_service_area);
        rcv_service_area.setLayoutManager(new GridLayoutManager(ProsProjectDetailsActivity.this, 2));

        rcv_license = (RecyclerView) findViewById(R.id.rcv_license);
        rcv_license.setLayoutManager(new GridLayoutManager(ProsProjectDetailsActivity.this, 2));


        rcv_project_gallery = (RecyclerView) findViewById(R.id.rcv_project_gallery);
        rcv_project_gallery.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        myLoader = new MyLoader(ProsProjectDetailsActivity.this);


        findViewById(R.id.tv_view_all).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {

                    Logger.printMessage("pros_id","-->"+pros_id);
                    Intent intent = new Intent(ProsProjectDetailsActivity.this, ProsReviewAllListActivity.class);
                    intent.putExtra("pros_company_name", pros_company_name);
                    intent.putExtra("pros_id", pros_id);
                    intent.putExtra("total_avg_review", infoArrayJsonObject.getString("total_avg_review"));
                    intent.putExtra("img", infoJsonObject.getString("profile_image"));
                    intent.putExtra("total_review", infoArrayJsonObject.getString("total_review"));
                    startActivity(intent);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        cardview_contact_pro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(ProsProjectDetailsActivity.this, ContactProServiceActivity.class);
                intent.putExtra("pros_company_name", pros_company_name);
                intent.putExtra("services", services.toString());
                intent.putExtra("contact_info_status", contact_info_status);
                intent.putExtra("pros_id", pros_id);
                startActivity(intent);
            }
        });


        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        if (getIntent().getExtras() != null) {
            pros_id = getIntent().getExtras().getString("pros_id");
            pros_company_name = getIntent().getExtras().getString("pros_company_name");
        }

        ((ProRegularTextView) findViewById(R.id.tv_toolbar)).setText(pros_company_name);

        findViewById(R.id.tv_review_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (infoJsonObject != null) {

                        Intent intent = new Intent(ProsProjectDetailsActivity.this, ProsReviewActivity.class);
                        intent.putExtra("pros_id", pros_id);
                        intent.putExtra("img", infoJsonObject.getString("profile_image"));
                        startActivity(intent);
                    } else {
                        Toast.makeText(ProsProjectDetailsActivity.this, "Details Page Loading problem", Toast.LENGTH_LONG).show();
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        findViewById(R.id.tv_show_more_describetion).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (infoArrayJsonObject != null && !infoArrayJsonObject.getJSONObject("about").getString("description").trim().equals("")) {

                        // showDescribetionDialog(infoArrayJsonObject.getJSONObject("about").getString("description"));
                        new ShowMyDialog(ProsProjectDetailsActivity.this).showDescribetionDialog("About", infoArrayJsonObject.getJSONObject("about").getString("description"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        setDataProListDetails();

    }

//    @Override
//    protected void onResume() {
//        super.onResume();
//        setDataProListDetails();
//    }


    public void setDataProListDetails() {

        RLCollapsingImage.setVisibility(View.VISIBLE);
        nested_scroll_main.setVisibility(View.VISIBLE);
        LLNetworkDisconnection.setVisibility(View.GONE);


        ProServiceApiHelper.getInstance(ProsProjectDetailsActivity.this).getProsIndividualListingAPI(new ProServiceApiHelper.getApiProcessCallback() {

                                                                                                         @Override
                                                                                                         public void onStart() {
                                                                                                             myLoader.showLoader();
                                                                                                         }

                                                                                                         @Override
                                                                                                         public void onComplete(String message) {
                                                                                                             if (myLoader != null && myLoader.isMyLoaderShowing())
                                                                                                                 myLoader.dismissLoader();

                                                                                                             Logger.printMessage("message", "" + message);
                                                                                                             try {
                                                                                                                 jsonObject = new JSONObject(message);
                                                                                                                 infoArrayJsonObject = jsonObject.getJSONObject("info_array");
                                                                                                                 infoJsonObject = infoArrayJsonObject.getJSONObject("info");

                                                                                                                 contact_info_status = infoJsonObject.getString("contact_info_status");
                                                                                                                 services = infoArrayJsonObject.getJSONArray("services");


                                                                                                                 if (!infoJsonObject.getString("header_image").equals(""))
                                                                                                                     Glide.with(ProsProjectDetailsActivity.this).load(infoJsonObject.getString("header_image")).centerCrop().into(img_top);


                                                                                                                 if (!infoJsonObject.getString("profile_image").equals(""))
                                                                                                                     Glide.with(ProsProjectDetailsActivity.this).load(infoJsonObject.getString("profile_image")).centerCrop().into(img_profile);


                                                                                                                 if (infoJsonObject.getString("verified_status").trim().equalsIgnoreCase("Y")) {
                                                                                                                     ((ProRegularTextView) findViewById(R.id.tv_contact_pro_btn_unverified)).setVisibility(View.GONE);
                                                                                                                     ((LinearLayout) findViewById(R.id.LLVerified)).setVisibility(View.VISIBLE);
                                                                                                                 } else {
                                                                                                                     ((LinearLayout) findViewById(R.id.LLVerified)).setVisibility(View.GONE);
                                                                                                                     ((ProRegularTextView) findViewById(R.id.tv_contact_pro_btn_unverified)).setVisibility(View.VISIBLE);
                                                                                                                 }

                                                                                                                 ((ProRegularTextView) findViewById(R.id.tv_company_name)).setText(infoJsonObject.getString("company_name"));
                                                                                                                 ((ProRegularTextView) findViewById(R.id.tv_user_name)).setText(infoJsonObject.getString("user_name"));
                                                                                                                 ((ProRegularTextView) findViewById(R.id.tv_designation)).setText(infoJsonObject.getString("designation"));
                                                                                                                 ((ProRegularTextView) findViewById(R.id.tv_address)).setText(infoJsonObject.getString("address"));
                                                                                                                 ((ProRegularTextView) findViewById(R.id.tv_city_state_zipcode)).setText(infoJsonObject.getString("city") + ", " + infoJsonObject.getString("state") + " " + infoJsonObject.getString("zipcode"));


                                                                                                                 ((ProRegularTextView) findViewById(R.id.tv_review_value)).setText(infoArrayJsonObject.getString("total_review"));
                                                                                                                 ((ProRegularTextView) findViewById(R.id.tv_rate_value)).setText(infoArrayJsonObject.getString("total_avg_review"));

                                                                                                                 ((RatingBar) findViewById(R.id.rbar)).setRating(Float.parseFloat(infoArrayJsonObject.getString("total_avg_review")));

                                                                                                                 ((ProRegularTextView) findViewById(R.id.tv_about)).setText(infoArrayJsonObject.getJSONObject("about").getString("description"));


                                                                                                                 if (infoArrayJsonObject.getJSONArray("services").length() > 14) {
                                                                                                                     ((ProRegularTextView) findViewById(R.id.view_all_service)).setVisibility(View.VISIBLE);
                                                                                                                 } else {
                                                                                                                     ((ProRegularTextView) findViewById(R.id.view_all_service)).setVisibility(View.GONE);
                                                                                                                 }

                                                                                                                 findViewById(R.id.view_all_service).setOnClickListener(new View.OnClickListener() {
                                                                                                                     @Override
                                                                                                                     public void onClick(View view) {
                                                                                                                         try {
                                                                                                                             if (infoArrayJsonObject.getJSONArray("services").length() > 14) {
                                                                                                                                 showServicesDialog(infoArrayJsonObject.getJSONArray("services"));
                                                                                                                             }
                                                                                                                         } catch (JSONException e) {
                                                                                                                             e.printStackTrace();
                                                                                                                         }
                                                                                                                     }
                                                                                                                 });


                                                                                                                 proDetailsService = new ProsDetailsServiceAdapter(ProsProjectDetailsActivity.this, infoArrayJsonObject.getJSONArray("services"), new onOptionSelected() {
                                                                                                                     @Override
                                                                                                                     public void onItemPassed(int position, String value) {
                                                                                                                         try {
                                                                                                                             if (value.equalsIgnoreCase("more")) {
                                                                                                                                 showServicesDialog(infoArrayJsonObject.getJSONArray("services"));
                                                                                                                             }
                                                                                                                         } catch (JSONException e) {
                                                                                                                             e.printStackTrace();
                                                                                                                         }
                                                                                                                     }
                                                                                                                 });
                                                                                                                 rcv_service.setAdapter(proDetailsService);

                                                                                                                 if (infoJsonObject.getString("business_hour").trim().equals("0")) {
                                                                                                                     rcv_business_hour.setVisibility(View.VISIBLE);
                                                                                                                     ((ProRegularTextView) findViewById(R.id.tv_business_hour)).setVisibility(View.GONE);
                                                                                                                     prosDetailsBusinessHourAdapter = new ProsDetailsBusinessHourAdapter(ProsProjectDetailsActivity.this, infoArrayJsonObject.getJSONArray("business_hours"));
                                                                                                                     rcv_business_hour.setAdapter(prosDetailsBusinessHourAdapter);
                                                                                                                 } else {
                                                                                                                     rcv_business_hour.setVisibility(View.GONE);
                                                                                                                     ((ProRegularTextView) findViewById(R.id.tv_business_hour)).setVisibility(View.VISIBLE);
                                                                                                                     ((ProRegularTextView) findViewById(R.id.tv_business_hour)).setText("Always Open");
                                                                                                                 }

                                                                                                                 if (infoArrayJsonObject.getJSONArray("service_area").length() > 14) {
                                                                                                                     findViewById(R.id.view_all_service_area).setVisibility(View.VISIBLE);
                                                                                                                 } else {
                                                                                                                     findViewById(R.id.view_all_service_area).setVisibility(View.GONE);
                                                                                                                 }

                                                                                                                 findViewById(R.id.view_all_service_area).setOnClickListener(new View.OnClickListener() {
                                                                                                                     @Override
                                                                                                                     public void onClick(View view) {
                                                                                                                         try {
                                                                                                                             if (infoArrayJsonObject.getJSONArray("service_area").length() > 14) {
                                                                                                                                 showServiceAreaDialog(infoArrayJsonObject.getJSONArray("service_area"));
                                                                                                                             }
                                                                                                                         } catch (JSONException e) {
                                                                                                                             e.printStackTrace();
                                                                                                                         }
                                                                                                                     }
                                                                                                                 });

                                                                                                                 ProDetailsServiceAreaAdapter proDetailsServiceAreaAdapter = new ProDetailsServiceAreaAdapter(ProsProjectDetailsActivity.this, infoArrayJsonObject.getJSONArray("service_area"), new onOptionSelected() {
                                                                                                                     @Override
                                                                                                                     public void onItemPassed(int position, String value) {
                                                                                                                         try {
                                                                                                                             if (value.equalsIgnoreCase("more")) {

                                                                                                                                 showServiceAreaDialog(infoArrayJsonObject.getJSONArray("service_area"));
                                                                                                                             }
                                                                                                                         } catch (JSONException e) {
                                                                                                                             e.printStackTrace();
                                                                                                                         }
                                                                                                                     }
                                                                                                                 });
                                                                                                                 rcv_service_area.setAdapter(proDetailsServiceAreaAdapter);

                                                                                                                 JSONObject company_infoJsonOBJ = infoArrayJsonObject.getJSONObject("company_info");

                                                                                                                 ((ProRegularTextView) findViewById(R.id.tv_business_since)).setText(company_infoJsonOBJ.getString("business_since"));
                                                                                                                 ((ProRegularTextView) findViewById(R.id.tv_no_of_employee)).setText(company_infoJsonOBJ.getString("no_of_employee"));
                                                                                                                 ((ProRegularTextView) findViewById(R.id.tv_proringer_awarded)).setText(company_infoJsonOBJ.getString("proringer_awarded"));
                                                                                                                 ((ProRegularTextView) findViewById(R.id.tv_business_review)).setText(company_infoJsonOBJ.getString("business_review"));
                                                                                                                 ((ProRegularTextView) findViewById(R.id.tv_last_verified_on)).setText(company_infoJsonOBJ.getString("last_verified_on"));


                                                                                                                 prosDetailsLicenseAdapter = new ProsDetailsLicenseAdapter(ProsProjectDetailsActivity.this, infoArrayJsonObject.getJSONArray("licence"));
                                                                                                                 rcv_license.setAdapter(prosDetailsLicenseAdapter);

                                                                                                                 ((ProRegularTextView) findViewById(R.id.tv_no_of_project_value)).setText(infoArrayJsonObject.getString("total_project"));
                                                                                                                 ((ProRegularTextView) findViewById(R.id.tv_no_of_picture_value)).setText(infoArrayJsonObject.getString("total_picture"));


                                                                                                                 prosDetailsImageAdapter = new ProsDetailsImageAdapter(ProsProjectDetailsActivity.this, infoArrayJsonObject.getJSONArray("project_gallery"), new onOptionSelected() {
                                                                                                                     @Override
                                                                                                                     public void onItemPassed(int position, String value) {
                                                                                                                         Intent intent = new Intent(ProsProjectDetailsActivity.this, ProsProjectGalleryActivity.class);
                                                                                                                         intent.putExtra("portfolio_id", value);
                                                                                                                         startActivity(intent);
                                                                                                                     }
                                                                                                                 });
                                                                                                                 rcv_project_gallery.setAdapter(prosDetailsImageAdapter);

                                                                                                                 if (!infoArrayJsonObject.getString("achievement").equals(""))
                                                                                                                     Glide.with(ProsProjectDetailsActivity.this).load(infoArrayJsonObject.getString("achievement"))
//                                                                                                                          .centerCrop()
                                                                                                                             .into(img_achievements);


                                                                                                             } catch (JSONException e) {
                                                                                                                 e.printStackTrace();
                                                                                                             }

                                                                                                         }

                                                                                                         @Override
                                                                                                         public void onError(String error) {
                                                                                                             if (myLoader != null && myLoader.isMyLoaderShowing())
                                                                                                                 myLoader.dismissLoader();

                                                                                                             if (error.equalsIgnoreCase(getResources().getString(R.string.no_internet_connection_found_Please_check_your_internet_connection))) {
                                                                                                                 RLCollapsingImage.setVisibility(View.GONE);
                                                                                                                 nested_scroll_main.setVisibility(View.GONE);
                                                                                                                 LLNetworkDisconnection.setVisibility(View.VISIBLE);
                                                                                                             }

                                                                                                             CustomAlert customAlert = new CustomAlert(ProsProjectDetailsActivity.this, "Contact Us Error", "" + error, ProsProjectDetailsActivity.this);
                                                                                                             customAlert.getListenerRetryCancelFromNormalAlert("retry", "abort", 1);
                                                                                                         }
                                                                                                     },
//                "56"
                ProApplication.getInstance().getUserId()
                ,
                pros_id
//                "82"
        );
    }

    @Override
    public void callbackForAlert(String result, int i) {
        if (result.equalsIgnoreCase("retry") && i == 1) {
            setDataProListDetails();
        }
    }


    public interface onOptionSelected {
        void onItemPassed(int position, String value);
    }

    private void showServiceAreaDialog(JSONArray serviceAreaJsonArray) {
        final Dialog dialog = new Dialog(ProsProjectDetailsActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//                    dialog.setCancelable(false);
        dialog.setContentView(R.layout.custom_dialogbox_pro_details_service_area);
        //dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));

        ((ProRegularTextView) dialog.findViewById(R.id.tv_title)).setText("Service Area");

        RecyclerView rcv_show_service_area = (RecyclerView) dialog.findViewById(R.id.rcv_show_service_area);
        rcv_show_service_area.setLayoutManager(new GridLayoutManager(ProsProjectDetailsActivity.this, 2));

        dialog.findViewById(R.id.img_cancel_dialog).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        ProDetailsServiceAreaDialogAdapter proDetailsServiceAreaDialogAdapter = new ProDetailsServiceAreaDialogAdapter(ProsProjectDetailsActivity.this, serviceAreaJsonArray);
        rcv_show_service_area.setAdapter(proDetailsServiceAreaDialogAdapter);

        rcv_show_service_area.getLayoutParams().width = (MethodsUtils.getScreenHeightAndWidth(ProsProjectDetailsActivity.this)[1] - 30);
//        rcv_show_service_area.getLayoutParams().height = (MethodsUtils.getScreenHeightAndWidth(ProsProjectDetailsActivity.this)[1] - 30);
        rcv_show_service_area.getLayoutParams().height = RecyclerView.LayoutParams.WRAP_CONTENT;
        //        rcv_show_service_area.getLayoutParams().height = (height-30)/2;

        dialog.show();
    }

    private void showServicesDialog(JSONArray serviceAreaJsonArray) {
        final Dialog dialog = new Dialog(ProsProjectDetailsActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//                    dialog.setCancelable(false);
        dialog.setContentView(R.layout.custom_dialogbox_pro_details_service_area);
        //dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));

        ((ProRegularTextView) dialog.findViewById(R.id.tv_title)).setText("Services");

        RecyclerView rcv_show_service_area = (RecyclerView) dialog.findViewById(R.id.rcv_show_service_area);
        rcv_show_service_area.setLayoutManager(new GridLayoutManager(ProsProjectDetailsActivity.this, 2));

        dialog.findViewById(R.id.img_cancel_dialog).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        ProDetailsServiceDialogAdapter proDetailsServiceDialogAdapter = new ProDetailsServiceDialogAdapter(ProsProjectDetailsActivity.this, serviceAreaJsonArray);
        rcv_show_service_area.setAdapter(proDetailsServiceDialogAdapter);

        rcv_show_service_area.getLayoutParams().width = (MethodsUtils.getScreenHeightAndWidth(ProsProjectDetailsActivity.this)[1] - 30);
//        rcv_show_service_area.getLayoutParams().height = (MethodsUtils.getScreenHeightAndWidth(ProsProjectDetailsActivity.this)[1] - 30);
        rcv_show_service_area.getLayoutParams().height = RecyclerView.LayoutParams.WRAP_CONTENT;
//        rcv_show_service_area.getLayoutParams().height = (height-30)/2;

        dialog.show();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_CANCELED) {
            return;
        } else {
            setDataProListDetails();
        }
    }


    public void showImageProsLicenceDialog(String url) {

        Logger.printMessage("url", url);
        final Dialog dialog = new Dialog(ProsProjectDetailsActivity.this, android.R.style.Theme_Light);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.custom_dialogbox_portfolio);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        dialog.findViewById(R.id.RelativeMainLL).getLayoutParams().width = (MethodsUtils.getScreenHeightAndWidth(ProsProjectDetailsActivity.this)[1]);
        dialog.findViewById(R.id.RelativeMainLL).getLayoutParams().height = MethodsUtils.getScreenHeightAndWidth(ProsProjectDetailsActivity.this)[0];


        dialog.findViewById(R.id.img_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });


        ImageViewTouch mImage = (ImageViewTouch) dialog.findViewById(R.id.imageview_dialog);

        // set the default image display type
        mImage.setDisplayType(ImageViewTouchBase.DisplayType.FIT_WIDTH);
        Glide.with(ProsProjectDetailsActivity.this).load(url).into(mImage);


        mImage.setSingleTapListener(
                new ImageViewTouch.OnImageViewTouchSingleTapListener() {

                    @Override
                    public void onSingleTapConfirmed() {
                        Logger.printMessage("onSingleTapConfirmed", "onSingleTapConfirmed");
                    }
                }
        );


        mImage.setDoubleTapListener(
                new ImageViewTouch.OnImageViewTouchDoubleTapListener() {

                    @Override
                    public void onDoubleTap() {
                        Logger.printMessage("onDoubleTap", "onDoubleTap");
                    }
                }
        );

        mImage.setOnDrawableChangedListener(
                new ImageViewTouchBase.OnDrawableChangeListener() {

                    @Override
                    public void onDrawableChanged(Drawable drawable) {
                        Logger.printMessage("onBitmapChanged", "onBitmapChanged: " + drawable);
                    }
                }
        );


        dialog.show();
    }

}
