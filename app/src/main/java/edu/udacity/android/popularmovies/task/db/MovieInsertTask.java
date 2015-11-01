package edu.udacity.android.popularmovies.task.db;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Button;

import java.util.List;

import edu.udacity.android.popularmovies.PopularMoviesApplication;
import edu.udacity.android.popularmovies.R;
import edu.udacity.android.popularmovies.db.PopularMoviesContract;
import edu.udacity.android.popularmovies.model.Movie;
import edu.udacity.android.popularmovies.model.Review;
import edu.udacity.android.popularmovies.model.Trailer;
import edu.udacity.android.popularmovies.util.Constants;
import edu.udacity.android.popularmovies.util.DbUtils;

public class MovieInsertTask extends AsyncTask<Uri, Void, Uri> {
    private static final String TAG = MovieInsertTask.class.getSimpleName();

    private final PopularMoviesApplication application;
    private final Activity activity;
    private final Movie movie;
    private final byte[] posterContent;

    public MovieInsertTask(Movie movie, byte[] posterContent, Activity activity, PopularMoviesApplication application) {
        this.application = application;
        this.activity = activity;
        this.movie = movie;
        this.posterContent = posterContent;
    }

    @Override
    protected Uri doInBackground(Uri... params) {
        if (params.length == 0) {
            Log.e(TAG, "no uri provided for insert operation");
        }

        Uri targetUri = params[0];
        ContentResolver contentResolver = activity.getContentResolver();
        ContentValues movieValues = DbUtils.convertMovie(movie);
        Uri resultUri = contentResolver.insert(targetUri, movieValues);

        // save the poster
        Uri posterUri = movie.getPosterUri();

        if (posterUri != null) {
            String posterId = posterUri.getLastPathSegment();
            ContentValues posterValues = DbUtils.convertPoster(posterId, posterContent, movie.getMovieId());
            contentResolver.insert(PopularMoviesContract.PosterEntry.CONTENT_URI, posterValues);
            Log.i(TAG, String.format("poster with id %s was inserted successfully", posterId));
        }

        // save the trailers
        List<Trailer> trailerList = movie.getTrailerList();

        if (trailerList.size() > 0) {
            Uri trailerUriForMovie = PopularMoviesContract.TrailerEntry.buildTrailerUriForMovie(movie.getMovieId());
            ContentValues[] trailerValues = DbUtils.convertTrailers(trailerList);
            int trailerCount = contentResolver.bulkInsert(PopularMoviesContract.TrailerEntry.CONTENT_URI, trailerValues);
            Log.i(TAG, String.format("%d trailers were inserted", trailerCount));
        }

        // save the reviews
        List<Review> reviewList = movie.getReviewList();

        if (reviewList.size() > 0) {
            Uri reviewUriForMovie = PopularMoviesContract.ReviewEntry.buildReviewUriForMovie(movie.getMovieId());
            ContentValues[] reviewValues = DbUtils.convertReviews(reviewList);
            int reviewCount = contentResolver.bulkInsert(PopularMoviesContract.ReviewEntry.CONTENT_URI, reviewValues);
            Log.i(TAG, String.format("%d reviews were inserted", reviewCount));
        }

        return resultUri;
    }

    @Override
    protected void onPostExecute(Uri resultUri) {
        if (resultUri == null) {
            Log.e(TAG, "favorite movie insertion failed");
            return;
        }

        // set the state of the favorite button to selected
        Button favoriteButton = (Button) activity.findViewById(R.id.favorite_button);

        // NOTE : occasionally an NPE is encountered when the user tries to select
        // a movie from the grid while the screen is getting rotated. In order to prevent
        // the NPE, we include a check.
        if (favoriteButton == null) {
            return;
        }

        favoriteButton.setText(R.string.favorite_button_added_text);
        favoriteButton.setSelected(true);

        // if main grid shows favorite movies,set the reload flag
        String sortPreference = application.getActiveSortPreference();

        if (Constants.SORT_FAVORITE.equals(sortPreference)) {
            application.setReloadFlag(true);
        }
    }
}
