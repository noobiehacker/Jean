package com.appetizerz.jean.utils.events;

import com.appetizerz.jean.models.jsonPojo.recieve.NotificationPreferenceRecieved;

/**
 * Created by david on 15-05-21.
 */
public class NotificationModelUpdateResponse {

    private NotificationPreferenceRecieved preferenceRecieved;

    public NotificationModelUpdateResponse(NotificationPreferenceRecieved preferenceRecieved) {
        this.preferenceRecieved = preferenceRecieved;
    }

    public NotificationPreferenceRecieved getPreferenceRecieved() {
        return preferenceRecieved;
    }

    public void setPreferenceRecieved(NotificationPreferenceRecieved preferenceRecieved) {
        this.preferenceRecieved = preferenceRecieved;
    }
}
