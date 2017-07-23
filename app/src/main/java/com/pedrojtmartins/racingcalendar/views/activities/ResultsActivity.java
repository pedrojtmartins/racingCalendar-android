package com.pedrojtmartins.racingcalendar.views.activities;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;

import com.pedrojtmartins.racingcalendar.R;
import com.pedrojtmartins.racingcalendar.adapters.recyclerViews.ResultsAdapter;
import com.pedrojtmartins.racingcalendar.databinding.ActivityResultsBinding;
import com.pedrojtmartins.racingcalendar.viewModels.ResultsViewModel;


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
                getIntent().getStringExtra("seriesName"));

        if (!viewModel.loadResults()) {
            return;
            // TODO: 23/07/2017 display msg
        }

        initToolBar();
        initRecyclerView();
    }

    private void initToolBar() {
        setSupportActionBar(binding.toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(R.string.standings);
        }
    }

    private void initRecyclerView() {
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerView.setAdapter(new ResultsAdapter(R.layout.row_results, viewModel.results));
    }
}
