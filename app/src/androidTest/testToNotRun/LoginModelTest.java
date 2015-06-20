package androidJunit;

import com.squareup.otto.Subscribe;

import org.junit.After;
import org.junit.Before;

import java.util.concurrent.CountDownLatch;

import com.appetizerz.jean.network.Services.SessionService;
import com.appetizerz.jean.network.ServiceBuilder;
import com.appetizerz.jean.network.SteakApi;
import com.appetizerz.jean.network.mockNetwork.MockSteakApiService;
import com.appetizerz.jean.utils.BusProvider;
import com.appetizerz.jean.utils.events.SessionModelRequestEvent;
import com.appetizerz.jean.utils.events.SessionModelResponseEvent;
import com.appetizerz.jean.utils.events.MitooActivitiesErrorEvent;


public class LoginModelTest extends MitooPojoTest{

    private SessionService model;
    private SteakApi mockApi;
    private SessionModelResponseEvent event;
    public LoginModelTest() {

    }

    @Before
    public void setUp() throws Exception {
        System.setProperty("dexmaker.dexcache", getContext().getCacheDir().getPath());
        model = new SessionService(null);
        BusProvider.register(this);
    }

    @After
    public void tearDown() throws Exception {
    }


    public void testLoginSuccess() throws Exception {

        signal = new CountDownLatch(1);
        mockApi = new ServiceBuilder().setEndPoint("http://www.mockendpoint.com")
                                      .create(SteakApi.class , new MockSteakApiService(200));
        model.setSteakApiService(mockApi);
        model.onSessioonRequest(new SessionModelRequestEvent("tim@mitoo.com", "password"));
        signal.await();
        assertNotNull(model.getUser());
        assertNotNull(this.event);

    }

    public void testLoginFail() throws Exception {

        testFailureStatusCode(400);
        testFailureStatusCode(401);
        testFailureStatusCode(422);
        testFailureStatusCode(500);

    }

    private void testFailureStatusCode(int statusCode){

        signal = new CountDownLatch(1);
        mockApi = new ServiceBuilder().create(SteakApi.class , new MockSteakApiService(200));
        model.setSteakApiService(mockApi);
        model.onSessioonRequest(new SessionModelRequestEvent("tim@mitoo.com", "password"));
        try{
            signal.await();
        }catch(Exception e){

        }
        assertEquals(this.error.getRetrofitError().getResponse().getStatus() , statusCode);
    }

    @Subscribe
    public void onLoginResponse(SessionModelResponseEvent event){
        this.event=event;
        signal.countDown();
    }

    @Subscribe
    public void onError(MitooActivitiesErrorEvent error){
        this.error = error;
        signal.countDown();
    }

}