package com.android.llc.proringer.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import com.android.llc.proringer.R;

/**
 * Created by su on 8/4/17.
 */

public class ProsReviewAllListActivity extends AppCompatActivity {
    String pros_id="";
    RecyclerView rcv_review_all;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pros_review_all_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (getIntent().getExtras() != null) {
            pros_id = getIntent().getExtras().getString("pros_id");
        }

        rcv_review_all= (RecyclerView) findViewById(R.id.rcv_review_all);
        rcv_review_all.setLayoutManager(new LinearLayoutManager(ProsReviewAllListActivity.this));
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
