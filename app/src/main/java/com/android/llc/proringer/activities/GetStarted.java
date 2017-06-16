package com.android.llc.proringer.activities;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.android.llc.proringer.R;
import com.android.llc.proringer.adapter.GetStartedTutorial;
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

public class GetStarted extends AppCompatActivity {
    private ViewPager get_started_pager;
    private GetStartedTutorial adapter;
    private ImageView pager_dot_one, pager_dot_two, pager_dot_three, pager_dot_four, pager_dot_five, slide_left, slide_right;
    private ProRegularTextView get_started, sign_in;
    public static final int LOG_IN_REQUEST = 1;
    public static final int SIGN_UP_REQUEST = 2;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_started);

        pager_dot_one = (ImageView) findViewById(R.id.pager_dot_one);
        pager_dot_two = (ImageView) findViewById(R.id.pager_dot_two);
        pager_dot_three = (ImageView) findViewById(R.id.pager_dot_three);
        pager_dot_four = (ImageView) findViewById(R.id.pager_dot_four);
        pager_dot_five = (ImageView) findViewById(R.id.pager_dot_five);

        slide_left = (ImageView) findViewById(R.id.slide_left);
        slide_right = (ImageView) findViewById(R.id.slide_right);
        get_started_pager = (ViewPager) findViewById(R.id.get_started_pager);
        get_started = (ProRegularTextView) findViewById(R.id.get_started);
        sign_in = (ProRegularTextView) findViewById(R.id.sign_in);

        get_started.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(GetStarted.this, SignUp.class), LOG_IN_REQUEST);
            }
        });
        sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(GetStarted.this, LogIn.class), SIGN_UP_REQUEST);
            }
        });

        slide_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (get_started_pager.getCurrentItem() != 0) {
                    int spipe = get_started_pager.getCurrentItem() - 1;
                    get_started_pager.setCurrentItem(spipe, true);
                }

            }
        });
        slide_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (get_started_pager.getCurrentItem() != 4) {
                    int spipe = get_started_pager.getCurrentItem() + 1;
                    get_started_pager.setCurrentItem(spipe, true);
                }
            }
        });


        adapter = new GetStartedTutorial(getSupportFragmentManager());
        get_started_pager.setAdapter(adapter);

        get_started_pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                manageDots(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void manageDots(int position) {
        switch (position) {
            case 0:
                pager_dot_one.setBackgroundResource(R.drawable.circle_orenge_border);
                pager_dot_two.setBackgroundResource(R.drawable.circle_dark);
                pager_dot_three.setBackgroundResource(R.drawable.circle_dark);
                pager_dot_four.setBackgroundResource(R.drawable.circle_dark);
                pager_dot_five.setBackgroundResource(R.drawable.circle_dark);
                break;
            case 1:
                pager_dot_one.setBackgroundResource(R.drawable.circle_dark);
                pager_dot_two.setBackgroundResource(R.drawable.circle_orenge_border);
                pager_dot_three.setBackgroundResource(R.drawable.circle_dark);
                pager_dot_four.setBackgroundResource(R.drawable.circle_dark);
                pager_dot_five.setBackgroundResource(R.drawable.circle_dark);
                break;
            case 2:
                pager_dot_one.setBackgroundResource(R.drawable.circle_dark);
                pager_dot_two.setBackgroundResource(R.drawable.circle_dark);
                pager_dot_three.setBackgroundResource(R.drawable.circle_orenge_border);
                pager_dot_four.setBackgroundResource(R.drawable.circle_dark);
                pager_dot_five.setBackgroundResource(R.drawable.circle_dark);
                break;
            case 3:
                pager_dot_one.setBackgroundResource(R.drawable.circle_dark);
                pager_dot_two.setBackgroundResource(R.drawable.circle_dark);
                pager_dot_three.setBackgroundResource(R.drawable.circle_dark);
                pager_dot_four.setBackgroundResource(R.drawable.circle_orenge_border);
                pager_dot_five.setBackgroundResource(R.drawable.circle_dark);
                break;
            case 4:
                pager_dot_one.setBackgroundResource(R.drawable.circle_dark);
                pager_dot_two.setBackgroundResource(R.drawable.circle_dark);
                pager_dot_three.setBackgroundResource(R.drawable.circle_dark);
                pager_dot_four.setBackgroundResource(R.drawable.circle_dark);
                pager_dot_five.setBackgroundResource(R.drawable.circle_orenge_border);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SIGN_UP_REQUEST) {
            startActivity(new Intent(GetStarted.this, LandScreenActivity.class));
            finish();
        } else if (LOG_IN_REQUEST == requestCode) {
            startActivity(new Intent(GetStarted.this, LandScreenActivity.class));
            finish();
        }
    }
}
