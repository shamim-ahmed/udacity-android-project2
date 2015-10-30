package edu.udacity.android.popularmovies;

import android.app.Activity;
import android.content.ContentValues;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import edu.udacity.android.popularmovies.db.PopularMoviesContract;
import edu.udacity.android.popularmovies.model.Movie;
import edu.udacity.android.popularmovies.task.db.FavoriteMovieInsertTask;
import edu.udacity.android.popularmovies.task.db.FavoriteMovieSingleQueryTask;
import edu.udacity.android.popularmovies.task.web.ReviewDataDownloadTask;
import edu.udacity.android.popularmovies.task.web.TrailerDataDownloadTask;
import edu.udacity.android.popularmovies.util.AppUtils;
import edu.udacity.android.popularmovies.util.Constants;
import edu.udacity.android.popularmovies.util.MathUtils;
import edu.udacity.android.popularmovies.util.StringUtils;

public class MovieDetailsFragment extends Fragment {
    private static final String TAG = MovieDetailsFragment.class.getSimpleName();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View rootView = inflater.inflate(R.layout.fragment_movie_details, container, false);
        ImageView posterView = (ImageView) rootView.findViewById(R.id.movie_details_poster);
        TextView titleView = (TextView) rootView.findViewById(R.id.movie_details_title);
        TextView yearView = (TextView) rootView.findViewById(R.id.movie_details_year);
        TextView ratingView = (TextView) rootView.findViewById(R.id.movie_details_rating);
        TextView synopsisView = (TextView) rootView.findViewById(R.id.movie_details_synopsis);

        boolean savedStateFlag = savedInstanceState != null;

        // find the movie
        Movie movie;

        if (savedStateFlag) {
            movie = (Movie) savedInstanceState.get(Constants.SELECTED_MOVIE_ATTRIBUTE_NAME);
        } else {
            movie = (Movie) getArguments().get(Constants.SELECTED_MOVIE_ATTRIBUTE_NAME);
        }

        if (movie == null) {
            Log.e(TAG, "No movie found, so cannot display movie details");
            return rootView;
        }

        final Movie selectedMovie = movie;
        final Activity activity = getActivity();

        int moviePlaceHolderId;

        if (AppUtils.isTablet(activity)) {
            moviePlaceHolderId = R.drawable.movie_placeholder;
        } else {
            moviePlaceHolderId = R.drawable.movie_placeholder_small;
        }

        // display the poster
        Picasso.with(activity)
                .load(selectedMovie.getPosterUri())
                .noFade()
                .placeholder(moviePlaceHolderId)
                .into(posterView);

        // display various metadata
        titleView.setText(selectedMovie.getTitle());
        yearView.setText(generateFormattedYear(selectedMovie.getReleaseDate()));
        ratingView.setText(generateFormattedRating(selectedMovie.getVoteAverage()));

        // display synopsis
        String synopsis = selectedMovie.getSynopsis();

        if (StringUtils.isBlank(synopsis)) {
            synopsis = getResources().getString(R.string.unknown_synopsis_message);
        }

        synopsisView.setText(synopsis);

        // check if the movie has been marked as favorite
        final PopularMoviesApplication application = (PopularMoviesApplication) activity.getApplication();
        final Uri movieUri = PopularMoviesContract.MovieEntry.buildMovieUri(selectedMovie.getMovieId());
        FavoriteMovieSingleQueryTask queryTask = new FavoriteMovieSingleQueryTask(activity);
        queryTask.execute(movieUri);

        Button favoriteButton = (Button) rootView.findViewById(R.id.favorite_button);

        favoriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Button btn = (Button) v;
                boolean selected = btn.isSelected();

