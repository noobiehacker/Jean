package com.appetizerz.jean.views.fragments;

import android.os.Bundle;
import android.view.*;
import android.view.View;
import android.view.ViewGroup;
import com.squareup.otto.Subscribe;
import com.appetizerz.jean.R;
import com.appetizerz.jean.models.jsonPojo.Invitation_token;
import com.appetizerz.jean.models.jsonPojo.recieve.NotificationReceive;
import com.appetizerz.jean.utils.BusProvider;
import com.appetizerz.jean.utils.events.BranchIOResponseEvent;
import com.appetizerz.jean.utils.events.MitooActivitiesErrorEvent;
import com.appetizerz.jean.utils.events.ModelPersistedDataDeletedEvent;
import com.appetizerz.jean.utils.events.ModelPersistedDataLoadedEvent;
import com.appetizerz.jean.utils.events.NotificationEvent;

/**
 * Created by david on 14-11-05.
 */
public class SplashScreenFragment extends DavixFragment {

    private boolean branchResponseRecieved = false;
    private boolean persistedDataResponseRecieved = false;
    private Invitation_token invitationToken;
    private boolean dialogButtonCreated;

    @Override
    public void onClick(View v) {
    }

    public static SplashScreenFragment newInstance() {
        SplashScreenFragment fragment = new SplashScreenFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = getActivity().getLayoutInflater().inflate(R.layout.fragment_splash,
                container, false);
        setRunnable(new Runnable() {
            @Override
            public void run() {
                regularFlow();
            }
        });
        getHandler().postDelayed(getRunnable(), 15000);
        return view;
    }
    
    @Subscribe
    public void onModelPersistedDataLoaded(ModelPersistedDataLoadedEvent event) {

        setPersistedDataResponseRecieved(true);
        loadFirstFragment();

    }

    @Subscribe
    public void onModelPersistedDataDeleted(ModelPersistedDataDeletedEvent event) {

        setPersistedDataResponseRecieved(true);
        loadFirstFragment();

    }

    @Subscribe
    public void onBranchIOResponse(BranchIOResponseEvent event){

        setBranchResponseRecieved(true);
        loadFirstFragment();

    }

    @Subscribe
    public void onError(MitooActivitiesErrorEvent error){
        
         super.onError(error);
    }

    public boolean recievedBranchIOResponse() {
        return branchResponseRecieved;
    }

    public void setBranchResponseRecieved(boolean branchResponseRecieved) {
        this.branchResponseRecieved = branchResponseRecieved;
    }

    public boolean recievedPersistedDataResponse() {
        return persistedDataResponseRecieved;
    }

    public void setPersistedDataResponseRecieved(boolean persistedDataResponseRecieved) {
        this.persistedDataResponseRecieved = persistedDataResponseRecieved;
    }

    private void loadFirstFragment() {

        if (recievedBranchIOResponse() && recievedPersistedDataResponse()) {

            getHandler().removeCallbacks(getRunnable());
            NotificationReceive notificationReceive = getMitooActivity().getMitooApplication().getNotificationReceive();
            //NOTIFICATION FLOW
            if (notificationReceive!= null) {
                BusProvider.post(new NotificationEvent(notificationReceive));
                getMitooActivity().getMitooApplication().setNotificationReceive(null);
            } else{
                regularFlow();
            }

        }

    }

    private void regularFlow(){
        //REGULAR FLOW
        boolean isInviteFlow = true;
        if (isInviteFlow)
            startInviteFlow();
        else
            startRegularFlow();
    }

    public void startInviteFlow() {


    }

}
