package com.appetizerz.jean.utils;

import com.appetizerz.jean.R;
import com.appetizerz.jean.views.activities.MainActivity;

/**
 * Created by david on 15-03-23.
 */
public class AppStringHelper {

    private MainActivity activity;

    public AppStringHelper(MainActivity activity) {
        this.activity = activity;
    }

    public MainActivity getActivity() {
        return activity;
    }

    public void setActivity(MainActivity activity) {
        this.activity = activity;
    }

    public String getBranchAPIKey(){
        return getActivity().getString(R.string.API_key_branch);
    }

}
