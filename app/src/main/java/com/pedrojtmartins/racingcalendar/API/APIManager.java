package com.pedrojtmartins.racingcalendar.Api;

import com.pedrojtmartins.racingcalendar.Database.DatabaseManager;
import com.pedrojtmartins.racingcalendar.Interfaces.ViewModels.IDataUpdater;
import com.pedrojtmartins.racingcalendar.Models.ServerData;
import com.pedrojtmartins.racingcalendar.SharedPreferences.SharedPreferencesManager;

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

    private void updateData(final IDataUpdater dataUpdater,
                            final DatabaseManager dbManager,
                            final SharedPreferencesManager sharedPreferencesManager,
                            final int version) {
        getApi().getData().enqueue(new Callback<ServerData>() {
            @Override
            public void onResponse(Call<ServerData> call, Response<ServerData> response) {
                ServerData data = response.body();
                if (response.code() == 200 && data.races != null && data.series != null) {
                    dbManager.addRaces(data.races);
                    dbManager.addSeries(data.series);

                    dataUpdater.getRaceList().clear();
                    dataUpdater.getRaceList().addAll(dbManager.getRaces());

                    dataUpdater.getSeriesList().clear();
                    dataUpdater.getSeriesList().addAll(dbManager.getSeries());

                    sharedPreferencesManager.addDataVersion(version);
                } else {
                    //TODO: Handle unsuccessful call
                }
            }
            @Override
            public void onFailure(Call<ServerData> call, Throwable t) {
                //TODO: Handle unsuccessful call
            }
        });
    }
    public void updateData(final IDataUpdater dataUpdater,
                           final SharedPreferencesManager sharedPreferencesManager,
                           final DatabaseManager dbManager) {
        final int currVersion = sharedPreferencesManager.getDataVersion();
        getApi().getDataVersion().enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                int version = response.body();
                if (response.code() == 200 && version > currVersion) {
                    updateData(dataUpdater, dbManager, sharedPreferencesManager, version);
                } else {
                    //TODO: Handle unsuccessful call
                }
            }
            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
                //TODO: Handle unsuccessful call
            }
        });
    }


}
