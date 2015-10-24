package edu.udacity.android.popularmovies.task.web;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import edu.udacity.android.popularmovies.PopularMoviesApplication;
import edu.udacity.android.popularmovies.R;
import edu.udacity.android.popularmovies.util.IOUtils;
import edu.udacity.android.popularmovies.model.MovieTrailer;

public class MovieTrailerDataDownloadTask extends AsyncTask<Uri, Void, List<MovieTrailer>> {
    private static final String TAG = MovieTrailerDataDownloadTask.class.getSimpleName();

    private final Activity activity;

    public MovieTrailerDataDownloadTask(Activity activity) {
        this.activity = activity;
    }

    @Override
    protected List<MovieTrailer> doInBackground(Uri... params) {
        if (params.length == 0) {
            Log.e(TAG, "No Uri provided for trailer data download");
            return Collections.emptyList();
        }

        BufferedReader reader = null;
        StringBuilder jsonBuilder = new StringBuilder();
        List<MovieTrailer> trailerList = new ArrayList<>();
        String urlString = params[0].toString();

        try {
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;

            while ((line = reader.readLine()) != null) {
                jsonBuilder.append(line);
            }

            String jsonStr = jsonBuilder.toString();
            Log.i(TAG, String.format("Trailer data : %s", jsonStr));

            JSONObject jsonObject = new JSONObject(jsonStr);
            JSONArray results = jsonObject.getJSONArray("results");

            if (results != null) {
                for (int i = 0, n = results.length(); i < n; i++) {
                    MovieTrailer trailer = new MovieTrailer(results.getJSONObject(i));
                    trailerList.add(trailer);
                }
            }
        } catch (MalformedURLException ex) {
            Log.e(TAG, String.format("Malformed URL : %s", urlString));
        } catch (IOException ex) {
            Log.e(TAG, String.format("An error occurred while fetching data from URL %s", urlString));
        } catch (JSONException ex) {
            Log.e(TAG, String.format("An error occurred while parsing json data returned from %s", urlString));
        } finally {
            IOUtils.close(reader);
        }

        return trailerList;
    }

    protected void onPostExecute(List<MovieTrailer> trailerList) {
        LinearLayout linearLayout = (LinearLayout) activity.findViewById(R.id.movie_trailers);
        LayoutInflater inflater = activity.getLayoutInflater();

        if (trailerList.size() > 0) {
            View titleView = inflater.inflate(R.layout.movie_trailers_title, linearLayout, false);
            linearLayout.addView(titleView);
        }

        for (MovieTrailer trailer : trailerList) {
            View view = inflater.inflate(R.layout.movie_trailer, linearLayout, false);
            Button trailerButton = (Button) view.findViewById(R.id.movie_trailer_item);
            trailerButton.setText(trailer.getName());
            trailerButton.setOnClickListener(new MovieTrailerOnClickListener(trailer.getKey()));
            linearLayout.addView(view);
        }
    }

    private class MovieTrailerOnClickListener implements View.OnClickListener {

        private final String trailerKey;

        public MovieTrailerOnClickListener(String trailerKey) {
            this.trailerKey = trailerKey;
        }

        @Override
        public void onClick(View v) {
            PopularMoviesApplication application = (PopularMoviesApplication) activity.getApplication();
            String scheme = application.getConfigurationProperty("youtube.video.scheme");
            String authority = application.getConfigurationProperty("youtube.video.authority");
            String path = application.getConfigurationProperty("youtube.video.path");

            Uri trailerUri = new Uri.Builder()
                    .scheme(scheme)
                    .authority(authority)
                    .path(path)
                    .appendQueryParameter("v", trailerKey)
                    .build();

            Intent intent = new Intent(Intent.ACTION_VIEW, trailerUri);
            activity.startActivity(intent);
        }
    }
}
