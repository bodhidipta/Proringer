package com.android.llc.proringer.activities;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.ScrollView;

import com.android.llc.proringer.R;
import com.android.llc.proringer.adapter.ProDetailsServiceAreaAdapter;
import com.android.llc.proringer.adapter.ProsDetailsBusinessHourAdapter;
import com.android.llc.proringer.adapter.ProsDetailsImageAdapter;
import com.android.llc.proringer.adapter.ProsDetailsLicenseAdapter;
import com.android.llc.proringer.adapter.ProsDetailsPortfolioImageAdapter;
import com.android.llc.proringer.adapter.ProsDetailsServiceAdapter;
import com.android.llc.proringer.appconstant.ProApplication;
import com.android.llc.proringer.helper.ProServiceApiHelper;
import com.android.llc.proringer.utils.Logger;
import com.android.llc.proringer.viewsmod.textview.ProRegularTextView;
import com.bumptech.glide.Glide;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by su on 7/12/17.
 */

public class ProProjectDetailsActivity extends AppCompatActivity {
    ImageView img_back, img_top, img_profile, img_achievements;
    String pros_id = "";
    RecyclerView rcv_service, rcv_business_hour, rcv_service_area, rcv_license, rcv_project_gallery;
    JSONObject jsonObject = null;
    ProgressDialog pgDialog1,pgDialog2;
    RatingBar rbar;
    ProsDetailsServiceAdapter proDetailsService;
    ProsDetailsBusinessHourAdapter prosDetailsBusinessHourAdapter;
    ProDetailsServiceAreaAdapter proDetailsServiceAreaAdapter;
    ProsDetailsLicenseAdapter prosDetailsLicenseAdapter;
    ProsDetailsImageAdapter prosDetailsImageAdapter;

