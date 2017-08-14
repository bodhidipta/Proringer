package com.android.llc.proringer.fragments.bottomNav;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.android.llc.proringer.R;
import com.android.llc.proringer.activities.LandScreenActivity;
import com.android.llc.proringer.appconstant.ProApplication;
import com.android.llc.proringer.appconstant.ProConstant;
import com.android.llc.proringer.helper.CustomAlert;
import com.android.llc.proringer.helper.MyCustomAlertListener;
import com.android.llc.proringer.helper.MyLoader;
import com.android.llc.proringer.helper.ProServiceApiHelper;
import com.android.llc.proringer.utils.ImageTakerActivityCamera;
import com.android.llc.proringer.utils.Logger;
import com.android.llc.proringer.utils.PermissionController;
import com.android.llc.proringer.viewsmod.BottomNav;
import com.android.llc.proringer.viewsmod.NavigationHandler;
import com.android.llc.proringer.viewsmod.textview.ProLightTextView;
import com.android.llc.proringer.viewsmod.textview.ProRegularTextView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

import static android.app.Activity.RESULT_OK;

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

public class DashBoardFragment extends Fragment implements MyCustomAlertListener {
    //Dialog dialog;
    private static final int REQUEST_IMAGE_CAPTURE = 5;
    private static final int PICK_IMAGE = 3;
    private String mCurrentPhotoPath = "";

    ImageView profile_pic;
    ProRegularTextView tv_name, tv_active_projects, tv_favorite_pros;
    ProLightTextView tv_address;
    MyLoader myLoader=null;

    LinearLayout LLNetworkDisconnection;
    NestedScrollView nested_scroll_main;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.content_land_screen, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        LLNetworkDisconnection = (LinearLayout) view.findViewById(R.id.LLNetworkDisconnection);
        nested_scroll_main = (NestedScrollView) view.findViewById(R.id.nested_scroll_main);

        profile_pic = (ImageView) view.findViewById(R.id.profile_pic);
        tv_name = (ProRegularTextView) view.findViewById(R.id.tv_name);
        tv_active_projects = (ProRegularTextView) view.findViewById(R.id.tv_active_projects);
        tv_favorite_pros = (ProRegularTextView) view.findViewById(R.id.tv_favorite_pros);
        tv_address = (ProLightTextView) view.findViewById(R.id.tv_address);

        myLoader=new MyLoader(getActivity());

