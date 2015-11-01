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

import edu.udacity.android.popularmovies.PopularMoviesApplication;
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
    private final PopularMoviesApplication application;

    public MovieIsFavoriteQueryTask(Activity activity, Movie movie) {
        this.activity = activity;
        this.movie = movie;
        application = (PopularMoviesApplication) activity.getApplication();
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

        return result;
    }

    @Override
    protected void onPostExecute(Boolean result) {
        if (result) {
            loadPosterFromDatabase();
            loadTrailersFromDatabase();
            loadReviewsFromDatabase();
        } else {
            startPosterDownload();
            startTrailerDataDownload();
            startReviewDataDownload();
        }

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
        String scheme = application.getConfigurationProperty("tmdb.api.scheme");
        String authority = application.getConfigurationProperty("tmdb.api.authority");
        String videoPath = application.getConfigurationProperty("tmdb.api.videos.path", movie.getMovieId().toString());
        String apiKey = application.getConfigurationProperty("tmdb.api.key");

        Uri trailerUri = new Uri.Builder().scheme(scheme)
                .authority(authority)
                .path(videoPath)
                .appendQueryParameter("api_key", apiKey)
                .build();

        Log.i(TAG, String.format("The trailer Uri is : %s", trailerUri.toString()));

        TrailerDataDownloadTask trailerDownloadTask = new TrailerDataDownloadTask(movie, activity);
        trailerDownloadTask.execute(trailerUri);
    }

    private void startReviewDataDownload() {
        String scheme = application.getConfigurationProperty("tmdb.api.scheme");
        String authority = application.getConfigurationProperty("tmdb.api.authority");
        String path = application.getConfigurationProperty("tmdb.api.reviews.path", movie.getMovieId().toString());
        String apiKey = application.getConfigurationProperty("tmdb.api.key");

        Uri reviewDataUri = new Uri.Builder().scheme(scheme)
                .authority(authority)
                .path(path)
                .appendQueryParameter("api_key", apiKey)
                .build();

        Log.i(TAG, String.format("The review URI is : %s", reviewDataUri.toString()));

        ReviewDataDownloadTask task = new ReviewDataDownloadTask(movie, activity);
        task.execute(reviewDataUri);
    }

    private void loadPosterFromDatabase() {
        PosterQueryTask task = new PosterQueryTask(movie, activity);
        task.execute();
    }

    private void loadTrailersFromDatabase() {
        TrailerQueryTask task = new TrailerQueryTask(movie, activity);
        task.execute();
    }

    private void loadReviewsFromDatabase() {
        ReviewQueryTask task = new ReviewQueryTask(movie, activity);
        task.execute();
    }
}
