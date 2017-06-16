package com.android.llc.proringer.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.android.llc.proringer.R;
import com.android.llc.proringer.fragments.messaging.IndevidualMessaging;
import com.android.llc.proringer.fragments.messaging.ProjectMessaging;
import com.android.llc.proringer.utils.Logger;
import com.android.llc.proringer.viewsmod.textview.ProSemiBoldTextView;

/**
 * Created by bodhidipta on 13/06/17.
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
 */

public class DetailedMessaging extends AppCompatActivity {

    ProSemiBoldTextView header_text;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_project_messaging);
        header_text = (ProSemiBoldTextView) findViewById(R.id.header_text);
        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        transactProjectMessaging();
    }

    public void transactProjectMessaging() {
        Logger.prontMessage("Tag_frg", "" + getSupportFragmentManager().getBackStackEntryCount());
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, new ProjectMessaging(), "" + ProjectMessaging.class.getCanonicalName());
        transaction.commit();
        transaction.addToBackStack("" + ProjectMessaging.class.getCanonicalName());
    }

    public void transactIndevidualMessaging() {
        Logger.prontMessage("Tag_frg", "" + getSupportFragmentManager().getBackStackEntryCount());
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, new IndevidualMessaging(), "" + IndevidualMessaging.class.getCanonicalName());
        transaction.commit();
        transaction.addToBackStack("" + IndevidualMessaging.class.getCanonicalName());
    }

    @Override
    public void onBackPressed() {

        if (getSupportFragmentManager().getBackStackEntryCount() > 1) {
            super.onBackPressed();
        } else {
            finish();
        }
    }
}
