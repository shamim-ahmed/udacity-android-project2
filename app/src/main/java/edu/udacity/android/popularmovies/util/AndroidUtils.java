package edu.udacity.android.popularmovies.util;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.Cursor;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.util.Log;

import java.io.IOException;
import java.util.Properties;

import edu.udacity.android.popularmovies.R;
import edu.udacity.android.popularmovies.db.MovieContract;

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

    public static ContentValues readCursor(Cursor cursor) {
        int movieIdIndex = cursor.getColumnIndex("movie_id");
        int titleIndex = cursor.getColumnIndex("title");
        int releaseDateIndex = cursor.getColumnIndex("release_date");
        int posterUriIndex = cursor.getColumnIndex("poster_uri");
        int voteAverageIndex = cursor.getColumnIndex("vote_average");
        int synopsisIndex = cursor.getColumnIndex("synopsis");

        ContentValues values = new ContentValues();
        values.put(MovieContract.MovieEntry.COLUMN_MOVIE_ID, cursor.getLong(movieIdIndex));
        values.put(MovieContract.MovieEntry.COLUMN_TITLE, cursor.getString(titleIndex));

        if (!cursor.isNull(releaseDateIndex)) {
            String releaseDate = cursor.getString(releaseDateIndex);
            values.put(MovieContract.MovieEntry.COLUMN_RELEASE_DATE, releaseDate);
        }

        if (!cursor.isNull(posterUriIndex)) {
            String uriString = cursor.getString(posterUriIndex);
            values.put(MovieContract.MovieEntry.COLUMN_POSTER_URI, uriString);
        }

        if (!cursor.isNull(voteAverageIndex)) {
            Double voteAverage = cursor.getDouble(voteAverageIndex);
            values.put(MovieContract.MovieEntry.COLUMN_VOTE_AVERAGE, voteAverage);
        }

        if (!cursor.isNull(synopsisIndex)) {
            String synopsis = cursor.getString(synopsisIndex);
            values.put(MovieContract.MovieEntry.COLUMN_SYNOPSIS, synopsis);
        }

        return values;
    }

    // private constructor to prevent instantiation
    private AndroidUtils() {
    }
}
