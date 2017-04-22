package com.pedrojtmartins.racingcalendar.viewModels;

import android.databinding.ObservableArrayList;

import com.pedrojtmartins.racingcalendar.database.DatabaseManager;
import com.pedrojtmartins.racingcalendar.models.RCNotification;

import java.util.ArrayList;

/**
 * Pedro Martins
 * 22/04/2017
 */

public class NotificationsViewModel {
    private final DatabaseManager mDatabaseManager;

    private ObservableArrayList<RCNotification> mNotifications;
    private ArrayList<RCNotification> toDelete;

    public ObservableArrayList<RCNotification> getNotifications() {
        return mNotifications;
    }

    public NotificationsViewModel(DatabaseManager databaseManager) {
        mDatabaseManager = databaseManager;

        mNotifications = new ObservableArrayList<>();
        toDelete = new ArrayList<>();

        loadAllNotificationsFromDb();
    }

    private void loadAllNotificationsFromDb() {
        mNotifications.clear();
        mNotifications.addAll(mDatabaseManager.getNotifications());
    }

    public boolean deleteNotification(RCNotification notification) {
        if (toDelete.contains(notification)) {
            toDelete.remove(notification);
            return false;
        } else {
            toDelete.add(notification);
            return true;
        }
    }

    public int saveChanges() {
        return mDatabaseManager.removeNotifications(toDelete);
    }

    public boolean somethingToDelete() {
        return toDelete.size() > 0;
    }
}
