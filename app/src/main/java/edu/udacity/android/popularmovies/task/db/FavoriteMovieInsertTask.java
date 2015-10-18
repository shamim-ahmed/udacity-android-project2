package edu.udacity.android.popularmovies.task.db;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Button;

import edu.udacity.android.popularmovies.PopularMoviesApplication;
import edu.udacity.android.popularmovies.R;
import edu.udacity.android.popularmovies.util.Constants;

public class FavoriteMovieInsertTask extends AsyncTask<Uri, Void, Uri> {
    private static final String TAG = FavoriteMovieInsertTask.class.getSimpleName();

    private final PopularMoviesApplication application;
    private final Activity activity;
    private final ContentValues values;

    public FavoriteMovieInsertTask(PopularMoviesApplication application, Activity activity, ContentValues values) {
        this.application = application;
        this.activity = activity;
        this.values = values;
    }

    @Override
    protected Uri doInBackground(Uri... params) {
        if (params.length == 0) {
            Log.e(TAG, "no uri provided for insert operation");
        }

        Uri targetUri = params[0];
        ContentResolver contentResolver = activity.getContentResolver();
        return contentResolver.insert(targetUri, values);
    }

    @Override
    protected void onPostExecute(Uri resultUri) {
        if (resultUri == null) {
            Log.e(TAG, "favorite movie insertion failed");
            return;
        }

        // set the state of the star button to selected
        Button favoriteButton = (Button) activity.findViewById(R.id.favorite_button);
        favoriteButton.setText(R.string.favorite_button_remove_text);
        favoriteButton.setSelected(true);

        // if main grid shows favorite movies,set the reload flag
        String sortPreference = application.getActiveSortPreference();

        if (Constants.SORT_FAVORITE.equals(sortPreference)) {
            application.setReloadFlag(true);
        }
    }
}
