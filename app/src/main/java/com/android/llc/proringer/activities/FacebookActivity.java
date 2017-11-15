package com.android.llc.proringer.activities;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;

import com.android.llc.proringer.R;
import com.android.llc.proringer.helper.CustomAlert;
import com.android.llc.proringer.helper.MyCustomAlertListener;
import com.android.llc.proringer.helper.MyLoader;
import com.android.llc.proringer.helper.ProServiceApiHelper;
import com.android.llc.proringer.utils.Logger;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import org.json.JSONException;
import org.json.JSONObject;
import java.security.MessageDigest;
import java.util.Arrays;

public class FacebookActivity extends AppCompatActivity implements MyCustomAlertListener {
    public static final int FacebookResponse = 11;

    CallbackManager callbackManager;
    private MyLoader myLoader = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.android.llc.proringer",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Logger.printMessage("## KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        setContentView(R.layout.activity_facebook);

        myLoader=new MyLoader(FacebookActivity.this);

        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile", "email", "user_friends"));

        callbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

                Logger.printMessage("@@ FB TOKEN", "" + loginResult.getAccessToken());

                GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(
                                    JSONObject object,
                                    GraphResponse response) {

//                                if(response.getJSONObject())


                                Logger.printMessage("@@ FB DETAILS-->",""+object.toString());
                                Logger.printMessage("@@ FB DETAILS_GRAPH-->",""+ response.getJSONObject());


                                LoginWithWanNyaan(response.getJSONObject());
                                // Application code
                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,first_name,last_name,picture.type(large),email,name,gender,birthday,friendlists,age_range,friends");
                request.setParameters(parameters);
                request.executeAsync();
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {
                Logger.printMessage("@@ FB ERROR", "" + error.toString());
            }
        });
    }

    private void LoginWithWanNyaan(JSONObject jsonObject) {
        try {

            ProServiceApiHelper.getInstance(FacebookActivity.this).getUserLoggedInFacebook(
                    new ProServiceApiHelper.getApiProcessCallback() {
                        @Override
                        public void onStart() {
                            myLoader.showLoader();
                        }

                        @Override
                        public void onComplete(String message) {
                            if (myLoader != null && myLoader.isMyLoaderShowing())
                                myLoader.dismissLoader();

                            Intent intent = new Intent();
                            setResult(RESULT_OK, intent);
                            finish();

                        }

                        @Override
                        public void onError(String error) {
                            if (myLoader != null && myLoader.isMyLoaderShowing())
                                myLoader.dismissLoader();

                            CustomAlert customAlert = new CustomAlert(FacebookActivity.this, "Sign in error", "" + error, FacebookActivity.this);
                            customAlert.createNormalAlert("ok",1);
                        }
                    },
                    jsonObject.getString("first_name"),
                    jsonObject.getString("last_name"),
                    jsonObject.getString("email"),
                    jsonObject.getString("id"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void callbackForAlert(String result, int i) {
        if (result.equalsIgnoreCase("ok")&&i==1) {
            Intent intent = new Intent();
            setResult(RESULT_CANCELED, intent);
            finish();
        }
    }
}
