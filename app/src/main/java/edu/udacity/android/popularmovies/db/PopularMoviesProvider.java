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
    private static final int POSTER_WITH_MOVIE_ID = 202;
    private static final int TRAILER = 300;
    private static final int TRAILER_WITH_ID = 301;
    private static final int TRAILER_WITH_MOVIE_ID = 302;
    private static final int REVIEW = 400;
    private static final int REVIEW_WITH_ID = 401;
    private static final int REVIEW_WITH_MOVIE_ID = 402;

    private static final SQLiteQueryBuilder sMovieQueryBuilder = new SQLiteQueryBuilder();
    private static final SQLiteQueryBuilder sPosterQueryBuilder = new SQLiteQueryBuilder();
    private static final SQLiteQueryBuilder sTrailerQueryBuilder = new SQLiteQueryBuilder();
    private static final SQLiteQueryBuilder sReviewQueryBuilder = new SQLiteQueryBuilder();

    static {
        sMovieQueryBuilder.setTables(PopularMoviesContract.MovieEntry.TABLE_NAME);
        sPosterQueryBuilder.setTables(PopularMoviesContract.PosterEntry.TABLE_NAME);
        sTrailerQueryBuilder.setTables(PopularMoviesContract.TrailerEntry.TABLE_NAME);
        sReviewQueryBuilder.setTables(PopularMoviesContract.ReviewEntry.TABLE_NAME);
    }

    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private static final String MOVIE_ID_SELECTION = "movie_id = ?";
    private static final String POSTER_ID_SELECTION = "poster_id = ?";
    private static final String TRAILER_ID_SELECTION = "trailer_id = ?";
    private static final String REVIEW_ID_SELECTION = "review_id = ?";

    private PopularMoviesDbHelper dbHelper;

    private static UriMatcher buildUriMatcher() {
        UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);

        matcher.addURI(PopularMoviesContract.CONTENT_AUTHORITY, PopularMoviesContract.MovieEntry.PATH_MOVIE, MOVIE);
        matcher.addURI(PopularMoviesContract.CONTENT_AUTHORITY, PopularMoviesContract.MovieEntry.PATH_MOVIE + "/#", MOVIE_WITH_ID);
        matcher.addURI(PopularMoviesContract.CONTENT_AUTHORITY, PopularMoviesContract.PosterEntry.PATH_POSTER, POSTER);
        matcher.addURI(PopularMoviesContract.CONTENT_AUTHORITY, PopularMoviesContract.PosterEntry.PATH_POSTER + "/#", POSTER_WITH_ID);
        matcher.addURI(PopularMoviesContract.CONTENT_AUTHORITY, PopularMoviesContract.MovieEntry.PATH_MOVIE + "/#/" + PopularMoviesContract.PosterEntry.PATH_POSTER, POSTER_WITH_MOVIE_ID);
        matcher.addURI(PopularMoviesContract.CONTENT_AUTHORITY, PopularMoviesContract.TrailerEntry.PATH_TRAILER, TRAILER);
        matcher.addURI(PopularMoviesContract.CONTENT_AUTHORITY, PopularMoviesContract.TrailerEntry.PATH_TRAILER + "/#", TRAILER_WITH_ID);
        matcher.addURI(PopularMoviesContract.CONTENT_AUTHORITY, PopularMoviesContract.MovieEntry.PATH_MOVIE + "/#/" + PopularMoviesContract.TrailerEntry.PATH_TRAILER, TRAILER_WITH_MOVIE_ID);
        matcher.addURI(PopularMoviesContract.CONTENT_AUTHORITY, PopularMoviesContract.ReviewEntry.PATH_REVIEW, REVIEW);
        matcher.addURI(PopularMoviesContract.CONTENT_AUTHORITY, PopularMoviesContract.ReviewEntry.PATH_REVIEW + "/#", REVIEW_WITH_ID);
        matcher.addURI(PopularMoviesContract.CONTENT_AUTHORITY, PopularMoviesContract.MovieEntry.PATH_MOVIE + "/#/" + PopularMoviesContract.ReviewEntry.PATH_REVIEW, REVIEW_WITH_MOVIE_ID);

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
        int matchValue = sUriMatcher.match(uri);
        switch (matchValue) {
            case MOVIE: {
                result = sMovieQueryBuilder.query(dbHelper.getWritableDatabase(), projection, selection, selectionArgs, null, null, sortOrder);
                break;
            }

            case MOVIE_WITH_ID: {
                Long movieId = PopularMoviesContract.MovieEntry.getMovieIdFromUri(uri);

                if (movieId != null) {
                    result = sMovieQueryBuilder.query(dbHelper.getWritableDatabase(), projection, MOVIE_ID_SELECTION, new String[]{movieId.toString()}, null, null, sortOrder);
                }

                break;
            }

            case POSTER: {
                result = sPosterQueryBuilder.query(dbHelper.getWritableDatabase(), projection, selection, selectionArgs, null, null, sortOrder);
                break;
            }

            case POSTER_WITH_ID: {
                String posterId = PopularMoviesContract.PosterEntry.getPosterIdFromUri(uri);

                if (posterId != null) {
                    result = sPosterQueryBuilder.query(dbHelper.getWritableDatabase(), projection, POSTER_ID_SELECTION, new String[]{posterId}, null, null, sortOrder);
                }

                break;
            }

            case POSTER_WITH_MOVIE_ID: {
                Long movieId = PopularMoviesContract.PosterEntry.getMovieIdFromUri(uri);

                if (movieId != null) {
                    result = sPosterQueryBuilder.query(dbHelper.getWritableDatabase(), projection, MOVIE_ID_SELECTION, new String[]{movieId.toString()}, null, null, sortOrder);
                }

                break;
            }

            case TRAILER: {
                result = sTrailerQueryBuilder.query(dbHelper.getWritableDatabase(), projection, selection, selectionArgs, null, null, sortOrder);
                break;
            }

            case TRAILER_WITH_ID: {
                String trailerId = PopularMoviesContract.TrailerEntry.getTrailerIdFromUri(uri);

                if (trailerId != null) {
                    result = sTrailerQueryBuilder.query(dbHelper.getWritableDatabase(), projection, TRAILER_ID_SELECTION, new String[]{trailerId}, null, null, sortOrder);
                }

                break;
            }

            case TRAILER_WITH_MOVIE_ID: {
                Long movieId = PopularMoviesContract.TrailerEntry.getMovieIdFromUri(uri);

                if (movieId != null) {
                    result = sTrailerQueryBuilder.query(dbHelper.getWritableDatabase(), projection, MOVIE_ID_SELECTION, new String[]{movieId.toString()}, null, null, sortOrder);
                }

                break;
            }

            case REVIEW: {
                result = sReviewQueryBuilder.query(dbHelper.getWritableDatabase(), projection, selection, selectionArgs, null, null, sortOrder);
                break;
            }

            case REVIEW_WITH_ID: {
                String reviewId = PopularMoviesContract.ReviewEntry.getReviewIdFromUri(uri);

                if (reviewId != null) {
                    result = sReviewQueryBuilder.query(dbHelper.getWritableDatabase(), projection, REVIEW_ID_SELECTION, new String[]{reviewId}, null, null, sortOrder);
                }

                break;
            }

            case REVIEW_WITH_MOVIE_ID: {
                Long movieId = PopularMoviesContract.ReviewEntry.getMovieIdFromUri(uri);

                if (movieId != null) {
                    result = sReviewQueryBuilder.query(dbHelper.getWritableDatabase(), projection, MOVIE_ID_SELECTION, new String[]{movieId.toString()}, null, null, sortOrder);
                }

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
        int matchValue = sUriMatcher.match(uri);

        switch (matchValue) {
            case MOVIE:
                result = PopularMoviesContract.MovieEntry.CONTENT_TYPE;
                break;
            case MOVIE_WITH_ID:
                result = PopularMoviesContract.MovieEntry.CONTENT_ITEM_TYPE;
                break;
            case POSTER:
                result = PopularMoviesContract.PosterEntry.CONTENT_TYPE;
                break;
            case POSTER_WITH_ID:
                result = PopularMoviesContract.PosterEntry.CONTENT_ITEM_TYPE;
                break;
            case POSTER_WITH_MOVIE_ID:
                result = PopularMoviesContract.PosterEntry.CONTENT_TYPE;
                break;
            case TRAILER:
                result = PopularMoviesContract.TrailerEntry.CONTENT_TYPE;
                break;
            case TRAILER_WITH_ID:
                result = PopularMoviesContract.TrailerEntry.CONTENT_ITEM_TYPE;
                break;
            case TRAILER_WITH_MOVIE_ID:
                result = PopularMoviesContract.TrailerEntry.CONTENT_TYPE;
                break;
            case REVIEW:
                result = PopularMoviesContract.ReviewEntry.CONTENT_TYPE;
                break;
            case REVIEW_WITH_ID:
                result = PopularMoviesContract.ReviewEntry.CONTENT_ITEM_TYPE;
                break;
            case REVIEW_WITH_MOVIE_ID:
                result = PopularMoviesContract.ReviewEntry.CONTENT_TYPE;
                break;
            default:
                Log.e(TAG, String.format("cannot determine type for uri : %s", uri.toString()));
        }

        return result;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Uri result = null;
        int matchValue = sUriMatcher.match(uri);

        switch (matchValue) {
            case MOVIE: {
                long _id = db.insert(PopularMoviesContract.MovieEntry.TABLE_NAME, null, values);

                if (_id != -1) {
                    Long movieId = (Long) values.get(PopularMoviesContract.MovieEntry.COLUMN_MOVIE_ID);
                    result = PopularMoviesContract.MovieEntry.buildMovieUri(movieId);
                }

                break;
            }

            case POSTER: {
                long _id = db.insert(PopularMoviesContract.PosterEntry.TABLE_NAME, null, values);

                if (_id != -1) {
                    String posterId = values.getAsString(PopularMoviesContract.PosterEntry.COLUMN_POSTER_ID);
                    result = PopularMoviesContract.PosterEntry.buildPosterUri(posterId);
                }

                break;
            }

            case TRAILER: {
                long _id = db.insert(PopularMoviesContract.TrailerEntry.TABLE_NAME, null, values);

                if (_id != -1) {
                    String trailerId = values.getAsString(PopularMoviesContract.TrailerEntry.COLUMN_TRAILER_ID);
                    result = PopularMoviesContract.TrailerEntry.buildTrailerUri(trailerId);
                }
                break;
            }

            case REVIEW: {
                long _id = db.insert(PopularMoviesContract.ReviewEntry.TABLE_NAME, null, values);

                if (_id != -1) {
                    String reviewId = values.getAsString(PopularMoviesContract.ReviewEntry.COLUMN_REVIEW_ID);
                    result = PopularMoviesContract.ReviewEntry.buildReviewUri(reviewId);
                }

                break;
            }

            default: {
                Log.e(TAG, String.format("Invalid uri for insert : %s", uri.toString()));
                break;
            }
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return result;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int result = 0;
        String tableName = null;
        int matchValue = sUriMatcher.match(uri);

        switch (matchValue) {
            case MOVIE: {
                tableName = PopularMoviesContract.MovieEntry.TABLE_NAME;
                break;
            }

            case POSTER: {
                tableName = PopularMoviesContract.PosterEntry.TABLE_NAME;
                break;
            }

            case TRAILER: {
                tableName = PopularMoviesContract.TrailerEntry.TABLE_NAME;
                break;
            }

            case REVIEW: {
                tableName = PopularMoviesContract.ReviewEntry.TABLE_NAME;
                break;
            }

            default: {
                Log.e(TAG, String.format("Invalid uri for delete : %s", uri.toString()));
                break;
            }
        }

        if (tableName != null) {
            result = db.delete(PopularMoviesContract.MovieEntry.TABLE_NAME, selection, selectionArgs);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return result;
    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] valuesArray) {
        int count = 0;
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int matchValue = sUriMatcher.match(uri);
        String tableName = null;

        switch (matchValue) {
            case MOVIE: {
                tableName = PopularMoviesContract.MovieEntry.TABLE_NAME;
                break;
            }
            case POSTER: {
                tableName = PopularMoviesContract.PosterEntry.TABLE_NAME;
                break;
            }
            case TRAILER: {
                tableName = PopularMoviesContract.TrailerEntry.TABLE_NAME;
                break;
            }
            case REVIEW: {
                tableName = PopularMoviesContract.ReviewEntry.TABLE_NAME;
                break;
            }
            default: {
                Log.w(TAG, String.format("invalid uri for bulkInsert : %s", uri.toString()));
                break;
            }
        }

        if (tableName == null) {
            return 0;
        }

        try {
            db.beginTransaction();

            for (ContentValues values : valuesArray) {
                long _id = db.insert(tableName, null, values);

                if (_id != -1) {
                    count++;
                }
            }

            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        Log.w(TAG, "update method is not implemented");
        return 0;
    }
}
