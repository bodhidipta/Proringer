package com.android.llc.proringer.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BaseTransientBottomBar;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Patterns;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.android.llc.proringer.R;
import com.android.llc.proringer.helper.MyLoader;
import com.android.llc.proringer.helper.ProServiceApiHelper;
import com.android.llc.proringer.viewsmod.edittext.ProLightEditText;
import com.android.llc.proringer.viewsmod.textview.ProRegularTextView;

/**
 * Created by bodhidipta on 12/06/17.
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

public class ForgetPasswordActivity extends AppCompatActivity {
    private ProRegularTextView header_text;
    private ProLightEditText email, request_code, password, confirm_password;
    private MyLoader myLoader = null;
    ProRegularTextView tv_contact_us;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acitivity_forget_password);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        header_text = (ProRegularTextView) findViewById(R.id.header_text);
        email = (ProLightEditText) findViewById(R.id.email);
        request_code = (ProLightEditText) findViewById(R.id.request_code);
        password = (ProLightEditText) findViewById(R.id.password);
        confirm_password = (ProLightEditText) findViewById(R.id.confirm_password);

        myLoader = new MyLoader(ForgetPasswordActivity.this);


        tv_contact_us = (ProRegularTextView) findViewById(R.id.tv_contact_us);

        tv_contact_us.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ForgetPasswordActivity.this, ContactUsActivity.class));
            }
        });

        findViewById(R.id.resend_confirmation).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ForgetPasswordActivity.this, ResendConfirmationActivity.class));
            }
        });

        findViewById(R.id.submit_email).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Patterns.EMAIL_ADDRESS.matcher(email.getText().toString().trim()).matches()) {
                    ProServiceApiHelper.getInstance(ForgetPasswordActivity.this).forgetPassword(email.getText().toString().trim(), new ProServiceApiHelper.getApiProcessCallback() {
                        @Override
                        public void onStart() {
                            myLoader.showLoader();
                        }

                        @Override
                        public void onComplete(String message) {
                            if (myLoader != null && myLoader.isMyLoaderShowing())
                                myLoader.dismissLoader();

                            closeKeypad();

                            new AlertDialog.Builder(ForgetPasswordActivity.this)
                                    .setTitle("forgot")
                                    .setMessage("" + message)
                                    .setCancelable(false)
                                    .setNegativeButton("ok", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                            findViewById(R.id.pre_submit_email).setVisibility(View.GONE);
                                            header_text.setText("RESET PASSWORD");
                                        }
                                    })
                                    .show();
                        }

                        @Override
                        public void onError(String error) {
                            if (myLoader != null && myLoader.isMyLoaderShowing())
                                myLoader.dismissLoader();

                            new AlertDialog.Builder(ForgetPasswordActivity.this)
                                    .setTitle("Request password error")
                                    .setMessage("" + error)
                                    .setCancelable(false)
                                    .setPositiveButton("retry", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                            findViewById(R.id.submit_email).performClick();
                                        }
                                    })
                                    .setNegativeButton("abort", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    })
                                    .show();
                        }
                    });


                } else {
                    Snackbar.make(findViewById(R.id.main_container), "Email address is not valid. Please enter a valid email address .", BaseTransientBottomBar.LENGTH_SHORT).show();
                }
            }
        });

        findViewById(R.id.reset_password).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (request_code.getText().toString().trim().equals("")) {
                    request_code.setError("Please enter request code sent to email-address.");
                } else {
                    if (password.getText().toString().trim().equals("")) {
                        password.setError("Please enter new password.");
                    } else {
                        if (confirm_password.getText().toString().trim().equals("")) {
                            password.setError("Please enter confirm password.");
                        } else {
                            if (password.getText().toString().trim().equals(confirm_password.getText().toString().trim())) {
                                ProServiceApiHelper.getInstance(ForgetPasswordActivity.this).resetPassword(
                                        request_code.getText().toString().trim(),
                                        confirm_password.getText().toString().trim(),
                                        new ProServiceApiHelper.getApiProcessCallback() {
                                            @Override
                                            public void onStart() {
                                                myLoader.showLoader();
                                            }

                                            @Override
                                            public void onComplete(String message) {
                                                if (myLoader != null && myLoader.isMyLoaderShowing())
                                                    myLoader.dismissLoader();

                                                closeKeypad();

                                                new AlertDialog.Builder(ForgetPasswordActivity.this)
                                                        .setTitle("Reset password success")
                                                        .setMessage("" + message)
                                                        .setCancelable(false)
                                                        .setNegativeButton("ok", new DialogInterface.OnClickListener() {
                                                            @Override
                                                            public void onClick(DialogInterface dialog, int which) {
                                                                dialog.dismiss();
                                                                finish();
                                                            }
                                                        })
                                                        .show();
                                            }

                                            @Override
                                            public void onError(String error) {
                                                if (myLoader != null && myLoader.isMyLoaderShowing())
                                                    myLoader.dismissLoader();

                                                new AlertDialog.Builder(ForgetPasswordActivity.this)
                                                        .setTitle("Reset password error")
                                                        .setMessage("" + error)
                                                        .setCancelable(false)
                                                        .setPositiveButton("retry", new DialogInterface.OnClickListener() {
                                                            @Override
                                                            public void onClick(DialogInterface dialog, int which) {
                                                                dialog.dismiss();
                                                                findViewById(R.id.submit_email).performClick();
                                                            }
                                                        })
                                                        .setNegativeButton("abort", new DialogInterface.OnClickListener() {
                                                            @Override
                                                            public void onClick(DialogInterface dialog, int which) {
                                                                dialog.dismiss();
                                                            }
                                                        })
                                                        .show();
                                            }
                                        }
                                );
                            } else {
                                confirm_password.setError("Password and Confirm password doesn't matched.");
                            }
                        }
                    }
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

    public void closeKeypad() {
        try {
            ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
