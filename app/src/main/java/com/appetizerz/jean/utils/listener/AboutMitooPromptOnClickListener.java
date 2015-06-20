package com.appetizerz.jean.utils.listener;

import android.content.Context;
import android.content.DialogInterface;

/**
 * Created by david on 15-01-20.
 */
public class AboutMitooPromptOnClickListener extends MitooOptionsDialogOnClickListner{

    public AboutMitooPromptOnClickListener(boolean startIntent, Context context) {
        super(startIntent, context);
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        if (isPositiveListener()) {

/*            Bundle bundle = new Bundle();
            bundle.putString(getContext().getString(R.string.bundle_key_prompt), String.valueOf(getSelectedOption()));
            FragmentChangeEvent fragmentChangeEvent = FragmentChangeEventBuilder.getSingletonInstance()
                    .setFragmentID(R.id.fragment_about_mitoo)
                    .setTransition(MitooEnum.FragmentTransition.PUSH)
                    .setBundle(bundle)
                    .build();
            BusProvider.post(fragmentChangeEvent);*/

        }

        dialog.dismiss();
    }
}
