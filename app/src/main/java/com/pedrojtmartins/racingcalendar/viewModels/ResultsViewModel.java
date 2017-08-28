package com.pedrojtmartins.racingcalendar.viewModels;

import android.databinding.ObservableArrayList;
import android.databinding.ObservableInt;

import com.pedrojtmartins.racingcalendar.api.APIManager;
import com.pedrojtmartins.racingcalendar.eventResults.RaceResultsManager;
import com.pedrojtmartins.racingcalendar.eventResults.StandingsResultsManager;
import com.pedrojtmartins.racingcalendar.models.EventResultUnit;
import com.pedrojtmartins.racingcalendar.models.ResultsViewModelStatus;

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
    public static final int CONNECTION_FAILURE = -1;
    public static final int CONNECTION_RESULTS_EMPTY = -2;
    public static final int CONNECTION_STANDINGS_EMPTY = -3;

    public final ResultsViewModelStatus status;
    public final ObservableInt connectionResult;

    public final int seriesId;
    private final int raceId;
    private final int raceNum;
    public final String seriesName;
    public final String raceName;

    public ObservableArrayList<EventResultUnit> results;

    public ResultsViewModel(int seriesId, int raceId, int raceNum, String seriesName, String raceName) {
        this.seriesId = seriesId;
        this.raceId = raceId;
        this.raceNum = raceNum;
        this.seriesName = seriesName;
        this.raceName = raceName;

        status = new ResultsViewModelStatus();
        connectionResult = new ObservableInt(0);

        results = new ObservableArrayList<>();
    }

    public boolean loadResults() {
        if (seriesId == -1)
            return false;

        if (raceId == -1) {
            loadStandings();
            return true;
        } else if (raceNum != -1) {
            loadRaceResults();
            return true;
        }

        return false;
    }
    private void loadRaceResults() {
        String url = RaceResultsManager.getUrl(seriesId, raceNum);
        if (url == null || url.isEmpty())
            return;

        APIManager.getApi().getHtmlFrom(url).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                status.setLoadComplete(true);
                if (response.isSuccessful()) {
                    int count = decodeHtmlRace(response.body(), seriesId);
                    if (count == 0) {
                        connectionResult.set(CONNECTION_RESULTS_EMPTY);
                    }
                } else {
                    connectionResult.set(response.code());
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                status.setLoadComplete(true);
                connectionResult.set(CONNECTION_FAILURE);
            }
        });

    }

    private void loadStandings() {
        String url = StandingsResultsManager.getUrl(seriesId);
        if (url == null || url.isEmpty())
            return;

        APIManager.getApi().getHtmlFrom(url).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                status.setLoadComplete(true);
                if (response.isSuccessful()) {
                    int count = decodeHtml(response.body(), seriesId);
                    if (count == 0) {
                        connectionResult.set(CONNECTION_STANDINGS_EMPTY);
                    }
                } else {
                    connectionResult.set(response.code());
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                status.setLoadComplete(true);
                connectionResult.set(CONNECTION_FAILURE);
            }
        });
    }

    private int decodeHtml(ResponseBody responseBody, int seriesId) {
        if (responseBody == null)
            return 0;

        try {
            String html = responseBody.string();
            ArrayList<EventResultUnit> htmlResults = StandingsResultsManager.getStandings(html, seriesId);

            if (htmlResults == null || htmlResults.isEmpty()) {
                return 0;
            }

            results.clear();
            results.addAll(htmlResults);
            return results.size();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return 0;
    }

    //// TODO: 18/08/2017 improve
    private int decodeHtmlRace(ResponseBody responseBody, int seriesId) {
        if (responseBody == null)
            return 0;

        try {
            String html = responseBody.string();
            ArrayList<EventResultUnit> htmlResults = RaceResultsManager.getAutoSportResult(html);

            if (htmlResults == null || htmlResults.isEmpty()) {
                // TODO: 23/07/2017 raise firebase exception to warn me
                return 0;
            }

            results.clear();
            results.addAll(htmlResults);
            return results.size();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return 0;
    }

    public boolean isRace() {
        return raceName != null && !raceName.isEmpty();
    }
}
