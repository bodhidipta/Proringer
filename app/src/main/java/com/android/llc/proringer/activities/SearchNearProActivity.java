package com.android.llc.proringer.activities;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.RelativeLayout;

import com.android.llc.proringer.R;
import com.android.llc.proringer.helper.CustomAlert;
import com.android.llc.proringer.helper.MyCustomAlertListener;
import com.android.llc.proringer.helper.MyLoader;
import com.android.llc.proringer.helper.ProServiceApiHelper;
import com.android.llc.proringer.pojo.SetGetAddressData;
import com.android.llc.proringer.utils.Logger;
import com.android.llc.proringer.viewsmod.edittext.ProLightEditText;
import com.android.llc.proringer.viewsmod.textview.ProRegularTextView;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by su on 8/23/17.
 */

public class SearchNearProActivity extends AppCompatActivity implements
        LocationListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, MyCustomAlertListener {

    private MyLoader myLoader = null;

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    private static final String TAG = "SearchNearProActivity";
    private static final long INTERVAL = 1000 * 100;//// Update location every 10 second
    private static final long FASTEST_INTERVAL = 1000 * 1;
    LocationRequest mLocationRequest;
    GoogleApiClient mGoogleApiClient;
    Location mCurrentLocation;
    String mLastUpdateTime;
    RelativeLayout RL_SetCancel;
    private InputMethodManager keyboard;

    ProLightEditText edt_zip;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_near_pro);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((ProRegularTextView) findViewById(R.id.tv_title)).setText("Search Near");
        RL_SetCancel = (RelativeLayout) findViewById(R.id.RL_SetCancel);
        RL_SetCancel.setVisibility(View.VISIBLE);

        edt_zip = (ProLightEditText) findViewById(R.id.edt_zip);

        keyboard = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);

        myLoader = new MyLoader(SearchNearProActivity.this);


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

        edt_zip.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                edt_zip.setError(null);
                if (
//                        (event.getAction() == KeyEvent.ACTION_DOWN)
//                                ||
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    Logger.printMessage("zip", edt_zip.getText().toString());
                    if (edt_zip.getText().toString().trim().length() > 4) {
                        searchLocationUsingZip(edt_zip.getText().toString().trim());
                    } else {
                        edt_zip.setError("zip code length would be more than four");
                    }
                    return true;
                }
                return false;
            }
        });

        findViewById(R.id.LLCurrentLocation).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getCurrentLocationZip();
            }
        });

        findViewById(R.id.tv_search).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Logger.printMessage("zip", edt_zip.getText().toString());
                if (edt_zip.getText().toString().length() > 4) {
                    searchLocationUsingZip(edt_zip.getText().toString().trim());
                } else {
                    edt_zip.setError("zip code length would be more than four");
                }
            }
        });

        findViewById(R.id.tv_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closeKeypad();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {

            Intent i = new Intent();
            Bundle BD = new Bundle();
            BD.putString("searchZip", "");
            i.putExtra("data", BD);
            setResult(Activity.RESULT_OK, i);

            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public void getCurrentLocationZip() {
        ProServiceApiHelper.getInstance(SearchNearProActivity.this).getZipCodeUsingGoogleAPI(new ProServiceApiHelper.getApiProcessCallback() {
            @Override
            public void onStart() {
                // myLoader.showLoader();
            }

            @Override
            public void onComplete(String message) {
                boolean outer_block_check = false;
                //Logger.printMessage("message",message);
//                if (myLoader != null && myLoader.isMyLoaderShowing())
//                    myLoader.dismissLoader();

                try {
                    JSONObject jsonObject = new JSONObject(message);
                    JSONArray jsonArrayResults = jsonObject.getJSONArray("results");
                    if (jsonArrayResults.length() > 0) {

                        for (int i = 0; i < jsonArrayResults.length(); i++) {

                            if (outer_block_check) {
                                break;
                            }

                            /**
                             * loop through address component
                             * for country and state
                             */
                            if (jsonArrayResults.getJSONObject(i).has("address_components") &&
                                    jsonArrayResults.getJSONObject(i).getJSONArray("address_components").length() > 0) {

                                JSONArray jsonArrayAddressComponents = jsonArrayResults.getJSONObject(i).getJSONArray("address_components");

                                for (int j = 0; j < jsonArrayAddressComponents.length(); j++) {

                                    if (jsonArrayAddressComponents.getJSONObject(j).has("types") &&
                                            jsonArrayAddressComponents.getJSONObject(j).getJSONArray("types").length() > 0
                                            ) {

                                        JSONArray jsonArrayType = jsonArrayAddressComponents.getJSONObject(j).getJSONArray("types");
                                        Logger.printMessage("types", "" + jsonArrayType.get(0));

                                        if (jsonArrayType.get(0).equals("postal_code")) {
                                            Logger.printMessage("postal_code_get", "" + jsonArrayType.get(0));
                                            searchLocationUsingZip(jsonArrayAddressComponents.getJSONObject(j).getString("long_name"));
                                            outer_block_check = true;
                                            break;
                                        } else {
                                            Logger.printMessage("postal_code", "" + jsonArrayType.get(0));
                                        }
                                    }
                                }
                            }
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Logger.printMessage("JSONException", "" + e.getMessage());
//                    if (myLoader != null && myLoader.isMyLoaderShowing())
//                        myLoader.dismissLoader();
                }
            }

            @Override
            public void onError(String error) {
                Logger.printMessage("error", "" + error);
//                if (myLoader != null && myLoader.isMyLoaderShowing())
//                    myLoader.dismissLoader();
            }
        });
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

    @Override
    public void onLocationChanged(Location location) {
        Logger.printMessage(TAG, "Firing onLocationChanged..............................................");
        mCurrentLocation = location;
        mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());
        updateUI();
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
    protected void onStop() {
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

                CustomAlert customAlert = new CustomAlert(SearchNearProActivity.this, getResources().getString(R.string.title_location_permission), getResources().getString(R.string.text_location_permission), SearchNearProActivity.this);
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
    public void callbackForAlert(String result, int i) {
        if (result.equalsIgnoreCase("ok") && i == 1) {
            ActivityCompat.requestPermissions(SearchNearProActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_LOCATION);
        } else if (result.equalsIgnoreCase("ok") && i == 2) {

        }
    }

    private void updateUI() {
        Logger.printMessage(TAG, "UI update initiated .............");
        if (null != mCurrentLocation) {
            String lat = String.valueOf(mCurrentLocation.getLatitude());
            String lng = String.valueOf(mCurrentLocation.getLongitude());

            ProServiceApiHelper.getInstance(SearchNearProActivity.this).setCurrentLatLng(lat, lng);

            Logger.printMessage("updateUI", "At Time: " + mLastUpdateTime + "\n" +
                    "Latitude: " + lat + "\n" +
                    "Longitude: " + lng + "\n" +
                    "Accuracy: " + mCurrentLocation.getAccuracy() + "\n" +
                    "Provider: " + mCurrentLocation.getProvider());
        } else {
            Logger.printMessage(TAG, "location is null ...............");
        }
    }

    public void closeKeypad() {
        try {
            keyboard.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void searchLocationUsingZip(String key) {
        ProServiceApiHelper.getInstance(SearchNearProActivity.this).getSearchAreaAPI(new ProServiceApiHelper.onSearchZipCallback() {
            @Override
            public void onComplete(List<SetGetAddressData> listdata) {
                if (myLoader != null && myLoader.isMyLoaderShowing())
                    myLoader.dismissLoader();
                Logger.printMessage("listData", "" + listdata.size());
                closeKeypad();

                Intent i = new Intent();
                Bundle BD = new Bundle();
                BD.putString("searchZip", edt_zip.getText().toString().trim());
                i.putExtra("data", BD);
                setResult(Activity.RESULT_OK, i);

                finish();
            }

            @Override
            public void onError(String error) {
                if (myLoader != null && myLoader.isMyLoaderShowing())
                    myLoader.dismissLoader();
                Logger.printMessage("error", "" + error);
                closeKeypad();
                CustomAlert customAlert = new CustomAlert(SearchNearProActivity.this, "ERROR", error, SearchNearProActivity.this);
                customAlert.createNormalAlert("ok", 2);
            }

            @Override
            public void onStartFetch() {
                myLoader.showLoader();
            }
        }, key);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent i = new Intent();
        Bundle BD = new Bundle();
        BD.putString("searchZip", "");
        i.putExtra("data", BD);
        setResult(Activity.RESULT_OK, i);

        finish();
    }
}