        view.findViewById(R.id.post_project).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((LandScreenActivity) getActivity()).performPostProject();
            }
        });

        plotUserInformation();

        view.findViewById(R.id.userInformation).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((LandScreenActivity) getActivity()).bottomNavInstance.highLightSelected(BottomNav.CREATE_PROJECT);  // Well CREATE_PROJECT will reset bottom nav bar to nothing selected !!!
                NavigationHandler.getInstance().highlightTag(NavigationHandler.USER_INFORMATION);

            }
        });
        view.findViewById(R.id.login_settings).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((LandScreenActivity) getActivity()).bottomNavInstance.highLightSelected(BottomNav.CREATE_PROJECT);
                NavigationHandler.getInstance().highlightTag(NavigationHandler.LOGIN_SETTINGS);
            }
        });
        view.findViewById(R.id.notification).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((LandScreenActivity) getActivity()).bottomNavInstance.highLightSelected(BottomNav.CREATE_PROJECT);
                NavigationHandler.getInstance().highlightTag(NavigationHandler.NOTIFICATION);
            }
        });
        view.findViewById(R.id.home_schedule).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((LandScreenActivity) getActivity()).bottomNavInstance.highLightSelected(BottomNav.CREATE_PROJECT);
                NavigationHandler.getInstance().highlightTag(NavigationHandler.HOME_SCHEDUL);
            }
        });
        view.findViewById(R.id.invite_a_friend).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((LandScreenActivity) getActivity()).bottomNavInstance.highLightSelected(BottomNav.CREATE_PROJECT);
                NavigationHandler.getInstance().highlightTag(NavigationHandler.INVITE_FRIEND);
            }
        });
        view.findViewById(R.id.img_upload).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getActivity(), PermissionController.class);
                intent.setAction(PermissionController.ACTION_READ_STORAGE_PERMISSION);
                startActivityForResult(intent, 200);
                //showUploadImage();
            }
        });
    }

    private void plotUserInformation() {

        nested_scroll_main.setVisibility(View.VISIBLE);
        LLNetworkDisconnection.setVisibility(View.GONE);

        ProServiceApiHelper.getInstance(getActivity()).getDashBoardDetails(new ProServiceApiHelper.getApiProcessCallback() {
            @Override
            public void onStart() {
                myLoader.showLoader();
            }

            @Override
            public void onComplete(String message) {
                try {
                    JSONObject jsonObject = new JSONObject(message);
                    JSONArray jsonInfoArray = jsonObject.getJSONArray("info_array");

                    if (!jsonInfoArray.getJSONObject(0).getString("profile_img").equals(""))
                        Glide.with(getActivity()).load(jsonInfoArray.getJSONObject(0).getString("profile_img")).centerCrop().into(profile_pic);

                    tv_name.setText(jsonInfoArray.getJSONObject(0).getString("user_name"));

                    tv_address.setText(jsonInfoArray.getJSONObject(0).getString("city")
                            + "," + jsonInfoArray.getJSONObject(0).getString("state_code")
                            + " " + jsonInfoArray.getJSONObject(0).getString("zipcode"));

                    tv_active_projects.setText(jsonInfoArray.getJSONObject(0).getString("total_active_project"));
                    tv_favorite_pros.setText(jsonInfoArray.getJSONObject(0).getString("total_fav_pro"));

                    getUpdateUserData();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(String error) {
                if (myLoader != null && myLoader.isMyLoaderShowing())
                    myLoader.dismissLoader();


                if (error.equalsIgnoreCase("No internet connection found. Please check your internet connection.")) {
                    nested_scroll_main.setVisibility(View.GONE);
                    LLNetworkDisconnection.setVisibility(View.VISIBLE);
                }


                CustomAlert customAlert = new CustomAlert(getActivity(), "Load Error", "" + error,DashBoardFragment.this);
                customAlert.getListenerRetryCancelFromNormalAlert();
            }
        });
    }

    private void getUpdateUserData() {

        ProServiceApiHelper.getInstance(getActivity()).getUserInformation(new ProServiceApiHelper.getApiProcessCallback() {
            @Override
            public void onStart() {
            }

            @Override
            public void onComplete(String message) {
                if (myLoader != null && myLoader.isMyLoaderShowing())
                    myLoader.dismissLoader();
            }

            @Override
            public void onError(String error) {
                if (myLoader != null && myLoader.isMyLoaderShowing())
                    myLoader.dismissLoader();
            }
        });
    }

    public void onActivityResult(final int requestCode, int resultCode, final Intent data) {
        try {
            Logger.printMessage("resultCode", "requestCode " + requestCode + " &b resultcode :: " + resultCode);
            if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (data != null) {
                            mCurrentPhotoPath = data.getExtras().get("data").toString();
                            Logger.printMessage("image****", "" + mCurrentPhotoPath);

                            loadProfileImage();

//                            Glide.with(getActivity()).load("file://" + mCurrentPhotoPath).into(new GlideDrawableImageViewTarget((ImageView) dialog.findViewById(R.id.img_temp)) {
//                                @Override
//                                public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> animation) {
//                                    super.onResourceReady(resource, animation);
//                                }
//                            });
                           // dialog.findViewById(R.id.img_select).setVisibility(View.GONE);
                          //  dialog.findViewById(R.id.img_cancel).setVisibility(View.VISIBLE);
                        }
                    }
                }, 800);
            } else if (requestCode == PICK_IMAGE && resultCode == RESULT_OK) {
                Logger.printMessage("image****", "" + data.getData());
                try {
                    Uri uri = data.getData();
                    File dataFile = new File(getRealPathFromURI(uri));
                    if (!dataFile.exists())
                        Logger.printMessage("image****", "data file does not exists");
                    mCurrentPhotoPath = dataFile.getAbsolutePath();

                    loadProfileImage();

//                    Glide.with(getActivity()).load(uri).fitCenter().into(new GlideDrawableImageViewTarget((ImageView) dialog.findViewById(R.id.img_temp)) {
//                        /**
//                         * {@inheritDoc}
//                         * If no {@link GlideAnimation} is given or if the animation does not set the
//                         * {@link Drawable} on the view, the drawable is set using
//                         * {@link ImageView#setImageDrawable(Drawable)}.
//                         *
//                         * @param resource  {@inheritDoc}
//                         * @param animation {@inheritDoc}
//                         */
//                        @Override
//                        public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> animation) {
//                            super.onResourceReady(resource, animation);
//                        }
//                    });
                   // dialog.findViewById(R.id.img_select).setVisibility(View.GONE);
                   // dialog.findViewById(R.id.img_cancel).setVisibility(View.VISIBLE);

                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else if (requestCode == 200 && resultCode == RESULT_OK) {
                showImagePickerOption();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public String getRealPathFromURI(Uri contentURI) {
        Cursor cursor = (getActivity()).getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) {
            // Source is Dropbox or other similar local file path
            return contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            return cursor.getString(idx);
        }
    }

    private void showImagePickerOption() {
        new AlertDialog.Builder(getActivity())
                .setCancelable(true)
                .setTitle("Property image")
                .setMessage("please choose image source type.")
                .setPositiveButton("Gallery", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        Intent intent = new Intent(Intent.ACTION_PICK);
                        if (intent.resolveActivity((getActivity()).getPackageManager()) != null) {
                            intent.setType("image/*");
                            startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);

                        }
                    }
                })
                .setNegativeButton("Camera", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        ProConstant.cameraRequested = true;
                        startActivityForResult(new Intent(getActivity(), ImageTakerActivityCamera.class), REQUEST_IMAGE_CAPTURE);
                    }
                })
                .create()
                .show();
    }

