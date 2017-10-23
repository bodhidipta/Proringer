package com.android.llc.proringer.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Toast;
import com.android.llc.proringer.R;
import com.android.llc.proringer.adapter.ProsReviewAllAdapter;
import com.android.llc.proringer.appconstant.ProApplication;
import com.android.llc.proringer.helper.MyLoader;
import com.android.llc.proringer.helper.ProServiceApiHelper;
import com.android.llc.proringer.viewsmod.textview.ProRegularTextView;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by su on 8/4/17.
 */

public class ProsReviewAllListActivity extends AppCompatActivity {
    public String pros_id = "", pros_company_name = "",img="",total_avg_review="",total_review="";
    RecyclerView rcv_review_all;
    ProsReviewAllAdapter prosReviewAllAdapter;
    MyLoader myLoader=null;
    JSONArray jsonInfoReviewArray;
    ImageView img_profile;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pros_review_all_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        jsonInfoReviewArray = new JSONArray();

        myLoader=new MyLoader(ProsReviewAllListActivity.this);

        img_profile= (ImageView) findViewById(R.id.img_profile);


        if (getIntent().getExtras() != null) {
            pros_id = getIntent().getExtras().getString("pros_id");
            img = getIntent().getExtras().getString("img");
            pros_company_name = getIntent().getExtras().getString("pros_company_name");
            total_avg_review = getIntent().getExtras().getString("total_avg_review");
            total_review = getIntent().getExtras().getString("total_review");
        }

        ((ProRegularTextView) findViewById(R.id.tv_toolbar)).setText(pros_company_name);


        rcv_review_all = (RecyclerView) findViewById(R.id.rcv_review_all);
        rcv_review_all.setLayoutManager(new LinearLayoutManager(ProsReviewAllListActivity.this));

        if (!img.trim().equals(""))
            Glide.with(ProsReviewAllListActivity.this).load(img).centerCrop().into(img_profile);

        ((ProRegularTextView)findViewById(R.id.tv_rate_value)).setText(total_avg_review);
        ((RatingBar)findViewById(R.id.rbar)).setRating(Float.parseFloat(total_avg_review));
        ((ProRegularTextView) findViewById(R.id.tv_review_value)).setText(total_review);


        findViewById(R.id.tv_review_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (!pros_id.trim().equals("")) {

                        Intent intent = new Intent(ProsReviewAllListActivity.this, ProsReviewActivity.class);
                        intent.putExtra("pros_id", pros_id);
                        intent.putExtra("img",img);
                        startActivity(intent);
                    } else {
                        Toast.makeText(ProsReviewAllListActivity.this, "Details Page Loading problem", Toast.LENGTH_LONG).show();
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
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
        Intent backIntent = new Intent();
        setResult(RESULT_CANCELED, backIntent );
//        setResult(GetStartedActivity.RESULT_CANCELED);
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        jsonInfoReviewArray = new JSONArray();
        loadReviewList(0, 10);
    }

    public void loadReviewList(int from, int perPage) {

        findViewById(R.id.RLMain).setVisibility(View.VISIBLE);
        findViewById(R.id.LLNetworkDisconnection).setVisibility(View.GONE);

        ProServiceApiHelper.getInstance(ProsReviewAllListActivity.this).getProsAllReview(new ProServiceApiHelper.getApiProcessCallback() {
                                                                                             @Override
                                                                                             public void onStart() {
                                                                                                myLoader.showLoader();
                                                                                             }

                                                                                             @Override
                                                                                             public void onComplete(String message) {
                                                                                                 if (myLoader != null && myLoader.isMyLoaderShowing())
                                                                                                     myLoader.dismissLoader();

                                                                                                 try {
                                                                                                     JSONObject jsonObject = new JSONObject(message);
                                                                                                     JSONArray info_array = jsonObject.getJSONArray("info_array");

                                                                                                     for (int i = 0; i < info_array.length(); i++) {
                                                                                                         jsonInfoReviewArray.put(info_array.getJSONObject(i));
                                                                                                     }
                                                                                                     if (prosReviewAllAdapter == null) {
                                                                                                         prosReviewAllAdapter = new ProsReviewAllAdapter(ProsReviewAllListActivity.this, jsonInfoReviewArray);
                                                                                                         rcv_review_all.setAdapter(prosReviewAllAdapter);
                                                                                                     } else {
                                                                                                         prosReviewAllAdapter.NotifyMeInLazyLoad(jsonInfoReviewArray);
                                                                                                     }

                                                                                                 } catch (JSONException e) {
                                                                                                     e.printStackTrace();
                                                                                                 }
                                                                                             }

                                                                                             @Override
                                                                                             public void onError(String error) {
                                                                                                 if (myLoader != null && myLoader.isMyLoaderShowing())
                                                                                                     myLoader.dismissLoader();

                                                                                                 if (error.equalsIgnoreCase(getResources().getString(R.string.no_internet_connection_found_Please_check_your_internet_connection))) {
                                                                                                     findViewById(R.id.RLMain).setVisibility(View.GONE);
                                                                                                     findViewById(R.id.LLNetworkDisconnection).setVisibility(View.VISIBLE);
                                                                                                 }

//                new AlertDialog.Builder(ProsReviewAllListActivity.this)
//                        .setTitle("Error")
//                        .setMessage(""+error)
//                        .setPositiveButton("retry", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                dialog.dismiss();
//                                loadReviewList(0,10);
//                            }
//                        })
//                        .setNegativeButton("abort", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                dialog.dismiss();
//                            }
//                        }).show();

                                                                                             }
                                                                                         }, ProApplication.getInstance().getUserId()
                , pros_id
                , "" + from
                , "" + perPage
        );
    }

}
