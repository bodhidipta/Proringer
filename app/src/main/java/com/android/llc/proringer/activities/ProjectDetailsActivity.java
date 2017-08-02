package com.android.llc.proringer.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;

import com.android.llc.proringer.R;
import com.android.llc.proringer.appconstant.ProApplication;
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
    ImageView img_back, img_top, img_profile;
    String pros_id = "";
    JSONObject jsonObject = null;
    ProgressDialog pgDialog1;
    ProRegularTextView tv_review_btn;
    ProRegularTextView tv_review_value,tv_rate_value;
    RatingBar rbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_details_pro);

        img_back = (ImageView) findViewById(R.id.img_back);
        img_top = (ImageView) findViewById(R.id.img_top);
        img_profile = (ImageView) findViewById(R.id.img_profile);

        tv_review_btn = (ProRegularTextView) findViewById(R.id.tv_review_btn);
        tv_review_value= (ProRegularTextView) findViewById(R.id.tv_review_value);
        tv_rate_value= (ProRegularTextView) findViewById(R.id.tv_rate_value);
        rbar= (RatingBar) findViewById(R.id.rbar);


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


                    tv_review_value.setText(infoArrayJsonObject.getString("total_review"));
                    tv_rate_value.setText(infoArrayJsonObject.getString("total_avg_review"));

                    rbar.setRating(Float.parseFloat(infoArrayJsonObject.getString("total_avg_review")));





                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onError(String error) {
                if (pgDialog1 != null && pgDialog1.isShowing())
                    pgDialog1.dismiss();
            }
        }, ProApplication.getInstance().getUserId(), pros_id);
    }
}
