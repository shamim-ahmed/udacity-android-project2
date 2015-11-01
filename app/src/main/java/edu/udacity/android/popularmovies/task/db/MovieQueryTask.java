package edu.udacity.android.popularmovies.task.db;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import edu.udacity.android.popularmovies.model.Movie;
import edu.udacity.android.popularmovies.util.AppUtils;

public class MovieQueryTask extends AsyncTask<Uri, Void, List<Movie>> {
    private static final String TAG = MovieQueryTask.class.getSimpleName();

    private final Activity activity;
    private final ArrayAdapter<Movie> adapter;

    public MovieQueryTask(ArrayAdapter<Movie> adapter, Activity activity) {
        this.activity = activity;
        this.adapter = adapter;
    }

    @Override
    protected List<Movie> doInBackground(Uri... params) {
        if (params.length == 0) {
            Log.e(TAG, "no uri provided");
            return Collections.emptyList();
        }

        List<Movie> movieList = new ArrayList<>();
        ContentResolver contentResolver = activity.getContentResolver();
        Uri targetUri = params[0];
        Cursor cursor = null;

        try {
            cursor = contentResolver.query(targetUri, null, null, null, null);

            while (cursor.moveToNext()) {
                ContentValues values = AppUtils.readMovieFromCursor(cursor);
                movieList.add(new Movie(values));
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return movieList;
    }

    @Override
    protected void onPostExecute(List<Movie> movieList) {
        adapter.clear();
        adapter.addAll(movieList);
    }
}
