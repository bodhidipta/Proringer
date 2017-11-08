package com.android.llc.proringer.activities;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.llc.proringer.R;
import com.android.llc.proringer.fragments.editPros.EditImageSelectFragment;
import com.android.llc.proringer.fragments.main_content.MyProjectDetailsFragment;
import com.android.llc.proringer.fragments.editPros.EditProsDetailsFragment;
import com.android.llc.proringer.fragments.editPros.EditSearchLocationFragment;
import com.android.llc.proringer.fragments.postProject.PostProjectRegistrationAndFinalizeFragment;
import com.android.llc.proringer.fragments.postProject.PostProjectSelectImageFragment;
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


    FragmentManager fragmentManager=null;
    ProRegularTextView tv_toolbar;
    ImageView img_back,img_home;
    public String mCurrentPhotoPath = "";
    public String project_description_text = "";
    ArrayList<String> fragmentPushList;
    private InputMethodManager keyboard;
    public ProRegularTextView  selected_service_category,selected_text;
    public AddressData selectedAddressData = null;
    ProgressBar progress_posting;
    private int progressStep = 5;
    private int step = 0;
    private String selectedId = "", step1Option = "", serviceId = "",
            step2option = "What type of work best describe this project?",
            step3option = "What type of property will this be for?",
            step4option = "What is the current status for this project?",
            step5option = "When would you like to start?",
            step6option = "Add a photo(Optional)",
            step7option = "Add details about the project",
            step8option = "Where is the project located",
            step9option = "Lets get acquainted",
            step10option = "WE SENT YOUR PROJECT TO THE PROS!";
    public boolean iseditPostProject=false;
    private MyLoader myLoader=null;
    String Projectid=Appsdata.projectid;
    String project_id,project_zipcode,city,state,country,latitude,longitude;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addphoto);
        fragmentManager = getSupportFragmentManager();
        tv_toolbar=(ProRegularTextView)findViewById(R.id.tv_toolbar);
        fragmentPushList = new ArrayList<>();
        Log.d("address", String.valueOf(selectedAddressData));
        img_back=(ImageView)findViewById(R.id.img_back);
        img_home=(ImageView)findViewById( R.id.img_home);
        progress_posting=(ProgressBar)findViewById(R.id.progress_posting);
        selected_service_category=(ProRegularTextView)findViewById(R.id.selected_service_category);
        selected_text=(ProRegularTextView)findViewById(R.id.selected_text);
        selected_service_category.setText(Appsdata.servic);
        myLoader=new MyLoader(AddEditProsActivity.this);

        project_id=Appsdata.projectid;
        project_zipcode=Appsdata.projectzip;
        city=Appsdata.city;
        state=Appsdata.state;
        country=Appsdata.county;
        latitude=Appsdata.latlong;
        longitude=Appsdata.longtitude;
        String projectname= getIntent().getExtras().getString("Project");
        tv_toolbar.setText(projectname);
        Log.d("project",projectname);
        img_back. setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        img_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i= new Intent(AddEditProsActivity.this,LandScreenActivity.class);
                startActivity(i);
                finish();
            }
        });
        onchange3();
        keyboard = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);

    }

    public void onchange3()
    {
//        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//        EditImageSelectFragment hello = new EditImageSelectFragment();
//        fragmentTransaction.replace(R.id.fragment_container, hello);
//        fragmentTransaction.commit();
        FragmentTransaction transaction3 = fragmentManager.beginTransaction();
        transaction3.replace(R.id.fragment_container, new EditImageSelectFragment(), "" + EditImageSelectFragment.class.getCanonicalName());
        transaction3.addToBackStack("" + EditImageSelectFragment.class.getCanonicalName());
        transaction3.commit();
        Logger.printMessage("Tag_frg", "" + getSupportFragmentManager().getBackStackEntryCount());

        fragmentPushList.add(PostProjectSelectImageFragment.class.getCanonicalName());
        Logger.printMessage("fragmentPushList", "" + fragmentPushList.size());

//        linear_progrebarr_text.setVisibility(View.VISIBLE);
//        progress_posting.setVisibility(View.VISIBLE);
//        selected_service_category.setText(Appsdata.servic);
    }
    public void changeFragmentNext(int next) {

        switch (next) {

            case 1:
                FragmentTransaction transaction4 = fragmentManager.beginTransaction();
                transaction4.replace(R.id.fragment_container, new EditProsDetailsFragment(), "" + EditProsDetailsFragment.class.getCanonicalName());
                transaction4.addToBackStack("" + EditProsDetailsFragment.class.getCanonicalName());
                transaction4.commit();
                Logger.printMessage("Tag_frg", "" + getSupportFragmentManager().getBackStackEntryCount());


                fragmentPushList.add(EditProsDetailsFragment.class.getCanonicalName());
                Logger.printMessage("fragmentPushList", "" + fragmentPushList.size());

                break;


            case 2:

                FragmentTransaction transaction5 = fragmentManager.beginTransaction();
                transaction5.replace(R.id.fragment_container, new EditSearchLocationFragment(), "" + EditSearchLocationFragment.class.getCanonicalName());
                transaction5.addToBackStack("" + EditSearchLocationFragment.class.getCanonicalName());
                transaction5.commit();
                Logger.printMessage("Tag_frg", "" + getSupportFragmentManager().getBackStackEntryCount());

                fragmentPushList.add(EditSearchLocationFragment.class.getCanonicalName());
                Logger.printMessage("fragmentPushList", "" + fragmentPushList.size());

                break;

            case 3:

                FragmentTransaction transaction6 = fragmentManager.beginTransaction();
                transaction6.replace(R.id.fragment_container, new MyProjectDetailsFragment(), "" + MyProjectDetailsFragment.class.getCanonicalName());
                transaction6.addToBackStack("" + MyProjectDetailsFragment.class.getCanonicalName());
                transaction6.commit();
                Logger.printMessage("Tag_frg", "" + getSupportFragmentManager().getBackStackEntryCount());


                fragmentPushList.add(PostProjectRegistrationAndFinalizeFragment.class.getCanonicalName());
                Logger.printMessage("fragmentPushList", "" + fragmentPushList.size());

                break;
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
    public boolean completeeditproject() {
        Log.d("project_id", project_id);
        Log.d("user_id", Appsdata.Uid);
        Log.d("project_zipcode", project_zipcode);
        Log.d("city", Appsdata.city);
        Log.d("state", Appsdata.state);
        Log.d("country",country);
        Log.d("latitude", latitude);
        Log.d("longitude", longitude);
        Log.d("Description",project_description_text );
        String projectdescription=project_description_text;
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

                Toast.makeText(AddEditProsActivity.this, ""+message, Toast.LENGTH_SHORT).show();

                Intent i=new Intent( AddEditProsActivity.this,LandScreenActivity.class);
                startActivity(i);
                iseditPostProject=true;
            }

            @Override
            public void onError(String error) {
                if (myLoader != null && myLoader.isMyLoaderShowing())
                    myLoader.dismissLoader();

                iseditPostProject=true;

                CustomAlert customAlert = new CustomAlert(AddEditProsActivity.this, "Project post error", "" + error, AddEditProsActivity.this);
                customAlert.getListenerRetryCancelFromNormalAlert("retry","abort",1);

            }


        },project_id,
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

        if (progressStep == 5) {
            progress_posting.setProgress(6);

        } else if (progressStep == 6) {
            progress_posting.setProgress(7);

        } else if (progressStep == 7) {
            progress_posting.setProgress(9);

        }
        else {

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
        if (result.equalsIgnoreCase("retry")&&i==1){
            completeeditproject();
        }
        else if (result.equalsIgnoreCase("retry")&&i==2){
            changeFragmentNext(1);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Log.d("size list",""+fragmentPushList.size());
        if(fragmentPushList.size()==2)
        {
            FragmentTransaction transaction3 = fragmentManager.beginTransaction();
            transaction3.replace(R.id.fragment_container, new EditImageSelectFragment(), "" + EditImageSelectFragment.class.getCanonicalName());
            transaction3.addToBackStack("" + EditImageSelectFragment.class.getCanonicalName());
            transaction3.commit();
            fragmentPushList.remove(fragmentPushList.size()-1);
            progress_posting.setProgress(6);

        }
        else if(fragmentPushList.size()==3)
        {
            FragmentTransaction transaction4 = fragmentManager.beginTransaction();
            transaction4.replace(R.id.fragment_container, new EditProsDetailsFragment(), "" + EditProsDetailsFragment.class.getCanonicalName());
            transaction4.addToBackStack("" + EditProsDetailsFragment.class.getCanonicalName());
            transaction4.commit();
            fragmentPushList.remove(fragmentPushList.size()-1);
            progress_posting.setProgress(8);

        }
        else if(fragmentPushList.size()==1)
        {
            finish();
        }
    }
}
