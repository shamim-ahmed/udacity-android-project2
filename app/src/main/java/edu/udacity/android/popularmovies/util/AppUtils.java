package edu.udacity.android.popularmovies.util;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.ShareActionProvider;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

import edu.udacity.android.popularmovies.PopularMoviesApplication;
import edu.udacity.android.popularmovies.R;
import edu.udacity.android.popularmovies.ShareMenuItemAware;
import edu.udacity.android.popularmovies.db.PopularMoviesContract;
import edu.udacity.android.popularmovies.listener.MovieTrailerOnClickListener;
import edu.udacity.android.popularmovies.model.Movie;
import edu.udacity.android.popularmovies.model.Review;
import edu.udacity.android.popularmovies.model.Trailer;

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

    public static ContentValues readMovieFromCursor(Cursor cursor) {
        int movieIdIndex = cursor.getColumnIndex(PopularMoviesContract.MovieEntry.COLUMN_MOVIE_ID);
        int titleIndex = cursor.getColumnIndex(PopularMoviesContract.MovieEntry.COLUMN_TITLE);
        int releaseDateIndex = cursor.getColumnIndex(PopularMoviesContract.MovieEntry.COLUMN_RELEASE_DATE);
        int posterUriIndex = cursor.getColumnIndex(PopularMoviesContract.MovieEntry.COLUMN_POSTER_URI);
        int voteAverageIndex = cursor.getColumnIndex(PopularMoviesContract.MovieEntry.COLUMN_VOTE_AVERAGE);
        int synopsisIndex = cursor.getColumnIndex(PopularMoviesContract.MovieEntry.COLUMN_SYNOPSIS);

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

    public static ContentValues readPosterFromCursor(Cursor cursor) {
        int posterIdIndex = cursor.getColumnIndex(PopularMoviesContract.PosterEntry.COLUMN_POSTER_ID);
        int movieIdIndex = cursor.getColumnIndex(PopularMoviesContract.PosterEntry.COLUMN_MOVIE_ID);
        int contentIndex = cursor.getColumnIndex(PopularMoviesContract.PosterEntry.COLUMN_CONTENT);

        ContentValues values = new ContentValues();
        values.put(PopularMoviesContract.PosterEntry.COLUMN_POSTER_ID, cursor.getString(posterIdIndex));
        values.put(PopularMoviesContract.PosterEntry.COLUMN_MOVIE_ID, cursor.getLong(movieIdIndex));
        values.put(PopularMoviesContract.PosterEntry.COLUMN_CONTENT, cursor.getBlob(contentIndex));

        return values;
    }

    public static ContentValues readTrailerFromCursor(Cursor cursor) {
        int trailerIdIndex = cursor.getColumnIndex(PopularMoviesContract.TrailerEntry.COLUMN_TRAILER_ID);
        int trailerKeyIndex = cursor.getColumnIndex(PopularMoviesContract.TrailerEntry.COLUMN_TRAILER_KEY);
        int trailerNameIndex = cursor.getColumnIndex(PopularMoviesContract.TrailerEntry.COLUMN_TRAILER_NAME);
        int trailerSiteIndex = cursor.getColumnIndex(PopularMoviesContract.TrailerEntry.COLUMN_TRAILER_SITE);
        int movieIdIndex = cursor.getColumnIndex(PopularMoviesContract.TrailerEntry.COLUMN_MOVIE_ID);

        ContentValues values = new ContentValues();
        values.put(PopularMoviesContract.TrailerEntry.COLUMN_TRAILER_ID, cursor.getString(trailerIdIndex));
        values.put(PopularMoviesContract.TrailerEntry.COLUMN_TRAILER_KEY, cursor.getString(trailerKeyIndex));
        values.put(PopularMoviesContract.TrailerEntry.COLUMN_MOVIE_ID, cursor.getLong(movieIdIndex));

        if (!cursor.isNull(trailerNameIndex)) {
            values.put(PopularMoviesContract.TrailerEntry.COLUMN_TRAILER_NAME, cursor.getString(trailerNameIndex));
        }

        if (!cursor.isNull(trailerSiteIndex)) {
            values.put(PopularMoviesContract.TrailerEntry.COLUMN_TRAILER_SITE, cursor.getString(trailerSiteIndex));
        }

        return values;
    }

    public static ContentValues readReviewFromCursor(Cursor cursor) {
        int reviewIdIndex = cursor.getColumnIndex(PopularMoviesContract.ReviewEntry.COLUMN_REVIEW_ID);
        int movieIdIndex = cursor.getColumnIndex(PopularMoviesContract.ReviewEntry.COLUMN_MOVIE_ID);
        int authorIndex = cursor.getColumnIndex(PopularMoviesContract.ReviewEntry.COLUMN_AUTHOR);
        int contentIndex = cursor.getColumnIndex(PopularMoviesContract.ReviewEntry.COLUMN_CONTENT);

        ContentValues values = new ContentValues();
        values.put(PopularMoviesContract.ReviewEntry.COLUMN_REVIEW_ID, cursor.getString(reviewIdIndex));
        values.put(PopularMoviesContract.ReviewEntry.COLUMN_MOVIE_ID, cursor.getLong(movieIdIndex));
        values.put(PopularMoviesContract.ReviewEntry.COLUMN_AUTHOR, cursor.getString(authorIndex));
        values.put(PopularMoviesContract.ReviewEntry.COLUMN_CONTENT, cursor.getString(contentIndex));

        return values;
    }

    public static void displayTrailersForMovie(Movie movie, Activity activity) {
        LinearLayout linearLayout = (LinearLayout) activity.findViewById(R.id.movie_trailers);

        // NOTE : occasionally an NPE is encountered when the user tries to select
        // a movie from the grid while the screen is getting rotated. In order to prevent
        // the NPE, we include a check.
        if (linearLayout == null) {
            return;
        }

        LayoutInflater inflater = activity.getLayoutInflater();
        List<Trailer> trailerList = movie.getTrailerList();

        if (trailerList.size() > 0) {
            View titleView = inflater.inflate(R.layout.movie_trailers_title, linearLayout, false);
            linearLayout.addView(titleView);
        }

        for (Trailer trailer : trailerList) {
            View view = inflater.inflate(R.layout.movie_trailer, linearLayout, false);
            Button trailerButton = (Button) view.findViewById(R.id.movie_trailer_item);
            trailerButton.setText(trailer.getName());
            trailerButton.setOnClickListener(new MovieTrailerOnClickListener(activity, trailer.getKey()));
            linearLayout.addView(view);
        }
    }

    public static void updateShareMenuItem(String trailerTitle, String trailerKey, Activity activity) {
        MenuItem shareMenuItem = ((ShareMenuItemAware) activity).getShareMenuItem();

        if (shareMenuItem != null) {
            PopularMoviesApplication application = (PopularMoviesApplication) activity.getApplication();
            ShareActionProvider shareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(shareMenuItem);
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_SUBJECT, trailerTitle);
            Uri trailerUri = AppUtils.createTrailerUri(trailerKey, application);
            intent.putExtra(Intent.EXTRA_TEXT, trailerUri.toString());
            shareActionProvider.setShareIntent(intent);
            shareMenuItem.setEnabled(true);
        }
    }

    public static void disableShareMenuItem(Activity activity) {
        MenuItem shareMenuItem = ((ShareMenuItemAware) activity).getShareMenuItem();

        if (shareMenuItem != null) {
            ShareActionProvider shareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(shareMenuItem);
            shareActionProvider.setShareIntent(null);
            shareMenuItem.setEnabled(false);
        }
    }

    public static void displayReviewsForMovie(Movie movie, Activity activity) {
        LinearLayout linearLayout = (LinearLayout) activity.findViewById(R.id.movie_reviews);

        // NOTE : occasionally an NPE is encountered when the user tries to select
        // a movie from the grid while the screen is getting rotated. In order to prevent
        // the NPE, we include a check.
        if (linearLayout == null) {
            return;
        }

        LayoutInflater inflater = activity.getLayoutInflater();
        List<Review> reviewList = movie.getReviewList();

        if (reviewList.size() > 0) {
            View titleView = inflater.inflate(R.layout.movie_reviews_title, linearLayout, false);
            linearLayout.addView(titleView);
        }

        for (Review review : reviewList) {
            View view = inflater.inflate(R.layout.movie_review, linearLayout, false);
            TextView reviewContentView = (TextView) view.findViewById(R.id.movie_review_content);
            reviewContentView.setText(Html.fromHtml(review.toString()));
            linearLayout.addView(view);
        }
    }

    public static byte[] extractPosterContent(ImageView imageView) {
        Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, outputStream);
        return outputStream.toByteArray();
    }

    public static Uri createTrailerUri(String trailerKey, PopularMoviesApplication application) {
        String scheme = application.getConfigurationProperty("youtube.video.scheme");
        String authority = application.getConfigurationProperty("youtube.video.authority");
        String path = application.getConfigurationProperty("youtube.video.path");

        return new Uri.Builder()
                .scheme(scheme)
                .authority(authority)
                .path(path)
                .appendQueryParameter("v", trailerKey)
                .build();
    }

    // private constructor to prevent instantiation
    private AppUtils() {
    }
}
