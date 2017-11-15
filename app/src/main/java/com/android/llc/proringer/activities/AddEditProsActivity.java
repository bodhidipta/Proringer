package com.android.llc.proringer.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.android.llc.proringer.R;
import com.android.llc.proringer.appconstant.ProApplication;
import com.android.llc.proringer.fragments.editProsProject.EditImageSelectFragment;
import com.android.llc.proringer.fragments.editProsProject.EditProsDetailsFragment;
import com.android.llc.proringer.fragments.editProsProject.EditSearchLocationFragment;
import com.android.llc.proringer.helper.CustomAlert;
import com.android.llc.proringer.helper.MyCustomAlertListener;
import com.android.llc.proringer.helper.MyLoader;
import com.android.llc.proringer.helper.ProServiceApiHelper;
import com.android.llc.proringer.pojo.AddressData;
import com.android.llc.proringer.pojo.Appsdata;
import com.android.llc.proringer.utils.Logger;
import com.android.llc.proringer.viewsmod.textview.ProRegularTextView;

import java.util.ArrayList;

public class AddEditProsActivity extends AppCompatActivity implements MyCustomAlertListener {


    FragmentManager fragmentManager = null;
    ProRegularTextView tv_toolbar;
    ImageView img_back, img_home;
    public String mCurrentPhotoPath = "";
    public String project_description_text = "";
    ArrayList<String> fragmentPushList;
    private InputMethodManager keyboard;
    public ProRegularTextView selected_service_category, selected_text;
    public AddressData selectedAddressData = null;
    ProgressBar progress_posting;
    private int progressStep = 6;
    public boolean iseditPostProject = false;
    private MyLoader myLoader = null;
    String Projectid = Appsdata.projectid;
    String project_id, project_zipcode, city, state, country, latitude, longitude;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addphoto);
        fragmentManager = getSupportFragmentManager();
        tv_toolbar = (ProRegularTextView) findViewById(R.id.tv_toolbar);
        fragmentPushList = new ArrayList<>();
        Log.d("address", String.valueOf(selectedAddressData));
        img_back = (ImageView) findViewById(R.id.img_back);
        img_home = (ImageView) findViewById(R.id.img_home);
        progress_posting = (ProgressBar) findViewById(R.id.progress_posting);
        selected_service_category = (ProRegularTextView) findViewById(R.id.selected_service_category);
        selected_text = (ProRegularTextView) findViewById(R.id.selected_text);
        selected_service_category.setText(Appsdata.servic);
        myLoader = new MyLoader(AddEditProsActivity.this);

        project_id = Appsdata.projectid;
        project_zipcode = Appsdata.projectzip;
        city = Appsdata.city;
        state = Appsdata.state;
        country = Appsdata.county;
        latitude = Appsdata.latlong;
        longitude = Appsdata.longtitude;
        String projectname = getIntent().getExtras().getString("Project");
        tv_toolbar.setText(projectname);
        Log.d("project", projectname);
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        img_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        keyboard = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);


        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.fragment_container, new EditImageSelectFragment(), "" + EditImageSelectFragment.class.getCanonicalName());
        transaction.addToBackStack("" + EditImageSelectFragment.class.getCanonicalName());
        transaction.commit();
        Logger.printMessage("Tag_frg", "" + fragmentManager.getBackStackEntryCount());

        fragmentPushList.add(EditImageSelectFragment.class.getCanonicalName());
        Logger.printMessage("fragmentPushList", "" + fragmentPushList.size());

    }

    public void changeFragmentNext(int next) {
        closeKeypad();
        switch (next) {
            case 1:
                FragmentTransaction transaction2 = fragmentManager.beginTransaction();
                transaction2.add(R.id.fragment_container, new EditProsDetailsFragment(), "" + EditProsDetailsFragment.class.getCanonicalName());
                transaction2.addToBackStack("" + EditProsDetailsFragment.class.getCanonicalName());
                transaction2.commit();
                Logger.printMessage("Tag_frg", "" + getSupportFragmentManager().getBackStackEntryCount());
                fragmentPushList.add(EditProsDetailsFragment.class.getCanonicalName());
                Logger.printMessage("fragmentPushList", "" + fragmentPushList.size());

                break;


            case 2:

                FragmentTransaction transaction3 = fragmentManager.beginTransaction();
                transaction3.add(R.id.fragment_container, new EditSearchLocationFragment(), "" + EditSearchLocationFragment.class.getCanonicalName());
                transaction3.addToBackStack("" + EditSearchLocationFragment.class.getCanonicalName());
                transaction3.commit();
                Logger.printMessage("Tag_frg", "" + getSupportFragmentManager().getBackStackEntryCount());

                fragmentPushList.add(EditSearchLocationFragment.class.getCanonicalName());
                Logger.printMessage("fragmentPushList", "" + fragmentPushList.size());

                break;

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    public boolean completeEditProject() {
        Log.d("project_id", project_id);
        Log.d("user_id", Appsdata.Uid);
        Log.d("project_zipcode", project_zipcode);
        Log.d("city", Appsdata.city);
        Log.d("state", Appsdata.state);
        Log.d("country", country);
        Log.d("latitude", latitude);
        Log.d("longitude", longitude);
        Log.d("Description", project_description_text);
        String projectdescription = project_description_text;
        Logger.printMessage("@registrationPostPro", "mCurrentPhotoPath :" + mCurrentPhotoPath);
        ProServiceApiHelper.getInstance(AddEditProsActivity.this).editproject(
                new ProServiceApiHelper.getApiProcessCallback() {


                    @Override
                    public void onStart() {
                        myLoader.showLoader();
                    }

                    @Override
                    public void onComplete(String message) {
                        if (myLoader != null && myLoader.isMyLoaderShowing())
                            myLoader.dismissLoader();

                        CustomAlert customAlert = new CustomAlert(AddEditProsActivity.this, "", "" + message, AddEditProsActivity.this);
                        customAlert.createNormalAlert("ok", 1);

                        iseditPostProject = true;
                    }

                    @Override
                    public void onError(String error) {
                        if (myLoader != null && myLoader.isMyLoaderShowing())
                            myLoader.dismissLoader();

                        iseditPostProject = true;

                        CustomAlert customAlert = new CustomAlert(AddEditProsActivity.this, "Project post error", "" + error, AddEditProsActivity.this);
                        customAlert.getListenerRetryCancelFromNormalAlert("retry", "abort", 1);

                    }


                }, project_id,
                project_zipcode,
                Appsdata.city,
                Appsdata.state,
                country,
                latitude,
                longitude,
                projectdescription,
                mCurrentPhotoPath
        );
        return iseditPostProject;
    }

    public void increaseStep() {
        Logger.printMessage("increaseStep", "increaseStep");
        progressStep++;
        progress_posting.setProgress(progressStep);

        if (progressStep == 7) {
            progress_posting.setProgress(8);

        } else if (progressStep == 8) {
            progress_posting.setProgress(9);

        }
    }

    public void closeKeypad() {
        try {
            keyboard.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void callbackForAlert(String result, int i) {
        if (result.equalsIgnoreCase("retry") && i == 1) {
            completeEditProject();
        } else if (result.equalsIgnoreCase("retry") && i == 2) {
            changeFragmentNext(1);
        } else if (result.equalsIgnoreCase("ok") && i == 1) {
            Intent intent = new Intent(AddEditProsActivity.this, LandScreenActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            ProApplication.getInstance().go_to="myProject";
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        closeKeypad();
        progressStep--;
        Log.d("size list", "" + fragmentPushList.size());
        if (fragmentPushList.size() >= 1) {
            if (fragmentPushList.size()==3) {
                fragmentPushList.remove(fragmentPushList.size() - 1);
                progress_posting.setProgress(progressStep);
            } else if (fragmentPushList.size()==2) {
                fragmentPushList.remove(fragmentPushList.size() - 1);
                progress_posting.setProgress(progressStep);
            } else if(fragmentPushList.size()==1){
                fragmentPushList.remove(fragmentPushList.size() - 1);
                progress_posting.setProgress(progressStep);
                finish();
            }
        }
    }
}
