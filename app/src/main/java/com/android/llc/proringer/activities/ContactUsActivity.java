package com.android.llc.proringer.activities;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Patterns;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.android.llc.proringer.R;
import com.android.llc.proringer.helper.ProServiceApiHelper;
import com.android.llc.proringer.viewsmod.edittext.ProLightEditText;

/**
 * Created by su on 7/19/17.
 */

public class ContactUsActivity extends AppCompatActivity {
    ProLightEditText first_name, last_name, email, phonenumber;
    ProgressDialog pgDialog;
    EditText contact_info;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        first_name = (ProLightEditText) findViewById(R.id.first_name);
        last_name = (ProLightEditText) findViewById(R.id.last_name);
        email = (ProLightEditText) findViewById(R.id.email);
        phonenumber = (ProLightEditText) findViewById(R.id.phonenumber);
        contact_info = (EditText) findViewById(R.id.contact_info);

        findViewById(R.id.contact_us).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateContactUs();
            }
        });
    }

    private void validateContactUs() {
        if (first_name.getText().toString().trim().equals("")) {
            first_name.setError("First name can not be blank.");
        } else {
            if (last_name.getText().toString().trim().equals("")) {
                last_name.setError("Last name can not be blank.");
                last_name.requestFocus();
            } else {
                if (email.getText().toString().trim().equals("")) {
                    email.setError("Email name can not be blank.");
                } else {
                    if (Patterns.EMAIL_ADDRESS.matcher(email.getText().toString().trim()).matches()) {

                        if (phonenumber.getText().toString().trim().equals("")) {
                            phonenumber.setError("Phone number name can not be blank.");
                            phonenumber.requestFocus();
                        } else {
                            if (contact_info.getText().toString().trim().equals("")) {
                                contact_info.setError("Phone number name can not be blank.");
                                contact_info.requestFocus();
                            } else {
                                getSubmitParams();
                            }
                        }
                    } else {
                        email.setError("Invalid email address.");
                        email.requestFocus();
                    }
                }
            }
        }
    }

    private void getSubmitParams() {
        ProServiceApiHelper.getInstance(ContactUsActivity.this).contactUs(
                new ProServiceApiHelper.getApiProcessCallback() {
                    @Override
                    public void onStart() {
                        pgDialog = new ProgressDialog(ContactUsActivity.this);
                        pgDialog.setTitle("Contact Us");
                        pgDialog.setMessage("your contact address is sending to us.Please wait...");
                        pgDialog.setCancelable(false);
                        pgDialog.show();
                    }

                    @Override
                    public void onComplete(String message) {
                        if (pgDialog != null && pgDialog.isShowing())
                            pgDialog.dismiss();
                        new AlertDialog.Builder(ContactUsActivity.this)
                                .setTitle("Contact Us")
                                .setMessage("" + message)
                                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                        resetForm();
                                    }
                                })
                                .setCancelable(false)
                                .show();
                    }

                    @Override
                    public void onError(String error) {
                        if (pgDialog != null && pgDialog.isShowing())
                            pgDialog.dismiss();
                        new AlertDialog.Builder(ContactUsActivity.this)
                                .setTitle("Contact Us Error")
                                .setMessage("" + error)
                                .setPositiveButton("retry", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                        validateContactUs();
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
                },
                first_name.getText().toString().trim(),
                last_name.getText().toString().trim(),
                email.getText().toString().trim(),
                phonenumber.getText().toString().trim(),
                contact_info.getText().toString().trim());
    }

    private void resetForm() {
        first_name.setText("");
        last_name.setText("");
        email.setText("");
        phonenumber.setText("");
        contact_info.setText("");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
