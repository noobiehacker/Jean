package com.appetizerz.jean.utils;

import com.appetizerz.jean.models.jsonPojo.RainOutMessage;

/**
 * Created by david on 15-05-19.
 */
public class RainOutModel {

    private RainOutMessage rainOutMessage;

    public RainOutMessage getRainOutMessage() {
        return rainOutMessage;
    }

    public void setRainOutMessage(RainOutMessage rainOutMessage) {
        this.rainOutMessage = rainOutMessage;
    }

    public RainOutModel(RainOutMessage rainOutMessage) {
        this.rainOutMessage = rainOutMessage;
    }
}
