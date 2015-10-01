package edu.udacity.android.popularmovies;

import android.app.Application;
import android.content.Context;

import edu.udacity.android.popularmovies.util.AndroidUtils;
import edu.udacity.android.popularmovies.util.Constants;

public class PopularMoviesApplication extends Application {
    private String activeSortPreference;
    private boolean sortPreferenceChanged;
    private int numberOfColumnsInGrid;

    @Override
    public void onCreate() {
        super.onCreate();

        Context context = getApplicationContext();

        if (AndroidUtils.isTablet(context)) {
            numberOfColumnsInGrid = Constants.TABLET_COLUMN_COUNT;
        } else {
            numberOfColumnsInGrid = Constants.MOBILE_COLUMN_COUNT;
        }

        activeSortPreference = AndroidUtils.readSortOrderFromPreference(context);
    }

    public synchronized String getActiveSortPreference() {
        return activeSortPreference;
    }

    public synchronized void setActiveSortPreference(String activeSortPreference) {
        this.activeSortPreference = activeSortPreference;
    }

    public synchronized boolean isSortPreferenceChanged() {
        return sortPreferenceChanged;
    }

    public synchronized void setSortPreferenceChanged(boolean sortPreferenceChanged) {
        this.sortPreferenceChanged = sortPreferenceChanged;
    }

    public synchronized void clearSortPreferenceChanged() {
        setSortPreferenceChanged(false);
    }

    public synchronized int getNumberOfColumnsInGrid() {
        return numberOfColumnsInGrid;
    }
}
