package edu.udacity.android.popularmovies.task.db;

import android.app.Activity;
import android.content.ContentResolver;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Button;

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

        // set the state of the favorite button to un-selected
        Button favoriteButton = (Button) activity.findViewById(R.id.favorite_button);

        // NOTE : occasionally an NPE is encourntered when the user tries to select
        // a movie from the grid while the screen is getting rotated. In order to prevent
        // the NPE, we include a check.
        if (favoriteButton == null) {
            return;
        }

        favoriteButton.setText(R.string.favorite_button_add_text);
        favoriteButton.setSelected(false);

        // if main grid shows favorite movies,set the reload flag
        String sortPreference = application.getActiveSortPreference();

        if (Constants.SORT_FAVORITE.equals(sortPreference)) {
            application.setReloadFlag(true);
        }
    }
}
