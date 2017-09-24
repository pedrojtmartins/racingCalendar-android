package com.pedrojtmartins.racingcalendar.views.activities;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.pedrojtmartins.racingcalendar.R;
import com.pedrojtmartins.racingcalendar.databinding.ActivitySettingsBinding;
import com.pedrojtmartins.racingcalendar.firebase.FirebaseManager;
import com.pedrojtmartins.racingcalendar.sharedPreferences.SharedPreferencesManager;
import com.pedrojtmartins.racingcalendar.viewModels.SettingsViewModel;

public class SettingsActivity extends AppCompatActivity {

    private ActivitySettingsBinding mBinding;
    private SettingsViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_settings);
        mViewModel = new SettingsViewModel(new SharedPreferencesManager(this));
        mBinding.setData(mViewModel.mSettings);

        initToolBar();

        FirebaseManager.logEvent(this, FirebaseManager.EVENT_ACTIVITY_SETTINGS);
    }

    @Override
    public void onBackPressed() {
        if (mViewModel.saveChanges()) {
            setResult(RESULT_OK, getIntent());
        }

        super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            default:
                onBackPressed();
        }

        return true;
    }

    private void initToolBar() {
        setSupportActionBar(mBinding.toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(R.string.maintab_settings);
        }
    }
}
