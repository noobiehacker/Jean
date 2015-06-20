package com.appetizerz.jean.utils.events;
import java.util.List;
import com.appetizerz.jean.utils.IsSearchable;

/**
 * Created by david on 15-01-26.
 */
public class LocationModelQueryResultEvent {
    
    private List<IsSearchable> places;

    public LocationModelQueryResultEvent(List<IsSearchable> places) {
        this.places = places;
    }

    public List<IsSearchable> getPlaces() {
        return places;
    }

    public void setPlaces(List<IsSearchable> places) {
        this.places = places;
    }
}
