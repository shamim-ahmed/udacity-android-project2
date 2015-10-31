package edu.udacity.android.popularmovies.task.db;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import edu.udacity.android.popularmovies.model.Movie;
import edu.udacity.android.popularmovies.model.Trailer;
import edu.udacity.android.popularmovies.util.AppUtils;

public class TrailerQueryTask extends AsyncTask<Uri, Void, List<Trailer>> {
    private static final String TAG = TrailerQueryTask.class.getSimpleName();

    private final Activity activity;
    private final Movie movie;

    public TrailerQueryTask(Activity activity, Movie movie) {
        this.activity = activity;
        this.movie = movie;
    }

    @Override
    protected List<Trailer> doInBackground(Uri... params) {
        if (params.length == 0) {
            Log.e(TAG, "no uri provided");
            return Collections.emptyList();
        }

        List<Trailer> trailerList = new ArrayList<>();
        ContentResolver contentResolver = activity.getContentResolver();
        Uri targetUri = params[0];
        Cursor cursor = null;

        try {
            cursor = contentResolver.query(targetUri, null, null, null, null);

            while (cursor.moveToNext()) {
                ContentValues values = AppUtils.readTrailerFromCursor(cursor);
                trailerList.add(new Trailer(values));
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return trailerList;
    }

    @Override
    protected void onPostExecute(List<Trailer> trailerList) {
        movie.setTrailerList(trailerList);
    }
}
