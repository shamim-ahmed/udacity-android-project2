package edu.udacity.android.popularmovies;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import edu.udacity.android.popularmovies.task.MovieDataDownloadTask;
import edu.udacity.android.popularmovies.model.Movie;
import edu.udacity.android.popularmovies.adapter.MovieGridAdapter;
import edu.udacity.android.popularmovies.util.AndroidUtils;
import edu.udacity.android.popularmovies.util.IOUtils;
import edu.udacity.android.popularmovies.util.Constants;

public class MainActivityFragment extends Fragment {
    private static final String TAG = MainActivityFragment.class.getSimpleName();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final Activity activity = getActivity();
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        GridView gridView = (GridView) view.findViewById(R.id.movie_grid);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Movie movie = (Movie) parent.getItemAtPosition(position);
                Intent intent = new Intent(activity.getApplicationContext(), MovieDetailsActivity.class);
                intent.putExtra(Constants.SELECTED_MOVIE_ATTRIBUTE_NAME, movie);
                startActivity(intent);
            }
        });

        MovieGridAdapter adapter = new MovieGridAdapter(activity.getApplicationContext());
        gridView.setAdapter(adapter);
        gridView.setEmptyView(activity.findViewById(R.id.empty));

        PopularMoviesApplication application = (PopularMoviesApplication) activity.getApplication();

        int n = application.getNumberOfColumnsInGrid();
        gridView.setNumColumns(n);

        Movie[] movieArray = null;

        if (savedInstanceState != null) {
            movieArray = (Movie[]) savedInstanceState.get(Constants.MOVIE_ARRAY_ATTRIBUTE_NAME);
            savedInstanceState.clear();
        }

        if (application.isSortPreferenceChanged()) {
            startMovieDataDownloadTask(application, gridView);
            // it is absolutely crucial to reset the flag here
            application.clearSortPreferenceChanged();
        } else if (movieArray == null || movieArray.length == 0) {
            startMovieDataDownloadTask(application, gridView);
        } else {
            adapter.clear();
            adapter.addAll(movieArray);
        }

        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        View view = getView();

        if (view == null) {
            return;
        }

        GridView gridView = (GridView) view.findViewById(R.id.movie_grid);
        MovieGridAdapter adapter = (MovieGridAdapter) gridView.getAdapter();
        List<Movie> movieList = new ArrayList<>();
        int count = adapter.getCount();

        for (int i = 0; i < count; i++) {
            Movie movie = adapter.getItem(i);
            movieList.add(movie);
        }

        outState.putParcelableArray(Constants.MOVIE_ARRAY_ATTRIBUTE_NAME, movieList.toArray(new Movie[count]));
    }

    private void startMovieDataDownloadTask(Application application, GridView gridView) {
        String sortOrder = AndroidUtils.readSortOrderFromPreference(application.getApplicationContext());
        Properties properties = readProperties();
        Uri searchUri = buildSearchUri(sortOrder, properties);
        Uri posterBaseUri = buildImageBaseUri(properties);
        MovieDataDownloadTask task = new MovieDataDownloadTask(gridView);
        task.execute(searchUri, posterBaseUri);
    }

    private Uri buildSearchUri(String sortOrder, Properties properties) {
        String scheme = properties.getProperty("tmdb.api.scheme");
        String authority = properties.getProperty("tmdb.api.authority");
        String path = properties.getProperty("tmdb.api.path");
        String apiKey = properties.getProperty("tmdb.api.key");

        Uri searchUri = new Uri.Builder()
                .scheme(scheme)
                .authority(authority)
                .path(path)
                .appendQueryParameter(Constants.API_KEY_QUERY_PARAM_NAME, apiKey)
                .appendQueryParameter(Constants.SORT_BY_QUERY_PARAM_NAME, sortOrder)
                .build();

        Log.i(TAG, String.format("The search URI is %s", searchUri.toString()));

        return searchUri;
    }

    private Uri buildImageBaseUri(Properties properties) {
        String scheme = properties.getProperty("tmdb.image.scheme");
        String authority = properties.getProperty("tmdb.image.authority");
        String path = properties.getProperty("tmdb.image.path");

        Uri imageBaseUri = new Uri.Builder()
                .scheme(scheme)
                .authority(authority)
                .path(path)
                .build();

        Log.i(TAG, String.format("The base URI for poster is %s", imageBaseUri.toString()));

        return imageBaseUri;
    }

    private Properties readProperties() {
        InputStream inStream = null;
        Properties properties = new Properties();

        try {
            inStream = getResources().openRawResource(R.raw.config);
            properties.load(inStream);
        } catch (IOException ex) {
            Log.e(TAG, "Error while reading config.properties");
        } finally {
            IOUtils.close(inStream);
        }

        return properties;
    }
}
