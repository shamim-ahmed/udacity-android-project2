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

import edu.udacity.android.popularmovies.db.MovieContract;
import edu.udacity.android.popularmovies.model.Movie;
import edu.udacity.android.popularmovies.task.db.FavoriteMovieDeleteTask;
import edu.udacity.android.popularmovies.task.db.FavoriteMovieInsertTask;
import edu.udacity.android.popularmovies.task.db.FavoriteMovieSingleQueryTask;
import edu.udacity.android.popularmovies.task.web.MovieReviewDataDownloadTask;
import edu.udacity.android.popularmovies.task.web.MovieTrailerDataDownloadTask;
import edu.udacity.android.popularmovies.util.Constants;
import edu.udacity.android.popularmovies.util.MathUtils;
import edu.udacity.android.popularmovies.util.StringUtils;

public class MovieDetailsFragment extends Fragment {
    private static final String TAG = MovieDetailsFragment.class.getSimpleName();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_movie_details, container, false);
        ImageView posterView = (ImageView) rootView.findViewById(R.id.movie_details_poster);
        TextView titleView = (TextView) rootView.findViewById(R.id.movie_details_title);
        TextView yearView = (TextView) rootView.findViewById(R.id.movie_details_year);
        TextView ratingView = (TextView) rootView.findViewById(R.id.movie_details_rating);
        TextView synopsisView = (TextView) rootView.findViewById(R.id.movie_details_synopsis);

        final Movie selectedMovie = (Movie) getArguments().get(Constants.SELECTED_MOVIE_ATTRIBUTE_NAME);

        if (selectedMovie == null) {
            Log.e(TAG, "No movie found, so cannot display movie details");
            return rootView;
        }

        final Activity activity = getActivity();
        // display the poster
        Picasso.with(activity)
                .load(selectedMovie.getPosterUri())
                .noFade()
                .placeholder(R.drawable.movie_placeholder_small)
                .into(posterView);

        // display various metadata
        titleView.setText(selectedMovie.getTitle());
        yearView.setText(generateFormattedYear(selectedMovie.getReleaseDate()));
        ratingView.setText(generateFormattedRating(selectedMovie.getVoteAverage()));

        // populate the trailer list
        startTrailerDataDownload(selectedMovie, activity);

        // populate the review list
        startReviewDataDownload(selectedMovie, activity);

        // display synopsis
        String synopsis = selectedMovie.getSynopsis();

        if (StringUtils.isBlank(synopsis)) {
            synopsis = getResources().getString(R.string.unknown_synopsis_message);
        }

        synopsisView.setText(synopsis);

        final PopularMoviesApplication application = (PopularMoviesApplication) activity.getApplication();
        final Uri movieUri = MovieContract.MovieEntry.buildMovieUri(selectedMovie.getMovieId());
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
                    insertTask.execute(MovieContract.CONTENT_URI);
                } else {
                    FavoriteMovieDeleteTask deleteTask = new FavoriteMovieDeleteTask(application, activity);
                    deleteTask.execute(movieUri);
                }
            }
        });

        return rootView;
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

        MovieTrailerDataDownloadTask trailerDownloadTask = new MovieTrailerDataDownloadTask(activity);
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

        MovieReviewDataDownloadTask task = new MovieReviewDataDownloadTask(activity);
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
        values.put(MovieContract.MovieEntry.COLUMN_MOVIE_ID, movie.getMovieId());
        values.put(MovieContract.MovieEntry.COLUMN_TITLE, movie.getTitle());
        values.put(MovieContract.MovieEntry.COLUMN_POSTER_URI, movie.getPosterUri().toString());
        values.put(MovieContract.MovieEntry.COLUMN_RELEASE_DATE, movie.getReleaseDate());
        values.put(MovieContract.MovieEntry.COLUMN_VOTE_AVERAGE, movie.getVoteAverage());
        values.put(MovieContract.MovieEntry.COLUMN_SYNOPSIS, movie.getSynopsis());

        return values;
    }
}