package edu.udacity.android.popularmovies.task.db;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import java.util.List;

import edu.udacity.android.popularmovies.PopularMoviesApplication;
import edu.udacity.android.popularmovies.R;
import edu.udacity.android.popularmovies.db.PopularMoviesContract;
import edu.udacity.android.popularmovies.model.Movie;
import edu.udacity.android.popularmovies.model.Review;
import edu.udacity.android.popularmovies.model.Trailer;
import edu.udacity.android.popularmovies.util.Constants;
import edu.udacity.android.popularmovies.util.DbUtils;

public class MovieInsertTask extends AsyncTask<Void, Void, Uri> {
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
    protected Uri doInBackground(Void... params) {
        Uri targetUri = PopularMoviesContract.MovieEntry.CONTENT_URI;
        ContentResolver contentResolver = activity.getContentResolver();
        ContentValues movieValues = DbUtils.convertMovie(movie);
        Uri resultUri = contentResolver.insert(targetUri, movieValues);

        Log.i(TAG, String.format("movie %s was saved to database", movie.getTitle()));

        // save the poster
        Uri posterUri = movie.getPosterUri();

        if (posterUri != null) {
            String posterId = posterUri.getLastPathSegment();
            ContentValues posterValues = DbUtils.convertPoster(posterId, posterContent, movie.getMovieId());
            contentResolver.insert(PopularMoviesContract.PosterEntry.CONTENT_URI, posterValues);
            Log.i(TAG, String.format("poster with id %s was saved to database", posterId));
        }

        // save the trailers
        List<Trailer> trailerList = movie.getTrailerList();

        if (trailerList.size() > 0) {
            ContentValues[] trailerValues = DbUtils.convertTrailers(trailerList);
            int trailerCount = contentResolver.bulkInsert(PopularMoviesContract.TrailerEntry.CONTENT_URI, trailerValues);
            Log.i(TAG, String.format("%d trailers were saved to database", trailerCount));
        }

        // save the reviews
        List<Review> reviewList = movie.getReviewList();

        if (reviewList.size() > 0) {
            ContentValues[] reviewValues = DbUtils.convertReviews(reviewList);
            int reviewCount = contentResolver.bulkInsert(PopularMoviesContract.ReviewEntry.CONTENT_URI, reviewValues);
            Log.i(TAG, String.format("%d reviews were saved to database", reviewCount));
        }

        return resultUri;
    }

    @Override
    protected void onPostExecute(Uri resultUri) {
        if (resultUri == null) {
            Log.e(TAG, "favorite movie insertion failed");
            return;
        }

        // show a toast message
        CharSequence toastMessage = activity.getResources().getText(R.string.favorite_movie_toast_text);
        Toast.makeText(activity, toastMessage, Toast.LENGTH_SHORT).show();

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
