package com.pedrojtmartins.racingcalendar.API;

import com.pedrojtmartins.racingcalendar.Models.Race;
import com.pedrojtmartins.racingcalendar.Models.Series;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Pedro Martins
 * 29/01/2017
 */

public interface IRacingCalendarAPI {

    String baseURL = "base url here";

    /**
     * Retrieves all races in the server
     */
    @GET("races")
    Call<List<Race>> getAllRaces();

    /**
     * Retrieves all series in the server
     */
    @GET("series")
    Call<List<Series>> getAllSeries();

    /**
     * Retrieve current races and series data versions in the server
     */
    @GET("versions")
    Call<List<Integer>> getVersions();
}
