package com.android.llc.proringer.activities;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;

import com.android.llc.proringer.R;
import com.android.llc.proringer.adapter.ProsReviewAllAdapter;
import com.android.llc.proringer.appconstant.ProApplication;
import com.android.llc.proringer.helper.ProServiceApiHelper;
import com.android.llc.proringer.viewsmod.textview.ProRegularTextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by su on 8/4/17.
 */

public class ProsReviewAllListActivity extends AppCompatActivity {
    public String pros_id="",pros_company_name="";
    RecyclerView rcv_review_all;
    ProsReviewAllAdapter prosReviewAllAdapter;
    ProgressDialog pgDialog;
    JSONArray jsonInfoReviewArray;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pros_review_all_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        jsonInfoReviewArray=new JSONArray();

        if (getIntent().getExtras() != null) {
            pros_id = getIntent().getExtras().getString("pros_id");
            pros_company_name = getIntent().getExtras().getString("pros_company_name");
        }

        ((ProRegularTextView)findViewById(R.id.tv_toolbar)).setText(pros_company_name);


        rcv_review_all= (RecyclerView) findViewById(R.id.rcv_review_all);
        rcv_review_all.setLayoutManager(new LinearLayoutManager(ProsReviewAllListActivity.this));

        loadReviewList(0,10);
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
    public void loadReviewList(int from,int perPage){

        findViewById(R.id.RLMain).setVisibility(View.VISIBLE);
        findViewById(R.id.LLNetworkDisconnection).setVisibility(View.GONE);

        ProServiceApiHelper.getInstance(ProsReviewAllListActivity.this).getProsAllReview(new ProServiceApiHelper.getApiProcessCallback() {
            @Override
            public void onStart() {
                pgDialog = new ProgressDialog(ProsReviewAllListActivity.this);
                pgDialog.setTitle("Pros Review");
                pgDialog.setCancelable(false);
                pgDialog.setMessage("Getting local pros review list.Please wait...");
                pgDialog.show();
            }

            @Override
            public void onComplete(String message) {
                if (pgDialog != null && pgDialog.isShowing())
                    pgDialog.dismiss();

                try {
                    JSONObject jsonObject=new JSONObject(message);
                    JSONArray info_array= jsonObject.getJSONArray("info_array");

                    for (int i=0;i<info_array.length();i++){
                        jsonInfoReviewArray.put(info_array.getJSONObject(i));
                    }
                    if(prosReviewAllAdapter==null){
                        prosReviewAllAdapter=new ProsReviewAllAdapter(ProsReviewAllListActivity.this,jsonInfoReviewArray);
                        rcv_review_all.setAdapter(prosReviewAllAdapter);
                    }else {
                        prosReviewAllAdapter.NotifyMeInLazyLoad(jsonInfoReviewArray);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(String error) {
                if (pgDialog != null && pgDialog.isShowing())
                    pgDialog.dismiss();

                if(error.equalsIgnoreCase("No internet connection found. Please check your internet connection.")){
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
                ,pros_id
                ,""+from
                ,""+perPage
        );
    }


    public void showReviewReplyResponseDescribetionDialog(String title,String describetion) {
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
