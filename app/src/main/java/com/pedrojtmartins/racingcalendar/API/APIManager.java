package com.pedrojtmartins.racingcalendar.Api;

import com.pedrojtmartins.racingcalendar.Interfaces.ViewModels.IDataUpdater;
import com.pedrojtmartins.racingcalendar.Models.ServerData;

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

    public void updateDataIfRequired(final IDataUpdater dataUpdater, final int currVersion) {
        if (dataUpdater == null)
            return;

        getApi().getDataVersion().enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                int version = response.body();
                if (response.code() == 200 && version > currVersion) {
                    updateData(dataUpdater, version);
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
    private void updateData(final IDataUpdater dataUpdater, final int version) {
        getApi().getData().enqueue(new Callback<ServerData>() {
            @Override
            public void onResponse(Call<ServerData> call, Response<ServerData> response) {
                ServerData data = response.body();
                if (response.code() == 200 && data != null && data.races != null && data.series != null) {
                    data.version = version;
                    dataUpdater.newDataIsReady(data);
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
}
