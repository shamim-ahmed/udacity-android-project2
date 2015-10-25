package edu.udacity.android.popularmovies.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MovieDbHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "popularMovies.db";
    public static final int DATABASE_VERSION = 1;

    private static final String CREATE_TABLE_SQL = "CREATE TABLE " + PopularMoviesContract.MovieEntry.TABLE_NAME + "("
            + PopularMoviesContract.MovieEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + PopularMoviesContract.MovieEntry.COLUMN_MOVIE_ID + " TEXT NOT NULL UNIQUE, "
            + PopularMoviesContract.MovieEntry.COLUMN_TITLE + " TEXT NOT NULL, "
            + PopularMoviesContract.MovieEntry.COLUMN_RELEASE_DATE + " TEXT, "
            + PopularMoviesContract.MovieEntry.COLUMN_POSTER_URI + " TEXT, "
            + PopularMoviesContract.MovieEntry.COLUMN_SYNOPSIS + " TEXT, "
            + PopularMoviesContract.MovieEntry.COLUMN_VOTE_AVERAGE + " REAL"
            + ")";

    public MovieDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_SQL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO implement it
    }
}
