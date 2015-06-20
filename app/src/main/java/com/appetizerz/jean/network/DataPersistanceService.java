package com.appetizerz.jean.network;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.appetizerz.jean.R;


/**
 * Created by david on 15-01-22.
 */
public class DataPersistanceService {

    protected Runnable serializeRunnable;
    private Activity activity;

    public DataPersistanceService(Activity activity){
        setActivity(activity);
    }

    protected String getSavedObjectData(String key ,String defaultValue){
        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        return sharedPref.getString(key, defaultValue);
    }

    protected void saveStringToPreference(String key, String value){
        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(key, value);
        editor.commit();
    }

    protected void deleteStringFromPreference(String key){
        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.remove(key);
        editor.commit();
    }

    protected void clearPreference() {
        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.clear().commit();
    }

    protected String getSharedPreferenceErrorValue(){
        return getActivity().getString(R.string.shared_preference_error);
    }

    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public  <T> T readFromPreference(String key , final Class<T> classType) {
        
        String savedUserSerialized = getSavedObjectData(key, getSharedPreferenceErrorValue());
        return (T) deserializeObject(savedUserSerialized, classType);

    }

    public <T> void saveToPreference(String key, T object){

        final Object objectToPassIn= object;
        final String keyToPassIn  = key;
        this.serializeRunnable =new Runnable() {
            @Override
            public void run() {

                try {
                    ObjectMapper objectMapper = new ObjectMapper();
                    String serializedValue = objectMapper.writeValueAsString(objectToPassIn);
                    saveStringToPreference(keyToPassIn , serializedValue);
                }
                catch(Exception e){
                }
            }
        };

        runRunnableOnNewThread(this.serializeRunnable);

    }

    public void deleteFromPreference(String key){

        deleteStringFromPreference(key);

    }

    private void runRunnableOnNewThread(Runnable runnable){

        Thread t = new Thread(runnable);
        t.start();

    }
    
    private <T> T deserializeObject(String savedUserSerialized  , Class<T> classType) {
        T deserializedObject = null;

        try {
            if (savedUserSerialized != getSharedPreferenceErrorValue()) {
                ObjectMapper objectMapper = new ObjectMapper();
                deserializedObject = objectMapper.readValue(savedUserSerialized, classType);
            }
        } catch (Exception e) {
        }
        return deserializedObject;

    }

}
