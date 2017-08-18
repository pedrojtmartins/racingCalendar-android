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

        logFirebase();
    }

    private void logFirebase() {
        if (viewModel.raceName == null || viewModel.raceName.isEmpty()) {
            FirebaseManager.logEvent(this, FirebaseManager.EVENT_ACTION_OPEN_SERIES_RESULTS);
        } else {
            FirebaseManager.logEvent(this, FirebaseManager.EVENT_ACTION_OPEN_RACE_RESULTS);
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
                    Handler handler = new Handler(new Handler.Callback() {
                        @Override
                        public boolean handleMessage(Message msg) {
                            ((Activity) ctx).onBackPressed();
                            return true;
                        }
                    });
                    AlertDialogHelper.displayOkDialog((Context) ctx, R.string.errorLoadingStandings, handler);
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

        if (viewModel.raceName == null || viewModel.raceName.isEmpty()) {
            binding.resultsSubtitle.setText(getString(R.string.standings));
            binding.resultsTableHeader.points.setVisibility(View.VISIBLE);
        } else {
            binding.resultsSubtitle.setText(viewModel.raceName);
            binding.resultsTableHeader.points.setVisibility(View.GONE);
        }

    }

    private void initRecyclerView() {
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerView.setAdapter(new ResultsAdapter(R.layout.row_results, viewModel.results));
    }
}
