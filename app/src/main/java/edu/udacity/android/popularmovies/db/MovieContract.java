package edu.udacity.android.popularmovies.db;

import android.net.Uri;
import android.provider.BaseColumns;

public class MovieContract {
    public static final String CONTENT_AUTHORITY = "edu.udacity.android.popularmovies";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_MOVIE = "movie";

    public static class MovieEntry implements BaseColumns {
        public static final String TABLE_NAME = "Movie";
        public static final String COLUMN_MOVIE_ID = "movie_id";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_RELEASE_DATE = "release_date";
        public static final String COLUMN_POSTER_URI = "poster_uri";
        public static final String COLUMN_VOTE_AVERAGE = "vote_average";
        public static final String COLUMN_SYNOPSIS = "synopsis";
    }
}