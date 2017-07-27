package com.android.llc.proringer.activities;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import com.android.llc.proringer.R;
import com.android.llc.proringer.helper.ProServiceApiHelper;
import com.android.llc.proringer.viewsmod.textview.ProRegularTextView;

/**
 * Created by su on 7/27/17.
 */

public class TermsPrivacyActivity extends AppCompatActivity {
    ProgressDialog pgDia;
    ProRegularTextView tv_title;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms_privacy);

        String HeaderString = getIntent().getStringExtra("value");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tv_title= (ProRegularTextView) findViewById(R.id.tv_title);

        if(HeaderString.equalsIgnoreCase("term")){
            tv_title.setText("Terms of Use");
        }else {
            tv_title.setText("Privacy Policy");
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
                pgDia.setTitle("Faq");
                pgDia.setMessage("Fag page loading Please wait...");
                pgDia.setCancelable(false);
                pgDia.show();
            }

            @Override
            public void onComplete(String s) {
                if (pgDia!=null && pgDia.isShowing())
                    pgDia.dismiss();
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
                pgDia.setTitle("Faq");
                pgDia.setMessage("Fag page loading Please wait...");
                pgDia.setCancelable(false);
                pgDia.show();
            }

            @Override
            public void onComplete(String s) {
                if (pgDia!=null && pgDia.isShowing())
                    pgDia.dismiss();
            }

            @Override
            public void onError(String error) {
                if (pgDia!=null && pgDia.isShowing())
                    pgDia.dismiss();
            }
        });
    }
}
