package edu.udacity.android.popularmovies.task.web;

import android.app.Activity;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import edu.udacity.android.popularmovies.R;
import edu.udacity.android.popularmovies.model.MovieReview;

public class MovieReviewDataDownloadTask extends AsyncTask<Uri, Void, List<MovieReview>> {
    private static final String TAG = MovieReviewDataDownloadTask.class.getSimpleName();

    private final Activity activity;

    public MovieReviewDataDownloadTask(Activity activity) {
        this.activity = activity;
    }

    @Override
    protected List<MovieReview> doInBackground(Uri... params) {
        if (params.length == 0) {
            Log.e(TAG, "No Uri provided for movie review download");
            return Collections.emptyList();
        }

        String urlString = params[0].toString();
        List<MovieReview> reviewList = new ArrayList<>();

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
                MovieReview review = new MovieReview(jsonArray.getJSONObject(i));
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
    protected void onPostExecute(List<MovieReview> reviewList) {
        LinearLayout linearLayout = (LinearLayout) activity.findViewById(R.id.movie_reviews);
        LayoutInflater inflater = activity.getLayoutInflater();

        for (MovieReview review : reviewList) {
            View view = inflater.inflate(R.layout.movie_review, linearLayout, false);
            TextView reviewContentView = (TextView) view.findViewById(R.id.movie_review_content);
            reviewContentView.setText(review.getContent());
            linearLayout.addView(view);
        }
    }
}
