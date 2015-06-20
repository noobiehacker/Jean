package com.appetizerz.jean.models.jsonPojo;

import com.appetizerz.jean.utils.IsSearchable;

/**
 * Created by david on 14-11-25.
 */
public class Sport implements IsSearchable {
    private String name;

    public Sport(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getItemID() {
        return getName();
    }
}
