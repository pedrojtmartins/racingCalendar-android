package com.pedrojtmartins.racingcalendar.views.activities;

import android.app.Activity;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.Observable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.view.MenuItem;
import android.view.View;

import com.pedrojtmartins.racingcalendar.R;
import com.pedrojtmartins.racingcalendar.adapters.recyclerViews.ResultsAdapter;
import com.pedrojtmartins.racingcalendar.alertDialog.AlertDialogHelper;
import com.pedrojtmartins.racingcalendar.databinding.ActivityResultsBinding;
import com.pedrojtmartins.racingcalendar.firebase.FirebaseManager;
import com.pedrojtmartins.racingcalendar.viewModels.ResultsViewModel;

import java.lang.ref.WeakReference;


public class ResultsActivity extends AppCompatActivity {

    private ActivityResultsBinding binding;
    private ResultsViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_results);

        viewModel = new ResultsViewModel(
                getIntent().getIntExtra("seriesId", -1),
                getIntent().getIntExtra("raceId", -1),
                getIntent().getIntExtra("raceNum", -1),
                getIntent().getStringExtra("seriesName"),
                getIntent().getStringExtra("raceName"));

        binding.setData(viewModel.status);

        if (!viewModel.loadResults()) {
            return;
            // TODO: 23/07/2017 display msg
        }

        registerForErrors();
        initToolBars();
        initRecyclerView();
        initHeader();

        logFirebase();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void logFirebase() {
        if (viewModel.isRace()) {
            FirebaseManager.logEvent(this, FirebaseManager.EVENT_ACTION_OPEN_RACE_RESULTS);
        } else {
            FirebaseManager.logEvent(this, FirebaseManager.EVENT_ACTION_OPEN_SERIES_RESULTS);
        }
    }

    private void registerForErrors() {
        // TODO: 01/08/2017 improve
        final WeakReference ref = new WeakReference<>(this);
        viewModel.connectionResult.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                final Object ctx = ref.get();
                if (ctx != null) {
                    AlertDialogHelper.displayOkDialog((Context) ctx,
                            R.string.errorLoadingStandings,
                            new Handler(new Handler.Callback() {
                                @Override
                                public boolean handleMessage(Message msg) {
                                    ((Activity) ctx).onBackPressed();
                                    return true;
                                }
                            }));
                }
            }
        });
    }

    private void initToolBars() {
        setSupportActionBar(binding.toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(viewModel.seriesName);
        }

        if (viewModel.isRace()) {
            binding.resultsSubtitle.setText(viewModel.raceName);
        } else {
            binding.resultsSubtitle.setText(getString(R.string.standings));
        }
    }

    private void initRecyclerView() {
        int layout = viewModel.isRace() ? R.layout.row_results_race : R.layout.row_results_standings;

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerView.setAdapter(new ResultsAdapter(layout, viewModel.results));
    }

    private void initHeader() {
        View header;
        if (viewModel.isRace()) {
            header = getLayoutInflater().inflate(R.layout.row_results_race_header,
                    binding.resultsTableHeaderParent,
                    false);
        } else {
            header = getLayoutInflater().inflate(R.layout.row_results_standings_header,
                    binding.resultsTableHeaderParent,
                    false);
        }

        binding.resultsTableHeaderParent.addView(header);
    }
}
