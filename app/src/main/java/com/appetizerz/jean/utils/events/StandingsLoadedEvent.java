package com.appetizerz.jean.utils.events;

import java.util.List;

import com.appetizerz.jean.models.appObject.StandingsRow;

/**
 * Created by david on 15-04-20.
 */
public class StandingsLoadedEvent {

    private List<StandingsRow> standingRows;

    public StandingsLoadedEvent(List<StandingsRow> standingRows) {
        this.standingRows = standingRows;
    }

    public List<StandingsRow> getStandingRows() {
        return standingRows;
    }
}
