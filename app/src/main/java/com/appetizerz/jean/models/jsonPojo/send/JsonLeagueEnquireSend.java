package com.appetizerz.jean.models.jsonPojo.send;

import com.fasterxml.jackson.annotation.JsonRootName;

/**
 * Created by david on 15-01-21.
 */
@JsonRootName(value = "")
public class JsonLeagueEnquireSend {
    
    public int user_id;
    public String sport;

    public JsonLeagueEnquireSend(int user_id, String sport) {
        this.user_id = user_id;
        this.sport = sport;
    }
}
