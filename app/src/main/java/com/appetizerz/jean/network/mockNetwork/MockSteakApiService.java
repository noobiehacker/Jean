package com.appetizerz.jean.network.mockNetwork;
import java.util.Collections;

import com.appetizerz.jean.network.SteakApi;
import com.appetizerz.jean.utils.BusProvider;
import retrofit.RetrofitError;
import retrofit.client.Header;
import retrofit.client.Response;
import rx.Observable;

/**
 * Created by david on 14-12-04.
 */
public class MockSteakApiService implements SteakApi {

    private int statusCode;

    public MockSteakApiService (int statusCode){
        this.statusCode = statusCode;
    }

    private <T> Observable<T> createMockRespoonse(T item){

            Observable<T> result = null;
            if (statusCode != 200) {
                BusProvider.post(createError());
            } else {
                result = Observable.just(item);
            }
            return result;

        }

    private RetrofitError createError(){
        Response response = createResponse();
        return RetrofitError.httpError("", response, null,null);
    }

    private Response createResponse(){
        return new Response("", statusCode, "", Collections.<Header>emptyList(),null );
    }


    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }


}
