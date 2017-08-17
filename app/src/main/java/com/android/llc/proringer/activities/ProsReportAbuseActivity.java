package com.android.llc.proringer.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import com.android.llc.proringer.R;
import com.android.llc.proringer.appconstant.ProApplication;
import com.android.llc.proringer.helper.CustomAlert;
import com.android.llc.proringer.helper.MyCustomAlertListener;
import com.android.llc.proringer.helper.MyLoader;
import com.android.llc.proringer.helper.ProServiceApiHelper;
import com.android.llc.proringer.viewsmod.edittext.ProRegularEditText;

/**
 * Created by su on 8/4/17.
 */

public class ProsReportAbuseActivity extends AppCompatActivity implements MyCustomAlertListener{
    String review_report_id = "";
    MyLoader myLoader = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pro_report_abuse_review);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        myLoader=new MyLoader(ProsReportAbuseActivity.this);

        if (getIntent().getExtras() != null) {
            review_report_id = getIntent().getExtras().getString("review_report_id");
        }

        findViewById(R.id.pro_review_report_continue).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (((ProRegularEditText) findViewById(R.id.pro_review_description_text)).getText().toString().trim().equals("")) {
                    ((ProRegularEditText) findViewById(R.id.pro_review_description_text)).setError("Please enter report abuse description");
                } else {
                    submitReviewReport();
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            onBackPressed();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        setResult(GetStartedActivity.RESULT_CANCELED);
        finish();
    }

    public void submitReviewReport() {

        ProServiceApiHelper.getInstance(ProsReportAbuseActivity.this).addReviewReportAbuse(new ProServiceApiHelper.getApiProcessCallback() {
                                                                                               @Override
                                                                                               public void onStart() {
                                                                                                  myLoader.showLoader();
                                                                                               }

                                                                                               @Override
                                                                                               public void onComplete(String message) {
                                                                                                   if (myLoader != null && myLoader.isMyLoaderShowing())
                                                                                                       myLoader.dismissLoader();

                                                                                                   CustomAlert customAlert = new CustomAlert(ProsReportAbuseActivity.this, "Pros Report Abuse","" + message, ProsReportAbuseActivity.this);
                                                                                                   customAlert.createNormalAlert("Ok",1);
                                                                                               }

                                                                                               @Override
                                                                                               public void onError(String error) {
                                                                                                   if (myLoader != null && myLoader.isMyLoaderShowing())
                                                                                                       myLoader.dismissLoader();

                                                                                                   CustomAlert customAlert = new CustomAlert(ProsReportAbuseActivity.this, "Pros Report Abuse","" + error, ProsReportAbuseActivity.this);
                                                                                                   customAlert.createNormalAlert("Ok",2);

                                                                                               }
                                                                                           }, ProApplication.getInstance().getUserId()
                , review_report_id
                , (((ProRegularEditText) findViewById(R.id.pro_review_description_text)).getText().toString().trim()));
    }

    @Override
    public void callbackForAlert(String result, int i) {

        if(result.equalsIgnoreCase("ok") && i==1){
            finish();
        }
    }
}
