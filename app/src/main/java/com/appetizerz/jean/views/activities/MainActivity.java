package com.appetizerz.jean.views.activities;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.os.Handler;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.content.ActivityNotFoundException;

import com.appetizerz.jean.R;
import com.appetizerz.jean.utils.events.ConsumeNotificationEvent;
import com.appetizerz.jean.views.application.DavixApplication;
import com.appetizerz.jean.views.fragments.DavixFragment;
import com.newrelic.agent.android.NewRelic;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Protocol;
import com.squareup.otto.Subscribe;
import com.squareup.picasso.OkHttpDownloader;
import com.squareup.picasso.Picasso;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

import com.appetizerz.jean.managers.MitooLocationManager;
import com.appetizerz.jean.models.jsonPojo.recieve.NotificationReceive;
import com.appetizerz.jean.models.jsonPojo.recieve.SessionRecieve;
import com.appetizerz.jean.network.DataPersistanceService;
import com.appetizerz.jean.network.ServiceBuilder;
import com.appetizerz.jean.utils.AppStringHelper;
import com.appetizerz.jean.utils.BusProvider;
import com.appetizerz.jean.utils.DataHelper;
import com.appetizerz.jean.utils.FragmentFactory;
import com.appetizerz.jean.utils.MitooConstants;
import com.appetizerz.jean.utils.MitooEnum;
import com.appetizerz.jean.managers.ModelManager;
import com.appetizerz.jean.utils.MitooNotificationIntentReceiver;
import com.appetizerz.jean.utils.events.FragmentChangeEvent;
import com.appetizerz.jean.utils.events.LogOutEvent;
import com.appetizerz.jean.utils.events.LogOutNetworkCompleteEevent;
import com.appetizerz.jean.utils.events.MobileTokenDisassociateRequestEvent;

