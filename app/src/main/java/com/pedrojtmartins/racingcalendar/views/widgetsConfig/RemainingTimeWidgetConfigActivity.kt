package com.pedrojtmartins.racingcalendar.views.widgetsConfig

import android.app.Activity
import android.appwidget.AppWidgetManager
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.pedrojtmartins.racingcalendar.R
import com.pedrojtmartins.racingcalendar.sharedPreferences.SharedPreferencesManager
import com.pedrojtmartins.racingcalendar.widgetProviders.RemainingTimeWidget


class RemainingTimeWidgetConfigActivity : AppCompatActivity() {

    private var appWidgetId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_remaining_time_widget_config)

        initAppWidget()
    }

    private fun initAppWidget() {
        if (intent.extras != null) {
            appWidgetId = intent.extras.getInt(
                    AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID)
        }

        if (appWidgetId == -1) {
            setResult(Activity.RESULT_CANCELED)
            finish()
        }
    }

    fun onClickSave(view: View) {
        saveConfiguration(5)
    }

    private fun saveConfiguration(seriesId: Int) {
        val spm = SharedPreferencesManager(this)
        spm.addWidgetConfig(appWidgetId, seriesId)

        updateWidget()

        val resultValue = Intent()
        resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
        setResult(Activity.RESULT_OK, resultValue)
        finish()
    }

    private fun updateWidget() {
        val appWidgetManager = AppWidgetManager.getInstance(this)
        RemainingTimeWidget.updateWidget(this, appWidgetId, appWidgetManager)
    }
}
