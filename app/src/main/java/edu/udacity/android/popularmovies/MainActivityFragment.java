package edu.udacity.android.popularmovies;

import android.app.Activity;
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
import java.util.Properties;

import edu.udacity.android.popularmovies.util.AndroidUtils;
import edu.udacity.android.popularmovies.util.IOUtils;
import edu.udacity.android.popularmovies.util.PopularMoviesConstants;

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
                intent.putExtra(PopularMoviesConstants.SELECTED_MNOVIE_ATTRIBUTE_NAME, movie);
                startActivity(intent);
            }
        });

        MovieGridAdapter adapter = new MovieGridAdapter(activity.getApplicationContext());
        gridView.setAdapter(adapter);

        PopularMoviesApplication application = (PopularMoviesApplication) activity.getApplication();
        String sortOrder = application.getCurrentSortOrder();

        if (AndroidUtils.isTablet(application.getApplicationContext())) {
            gridView.setNumColumns(3);
        } else {
            gridView.setNumColumns(2);
        }

        Properties properties = getProperties();
        Uri searchUri = getSearchUri(sortOrder, properties);
        Uri posterBaseUri = getImageBaseUri(properties);

        MovieDataDownloadTask task = new MovieDataDownloadTask(gridView);
        task.execute(searchUri, posterBaseUri);

        return view;
    }

    private Uri getSearchUri(String sortOrder, Properties properties) {
        String scheme = properties.getProperty("tmdb.api.scheme");
        String authority = properties.getProperty("tmdb.api.authority");
        String path = properties.getProperty("tmdb.api.path");
        String apiKey = properties.getProperty("tmdb.api.key");

        Uri searchUri = new Uri.Builder()
                .scheme(scheme)
                .authority(authority)
                .path(path)
                .appendQueryParameter(PopularMoviesConstants.API_KEY_QUERY_PARAM_NAME, apiKey)
                .appendQueryParameter(PopularMoviesConstants.SORT_BY_QUERY_PARAM_NAME, sortOrder)
                .build();

        Log.i(TAG, String.format("The search URI is %s", searchUri.toString()));

        return searchUri;
    }

    private Uri getImageBaseUri(Properties properties) {
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

    private Properties getProperties() {
        InputStream inStream = null;
        Properties properties = new Properties();

        try {
            inStream = getResources().openRawResource(R.raw.config);
            properties.load(inStream);
        } catch (IOException ex) {
            Log.e(TAG, "Error while reading config.configProperties");
        } finally {
            IOUtils.close(inStream);
        }

        return properties;
    }
}
