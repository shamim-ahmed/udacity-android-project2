package edu.udacity.android.popularmovies;

import android.app.Activity;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import java.util.ArrayList;
import java.util.List;

import edu.udacity.android.popularmovies.db.PopularMoviesContract;
import edu.udacity.android.popularmovies.task.db.FavoriteMovieBulkQueryTask;
import edu.udacity.android.popularmovies.task.web.MovieDataDownloadTask;
import edu.udacity.android.popularmovies.model.Movie;
import edu.udacity.android.popularmovies.adapter.MovieGridAdapter;
import edu.udacity.android.popularmovies.util.AppUtils;
import edu.udacity.android.popularmovies.util.Constants;

public class MovieGridFragment extends Fragment {
    private static final String TAG = MovieGridFragment.class.getSimpleName();

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        final Activity activity = getActivity();
        View view = inflater.inflate(R.layout.fragment_movie_grid, container, false);
        GridView gridView = (GridView) view.findViewById(R.id.movie_grid);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Movie movie = (Movie) parent.getItemAtPosition(position);
                ((Callback) activity).onItemSelected(movie);
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

        String sortPreference = AppUtils.readSortOrderFromPreference(application.getApplicationContext());

        if (application.isReloadFlag()) {
            if (Constants.SORT_FAVORITE.equals(sortPreference)) {
                startFavoriteMovieQueryTask(gridView);
            } else {
                startMovieDataDownloadTask(application, gridView, sortPreference);
            }

            // it is absolutely crucial to reset the flag here
            application.clearSortPreferenceChanged();
        } else if (movieArray == null || movieArray.length == 0) {
            if (Constants.SORT_FAVORITE.equals(sortPreference)) {
                startFavoriteMovieQueryTask(gridView);
            } else {
                startMovieDataDownloadTask(application, gridView, sortPreference);
            }
        } else {
            adapter.clear();
            adapter.addAll(movieArray);
        }

        return view;
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setRetainInstance(true);
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

    private void startMovieDataDownloadTask(PopularMoviesApplication application, GridView gridView, String sortPreference) {
        Uri searchUri = buildSearchUri(application, sortPreference);
        Uri posterBaseUri = buildImageBaseUri(application);
        MovieDataDownloadTask task = new MovieDataDownloadTask(gridView);
        task.execute(searchUri, posterBaseUri);
    }

    private void startFavoriteMovieQueryTask(GridView gridView) {
        MovieGridAdapter adapter = (MovieGridAdapter) gridView.getAdapter();
        FavoriteMovieBulkQueryTask queryTask = new FavoriteMovieBulkQueryTask(getActivity(), adapter);
        queryTask.execute(PopularMoviesContract.MovieEntry.MOVIE_CONTENT_URI);
    }

    private Uri buildSearchUri(PopularMoviesApplication application, String sortPreference) {
        String scheme = application.getConfigurationProperty("tmdb.api.scheme");
        String authority = application.getConfigurationProperty("tmdb.api.authority");
        String path = application.getConfigurationProperty("tmdb.api.discover.path");
        String apiKey = application.getConfigurationProperty("tmdb.api.key");

        Uri searchUri = new Uri.Builder()
                .scheme(scheme)
                .authority(authority)
                .path(path)
                .appendQueryParameter(Constants.API_KEY_QUERY_PARAM_NAME, apiKey)
                .appendQueryParameter(Constants.SORT_BY_QUERY_PARAM_NAME, sortPreference)
                .build();

        Log.i(TAG, String.format("The search URI is %s", searchUri.toString()));

        return searchUri;
    }

    private Uri buildImageBaseUri(PopularMoviesApplication application) {
        String scheme = application.getConfigurationProperty("tmdb.image.scheme");
        String authority = application.getConfigurationProperty("tmdb.image.authority");
        String path = application.getConfigurationProperty("tmdb.image.path");

        Uri imageBaseUri = new Uri.Builder()
                .scheme(scheme)
                .authority(authority)
                .path(path)
                .build();

        Log.i(TAG, String.format("The base URI for poster is %s", imageBaseUri.toString()));

        return imageBaseUri;
    }

    public interface Callback {
        void onItemSelected(Movie selectedMovie);
    }
}
