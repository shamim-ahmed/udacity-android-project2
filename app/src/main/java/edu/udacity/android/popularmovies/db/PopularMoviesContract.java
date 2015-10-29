package edu.udacity.android.popularmovies.db;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;
import android.util.Log;

import java.util.List;

public class PopularMoviesContract {
    public static final String CONTENT_AUTHORITY = "edu.udacity.android.popularmovies";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static class MovieEntry implements BaseColumns {
        private static final String TAG = MovieEntry.class.getSimpleName();

        public static final String TABLE_NAME = "Movie";
        public static final String COLUMN_MOVIE_ID = "movie_id";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_RELEASE_DATE = "release_date";
        public static final String COLUMN_POSTER_URI = "poster_uri";
        public static final String COLUMN_VOTE_AVERAGE = "vote_average";
        public static final String COLUMN_SYNOPSIS = "synopsis";

        public static final String PATH_MOVIE = "movie";
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_MOVIE).build();
        public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + PATH_MOVIE;
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + PATH_MOVIE;

        public static Long getMovieIdFromUri(Uri uri) {
            List<String> segments = uri.getPathSegments();
            Long result = null;

            try {
                result = Long.valueOf(segments.get(1));
            } catch (NumberFormatException ex) {
                Log.e(TAG, String.format("An error occurred while parsing movie id from the uri %s", uri.toString()));
            }

            return result;
        }

        public static Uri buildMovieUri(Long movieId) {
            return MovieEntry.CONTENT_URI.buildUpon().appendPath(movieId.toString()).build();
        }
    }

    public static class PosterEntry implements BaseColumns {
        private static final String TAG = PosterEntry.class.getSimpleName();

        public static final String TABLE_NAME = "Poster";
        public static final String COLUMN_POSTER_ID = "poster_id";
        public static final String COLUMN_MOVIE_ID = "movie_id";
        public static final String COLUMN_CONTENT = "content";

        public static final String PATH_POSTER = "poster";
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_POSTER).build();
        public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + PATH_POSTER;
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + PATH_POSTER;

        public static Long getMovieIdFromUri(Uri uri) {
            List<String> segmentList = uri.getPathSegments();

            if (segmentList.size() < 2) {
                return null;
            }

            Long result = null;

            try {
                result = Long.parseLong(segmentList.get(1));
            } catch (NumberFormatException ex) {
                Log.e(TAG, String.format("An error occurred while parsing movie id from the uri %s", uri.toString()));
            }

            return result;
        }

        public static String getPosterIdFromUri(Uri uri) {
            List<String> segmentList = uri.getPathSegments();

            if (segmentList.size() < 2) {
                return null;
            }

            return segmentList.get(1);
        }

        public static Uri buildPosterUri(String posterId) {
            return PosterEntry.CONTENT_URI.buildUpon().appendEncodedPath(posterId).build();
        }

        public static Uri buildPosterUriForMovie(Long movieId) {
            return MovieEntry.CONTENT_URI.buildUpon().appendPath(movieId.toString()).appendPath(PATH_POSTER).build();
        }
    }

    public static class ReviewEntry implements BaseColumns {
        private static final String TAG = ReviewEntry.class.getSimpleName();

        public static final String TABLE_NAME = "Review";
        public static final String COLUMN_REVIEW_ID = "review_id";
        public static final String COLUMN_AUTHOR = "author";
        public static final String COLUMN_CONTENT = "content";
        public static final String COLUMN_MOVIE_ID = "movie_id";

        public static final String PATH_REVIEW = "review";
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_REVIEW).build();
        public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + PATH_REVIEW;
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + PATH_REVIEW;

        public static Long getMovieIdFromUri(Uri uri) {
            List<String> segmentList = uri.getPathSegments();

            if (segmentList.size() < 2) {
                return null;
            }

            Long result = null;

            try {
                result = Long.parseLong(segmentList.get(1));
            } catch (NumberFormatException ex) {
                Log.e(TAG, String.format("An error occurred while parsing movie id from URI %s", uri.toString()));
            }

            return result;
        }

        public static String getReviewIdFromUri(Uri uri) {
            List<String> segmentList = uri.getPathSegments();

            if (segmentList.size() < 2) {
                return null;
            }

            return segmentList.get(1);
        }

        public static Uri buildReviewUri(String reviewId) {
            return ReviewEntry.CONTENT_URI.buildUpon().appendPath(reviewId).build();
        }

        public static Uri buildReviewUriForMovie(Long movieId) {
            return MovieEntry.CONTENT_URI.buildUpon().appendPath(movieId.toString()).appendPath(PATH_REVIEW).build();
        }
    }

    public static class TrailerEntry implements BaseColumns {
        private static final String TAG = TrailerEntry.class.getSimpleName();

        public static final String TABLE_NAME = "Trailer";
        public static final String COLUMN_TRAILER_ID = "trailer_id";

        public static final String COLUMN_TRAILER_NAME = "name";
        public static final String COLUMN_TRAILER_SITE = "site";
        public static final String COLUMN_MOVIE_ID = "movie_id";

        public static final String PATH_TRAILER = "trailer";
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_TRAILER).build();
        public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + PATH_TRAILER;
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + PATH_TRAILER;

        public static Long getMovieIdFromUri(Uri uri) {
            List<String> segmentList = uri.getPathSegments();

            if (segmentList.size() < 2) {
                return null;
            }

            Long result = null;

            try {
                result = Long.parseLong(segmentList.get(1));
            } catch (NumberFormatException ex) {
                Log.e(TAG, String.format("An error occurred while parsing movie id from uri %s", uri.toString()));
            }

            return result;
        }

        public static String getTrailerIdFromUri(Uri uri) {
            List<String> segmentList = uri.getPathSegments();

            if (segmentList.size() < 2) {
                return null;
            }

            return segmentList.get(1);
        }

        public static Uri buildTrailerUri(String trailerId) {
            return ReviewEntry.CONTENT_URI.buildUpon().appendPath(trailerId).build();
        }

        public static Uri buildTrailerUriForMovie(Long movieId) {
            return MovieEntry.CONTENT_URI.buildUpon().appendPath(movieId.toString()).appendPath(TrailerEntry.PATH_TRAILER).build();
        }
    }
}