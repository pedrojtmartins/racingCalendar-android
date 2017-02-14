package com.pedrojtmartins.racingcalendar.Views.Activities;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;

import com.pedrojtmartins.racingcalendar.Adapters.RecyclerViews.SeriesAdapter;
import com.pedrojtmartins.racingcalendar.Database.DatabaseManager;
import com.pedrojtmartins.racingcalendar.R;
import com.pedrojtmartins.racingcalendar.ViewModels.FavoritesViewModel;
import com.pedrojtmartins.racingcalendar.databinding.ActivityFavoritesBinding;

public class FavoritesActivity extends AppCompatActivity {

    private FavoritesViewModel mViewModel;
    private ActivityFavoritesBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_favorites);

        initViewModel();
        initRecyclerView();
    }

    private void initViewModel() {
        DatabaseManager databaseManager = DatabaseManager.getInstance(this);
        mViewModel = new FavoritesViewModel(databaseManager);
    }
    private void initRecyclerView() {
        SeriesAdapter adapter = new SeriesAdapter(R.layout.row_series, mViewModel.getSeries());
        mBinding.recyclerView.setLayoutManager(new GridLayoutManager(this, 4));
        mBinding.recyclerView.setAdapter(adapter);
    }

    @Override
    public void onBackPressed() {
        mViewModel.saveFavourites();

        Intent returnIntent = getIntent();
        setResult(RESULT_OK, returnIntent);
        finish();
    }
}
