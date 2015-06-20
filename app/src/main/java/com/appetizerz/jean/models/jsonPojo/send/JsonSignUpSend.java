package com.appetizerz.jean.models.jsonPojo.send;

import com.fasterxml.jackson.annotation.JsonRootName;

import com.appetizerz.jean.models.jsonPojo.recieve.UserInfoRecieve;

/**
 * Created by david on 14-11-12.
 */
@JsonRootName(value = "")
public class JsonSignUpSend {

    public JsonSignUpSend(String email, String password, String name, String phone, String time_zone) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.phone = phone;
        this.time_zone = time_zone;
    }

    public JsonSignUpSend(String time_zone, String password, UserInfoRecieve userInfoRecieve) {
        this.time_zone = time_zone;
        this.password = password;
        this.email = userInfoRecieve.email;
        this.name = userInfoRecieve.name;
        this.phone = userInfoRecieve.phone;
    }

    public String email;
    public String password;
    public String name;
    public String phone;
    public String time_zone;

}
