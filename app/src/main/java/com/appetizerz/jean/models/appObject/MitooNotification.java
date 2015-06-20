package com.appetizerz.jean.models.appObject;

import com.appetizerz.jean.R;
import com.appetizerz.jean.utils.MitooEnum;
import com.appetizerz.jean.views.activities.MainActivity;

/**
 * Created by david on 15-03-12.
 */
public class MitooNotification {

    private MitooEnum.NotificationCategory notificationCategory;
    private MitooEnum.NotificationType notificationType;
    private MainActivity activity;

    public MitooEnum.NotificationCategory getNotificationCategory() {
        return notificationCategory;
    }

    public void setNotificationCategory(MitooEnum.NotificationCategory notificationCategory) {
        this.notificationCategory = notificationCategory;
    }

    public MitooNotification(MitooEnum.NotificationCategory notificationCategory, MitooEnum.NotificationType notificationType, MainActivity activity) {
        this.notificationCategory = notificationCategory;
        this.notificationType = notificationType;
        this.activity = activity;
    }

    public MainActivity getActivity() {
        return activity;
    }

    public void setActivity(MainActivity activity) {
        this.activity = activity;
    }

    public String getNotificationText() {

        String result = "";

        switch(getNotificationCategory()){
            case TeamGames:
                break;
            case TeamResults:
                break;
            case LeagueResults:
                break;
            case RainOut:
                break;
        }

        return result;
    }

    public MitooEnum.NotificationType getNotificationType() {
        return notificationType;
    }

    public void setNotificationType(MitooEnum.NotificationType notificationType) {
        this.notificationType = notificationType;
    }

    public boolean isHeaderObject(){
        if(this.notificationCategory == MitooEnum.NotificationCategory.TeamGames)
            return true;
        return false;
    }

    public boolean equals(MitooNotification anotherNotification){

        boolean result = false;
        if(anotherNotification!=null){
            result = this.getNotificationType() == anotherNotification.getNotificationType();
            result = result && this.getNotificationCategory() == anotherNotification.getNotificationCategory();
        }
        return result;

    }
}
