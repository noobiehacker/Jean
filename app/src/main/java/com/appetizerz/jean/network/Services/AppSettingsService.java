package com.appetizerz.jean.network.Services;
import com.appetizerz.jean.R;
import com.appetizerz.jean.network.DataPersistanceService;
import com.appetizerz.jean.utils.IsPersistable;
import com.appetizerz.jean.views.activities.MainActivity;

/**
 * Created by david on 15-02-18.
 */
public class AppSettingsService extends BaseService implements IsPersistable {

    public AppSettingsService(MainActivity activity) {
        super(activity);
    }

    private Boolean userHasUsedApp;

    public Boolean getUserHasUsedApp() {
        return userHasUsedApp;
    }

    public void setUserHasUsedApp(Boolean userHasUsedApp) {
        this.userHasUsedApp = userHasUsedApp;
        saveData();
    }

    @Override
    public void readData() {

        DataPersistanceService service = getPersistanceService();
        setUserHasUsedApp(service.readFromPreference(getPreferenceKey(), Boolean.class));

    }

    @Override
    public void saveData() {

        getPersistanceService().saveToPreference(getPreferenceKey() , getUserHasUsedApp());

    }

    @Override
    public void deleteData() {

        getPersistanceService().deleteFromPreference(getPreferenceKey());
        setUserHasUsedApp(null);

    }

    @Override
    public String getPreferenceKey() {
        return getActivity().getString(R.string.shared_preference_user_has_used_app);
    }

    public void saveUsedAppBoolean(){

        //Field is set and save at the save time, so if its already set, it must have been saved
        //(Other than error / exception of course, so for optimization purposes, don't save 
        //if the field is not null
        if(getUserHasUsedApp()==null)
            setUserHasUsedApp(new Boolean(true));
    }

}