    JSONObject infoArrayJsonObject=null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_details_pro);

        img_back = (ImageView) findViewById(R.id.img_back);
        img_top = (ImageView) findViewById(R.id.img_top);
        img_profile = (ImageView) findViewById(R.id.img_profile);
        img_achievements = (ImageView) findViewById(R.id.img_achievements);

        rbar = (RatingBar) findViewById(R.id.rbar);

        rcv_service = (RecyclerView) findViewById(R.id.rcv_service);
        rcv_service.setLayoutManager(new GridLayoutManager(ProProjectDetailsActivity.this, 2));

        rcv_business_hour = (RecyclerView) findViewById(R.id.rcv_business_hour);
        rcv_business_hour.setLayoutManager(new LinearLayoutManager(ProProjectDetailsActivity.this));

        rcv_service_area = (RecyclerView) findViewById(R.id.rcv_service_area);
        rcv_service_area.setLayoutManager(new GridLayoutManager(ProProjectDetailsActivity.this, 2));

        rcv_license = (RecyclerView) findViewById(R.id.rcv_license);
        rcv_license.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));


        rcv_project_gallery = (RecyclerView) findViewById(R.id.rcv_project_gallery);
        rcv_project_gallery.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));


        findViewById(R.id.LLViewAll).setVisibility(View.GONE);

        findViewById(R.id.view_all_service_area).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                findViewById(R.id.LLViewAll).setVisibility(View.VISIBLE);
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
        }

        findViewById(R.id.tv_review_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProProjectDetailsActivity.this, ProReviewActivity.class);
                intent.putExtra("pros_id", pros_id);
                startActivity(intent);
            }
        });

        findViewById(R.id.tv_show_more_describetion).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                if (infoArrayJsonObject!=null && !infoArrayJsonObject.getJSONObject("about").getString("description").trim().equals("")){

                        showDescribetion(infoArrayJsonObject.getJSONObject("about").getString("description"));
                }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        setDataProListDetails();
    }

    public void setDataProListDetails() {
        ProServiceApiHelper.getInstance(ProProjectDetailsActivity.this).getProIndividualListing(new ProServiceApiHelper.getApiProcessCallback() {
                                                                                                    @Override
                                                                                                    public void onStart() {
                                                                                                        pgDialog1 = new ProgressDialog(ProProjectDetailsActivity.this);
                                                                                                        pgDialog1.setTitle("Local Pros Details");
                                                                                                        pgDialog1.setCancelable(false);
                                                                                                        pgDialog1.setMessage("Getting local pros details.Please wait...");
                                                                                                        pgDialog1.show();
                                                                                                    }

                                                                                                    @Override
                                                                                                    public void onComplete(String message) {
                                                                                                        if (pgDialog1 != null && pgDialog1.isShowing())
                                                                                                            pgDialog1.dismiss();

                                                                                                        Logger.printMessage("message", "" + message);
                                                                                                        try {
                                                                                                            jsonObject = new JSONObject(message);
                                                                                                            infoArrayJsonObject = jsonObject.getJSONObject("info_array");
                                                                                                            JSONObject infoJsonObject = infoArrayJsonObject.getJSONObject("info");


                                                                                                            if (!infoJsonObject.getString("header_image").equals(""))
                                                                                                                Glide.with(ProProjectDetailsActivity.this).load(infoJsonObject.getString("header_image")).centerCrop().into(img_top);


                                                                                                            if (!infoJsonObject.getString("profile_image").equals(""))
                                                                                                                Glide.with(ProProjectDetailsActivity.this).load(infoJsonObject.getString("profile_image")).centerCrop().into(img_profile);

                                                                                                            ((ProRegularTextView) findViewById(R.id.tv_company_name)).setText(infoJsonObject.getString("company_name"));
                                                                                                            ((ProRegularTextView) findViewById(R.id.tv_user_name)).setText(infoJsonObject.getString("user_name"));
                                                                                                            ((ProRegularTextView) findViewById(R.id.tv_address)).setText(infoJsonObject.getString("address"));
                                                                                                            ((ProRegularTextView) findViewById(R.id.tv_city_state_zipcode)).setText(infoJsonObject.getString("city") + ", " + infoJsonObject.getString("state") + " " + infoJsonObject.getString("zipcode"));


                                                                                                            ((ProRegularTextView)findViewById(R.id.tv_review_value)).setText(infoArrayJsonObject.getString("total_review"));
                                                                                                            ((ProRegularTextView) findViewById(R.id.tv_rate_value)).setText(infoArrayJsonObject.getString("total_avg_review"));


                                                                                                            rbar.setRating(Float.parseFloat(infoArrayJsonObject.getString("total_avg_review")));

                                                                                                            ((ProRegularTextView) findViewById(R.id.tv_about)).setText(infoArrayJsonObject.getJSONObject("about").getString("description"));

                                                                                                            proDetailsService = new ProsDetailsServiceAdapter(ProProjectDetailsActivity.this, infoArrayJsonObject.getJSONArray("services"));
                                                                                                            rcv_service.setAdapter(proDetailsService);

                                                                                                            if (infoJsonObject.getString("business_hour").trim().equals("0")) {
                                                                                                                rcv_business_hour.setVisibility(View.VISIBLE);
                                                                                                                ((ProRegularTextView) findViewById(R.id.tv_business_hour)).setVisibility(View.GONE);
                                                                                                                prosDetailsBusinessHourAdapter = new ProsDetailsBusinessHourAdapter(ProProjectDetailsActivity.this, infoArrayJsonObject.getJSONArray("business_hours"));
                                                                                                                rcv_business_hour.setAdapter(prosDetailsBusinessHourAdapter);
                                                                                                            } else {
                                                                                                                rcv_business_hour.setVisibility(View.GONE);
                                                                                                                ((ProRegularTextView) findViewById(R.id.tv_business_hour)).setVisibility(View.VISIBLE);
                                                                                                                ((ProRegularTextView) findViewById(R.id.tv_business_hour)).setText("Always Open");
                                                                                                            }


                                                                                                            proDetailsServiceAreaAdapter = new ProDetailsServiceAreaAdapter(ProProjectDetailsActivity.this, infoArrayJsonObject.getJSONArray("service_area"));
                                                                                                            rcv_service_area.setAdapter(proDetailsServiceAreaAdapter);

                                                                                                            JSONObject company_infoJsonOBJ = infoArrayJsonObject.getJSONObject("company_info");

                                                                                                            ((ProRegularTextView) findViewById(R.id.tv_business_since)).setText(company_infoJsonOBJ.getString("business_since"));
                                                                                                            ((ProRegularTextView) findViewById(R.id.tv_no_of_employee)).setText(company_infoJsonOBJ.getString("no_of_employee"));
                                                                                                            ((ProRegularTextView) findViewById(R.id.tv_proringer_awarded)).setText(company_infoJsonOBJ.getString("proringer_awarded"));
                                                                                                            ((ProRegularTextView) findViewById(R.id.tv_business_review)).setText(company_infoJsonOBJ.getString("business_review"));
                                                                                                            ((ProRegularTextView) findViewById(R.id.tv_last_verified_on)).setText(company_infoJsonOBJ.getString("last_verified_on"));


                                                                                                            prosDetailsLicenseAdapter = new ProsDetailsLicenseAdapter(ProProjectDetailsActivity.this, infoArrayJsonObject.getJSONArray("licence"));
                                                                                                            rcv_license.setAdapter(prosDetailsLicenseAdapter);

                                                                                                            ((ProRegularTextView) findViewById(R.id.tv_no_of_project_value)).setText(infoArrayJsonObject.getString("total_project"));
                                                                                                            ((ProRegularTextView) findViewById(R.id.tv_no_of_picture_value)).setText(infoArrayJsonObject.getString("total_picture"));


                                                                                                            prosDetailsImageAdapter = new ProsDetailsImageAdapter(ProProjectDetailsActivity.this, infoArrayJsonObject.getJSONArray("project_gallery"), new onOptionSelected() {
                                                                                                                @Override
                                                                                                                public void onItemPassed(int position, String value) {
                                                                                                                    showImagePortFolioDialog(value);
                                                                                                                }
                                                                                                            });
                                                                                                            rcv_project_gallery.setAdapter(prosDetailsImageAdapter);

                                                                                                            if (!infoArrayJsonObject.getString("achievement").equals(""))
                                                                                                                Glide.with(ProProjectDetailsActivity.this).load(infoArrayJsonObject.getString("achievement")).centerCrop().into(img_achievements);


                                                                                                        } catch (JSONException e) {
                                                                                                            e.printStackTrace();
                                                                                                        }

                                                                                                    }

                                                                                                    @Override
                                                                                                    public void onError(String error) {
                                                                                                        if (pgDialog1 != null && pgDialog1.isShowing())
                                                                                                            pgDialog1.dismiss();
                                                                                                    }
                                                                                                },
//                "56"
                ProApplication.getInstance().getUserId()
                ,
                pros_id
//                "82"
        );
    }


    public void showImagePortFolioDialog(String portfolio_id) {

        ProServiceApiHelper.getInstance(ProProjectDetailsActivity.this).getProIndividualPortfolioImage(new ProServiceApiHelper.getApiProcessCallback() {
            @Override
            public void onStart() {
                pgDialog2 = new ProgressDialog(ProProjectDetailsActivity.this);
                pgDialog2.setTitle("Portfolio Images");
                pgDialog2.setCancelable(false);
                pgDialog2.setMessage("Getting Portfolio Images list.Please wait...");
                pgDialog2.show();
            }

            @Override
            public void onComplete(String message) {
                if (pgDialog2 != null && pgDialog2.isShowing())
                    pgDialog2.dismiss();

                try {
                    JSONObject portfolioObj=new JSONObject(message);
                    JSONArray portfolioInfoArray=portfolioObj.getJSONArray("info_array");


                    final Dialog dialog = new Dialog(ProProjectDetailsActivity.this);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//                    dialog.setCancelable(false);
                    dialog.setContentView(R.layout.custom_dialogbox_portfolio);
                    //dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));

                    RecyclerView rcv_portfolio = (RecyclerView) dialog.findViewById(R.id.rcv_portfolio);
                    rcv_portfolio.setLayoutManager(new LinearLayoutManager(ProProjectDetailsActivity.this, LinearLayoutManager.HORIZONTAL, false));

                    ProsDetailsPortfolioImageAdapter prosDetailsPortfolioImageAdapter=new ProsDetailsPortfolioImageAdapter(ProProjectDetailsActivity.this,portfolioInfoArray);
                    rcv_portfolio.setAdapter(prosDetailsPortfolioImageAdapter);
                    dialog.show();


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onError(String error) {
                if (pgDialog2 != null && pgDialog2.isShowing())
                    pgDialog2.dismiss();
            }
        },portfolio_id);




    }

    public interface onOptionSelected {
        void onItemPassed(int position, String value);
    }


    private void showDescribetion(String msg){
        final Dialog dialog = new Dialog(ProProjectDetailsActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//                    dialog.setCancelable(false);
        dialog.setContentView(R.layout.custom_dialogbox_pro_details_describetion);
        //dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));

        ScrollView scrollView = (ScrollView) dialog.findViewById(R.id.scrollView);
        ProRegularTextView tv_show_describetion = (ProRegularTextView) dialog.findViewById(R.id.tv_show_describetion);


        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;

        scrollView.getLayoutParams().width = (width-30);
//        scrollView.getLayoutParams().height = (height-30)/2;

        tv_show_describetion.setText(msg);
        dialog.show();
    }

}
