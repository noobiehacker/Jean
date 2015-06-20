package com.appetizerz.jean.views.fragments;
import android.os.Bundle;
import android.view.*;

import com.appetizerz.jean.R;

/**
 * Created by david on 14-11-05.
 */
public class HomeFragment extends DavixFragment {


    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = getActivity().getLayoutInflater().inflate(R.layout.fragment_home,
                container, false);
        initializeViews(view);
        initializeFields();
        return view;
    }

    @Override
    public void onResume(){
        super.onResume();
    }

    @Override
    public void onPause(){
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();

    }

    @Override
    protected void initializeFields(){
        super.initializeFields();
        setPopActionRequiresDelay(true);
    }

    @Override
    protected void initializeViews(View view){
        initializeOnClickListeners(view);

    }

    @Override
    protected void initializeOnClickListeners(View view){


    }

    @Override
    public void onClick(View v) {

    }


}
