package edu.udacity.android.popularmovies.task;

import android.net.Uri;
import android.os.AsyncTask;

import java.util.List;

import edu.udacity.android.popularmovies.model.Movie;

public class FavoriteMovieBulkQueryTask extends AsyncTask<Uri, Void, List<Movie>> {
    @Override
    protected List<Movie> doInBackground(Uri... params) {
        return null;
    }
}
