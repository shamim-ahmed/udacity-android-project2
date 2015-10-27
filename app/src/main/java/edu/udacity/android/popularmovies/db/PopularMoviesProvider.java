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

    private PopularMoviesDbHelper dbHelper;

    private static UriMatcher buildUriMatcher() {
        UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);

        matcher.addURI(PopularMoviesContract.CONTENT_AUTHORITY, PopularMoviesContract.MovieEntry.PATH_MOVIE, MOVIE);
        matcher.addURI(PopularMoviesContract.CONTENT_AUTHORITY, PopularMoviesContract.MovieEntry.PATH_MOVIE + "/#", MOVIE_WITH_ID);
        matcher.addURI(PopularMoviesContract.CONTENT_AUTHORITY, PopularMoviesContract.PosterEntry.PATH_POSTER, POSTER);
        matcher.addURI(PopularMoviesContract.CONTENT_AUTHORITY, PopularMoviesContract.PosterEntry.PATH_POSTER + "/#", POSTER_WITH_ID);
        matcher.addURI(PopularMoviesContract.CONTENT_AUTHORITY, PopularMoviesContract.MovieEntry.PATH_MOVIE + "/#" + PopularMoviesContract.PosterEntry.PATH_POSTER, POSTER_WITH_MOVIE_ID);
        matcher.addURI(PopularMoviesContract.CONTENT_AUTHORITY, PopularMoviesContract.TrailerEntry.PATH_TRAILER, TRAILER);
        matcher.addURI(PopularMoviesContract.CONTENT_AUTHORITY, PopularMoviesContract.TrailerEntry.PATH_TRAILER + "/#", TRAILER_WITH_ID);
        matcher.addURI(PopularMoviesContract.CONTENT_AUTHORITY, PopularMoviesContract.MovieEntry.PATH_MOVIE + "/#" + PopularMoviesContract.TrailerEntry.PATH_TRAILER, TRAILER_WITH_MOVIE_ID);
        matcher.addURI(PopularMoviesContract.CONTENT_AUTHORITY, PopularMoviesContract.ReviewEntry.PATH_REVIEW, REVIEW);
        matcher.addURI(PopularMoviesContract.CONTENT_AUTHORITY, PopularMoviesContract.ReviewEntry.PATH_REVIEW + "/#", REVIEW_WITH_ID);
        matcher.addURI(PopularMoviesContract.CONTENT_AUTHORITY, PopularMoviesContract.MovieEntry.PATH_MOVIE + "/#" + PopularMoviesContract.ReviewEntry.PATH_REVIEW, REVIEW_WITH_MOVIE_ID);

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
        String type = getType(uri);

        switch (sUriMatcher.match(uri)) {
            case MOVIE: {
                result = sMovieQueryBuilder.query(dbHelper.getWritableDatabase(), projection, selection, selectionArgs, null, null, sortOrder);
                break;
            }

            case MOVIE_WITH_ID: {
                Long movieId = PopularMoviesContract.MovieEntry.getMovieIdFromUri(uri);

                if (movieId != null) {
                    result = sMovieQueryBuilder.query(dbHelper.getWritableDatabase(), projection, "movie_id = ?", new String[]{movieId.toString()}, null, null, sortOrder);
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
                    result = sPosterQueryBuilder.query(dbHelper.getWritableDatabase(), projection, "poster_id = ?", new String[]{posterId}, null, null, sortOrder);
                }

                break;
            }

            case POSTER_WITH_MOVIE_ID: {
                Long movieId = PopularMoviesContract.PosterEntry.getMovieIdFromUri(uri);

                if (movieId != null) {
                    result = sPosterQueryBuilder.query(dbHelper.getWritableDatabase(), projection, "movie_id = ?", new String[]{movieId.toString()}, null, null, sortOrder);
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
                    result = sTrailerQueryBuilder.query(dbHelper.getWritableDatabase(), projection, "trailer_id = ?", new String[]{trailerId}, null, null, sortOrder);
                }

                break;
            }

            case TRAILER_WITH_MOVIE_ID: {
                Long movieId = PopularMoviesContract.TrailerEntry.getMovieIdFromUri(uri);

                if (movieId != null) {
                    result = sTrailerQueryBuilder.query(dbHelper.getWritableDatabase(), projection, "movie_id = ?", new String[]{movieId.toString()}, null, null, sortOrder);
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
                    result = sReviewQueryBuilder.query(dbHelper.getWritableDatabase(), projection, "review_id = ?", new String[] {reviewId}, null, null, sortOrder);
                }

                break;
            }

            case REVIEW_WITH_MOVIE_ID: {
                Long movieId = PopularMoviesContract.ReviewEntry.getMovieIdFromUri(uri);

                if (movieId != null) {
                    result = sReviewQueryBuilder.query(dbHelper.getWritableDatabase(), projection, "movie_id = ?", new String[]{movieId.toString()}, null, null, sortOrder);
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

        switch (sUriMatcher.match(uri)) {
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
                Log.e(TAG, String.format("unknown type for uri : %s", uri.toString()));
        }

        return result;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Uri result = null;

        switch (sUriMatcher.match(uri)) {
            case MOVIE: {
                long _id = db.insert(PopularMoviesContract.MovieEntry.TABLE_NAME, null, values);

                if (_id != -1) {
                    Long movieId = (Long) values.get("movie_id");
                    result = PopularMoviesContract.MovieEntry.buildMovieUri(movieId);
                }

                break;
            }

            case POSTER : {
                long _id = db.insert(PopularMoviesContract.PosterEntry.TABLE_NAME, null, values);

                if (_id != -1) {
                    String posterId = values.getAsString("poster_id");
                    result = PopularMoviesContract.PosterEntry.buildPosterUri(posterId);
                }

                break;
            }

            case TRAILER: {
                long _id = db.insert(PopularMoviesContract.TrailerEntry.TABLE_NAME, null, values);

                if (_id != -1) {
                    String trailerId = values.getAsString("trailer_id");
                    result = PopularMoviesContract.TrailerEntry.buildTrailerUri(trailerId);
                }
                break;
            }

            case REVIEW: {
                long _id = db.insert(PopularMoviesContract.ReviewEntry.TABLE_NAME, null, values);

                if (_id != -1) {
                    String reviewId = values.getAsString("review_id");
                    result = PopularMoviesContract.ReviewEntry.buildReviewUri(reviewId);
                }

                break;
            }

            default: {
                Log.e(TAG, String.format("Invalid uri : %S", uri.toString()));
                break;
            }
        }

        return result;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        String type = getType(uri);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int result = 0;

        switch (sUriMatcher.match(uri)) {
            case MOVIE: {
                result = db.delete(PopularMoviesContract.MovieEntry.TABLE_NAME, null, null);
                break;
            }

            case MOVIE_WITH_ID: {
                Long movieId = PopularMoviesContract.MovieEntry.getMovieIdFromUri(uri);

                if (movieId != null) {
                    result = db.delete(PopularMoviesContract.MovieEntry.TABLE_NAME, "movie_id = ?", new String[]{movieId.toString()});
                }

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
