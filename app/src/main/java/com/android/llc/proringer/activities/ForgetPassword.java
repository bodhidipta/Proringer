package com.android.llc.proringer.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BaseTransientBottomBar;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Patterns;
import android.view.View;

import com.android.llc.proringer.R;
import com.android.llc.proringer.viewsmod.edittext.ProLightEditText;
import com.android.llc.proringer.viewsmod.textview.ProRegularTextView;
import com.android.llc.proringer.viewsmod.textview.ProSemiBoldTextView;

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

public class ForgetPassword extends AppCompatActivity {
    private ProSemiBoldTextView header_text;
    private ProLightEditText email;
    private ProRegularTextView submit_email;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acitivity_forget_password);
        header_text = (ProSemiBoldTextView) findViewById(R.id.header_text);
        email = (ProLightEditText) findViewById(R.id.email);

        findViewById(R.id.submit_email).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Patterns.EMAIL_ADDRESS.matcher(email.getText().toString().trim()).matches()) {
                    findViewById(R.id.pre_submit_email).setVisibility(View.GONE);
                    header_text.setText("RESET PASSWORD");
                } else {
                    Snackbar.make(findViewById(R.id.main_container), "Email address is not valid. Please enter a valid email address .", BaseTransientBottomBar.LENGTH_SHORT).show();
                }
            }
        });

        findViewById(R.id.tool_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
}
