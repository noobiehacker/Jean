package com.appetizerz.jean.models.jsonPojo.recieve.notification;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonRootName;

import java.io.Serializable;

/**
 * Created by david on 15-04-08.
 */
@JsonRootName("group_team_games")
@JsonIgnoreProperties(ignoreUnknown = true)
public class GroupTeamGames implements Serializable{

    private boolean email;
    private boolean push;

    public boolean isEmail() {
        return email;
    }

    public void setEmail(boolean email) {
        this.email = email;
    }

    public boolean isPush() {
        return push;
    }

    public void setPush(boolean push) {
        this.push = push;
    }
}
