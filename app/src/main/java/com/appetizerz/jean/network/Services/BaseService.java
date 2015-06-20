package com.appetizerz.jean.network.Services;
import android.os.Handler;

import java.util.Stack;

import com.appetizerz.jean.managers.ModelManager;
import com.appetizerz.jean.network.DataPersistanceService;
import com.appetizerz.jean.network.ServiceBuilder;
import com.appetizerz.jean.network.SteakApi;
import com.appetizerz.jean.utils.BusProvider;
import com.appetizerz.jean.utils.MitooConstants;
import com.appetizerz.jean.views.activities.MainActivity;
import rx.Observable;
import rx.Subscriber;

/**
 * Created by david on 14-11-12.
 */

public abstract class BaseService {

    protected MainActivity activity;
    protected DataPersistanceService persistanceService;
    protected Handler handler;
    protected Runnable backgroundRunnable;
    private Stack<Object> events;


    private SteakApi steakApiService;

    public BaseService(MainActivity activity) {
        setActivity(activity);
        BusProvider.register(this);
    }
    
    public SteakApi getSteakApiService() {
        if(steakApiService == null)
            steakApiService = ServiceBuilder.getSingleTonInstance().getSteakApiService();
        return steakApiService;
    }

    public void setSteakApiService(SteakApi steakApiService) {
        this.steakApiService = steakApiService;
    }

    protected void removeReferences(){
        BusProvider.unregister(this);
    }

    public boolean isPersistanceStorage() {
        return MitooConstants.getPersistenceStorage();
    }

    public MainActivity getActivity() {
        return activity;
    }

    public void setActivity(MainActivity activity) {
        this.activity = activity;
    }

    public DataPersistanceService getPersistanceService() {
        if(persistanceService==null)
            persistanceService=getActivity().getPersistanceService();
        return persistanceService;
    }

    protected <T> void handleObservable(Observable<T> observable, Class<T> classType) {
        observable.subscribe(createSubscriber(classType));
    }

    protected <T> Subscriber<T> createSubscriber(Class<T> objectRecieve) {
        return new Subscriber<T>() {

            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {

                String error = e.getMessage();

            }

            @Override
            public void onNext(T objectRecieve) {

                handleSubscriberResponse(objectRecieve);
            }
        };
    }

    protected void handleSubscriberResponse(Object objectRecieve) {
    }

    protected void obtainResults() {
    }

    public void resetFields() {}

    protected int getUserID(){
        ModelManager manager = this.activity.getModelManager();
        if(manager.getSessionModel().getSession()!=null)
            return manager.getSessionModel().getSession().id;
        else
            return MitooConstants.invalidConstant;
    }

    protected Stack<Object> getEventsStack(){
        if(this.events == null)
            this.events = new Stack<Object>();
        return this.events;

    }
}
