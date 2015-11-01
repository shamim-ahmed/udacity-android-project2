package edu.udacity.android.popularmovies.task.db;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import java.util.ArrayList;
import java.util.List;

import edu.udacity.android.popularmovies.db.PopularMoviesContract;
import edu.udacity.android.popularmovies.model.Movie;
import edu.udacity.android.popularmovies.model.Trailer;
import edu.udacity.android.popularmovies.util.AppUtils;

public class TrailerQueryTask extends AsyncTask<Void, Void, List<Trailer>> {
    private static final String TAG = TrailerQueryTask.class.getSimpleName();

    private final Activity activity;
    private final Movie movie;

    public TrailerQueryTask(Movie movie, Activity activity) {
        this.activity = activity;
        this.movie = movie;
    }

    @Override
    protected List<Trailer> doInBackground(Void... params) {
        List<Trailer> trailerList = new ArrayList<>();
        ContentResolver contentResolver = activity.getContentResolver();
        Uri targetUri = PopularMoviesContract.TrailerEntry.buildTrailerUriForMovie(movie.getMovieId());
        Cursor cursor = null;

        try {
            cursor = contentResolver.query(targetUri, null, null, null, PopularMoviesContract.TrailerEntry._ID);

            while (cursor.moveToNext()) {
                ContentValues values = AppUtils.readTrailerFromCursor(cursor);
                trailerList.add(new Trailer(values));
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return trailerList;
    }

    @Override
    protected void onPostExecute(List<Trailer> trailerList) {
        movie.setTrailerList(trailerList);
        AppUtils.displayTrailersForMovie(movie, activity);

        if (trailerList.size() > 0) {
            Trailer firstTrailer = trailerList.get(0);
            String trailerTitle = String.format("%s : %s", movie.getTitle(), firstTrailer.getName());
            String trailerKey = firstTrailer.getKey();
            AppUtils.updateShareMenuItem(trailerTitle, trailerKey, activity);
        }
    }
}
