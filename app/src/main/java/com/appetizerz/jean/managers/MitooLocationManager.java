package com.appetizerz.jean.managers;

import android.content.Context;
import android.location.LocationManager;

import com.appetizerz.jean.views.activities.MainActivity;

/**
 * Created by david on 14-12-08.
 */
public class MitooLocationManager {
    
    private MainActivity mainActivity;

    public MitooLocationManager(MainActivity activity) {
        this.mainActivity = activity;
    }

    public Boolean LocationServicesIsOn() {

        LocationManager locationManager = (LocationManager) this.mainActivity.getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    public MainActivity getMainActivity() {
        return mainActivity;
    }

}