package com.android.llc.proringer.activities;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.llc.proringer.R;
import com.android.llc.proringer.helper.ProServiceApiHelper;
import com.android.llc.proringer.utils.Logger;
import com.android.llc.proringer.viewsmod.textview.ProRegularTextView;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by su on 7/27/17.
 */

public class TermsPrivacyActivity extends AppCompatActivity {
    ProgressDialog pgDia;
    ProRegularTextView tv_title;
    LinearLayout main_container;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms_privacy);

        String HeaderString = getIntent().getStringExtra("value");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        main_container= (LinearLayout) findViewById(R.id.main_container);

        tv_title= (ProRegularTextView) findViewById(R.id.tv_title);

        if(HeaderString.equalsIgnoreCase("term")){
            tv_title.setText("Terms of Use");
            loadDataTermsOfUse();
        }else {
            tv_title.setText("Privacy Policy");
            loadPrivacyPolicy();
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public void loadDataTermsOfUse(){

        ProServiceApiHelper.getInstance(TermsPrivacyActivity.this).getTermsOfUseInformation(new ProServiceApiHelper.faqCallback() {
            @Override
            public void onStart() {
                pgDia=new ProgressDialog(TermsPrivacyActivity.this);
                pgDia.setTitle("Terms of Use");
                pgDia.setMessage("Terms of Use page loading Please wait...");
                pgDia.setCancelable(false);
                pgDia.show();
            }

            @Override
            public void onComplete(String s) {
                if (pgDia!=null && pgDia.isShowing())
                    pgDia.dismiss();

                Logger.printMessage("message",""+s);

                try {
                    JSONObject jsonObject=new JSONObject(s);

                    LinearLayout.LayoutParams lparams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    TextView tv=new TextView(TermsPrivacyActivity.this);
                    tv.setLayoutParams(lparams);
                    tv.setText(Html.fromHtml(jsonObject.getString("page_content")));
                    main_container.addView(tv);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(String error) {
                if (pgDia!=null && pgDia.isShowing())
                    pgDia.dismiss();
            }
        });
    }

    public void loadPrivacyPolicy(){
        ProServiceApiHelper.getInstance(TermsPrivacyActivity.this).getPrivacyPolicyInformation(new ProServiceApiHelper.faqCallback() {
            @Override
            public void onStart() {
                pgDia=new ProgressDialog(TermsPrivacyActivity.this);
                pgDia.setTitle("Privacy Policy");
                pgDia.setMessage("Privacy Policy loading Please wait...");
                pgDia.setCancelable(false);
                pgDia.show();
            }

            @Override
            public void onComplete(String s) {
                if (pgDia!=null && pgDia.isShowing())
                    pgDia.dismiss();

                Logger.printMessage("message",""+s);

                try {
                    JSONObject jsonObject=new JSONObject(s);

                    LinearLayout.LayoutParams lparams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    TextView tv=new TextView(TermsPrivacyActivity.this);
                    tv.setLayoutParams(lparams);
                    tv.setText(Html.fromHtml(jsonObject.getString("page_content")));
                    main_container.addView(tv);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(String error) {
                if (pgDia!=null && pgDia.isShowing())
                    pgDia.dismiss();
            }
        });
    }
}