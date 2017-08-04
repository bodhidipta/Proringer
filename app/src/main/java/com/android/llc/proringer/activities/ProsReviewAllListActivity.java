package com.android.llc.proringer.activities;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import com.android.llc.proringer.R;
import com.android.llc.proringer.adapter.ProsReviewAllAdapter;
import com.android.llc.proringer.appconstant.ProApplication;
import com.android.llc.proringer.helper.ProServiceApiHelper;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by su on 8/4/17.
 */

public class ProsReviewAllListActivity extends AppCompatActivity {
    String pros_id="";
    RecyclerView rcv_review_all;
    ProsReviewAllAdapter prosReviewAllAdapter;
    ProgressDialog pgDialog1;
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
        }

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

        ProServiceApiHelper.getInstance(ProsReviewAllListActivity.this).getProsAllReview(new ProServiceApiHelper.getApiProcessCallback() {
            @Override
            public void onStart() {
                pgDialog1 = new ProgressDialog(ProsReviewAllListActivity.this);
                pgDialog1.setTitle("Pros Review");
                pgDialog1.setCancelable(false);
                pgDialog1.setMessage("Getting local pros review list.Please wait...");
                pgDialog1.show();
            }

            @Override
            public void onComplete(String message) {
                if (pgDialog1 != null && pgDialog1.isShowing())
                    pgDialog1.dismiss();

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
                        prosReviewAllAdapter.NotifyMeInLazzyLoad(jsonInfoReviewArray);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(String error) {
                if (pgDialog1 != null && pgDialog1.isShowing())
                    pgDialog1.dismiss();
            }
        }, ProApplication.getInstance().getUserId()
                ,pros_id
                ,""+from
                ,""+perPage
        );

    }
}
