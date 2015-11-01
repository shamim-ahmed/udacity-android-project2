package edu.udacity.android.popularmovies.listener;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.view.View;

import edu.udacity.android.popularmovies.PopularMoviesApplication;
import edu.udacity.android.popularmovies.util.AppUtils;

public class MovieTrailerOnClickListener implements View.OnClickListener {
    private final Activity activity;
    private final String trailerKey;

    public MovieTrailerOnClickListener(Activity activity, String trailerKey) {
        this.activity = activity;
        this.trailerKey = trailerKey;
    }

    @Override
    public void onClick(View v) {
        PopularMoviesApplication application = (PopularMoviesApplication) activity.getApplication();
        Uri trailerUri = AppUtils.createTrailerUri(trailerKey, application);
        Intent intent = new Intent(Intent.ACTION_VIEW, trailerUri);
        activity.startActivity(intent);
    }
}