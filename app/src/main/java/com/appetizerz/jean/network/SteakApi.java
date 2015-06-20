package com.appetizerz.jean.network;

/**
 * Created by david on 14-11-10.
 */
public interface SteakApi {

    /* sample

    @POST("/auth/v1/registrations")
    Observable<SessionRecieve>  createRegistration(@Query("save_type") String param1, @Body JsonSignUpSend jsonObject);

    @DELETE("/auth/v1/sessions")
    Observable<SessionRecieve> deleteSession();

    @GET("/leagues/v1/users/{id}/competition_seasons")
    Observable<Competition[]> getCompetitionSeasonFromUserID(@Query("filter") String filter ,@Query("league_info") String league_info ,@Path("id") int id);

    @PATCH("/notifications/v1/users/{user_id}/preferences/competition/{competition_id}")
    Observable<NotificationPreferenceRecieved> updateNotificationPreference(@Path("user_id") int user_id,
                                                                     @Path("competition_id") int competition_id,
                                                                     @Body NotificationPreferenceRecieved jsonObject);
    */

}
