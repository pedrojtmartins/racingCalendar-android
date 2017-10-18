package com.pedrojtmartins.racingcalendar.api;

import android.support.annotation.NonNull;

import com.pedrojtmartins.racingcalendar.interfaces.viewModels.IDataUpdater;
import com.pedrojtmartins.racingcalendar.models.ServerData;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Pedro Martins
 * 29/01/2017
 */

public class APIManager {
    private static IRacingCalendarAPI mApi;

    public static IRacingCalendarAPI getApi() {
        if (mApi == null) {
            mApi = new Retrofit.Builder()
                    .baseUrl(ApiAddresses.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                    .create(IRacingCalendarAPI.class);
        }

        return mApi;
    }

    public APIManager() {
    }

    public void updateDataIfRequired(final IDataUpdater dataUpdater, final int currVersion, final boolean forceUpdate) {
        if (dataUpdater == null)
            return;

        getApi().getDataVersion().enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(@NonNull Call<Integer> call, @NonNull Response<Integer> response) {
                if (response.code() != 200)
                    return;

                int version = response.body();
                if (version != currVersion || forceUpdate) {
                    updateData(dataUpdater, version);
                }
            }

            @Override
            public void onFailure(@NonNull Call<Integer> call, @NonNull Throwable t) {
                //TODO: Handle unsuccessful call
            }
        });
    }

    private void updateData(final IDataUpdater dataUpdater, final int version) {
        dataUpdater.newDataIsAvailable();
        getApi().getData().enqueue(new Callback<ServerData>() {
            @Override
            public void onResponse(@NonNull Call<ServerData> call, @NonNull Response<ServerData> response) {
                if (response.code() != 200)
                    return;

                ServerData data = response.body();
                if (data != null && data.races != null && data.series != null) {
                    data.version = version;
                    dataUpdater.newDataIsReady(data);
                }
            }

            @Override
            public void onFailure(@NonNull Call<ServerData> call, @NonNull Throwable t) {
                //TODO: Handle unsuccessful call
            }
        });
    }

    public void checkAppVersion(final IDataUpdater dataUpdater, final int currVersion) {
        if (dataUpdater == null)
            return;

        getApi().getAppVersion().enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(@NonNull Call<Integer> call, @NonNull Response<Integer> response) {
                if (response.code() != 200)
                    return;

                int version = response.body();
                if (version > currVersion) {
                    dataUpdater.newAppVersionIsAvailable();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Integer> call, @NonNull Throwable t) {
                //TODO: Handle unsuccessful call
            }
        });
    }
}
