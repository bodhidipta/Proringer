package com.android.llc.proringer.activities;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.android.llc.proringer.R;
import com.android.llc.proringer.helper.CustomAlert;
import com.android.llc.proringer.helper.MyCustomAlertListener;
import com.android.llc.proringer.helper.MyLoader;
import com.android.llc.proringer.helper.ProServiceApiHelper;
import com.android.llc.proringer.utils.Logger;
import com.android.llc.proringer.viewsmod.textview.ProRegularTextView;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by su on 7/19/17.
 */

public class FaqActivity extends AppCompatActivity implements MyCustomAlertListener{
    LinearLayout linear_main_container;
    MyLoader myLoader=null;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.faq_activity);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((ProRegularTextView) findViewById(R.id.tv_title)).setText("Faq");


        myLoader=new MyLoader(FaqActivity.this);

        linear_main_container = (LinearLayout)findViewById(R.id.linear_main_container);

        ProServiceApiHelper.getInstance(FaqActivity.this).getFaqInformation(new ProServiceApiHelper.faqCallback() {
            @Override
            public void onStart() {
                myLoader.showLoader();
            }

            @Override
            public void onComplete(String s) {
                if (myLoader != null && myLoader.isMyLoaderShowing())
                    myLoader.dismissLoader();

                try {
                    JSONObject jsonObject = new JSONObject(s);

                    if (jsonObject.has("faq")) {

                        JSONArray faqArray = jsonObject.getJSONArray("faq");
                        Logger.printMessage("faq", "" + faqArray);

                        for (int i = 0; i < faqArray.length(); i++) {
                            LinearLayout.LayoutParams lparams1 = new LinearLayout.LayoutParams(
                                    LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                            ProRegularTextView tv1 = new ProRegularTextView(FaqActivity.this);
                            tv1.setLayoutParams(lparams1);
                            tv1.setText(faqArray.getJSONObject(i).getString("question"));

                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                tv1.setTextColor(getResources().getColor(R.color.colorAccent, null));
                            } else {
                                tv1.setTextColor(getResources().getColor(R.color.colorAccent));
                            }


                            LinearLayout.LayoutParams lparams2 = new LinearLayout.LayoutParams(
                                    LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                            lparams2.setMargins(0, 20, 0, 0);
                            ProRegularTextView tv2 = new ProRegularTextView(FaqActivity.this);
                            tv2.setLayoutParams(lparams2);
                            tv2.setText(faqArray.getJSONObject(i).getString("answer"));

                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                tv2.setTextColor(getResources().getColor(R.color.colorTextBlack, null));
                            } else {
                                tv1.setTextColor(getResources().getColor(R.color.colorTextBlack));
                            }

                            linear_main_container.addView(tv1);
                            linear_main_container.addView(tv2);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(String error) {
                if (myLoader != null && myLoader.isMyLoaderShowing())
                    myLoader.dismissLoader();

                CustomAlert customAlert = new CustomAlert(FaqActivity.this, "Faq", "" + error, FaqActivity.this);
                customAlert.createNormalAlert("ok",1);

                if (error.equalsIgnoreCase(getResources().getString(R.string.no_internet_connection_found_Please_check_your_internet_connection))) {
                    findViewById(R.id.ScrollViewMAin).setVisibility(View.GONE);
                    findViewById(R.id.LLNetworkDisconnection).setVisibility(View.VISIBLE);
                }
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void callbackForAlert(String result, int i) {

    }
}
