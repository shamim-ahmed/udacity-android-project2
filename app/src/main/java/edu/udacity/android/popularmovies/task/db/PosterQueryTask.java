package edu.udacity.android.popularmovies.task.db;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import edu.udacity.android.popularmovies.R;
import edu.udacity.android.popularmovies.db.PopularMoviesContract;
import edu.udacity.android.popularmovies.model.Movie;
import edu.udacity.android.popularmovies.util.AppUtils;

public class PosterQueryTask extends AsyncTask<Void, Void, byte[]> {
    private static final String TAG = PosterQueryTask.class.getSimpleName();

    private final Activity activity;
    private final Movie movie;

    public PosterQueryTask(Movie movie, Activity activity) {
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

            // we assume for the time being that there is only one poster
            if (cursor.moveToNext()) {
                ContentValues values = AppUtils.readPosterFromCursor(cursor);
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
        Bitmap bitmap = BitmapFactory.decodeByteArray(posterContent, 0, posterContent.length);
        ImageView posterView = (ImageView) activity.findViewById(R.id.movie_details_poster);
        posterView.setImageBitmap(bitmap);

        Log.i(TAG, String.format("poster for movie id %d was successfully loaded from database", movie.getMovieId()));
    }
}
