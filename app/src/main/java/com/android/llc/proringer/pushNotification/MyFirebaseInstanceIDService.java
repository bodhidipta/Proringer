package com.android.llc.proringer.pushNotification;

import android.util.Log;
import com.android.llc.proringer.appconstant.ProApplication;
import com.android.llc.proringer.helper.ProServiceApiHelper;
import com.android.llc.proringer.utils.Logger;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by apple on 23/05/17.
 */


public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {

    private static final String TAG = "MyFirebaseIIDService";

    /**
     * Called if InstanceID token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is called when the InstanceID token
     * is initially generated so this is where you would retrieve the token.
     */
    // [START refresh_token]
    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d("@@ ", "Refreshed token: " + refreshedToken);

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        sendRegistrationToServer(refreshedToken);
    }
    // [END refresh_token]

    private void sendRegistrationToServer(String token) {

        ProServiceApiHelper.getInstance(getApplication()).setUserDeviceAPI(new ProServiceApiHelper.getApiProcessCallback() {
            @Override
            public void onStart() {
                Logger.printMessage("start", "start");
            }

            @Override
            public void onComplete(String message) {
                Logger.printMessage("complete", message);
            }

            @Override
            public void onError(String error) {

            }
        },  ProApplication.getInstance().getUserId(), token);

    }
}