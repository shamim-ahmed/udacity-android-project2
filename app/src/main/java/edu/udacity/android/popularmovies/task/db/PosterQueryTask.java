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

import edu.udacity.android.popularmovies.PopularMoviesApplication;
import edu.udacity.android.popularmovies.db.PopularMoviesContract;
import edu.udacity.android.popularmovies.model.Movie;
import edu.udacity.android.popularmovies.util.AppUtils;

public class PosterQueryTask extends AsyncTask<Void, Void, Bitmap> {
    private static final String TAG = PosterQueryTask.class.getSimpleName();

    private final Movie movie;
    private final Activity activity;
    private final ImageView posterView;

    public PosterQueryTask(Movie movie, Activity activity, ImageView imageView) {
        this.movie = movie;
        this.activity = activity;
        this.posterView = imageView;
    }

    @Override
    protected Bitmap doInBackground(Void... params) {
        String posterId = movie.getPosterUri().getLastPathSegment();
        PopularMoviesApplication application = (PopularMoviesApplication) activity.getApplication();
        Bitmap posterBitmap = application.getBitmapFromMemCache(posterId);

        if (posterBitmap != null) {
            Log.i(TAG, String.format("poster for movie %s was retrieved from in-memory cache", movie.getTitle()));
            return posterBitmap;
        }

        ContentResolver contentResolver = activity.getContentResolver();
        Uri targetUri = PopularMoviesContract.PosterEntry.buildPosterUriForMovie(movie.getMovieId());
        Cursor cursor = null;

        try {
            cursor = contentResolver.query(targetUri, null, null, null, PopularMoviesContract.PosterEntry._ID);

            // we assume for the time being that there is only one poster per movie
            if (cursor.moveToNext()) {
                ContentValues values = AppUtils.readPosterFromCursor(cursor);
                byte[] posterContent = values.getAsByteArray(PopularMoviesContract.PosterEntry.COLUMN_CONTENT);
                posterBitmap = BitmapFactory.decodeByteArray(posterContent, 0, posterContent.length);

                // put the scaled bitmap in cache for future retrieval
                application.addBitmapToMemoryCache(posterId, posterBitmap);

                Log.i(TAG, String.format("poster for movie %s was loaded from database", movie.getTitle()));
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return posterBitmap;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        if (posterView != null) {
            posterView.setImageBitmap(bitmap);
        }
    }
}
