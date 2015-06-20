package com.appetizerz.jean.network.Services;

import com.appetizerz.jean.views.application.DavixApplication;
import com.appetizerz.jean.views.fragments.DavixFragment;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.Stack;

import com.appetizerz.jean.R;
import com.appetizerz.jean.models.jsonPojo.recieve.NotificationReceive;
import com.appetizerz.jean.services.EventTrackingService;
import com.appetizerz.jean.utils.MitooEnum;
import com.appetizerz.jean.utils.events.NotificationEvent;
import com.appetizerz.jean.views.activities.MainActivity;
import com.appetizerz.jean.views.fragments.HomeFragment;

/**
 * Created by david on 15-05-19.
 */
public class NotificationInAppServices extends BaseService {

    public NotificationInAppServices(MainActivity activity) {
        super(activity);
    }

    private List<NotificationReceive> notificationInQueue = new ArrayList<NotificationReceive>();

    @Subscribe
    public void onNotificationRecieve(NotificationEvent event) {

        NotificationReceive notification = event.getNotificationReceive();
        EventTrackingService.userOpenedNotification(this.getUserID(), "", notification.getObj_type(), notification.getObj_id(), notification.getMitoo_action());
        MitooEnum.RoutingDestination destination = getRoutingDestination(event.getNotificationReceive());
        this.notificationInQueue.add(notification);

        if (destination == MitooEnum.RoutingDestination.FIXTURE) {
            String fixtureObjectID = event.getNotificationReceive().getObj_id();
            routeToFixture(fixtureObjectID);
        } else if (destination == MitooEnum.RoutingDestination.COMPETITIONSEASON) {
            String competitionObjectID = event.getNotificationReceive().getObj_id();
            routeToCompetitionSeason(competitionObjectID);
        }

    }

    private void routeToFixture(String fixtureObjectID) {

        int fixtureID = Integer.parseInt(fixtureObjectID);
        enableHomeScreenLoading();
        //BusProvider.post(new FixtureNotificaitonRequestEvent(fixtureID));
    }

    private void routeToCompetitionSeason(String competitionObjectID) {
        int competitionSeasonID = Integer.parseInt(competitionObjectID);
        enableHomeScreenLoading();
        // BusProvider.post(new CompetitionNotificationRequestEvent(competitionSeasonID));

    }

    private MitooEnum.RoutingDestination getRoutingDestination(NotificationReceive notificationReceive) {

        return MitooEnum.RoutingDestination.FIXTURE;
    }

    public void consumeEventsInQueue() {

        //  BusProvider.post(new ConsumeNotificationEvent());

    }
/*
    @Subscribe
    public void onCompetitionNotificationResponseEvent(CompetitionNotificationResponseEvent event) {

        disableHomeScreenLoading();

        //IF WER ARE ON Competition SCREEN
        if (getActivity().topFragmentType() == CompetitionSeasonFragment.class && !event.hasError()){

            String mitooAction = getMitooActionFromID(event.getCompetition().getId());
            if(mitooAction!=null)
                BusProvider.post(new CompetitionNotificationUpdateResponseEvent(event.getCompetition(), mitooAction));
            else
                BusProvider.post(new CompetitionNotificationUpdateResponseEvent(event.getCompetition()));

        }
        else {

            //IF WER ARE ON OTHER SCREEN
            queueHomeFragment();

            if (!event.hasError()) {
                //COMPETITION SEASON SCREEN PUSH
                queueCompetitionFragment(event.getCompetition().getId());
            }
            if (!getFragmentStack().isEmpty()){
                consumeEventsInQueue();

            }

        }

    }*/

