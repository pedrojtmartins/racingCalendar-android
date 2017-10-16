package com.pedrojtmartins.racingcalendar.models;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.google.gson.annotations.SerializedName;
import com.pedrojtmartins.racingcalendar.BR;
import com.pedrojtmartins.racingcalendar._settings.Settings;

/**
 * Pedro Martins
 * 24/04/2017
 */

public class RCSettings extends BaseObservable {
    @SerializedName("notif_remember")
    public boolean notificationsRemember;

    @SerializedName("notif_minBefore")
    private String notifMinutesBefore;
    public String getNotificationMinutesBefore() {
        return notifMinutesBefore;
    }
    public void setNotificationMinutesBefore(String notificationMinutesBefore) {
        if (notificationMinutesBefore.isEmpty())
            notificationMinutesBefore = "0";

        this.notifMinutesBefore = notificationMinutesBefore;
    }

    @SerializedName("links_inBrowser")
    public boolean openLinksInBrowser;

    @SerializedName("layout_miniAll")
    private boolean isMiniLayoutAllActive;
    @Bindable
    public boolean isMiniLayoutAllActive() {
        return isMiniLayoutAllActive;
    }
    public void setMiniLayoutAllActive(boolean miniLayoutAllActive) {
        isMiniLayoutAllActive = miniLayoutAllActive;
        notifyPropertyChanged(BR.miniLayoutAllActive);
    }

    @SerializedName("layout_miniFav")
    private boolean isMiniLayoutFavActive;
    @Bindable
    public boolean isMiniLayoutFavActive() {
        return isMiniLayoutFavActive;
    }
    public void setMiniLayoutFavActive(boolean miniLayoutFavActive) {
        isMiniLayoutFavActive = miniLayoutFavActive;
        notifyPropertyChanged(BR.miniLayoutFavActive);
    }

    @SerializedName("layout_miniSer")
    private boolean isMiniLayoutSeriesActive;
    @Bindable
    public boolean isMiniLayoutSeriesActive() {
        return isMiniLayoutSeriesActive;
    }
    public void setMiniLayoutSeriesActive(boolean miniLayoutSeriesActive) {
        isMiniLayoutSeriesActive = miniLayoutSeriesActive;
        notifyPropertyChanged(BR.miniLayoutSeriesActive);
    }

    private boolean originalMiniLayoutAllActive;
    private boolean originalMiniLayoutFavActive;
    private boolean originalMiniLayoutSerActive;

    @SerializedName("show_other_years")
    private boolean showOtherYears;
    @Bindable
    public boolean isShowOtherYears() {
        return showOtherYears;
    }
    public void setShowOtherYears(boolean showOtherYears) {
        this.showOtherYears = showOtherYears;
        notifyPropertyChanged(BR.showOtherYears);
    }

    private boolean originalShowOtherYears;

    public RCSettings(String serialized) {
        setDefaults();

        if (serialized != null && !serialized.isEmpty() && serialized.contains(";")) {
            // This will only be called if settings are still stored as plain text (old version)

            String[] aSettings = serialized.split(";");
            if (aSettings.length > 0)
                notificationsRemember = aSettings[0].equals("1");

            if (aSettings.length > 1)
                notifMinutesBefore = aSettings[1];

            if (aSettings.length > 2)
                openLinksInBrowser = aSettings[2].equals("1");

            if (aSettings.length > 3) {
                originalMiniLayoutAllActive = isMiniLayoutAllActive = aSettings[3].equals("1");
            }

//            if (aSettings.length > 4)
//                isMiniLayoutFavActive = aSettings[4].equals("1");
//
//            if (aSettings.length > 5)
//                isMiniLayoutSeriesActive = aSettings[5].equals("1");
        }
    }

    public void normalize() {
        originalMiniLayoutAllActive = isMiniLayoutAllActive;
        originalMiniLayoutFavActive = isMiniLayoutFavActive;
        originalMiniLayoutSerActive = isMiniLayoutSeriesActive;
        originalShowOtherYears = showOtherYears;
    }

    private void setDefaults() {
        notificationsRemember = Settings.NOTIFICATION_REMEMBER;
        notifMinutesBefore = Settings.NOTIFICATION_MINUTES_BEFORE;
        openLinksInBrowser = Settings.OPEN_LINK_IN_BROWSER;

        originalShowOtherYears = showOtherYears = Settings.SHOW_OTHER_SERIES_YEARS;
        originalMiniLayoutAllActive = isMiniLayoutAllActive = Settings.IS_MINI_LAYOUT_ALL_ACTIVE;
        originalMiniLayoutFavActive = isMiniLayoutFavActive = Settings.IS_MINI_LAYOUT_FAV_ACTIVE;
        originalMiniLayoutSerActive = isMiniLayoutSeriesActive = Settings.IS_MINI_LAYOUT_SERIES_ACTIVE;
    }

    @Override
    public String toString() {
        return (notificationsRemember ? "1" : "0") +
                ";" + notifMinutesBefore +
                ";" + (openLinksInBrowser ? "1" : "0") +
                ";" + (isMiniLayoutAllActive ? "1" : "0");
    }

    public boolean isMiniLayoutActive() {
        return isMiniLayoutAllActive || isMiniLayoutFavActive || isMiniLayoutSeriesActive;
    }

    public boolean miniLayoutChanged() {
        return originalMiniLayoutAllActive != isMiniLayoutAllActive ||
                originalMiniLayoutFavActive != isMiniLayoutFavActive ||
                originalMiniLayoutSerActive != isMiniLayoutSeriesActive;
    }

    public boolean showPreviousYearsChanged() {
        return showOtherYears != originalShowOtherYears;
    }
}
