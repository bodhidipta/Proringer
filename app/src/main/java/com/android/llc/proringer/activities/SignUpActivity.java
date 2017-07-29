package com.android.llc.proringer.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Patterns;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.android.llc.proringer.R;
import com.android.llc.proringer.helper.ProServiceApiHelper;
import com.android.llc.proringer.utils.Logger;
import com.android.llc.proringer.viewsmod.edittext.ProLightEditText;
import com.android.llc.proringer.viewsmod.textview.ProBoldTextView;
import com.android.llc.proringer.viewsmod.textview.ProRegularTextView;
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

public class SignUpActivity extends AppCompatActivity {
    private ProBoldTextView sign_in;
    private RelativeLayout sign_up_with_facebook;
    private ProLightEditText first_name, last_name, email, password, confirm_password, zip_code;
    private ProSemiBoldTextView complete_submit;
    private LinearLayout main_content;
    private ProgressDialog pgDialog = null;
    private InputMethodManager keyboard;
    private RelativeLayout container_confirm;
    private ProRegularTextView email_confirmed, contact, mail_resent;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        keyboard = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        sign_up_with_facebook = (RelativeLayout) findViewById(R.id.sign_up_with_facebook);

        first_name = (ProLightEditText) findViewById(R.id.first_name);
        last_name = (ProLightEditText) findViewById(R.id.last_name);
        email = (ProLightEditText) findViewById(R.id.email);
        password = (ProLightEditText) findViewById(R.id.password);
        confirm_password = (ProLightEditText) findViewById(R.id.confirm_password);
        zip_code = (ProLightEditText) findViewById(R.id.zip_code);

        complete_submit = (ProSemiBoldTextView) findViewById(R.id.complete_submit);

        main_content = (LinearLayout) findViewById(R.id.main_content);
        container_confirm = (RelativeLayout) findViewById(R.id.container_confirm);
        email_confirmed = (ProRegularTextView) findViewById(R.id.email_confirmed);
        contact = (ProRegularTextView) findViewById(R.id.contact);
        mail_resent = (ProRegularTextView) findViewById(R.id.mail_resent);
        contact.setMovementMethod(LinkMovementMethod.getInstance());
        mail_resent.setMovementMethod(LinkMovementMethod.getInstance());

