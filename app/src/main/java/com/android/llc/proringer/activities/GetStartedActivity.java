package com.android.llc.proringer.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.android.llc.proringer.R;
import com.android.llc.proringer.adapter.GetStartedTutorialPagerAdapter;
import com.android.llc.proringer.helper.CustomAlert;
import com.android.llc.proringer.helper.MyCustomAlertListener;
import com.android.llc.proringer.helper.ProServiceApiHelper;
import com.android.llc.proringer.utils.Logger;
import com.android.llc.proringer.viewsmod.textview.ProRegularTextView;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.text.DateFormat;
import java.util.Date;


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

public class GetStartedActivity extends AppCompatActivity implements
        LocationListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, MyCustomAlertListener {

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    private ViewPager get_started_pager;
    private GetStartedTutorialPagerAdapter adapter;
    private ImageView pager_dot_one, pager_dot_two,
    //            pager_dot_three,
    pager_dot_four, pager_dot_five, slide_left, slide_right;
    private ProRegularTextView get_started, sign_in;


    private static final String TAG = "GetStartedActivity";
    private static final long INTERVAL = 1000 * 10;//// Update location every 10 second
    private static final long FASTEST_INTERVAL = 1000 * 1;
    LocationRequest mLocationRequest;
    GoogleApiClient mGoogleApiClient;
    Location mCurrentLocation;
    String mLastUpdateTime;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_started);

        pager_dot_one = (ImageView) findViewById(R.id.pager_dot_one);
        pager_dot_two = (ImageView) findViewById(R.id.pager_dot_two);
        //pager_dot_three = (ImageView) findViewById(R.id.pager_dot_three);
        pager_dot_four = (ImageView) findViewById(R.id.pager_dot_four);
        pager_dot_five = (ImageView) findViewById(R.id.pager_dot_five);

        slide_left = (ImageView) findViewById(R.id.slide_left);
        slide_right = (ImageView) findViewById(R.id.slide_right);
        get_started_pager = (ViewPager) findViewById(R.id.get_started_pager);
        get_started = (ProRegularTextView) findViewById(R.id.get_started);
        sign_in = (ProRegularTextView) findViewById(R.id.sign_in);

        Logger.printMessage(TAG, "onCreate ...............................");
        //show error dialog if GoolglePlayServices not available
        if (!isGooglePlayServicesAvailable()) {
            finish();
        }
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);


        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();


        get_started.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(GetStartedActivity.this, SignUpActivity.class));
            }
        });
        sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(GetStartedActivity.this, LogInActivity.class));
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
                if (get_started_pager.getCurrentItem() != 3) {
                    int spipe = get_started_pager.getCurrentItem() + 1;
                    get_started_pager.setCurrentItem(spipe, true);
                }
            }
        });

        findViewById(R.id.post_project).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(GetStartedActivity.this, PostProjectActivity.class));
            }
        });

        adapter = new GetStartedTutorialPagerAdapter(getSupportFragmentManager());
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
                //pager_dot_three.setBackgroundResource(R.drawable.circle_dark);
                pager_dot_four.setBackgroundResource(R.drawable.circle_dark);
                pager_dot_five.setBackgroundResource(R.drawable.circle_dark);
                break;
            case 1:
                pager_dot_one.setBackgroundResource(R.drawable.circle_dark);
                pager_dot_two.setBackgroundResource(R.drawable.circle_orenge_border);
                //pager_dot_three.setBackgroundResource(R.drawable.circle_dark);
                pager_dot_four.setBackgroundResource(R.drawable.circle_dark);
                pager_dot_five.setBackgroundResource(R.drawable.circle_dark);
                break;
            case 2:
                pager_dot_one.setBackgroundResource(R.drawable.circle_dark);
                pager_dot_two.setBackgroundResource(R.drawable.circle_dark);
                //pager_dot_three.setBackgroundResource(R.drawable.circle_orenge_border);
                pager_dot_four.setBackgroundResource(R.drawable.circle_orenge_border);
                pager_dot_five.setBackgroundResource(R.drawable.circle_dark);
                break;
            case 3:
                pager_dot_one.setBackgroundResource(R.drawable.circle_dark);
                pager_dot_two.setBackgroundResource(R.drawable.circle_dark);
                //pager_dot_three.setBackgroundResource(R.drawable.circle_dark);
                pager_dot_four.setBackgroundResource(R.drawable.circle_dark);
                pager_dot_five.setBackgroundResource(R.drawable.circle_orenge_border);
                break;