  /*  @Subscribe
    public void onFixtureNotificationResponseEvent(FixtureNotificationResponseEvent event) {

        disableHomeScreenLoading();

        //IF WER ARE ON FIXTURE SCREEN
        if (getActivity().topFragmentType() == FixtureFragment.class && !event.hasError())
            BusProvider.post(new FixtureNotificationUpdateResponseEvent(event.getFixture()));
        else {

            //IF WER ARE ON OTHER SCREEN
            queueHomeFragment();

            if (!event.hasError()) {
                //COMPETITION SEASON SCREEN PUSH
                queueCompetitionFragment(event.getFixture().getFixture().getCompetition_season_id());

                //FIXTURE SCREEN PUSH
                queueFixtureFragment(event.getFixture().getFixture().getId());

            }

            if (!getFragmentStack().isEmpty())
                consumeEventsInQueue();

        }
    }*/

    private void disableHomeScreenLoading() {

        if (getFragmentStack().size() > 0) {
            DavixFragment fragmemt = getFragmentStack().peek();
            if (fragmemt instanceof HomeFragment)
                fragmemt.setLoading(false);
        }
    }

    private void enableHomeScreenLoading() {

        if (getFragmentStack().size() > 0) {
            DavixFragment fragmemt = getFragmentStack().peek();
            if (fragmemt instanceof HomeFragment)
                fragmemt.setLoading(true);
        }
    }

    private void queueHomeFragment() {
/*
        Bundle homeBundle = new Bundle();
        homeBundle.putInt(getUserIDKey(), getUserID());

        //HOME SCREEN PUSH
        FragmentChangeEvent firstEvent = FragmentChangeEventBuilder.getSingletonInstance()
                .setFragmentID(R.id.fragment_home)
                .setTransition(MitooEnum.FragmentTransition.CHANGE)
                .setBundle(homeBundle)
                .build();


        getEventQueue().offer(firstEvent);*/

    }

    private void queueCompetitionFragment(int competitionSeasonID) {

/*        Bundle competitionBundle = new Bundle();
        competitionBundle.putInt(getCompetitionSeasonIdKey(), competitionSeasonID);
        competitionBundle.putString(getTeamColorKey(), getActivity().getString(R.string.place_holder_color_one));
        if(getMitooActionFromID(competitionSeasonID)!=null)
            competitionBundle.putString(getMitooActionKey(), getMitooActionFromID(competitionSeasonID));
        FragmentChangeEvent seconndEvent = FragmentChangeEventBuilder.getSingletonInstance()
                .setFragmentID(R.id.fragment_competition)
                .setTransition(MitooEnum.FragmentTransition.PUSH)
                .setBundle(competitionBundle)
                .build();
        getEventQueue().offer(seconndEvent);*/

    }

    private String getMitooActionFromID(int id) {
        NotificationReceive notificationReceive = findNotificationInQueue(id);
        if (notificationReceive != null) {
            return notificationReceive.getMitoo_action();
        } else {
            return null;
        }
    }

    private NotificationReceive findNotificationInQueue(int id) {

        NotificationReceive result = null;
        if (this.notificationInQueue != null && !this.notificationInQueue.isEmpty()) {

            loop:
            for (NotificationReceive item : this.notificationInQueue) {
                String objectID = item.getObj_id();
                if (objectID != null) {
                    int notificationID = Integer.parseInt(objectID);
                    if (notificationID == id) {
                        result = item;
                        break loop;
                    }
                }
            }
        }

        return result;
    }

    private void queueFixtureFragment(int fixtureID) {

/*        Bundle fixtureBundle = new Bundle();
        fixtureBundle.putInt(getFixtureIdKey(), fixtureID);
        FragmentChangeEvent thirdEvent = FragmentChangeEventBuilder.getSingletonInstance()
                .setFragmentID(R.id.fragment_fixture)
                .setTransition(MitooEnum.FragmentTransition.PUSH)
                .setBundle(fixtureBundle)
                .build();
        getEventQueue().offer(thirdEvent);*/

    }


    private Stack<DavixFragment> getFragmentStack() {
        DavixApplication application = (DavixApplication) getActivity().getApplication();
        return application.getFragmentStack();
    }

    private Queue<Object> getEventQueue() {
        DavixApplication application = (DavixApplication) getActivity().getApplication();
        return application.getEventQueue();
    }

}