//    private void showUploadImage() {
//
//        dialog = new Dialog(getActivity());
//        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        // Include dialog.xml file
//        dialog.setContentView(R.layout.dialog_upload_image);
//        // Set dialog title
//        //dialog.setTitle("Profile Image Upload");
//
//        final DisplayMetrics displayMetrics = new DisplayMetrics();
//        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
//        int widthLcl = (int) (displayMetrics.widthPixels * 0.8f);
//        int heightLcl = (int) (displayMetrics.heightPixels * 0.6f);
//
//        RelativeLayout RLMain = (RelativeLayout) dialog.findViewById(R.id.RLMain);
//
//        RLMain.getLayoutParams().width = widthLcl;
//        RLMain.getLayoutParams().height = heightLcl;
//
//        dialog.show();
//
//        dialog.findViewById(R.id.tv_ok).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//
//            }
//        });
//        dialog.findViewById(R.id.tv_cancel).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                dialog.dismiss();
//            }
//        });
//
//        dialog.findViewById(R.id.img_select).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                Intent intent = new Intent(getActivity(), PermissionController.class);
//                intent.setAction(PermissionController.ACTION_READ_STORAGE_PERMISSION);
//                startActivityForResult(intent, 200);
//            }
//        });
//        dialog.findViewById(R.id.img_cancel).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                ((ImageView) dialog.findViewById(R.id.img_temp)).setImageResource(android.R.color.transparent);
//                dialog.findViewById(R.id.img_select).setVisibility(View.VISIBLE);
//                dialog.findViewById(R.id.img_cancel).setVisibility(View.GONE);
//                mCurrentPhotoPath = "";
//            }
//        });
//    }

    public void loadProfileImage(){
        if (mCurrentPhotoPath.trim().equals("")) {
            Toast.makeText(getActivity(), "Please choose Image", Toast.LENGTH_SHORT).show();
        } else {
            ProServiceApiHelper.getInstance(getActivity()).upLoadProfileImage(new ProServiceApiHelper.getApiProcessCallback() {
                @Override
                public void onStart() {
                    myLoader.isMyLoaderShowing();
                }

                @Override
                public void onComplete(String message) {
                    if (myLoader != null && myLoader.isMyLoaderShowing())
                        myLoader.dismissLoader();

                    //dialog.dismiss();

                    CustomAlert customAlert = new CustomAlert(getActivity(), "" + "Upload Image", "" + message, DashBoardFragment.this);
                    customAlert.createNormalAlert();
                }

                @Override
                public void onError(String error) {
                    if (myLoader != null && myLoader.isMyLoaderShowing())
                        myLoader.dismissLoader();
                    new AlertDialog.Builder(getActivity())
                            .setTitle("" + "Upload Image")
                            .setMessage("" + error)
                            .setCancelable(false)
                            .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();

                                }
                            })
                            .show();
                }
            }, ProApplication.getInstance().getUserId(), mCurrentPhotoPath);
        }
    }

    @Override
    public void callbackForAlert(String result) {
        if (result.equalsIgnoreCase("retry")) {
            plotUserInformation();
        }
        if (result.equalsIgnoreCase("ok")){
            plotUserInformation();
        }
    }
}
