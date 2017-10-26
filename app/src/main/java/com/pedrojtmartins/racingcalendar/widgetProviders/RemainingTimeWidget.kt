package com.pedrojtmartins.racingcalendar.widgetProviders

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.widget.RemoteViews
import com.pedrojtmartins.racingcalendar.R
import com.pedrojtmartins.racingcalendar.database.DatabaseManager
import com.pedrojtmartins.racingcalendar.helpers.DateFormatter
import com.pedrojtmartins.racingcalendar.models.Race
import com.pedrojtmartins.racingcalendar.sharedPreferences.SharedPreferencesManager


/**
 * Pedro Martins
 * 07/10/2017
 */
class RemainingTimeWidget : AppWidgetProvider() {
    override fun onUpdate(context: Context?, appWidgetManager: AppWidgetManager?, appWidgetIds: IntArray?) {
        if (context == null || appWidgetManager == null || appWidgetIds == null)
            return

        val count = appWidgetIds.size

        for (i in 0 until count) {
            val widgetId = appWidgetIds[i]
            updateWidget(context, widgetId, appWidgetManager)

//            val intent = Intent(context, RemainingTimeWidget::class.java)
//            intent.action = AppWidgetManager.ACTION_APPWIDGET_UPDATE
//            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds)
//            val pendingIntent = PendingIntent.getBroadcast(context,
//                    0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
//            remoteViews.setOnClickPendingIntent(R.id.actionButton, pendingIntent)
        }
    }

    companion object {
        fun updateWidget(context: Context, widgetId: Int, appWidgetManager: AppWidgetManager) {
//            val remoteViews =
//                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
//                        val options = appWidgetManager.getAppWidgetOptions(widgetId)
//                        val minWidth = options.getInt(AppWidgetManager.OPTION_APPWIDGET_MIN_WIDTH)
//                        val minHeight = options.getInt(AppWidgetManager.OPTION_APPWIDGET_MIN_HEIGHT)
//                        getRemoteViews(context, minWidth, minHeight)
//                    } else {
//                        getRemoteViews(context, 1, 1)
//                    }

            val remoteViews = RemoteViews(context.packageName, R.layout.widget_remaining_time_normal)
            val seriesId = SharedPreferencesManager(context).getWidgetConfig(widgetId)
            val remaining = DatabaseManager.getInstance(context).getRaces(seriesId)

            val size = remaining?.size ?: 0
            if (size > 0) {
                val built = buildRemoteView(remaining[0], remoteViews, context, appWidgetManager, widgetId)
                if (!built && size > 1)
                // In case the first race in the list was today (e.i in the past)
                // we should try to display the next one
                    buildRemoteView(remaining[1], remoteViews, context, appWidgetManager, widgetId)
            }
        }

        private fun buildRemoteView(race: Race, remoteViews: RemoteViews, context: Context, appWidgetManager: AppWidgetManager, widgetId: Int): Boolean {
            val fullDate = race.getFullDate(0)
            val hours = DateFormatter.hoursUntil(fullDate, false)

            if (hours <= 0)
                return false

            val value: String
            val unit: String
            val time: String

            if (!fullDate.contains("T") && DateFormatter.isToday(fullDate)) {
                // This race doesn't have any hour but it is starting today
                value = context.getString(R.string.today)
                unit = ""
                time = DateFormatter.getSimplifiedDate(fullDate)
            } else if (hours >= 24 || !fullDate.contains("T")) {
                // Still more than a day to go
                val daysVal = (DateFormatter.hoursUntil(fullDate, true) / 24).toInt()
                value = if (daysVal > 0) daysVal.toString() else "1"
                unit = context.getString(if (daysVal > 1) R.string.days else R.string.day)
                time = DateFormatter.getSimplifiedDate(fullDate)
            } else {
                // This race has a hour and will start in less than 24h
                value = hours.toString()
                unit = context.getString(if (hours > 1) R.string.hours else R.string.hour)
                time = DateFormatter.getHour(fullDate)
            }

            var seriesName = race.seriesName ?: ""
            if (seriesName.contains(" - "))
                seriesName = seriesName.split(" - ")[0]

            remoteViews.setTextViewText(R.id.wd_series, seriesName)
            remoteViews.setTextViewText(R.id.wd_value, value)
            remoteViews.setTextViewText(R.id.wd_unit, unit)
            remoteViews.setTextViewText(R.id.wd_time, time)

            appWidgetManager.updateAppWidget(widgetId, remoteViews)
            return true
        }

//        /**
//         * Determine appropriate view based on width provided.
//         *
//         * @param minWidth
//         * @param minHeight
//         * @return
//         */
//        private fun getRemoteViews(context: Context, minWidth: Int, minHeight: Int): RemoteViews {
//            val rows = getCellsForSize(minHeight)
////            val columns = getCellsForSize(minWidth)
//
////            if (rows > 1)
//            return RemoteViews(context.packageName, R.layout.widget_remaining_time_normal)
//
////            return RemoteViews(context.packageName, R.layout.widget_remaining_time_1x1)
//        }

//        /**
//         * Returns number of cells needed for given size of the widget.
//         *
//         * @param size Widget size in dp.
//         * @return Size in number of cells.
//         */
//        private fun getCellsForSize(size: Int): Int {
//            var n = 2
//            while (70 * n - 30 < size) {
//                ++n
//            }
//            return n - 1
//        }
    }


}