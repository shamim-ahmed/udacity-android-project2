package edu.udacity.android.popularmovies.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.preference.PreferenceManager;

public class AndroidUtils {
    public static boolean isTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK)
                >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }

    public static String readSortOrderFromPreference(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString(Constants.SORT_PREFERENCE_KEY, Constants.SORT_PREFERENCE_DEFAULT_VALUE);
    }

    // private constructor to prevent instantiation
    private AndroidUtils() {
    }
}
