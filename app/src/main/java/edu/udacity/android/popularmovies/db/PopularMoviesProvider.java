package edu.udacity.android.popularmovies.db;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.util.Log;

public class PopularMoviesProvider extends ContentProvider {
    private static final String TAG = PopularMoviesProvider.class.getSimpleName();

    private static final int MOVIE = 100;
    private static final int MOVIE_WITH_ID = 101;
    private static final int POSTER = 200;
    private static final int POSTER_WITH_ID = 201;
    private static final int TRAILER = 300;
    private static final int TRAILER_WITH_ID = 301;
    private static final int REVIEW = 400;
    private static final int REVIEW_WITH_ID = 401;

    private static final SQLiteQueryBuilder sQueryBuilder = new SQLiteQueryBuilder();

    static {
        sQueryBuilder.setTables(PopularMoviesContract.MovieEntry.TABLE_NAME + "," +
                PopularMoviesContract.MoviePosterEntry.TABLE_NAME + "," +
                PopularMoviesContract.MovieTrailerEntry.TABLE_NAME + "," +
                PopularMoviesContract.MovieReviewEntry.TABLE_NAME);
    }

    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private static final String MOVIE_ID_SELECTION = "movie_id = ?";

    private PopularMoviesDbHelper dbHelper;

    private static UriMatcher buildUriMatcher() {
        UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        matcher.addURI(PopularMoviesContract.CONTENT_AUTHORITY, PopularMoviesContract.MovieEntry.PATH_MOVIE, MOVIE);
        matcher.addURI(PopularMoviesContract.CONTENT_AUTHORITY, PopularMoviesContract.MovieEntry.PATH_MOVIE + "/#", MOVIE_WITH_ID);
        matcher.addURI(PopularMoviesContract.CONTENT_AUTHORITY, PopularMoviesContract.MovieEntry.PATH_MOVIE + "/#" + PopularMoviesContract.MoviePosterEntry.PATH_POSTER, POSTER);
        matcher.addURI(PopularMoviesContract.CONTENT_AUTHORITY, PopularMoviesContract.MovieEntry.PATH_MOVIE + "/#" + PopularMoviesContract.MoviePosterEntry.PATH_POSTER + "/#", POSTER_WITH_ID);
        matcher.addURI(PopularMoviesContract.CONTENT_AUTHORITY, PopularMoviesContract.MovieEntry.PATH_MOVIE + "/#" + PopularMoviesContract.MovieTrailerEntry.PATH_TRAILER, TRAILER);
        matcher.addURI(PopularMoviesContract.CONTENT_AUTHORITY, PopularMoviesContract.MovieEntry.PATH_MOVIE + "/#" + PopularMoviesContract.MovieTrailerEntry.PATH_TRAILER + "/#", TRAILER_WITH_ID);
        matcher.addURI(PopularMoviesContract.CONTENT_AUTHORITY, PopularMoviesContract.MovieEntry.PATH_MOVIE + "/#" + PopularMoviesContract.MovieReviewEntry.PATH_REVIEW, REVIEW);
        matcher.addURI(PopularMoviesContract.CONTENT_AUTHORITY, PopularMoviesContract.MovieEntry.PATH_MOVIE + "/#" + PopularMoviesContract.MovieReviewEntry.PATH_REVIEW + "/#", REVIEW_WITH_ID);

        return matcher;
    }

    @Override
    public boolean onCreate() {
        dbHelper = new PopularMoviesDbHelper(getContext());
        return true;
    }

    PopularMoviesDbHelper getMovieDbHelper() {
        return dbHelper;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Cursor result = null;

        switch (sUriMatcher.match(uri)) {
            case MOVIE: {
                result = sQueryBuilder.query(dbHelper.getWritableDatabase(), projection, selection, selectionArgs, null, null, sortOrder);
                break;
            }
            case MOVIE_WITH_ID: {
                Long movieId = PopularMoviesContract.MovieEntry.getMovieIdFromUri(uri);

                if (movieId != null) {
                    result = sQueryBuilder.query(dbHelper.getWritableDatabase(), projection, MOVIE_ID_SELECTION, new String[]{movieId.toString()}, null, null, sortOrder);
                }

                break;
            }
            case POSTER: {
                break;
            }
            case POSTER_WITH_ID: {
                break;
            }
            case TRAILER: {
                break;
            }
            case TRAILER_WITH_ID: {
                break;
            }
            case REVIEW: {
                break;
            }
            case REVIEW_WITH_ID: {
                break;
            }
            default: {
                Log.w(TAG, String.format("No match found for uri %s", uri.toString()));
                break;
            }
        }

        return result;
    }

    @Override
    public String getType(Uri uri) {
        String result = null;

        switch (sUriMatcher.match(uri)) {
            case MOVIE:
                result = PopularMoviesContract.MovieEntry.CONTENT_TYPE;
                break;
            case MOVIE_WITH_ID:
                result = PopularMoviesContract.MovieEntry.CONTENT_ITEM_TYPE;
                break;
            default:
                Log.e(TAG, String.format("unknown type for uri : %s", uri.toString()));
        }

        return result;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        if (sUriMatcher.match(uri) != MOVIE) {
            Log.e(TAG, String.format("Invalid URI : %s", uri.toString()));
            return null;
        }

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Uri result = null;
        long _id = db.insert(PopularMoviesContract.MovieEntry.TABLE_NAME, null, values);

        if (_id != -1) {
            Long movieId = (Long) values.get("movie_id");
            result = PopularMoviesContract.MovieEntry.buildMovieUri(movieId);
        }

        return result;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int result = 0;

        switch (sUriMatcher.match(uri)) {
            case MOVIE: {
                result = db.delete(PopularMoviesContract.MovieEntry.TABLE_NAME, null, null);
                break;
            }

            case MOVIE_WITH_ID: {
                Long movieId = PopularMoviesContract.MovieEntry.getMovieIdFromUri(uri);
                result = db.delete(PopularMoviesContract.MovieEntry.TABLE_NAME, MOVIE_ID_SELECTION, new String[] {movieId.toString()});
                break;
            }

            default: {
                Log.e(TAG, String.format("Invalid uri for delete : %s", uri.toString()));
                break;
            }
        }

        return result;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }
}
