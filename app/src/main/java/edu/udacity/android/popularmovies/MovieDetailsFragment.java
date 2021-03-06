package edu.udacity.android.popularmovies;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import edu.udacity.android.popularmovies.model.Movie;
import edu.udacity.android.popularmovies.task.db.MovieInsertTask;
import edu.udacity.android.popularmovies.task.db.MovieIsFavoriteQueryTask;
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
        final ImageView posterView = (ImageView) rootView.findViewById(R.id.movie_details_poster);
        TextView titleView = (TextView) rootView.findViewById(R.id.movie_details_title);
        TextView yearView = (TextView) rootView.findViewById(R.id.movie_details_year);
        TextView ratingView = (TextView) rootView.findViewById(R.id.movie_details_rating);
        TextView synopsisView = (TextView) rootView.findViewById(R.id.movie_details_synopsis);

        boolean savedStateFlag = (savedInstanceState != null);

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
        MovieIsFavoriteQueryTask queryTask = new MovieIsFavoriteQueryTask(activity, selectedMovie, savedStateFlag);
        queryTask.execute();

        Button favoriteButton = (Button) rootView.findViewById(R.id.favorite_button);

        favoriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Button btn = (Button) v;
                // disable button so that double click is not possible. Multiple click can
                // result in conflicts in database operations
                btn.setEnabled(false);

                boolean selected = btn.isSelected();

                if (!selected) {
                    byte[] posterContent = AppUtils.extractPosterContent(posterView, activity);
                    MovieInsertTask insertTask = new MovieInsertTask(selectedMovie, posterContent, activity, application);
                    insertTask.execute();
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

        // populate the trailer and review list
        if (savedStateFlag) {
            AppUtils.displayTrailersForMovie(selectedMovie, activity);
            AppUtils.displayReviewsForMovie(selectedMovie, activity);
        }
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
}
