package TestHelperClass;
import android.app.Fragment;

import com.google.android.apps.common.testing.ui.espresso.IdlingResource;

import com.appetizerz.jean.utils.BusProvider;
import com.appetizerz.jean.views.activities.MainActivity;
/**
 * Created by david on 14-12-11.
 */
public class MitooIdlingResource extends MainActivity implements IdlingResource {

    private ResourceCallback callback;
    private int id;
    public MitooIdlingResource(int id) {
        super();
        this.id=id;

    }

    @Override public String getName() {

        return "MitooIdlingResource is Loading";
    }

    @Override public boolean isIdleNow() {
        // The mitooActivity hasn't been injected yet, so we're idling
        Boolean result = false;
        Fragment landingFragment  =  getFragmentManager().findFragmentById(this.id);
        if (landingFragment== null){
            result =true;
        }
        return result;
    }

    @Override
    public void registerIdleTransitionCallback(ResourceCallback resourceCallback) {
        this.callback = resourceCallback;
    }

    private void postResponse(Object object){
        BusProvider.post(object);
    }
}