package com.pedrojtmartins.racingcalendar.viewModels;

import android.databinding.ObservableArrayList;

import com.pedrojtmartins.racingcalendar.api.APIManager;
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
    private final int seriesId;
    private final int raceId;
    private final String seriesName;

    public ObservableArrayList<EventResultUnit> results;

    public ResultsViewModel(int seriesId, int raceId, String seriesName) {
        this.seriesId = seriesId;
        this.raceId = raceId;
        this.seriesName = seriesName;

        results = new ObservableArrayList<>();
    }

    public boolean loadResults() {
        if (seriesId != -1 && raceId == -1) {
            loadStandings();
            return true;
        }

        return false;
    }

    private void loadStandings() {
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

            if (htmlResults == null || htmlResults.isEmpty()) {
                // TODO: 23/07/2017 raise firebase exception to warn me
                return;
            }

            results.clear();
            results.addAll(htmlResults);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