import io.branch.referral.Branch;
import io.keen.client.java.KeenClient;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MainActivity extends ActionBarActivity {

    public static boolean activityStarted = false;
    private MitooLocationManager locationManager;
    private Handler handler;
    private Runnable runnable;
    private DataHelper dataHelper;
    private Picasso picasso;
    protected DataPersistanceService persistanceService;
    private int firstFragmentToStart = R.id.fragment_splash;
    private Branch branch;
    private AppStringHelper appStringHelper;
    private boolean onSplashScreen = true;
    private Queue<Object> eventQueue;
    private Branch.BranchReferralInitListener branchReferralInitListener;
    private static boolean confirmFlowFired = false;
    private String authToken;
    private boolean notifcationRetrieved = false;
    private boolean onResumeCalled = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mitoo);
        initializeFields();
        if (savedInstanceState == null) {
            MainActivity.activityStarted = true;
            startApp();
        } else {
            this.authToken = savedInstanceState.getString(getAuthTokenKey());
            updateAuthToken(this.authToken);
        }
        setUpPersistenceData();

    }

    @Override
    public void onPause() {
        tearDownReferences();
        // This causes queued Keen events to be sent (async) to Keen
        KeenClient.client().sendQueuedEventsAsync();
        onSaveInstanceState(new Bundle());
        super.onPause();
    }

    @Override
    public void onSaveInstanceState(Bundle bundle) {
        bundle.putString(getAuthTokenKey(), this.authToken);
        super.onSaveInstanceState(bundle);
    }

    @Override
    public void onDestroy() {
        tearDownReferences();
        BusProvider.unregister(this);
        super.onDestroy();
    }

    @Override
    public void onStop() {
        branch.closeSession();
        tearDownReferences();
        super.onStop();
    }

    @Override
    protected void onResume() {
        super.onResume();
        this.onResumeCalled = true;
        getBranch().initSession(getBranchReferralInitListener(), getIntent().getData(), this);
        if(this.notifcationRetrieved){
            consumeEventsInQueue();
        }

    }

    @Override
    protected void onRestart() {
        super.onRestart();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.menu_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Subscribe
    public void onConsumeNotifcationEvent(ConsumeNotificationEvent event){
        this.notifcationRetrieved = true;
        consumeEventsInQueue();
    }

    public void consumeEventsInQueue() {

        if (this.notifcationRetrieved == true &&
                this.onResumeCalled == true &&
                getModelManager().getSessionModel().userIsLoggedIn()) {

            this.notifcationRetrieved= false;
            if (!getEventQueue().isEmpty())
                popAllFragments();
            while (!getEventQueue().isEmpty()) {
                BusProvider.post(getEventQueue().poll());
            }
        }

    }

    private Queue<Object> getEventQueue(){
        DavixApplication application = (DavixApplication) getApplication();
        return application.getEventQueue();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    private void initializeFields() {

        setUpNewRelic();
        setUpInitialCalligraphy();
        setLocationManager(new MitooLocationManager(this));
//        setUpBranch();
        initializeNotification();
        BusProvider.register(this);

    }

    private void initializeNotification() {

        Bundle extra = getIntent().getBundleExtra(MitooNotificationIntentReceiver.bundleKey);
        if (extra != null) {
            getMitooApplication().setNotificationReceive(new NotificationReceive(extra));
            getIntent().removeExtra(MitooNotificationIntentReceiver.bundleKey);
        }

    }

    private void setUpInitialCalligraphy() {

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                        .setDefaultFontPath(getString(R.string.DIN_Regular))
                        .setFontAttrId(R.attr.fontPath)
                        .build()
        );
    }

    @Subscribe
    public void onFragmentQueueChange(LinkedList<FragmentChangeEvent> queueOfEvents) {

        popAllFragments();
        while (!queueOfEvents.isEmpty())
            BusProvider.post(queueOfEvents.pop());
    }

    @Subscribe
    public void onFragmentChange(final FragmentChangeEvent event) {

        if (fragmentIsRoot(event.getFragmentId()))
            popAllFragments();
        hideSoftKeyboard();

        if (event.popPrevious())
            popFragment();

        switch (event.getTransition()) {
            case PUSH:
                pushFragment(event);
                break;
            case CHANGE:
            case NONE:
                swapFragment(event);
                break;
            case POP:
                popFragment();
                break;
        }
    }

    @Subscribe
    public void startIntent(Intent intent) {
        startActivity(intent);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    public boolean LocationServicesIsOn() {
        return getLocationManager().LocationServicesIsOn();
    }


    private void pushFragment(FragmentChangeEvent event) {

        try {
            DavixFragment fragment = FragmentFactory.getInstance().buildFragment(event);
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            setFragmentAnimation(ft, event.getAnimation());
            ft.addToBackStack(String.valueOf(event.getFragmentId()));
            ft.replace(R.id.content_frame, fragment);
            ft.commit();
            getFragmentStack().push(fragment);
        } catch (Exception e) {
            Log.i("MitooFragmentException", e.getStackTrace().toString());
        }

    }

    private void swapFragment(FragmentChangeEvent event) {

        try {
            DavixFragment fragment = FragmentFactory.getInstance().buildFragment(event);
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            setFragmentAnimation(ft, event.getAnimation());
            ft.replace(R.id.content_frame, fragment);
            ft.commitAllowingStateLoss();
            getFragmentStack().push(fragment);
        } catch (Exception e) {
            Log.i("MitooFragmentException", e.getStackTrace().toString());
        }

    }

    private void setFragmentAnimation(FragmentTransaction transction, MitooEnum.FragmentAnimation animation) {

        if (animation == MitooEnum.FragmentAnimation.HORIZONTAL)
            setLeftToRightAnimation(transction);
        else if (animation == MitooEnum.FragmentAnimation.VERTICAL)
            setBottomToTopAnimation(transction);
        else if (animation == MitooEnum.FragmentAnimation.DOWNLEFT)
            setBottomToTopAnimation(transction);
    }

    private void setBottomToTopAnimation(FragmentTransaction transaction) {

        transaction.setCustomAnimations(R.animator.enter_top, R.animator.no_animation,
                0, R.animator.enter_bottom);

    }

    private void setLeftToRightAnimation(FragmentTransaction transaction) {

        transaction.setCustomAnimations(R.animator.enter_right, R.animator.exit_right,
                R.animator.exit_left, R.animator.enter_left);
    }


    public void popFragment() {

        try {
            if (getFragmentStack().size() > 0) {
                getFragmentStack().pop();
                getFragmentManager().popBackStack();
                setPreviousFragmentBackClicked();
            }
        } catch (Exception e) {
            Log.i("MitooFragmentException", e.getStackTrace().toString());
        }

    }

    public void popFragment(int delayed) {

        Runnable popFragmentRunnable = new Runnable() {
            @Override
            public void run() {
                popFragment();
            }
        };
        setRunnable(popFragmentRunnable);
        getHandler().postDelayed(getRunnable(), delayed);
    }

    public void popAllFragments() {

        getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        while (getFragmentStack().size() > 0)
            getFragmentStack().pop();

    }

    public boolean NetWorkConnectionIsOn() {

        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();

    }

    private void setUpNewRelic() {

        NewRelic.withApplicationToken(getDataHelper().getNewRelicKey())
                .start(this.getApplication());

    }

    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() == 0) {
            this.moveTaskToBack(true);
        } else {
            if (!getFragmentStack().isEmpty() && getFragmentStack().peek().backPressedAllowed()) {
                hideSoftKeyboard();
                DavixFragment fragmentToDesplay = getSecondTopFragment();
                if (fragmentToDesplay != null && fragmentToDesplay.popActionRequiresDelay())
                    popFragment(250);
                else
                    popFragment();
            }

        }
    }

    private DavixFragment getSecondTopFragment() {

        if (getFragmentStack().size() > 1) {
            return getFragmentStack().get(getFragmentStack().size() - 2);
        }
        return null;

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    }

    public void updateAuthToken(String auth_token) {

        if (auth_token != null) {
            this.authToken = auth_token;
            ServiceBuilder.getSingleTonInstance().setXAuthToken(this.authToken);
        }
    }

    public void resetAuthToken() {

        ServiceBuilder.getSingleTonInstance().resetXAuthToken();
    }

    public void startApp() {

        FragmentChangeEvent event =
                new FragmentChangeEvent(this, MitooEnum.FragmentTransition.NONE,
                        getFirstFragmentToStart(), MitooEnum.FragmentAnimation.NONE);
        BusProvider.post(event);

    }

    @Subscribe
    public void logOut(LogOutEvent event) {

        logOutNetWorkCalls();

    }

    private void logOutNetWorkCalls() {
        getModelManager().getMobileTokenModel();
        BusProvider.post(new MobileTokenDisassociateRequestEvent());
    }

    @Subscribe
    public void logOutCleanUpAppReferences(LogOutNetworkCompleteEevent event) {

        getModelManager().deleteAllPersistedData();
        getModelManager().clearAllUserServices();
        resetAuthToken();
        popAllFragments();

        FragmentChangeEvent fragmentChangeEvent =
                new FragmentChangeEvent(this, MitooEnum.FragmentTransition.NONE,
                        R.id.fragment_splash, MitooEnum.FragmentAnimation.HORIZONTAL);

        BusProvider.post(fragmentChangeEvent);

    }

    public void hideSoftKeyboard(View view) {
        Activity activity = this;
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }


    public void hideSoftKeyboard(int delayed) {

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                MainActivity.this.hideSoftKeyboard();
            }
        };
        setRunnable(runnable);
        getHandler().postDelayed(getRunnable(), delayed);

    }

    public void hideSoftKeyboard() {

        if (this.getCurrentFocus() != null) {
            View view = this.getCurrentFocus();
            InputMethodManager inputMethodManager = (InputMethodManager) this.getSystemService(Activity.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);

        }
    }

    public Picasso getPicasso() {
        if (picasso == null) {
            OkHttpClient client = new OkHttpClient();
            client.setProtocols(Arrays.asList(Protocol.HTTP_1_1));
            Picasso picasso = new Picasso.Builder(this)
                    .downloader(new OkHttpDownloader(client))
                    .build();
            setPicasso(picasso);
        }
        return picasso;
    }

    public void setPicasso(Picasso picasso) {
        this.picasso = picasso;
    }

    public DataHelper getDataHelper() {
        if (dataHelper == null)
            setDataHelper(new DataHelper(this));
        return dataHelper;
    }

    public void setDataHelper(DataHelper dataHelper) {
        this.dataHelper = dataHelper;
    }

    public void showKeyboard() {

        if (MainActivity.this.getCurrentFocus() != null) {
            View view = MainActivity.this.getCurrentFocus();
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
        }

    }

    public void showKeyboard(int delayed) {

        Runnable runnable = new Runnable() {
            @Override
            public void run() {

                MainActivity.this.showKeyboard();

            }
        };
        setRunnable(runnable);
        getHandler().postDelayed(getRunnable(), delayed);

    }

    public Handler getHandler() {
        if (handler == null)
            handler = new Handler();
        return handler;
    }

    public void setHandler(Handler handler) {
        this.handler = handler;
    }

    public Runnable getRunnable() {
        return runnable;
    }

    public void setRunnable(Runnable runnable) {
        this.runnable = runnable;
    }

    private void handleCallBacks() {

        if (getHandler() != null) {
            getHandler().removeCallbacksAndMessages(getRunnable());
        }

    }

    public void tearDownReferences() {

        this.onResumeCalled = false;
        handleCallBacks();

    }

    public DataPersistanceService getPersistanceService() {
        if (persistanceService == null)
            persistanceService = new DataPersistanceService(this);
        return persistanceService;
    }

    private boolean fragmentIsRoot(int id) {
        return true;
        //return id == R.id.fragment_home || id == R.id.fragment_landing;
    }


    public MitooLocationManager getLocationManager() {
        return locationManager;
    }

    public void setLocationManager(MitooLocationManager locationManager) {
        if (locationManager == null)
            locationManager = new MitooLocationManager(this);
        this.locationManager = locationManager;
    }

    public int getFirstFragmentToStart() {
        return firstFragmentToStart;
    }

    public boolean isOnSplashScreen() {
        return onSplashScreen;
    }

    public void setOnSplashScreen(boolean onSplashScreen) {
        this.onSplashScreen = onSplashScreen;
    }

    private Branch getBranch() {
        if (branch == null)
            branch = Branch.getInstance(this.getApplicationContext(), getAppStringHelper().getBranchAPIKey());
        return branch;
    }

    public AppStringHelper getAppStringHelper() {
        if (appStringHelper == null)
            appStringHelper = new AppStringHelper(this);
        return appStringHelper;
    }


    private void setPreviousFragmentBackClicked() {
        if (getFragmentStack().size() > 0 && getFragmentStack().peek() != null)
            getFragmentStack().peek().setBackClicked(true);
    }

    public Class<?> topFragmentType() {
        if (getFragmentStack().size() != 0) {
            DavixFragment fragment = getFragmentStack().peek();
            return fragment.getClass();
        }
        return null;
    }

    public Branch.BranchReferralInitListener getBranchReferralInitListener() {
        return branchReferralInitListener;
    }


    public DavixApplication getMitooApplication() {

        if (getApplication() instanceof DavixApplication) {
            return (DavixApplication) getApplication();
        }
        return null;
    }

    private Stack<DavixFragment> getFragmentStack() {
        return getMitooApplication().getFragmentStack();

    }

    protected String getConfirmInfoKey() {
        return getString(R.string.bundle_key_confirm_token_key);
    }

    protected String getAuthTokenKey() {
        return getString(R.string.bundle_key_auth_token_key);
    }


    protected int getUserID() {

        DataPersistanceService service = getPersistanceService();
        String key = getString(R.string.shared_preference_session_key);

        Object object = service.readFromPreference(key, SessionRecieve.class);

        if (object instanceof SessionRecieve) {
            SessionRecieve session = (SessionRecieve) object;
            return session.id;
        } else {
            return MitooConstants.invalidConstant;
        }
    }

    public ModelManager getModelManager() {
        return getMitooApplication().getModelManager();
    }

    public void setUpPersistenceData() {

       getMitooApplication().setUpPersistenceData(this);

    }

}
