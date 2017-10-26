package com.pedrojtmartins.racingcalendar.alarms;

import android.app.AlarmManager;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.JobIntentService;
import android.util.Log;

import com.pedrojtmartins.racingcalendar.database.DatabaseManager;
import com.pedrojtmartins.racingcalendar.models.RCSettings;
import com.pedrojtmartins.racingcalendar.sharedPreferences.SharedPreferencesManager;

/**
 * Pedro Martins
 * 10/09/2017
 */

public class RCAlarmResetService extends JobIntentService {
    static final int JOB_ID = 14782;

    /**
     * Convenience method for enqueuing work in to this service.
     */
    static void enqueueWork(Context context, Intent work) {
        enqueueWork(context, RCAlarmResetService.class, JOB_ID, work);
    }

    @Override
    protected void onHandleWork(@NonNull Intent intent) {
        Log.i("debug", "RCAlarmResetService: onHandleWork");

        final DatabaseManager db = DatabaseManager.getInstance(this);
        final AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        final RCSettings settings = new SharedPreferencesManager(this).getSettings();

        RCAlarmManager.resetRaceAlarms(this, am, db);
        RCAlarmManager.resetWeeklyAlarm(this, am, db, settings);
    }
}
