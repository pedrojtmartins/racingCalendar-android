package com.pedrojtmartins.racingcalendar.views.activities;

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

import com.pedrojtmartins.racingcalendar.R;
import com.pedrojtmartins.racingcalendar.adapters.recyclerViews.NotificationsAdapter;
import com.pedrojtmartins.racingcalendar.alertDialog.AlertDialogHelper;
import com.pedrojtmartins.racingcalendar.database.DatabaseManager;
import com.pedrojtmartins.racingcalendar.databinding.ActivityNotificationsBinding;
import com.pedrojtmartins.racingcalendar.firebase.FirebaseManager;
import com.pedrojtmartins.racingcalendar.interfaces.fragments.INotificationsCallback;
import com.pedrojtmartins.racingcalendar.models.RCNotification;
import com.pedrojtmartins.racingcalendar.viewModels.NotificationsViewModel;

public class NotificationsActivity extends AppCompatActivity implements INotificationsCallback {

    private NotificationsViewModel mViewModel;
    private ActivityNotificationsBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_notifications);

        intiViewModel();
        initToolBar();

        long focusRace = getIntent().getIntExtra("raceId", 0);
        initRecyclerView(focusRace);

        FirebaseManager.logEvent(this, FirebaseManager.EVENT_ACTIVITY_NOTIFICATIONS);

    }

    private void discardChanges() {
        if (mViewModel.somethingToDelete()) {
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
        } else {
            goBack(false);
        }
    }

    private void intiViewModel() {
        mViewModel = new NotificationsViewModel(DatabaseManager.getInstance(this));
    }

    private void initToolBar() {
        setSupportActionBar(mBinding.toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(R.string.maintab_notifications);
        }
    }

    private void initRecyclerView(long focusRace) {
        NotificationsAdapter adapter = new NotificationsAdapter(R.layout.row_notification, mViewModel.getNotifications(), this,focusRace);
        mBinding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mBinding.recyclerView.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.notifications, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_all_done:
                int total = mViewModel.saveChanges();
                FirebaseManager.logEvent(this, FirebaseManager.EVENT_ACTION_REMOVE_NOTIFICATION, total);
                goBack(total > 0);
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

    private void goBack(boolean reloadList) {
        if (reloadList) {
            Intent returnIntent = getIntent();
            setResult(RESULT_OK, returnIntent);
        }

        finish();
    }

    @Override
    public boolean deleteNotification(RCNotification notification) {
        return mViewModel.deleteNotification(notification);
    }
}
