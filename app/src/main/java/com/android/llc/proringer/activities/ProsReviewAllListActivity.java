package com.android.llc.proringer.activities;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
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


        loadReviewList(0, 10);
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

                                                                                                 if (error.equalsIgnoreCase("No internet connection found. Please check your internet connection.")) {
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


    public void showReviewReplyResponseDescribetionDialog(String title, String describetion) {
        final Dialog dialog = new Dialog(ProsReviewAllListActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//                    dialog.setCancelable(false);
        dialog.setContentView(R.layout.custom_dialogbox_pro_review_describetion);
        //dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));

        LinearLayout LLMain = (LinearLayout) dialog.findViewById(R.id.LLMain);

        ProRegularTextView tv_tittle = (ProRegularTextView) dialog.findViewById(R.id.tv_tittle);
        ProRegularTextView tv_show_describetion = (ProRegularTextView) dialog.findViewById(R.id.tv_show_describetion);


        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;

        LLMain.getLayoutParams().width = (width - 30);
        LLMain.getLayoutParams().height = (width - 30);
//        scrollView.getLayoutParams().height = (height-30)/2;

        dialog.findViewById(R.id.img_cancel_dialog).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        tv_tittle.setText(title);
        tv_show_describetion.setText(describetion);
        dialog.show();
    }


}
