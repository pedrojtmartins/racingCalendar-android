package com.pedrojtmartins.racingcalendar.Api;

import com.pedrojtmartins.racingcalendar.Models.Race;
import com.pedrojtmartins.racingcalendar.Models.Series;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Pedro Martins
 * 29/01/2017
 */

public class ApiManager {
    private static IRacingCalendarAPI mApi;
    private static IRacingCalendarAPI getApi() {
        if (mApi == null) {
            mApi = new Retrofit.Builder()
                    .baseUrl(ApiAddresses.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                    .create(IRacingCalendarAPI.class);
        }

        return mApi;
    }

    public ApiManager() {
    }
    public boolean updateRaceDataAsync(final ArrayList<Race> list) {
        if (list == null)
            return false;

        getApi().getAllRaces().enqueue(new Callback<List<Race>>() {
            @Override
            public void onResponse(Call<List<Race>> call, Response<List<Race>> response) {
                List<Race> newList = response.body();
                if (response.code() == 200 && newList != null && newList.size() > 0) {
                    list.clear();
                    list.addAll(response.body());
                } else {
                    //TODO: Handle unsuccessful call
                }
            }
            @Override
            public void onFailure(Call<List<Race>> call, Throwable t) {
                //TODO: Handle unsuccessful call
            }
        });

        return true;
    }
    public boolean updateSeriesDataAsync(final ArrayList<Series> list) {
        if (list == null)
            return false;

        getApi().getAllSeries().enqueue(new Callback<List<Series>>() {
            @Override
            public void onResponse(Call<List<Series>> call, Response<List<Series>> response) {
                List<Series> newList = response.body();
                if (response.code() == 200 && newList != null && newList.size() > 0) {
                    list.clear();
                    list.addAll(response.body());
                } else {
                    //TODO: Handle unsuccessful call
                }
            }
            @Override
            public void onFailure(Call<List<Series>> call, Throwable t) {
                //TODO: Handle unsuccessful call
            }
        });

        return true;
    }
}
