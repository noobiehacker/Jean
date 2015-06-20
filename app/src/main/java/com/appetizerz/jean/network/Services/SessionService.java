package com.appetizerz.jean.network.Services;
import com.squareup.otto.Subscribe;
import com.appetizerz.jean.R;
import com.appetizerz.jean.models.jsonPojo.Invitation_token;
import com.appetizerz.jean.models.jsonPojo.recieve.SessionRecieve;
import com.appetizerz.jean.models.jsonPojo.recieve.UserInfoRecieve;
import com.appetizerz.jean.network.DataPersistanceService;
import com.appetizerz.jean.utils.BusProvider;
import com.appetizerz.jean.utils.IsPersistable;
import com.appetizerz.jean.utils.events.SessionPersistanceResponseEvent;
import com.appetizerz.jean.utils.events.SessionModelRequestEvent;
import com.appetizerz.jean.utils.events.SessionModelResponseEvent;
import com.appetizerz.jean.views.activities.MainActivity;
import retrofit.client.Response;

/**
 * Created by david on 14-11-11.
 */

public class SessionService extends BaseService implements IsPersistable {

    public SessionService(MainActivity activity) {
        super(activity);
    }

    private SessionRecieve session;

    private Invitation_token invitation_token;

    public SessionRecieve getSession() {

        return session;
    }

    public void setSession(SessionRecieve user) {
        this.session = user;
    }

    @Subscribe
    public void requestSession(SessionModelRequestEvent event) {

        handleRequestEvent(event);
    }

    /*public void requestPasswordRequest(ResetPasswordRequestEvent event) {

        handleObservable(getSteakApiService().resetPassword(new JsonResetPasswordSend(event.getEmail())),
                Response.class);
    }*/

    private void postUserRecieveResponse() {

        BusProvider.post(new SessionModelResponseEvent(getSession()));

    }

    private void postResetPasswordResponse(Response response) {

      //  BusProvider.post(new ResetPasswordResponseEvent(response));

    }

    private void handleRequestEvent(SessionModelRequestEvent event) {

        switch (event.getRequestType()){

            /*
            case SIGNUP:
                handleObservable(getSteakApiService().createRegistration(
                                StaticString.apiConstantRegister, event.getSingUpUser()),
                        SessionRecieve.class);
                break;
            case LOGIN:
                handleObservable(getSteakApiService().createSession(event.getLoginUser()),
                        SessionRecieve.class);
                break;
                */
        }
    }

    @Override
    protected void handleSubscriberResponse(Object objectRecieve) {

        if (objectRecieve instanceof SessionRecieve) {
            setSession((SessionRecieve)objectRecieve);
            updateToken();
            saveData();
            postUserRecieveResponse();

        } else if (objectRecieve instanceof Response) {
            postResetPasswordResponse((Response) objectRecieve);
        }
    }

    @Override
    public void readData() {

        DataPersistanceService service = getPersistanceService();
        setSession(service.readFromPreference(getPreferenceKey() , SessionRecieve.class));
        updateToken();
    }

    @Override
    public void saveData() {

        getPersistanceService().saveToPreference(getPreferenceKey() , getSession());

    }

    @Override
    public void deleteData() {

        getPersistanceService().deleteFromPreference(getPreferenceKey());
        setSession(null);
    }

    @Override
    public String getPreferenceKey() {
        return getActivity().getString(R.string.shared_preference_session_key);
    }

    @Subscribe
    public void sessionPersistanceResponse(SessionPersistanceResponseEvent event) {

        setSession(event.getPersistedObject());
        updateToken();
        saveData();
        postUserRecieveResponse();

    }
    
    private void updateToken(){
        if(this.session!=null)
            getActivity().updateAuthToken(this.session.auth_token);
    }
    
    public boolean userIsLoggedIn(){
        
        return getSession()!=null;
        
    }

    public Invitation_token getInvitation_token() {
        return invitation_token;
    }

    public void setInvitation_token(Invitation_token invitation_token) {
        this.invitation_token = invitation_token;
    }

    public void updateSession(UserInfoRecieve userInfoRecieve) {
        if (session == null)
            setSession(new SessionRecieve());
        if(userInfoRecieve.auth_token!=null)
            session.auth_token = userInfoRecieve.auth_token;
        session.id = userInfoRecieve.id;
        updateToken();
        saveData();
    }

}