package edu.udacity.android.popularmovies.db;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;
import android.util.Log;

import java.util.List;

public class MovieContract {
    public static final String CONTENT_AUTHORITY = "edu.udacity.android.popularmovies";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_MOVIE = "movie";
    public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_MOVIE).build();
    public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + PATH_MOVIE;
    public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + PATH_MOVIE;

    public static class MovieEntry implements BaseColumns {
        private static final String TAG = MovieEntry.class.getSimpleName();
        public static final String TABLE_NAME = "Movie";
        public static final String COLUMN_MOVIE_ID = "movie_id";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_RELEASE_DATE = "release_date";
        public static final String COLUMN_POSTER_URI = "poster_uri";
        public static final String COLUMN_VOTE_AVERAGE = "vote_average";
        public static final String COLUMN_SYNOPSIS = "synopsis";

        public static Long getMovieIdFromUri(Uri uri) {
            Long result = null;
            List<String> segments =  uri.getPathSegments();

            if (segments.size() == 2) {
                try {
                    result = Long.valueOf(segments.get(1));
                } catch (NumberFormatException ex) {
                    Log.e(TAG, String.format("An error occurred while parsing movie id from the uri %s", uri.toString()));
                }
            }

            return result;
        }

        public static Uri buildMovieUri(Long movieId) {
            return CONTENT_URI.buildUpon().appendPath(movieId.toString()).build();
        }
    }
}