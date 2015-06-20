package com.appetizerz.jean.models.jsonPojo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.android.gms.maps.model.LatLng;
import java.io.Serializable;

import com.appetizerz.jean.utils.MitooConstants;
import com.appetizerz.jean.views.widgets.MitooImageTarget;

/**
 * Created by david on 14-11-21.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class League implements Serializable {
    private int id;
    private int objectID;
    private String name;
    private String about;
    private String color_1;
    private String color_2;
    private String website;
    private boolean claimed;
    private String created_at;
    private String updated_at;
    private String logo;
    private String logo_large;
    private String logo_medium;
    private String logo_small;
    private String logo_thumb;
    private String cover;
    private String cover_mobile;
    private String cover_mobile_tall;
    private GeoLocation _geoloc;
    private location location;
    private String[] sports;
    private MitooImageTarget iconTarget;
    private MitooImageTarget leagueCover;
    private MitooImageTarget leagueMobileCover;

    public boolean isClaimed() {
        return claimed;
    }

    public void setClaimed(boolean claimed) {
        this.claimed = claimed;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getCover_mobile_tall() {
        return cover_mobile_tall;
    }

    public void setCover_mobile_tall(String cover_mobile_tall) {
        this.cover_mobile_tall = cover_mobile_tall;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getColor_1() {

        return color_1;
    }

    public void setColor_1(String color_1) {
        this.color_1 = color_1;
    }

    public String getColor_2() {
        return color_2;
    }

    public void setColor_2(String color_2) {
        this.color_2 = color_2;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public GeoLocation get_geoloc() {
        return _geoloc;
    }

    @JsonProperty("_geoLoc")
    public void set_geoloc(GeoLocation _geoloc) {
        this._geoloc = _geoloc;
    }

    public location getLocation() {
        return location;
    }

    public void setLocation(location location) {
        this.location = location;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public String getLogo_large() {
        return logo_large;
    }

    public void setLogo_large(String logo_large) {
        this.logo_large = logo_large;
    }

    public String getLogo_medium() {
        return logo_medium;
    }

    public void setLogo_medium(String logo_medium) {
        this.logo_medium = logo_medium;
    }

    public String getLogo_small() {
        return logo_small;
    }

    public void setLogo_small(String logo_small) {
        this.logo_small = logo_small;
    }

    public String getLogo_thumb() {
        return logo_thumb;
    }

    public void setLogo_thumb(String logo_thumb) {
        this.logo_thumb = logo_thumb;
    }

    public int getObjectID() {
        return objectID;
    }

    public void setObjectID(int objectID) {
        this.objectID = objectID;
    }

    public String getCover_mobile() {
        return cover_mobile;
    }

    public void setCover_mobile(String cover_mobile) {
        this.cover_mobile = cover_mobile;
    }

    public LatLng getLatLng(){

        LatLng result = null;
        if(get_geoloc()!=null){
            result = new LatLng(get_geoloc().getLat(), get_geoloc().getLng());
        }else if(getLocation()!=null){
            result = new LatLng( getLocation().getLat(), getLocation().getLng());
        }
        return result;
    }
    
    @Override
    public boolean equals(Object league){
        League compareLeague= (League) league;
        return this.getId()== compareLeague.getId();
    }
    
    public String[] getSports() {
        return sports;
    }

    public void setSports(String[] sports) {
        this.sports = sports;
    }

    public String getLeagueSports(){

        String result= "";
        String[] sports = getSports();
        for(int i = 0 ; i< sports.length ;i++){
            if(i!=0)
                result+= " - ";
            result+= sports[i];
        }

        return result;
    }

    public String getFirstSports(){
        if(getSports()!= null && getSports().length>0)
            return getSports()[0];
        return null;
    }
    
    public String getCity(){
        
        String result = "";
        if(getLocation()!=null){
            result = getLocation().getCity();
        }
        return result;
    }
    
    public String getShortenName(){
        
        String leagueName = getName();
        if(leagueName.length()> MitooConstants.maxLeagueCharacterName){
            leagueName = leagueName.substring(0,MitooConstants.maxLeagueCharacterName) + "...";
        }
        return leagueName;
    }

    public MitooImageTarget getIconTarget() {
        return iconTarget;
    }

    public void setIconTarget(MitooImageTarget iconTarget) {
        this.iconTarget = iconTarget;
    }

    public MitooImageTarget getLeagueCover() {
        return leagueCover;
    }

    public void setLeagueCover(MitooImageTarget leagueCover) {
        this.leagueCover = leagueCover;
    }

    public MitooImageTarget getLeagueMobileCover() {
        return leagueMobileCover;
    }

    public void setLeagueMobileCover(MitooImageTarget leagueMobileCover) {
        this.leagueMobileCover = leagueMobileCover;
    }


}
