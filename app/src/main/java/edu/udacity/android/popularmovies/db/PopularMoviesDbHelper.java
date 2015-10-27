package edu.udacity.android.popularmovies.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class PopularMoviesDbHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "popularMovies.db";
    public static final int DATABASE_VERSION = 1;

    private static final String CREATE_MOVIE_TABLE_SQL = "CREATE TABLE " + PopularMoviesContract.MovieEntry.TABLE_NAME + "("
            + PopularMoviesContract.MovieEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + PopularMoviesContract.MovieEntry.COLUMN_MOVIE_ID + " TEXT NOT NULL UNIQUE, "
            + PopularMoviesContract.MovieEntry.COLUMN_TITLE + " TEXT NOT NULL, "
            + PopularMoviesContract.MovieEntry.COLUMN_RELEASE_DATE + " TEXT, "
            + PopularMoviesContract.MovieEntry.COLUMN_POSTER_URI + " TEXT, "
            + PopularMoviesContract.MovieEntry.COLUMN_SYNOPSIS + " TEXT, "
            + PopularMoviesContract.MovieEntry.COLUMN_VOTE_AVERAGE + " REAL"
            + ")";

    private static final String CREATE_POSTER_TABLE_SQL = "CREATE TABLE " + PopularMoviesContract.PosterEntry.TABLE_NAME + "("
            + PopularMoviesContract.PosterEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + PopularMoviesContract.PosterEntry.COLUMN_POSTER_ID + " TEXT NOT NULL UNIQUE, "
            + PopularMoviesContract.PosterEntry.COLUMN_MOVIE_ID + " TEXT NOT NULL, "
            + PopularMoviesContract.PosterEntry.COLUMN_CONTENT + " BLOB NOT NULL, "
            + "FOREIGN KEY " + PopularMoviesContract.PosterEntry.COLUMN_MOVIE_ID + " REFERENCES "
            + PopularMoviesContract.MovieEntry.TABLE_NAME + "(" + PopularMoviesContract.MovieEntry.COLUMN_MOVIE_ID + ")"
            + ")";

    private static final String CREATE_TRAILER_TABLE_SQL = "CREATE TABLE " + PopularMoviesContract.TrailerEntry.TABLE_NAME + "("
            + PopularMoviesContract.TrailerEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + PopularMoviesContract.TrailerEntry.COLUMN_TRAILER_ID + " TEXT NOT NULL UNIQUE, "
            + PopularMoviesContract.TrailerEntry.COLUMN_TRAILER_NAME + " TEXT, "
            + PopularMoviesContract.TrailerEntry.COLUMN_TRAILER_SITE + " TEXT"
            + "FOREIGN KEY " + PopularMoviesContract.PosterEntry.COLUMN_MOVIE_ID + " REFERENCES "
            + PopularMoviesContract.MovieEntry.TABLE_NAME + "(" + PopularMoviesContract.MovieEntry.COLUMN_MOVIE_ID + ")"
            + ")";

    private static final String CREATE_REVIEW_TABLE_SQL = "CREATE TABLE " + PopularMoviesContract.ReviewEntry.TABLE_NAME + "("
            + PopularMoviesContract.ReviewEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + PopularMoviesContract.ReviewEntry.COLUMN_REVIEW_ID + " TEXT NOT NULL UNIQUE, "
            + PopularMoviesContract.ReviewEntry.COLUMN_AUTHOR + " TEXT NOT NULL, "
            + PopularMoviesContract.ReviewEntry.COLUMN_CONTENT + " TEXT NOT NULL"
            + "FOREIGN KEY " + PopularMoviesContract.PosterEntry.COLUMN_MOVIE_ID + " REFERENCES "
            + PopularMoviesContract.MovieEntry.TABLE_NAME + "(" + PopularMoviesContract.MovieEntry.COLUMN_MOVIE_ID + ")"
            + ")";

    public PopularMoviesDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_MOVIE_TABLE_SQL);
        db.execSQL(CREATE_POSTER_TABLE_SQL);
        db.execSQL(CREATE_TRAILER_TABLE_SQL);
        db.execSQL(CREATE_REVIEW_TABLE_SQL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO implement it
    }
}
