package com.pedrojtmartins.racingcalendar.viewModels;

import android.databinding.ObservableArrayList;

import com.pedrojtmartins.racingcalendar.api.APIManager;
import com.pedrojtmartins.racingcalendar.database.DatabaseManager;
import com.pedrojtmartins.racingcalendar.eventResults.EventResultsParser;
import com.pedrojtmartins.racingcalendar.models.EventResultUnit;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Pedro Martins
 * 26/06/2017
 */

public class ResultsViewModel {
    private final DatabaseManager databaseManager;
    private final int seriesId;
    private final int raceId;

    public ObservableArrayList<EventResultUnit> results;

    public ResultsViewModel(DatabaseManager databaseManager, int seriesId, int raceId) {
        this.databaseManager = databaseManager;
        this.seriesId = seriesId;
        this.raceId = raceId;

        results = new ObservableArrayList<>();

        downloadStatus(seriesId);
    }

    private void downloadStatus(final int seriesId) {
        String url = EventResultsParser.getUrl(seriesId);
        if (url == null || url.isEmpty())
            return;

        APIManager.getApi().getHtmlFrom(url).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    decodeHtml(response.body(), seriesId);
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    private void decodeHtml(ResponseBody responseBody, int seriesId) {
        if (responseBody == null)
            return;

        try {
            String html = responseBody.string();
            ArrayList<EventResultUnit> htmlResults = EventResultsParser.getStandings(html, seriesId);

            if (htmlResults == null || htmlResults.isEmpty())
                return;

            results.clear();
            results.addAll(htmlResults);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
