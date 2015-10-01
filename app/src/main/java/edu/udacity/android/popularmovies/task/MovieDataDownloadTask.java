package edu.udacity.android.popularmovies.task;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.GridView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import edu.udacity.android.popularmovies.model.Movie;
import edu.udacity.android.popularmovies.adapter.MovieGridAdapter;
import edu.udacity.android.popularmovies.util.IOUtils;

public class MovieDataDownloadTask extends AsyncTask<Uri, String, List<Movie>> {
    private static final String TAG = MovieDataDownloadTask.class.getSimpleName();

    private final GridView gridView;

    public MovieDataDownloadTask(GridView gridView) {
        this.gridView = gridView;
    }

    @Override
    protected List<Movie> doInBackground(Uri... params) {
        if (params.length < 2) {
            Log.w(TAG, "Not enough arguments");
            return null;
        }

        BufferedReader reader = null;
        List<Movie> movieList = Collections.emptyList();

        try {
            URL movieDataUrl = new URL(params[0].toString());
            InputStream in = movieDataUrl.openStream();
            reader = new BufferedReader(new InputStreamReader(in));

            StringBuilder builder = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }

            JSONObject jsonData = new JSONObject(builder.toString());
            movieList = parseMovieData(jsonData, params[1]);
        } catch (IOException | JSONException ex) {
            Log.e(TAG, "An error occurred while fetching and parsing json data", ex);
        } finally {
            IOUtils.close(reader);
        }

        return movieList;
    }

    @Override
    protected void onPostExecute(List<Movie> movieList) {
        gridView.invalidateViews();

        MovieGridAdapter adapter = (MovieGridAdapter) gridView.getAdapter();
        adapter.clear();
        adapter.addAll(movieList);
    }

    private static List<Movie> parseMovieData(JSONObject jsonData, Uri posterBaseUri) throws JSONException {
        if (jsonData == null) {
            return Collections.emptyList();
        }

        List<Movie> movieList = new ArrayList<>();
        JSONArray jsonArray = jsonData.getJSONArray("results");

        for (int i = 0, n = jsonArray.length(); i < n; i++) {
            JSONObject jsonObj = (JSONObject) jsonArray.get(i);
            Movie movie = new Movie(jsonObj, posterBaseUri);
            movieList.add(movie);
        }

        return movieList;
    }
}
