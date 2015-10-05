package edu.udacity.android.popularmovies.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.preference.PreferenceManager;
import android.util.Log;

import java.io.IOException;
import java.util.Properties;

import edu.udacity.android.popularmovies.R;

public class AndroidUtils {
    private static final String TAG = AndroidUtils.class.getSimpleName();

    public static boolean isTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK)
                >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }

    public static String readSortOrderFromPreference(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString(Constants.SORT_PREFERENCE_KEY, Constants.SORT_PREFERENCE_DEFAULT_VALUE);
    }

    public static Properties readConfiguration(Context context) {
        Properties configProperties = new Properties();

        try {
            configProperties.load(context.getResources().openRawResource(R.raw.config));
        } catch (IOException ex) {
            Log.e(TAG, "Error while reading config.properties");
        }

        return configProperties;
    }

    // private constructor to prevent instantiation
    private AndroidUtils() {
    }
}
