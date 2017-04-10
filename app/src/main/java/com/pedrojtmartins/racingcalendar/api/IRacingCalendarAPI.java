package com.pedrojtmartins.racingcalendar.api;

import com.pedrojtmartins.racingcalendar.models.ServerData;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Pedro Martins
 * 29/01/2017
 */

public interface IRacingCalendarAPI {

    /**
     * Retrieve current races data version in the server
     */
    @GET(ApiAddresses.URL_VERSION)
    Call<Integer> getDataVersion();

    /**
     * Retrieves all races in the server
     */
    @GET(ApiAddresses.URL_DATA)
    Call<ServerData> getData();

    @GET(ApiAddresses.URL_APP_VERSION)
    Call<Integer> getAppVersion();
}
