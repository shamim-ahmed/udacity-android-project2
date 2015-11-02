package edu.udacity.android.popularmovies.task.web;

import android.app.Activity;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import edu.udacity.android.popularmovies.PopularMoviesApplication;
import edu.udacity.android.popularmovies.model.Movie;
import edu.udacity.android.popularmovies.model.Review;
import edu.udacity.android.popularmovies.util.AppUtils;

public class ReviewsDataDownloadTask extends AsyncTask<Void, Void, List<Review>> {
    private static final String TAG = ReviewsDataDownloadTask.class.getSimpleName();

    private final Movie movie;
    private final Activity activity;

    public ReviewsDataDownloadTask(Movie movie, Activity activity) {
        this.movie = movie;
        this.activity = activity;
    }

    @Override
    protected List<Review> doInBackground(Void... params) {
        PopularMoviesApplication application = (PopularMoviesApplication) activity.getApplication();
        String scheme = application.getConfigurationProperty("tmdb.api.scheme");
        String authority = application.getConfigurationProperty("tmdb.api.authority");
        String path = application.getConfigurationProperty("tmdb.api.reviews.path", movie.getMovieId().toString());
        String apiKey = application.getConfigurationProperty("tmdb.api.key");

        Uri reviewDataUri = new Uri.Builder().scheme(scheme)
                .authority(authority)
                .path(path)
                .appendQueryParameter("api_key", apiKey)
                .build();

        String urlString = reviewDataUri.toString();
        Log.i(TAG, String.format("The review URI is : %s", urlString));

        List<Review> reviewList = new ArrayList<>();

        try {
            URL url = new URL(urlString);
            BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
            StringBuilder jsonBuilder = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                jsonBuilder.append(line);
            }

            String jsonString = jsonBuilder.toString();
            JSONObject jsonObject = new JSONObject(jsonString);
            JSONArray jsonArray = jsonObject.getJSONArray("results");

            for (int i = 0, n = jsonArray.length(); i < n; i++) {
                Review review = new Review(jsonArray.getJSONObject(i), movie.getMovieId());
                reviewList.add(review);
            }
        } catch (MalformedURLException ex) {
            Log.e(TAG, String.format("Malformed URL : %s", urlString));
        } catch (IOException ex) {
            Log.e(TAG, String.format("An error occurred while accessing the URL %s", urlString));
        } catch (JSONException ex) {
            Log.e(TAG, String.format("An error occurred while parsing json data fetched from the URL %s", urlString));
        }

        return reviewList;
    }

    @Override
    protected void onPostExecute(List<Review> reviewList) {
        movie.setReviewList(reviewList);
        AppUtils.displayReviewsForMovie(movie, activity);
    }
}
