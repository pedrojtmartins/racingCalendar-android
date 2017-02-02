package com.pedrojtmartins.racingcalendar.Api;

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

    /**
     * Retrieve current races and series data versions in the server
     */
    @GET(ApiAddresses.URL_DATA_VERSION)
    Call<List<Integer>> getVersions();

    /**
     * Retrieves all races in the server
     */
    @GET(ApiAddresses.URL_RACES)
    Call<List<Race>> getAllRaces();

    /**
     * Retrieves all series in the server
     */
    @GET(ApiAddresses.URL_SERIES)
    Call<List<Series>> getAllSeries();
}
