package com.appetizerz.jean.views.application;
import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.appetizerz.jean.views.fragments.DavixFragment;
import com.urbanairship.AirshipConfigOptions;
import com.urbanairship.Logger;
import com.urbanairship.UAirship;
import com.urbanairship.push.notifications.DefaultNotificationFactory;

import com.appetizerz.jean.BuildConfig;
import com.appetizerz.jean.models.jsonPojo.recieve.NotificationReceive;

import io.keen.client.java.KeenClient;
import io.keen.client.android.AndroidKeenClientBuilder;
import io.keen.client.java.KeenProject;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Stack;
import com.appetizerz.jean.R;
import com.appetizerz.jean.managers.ModelManager;
import com.appetizerz.jean.utils.MitooConstants;
import com.appetizerz.jean.utils.MitooEnum;
import com.appetizerz.jean.views.activities.MainActivity;


/**
 * Created by david on 15-03-26.
 */

public class DavixApplication extends Application{

    public static int userID = MitooConstants.invalidConstant;
    private Stack<DavixFragment> fragmentStack;
    private ModelManager modelManager;
    private boolean persistedDataLoaded = false;
    private Queue<Object> eventQueue;
    private NotificationReceive notificationReceive;

    @Override
    public void onCreate() {

        super.onCreate();
        this.modelManager = null;
        this.eventQueue = new LinkedList<Object>();
        this.modelManager = null;
        setUpKeen();
        setUpUAirship();

    }

    private void setUpUAirship(){

        UAirship.takeOff(this, createAirshipOptions(), new UAirship.OnReadyCallback() {
            @Override
            public void onAirshipReady(UAirship airship) {
                // Create a customized default notification factory
                DefaultNotificationFactory defaultNotificationFactory = new DefaultNotificationFactory(getApplicationContext());
                defaultNotificationFactory.setSmallIconId(R.drawable.notification_icon);
                defaultNotificationFactory.setColor(getResources().getColor(R.color.black));
                // Set it
                airship.getPushManager().setNotificationFactory(defaultNotificationFactory);

                // Enable Push
                airship.getPushManager().setPushEnabled(true);
                airship.getPushManager().setUserNotificationsEnabled(true);
                String channelId = UAirship.shared().getPushManager().getChannelId();
                Logger.info("My Application Channel ID: " + channelId);
            }
        });

    }

    private void setUpKeen(){

        // If the Keen Client isn't already initialized, initialize it.
        if (!KeenClient.isInitialized()) {

            // Create a new instance of the client.
            KeenClient client = new AndroidKeenClientBuilder(this).build();

            // Get the project ID and write key from string resources, then create a project and set
            // it as the default for the client.
            String projectId = getString(R.string.keen_project_id);
            String writeKey = getString(R.string.keen_write_key);
            KeenProject project = new KeenProject(projectId, writeKey, null);
            client.setDefaultProject(project);

            setUpKeenClientGlobalProperties(client);

            // Initialize the KeenClient singleton with the created client.
            KeenClient.initialize(client);
        }

    }

    @Override
    protected void attachBaseContext(Context base){
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    private AirshipConfigOptions createAirshipOptions() {

        /*
        AirshipConfigOptions options = AirshipConfigOptions.loadDefaultOptions(this);
        options.developmentAppKey = getString(R.string.UA_app_dev_key);
        options.productionAppKey = getString(R.string.UA_app_prod_key);
        options.developmentAppSecret = getString(R.string.UA_app_dev_secret);
        options.productionAppSecret = getString(R.string.UA_app_prod_secret);
        options.gcmSender = getString(R.string.API_key_gcm_sender);
        options.inProduction = (MitooConstants.getAppEnvironment() == MitooEnum.AppEnvironment.PRODUCTION);
        return options;
        */
        return null;

    }

    public Stack<DavixFragment> getFragmentStack() {
        if (fragmentStack == null)
            fragmentStack = new Stack<DavixFragment>();
        return fragmentStack;
    }

    public ModelManager getModelManager() {
        return this.modelManager;
    }

    public void setUpPersistenceData(MainActivity activity) {

        if(this.persistedDataLoaded==false){
            this.modelManager = new ModelManager(activity);
            if (MitooConstants.getPersistenceStorage()) {
                this.modelManager.readAllPersistedData();
            } else {
                this.modelManager.deleteAllPersistedData();
            }
            this.persistedDataLoaded=true;
        }else{
            if(this.modelManager!=null){
                this.modelManager.setActivity(activity);
            }
        }

    }

    private void setUpKeenClientGlobalProperties(KeenClient client){
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("platform", "android");
        map.put("app_version", BuildConfig.VERSION_NAME);
        client.setGlobalProperties(map);
    }


    public Queue<Object> getEventQueue() {
        return eventQueue;
    }

    public NotificationReceive getNotificationReceive() {
        return notificationReceive;
    }

    public void setNotificationReceive(NotificationReceive notificationReceive) {
        this.notificationReceive = notificationReceive;
    }
}