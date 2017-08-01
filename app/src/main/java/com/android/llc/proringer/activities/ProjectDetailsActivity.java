package com.android.llc.proringer.activities;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import com.android.llc.proringer.R;
import com.android.llc.proringer.appconstant.ProApplication;
import com.android.llc.proringer.helper.ProServiceApiHelper;

/**
 * Created by su on 7/12/17.
 */

public class ProjectDetailsActivity extends AppCompatActivity {
    ImageView img_back;
    String pros_id="";
    ProgressDialog pgDialog;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_details_pro);
        img_back= (ImageView) findViewById(R.id.img_back);
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        if(getIntent().getExtras() != null) {
            pros_id=getIntent().getExtras().getString("pros_id");
        }

        setDataProListDetails();
    }
    public void setDataProListDetails(){
        ProServiceApiHelper.getInstance(ProjectDetailsActivity.this).getProIndividualListing(new ProServiceApiHelper.getApiProcessCallback() {
            @Override
            public void onStart() {
                pgDialog = new ProgressDialog(ProjectDetailsActivity.this);
                pgDialog.setTitle("Local Pros Details");
                pgDialog.setCancelable(false);
                pgDialog.setMessage("Getting local pros details.Please wait...");
                pgDialog.show();
            }

            @Override
            public void onComplete(String message) {
                if (pgDialog != null && pgDialog.isShowing())
                    pgDialog.dismiss();
            }

            @Override
            public void onError(String error) {
                if (pgDialog != null && pgDialog.isShowing())
                    pgDialog.dismiss();
            }
        }, ProApplication.getInstance().getUserId(),pros_id);
    }
}
