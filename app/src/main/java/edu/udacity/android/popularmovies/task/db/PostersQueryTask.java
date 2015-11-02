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

import edu.udacity.android.popularmovies.db.PopularMoviesContract;
import edu.udacity.android.popularmovies.model.Movie;
import edu.udacity.android.popularmovies.util.AppUtils;

public class PostersQueryTask extends AsyncTask<Void, Void, byte[]> {
    private static final String TAG = PostersQueryTask.class.getSimpleName();

    private final Movie movie;
    private final Activity activity;
    private final ImageView posterView;

    public PostersQueryTask(Movie movie, Activity activity, ImageView imageView) {
        this.movie = movie;
        this.activity = activity;
        this.posterView = imageView;
    }

    @Override
    protected byte[] doInBackground(Void... params) {
        byte[] posterContent = null;
        ContentResolver contentResolver = activity.getContentResolver();
        Uri targetUri = PopularMoviesContract.PosterEntry.buildPosterUriForMovie(movie.getMovieId());
        Cursor cursor = null;

        try {
            cursor = contentResolver.query(targetUri, null, null, null, PopularMoviesContract.PosterEntry._ID);

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

        if (posterView != null) {
            posterView.setImageBitmap(bitmap);
            Log.i(TAG, String.format("poster for movie %s was loaded from database", movie.getTitle()));
        }
    }
}
