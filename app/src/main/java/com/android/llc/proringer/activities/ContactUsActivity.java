package com.android.llc.proringer.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Patterns;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.android.llc.proringer.R;
import com.android.llc.proringer.helper.CustomAlert;
import com.android.llc.proringer.helper.MyCustomAlertListener;
import com.android.llc.proringer.helper.MyLoader;
import com.android.llc.proringer.helper.ProServiceApiHelper;
import com.android.llc.proringer.viewsmod.edittext.ProLightEditText;

/**
 * Created by su on 7/19/17.
 */

public class ContactUsActivity extends AppCompatActivity implements MyCustomAlertListener {
    ProLightEditText first_name, last_name, email, phonenumber;
    MyLoader myLoader;
    EditText contact_info;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        myLoader = new MyLoader(ContactUsActivity.this);


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
        ProServiceApiHelper.getInstance(ContactUsActivity.this).contactUsAPI(
                new ProServiceApiHelper.getApiProcessCallback() {
                    @Override
                    public void onStart() {
                        myLoader.showLoader();
                    }

                    @Override
                    public void onComplete(String message) {
                        if (myLoader != null && myLoader.isMyLoaderShowing())
                            myLoader.dismissLoader();

                        CustomAlert customAlert = new CustomAlert(ContactUsActivity.this, "Contact Us", "" + message, ContactUsActivity.this);
                        customAlert.createNormalAlert("ok", 1);
                    }

                    @Override
                    public void onError(String error) {
                        if (myLoader != null && myLoader.isMyLoaderShowing())
                            myLoader.dismissLoader();


                        CustomAlert customAlert = new CustomAlert(ContactUsActivity.this, "Contact Us Error", "" + error, ContactUsActivity.this);
                        customAlert.getListenerRetryCancelFromNormalAlert("retry", "abort", 1);
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

    @Override
    public void callbackForAlert(String result, int i) {

        if (result.equalsIgnoreCase("ok") && i == 1) {
            resetForm();
        } else if (result.equalsIgnoreCase("retry") && i == 1) {
            validateContactUs();
        }
    }
}
