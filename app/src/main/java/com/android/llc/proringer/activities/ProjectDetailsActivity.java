package com.android.llc.proringer.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import com.android.llc.proringer.R;
import com.android.llc.proringer.adapter.ProDetailsBusinessHourAdapter;
import com.android.llc.proringer.adapter.ProDetailsLicenseAdapter;
import com.android.llc.proringer.adapter.ProDetailsServiceAdapter;
import com.android.llc.proringer.adapter.ProDetailsServiceAreaAdapter;
import com.android.llc.proringer.adapter.ProsProjectDetailsAdapter;
import com.android.llc.proringer.helper.ProServiceApiHelper;
import com.android.llc.proringer.utils.Logger;
import com.android.llc.proringer.viewsmod.textview.ProRegularTextView;
import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by su on 7/12/17.
 */

public class ProjectDetailsActivity extends AppCompatActivity {
    ImageView img_back, img_top, img_profile,img_achievements;
    String pros_id = "";
    RecyclerView rcv_service,rcv_business_hour,rcv_service_area,rcv_license,rcv_project_gallery;
    JSONObject jsonObject = null;
    ProgressDialog pgDialog1;
    ProRegularTextView tv_review_btn,tv_review_value,tv_rate_value,tv_about,tv_company_name,
            tv_user_name,tv_address,tv_city_state_zipcode,tv_business_since,tv_no_of_employee
            ,tv_proringer_awarded,tv_business_review,tv_last_verified_on,view_all_service_area
            ,tv_no_of_project_value,tv_no_of_picture_value,tv_business_hour;
    RatingBar rbar;
    ProDetailsServiceAdapter proDetailsService;
    ProDetailsBusinessHourAdapter proDetailsBusinessHourAdapter;
    ProDetailsServiceAreaAdapter proDetailsServiceAreaAdapter;
    ProDetailsLicenseAdapter proDetailsLicenseAdapter;
    ProsProjectDetailsAdapter prosProjectDetailsAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_details_pro);

        img_back = (ImageView) findViewById(R.id.img_back);
        img_top = (ImageView) findViewById(R.id.img_top);
        img_profile = (ImageView) findViewById(R.id.img_profile);
        img_achievements = (ImageView) findViewById(R.id.img_achievements);

        tv_review_btn = (ProRegularTextView) findViewById(R.id.tv_review_btn);
        tv_review_value= (ProRegularTextView) findViewById(R.id.tv_review_value);
        tv_rate_value= (ProRegularTextView) findViewById(R.id.tv_rate_value);
        tv_about= (ProRegularTextView) findViewById(R.id.tv_about);
        tv_company_name= (ProRegularTextView) findViewById(R.id.tv_company_name);
        tv_user_name= (ProRegularTextView) findViewById(R.id.tv_user_name);
        tv_address= (ProRegularTextView) findViewById(R.id.tv_address);
        tv_city_state_zipcode= (ProRegularTextView) findViewById(R.id.tv_city_state_zipcode);
        tv_business_hour= (ProRegularTextView) findViewById(R.id.tv_business_hour);

        tv_business_since= (ProRegularTextView) findViewById(R.id.tv_business_since);
        tv_no_of_employee= (ProRegularTextView) findViewById(R.id.tv_no_of_employee);
        tv_proringer_awarded= (ProRegularTextView) findViewById(R.id.tv_proringer_awarded);
        tv_business_review= (ProRegularTextView) findViewById(R.id.tv_business_review);
        tv_last_verified_on= (ProRegularTextView) findViewById(R.id.tv_last_verified_on);
        view_all_service_area= (ProRegularTextView) findViewById(R.id.view_all_service_area);
        tv_no_of_project_value= (ProRegularTextView) findViewById(R.id.tv_no_of_project_value);
        tv_no_of_picture_value= (ProRegularTextView) findViewById(R.id.tv_no_of_picture_value);


        rbar= (RatingBar) findViewById(R.id.rbar);

        rcv_service= (RecyclerView) findViewById(R.id.rcv_service);
        rcv_service.setLayoutManager(new GridLayoutManager(ProjectDetailsActivity.this,2));

        rcv_business_hour= (RecyclerView) findViewById(R.id.rcv_business_hour);
        rcv_business_hour.setLayoutManager(new LinearLayoutManager(ProjectDetailsActivity.this));

        rcv_service_area= (RecyclerView) findViewById(R.id.rcv_service_area);
        rcv_service_area.setLayoutManager(new GridLayoutManager(ProjectDetailsActivity.this,2));

        rcv_license= (RecyclerView) findViewById(R.id.rcv_license);
        rcv_license.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));


        rcv_project_gallery= (RecyclerView) findViewById(R.id.rcv_project_gallery);
        rcv_project_gallery.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));



        findViewById(R.id.LLViewAll).setVisibility(View.GONE);

        view_all_service_area.setOnClickListener(new View.OnClickListener() {
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
        tv_review_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(ProjectDetailsActivity.this, ProReviewActivity.class);
                startActivity(i);
            }
        });

        setDataProListDetails();
    }

    public void setDataProListDetails() {
        ProServiceApiHelper.getInstance(ProjectDetailsActivity.this).getProIndividualListing(new ProServiceApiHelper.getApiProcessCallback() {
            @Override
            public void onStart() {
                pgDialog1 = new ProgressDialog(ProjectDetailsActivity.this);
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
                    JSONObject infoArrayJsonObject = jsonObject.getJSONObject("info_array");
                    JSONObject infoJsonObject = infoArrayJsonObject.getJSONObject("info");


                    if (!infoJsonObject.getString("header_image").equals(""))
                        Glide.with(ProjectDetailsActivity.this).load(infoJsonObject.getString("header_image")).centerCrop().into(img_top);


                    if (!infoJsonObject.getString("profile_image").equals(""))
                        Glide.with(ProjectDetailsActivity.this).load(infoJsonObject.getString("profile_image")).centerCrop().into(img_profile);

                    tv_company_name.setText(infoJsonObject.getString("company_name"));
                    tv_user_name.setText(infoJsonObject.getString("user_name"));
                    tv_address.setText(infoJsonObject.getString("address"));
                    tv_city_state_zipcode.setText(infoJsonObject.getString("city")+", "+infoJsonObject.getString("state")+" "+infoJsonObject.getString("zipcode"));


                    tv_review_value.setText(infoArrayJsonObject.getString("total_review"));
                    tv_rate_value.setText(infoArrayJsonObject.getString("total_avg_review"));


                    rbar.setRating(Float.parseFloat(infoArrayJsonObject.getString("total_avg_review")));

                    tv_about.setText(infoArrayJsonObject.getJSONObject("about").getString("description"));

                    proDetailsService =new ProDetailsServiceAdapter(ProjectDetailsActivity.this,infoArrayJsonObject.getJSONArray("services"));
                    rcv_service.setAdapter(proDetailsService);

                    if(infoJsonObject.getString("business_hour").trim().equals("0")){
                        rcv_business_hour.setVisibility(View.VISIBLE);
                        tv_business_hour.setVisibility(View.GONE);
                        proDetailsBusinessHourAdapter=new ProDetailsBusinessHourAdapter(ProjectDetailsActivity.this,infoArrayJsonObject.getJSONArray("business_hours"));
                        rcv_business_hour.setAdapter(proDetailsBusinessHourAdapter);
                    }else {
                        rcv_business_hour.setVisibility(View.GONE);
                        tv_business_hour.setVisibility(View.VISIBLE);
                        tv_business_hour.setText("Always Open");
                    }



                    proDetailsServiceAreaAdapter=new ProDetailsServiceAreaAdapter(ProjectDetailsActivity.this,infoArrayJsonObject.getJSONArray("service_area"));
                    rcv_service_area.setAdapter(proDetailsServiceAreaAdapter);

                    JSONObject company_infoJsonOBJ=infoArrayJsonObject.getJSONObject("company_info");

                    tv_business_since.setText(company_infoJsonOBJ.getString("business_since"));
                    tv_no_of_employee.setText(company_infoJsonOBJ.getString("no_of_employee"));
                    tv_proringer_awarded.setText(company_infoJsonOBJ.getString("proringer_awarded"));
                    tv_business_review.setText(company_infoJsonOBJ.getString("business_review"));
                    tv_last_verified_on.setText(company_infoJsonOBJ.getString("last_verified_on"));


                    proDetailsLicenseAdapter=new ProDetailsLicenseAdapter(ProjectDetailsActivity.this,infoArrayJsonObject.getJSONArray("licence"));
                    rcv_license.setAdapter(proDetailsLicenseAdapter);

                    tv_no_of_project_value.setText(infoArrayJsonObject.getString("total_project"));
                    tv_no_of_picture_value.setText(infoArrayJsonObject.getString("total_picture"));


                    prosProjectDetailsAdapter=new ProsProjectDetailsAdapter(ProjectDetailsActivity.this,infoArrayJsonObject.getJSONArray("project_gallery"));
                    rcv_project_gallery.setAdapter(prosProjectDetailsAdapter);

                    if (!infoArrayJsonObject.getString("achievement").equals(""))
                        Glide.with(ProjectDetailsActivity.this).load(infoArrayJsonObject.getString("achievement")).centerCrop().into(img_achievements);


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
                "56"
//                ProApplication.getInstance().getUserId()
                ,
//                pros_id
                "82"
        );
    }
}
