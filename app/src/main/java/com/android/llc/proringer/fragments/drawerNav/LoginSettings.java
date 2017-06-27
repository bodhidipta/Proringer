package com.android.llc.proringer.fragments.drawerNav;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.android.llc.proringer.R;
import com.android.llc.proringer.activities.GetStarted;
import com.android.llc.proringer.activities.LandScreenActivity;
import com.android.llc.proringer.appconstant.ProApplication;
import com.android.llc.proringer.helper.ProServiceApiHelper;
import com.android.llc.proringer.viewsmod.edittext.ProLightEditText;
import com.android.llc.proringer.viewsmod.textview.ProRegularTextView;

/**
 * Created by bodhidipta on 22/06/17.
 * <!-- * Copyright (c) 2017, The Proringer-->
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
 */

public class LoginSettings extends Fragment {
    private ProRegularTextView current_email, update_email, change_password;
    private ProLightEditText new_email, confirm_new_email, current_password, new_password, confirm_new_password;
    private ProgressDialog prgress = null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragement_login_settings, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        current_email = (ProRegularTextView) view.findViewById(R.id.current_email);
        update_email = (ProRegularTextView) view.findViewById(R.id.update_email);
        change_password = (ProRegularTextView) view.findViewById(R.id.change_password);

        current_email.setText(ProApplication.getInstance().getUserEmail());

        confirm_new_email = (ProLightEditText) view.findViewById(R.id.confirm_new_email);
        new_email = (ProLightEditText) view.findViewById(R.id.new_email);
        current_password = (ProLightEditText) view.findViewById(R.id.current_password);
        new_password = (ProLightEditText) view.findViewById(R.id.new_password);
        confirm_new_password = (ProLightEditText) view.findViewById(R.id.confirm_new_password);

        update_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateEmailAddress();
            }
        });
        change_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validatePasswordInput();
            }
        });

        view.findViewById(R.id.show_password).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction()==MotionEvent.ACTION_DOWN){
                    current_password.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                }else if (event.getAction()==MotionEvent.ACTION_UP){
                    current_password.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);

                }
                return true;
            }
        });

    }

    /**
     * Validate email address before submit
     */
    private void validateEmailAddress() {
        if (new_email.getText().toString().trim().equals("")) {
            new_email.setError("New email address can't be blank.");
        } else if (Patterns.EMAIL_ADDRESS.matcher(new_email.getText().toString().trim()).matches()) {

            if (confirm_new_email.getText().toString().trim().equals(new_email.getText().toString().trim())) {
                updateCurrentEmail();
            } else {
                new_email.setError("Confirm email address doesn't match new email address.");
            }
        } else {
            new_email.setError("Invalid email address.");
        }
    }

    /**
     * Update current email address ,upon succeeding log out current user to re login
     */
    private void updateCurrentEmail() {
        ProServiceApiHelper.getInstance(getActivity()).updateUserEmail(
                new ProServiceApiHelper.getApiProcessCallback() {
                    @Override
                    public void onStart() {
                        prgress = new ProgressDialog(getActivity());
                        prgress.setTitle("Change Email");
                        prgress.setMessage("Updating new email address. Please wait.");
                        prgress.setCancelable(false);
                        prgress.show();
                    }

                    @Override
                    public void onComplete(String message) {
                        if (prgress != null && prgress.isShowing())
                            prgress.dismiss();

                        new AlertDialog.Builder(getActivity())
                                .setTitle("Change Email")
                                .setMessage("" + message)
                                .setCancelable(false)
                                .setPositiveButton("Re-login", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                        ProApplication.getInstance().logOut();
                                        startActivity(new Intent(getActivity(), GetStarted.class));
                                        getActivity().finish();
                                    }
                                })
                                .show();

                    }

                    @Override
                    public void onError(String error) {
                        if (prgress != null && prgress.isShowing())
                            prgress.dismiss();

                        new AlertDialog.Builder(getActivity())
                                .setTitle("Change Email Error")
                                .setMessage("" + error)
                                .setCancelable(false)
                                .setPositiveButton("Retry", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                        update_email.performClick();
                                    }
                                })
                                .setNegativeButton("Abort", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                })
                                .show();

                    }
                },
                new_email.getText().toString().trim(),
                confirm_new_email.toString().trim());
    }

    /**
     * Check password input before submit
     */
    private void validatePasswordInput() {
        if (current_password.getText().toString().equals("")) {
            current_password.setError("Please enter current password");
        } else {
            if (new_password.getText().toString().trim().equals("")) {
                new_password.setError("Please enter new password");
            } else {
                if (new_password.getText().toString().trim().equals(confirm_new_password.getText().toString().trim())) {
                    updatePassword();
                } else {
                    confirm_new_password.setError("Confirm password doesn't match new password");
                }
            }
        }
    }

    /**
     * Update password , on success log out current profile to re login
     */
    private void updatePassword() {
        ProServiceApiHelper.getInstance(getActivity()).updateUserPassword(
                new ProServiceApiHelper.getApiProcessCallback() {
                    @Override
                    public void onStart() {
                        prgress = new ProgressDialog(getActivity());
                        prgress.setTitle("Change Password");
                        prgress.setMessage("Updating new password. Please wait.");
                        prgress.setCancelable(false);
                        prgress.show();
                    }

                    @Override
                    public void onComplete(String message) {
                        if (prgress != null && prgress.isShowing())
                            prgress.dismiss();

                        new AlertDialog.Builder(getActivity())
                                .setTitle("Change Password")
                                .setMessage("" + message)
                                .setCancelable(false)
                                .setPositiveButton("Re-login", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                        ProApplication.getInstance().logOut();
                                        startActivity(new Intent(getActivity(), GetStarted.class));
                                        getActivity().finish();
                                    }
                                })
                                .show();
                    }

                    @Override
                    public void onError(String error) {
                        if (prgress != null && prgress.isShowing())
                            prgress.dismiss();

                        new AlertDialog.Builder(getActivity())
                                .setTitle("Change Password Error")
                                .setMessage("" + error)
                                .setCancelable(false)
                                .setPositiveButton("Retry", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                        change_password.performClick();
                                    }
                                })
                                .setNegativeButton("Abort", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                })
                                .show();
                    }
                },
                current_password.getText().toString().trim(),
                new_password.getText().toString().trim(),
                confirm_new_password.getText().toString().trim());
    }
}
