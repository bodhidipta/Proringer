package com.android.llc.proringer.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;

import com.android.llc.proringer.R;
import com.android.llc.proringer.appconstant.ProApplication;

/**
 * Created by bodhidipta on 09/06/17.
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

public class SplashScreenActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash_screen);
        final ImageView image_l = (ImageView) findViewById(R.id.loader);

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    RotateAnimation rotate = new RotateAnimation(0, 180, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                    rotate.setDuration(3000);
                    rotate.setInterpolator(new LinearInterpolator());
                    image_l.startAnimation(rotate);
                } catch (Exception ioe) {
                    ioe.printStackTrace();
                }

            }
        });

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (ProApplication.getInstance().getUserId().equals("")) {
                    startActivity(new Intent(SplashScreenActivity.this, GetStartedActivity.class));
                    finish();
                } else {
                    startActivity(new Intent(SplashScreenActivity.this, LandScreenActivity.class));
                    finish();
                }

            }
        }, 2700);
    }
}
