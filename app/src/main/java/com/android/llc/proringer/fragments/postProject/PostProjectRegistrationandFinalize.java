package com.android.llc.proringer.fragments.postProject;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.android.llc.proringer.R;
import com.android.llc.proringer.activities.ActivityPostProject;
import com.android.llc.proringer.appconstant.ProApplication;
import com.android.llc.proringer.viewsmod.edittext.ProLightEditText;

/**
 * Created by su on 7/13/17.
 */

public class PostProjectRegistrationandFinalize extends Fragment {
    private ProLightEditText first_name, last_name, email, password, confirm_password, zip_code;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.content_post_project_registration, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        first_name = (ProLightEditText) view.findViewById(R.id.first_name);
        last_name = (ProLightEditText) view.findViewById(R.id.last_name);
        email = (ProLightEditText) view.findViewById(R.id.email);
        password = (ProLightEditText) view.findViewById(R.id.password);
        confirm_password = (ProLightEditText) view.findViewById(R.id.confirm_password);
        zip_code = (ProLightEditText) view.findViewById(R.id.zip_code);


        if (ProApplication.getInstance().getUserId().equals("")) {
/**
 * IF no user is login then visible registration process or visible last part
 */
            view.findViewById(R.id.container_registration).setVisibility(View.VISIBLE);
        } else {
            view.findViewById(R.id.content_post_form_submit).setVisibility(View.VISIBLE);
        }


        view.findViewById(R.id.register).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateRegistration()) {

                    ((ActivityPostProject) getActivity()).first_name = first_name.getText().toString().trim();
                    ((ActivityPostProject) getActivity()).last_name = last_name.getText().toString().trim();
                    ((ActivityPostProject) getActivity()).email = email.getText().toString().trim();
                    ((ActivityPostProject) getActivity()).confirm_password = confirm_password.getText().toString().trim();
                    ((ActivityPostProject) getActivity()).completePostProject();
                }

            }
        });

    }

    private boolean validateRegistration() {
        if (first_name.getText().toString().trim().equals("")) {
            first_name.setError("Please enter First name.");
            return false;
        } else {
            if (last_name.getText().toString().trim().equals("")) {
                last_name.setError("Please enter Last name.");
                return false;
            } else {
                if (email.getText().toString().trim().equals("")) {
                    email.setError("Please enter Email.");
                    return false;

                } else {
                    if (Patterns.EMAIL_ADDRESS.matcher(email.getText().toString().trim()).matches()) {
                        if (password.getText().toString().trim().equals("")) {
                            password.setError("Please enter Password.");
                            return false;

                        } else {
                            if (confirm_password.getText().toString().trim().equals("")) {
                                confirm_password.setError("Please enter Confirm password.");
                                return false;

                            } else {

                                if (confirm_password.getText().toString().trim().equals(password.getText().toString().trim())) {

                                    if (confirm_password.getText().toString().trim().length() > 6 &&
                                            confirm_password.getText().toString().trim().matches(".*[^0-9].*")) {
                                        if (zip_code.getText().toString().trim().equals("")) {
                                            zip_code.setError("Please enter Zip.");
                                            return false;
                                        } else {
                                            ((ActivityPostProject) getActivity()).completePostProject();
                                            return true;
                                        }
                                    } else {

                                        if (confirm_password.getText().toString().trim().length() < 6) {
                                            confirm_password.setError("Password should contains at least 6 character.");
                                        } else if (!confirm_password.getText().toString().trim().matches(".*[^0-9].*")) {
                                            confirm_password.setError("Password should combination of Alphanumeric and Number.");
                                        }
                                        return false;
                                    }


                                } else {
                                    confirm_password.setError("Password and Confirm password does not matched.");
                                    return false;
                                }
                            }
                        }
                    } else {
                        email.setError("Please enter valid Email address.");
                        return false;

                    }
                }
            }
        }
    }
}
