package espresso;
import android.test.ActivityInstrumentationTestCase2;
import com.google.android.apps.common.testing.ui.espresso.ViewInteraction;
import com.google.android.apps.common.testing.ui.espresso.assertion.ViewAssertions;
import com.squareup.otto.Subscribe;
import java.util.concurrent.CountDownLatch;
import static com.google.android.apps.common.testing.ui.espresso.Espresso.onView;
import static com.google.android.apps.common.testing.ui.espresso.Espresso.registerIdlingResources;
import static com.google.android.apps.common.testing.ui.espresso.matcher.ViewMatchers.withId;
import static com.google.android.apps.common.testing.ui.espresso.matcher.ViewMatchers.*;
import TestHelperClass.MitooIdlingResource;
import com.appetizerz.jean.utils.BusProvider;
import com.appetizerz.jean.utils.events.MitooActivitiesErrorEvent;
import com.appetizerz.jean.views.activities.MainActivity;
import com.appetizerz.jean.R;

/**
 * Created by david on 14-12-10.
 */
public class MitooActivityTest  extends ActivityInstrumentationTestCase2<MainActivity> {

    public MitooActivityTest() {
        super(MainActivity.class);
    }

    protected MitooActivitiesErrorEvent error;
    protected Object itemToCheck ;
    protected CountDownLatch signal = new CountDownLatch(1);

    @Override
    public void setUp() throws Exception {
        super.setUp();
        MainActivity activity =getActivity();
        registerIdlingResources(new MitooIdlingResource(R.id.fragment_landing));
        BusProvider.register(this);
        signal.await();
    }

    public void testLandingFragmentExist() {

        //Test if certain elements of the Landing Fragment exists
        onView(withText(R.id.slider)).check(ViewAssertions.matches(isDisplayed()));
    }

    public void testCompeitionSearchButtonExist() {

        //Test if certain elements of the Landing Fragment exists
        onView(withId(R.id.searchButton)).check(ViewAssertions.matches(isDisplayed()));
    }

    public void testLoginButtonExist() {

        //Test if certain elements of the Landing Fragment exists
        onView(withId(R.id.searchButton)).check(ViewAssertions.matches(isDisplayed()));
    }

    @Subscribe
    public void onObserverResponse(Object object){
        signal.countDown();
    }
}
