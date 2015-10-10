package edu.udacity.android.popularmovies.db;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.util.Log;

public class MovieProvider extends ContentProvider {
    private static final String TAG = MovieProvider.class.getSimpleName();

    private static final int MOVIE = 100;
    private static final int MOVIE_WITH_ID = 200;
    private static final SQLiteQueryBuilder sQueryBuilder = new SQLiteQueryBuilder();

    static {
        sQueryBuilder.setTables(MovieContract.MovieEntry.TABLE_NAME);
    }

    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private static final String MOVIE_ID_SELECTION = "movie_id = ?";

    private MovieDbHelper dbHelper;

    private static UriMatcher buildUriMatcher() {
        UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        matcher.addURI(MovieContract.CONTENT_AUTHORITY, MovieContract.PATH_MOVIE, MOVIE);
        matcher.addURI(MovieContract.CONTENT_AUTHORITY, MovieContract.PATH_MOVIE + "/#", MOVIE_WITH_ID);

        return matcher;
    }

    @Override
    public boolean onCreate() {
        dbHelper = new MovieDbHelper(getContext());
        return true;
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
                Long movieId = MovieContract.MovieEntry.getMovieIdFromUri(uri);

                if (movieId != null) {
                    result = sQueryBuilder.query(dbHelper.getWritableDatabase(), projection, MOVIE_ID_SELECTION, new String[]{movieId.toString()}, null, null, sortOrder);
                }

                break;
            }
            default:
                Log.w(TAG, String.format("No match found for uri %s", uri.toString()));
                break;
        }

        return result;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Uri result = null;
        long _id = db.insert(MovieContract.MovieEntry.TABLE_NAME, null, values);

        if (_id != -1) {
            Long movieId = (Long) values.get("movie_id");
            result = MovieContract.MovieEntry.buildMovieUri(movieId);
        }

        return result;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        Long movieId = MovieContract.MovieEntry.getMovieIdFromUri(uri);
        int result = 0;

        if (movieId != null) {
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            result = db.delete(MovieContract.MovieEntry.TABLE_NAME, MOVIE_ID_SELECTION, new String[] {movieId.toString()});
        }


        return result;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }
}
