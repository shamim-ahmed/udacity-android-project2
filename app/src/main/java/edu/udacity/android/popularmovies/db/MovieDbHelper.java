package edu.udacity.android.popularmovies.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MovieDbHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "popularMovies.db";
    public static final int DATABASE_VERSION = 1;

    private static final String CREATE_TABLE_SQL = "CREATE TABLE " + MovieContract.MovieEntry.TABLE_NAME + "("
            + MovieContract.MovieEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + MovieContract.MovieEntry.COLUMN_MOVIE_ID + " TEXT NOT NULL UNIQUE, "
            + MovieContract.MovieEntry.COLUMN_TITLE + " TEXT NOT NULL, "
            + MovieContract.MovieEntry.COLUMN_RELEASE_DATE + " TEXT, "
            + MovieContract.MovieEntry.COLUMN_POSTER_URI + " TEXT, "
            + MovieContract.MovieEntry.COLUMN_SYNOPSIS + " TEXT, "
            + MovieContract.MovieEntry.COLUMN_VOTE_AVERAGE + " REAL"
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
