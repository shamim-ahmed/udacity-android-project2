package edu.udacity.android.popularmovies.task.db;

import android.app.Activity;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import edu.udacity.android.popularmovies.R;
import edu.udacity.android.popularmovies.db.PopularMoviesContract;
import edu.udacity.android.popularmovies.model.Movie;
import edu.udacity.android.popularmovies.task.web.ReviewDataDownloadTask;
import edu.udacity.android.popularmovies.task.web.TrailerDataDownloadTask;
import edu.udacity.android.popularmovies.util.AppUtils;

public class MovieIsFavoriteQueryTask extends AsyncTask<Void, Void, Boolean> {
    private static final String TAG = MovieIsFavoriteQueryTask.class.getSimpleName();

    private final Movie movie;
    private final Activity activity;

    public MovieIsFavoriteQueryTask(Activity activity, Movie movie) {
        this.activity = activity;
        this.movie = movie;
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        Uri targetUri = PopularMoviesContract.MovieEntry.buildMovieUri(movie.getMovieId());
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

        if (result) {
            loadPosterFromDatabase();
            loadTrailersFromDatabase();
            loadReviewsFromDatabase();
        } else {
            startPosterDownload();
            startTrailerDataDownload();
            startReviewDataDownload();
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
            favoriteButton.setText(R.string.favorite_button_added_text);
        } else {
            favoriteButton.setText(R.string.favorite_button_add_text);
        }

        favoriteButton.setVisibility(View.VISIBLE);
    }

    private void startPosterDownload() {
        Log.i(TAG, String.format("starting poster download for movie %s", movie.getTitle()));

        int moviePlaceHolderId;

        if (AppUtils.isTablet(activity)) {
            moviePlaceHolderId = R.drawable.movie_placeholder;
        } else {
            moviePlaceHolderId = R.drawable.movie_placeholder_small;
        }

        ImageView posterView = (ImageView) activity.findViewById(R.id.movie_details_poster);

        // display the poster
        Picasso.with(activity)
                .load(movie.getPosterUri())
                .noFade()
                .placeholder(moviePlaceHolderId)
                .into(posterView);
    }

    private void startTrailerDataDownload() {
        Log.i(TAG, String.format("starting trailer download for movie %s", movie.getTitle()));

        TrailerDataDownloadTask trailerDownloadTask = new TrailerDataDownloadTask(movie, activity);
        trailerDownloadTask.execute();
    }

    private void startReviewDataDownload() {
        Log.i(TAG, String.format("starting review download for movie %s", movie.getTitle()));

        ReviewDataDownloadTask task = new ReviewDataDownloadTask(movie, activity);
        task.execute();
    }

    private void loadPosterFromDatabase() {
        Log.i(TAG, String.format("loading poster for movie %s from database", movie.getTitle()));

        ImageView posterView = (ImageView) activity.findViewById(R.id.movie_details_poster);
        PostersQueryTask task = new PostersQueryTask(movie, activity, posterView);
        task.execute();
    }

    private void loadTrailersFromDatabase() {
        Log.i(TAG, String.format("loading trailer data for movie %s from database", movie.getTitle()));

        TrailersQueryTask task = new TrailersQueryTask(movie, activity);
        task.execute();
    }

    private void loadReviewsFromDatabase() {
        Log.i(TAG, String.format("loading review data for movie %s from database", movie.getTitle()));

        ReviewsQueryTask task = new ReviewsQueryTask(movie, activity);
        task.execute();
    }
}
