package com.appetizerz.jean.models.jsonPojo.recieve;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonRootName;

/**
 * Created by david on 15-01-21.
 */
@JsonRootName(value = "")
@JsonIgnoreProperties(ignoreUnknown = true)

public class UserInfoRecieve {
    
    public int id;
    public String email;
    public String name;
    public String phone;
    public String auth_token;
    public String time_zone;
    public String picture;
    public String picture_large;
    public String picture_medium;
    public String picture_small;
    public String picture_thumb;
    public String confirmed_at;

}
