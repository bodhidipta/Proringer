package com.android.llc.proringer.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import com.android.llc.proringer.R;
import com.android.llc.proringer.appconstant.ProApplication;
import com.android.llc.proringer.helper.CustomAlert;
import com.android.llc.proringer.helper.MyCustomAlertListener;
import com.android.llc.proringer.helper.MyLoader;
import com.android.llc.proringer.helper.ProServiceApiHelper;
import com.android.llc.proringer.viewsmod.edittext.ProLightEditText;
import com.android.llc.proringer.viewsmod.textview.ProSemiBoldTextView;

/**
 * Created by bodhidipta on 10/06/17.
 * <!-- * Copyright (c) 2017, Proringer-->
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * -->
 */

public class LogInActivity extends AppCompatActivity implements MyCustomAlertListener{
    private ProSemiBoldTextView sign_up;
    private ProSemiBoldTextView log_in;
    private ProLightEditText email, password;
    private MyLoader myLoader = null;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        myLoader=new MyLoader(LogInActivity.this);


        findViewById(R.id.forget_password).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LogInActivity.this, ForgetPasswordActivity.class));
            }
        });
        sign_up = (ProSemiBoldTextView) findViewById(R.id.sign_up);
        log_in = (ProSemiBoldTextView) findViewById(R.id.log_in);
        email = (ProLightEditText) findViewById(R.id.email);
        password = (ProLightEditText) findViewById(R.id.password);
        log_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (email.getText().toString().trim().equals("")) {
                    email.setError("Please enter your email address.");
                } else if (password.getText().toString().trim().equals("")) {
                    password.setError("Please enter password.");
                } else {
                    ProServiceApiHelper.getInstance(LogInActivity.this).getUserLoggedIn(
                            new ProServiceApiHelper.getApiProcessCallback() {
                                @Override
                                public void onStart() {
                                    myLoader.showLoader();
                                }

                                @Override
                                public void onComplete(String message) {
                                    if (myLoader != null && myLoader.isMyLoaderShowing())
                                        myLoader.dismissLoader();

                                    ProApplication.getInstance().setUserEmail(email.getText().toString().trim());

                                    Intent intent = new Intent(LogInActivity.this, LandScreenActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);
                                    finish();
                                }

                                @Override
                                public void onError(String error) {
                                    if (myLoader != null && myLoader.isMyLoaderShowing())
                                        myLoader.dismissLoader();

                                    CustomAlert customAlert = new CustomAlert(LogInActivity.this, "Sign in error", "" + error, LogInActivity.this);
                                    customAlert.getListenerRetryCancelFromNormalAlert();
                                }
                            },
                            email.getText().toString().trim(),
                            password.getText().toString().trim());
                }
            }
        });

        sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LogInActivity.this, SignUpActivity.class));
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

    @Override
    public void callbackForAlert(String result) {
        if (result.equalsIgnoreCase("retry")) {
            log_in.performClick();
        }
    }
}
