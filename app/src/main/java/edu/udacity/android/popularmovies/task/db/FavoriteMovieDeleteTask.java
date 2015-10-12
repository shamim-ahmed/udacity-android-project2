package edu.udacity.android.popularmovies.task.db;

import android.app.Activity;
import android.content.ContentResolver;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageButton;

import edu.udacity.android.popularmovies.PopularMoviesApplication;
import edu.udacity.android.popularmovies.R;
import edu.udacity.android.popularmovies.util.Constants;

public class FavoriteMovieDeleteTask extends AsyncTask<Uri, Void, Integer> {
    private static final String TAG = FavoriteMovieDeleteTask.class.getSimpleName();

    private final PopularMoviesApplication application;
    private final Activity activity;

    public FavoriteMovieDeleteTask(PopularMoviesApplication application, Activity activity) {
        this.application = application;
        this.activity = activity;
    }

    @Override
    protected Integer doInBackground(Uri... params) {
        if (params.length == 0) {
            Log.e(TAG, "no uri provided for delete operation");
            return 0;
        }

        Uri targetUri = params[0];
        ContentResolver contentResolver = activity.getContentResolver();
        return contentResolver.delete(targetUri, null, null);
    }

    @Override
    protected void onPostExecute(Integer result) {
        if (result == 0) {
            Log.e(TAG, "delete operation failed");
            return;
        }

        // set the state of the star button to un-selected
        ImageButton favoriteButton = (ImageButton) activity.findViewById(R.id.favorite_button);
        favoriteButton.setSelected(false);

        // if main grid shows favorite movies,set the reload flag
        String sortPreference = application.getActiveSortPreference();

        if (Constants.SORT_FAVORITE.equals(sortPreference)) {
            application.setReloadFlag(true);
        }
    }
}
