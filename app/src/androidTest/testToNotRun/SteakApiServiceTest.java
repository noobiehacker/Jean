package androidJunit; /**
 * Created by david on 14-12-01.
 */
import com.squareup.otto.Subscribe;

import junit.framework.TestCase;

import org.junit.After;
import org.junit.Before;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;

import com.appetizerz.jean.models.jsonPojo.recieve.SessionRecieve;
import com.appetizerz.jean.models.jsonPojo.send.JsonResetPasswordSend;
import com.appetizerz.jean.models.jsonPojo.send.JsonLoginSend;
import com.appetizerz.jean.network.ServiceBuilder;
import com.appetizerz.jean.network.SteakApi;
import com.appetizerz.jean.network.mockNetwork.MockSteakApiService;
import com.appetizerz.jean.utils.BusProvider;
import com.appetizerz.jean.utils.StaticString;
import com.appetizerz.jean.utils.events.MitooActivitiesErrorEvent;
import retrofit.MockRestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Action1;
import org.junit.Test;


public class SteakApiServiceTest extends TestCase {

    protected MitooActivitiesErrorEvent error;
    protected CountDownLatch signal = new CountDownLatch(1);
    protected Object itemToCheck ;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        BusProvider.register(this);
    }

    @After
    public void tearDown() throws Exception {
        super.tearDown();
    }

    @Test
    public void testNoNetwork() throws Exception {

        ServiceBuilder builder = ServiceBuilder.getSingleTonInstance();
        SteakApi api = builder.setEndPoint(StaticString.steakLocalEndPoint)
                .create(SteakApi.class, new MockSteakApiService(200));
        MockRestAdapter adapter = builder.getMockRestAdapter();
        adapter.setErrorPercentage(100);
        String userID= UUID.randomUUID().toString();
        testCreateSession(userID);
        assertEquals(this.error.getRetrofitError().getKind(), RetrofitError.Kind.NETWORK);

    }

    public void testCreateRegSessionResetPasswordPass() throws Exception {

        String userID= UUID.randomUUID().toString();
        testCreateRegistration(userID );
        testCreateSession(userID);
        testResetPassword(userID);

    }

    public void testCreateRegFail() throws Exception {

        String userID= UUID.randomUUID().toString();
        testCreateRegistration(userID );
        testCreateRegistration(userID);

    }

    public void testCreateSessionFail() throws Exception {

        String userID= UUID.randomUUID().toString();
        testCreateRegistration(userID);

    }

    public void deleteSession() throws Exception {

        SteakApi api = new ServiceBuilder().setEndPoint(StaticString.steakLocalEndPoint)
                                           .create(SteakApi.class);
        Observable<SessionRecieve> recieve = api.deleteSession();
        recieve.subscribe(new Action1<SessionRecieve>() {
            public void call(SessionRecieve s) {
                assertNotNull(s);
            }
        });


    }
    private void testCreateRegistration(String userID) throws Exception {

        SteakApi api = ServiceBuilder.getSingleTonInstance().setEndPoint(StaticString.steakLocalEndPoint)
                .create(SteakApi.class);
        JsonLoginSend sendingObject = new JsonLoginSend(userID, StaticString.testPassword);
        Observable<SessionRecieve> observable = api.createRegistration(StaticString.apiConstantRegister
                ,sendingObject);
        testApiMethod(observable,SessionRecieve.class);
        assertNotNull(getItemToCheck());
        setItemToCheck(null);

    }

    private void testCreateSession(String userID) throws Exception {

        SteakApi api = ServiceBuilder.getSingleTonInstance().setEndPoint(StaticString.steakLocalEndPoint)
                .create(SteakApi.class);
        JsonLoginSend loginObject = new JsonLoginSend(userID, StaticString.testPassword);
        Observable<SessionRecieve> observable = api.createSession(loginObject);
        testApiMethod(observable, SessionRecieve.class);
        assertNotNull(getItemToCheck());
        setItemToCheck(null);

    }

    private void testResetPassword(String userID) throws Exception {

        SteakApi api =ServiceBuilder.getSingleTonInstance().setEndPoint(StaticString.steakLocalEndPoint)
                .create(SteakApi.class);
        Observable<Response> observable = api.resetPassword(new JsonResetPasswordSend(userID));
        testApiMethod(observable , Response.class);
        assertNotNull(getItemToCheck());
        setItemToCheck(null);

    }

    private <T> void testApiMethod(Observable<T> observable , Class<T> classType) throws Exception {

        signal = new CountDownLatch(1);
        handleObservable(observable , classType);
        signal.await();;

    }

    private <T> void handleObservable(Observable<T> observable , Class<T> classType){
        observable.subscribe(createSubscriber(classType));
    }

    @Subscribe
    public void onObserverResponse(Object object){
        setItemToCheck(object);
        signal.countDown();
    }

    @Subscribe
    public void onError(MitooActivitiesErrorEvent error){
        this.error = error;
        signal.countDown();
    }


    private <T> Subscriber<T> createSubscriber(Class<T> objectRecieve){
        return new Subscriber<T>() {

            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
            }

            @Override
            public void onNext(T objectRecieve) {
                BusProvider.post(objectRecieve);
            }
        };
    }

    public Object getItemToCheck() {
        return itemToCheck;
    }

    public void setItemToCheck(Object itemToCheck) {
        this.itemToCheck = itemToCheck;
    }

    @Subscribe
    public void getError(MitooActivitiesErrorEvent error){
        this.error = error;
    }

}