//            case 4:
//                pager_dot_one.setBackgroundResource(R.drawable.circle_dark);
//                pager_dot_two.setBackgroundResource(R.drawable.circle_dark);
//                //pager_dot_three.setBackgroundResource(R.drawable.circle_dark);
//                pager_dot_four.setBackgroundResource(R.drawable.circle_dark);
//                pager_dot_five.setBackgroundResource(R.drawable.circle_orenge_border);
//                break;
        }
    }


    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

                CustomAlert customAlert = new CustomAlert(GetStartedActivity.this, getResources().getString(R.string.title_location_permission), getResources().getString(R.string.text_location_permission), GetStartedActivity.this);
                customAlert.createNormalAlert("ok", 1);

            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        //Request location updates:
                        ///////////////Here called location /////////////////

                        mGoogleApiClient.connect();

                        if (mGoogleApiClient.isConnected()) {
                            PendingResult<Status> pendingResult = LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
                            Logger.printMessage(TAG, "Location update started ..............: ");
                            Logger.printMessage(TAG, "Location update resumed .....................");
                        }
                    }

                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.

                }
                return;
            }

        }
    }

    @Override
    public void onLocationChanged(Location location) {
        Logger.printMessage(TAG, "Firing onLocationChanged..............................................");
        mCurrentLocation = location;
        mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());
        updateUI();
    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Logger.printMessage(TAG, "onConnected - isConnected ...............: " + mGoogleApiClient.isConnected());
        if (checkLocationPermission()) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {

                ///////////////Here called location /////////////////

                PendingResult<Status> pendingResult = LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
                Logger.printMessage(TAG, "Location update started ..............: ");
            }
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Logger.printMessage(TAG, "Connection failed: " + connectionResult.toString());

    }


    private boolean isGooglePlayServicesAvailable() {
        GoogleApiAvailability googleAPI = GoogleApiAvailability.getInstance();
        int result = googleAPI.isGooglePlayServicesAvailable(this);
        if (result != ConnectionResult.SUCCESS) {
            if (googleAPI.isUserResolvableError(result)) {
                googleAPI.getErrorDialog(this, result,
                        0).show();
            }

            return false;
        }
        return true;
    }


    private void updateUI() {
        Logger.printMessage(TAG, "UI update initiated .............");
        if (null != mCurrentLocation) {
            String lat = String.valueOf(mCurrentLocation.getLatitude());
            String lng = String.valueOf(mCurrentLocation.getLongitude());

            ProServiceApiHelper.getInstance(GetStartedActivity.this).setCurrentLatLng(lat, lng);

            Logger.printMessage("updateUI", "At Time: " + mLastUpdateTime + "\n" +
                    "Latitude: " + lat + "\n" +
                    "Longitude: " + lng + "\n" +
                    "Accuracy: " + mCurrentLocation.getAccuracy() + "\n" +
                    "Provider: " + mCurrentLocation.getProvider());
        } else {
            Logger.printMessage(TAG, "location is null ...............");
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        Logger.printMessage(TAG, "onStart fired ..............");

        if (checkLocationPermission()) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {

                ///////////////Here called location /////////////////
                mGoogleApiClient.connect();
            }
        }
    }


    @Override
    protected void onResume() {
        super.onResume();

        if (checkLocationPermission()) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {

                ///////////////Here called location /////////////////
                if (mGoogleApiClient.isConnected()) {
                    PendingResult<Status> pendingResult = LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
                    Logger.printMessage(TAG, "Location update resumed .....................");
                }
            }
        }
    }


    @Override
    protected void onPause() {
        super.onPause();

        if (checkLocationPermission()) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {

                ///////////////Here called location /////////////////
                if (mGoogleApiClient.isConnected()) {
                    LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
                    Logger.printMessage(TAG, "Location update stopped .......................");
                }
            }
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        Logger.printMessage(TAG, "onStop fired ..............");

        if (checkLocationPermission()) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {

                ///////////////Here called location /////////////////
                if (mGoogleApiClient.isConnected()) {
                    mGoogleApiClient.disconnect();
                    Logger.printMessage(TAG, "isConnected ...............: " + mGoogleApiClient.isConnected());
                }
            }
        }
    }

    @Override
    public void callbackForAlert(String result, int i) {
        if (result.equalsIgnoreCase("ok") && i == 1) {
            ActivityCompat.requestPermissions(GetStartedActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_LOCATION);
        }
    }
}
