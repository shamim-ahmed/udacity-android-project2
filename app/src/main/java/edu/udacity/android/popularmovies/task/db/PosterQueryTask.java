package edu.udacity.android.popularmovies.task.db;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import edu.udacity.android.popularmovies.db.PopularMoviesContract;
import edu.udacity.android.popularmovies.model.Movie;
import edu.udacity.android.popularmovies.model.Trailer;
import edu.udacity.android.popularmovies.util.AppUtils;

public class PosterQueryTask extends AsyncTask<Uri, Void, byte[]> {
    private static final String TAG = PosterQueryTask.class.getSimpleName();

    private final Activity activity;
    private final Movie movie;

    public PosterQueryTask(Activity activity, Movie movie) {
        this.activity = activity;
        this.movie = movie;
    }

    @Override
    protected byte[] doInBackground(Uri... params) {
        if (params.length == 0) {
            Log.e(TAG, "no uri provided");
            return null;
        }

        byte[] posterContent = null;
        ContentResolver contentResolver = activity.getContentResolver();
        Uri targetUri = params[0];
        Cursor cursor = null;

        try {
            cursor = contentResolver.query(targetUri, null, null, null, null);

            if (cursor.moveToNext()) {
                ContentValues values = AppUtils.readTrailerFromCursor(cursor);
                posterContent = values.getAsByteArray(PopularMoviesContract.PosterEntry.COLUMN_CONTENT);
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return posterContent;
    }

    @Override
    protected void onPostExecute(byte[] posterContent) {
        // TODO implement it
    }
}
