package com.android.llc.proringer.fragments.drawerNav;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.llc.proringer.R;
import com.android.llc.proringer.activities.LandScreenActivity;
import com.android.llc.proringer.helper.CustomAlert;
import com.android.llc.proringer.helper.MyCustomAlertListener;
import com.android.llc.proringer.helper.MyLoader;
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

public class InviteAFriendFragment extends Fragment implements MyCustomAlertListener {
    ProLightEditText first_name, last_name, email, confirm_email;
    ProRegularTextView invited_submit;
    MyLoader myLoader = null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_invite_friend, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        first_name = (ProLightEditText) view.findViewById(R.id.first_name);
        last_name = (ProLightEditText) view.findViewById(R.id.last_name);
        email = (ProLightEditText) view.findViewById(R.id.email);
        confirm_email = (ProLightEditText) view.findViewById(R.id.confirm_email);
        invited_submit = (ProRegularTextView) view.findViewById(R.id.invited_submit);
        invited_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateInvite();
            }
        });

        myLoader = new MyLoader(getActivity());

    }

    private void validateInvite() {
        if (first_name.getText().toString().trim().equals("")) {
            first_name.setError("First name can not be blank.");
        } else {
            if (last_name.getText().toString().trim().equals("")) {
                last_name.setError("Last name can not be blank.");
                last_name.requestFocus();
            } else {
                if (email.getText().toString().trim().equals("")) {
                    email.setError("Email name can not be blank.");
                    email.requestFocus();
                } else {
                    if (Patterns.EMAIL_ADDRESS.matcher(email.getText().toString().trim()).matches()) {
                        if (email.getText().toString().trim().equals(confirm_email.getText().toString().trim())) {
                            getSubmitParams();
                        } else {
                            confirm_email.setError("Email and confirm email does not match.");
                            confirm_email.requestFocus();
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
        ProServiceApiHelper.getInstance((LandScreenActivity) getActivity()).inviteFriends(
                new ProServiceApiHelper.getApiProcessCallback() {
                    @Override
                    public void onStart() {
                        myLoader.showLoader();
                    }

                    @Override
                    public void onComplete(String message) {
                        if (myLoader != null && myLoader.isMyLoaderShowing())
                            myLoader.dismissLoader();

                        CustomAlert customAlert = new CustomAlert(getActivity(), "Invite Friend", "" + message, InviteAFriendFragment.this);
                        customAlert.createNormalAlert("ok", 1);
                    }

                    @Override
                    public void onError(String error) {
                        if (myLoader != null && myLoader.isMyLoaderShowing())
                            myLoader.dismissLoader();

                        CustomAlert customAlert = new CustomAlert(getActivity(), "Invite Friend Error", "" + error, InviteAFriendFragment.this);
                        customAlert.getListenerRetryCancelFromNormalAlert("retry", "abort", 1);

                    }
                },
                first_name.getText().toString().trim(),
                last_name.getText().toString().trim(),
                email.getText().toString().trim(),
                confirm_email.getText().toString().trim());
    }

    private void resetForm() {
        first_name.setText("");
        last_name.setText("");
        email.setText("");
        confirm_email.setText("");
    }

    @Override
    public void callbackForAlert(String result, int i) {
        if (result.equalsIgnoreCase("ok") && i == 1) {
            resetForm();
        } else if (result.equalsIgnoreCase("retry") && i == 1) {
            validateInvite();
        }
    }
}
