package edu.udacity.android.popularmovies.task;

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
import java.util.Collections;
import java.util.List;

import edu.udacity.android.popularmovies.adapter.MovieTrailerAdapter;
import edu.udacity.android.popularmovies.util.IOUtils;
import edu.udacity.android.popularmovies.model.MovieTrailer;

public class MovieTrailerDataDownloadTask extends AsyncTask<Uri, Void, List<MovieTrailer>> {
    private static final String TAG = MovieTrailerDataDownloadTask.class.getSimpleName();

    private final MovieTrailerAdapter adapter;

    public MovieTrailerDataDownloadTask(MovieTrailerAdapter adapter) {
        this.adapter = adapter;
    }

    @Override
    protected List<MovieTrailer> doInBackground(Uri... params) {
        if (params.length < 1) {
            Log.e(TAG, "No Uri provided for trailer data download");
            return Collections.emptyList();
        }

        BufferedReader reader = null;
        StringBuilder jsonBuilder = new StringBuilder();
        List<MovieTrailer> trailerList = new ArrayList<>();

        try {
            URL url = new URL(params[0].toString());
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
            Log.e(TAG, String.format("Malformed URL : %s", params[0]));
        } catch (IOException ex) {
            Log.e(TAG, String.format("An error occurred while fetching data from URL %s", params[0]));
        } catch (JSONException ex) {
            Log.e(TAG, String.format("An error occurred while parsing json data returned from %s", params[0]));
        } finally {
            IOUtils.close(reader);
        }

        return trailerList;
    }

    protected void onPostExecute(List<MovieTrailer> trailerList) {
        adapter.clear();
        adapter.addAll(trailerList);
    }
}