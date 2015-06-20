package com.appetizerz.jean.test;

import android.test.ActivityInstrumentationTestCase2;

import com.robotium.solo.Solo;

import com.appetizerz.jean.views.activities.MainActivity;

/**
 * Created by david on 15-02-24.
 */
public class UITest extends
        ActivityInstrumentationTestCase2<MainActivity> {

    private Solo solo;

    public UITest() {
        super(MainActivity.class);
    }

    public void setUp() throws Exception {
        solo = new Solo(getInstrumentation(), getActivity());
    }


    @Override
    public void tearDown() throws Exception {
        solo.finishOpenedActivities();
    }
}