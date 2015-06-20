package com.appetizerz.jean.managers;
import java.util.ArrayList;
import java.util.List;

import com.appetizerz.jean.network.Services.AppSettingsService;
import com.appetizerz.jean.network.Services.BaseService;
import com.appetizerz.jean.network.Services.LeagueService;
import com.appetizerz.jean.network.Services.MobileTokenService;
import com.appetizerz.jean.network.Services.NotificationInAppServices;
import com.appetizerz.jean.network.Services.SessionService;
import com.appetizerz.jean.utils.BusProvider;
import com.appetizerz.jean.utils.DataHelper;
import com.appetizerz.jean.utils.IsPersistable;
import com.appetizerz.jean.utils.events.ModelPersistedDataDeletedEvent;
import com.appetizerz.jean.utils.events.ModelPersistedDataLoadedEvent;
import com.appetizerz.jean.views.activities.MainActivity;

/**
 * Created by david on 15-01-21.
 */
public class ModelManager {

    private MainActivity activity;
    private List<BaseService> baseServiceList;
    private List<IsPersistable> persistableList;
    protected Runnable currentRunnable;

    public ModelManager(MainActivity activity) {
        setActivity(activity);
        initializeServices();
    }

    private void initializeServices(){
        setMitooModelList(new ArrayList<BaseService>());
        setPersistableList(new ArrayList<IsPersistable>());
        inializeOnStartModels();
    }

    //USED FOR LOGING OUT
    public void clearAllUserServices(){
        for(BaseService service : this.baseServiceList){
            BusProvider.unregister(service);
        }
        initializeServices();
    }

    public MainActivity getMitooActivity() {
        return activity;
    }

    public void setActivity(MainActivity activity) {
        this.activity = activity;
    }

    public LeagueService getLeagueModel() {

        return (LeagueService) getModel(LeagueService.class);

    }

    public SessionService getSessionModel() {

        return (SessionService) getModel(SessionService.class);

    }

    public AppSettingsService getAppSettingsModel() {

        return (AppSettingsService) getModel(AppSettingsService.class);
    }

    public NotificationInAppServices getNotificationInAppService() {

        return (NotificationInAppServices)getModel(NotificationInAppServices.class);
    }


    private <T> BaseService getModel(Class<T> classType) {

        T classModel = null;
        Object model = getModelFromList(classType);
        if (model != null) {
            classModel = (T) model;
        } else {

            try {
                model = classType.getConstructor(MainActivity.class)
                        .newInstance(getMitooActivity());
            } catch (Exception e) {

                String tremp = e.toString();

            }
            addModel((BaseService) model);
        }
        return (BaseService) model;
    }

    public MobileTokenService getMobileTokenModel() {

        MobileTokenService mobileTokenModel = null;
        BaseService model = getModel(MobileTokenService.class);
        if (model != null) {
            mobileTokenModel  = (MobileTokenService) model;
        } else {
            mobileTokenModel  = new MobileTokenService(getMitooActivity());
            addModel(mobileTokenModel );
        }
        return mobileTokenModel;
    }

    public List<IsPersistable> getPersistableList() {
        return persistableList;
    }

    public void setPersistableList(List<IsPersistable> persistableList) {
        this.persistableList = persistableList;
    }

    public List<BaseService> getMitooModelList() {
        return baseServiceList;
    }

    public void setMitooModelList(List<BaseService> baseServiceList) {
        this.baseServiceList = baseServiceList;
    }

    public void addModel(BaseService modelToAdd) {

        getMitooModelList().add(modelToAdd);

        if (modelToAdd instanceof IsPersistable) {
            addModelToPersistableList((IsPersistable) modelToAdd);

        }
    }

    public BaseService getModelFromList(Class<?> modelClass) {

        BaseService result = null;
        forloop:
        for(BaseService item : this.baseServiceList){
            if(modelClass.isInstance(item)){
                result = item;
            }
            if(result!=null)
                break forloop;
        }
        return result;
    }
    
    private void addModelToPersistableList(IsPersistable persistable){

        getPersistableList().add(persistable);

    }
    private boolean containsModelType(Class<?> modelClass){

        boolean result = false;
        forloop:
        for(BaseService item : this.baseServiceList){
            if(modelClass.isInstance(item)){
                result = true;
            }
            if(result)
                break forloop;
        }
        return result;

    }
    
    private void inializeOnStartModels(){
        
        getSessionModel();
        getLeagueModel();
        getAppSettingsModel();
        getMobileTokenModel();
        getNotificationInAppService();

    }

    public void readAllPersistedData(){

        Runnable runnable =new Runnable() {
            @Override
            public void run() {

                try {
                    for(IsPersistable item  : getPersistableList()){
                        item.readData();
                    }
                    BusProvider.post(new ModelPersistedDataLoadedEvent());
                }
                catch(Exception e){
                    String temp = e.toString();
                }
            }
        };
        runRunnableInBackground(runnable);

    }

    public void deleteAllPersistedData(){

        Runnable runnable =new Runnable() {
            @Override
            public void run() {

                try {
                    for(IsPersistable item  : getPersistableList()){
                        item.deleteData();
                    }
                    removeModelReferences();
                    BusProvider.post(new ModelPersistedDataDeletedEvent());
                }
                catch(Exception e){
                }
            }
        };

        runRunnableInBackground(runnable);
    }
    
    private void runRunnableInBackground(Runnable runnable){
        
        this.currentRunnable= runnable;
        Thread thread = new Thread(this.currentRunnable);
        thread.start();

    }
    
    private void removeModelReferences(){

        DataHelper dataHelper = new DataHelper(getMitooActivity());

        for(BaseService item  : getMitooModelList()){
            item.resetFields();
        }
        System.gc();
    }
}
