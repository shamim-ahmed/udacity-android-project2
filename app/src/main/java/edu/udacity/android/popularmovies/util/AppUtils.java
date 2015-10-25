package edu.udacity.android.popularmovies.util;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.Cursor;
import android.preference.PreferenceManager;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.IOException;
import java.util.List;
import java.util.Properties;

import edu.udacity.android.popularmovies.R;
import edu.udacity.android.popularmovies.db.PopularMoviesContract;
import edu.udacity.android.popularmovies.listener.MovieTrailerOnClickListener;
import edu.udacity.android.popularmovies.model.Movie;
import edu.udacity.android.popularmovies.model.MovieReview;
import edu.udacity.android.popularmovies.model.MovieTrailer;

public class AppUtils {
    private static final String TAG = AppUtils.class.getSimpleName();

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
        values.put(PopularMoviesContract.MovieEntry.COLUMN_MOVIE_ID, cursor.getLong(movieIdIndex));
        values.put(PopularMoviesContract.MovieEntry.COLUMN_TITLE, cursor.getString(titleIndex));

        if (!cursor.isNull(releaseDateIndex)) {
            String releaseDate = cursor.getString(releaseDateIndex);
            values.put(PopularMoviesContract.MovieEntry.COLUMN_RELEASE_DATE, releaseDate);
        }

        if (!cursor.isNull(posterUriIndex)) {
            String uriString = cursor.getString(posterUriIndex);
            values.put(PopularMoviesContract.MovieEntry.COLUMN_POSTER_URI, uriString);
        }

        if (!cursor.isNull(voteAverageIndex)) {
            Double voteAverage = cursor.getDouble(voteAverageIndex);
            values.put(PopularMoviesContract.MovieEntry.COLUMN_VOTE_AVERAGE, voteAverage);
        }

        if (!cursor.isNull(synopsisIndex)) {
            String synopsis = cursor.getString(synopsisIndex);
            values.put(PopularMoviesContract.MovieEntry.COLUMN_SYNOPSIS, synopsis);
        }

        return values;
    }

    public static void displayTrailersForMovie(Movie movie, Activity activity) {
        LinearLayout linearLayout = (LinearLayout) activity.findViewById(R.id.movie_trailers);

        // NOTE : occasionally an NPE is encourntered when the user tries to select
        // a movie from the grid while the screen is getting rotated. In order to prevent
        // the NPE, we include a check.
        if (linearLayout == null) {
            return;
        }

        LayoutInflater inflater = activity.getLayoutInflater();
        List<MovieTrailer> trailerList = movie.getTrailerList();

        if (trailerList.size() > 0) {
            View titleView = inflater.inflate(R.layout.movie_trailers_title, linearLayout, false);
            linearLayout.addView(titleView);
        }

        for (MovieTrailer trailer : trailerList) {
            View view = inflater.inflate(R.layout.movie_trailer, linearLayout, false);
            Button trailerButton = (Button) view.findViewById(R.id.movie_trailer_item);
            trailerButton.setText(trailer.getName());
            trailerButton.setOnClickListener(new MovieTrailerOnClickListener(activity, trailer.getKey()));
            linearLayout.addView(view);
        }
    }

    public static void displayReviewsForMovie(Movie movie, Activity activity) {
        LinearLayout linearLayout = (LinearLayout) activity.findViewById(R.id.movie_reviews);

        // NOTE : occasionally an NPE is encourntered when the user tries to select
        // a movie from the grid while the screen is getting rotated. In order to prevent
        // the NPE, we include a check.
        if (linearLayout == null) {
            return;
        }

        LayoutInflater inflater = activity.getLayoutInflater();
        List<MovieReview> reviewList = movie.getReviewList();

        if (reviewList.size() > 0) {
            View titleView = inflater.inflate(R.layout.movie_reviews_title, linearLayout, false);
            linearLayout.addView(titleView);
        }

        for (MovieReview review : reviewList) {
            View view = inflater.inflate(R.layout.movie_review, linearLayout, false);
            TextView reviewContentView = (TextView) view.findViewById(R.id.movie_review_content);
            reviewContentView.setText(Html.fromHtml(review.toString()));
            linearLayout.addView(view);
        }
    }

    // private constructor to prevent instantiation
    private AppUtils() {
    }
}
