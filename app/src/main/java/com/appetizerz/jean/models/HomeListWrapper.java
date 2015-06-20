package com.appetizerz.jean.models;

import com.appetizerz.jean.models.jsonPojo.Competition;

/**
 * Created by david on 15-05-22.
 */
public class HomeListWrapper {

    private LeagueModel leagueModel;
    private Competition competition;

    public HomeListWrapper(LeagueModel leagueModel) {
        this.leagueModel = leagueModel;
    }

    public HomeListWrapper(Competition competition) {
        this.competition = competition;
    }
}
