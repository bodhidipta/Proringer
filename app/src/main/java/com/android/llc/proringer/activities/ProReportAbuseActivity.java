package com.android.llc.proringer.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import com.android.llc.proringer.R;

/**
 * Created by su on 8/4/17.
 */

public class ProReportAbuseActivity extends AppCompatActivity {
    String  review_report_id="";
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
}
