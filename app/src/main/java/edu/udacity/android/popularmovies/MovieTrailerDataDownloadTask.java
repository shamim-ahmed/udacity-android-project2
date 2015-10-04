package edu.udacity.android.popularmovies;

import android.net.Uri;
import android.os.AsyncTask;

import java.util.List;

import edu.udacity.android.popularmovies.util.MovieTrailer;

public class MovieTrailerDataDownloadTask extends AsyncTask<Uri, Void, List<MovieTrailer>> {

    @Override
    protected List<MovieTrailer> doInBackground(Uri... params) {
        return null;
    }
}
