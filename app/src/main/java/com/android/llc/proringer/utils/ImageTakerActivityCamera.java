package com.android.llc.proringer.utils;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.android.llc.proringer.BuildConfig;
import com.android.llc.proringer.R;
import com.android.llc.proringer.appconstant.ProConstant;
import com.bumptech.glide.Glide;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by su on 2/10/16.
 */
public class ImageTakerActivityCamera extends AppCompatActivity {


    private File finalFile = null;
    private ImageView profileimage;
    private String mCurrentPhotoPath;
    private final String BITMAP_STORAGE_URL = "IMAGE_URL";
    private final int ACTION_TAKE_GALLERY = 1000;
    private static boolean SELECTED = false;
    public static final String ACTION_SELECT_CAMERA = "take_camera";
    private final int REQUEST_IMAGE_CAPTURE = 2000;


    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    //SimpleDraweeView draweeView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_taker_activity_camera);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Image Preview");
        profileimage = (ImageView) findViewById(R.id.main_prof_image);

        findViewById(R.id.bttn_ok).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                SELECTED = true;
                onBackPressed();
            }
        });

        findViewById(R.id.bttn_retake).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {

                    try {
                        finalFile = createImageFile();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                        // Error occurred while creating the File
                    }
                    // Continue only if the File was successfully created
                    if (finalFile != null) {
                        Uri photoURI = null;
                        photoURI = FileProvider.getUriForFile(ImageTakerActivityCamera.this,
                                BuildConfig.APPLICATION_ID + ".provider",
                                finalFile);
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                        startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                        ProConstant.cameraRequested = false;
                    }
                } else {
                    onBackPressed();
                }
            }
        });


        if (ProConstant.cameraRequested) {
            findViewById(R.id.tutorial).setVisibility(View.GONE);
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (takePictureIntent.resolveActivity(getPackageManager()) != null) {

                try {
                    finalFile = createImageFile();
                } catch (IOException ex) {
                    ex.printStackTrace();
                    // Error occurred while creating the File
                }
                // Continue only if the File was successfully created
                if (finalFile != null) {
                    Uri photoURI = null;
                    photoURI = FileProvider.getUriForFile(ImageTakerActivityCamera.this,
                            BuildConfig.APPLICATION_ID + ".provider",
                            finalFile);
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                    ProConstant.cameraRequested = false;
                }
            } else {
                onBackPressed();
            }
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        Log.i("image Taker", "requestCode : " + requestCode + " resultCode " + resultCode);

        if (REQUEST_IMAGE_CAPTURE == requestCode && resultCode == RESULT_OK) {
            if (finalFile != null) {
                findViewById(R.id.tutorial).setVisibility(View.GONE);
                Glide.with(getApplicationContext()).load(finalFile).fitCenter().into(profileimage);
            }
        } else if (resultCode == RESULT_CANCELED) {
            onBackPressed();
        }

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (finalFile != null) {
            outState.putString(BITMAP_STORAGE_URL, finalFile.getAbsolutePath());
            Log.i("image Taker", "onSaveInstanceState : " + finalFile.getAbsolutePath());
        }
    }


    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (finalFile == null) {
            findViewById(R.id.tutorial).setVisibility(View.GONE);
            finalFile = new File(savedInstanceState.getString(BITMAP_STORAGE_URL));
            Glide.with(getApplicationContext()).load(finalFile).fitCenter().centerCrop().into(profileimage);
        }
        Log.i("image Taker", "onRestoreInstanceState : " + finalFile.getAbsolutePath().toString());
    }


    @Override
    public void onBackPressed() {
        if (finalFile != null) {
            Log.i("image Taker", "selected : " + SELECTED);

            if (SELECTED) {
                Intent i = new Intent();
                if (finalFile.exists()) {
                    i.putExtra("data", finalFile.getAbsolutePath());
                    setResult(RESULT_OK, i);
                } else {
                    i.putExtra("data", "");
                    setResult(RESULT_CANCELED, i);
                }
            } else {
                Intent i = new Intent();
                i.putExtra("data", "");
                setResult(RESULT_CANCELED, i);

            }
        }
        super.onBackPressed();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }


    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }


}
