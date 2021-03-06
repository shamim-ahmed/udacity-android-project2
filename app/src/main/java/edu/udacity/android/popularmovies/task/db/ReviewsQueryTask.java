package edu.udacity.android.popularmovies.task.db;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import edu.udacity.android.popularmovies.db.PopularMoviesContract;
import edu.udacity.android.popularmovies.model.Movie;
import edu.udacity.android.popularmovies.model.Review;
import edu.udacity.android.popularmovies.util.AppUtils;

public class ReviewsQueryTask extends AsyncTask<Void, Void, List<Review>> {
    private static final String TAG = ReviewsQueryTask.class.getSimpleName();

    private final Activity activity;
    private final Movie movie;

    public ReviewsQueryTask(Movie movie, Activity activity) {
        this.activity = activity;
        this.movie = movie;
    }

    @Override
    protected List<Review> doInBackground(Void... params) {
        List<Review> reviewList = new ArrayList<>();
        ContentResolver contentResolver = activity.getContentResolver();
        Uri targetUri = PopularMoviesContract.ReviewEntry.buildReviewUriForMovie(movie.getMovieId());
        Cursor cursor = null;

        try {
            cursor = contentResolver.query(targetUri, null, null, null, PopularMoviesContract.ReviewEntry._ID);

            while (cursor.moveToNext()) {
                ContentValues values = AppUtils.readReviewFromCursor(cursor);
                reviewList.add(new Review(values));
            }

            Log.i(TAG, String.format("%d reviews for movie %s were loaded from database", reviewList.size(), movie.getTitle()));
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return reviewList;
    }

    @Override
    protected void onPostExecute(List<Review> reviewList) {
        movie.setReviewList(reviewList);
        AppUtils.displayReviewsForMovie(movie, activity);
    }
}
