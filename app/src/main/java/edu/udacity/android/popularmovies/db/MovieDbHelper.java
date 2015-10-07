package edu.udacity.android.popularmovies.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MovieDbHelper extends SQLiteOpenHelper {
    private static final String CREATE_TABLE_SQL = "CREATE TABLE " + MovieContract.MovieEntry.TABLE_NAME + "("
            + MovieContract.MovieEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + MovieContract.MovieEntry.COLUMN_MOVIE_ID + " TEXT NOT NULL UNIQUE, "
            + MovieContract.MovieEntry.COLUMN_TITLE + " TEXT NOT NULL, "
            + MovieContract.MovieEntry.COLUMN_RELEASE_DATE + " TEXT, "
            + MovieContract.MovieEntry.COLUMN_POSTER_URI + " TEXT, "
            + MovieContract.MovieEntry.COLUMN_SYNOPSIS + " TEXT, "
            + MovieContract.MovieEntry.COLUMN_VOTE_AVERAGE + " REAL"
            + ")";

    public MovieDbHelper(Context context, String name, int version) {
        super(context, name, null, version);
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
