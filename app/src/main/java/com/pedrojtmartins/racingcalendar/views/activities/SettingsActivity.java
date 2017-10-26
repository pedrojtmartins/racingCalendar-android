package com.pedrojtmartins.racingcalendar.views.activities;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.app.AlarmManager;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.widget.CompoundButton;
import android.widget.LinearLayout;

import com.pedrojtmartins.racingcalendar.R;
import com.pedrojtmartins.racingcalendar._settings.AnimationConstants;
import com.pedrojtmartins.racingcalendar.alarms.RCAlarmManager;
import com.pedrojtmartins.racingcalendar.database.DatabaseManager;
import com.pedrojtmartins.racingcalendar.databinding.ActivitySettingsBinding;
import com.pedrojtmartins.racingcalendar.firebase.FirebaseManager;
import com.pedrojtmartins.racingcalendar.sharedPreferences.SharedPreferencesManager;
import com.pedrojtmartins.racingcalendar.viewModels.SettingsViewModel;

public class SettingsActivity extends AppCompatActivity {

    private ActivitySettingsBinding binding;
    private SettingsViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_settings);
        viewModel = new SettingsViewModel(new SharedPreferencesManager(this));
        binding.setData(viewModel.mSettings);

        initToolBar();
        initListeners();

        FirebaseManager.logEvent(this, FirebaseManager.EVENT_ACTIVITY_SETTINGS);
    }

    @Override
    public void onBackPressed() {
        viewModel.saveChanges();
        setupIntentResult();

        super.onBackPressed();
    }

    void setupIntentResult() {
        if (viewModel.needsLayoutUpdate()) {
            getIntent().putExtra("update_layout", true);
            setResult(RESULT_OK, getIntent());

        }
        if (viewModel.needsSeriesUpdate()) {
            getIntent().putExtra("reload_series", true);
            setResult(RESULT_OK, getIntent());
        }

        if (viewModel.needsWeeklyNotificationUpdate()) {
            final DatabaseManager db = DatabaseManager.getInstance(this);
            boolean newState = viewModel.mSettings.isWeeklyNotification();

            if (newState) {
                final AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                RCAlarmManager.resetWeeklyAlarm(this, am, db, viewModel.mSettings);
            } else {
                RCAlarmManager.removePendingWeeklyNotifications(db);
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        onBackPressed();
        return true;
    }

    private void initToolBar() {
        setSupportActionBar(binding.toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(R.string.maintab_settings);
        }
    }

    private void initListeners() {
        binding.miniLayoutActiveSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                if (checked) showLayoutSettings();
                else hideLayoutSettings();
            }
        });
    }

    private void showLayoutSettings() {
        if (binding.miniLayoutsettings.getVisibility() == View.VISIBLE)
            return;

        viewModel.setMiniLayoutActive(true);

        binding.miniLayoutsettings.measure(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        final int targetHeight = binding.miniLayoutsettings.getMeasuredHeight();

        ValueAnimator anim = ValueAnimator.ofInt(0, targetHeight);
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                ViewGroup.LayoutParams layoutParams = binding.miniLayoutsettings.getLayoutParams();
                layoutParams.height = (Integer) valueAnimator.getAnimatedValue();
                binding.miniLayoutsettings.setLayoutParams(layoutParams);
            }
        });
        anim.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
                binding.miniLayoutsettings.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animator animator) {
            }

            @Override
            public void onAnimationCancel(Animator animator) {
            }

            @Override
            public void onAnimationRepeat(Animator animator) {
            }
        });
        anim.setDuration(AnimationConstants.EXPAND_ANIM_MS);
        anim.setInterpolator(new AccelerateInterpolator());
        anim.start();
    }

    private void hideLayoutSettings() {
        if (binding.miniLayoutsettings.getVisibility() == View.GONE)
            return;

        viewModel.setMiniLayoutActive(false);

        binding.miniLayoutsettings.measure(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        final int targetHeight = binding.miniLayoutsettings.getMeasuredHeight();

        ValueAnimator anim = ValueAnimator.ofInt(targetHeight, 0);
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                ViewGroup.LayoutParams layoutParams = binding.miniLayoutsettings.getLayoutParams();
                layoutParams.height = (Integer) valueAnimator.getAnimatedValue();
                binding.miniLayoutsettings.setLayoutParams(layoutParams);
            }
        });
        anim.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
            }

            @Override
            public void onAnimationEnd(Animator animator) {
                binding.miniLayoutsettings.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationCancel(Animator animator) {
            }

            @Override
            public void onAnimationRepeat(Animator animator) {
            }
        });
        anim.setDuration(AnimationConstants.EXPAND_ANIM_MS);
        anim.setInterpolator(new AccelerateInterpolator());
        anim.start();
    }
}
