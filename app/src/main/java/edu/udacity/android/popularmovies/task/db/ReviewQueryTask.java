package edu.udacity.android.popularmovies.task.db;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import edu.udacity.android.popularmovies.model.Movie;
import edu.udacity.android.popularmovies.model.Review;
import edu.udacity.android.popularmovies.util.AppUtils;

public class ReviewQueryTask extends AsyncTask<Uri, Void, List<Review>> {
    private static final String TAG = ReviewQueryTask.class.getSimpleName();

    private final Activity activity;
    private final Movie movie;

    public ReviewQueryTask(Activity activity, Movie movie) {
        this.activity = activity;
        this.movie = movie;
    }

    @Override
    protected List<Review> doInBackground(Uri... params) {
        if (params.length == 0) {
            Log.e(TAG, "no uri provided");
            return Collections.emptyList();
        }

        List<Review> reviewList = new ArrayList<>();
        ContentResolver contentResolver = activity.getContentResolver();
        Uri targetUri = params[0];
        Cursor cursor = null;

        try {
            cursor = contentResolver.query(targetUri, null, null, null, null);

            while (cursor.moveToNext()) {
                ContentValues values = AppUtils.readReviewFromCursor(cursor);
                reviewList.add(new Review(values));
            }
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
    }
}
