package edu.udacity.android.popularmovies;

import android.app.Application;
import android.content.Context;

import java.util.Properties;

import edu.udacity.android.popularmovies.util.AndroidUtils;
import edu.udacity.android.popularmovies.util.Constants;

public class PopularMoviesApplication extends Application {
    private String activeSortPreference;
    private boolean reloadFlag;
    private int numberOfColumnsInGrid;
    private Properties configProperties;

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
        configProperties = AndroidUtils.readConfiguration(context);
    }

    public synchronized String getActiveSortPreference() {
        return activeSortPreference;
    }

    public synchronized void setActiveSortPreference(String activeSortPreference) {
        this.activeSortPreference = activeSortPreference;
    }

    public synchronized boolean isReloadFlag() {
        return reloadFlag;
    }

    public synchronized void setReloadFlag(boolean reloadFlag) {
        this.reloadFlag = reloadFlag;
    }

    public synchronized void clearSortPreferenceChanged() {
        setReloadFlag(false);
    }

    public synchronized int getNumberOfColumnsInGrid() {
        return numberOfColumnsInGrid;
    }

    public String getConfigurationProperty(String key, String... substitutes) {
        String configValue = configProperties.getProperty(key);

        for (int i = 0; i < substitutes.length; i++) {
            configValue = configValue.replaceAll(String.format("\\{%d\\}", i), substitutes[i]);
        }

        return configValue;
    }
}
