package com.appetizerz.jean.utils.events;

import com.appetizerz.jean.models.jsonPojo.recieve.SessionRecieve;

/**
 * Created by david on 15-01-22.
 */
public class SessionPersistanceResponseEvent {
    
    private SessionRecieve persistedObject;

    public SessionPersistanceResponseEvent(SessionRecieve persistedObject) {
        this.persistedObject = persistedObject;
    }

    public SessionRecieve getPersistedObject() {
        return persistedObject;
    }

    public void setPersistedObject(SessionRecieve persistedObject) {
        this.persistedObject = persistedObject;
    }
}
