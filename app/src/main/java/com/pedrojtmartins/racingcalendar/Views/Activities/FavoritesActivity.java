package com.pedrojtmartins.racingcalendar.Views.Activities;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.pedrojtmartins.racingcalendar.Adapters.RecyclerViews.SeriesSelectionAdapter;
import com.pedrojtmartins.racingcalendar.Database.DatabaseManager;
import com.pedrojtmartins.racingcalendar.Helpers.AlertDialogHelper;
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
        initToolBar();
        initRecyclerView();
    }

    private void initViewModel() {
        DatabaseManager databaseManager = DatabaseManager.getInstance(this);
        mViewModel = new FavoritesViewModel(databaseManager);
    }
    private void initToolBar() {
        setSupportActionBar(mBinding.toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(R.string.maintab_favourites);
        }
    }
    private void initRecyclerView() {
        SeriesSelectionAdapter adapter = new SeriesSelectionAdapter(R.layout.row_series_selection, mViewModel.getSeries());
        mBinding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mBinding.recyclerView.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.favorites, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save_favorites:
                saveFavorites();
                goBack(true);
                break;

            default:
                onBackPressed();
        }

        return true;
    }


    @Override
    public void onBackPressed() {
        discardChanges();
    }
    private void discardChanges() {
        // TODO: 14/02/2017 Check if something really changed before showing the dialog.
        AlertDialogHelper.displayYesNoDialog(this,
                R.string.discardFavorites,
                R.string.discard,
                R.string.cancel,
                new Handler(new Handler.Callback() {
                    @Override
                    public boolean handleMessage(Message message) {
                        if (message.what == 1) {
                            goBack(false);
                        }

                        return true;
                    }
                }));
    }
    private void goBack(boolean reloadList) {
        if (reloadList) {
            //This will make the list update
            Intent returnIntent = getIntent();
            setResult(RESULT_OK, returnIntent);
        }

        finish();
    }

    private void saveFavorites() {
        mViewModel.saveFavorites();
    }

//    private int getNumberOfRows() {
//        Display display = getWindowManager().getDefaultDisplay();
//        DisplayMetrics outMetrics = new DisplayMetrics();
//        display.getMetrics(outMetrics);
//
//        float density = getResources().getDisplayMetrics().density;
//        int dpHeight = (int) (outMetrics.heightPixels / density);
//        int dpWidth = (int) (outMetrics.widthPixels / density);
//
//        int dp = dpHeight < dpWidth ? dpHeight : dpWidth;
//        return dp / 90;
//    }
}
