package edu.udacity.android.popularmovies.task;

import android.app.Activity;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageButton;

import edu.udacity.android.popularmovies.R;
import edu.udacity.android.popularmovies.model.Movie;

public class FavoriteMovieSingleQueryTask extends AsyncTask<Uri, Void, Boolean> {
    private static final String TAG = FavoriteMovieSingleQueryTask.class.getSimpleName();

    private final Activity activity;

    public FavoriteMovieSingleQueryTask(Activity activity) {
        this.activity = activity;
    }

    @Override
    protected Boolean doInBackground(Uri... params) {
        if (params.length == 0) {
            Log.e(TAG, "no uri provided for query");
        }

        Uri targetUri = params[0];
        ContentResolver contentResolver = activity.getContentResolver();
        Cursor cursor = null;
        Boolean result = Boolean.FALSE;

        try {
            cursor = contentResolver.query(targetUri, null, null, null, null);
            result = cursor.moveToFirst();
        } finally {
            if (cursor != null) {
                cursor.close();;
            }
        }

        return result;
    }

    @Override
    protected void onPostExecute(Boolean result) {
        ImageButton favoriteButton = (ImageButton) activity.findViewById(R.id.favorite_button);
        favoriteButton.setSelected(result);
    }
}
