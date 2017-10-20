package com.pedrojtmartins.racingcalendar.views.activities;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;

import com.pedrojtmartins.racingcalendar.BuildConfig;
import com.pedrojtmartins.racingcalendar.R;
import com.pedrojtmartins.racingcalendar.databinding.ActivityAboutBinding;
import com.pedrojtmartins.racingcalendar.firebase.FirebaseManager;
import com.pedrojtmartins.racingcalendar.helpers.AppVersionHelper;
import com.pedrojtmartins.racingcalendar.helpers.IntentHelper;
import com.pedrojtmartins.racingcalendar.sharedPreferences.SharedPreferencesManager;

public class AboutActivity extends AppCompatActivity {

    private ActivityAboutBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_about);

        initToolBar();

        int dataVersion = new SharedPreferencesManager(this).getDataVersion();
        mBinding.aboutVersion.setText("v" + BuildConfig.VERSION_NAME);
        mBinding.aboutDataVersion.setText("v" + dataVersion);

        FirebaseManager.logEvent(this, FirebaseManager.EVENT_ACTIVITY_ABOUT);
    }

    private void initToolBar() {
        setSupportActionBar(mBinding.toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(R.string.maintab_about);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            default:
                onBackPressed();
        }

        return true;
    }

    public void onClick_sendEmail(View v) {
        IntentHelper.sendFeedback(this);
    }

    public void onClick_toStore(View v) {
        Intent intent = AppVersionHelper.getGooglePlayIntent(getPackageName(), getPackageManager());
        startActivity(intent);
    }

}
