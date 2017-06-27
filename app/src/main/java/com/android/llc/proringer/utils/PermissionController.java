package com.android.llc.proringer.utils;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by bodhidipta on 16/04/17.
 */

public class PermissionController extends AppCompatActivity {
    public static final String ACTION_INTERNET_PERMISSION = "internet_permission";
    public static final int INTERNET_PERMISSION = 1;
    public static final String ACTION_CAMERA_PERMISSION = "camera_permission";
    public static final int CAMERA_PERMISSION = 2;
    public static final String ACTION_READ_STORAGE_PERMISSION = "read_storage_permission";
    public static final int STORAGE_PERMISSION = 3;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getIntent().getAction() == null) {
            setResult(RESULT_CANCELED);
            finish();
        } else if (getIntent().getAction().equalsIgnoreCase(ACTION_CAMERA_PERMISSION)) {
            actionCameraPermission();
        } else if (getIntent().getAction().equalsIgnoreCase(ACTION_INTERNET_PERMISSION)) {

        } else if (getIntent().getAction().equalsIgnoreCase(ACTION_READ_STORAGE_PERMISSION)) {
            actionReadStoragePermission();
        }

    }

    private void actionCameraPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
                &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {

            setResult(RESULT_OK);
            finish();

        } else {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)
                    && ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    && ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                    ) {
                new AlertDialog.Builder(this)
                        .setTitle("Permission Required")
                        .setMessage("To take picture and save on the storage the application needs permission. Without permission these features won't work.")
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                ActivityCompat.requestPermissions(PermissionController.this,
                                        new String[]{
                                                Manifest.permission.CAMERA,
                                                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                                Manifest.permission.READ_EXTERNAL_STORAGE
                                        },
                                        CAMERA_PERMISSION);
                            }
                        })
                        .setNegativeButton("Abort", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                setResult(RESULT_CANCELED);
                                finish();
                            }
                        })
                        .setCancelable(false)
                        .show();
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{
                                Manifest.permission.CAMERA,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                Manifest.permission.READ_EXTERNAL_STORAGE
                        },
                        CAMERA_PERMISSION);

            }
        }
    }

    private void actionInternetPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_NETWORK_STATE) == PackageManager.PERMISSION_GRANTED
                &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_WIFI_STATE) == PackageManager.PERMISSION_GRANTED) {
            setResult(RESULT_OK);
            finish();
        } else {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_NETWORK_STATE)
                    && ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_WIFI_STATE)
                    ) {

            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{
                                Manifest.permission.ACCESS_NETWORK_STATE,
                                Manifest.permission.ACCESS_WIFI_STATE
                        },
                        INTERNET_PERMISSION);
            }
        }
    }

    private void actionReadStoragePermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            setResult(RESULT_OK);
            finish();
        } else {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    && ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                    ) {
                new AlertDialog.Builder(this)
                        .setTitle("Permission Required")
                        .setMessage("To reads images  permission. Without permission these features won't work.")
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                ActivityCompat.requestPermissions(PermissionController.this,
                                        new String[]{
                                                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                                Manifest.permission.READ_EXTERNAL_STORAGE
                                        },
                                        STORAGE_PERMISSION);
                            }
                        })
                        .setNegativeButton("Abort", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                setResult(RESULT_CANCELED);
                                finish();
                            }
                        })
                        .setCancelable(false)
                        .show();
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{
                                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                Manifest.permission.READ_EXTERNAL_STORAGE
                        },
                        STORAGE_PERMISSION);

            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        boolean permissionGranted = true;
        if (requestCode == STORAGE_PERMISSION) {
            for (int permission : grantResults) {
                if (permission != PackageManager.PERMISSION_GRANTED) {
                    permissionGranted = false;
                    break;
                }
            }
            if (permissionGranted) {
                setResult(RESULT_OK);
                finish();
            } else {
                setResult(RESULT_CANCELED);
                finish();
            }
        }
    }
}
