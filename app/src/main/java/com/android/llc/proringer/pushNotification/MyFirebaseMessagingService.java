package com.android.llc.proringer.pushNotification;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.android.llc.proringer.R;
import com.android.llc.proringer.utils.Logger;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

//import com.firebase.jobdispatcher.FirebaseJobDispatcher;
//import com.firebase.jobdispatcher.GooglePlayDriver;
//import com.firebase.jobdispatcher.Job;

/**
 * Created by apple on 23/05/17.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = "MyFirebaseMsgService";

    /**
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */
    // [START receive_message]
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
//        // [START_EXCLUDE]
//        // There are two types of messages data messages and notification messages. Data messages are handled
//        // here in onMessageReceived whether the app is in the foreground or background. Data messages are the type
//        // traditionally used with GCM. Notification messages are only received here in onMessageReceived when the app
//        // is in the foreground. When the app is in the background an automatically generated notification is displayed.
//        // When the user taps on the notification they are returned to the app. Messages containing both notification
//        // and data payloads are treated as notification messages. The Firebase console always sends notification
//        // messages. For more see: https://firebase.google.com/docs/cloud-messaging/concept-options
//        // [END_EXCLUDE]
//
//        // TODO(developer): Handle FCM messages here.
//        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
//        Logger.printMessage(TAG, "From: " + remoteMessage.getFrom());
//
//        // Check if message contains a data payload.
//        if (remoteMessage.getData().size() > 0) {
//            Logger.printMessage(TAG, "Message data payload: " + remoteMessage.getData());
//
//            if (/* Check if data needs to be processed by long running job */ true) {
//                // For long-running tasks (10 seconds or more) use Firebase Job Dispatcher.
//                scheduleJob();
//            } else {
//                // Handle message within 10 seconds
//                handleNow();
//            }
//
//        }
//
//        // Check if message contains a notification payload.
//        if (remoteMessage.getNotification() != null) {
//            Logger.printMessage(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
//        }
//
//        // Also if you intend on generating your own notifications as a result of a received FCM
//        // message, here is where that should be initiated. See sendNotification method below.

        Logger.printMessage("@@@ PUSH", remoteMessage.getData().toString());
        try {
            JSONObject Object = new JSONObject(remoteMessage.getData().get("body"));

            Logger.printMessage("PushResponse", "==" + Object.toString());
            sendNotification(Object.toString());


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
    // [END receive_message]

    /**
     * Schedule a job using FirebaseJobDispatcher.
     */
    private void scheduleJob() {
        // [START dispatch_job]
//        FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(new GooglePlayDriver(this));
//        Job myJob = dispatcher.newJobBuilder()
//                .setService(MyJobService.class)
//                .setTag("my-job-tag")
//                .build();
//        dispatcher.schedule(myJob);
        // [END dispatch_job]
    }

    /**
     * Handle time allotted to BroadcastReceivers.
     */
    private void handleNow() {
        Logger.printMessage(TAG, "Short lived task is done.");
    }

    /**
     * Create and show a simple notification containing the received FCM message.
     *
     * @param messageBody FCM message body received.
     */
    private void sendNotification(String messageBody) {

        try {

            Bitmap icon = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher_owner_one);

            //Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//            Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
//            if(defaultSoundUri == null){
//                defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
//                if(defaultSoundUri == null){
//                    defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//                }
//            }

            NotificationCompat.BigTextStyle bigText = new NotificationCompat.BigTextStyle();
            bigText.bigText(messageBody);
            bigText.setBigContentTitle(messageBody);


            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                    .setLargeIcon(icon)
                    .setSmallIcon(R.mipmap.ic_launcher_owner_one)
                    .setContentTitle("ProringerPro")
                    .setContentText(messageBody)
                    .setAutoCancel(true)
                    .setDefaults(Notification.DEFAULT_ALL)

                    .setStyle(bigText)

//                     //Vibration
//                    .setVibrate(new long[] { 1000, 1000, 1000, 1000, 1000 })
//
//                    //LED
//                    .setLights(Color.GREEN, 3000, 3000)

                     //Ton
                    //.setSound(Uri.parse("uri://sadfasdfasdf.mp3"))
                    //.setSound(defaultSoundUri)

                    .setPriority(Notification.PRIORITY_HIGH);

            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean applicationInForeground() {
        ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> services = activityManager.getRunningAppProcesses();
        boolean isActivityFound = false;

        if (services.get(0).processName.equalsIgnoreCase(getPackageName())) {
            isActivityFound = true;
        }
        return isActivityFound;
    }
}