                if (!selected) {
                    ContentValues values = convertMovie(selectedMovie);
                    FavoriteMovieInsertTask insertTask = new FavoriteMovieInsertTask(application, activity, values);
                    insertTask.execute(PopularMoviesContract.MovieEntry.CONTENT_URI);
                }
            }
        });

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setRetainInstance(true);

        Activity activity = getActivity();
        boolean savedStateFlag = savedInstanceState != null;

        // find the movie
        Movie selectedMovie;

        if (savedStateFlag) {
            selectedMovie = (Movie) savedInstanceState.get(Constants.SELECTED_MOVIE_ATTRIBUTE_NAME);
        } else {
            selectedMovie = (Movie) getArguments().get(Constants.SELECTED_MOVIE_ATTRIBUTE_NAME);
        }

        if (selectedMovie == null) {
            return;
        }

        // populate the trailer list
        populateTrailers(selectedMovie, activity, savedStateFlag);

        // populate the review list
        populateReviews(selectedMovie, activity, savedStateFlag);
    }

    // save the movie in its current state, to be restored later
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        View view = getView();

        if (view == null) {
            return;
        }

        Movie movie = (Movie) getArguments().get(Constants.SELECTED_MOVIE_ATTRIBUTE_NAME);
        outState.putParcelable(Constants.SELECTED_MOVIE_ATTRIBUTE_NAME, movie);
    }

    private void populateTrailers(Movie movie, Activity activity, boolean savedStateFlag) {
        if (savedStateFlag) {
            AppUtils.displayTrailersForMovie(movie, activity);
        } else {
            startTrailerDataDownload(movie, activity);
        }
    }

    private void populateReviews(Movie movie, Activity activity, boolean savedStateFlag) {
        if (savedStateFlag) {
            AppUtils.displayReviewsForMovie(movie, activity);
        } else {
            startReviewDataDownload(movie, activity);
        }
    }

    private void startTrailerDataDownload(Movie movie, Activity activity) {
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

        Log.i(TAG, String.format("The trailer Uri is : %s", trailerUri.toString()));

        TrailerDataDownloadTask trailerDownloadTask = new TrailerDataDownloadTask(movie, activity);
        trailerDownloadTask.execute(trailerUri);
    }

    private void startReviewDataDownload(Movie movie, Activity activity) {
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

        Log.i(TAG, String.format("The review URI is : %s", reviewDataUri.toString()));

        ReviewDataDownloadTask task = new ReviewDataDownloadTask(movie, activity);
        task.execute(reviewDataUri);
    }

    private String generateFormattedRating(Double rating) {
        if (rating == null) {
            return getResources().getString(R.string.unknown_rating_message);
        }

        String result;

        if (MathUtils.isInteger(rating)) {
            result = String.format("%d/%d", rating.intValue(), Constants.MAX_RATING);
        } else {
            result = String.format("%.1f/%d", rating.doubleValue(), Constants.MAX_RATING);
        }

        return result;
    }

    private String generateFormattedYear(String releaseDateStr) {
        if (StringUtils.isBlank(releaseDateStr)) {
            return getResources().getString(R.string.unknown_release_date_message);
        }

        DateFormat dateFormat = new SimpleDateFormat(Constants.MOVIE_RELEASE_DATE_FORMAT, Locale.US);
        String result = null;

        try {
            Date date = dateFormat.parse(releaseDateStr);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            result = Integer.toString(calendar.get(Calendar.YEAR));
        } catch (ParseException ex) {
            Log.e(TAG, String.format("Error while parsing date %s", releaseDateStr));
        }

        return result;
    }

    private ContentValues convertMovie(Movie movie) {
        ContentValues values = new ContentValues();
        values.put(PopularMoviesContract.MovieEntry.COLUMN_MOVIE_ID, movie.getMovieId());
        values.put(PopularMoviesContract.MovieEntry.COLUMN_TITLE, movie.getTitle());
        values.put(PopularMoviesContract.MovieEntry.COLUMN_POSTER_URI, movie.getPosterUri().toString());
        values.put(PopularMoviesContract.MovieEntry.COLUMN_RELEASE_DATE, movie.getReleaseDate());
        values.put(PopularMoviesContract.MovieEntry.COLUMN_VOTE_AVERAGE, movie.getVoteAverage());
        values.put(PopularMoviesContract.MovieEntry.COLUMN_SYNOPSIS, movie.getSynopsis());

        return values;
    }
}
