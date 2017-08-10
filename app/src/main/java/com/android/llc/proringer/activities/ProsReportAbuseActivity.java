package com.android.llc.proringer.activities;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.android.llc.proringer.R;
import com.android.llc.proringer.appconstant.ProApplication;
import com.android.llc.proringer.helper.ProServiceApiHelper;
import com.android.llc.proringer.viewsmod.edittext.ProRegularEditText;

/**
 * Created by su on 8/4/17.
 */

public class ProsReportAbuseActivity extends AppCompatActivity {
    String review_report_id = "";
    ProgressDialog pgDialog = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pro_report_abuse_review);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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
                                                                                                   pgDialog = new ProgressDialog(ProsReportAbuseActivity.this);
                                                                                                   pgDialog.setTitle("Pros Report Abuse");
                                                                                                   pgDialog.setCancelable(false);
                                                                                                   pgDialog.setMessage("Please wait...");
                                                                                                   pgDialog.show();
                                                                                               }

                                                                                               @Override
                                                                                               public void onComplete(String message) {
                                                                                                   if (pgDialog != null && pgDialog.isShowing())
                                                                                                       pgDialog.dismiss();

                                                                                                   new AlertDialog.Builder(ProsReportAbuseActivity.this)
                                                                                                           .setTitle("Pros Report Abuse")
                                                                                                           .setMessage("" + message)
                                                                                                           .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                                                                                               @Override
                                                                                                               public void onClick(DialogInterface dialogInterface, int i) {
                                                                                                                   dialogInterface.dismiss();
                                                                                                               }
                                                                                                           })
                                                                                                           .show();
                                                                                               }

                                                                                               @Override
                                                                                               public void onError(String error) {
                                                                                                   if (pgDialog != null && pgDialog.isShowing())
                                                                                                       pgDialog.dismiss();
                                                                                                   new AlertDialog.Builder(ProsReportAbuseActivity.this)
                                                                                                           .setTitle("Pros Report Abuse")
                                                                                                           .setMessage("" + error)
                                                                                                           .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                                                                                               @Override
                                                                                                               public void onClick(DialogInterface dialogInterface, int i) {
                                                                                                                   dialogInterface.dismiss();
                                                                                                               }
                                                                                                           })
                                                                                                           .show();
                                                                                               }
                                                                                           }, ProApplication.getInstance().getUserId()
                , review_report_id
                , (((ProRegularEditText) findViewById(R.id.pro_review_description_text)).getText().toString().trim()));
    }
}
