package com.android.llc.proringer.fragments.postProject;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.llc.proringer.R;
import com.android.llc.proringer.activities.PostProjectActivity;
import com.android.llc.proringer.cropImagePackage.CropImage;
import com.android.llc.proringer.cropImagePackage.CropImageView;
import com.android.llc.proringer.utils.Logger;
import com.android.llc.proringer.utils.PermissionController;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;

import static android.app.Activity.RESULT_OK;

/**
 * Created by su on 7/13/17.
 */

public class PostProjectSelectImageFragment extends Fragment {
//    private static final int REQUEST_IMAGE_CAPTURE = 5;
//    private static final int PICK_IMAGE = 3;
    private String mCurrentPhotoPath = "";
    private ImageView image_pager;

    static PostProjectActivity postProjectActivityMy;

    public static PostProjectSelectImageFragment newInstance (PostProjectActivity postProjectActivity) {
        PostProjectSelectImageFragment yf = new PostProjectSelectImageFragment();
        postProjectActivityMy=postProjectActivity;
    /* See this code gets executed immediately on your object construction */
        return yf;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.content_post_project_image, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        image_pager = (ImageView) view.findViewById(R.id.image_pager);
        view.findViewById(R.id.add_photo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), PermissionController.class);
                intent.setAction(PermissionController.ACTION_READ_STORAGE_PERMISSION);
                startActivityForResult(intent, 200);
            }
        });

        view.findViewById(R.id.continue_image_section).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /**
                 * fragment calling
                 */
                ((PostProjectActivity) getActivity()).mCurrentPhotoPath = mCurrentPhotoPath;
                ((PostProjectActivity) getActivity()).increaseStep();
                ((PostProjectActivity) getActivity()).changeFragmentNext(4);
            }
        });

    }

    public void onActivityResult(final int requestCode, int resultCode, final Intent data) {
//        try {
//            Logger.printMessage("resultCode", "requestCode " + requestCode + " &b resultcode :: " + resultCode);
//            if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
//                new Handler().postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        if (data != null) {
//                            mCurrentPhotoPath = data.getExtras().get("data").toString();
//                            Logger.printMessage("image****", "" + mCurrentPhotoPath);
//                            Glide.with(getActivity()).load("file://" + mCurrentPhotoPath).into(new GlideDrawableImageViewTarget(image_pager) {
//                                @Override
//                                public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> animation) {
//                                    super.onResourceReady(resource, animation);
//                                }
//                            });
//                        }
//                    }
//                }, 800);
//            } else if (requestCode == PICK_IMAGE && resultCode == RESULT_OK) {
//                Logger.printMessage("image****", "" + data.getData());
//                try {
//                    Uri uri = data.getData();
//                    File dataFile = new File(getRealPathFromURI(uri));
//                    if (!dataFile.exists())
//                        Logger.printMessage("image****", "data file does not exists");
//                    mCurrentPhotoPath = dataFile.getAbsolutePath();
//                    Glide.with((PostProjectActivity) getActivity()).load(uri).fitCenter().into(new GlideDrawableImageViewTarget(image_pager) {
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
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//
//            } else if (requestCode == 200 && resultCode == RESULT_OK) {
//               // showImagePickerOption();
//                startCropImageActivity(null);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

            // handle result of CropImageActivity
            if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
                CropImage.ActivityResult result = CropImage.getActivityResult(data);
                if (resultCode == RESULT_OK) {

                    mCurrentPhotoPath = result.getUri().toString();

                    Glide.with((PostProjectActivity) getActivity()).load(result.getUri()).fitCenter().into(new GlideDrawableImageViewTarget(image_pager) {
                        /**
                         * {@inheritDoc}
                         * If no {@link GlideAnimation} is given or if the animation does not set the
                         * {@link Drawable} on the view, the drawable is set using
                         * {@link ImageView#setImageDrawable(Drawable)}.
                         *
                         * @param resource  {@inheritDoc}
                         * @param animation {@inheritDoc}
                         */
                        @Override
                        public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> animation) {
                            super.onResourceReady(resource, animation);
                        }
                    });

                    Logger.printMessage("path-->", mCurrentPhotoPath);

                    Toast.makeText(getActivity(), "Cropping successful, Sample: " + result.getSampleSize(), Toast.LENGTH_LONG).show();
                } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                    Toast.makeText(getActivity(), "Cropping failed: " + result.getError(), Toast.LENGTH_LONG).show();
                }
            } else if (requestCode == 200 && resultCode == RESULT_OK) {
//               // showImagePickerOption();
                startCropImageActivity(null);
            }
        }

    /**
     * Start crop image activity for the given image.
     */
    private void startCropImageActivity(Uri imageUri) {
        Intent intent = CropImage.activity(imageUri)
                .setGuidelines(CropImageView.Guidelines.ON)
                .setMultiTouchEnabled(false)
                .setAspectRatio(4,3)
                .getIntent(postProjectActivityMy);
        startActivityForResult(intent, CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE);
    }

//    public String getRealPathFromURI(Uri contentURI) {
//        Cursor cursor = ((PostProjectActivity) getActivity()).getContentResolver().query(contentURI, null, null, null, null);
//        if (cursor == null) {
//            // Source is Dropbox or other similar local file path
//            return contentURI.getPath();
//        } else {
//            cursor.moveToFirst();
//            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
//            return cursor.getString(idx);
//        }
//    }

//    private void showImagePickerOption() {
//        new AlertDialog.Builder((PostProjectActivity) getActivity())
//                .setCancelable(true)
//                .setTitle("Property image")
//                .setMessage("please choose image source type.")
//                .setPositiveButton("Gallery", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.dismiss();
//                        Intent intent = new Intent(Intent.ACTION_PICK);
//                        if (intent.resolveActivity(((PostProjectActivity) getActivity()).getPackageManager()) != null) {
//                            intent.setType("image/*");
//                            startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
//
//                        }
//                    }
//                })
//                .setNegativeButton("Camera", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.dismiss();
//                        ProConstant.cameraRequested = true;
//                        startActivityForResult(new Intent(getActivity(), ImageTakerActivityCamera.class), REQUEST_IMAGE_CAPTURE);
//                    }
//                })
//                .create()
//                .show();
//    }

}