        findViewById(R.id.terms_of_use).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(SignUpActivity.this, TermsPrivacyActivity.class);
                intent.putExtra("value", "term");
                startActivity(intent);

            }
        });


        findViewById(R.id.privacy_policy).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(SignUpActivity.this, TermsPrivacyActivity.class);
                intent.putExtra("value", "policy");
                startActivity(intent);
            }
        });

        /**
         * Contact us spannable text with click listener
         */
        String contactTextOne = "If you already request your confirmation email to be reset and you have not received that email within 30 minutes and do not see it in your Spam/Junk folder please  ";
        String contactTextClick = "contact us ";
        String contactTextTwo = "and include a phone number.";

        Spannable word1 = new SpannableString(contactTextOne);

        word1.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.colorTextDark)), 0, contactTextOne.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        contact.setText(word1);

        Spannable word2 = new SpannableString(contactTextClick);

        ClickableSpan myClickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                // There is the OnCLick. put your intent to Register class here
                widget.invalidate();
                Logger.printMessage("SpanHello", "click");
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                ds.setColor(getResources().getColor(R.color.colorAccent));
                ds.setUnderlineText(false);
            }
        };
        word2.setSpan(myClickableSpan, 0, contactTextClick.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        contact.append(word2);

        Spannable word3 = new SpannableString(contactTextTwo);

        word3.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.colorTextDark)), 0, contactTextTwo.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        contact.append(word3);


        /**
         * Mail resent spannable text with click listener
         */
        String mailResentPart1 = "If you do not received confirmation email within 30 minutes you can ";
        String mailResentPart2 = "request it to be resent.";

        Spannable resentWord = new SpannableString(mailResentPart1);

        resentWord.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.colorTextDark)), 0, mailResentPart1.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        mail_resent.setText(resentWord);

        Spannable resentWordClick = new SpannableString(mailResentPart2);

        ClickableSpan resentClickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                // There is the OnCLick. put your intent to Register class here
                widget.invalidate();
                Logger.printMessage("SpanHello", "click");
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                ds.setColor(getResources().getColor(R.color.colorAccent));
                ds.setUnderlineText(false);
            }
        };
        resentWordClick.setSpan(resentClickableSpan, 0, mailResentPart2.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        mail_resent.append(resentWordClick);


        sign_in = (ProBoldTextView) findViewById(R.id.sign_in);
        sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignUpActivity.this, LogInActivity.class));
            }
        });

        complete_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateRegistration();
            }
        });

        findViewById(R.id.confirm_login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(SignUpActivity.this, LogInActivity.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
            finish();
    }

    private void validateRegistration() {
        if (first_name.getText().toString().trim().equals("")) {
            first_name.setError("Please enter First name.");
        } else {
            if (last_name.getText().toString().trim().equals("")) {
                last_name.setError("Please enter Last name.");
            } else {
                if (email.getText().toString().trim().equals("")) {
                    email.setError("Please enter Email.");

                } else {
                    if (Patterns.EMAIL_ADDRESS.matcher(email.getText().toString().trim()).matches()) {
                        if (password.getText().toString().trim().equals("")) {
                            password.setError("Please enter Password.");

                        } else {
                            if (confirm_password.getText().toString().trim().equals("")) {
                                confirm_password.setError("Please enter Confirm password.");

                            } else {

                                if (confirm_password.getText().toString().trim().equals(password.getText().toString().trim())) {
                                    if (confirm_password.getText().toString().trim().length() > 6 &&
                                            confirm_password.getText().toString().trim().matches(".*[^0-9].*")) {
                                        if (zip_code.getText().toString().trim().equals("")) {
                                            zip_code.setError("Please enter Zip.");
                                        } else {
                                            callRegisteration();
                                        }
                                    } else {

                                        if (confirm_password.getText().toString().trim().length() < 6) {
                                            confirm_password.setError("Password should contains at least 6 character.");
                                        } else if (!confirm_password.getText().toString().trim().matches(".*[^0-9].*")) {
                                            confirm_password.setError("Password should combination of Alphanumeric and Number.");
                                        }
                                    }


                                } else {
                                    confirm_password.setError("Password and Confirm password does not matched.");
                                }
                            }
                        }
                    } else {
                        email.setError("Please enter valid Email address.");

                    }
                }
            }
        }
    }

    private void callRegisteration() {
        ProServiceApiHelper.getInstance(SignUpActivity.this).getUserRegistered(new ProServiceApiHelper.getApiProcessCallback() {
                                                                           @Override
                                                                           public void onStart() {
                                                                               pgDialog = new ProgressDialog(SignUpActivity.this);
                                                                               pgDialog.setTitle("Registering");
                                                                               pgDialog.setMessage("Please wait ..");
                                                                               pgDialog.setCancelable(false);
                                                                               pgDialog.show();

                                                                           }

                                                                           @Override
                                                                           public void onComplete(String message) {
                                                                               if (pgDialog != null && pgDialog.isShowing())
                                                                                   pgDialog.dismiss();

                                                                               email_confirmed.setText(email.getText().toString().trim());

                                                                               findViewById(R.id.header_image).setVisibility(View.VISIBLE);
                                                                               findViewById(R.id.header_text).setVisibility(View.GONE);
                                                                               getSupportActionBar().setDisplayHomeAsUpEnabled(false);

                                                                               try {
                                                                                   keyboard.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
                                                                               } catch (Exception e) {
                                                                                   e.printStackTrace();
                                                                               }


                                                                               showDialog("Registration ", "" + message);

                                                                           }

                                                                           @Override
                                                                           public void onError(String error) {
                                                                               if (pgDialog != null && pgDialog.isShowing())
                                                                                   pgDialog.dismiss();
                                                                               showErrorDialog("Registration error", "" + error);
                                                                           }
                                                                       },
                first_name.getText().toString().trim(),
                last_name.getText().toString().trim(),
                email.getText().toString().trim(),
                confirm_password.getText().toString().trim(),
                zip_code.getText().toString().trim()
        );
    }

    private void showDialog(String title, String message) {
        new AlertDialog.Builder(SignUpActivity.this)
                .setTitle("" + title)
                .setMessage("" + message)
                .setCancelable(false)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        container_confirm.setVisibility(View.VISIBLE);
                    }
                })
                .show();
    }

    private void showErrorDialog(String title, String message) {
        new AlertDialog.Builder(SignUpActivity.this)
                .setTitle("" + title)
                .setMessage("" + message)
                .setCancelable(false)
                .setPositiveButton("retry", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        validateRegistration();
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


    boolean isAlphanumeric(String str) {
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if (c < 0x30 || (c >= 0x3a && c <= 0x40) || (c > 0x5a && c <= 0x60) || c > 0x7a)
                return false;
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            onBackPressed();

        return super.onOptionsItemSelected(item);
    }
}
