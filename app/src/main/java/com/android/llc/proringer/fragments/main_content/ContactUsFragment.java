package com.android.llc.proringer.fragments.main_content;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.android.llc.proringer.R;
import com.android.llc.proringer.helper.ProServiceApiHelper;
import com.android.llc.proringer.viewsmod.edittext.ProLightEditText;


/**
 * Created by su on 7/19/17.
 */

public class ContactUsFragment extends Fragment {
    ProLightEditText first_name, last_name, email, phonenumber, contact_info;
    ProgressDialog pgDialog;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_contact_us, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        first_name = (ProLightEditText) view.findViewById(R.id.first_name);
        last_name = (ProLightEditText) view.findViewById(R.id.last_name);
        email = (ProLightEditText) view.findViewById(R.id.email);
        phonenumber = (ProLightEditText) view.findViewById(R.id.phonenumber);
        contact_info = (ProLightEditText) view.findViewById(R.id.contact_info);

        view.findViewById(R.id.contact_us).setOnClickListener(new View.OnClickListener() {
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

                    if(phonenumber.getText().toString().trim().equals("")){
                        phonenumber.setError("Phone number name can not be blank.");
                        phonenumber.requestFocus();
                    }
                    else {
                        if (contact_info.getText().toString().trim().equals("")){
                            contact_info.setError("Phone number name can not be blank.");
                            contact_info.requestFocus();
                        }else {
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

    private void getSubmitParams() {
        ProServiceApiHelper.getInstance(getActivity()).contactUs(
                new ProServiceApiHelper.getApiProcessCallback() {
                    @Override
                    public void onStart() {
                        pgDialog = new ProgressDialog(getActivity());
                        pgDialog.setTitle("Contact Us");
                        pgDialog.setMessage("your contact address is sending to us.Please wait...");
                        pgDialog.setCancelable(false);
                        pgDialog.show();
                    }

                    @Override
                    public void onComplete(String message) {
                        if (pgDialog != null && pgDialog.isShowing())
                            pgDialog.dismiss();
                        new AlertDialog.Builder(getActivity())
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
                        new AlertDialog.Builder(getActivity())
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
}
