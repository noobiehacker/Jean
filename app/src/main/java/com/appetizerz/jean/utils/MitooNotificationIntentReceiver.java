package com.appetizerz.jean.utils;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.urbanairship.push.BaseIntentReceiver;
import com.urbanairship.push.PushMessage;

import com.appetizerz.jean.models.jsonPojo.recieve.NotificationReceive;
import com.appetizerz.jean.utils.events.NotificationEvent;
import com.appetizerz.jean.views.activities.MainActivity;

/**
 * Created by david on 15-03-27.
 */
public class MitooNotificationIntentReceiver extends BaseIntentReceiver {

    private static final String TAG = "IntentReceiver";
    public static String bundleKey = "bundle_key_notification";

    @Override
    protected void onChannelRegistrationSucceeded(Context context, String channelId) {
        Log.i(TAG, "Channel registration updated. Channel Id:" + channelId + ".");
    }

    @Override
    protected void onChannelRegistrationFailed(Context context) {
        Log.i(TAG, "Channel registration failed.");
    }

    @Override
    protected void onPushReceived(Context context, PushMessage message, int notificationId) {
        Log.i(TAG, "Received push notification. Alert: " + message.getAlert() + ". Notification ID: " + notificationId);
    }

    @Override
    protected void onBackgroundPushReceived(Context context, PushMessage message) {
        Log.i(TAG, "Received background push message: " + message);
    }

    @Override
    protected boolean onNotificationOpened(Context context, PushMessage message, int notificationId) {
        Log.i(TAG, "User clicked notification. Alert: " + message.getAlert());
        Bundle bundle = message.getPushBundle();
        if(MainActivity.activityStarted){
            NotificationReceive notificationReceive = new NotificationReceive(bundle);
            BusProvider.post(new NotificationEvent(notificationReceive));
        }else{
            Intent myIntent = new Intent(context.getApplicationContext(), MainActivity.class);
            myIntent.putExtra(MitooNotificationIntentReceiver.bundleKey ,bundle);
            myIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(myIntent);
        }
        return false;

    }

    @Override
    protected boolean onNotificationActionOpened(Context context, PushMessage message, int notificationId, String buttonId, boolean isForeground) {
        Log.i(TAG, "User clicked notification button. Button ID: " + buttonId + " Alert: " + message.getAlert());
        // Return false to let UA handle launching the launch activity
        return false;
    }

    @Override
    protected void onNotificationDismissed(Context context, PushMessage message, int notificationId) {
        Log.i(TAG, "Notification dismissed. Alert: " + message.getAlert() + ". Notification ID: " + notificationId);
    }

}