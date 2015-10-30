package edu.udacity.android.popularmovies.task.db;

import android.app.Activity;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import edu.udacity.android.popularmovies.R;

public class MovieSingleQueryTask extends AsyncTask<Uri, Void, Boolean> {
    private static final String TAG = MovieSingleQueryTask.class.getSimpleName();

    private final Activity activity;

    public MovieSingleQueryTask(Activity activity) {
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
                cursor.close();
            }
        }

        return result;
    }

    @Override
    protected void onPostExecute(Boolean result) {
        Button favoriteButton = (Button) activity.findViewById(R.id.favorite_button);

        // NOTE : occasionally an NPE is encountered when the user tries to select
        // a movie from the grid while the screen is getting rotated. In order to prevent
        // the NPE, we include a check.
        if (favoriteButton == null) {
            return;
        }

        favoriteButton.setSelected(result);
        favoriteButton.setEnabled(!result);

        if (result) {
            favoriteButton.setText(R.string.favorite_button_remove_text);
        } else {
            favoriteButton.setText(R.string.favorite_button_add_text);
        }

        favoriteButton.setVisibility(View.VISIBLE);
    }
}
