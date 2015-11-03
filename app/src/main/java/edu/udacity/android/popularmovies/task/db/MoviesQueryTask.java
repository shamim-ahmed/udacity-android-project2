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
import java.util.List;

import edu.udacity.android.popularmovies.db.PopularMoviesContract;
import edu.udacity.android.popularmovies.model.Movie;
import edu.udacity.android.popularmovies.util.AppUtils;

public class MoviesQueryTask extends AsyncTask<Void, Void, List<Movie>> {
    private static final String TAG = MoviesQueryTask.class.getSimpleName();

    private final Activity activity;
    private final ArrayAdapter<Movie> adapter;

    public MoviesQueryTask(ArrayAdapter<Movie> adapter, Activity activity) {
        this.activity = activity;
        this.adapter = adapter;
    }

    @Override
    protected List<Movie> doInBackground(Void... params) {
        List<Movie> movieList = new ArrayList<>();
        ContentResolver contentResolver = activity.getContentResolver();
        Uri targetUri = PopularMoviesContract.MovieEntry.CONTENT_URI;
        Cursor cursor = null;

        try {
            cursor = contentResolver.query(targetUri, null, null, null, PopularMoviesContract.MovieEntry._ID);

            while (cursor.moveToNext()) {
                ContentValues values = AppUtils.readMovieFromCursor(cursor);
                movieList.add(new Movie(values));
            }

            Log.i(TAG, String.format("%d favorite movies were loaded from database", movieList.size()));
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
