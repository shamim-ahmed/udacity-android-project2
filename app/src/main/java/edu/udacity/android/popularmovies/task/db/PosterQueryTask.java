package edu.udacity.android.popularmovies.task.db;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import edu.udacity.android.popularmovies.db.PopularMoviesContract;
import edu.udacity.android.popularmovies.model.Movie;
import edu.udacity.android.popularmovies.util.AppUtils;

public class PosterQueryTask extends AsyncTask<Void, Void, byte[]> {
    private static final String TAG = PosterQueryTask.class.getSimpleName();

    private final Activity activity;
    private final Movie movie;

    public PosterQueryTask(Activity activity, Movie movie) {
        this.activity = activity;
        this.movie = movie;
    }

    @Override
    protected byte[] doInBackground(Void... params) {
        byte[] posterContent = null;
        ContentResolver contentResolver = activity.getContentResolver();
        Uri targetUri = PopularMoviesContract.PosterEntry.buildPosterUriForMovie(movie.getMovieId());
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
