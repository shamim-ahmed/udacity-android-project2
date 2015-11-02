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
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import edu.udacity.android.popularmovies.PopularMoviesApplication;
import edu.udacity.android.popularmovies.model.Movie;
import edu.udacity.android.popularmovies.model.Trailer;
import edu.udacity.android.popularmovies.util.AppUtils;
import edu.udacity.android.popularmovies.util.IOUtils;

public class TrailersDataDownloadTask extends AsyncTask<Void, Void, List<Trailer>> {
    private static final String TAG = TrailersDataDownloadTask.class.getSimpleName();

    private final Movie movie;
    private final Activity activity;

    public TrailersDataDownloadTask(Movie movie, Activity activity) {
        this.movie = movie;
        this.activity = activity;
    }

    @Override
    protected List<Trailer> doInBackground(Void... params) {
        PopularMoviesApplication application = (PopularMoviesApplication) activity.getApplication();
        String scheme = application.getConfigurationProperty("tmdb.api.scheme");
        String authority = application.getConfigurationProperty("tmdb.api.authority");
        String videoPath = application.getConfigurationProperty("tmdb.api.videos.path", movie.getMovieId().toString());
        String apiKey = application.getConfigurationProperty("tmdb.api.key");

        Uri trailerUri = new Uri.Builder().scheme(scheme)
                .authority(authority)
                .path(videoPath)
                .appendQueryParameter("api_key", apiKey)
                .build();

        String urlString = trailerUri.toString();
        Log.i(TAG, String.format("The trailer Uri is : %s", urlString));

        BufferedReader reader = null;
        StringBuilder jsonBuilder = new StringBuilder();
        List<Trailer> trailerList = new ArrayList<>();

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
                    Trailer trailer = new Trailer(results.getJSONObject(i), movie.getMovieId());
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

    protected void onPostExecute(List<Trailer> trailerList) {
        movie.setTrailerList(trailerList);
        AppUtils.displayTrailersForMovie(movie, activity);

        if (trailerList.size() > 0) {
            Trailer firstTrailer = trailerList.get(0);
            String trailerTitle = String.format("%s : %s", movie.getTitle(), firstTrailer.getName());
            String trailerKey = firstTrailer.getKey();
            AppUtils.updateShareMenuItem(trailerTitle, trailerKey, activity);
        }
    }
}
