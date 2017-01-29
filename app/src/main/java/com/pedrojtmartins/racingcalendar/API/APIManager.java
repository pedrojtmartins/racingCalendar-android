package com.pedrojtmartins.racingcalendar.API;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Pedro Martins
 * 29/01/2017
 */

public class APIManager {
    public static IRacingCalendarAPI getApi() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(IRacingCalendarAPI.baseURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit.create(IRacingCalendarAPI.class);
    }
}
