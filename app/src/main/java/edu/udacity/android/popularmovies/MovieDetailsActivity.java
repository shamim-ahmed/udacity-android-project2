package edu.udacity.android.popularmovies;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import edu.udacity.android.popularmovies.util.PopularMoviesConstants;

public class MovieDetailsActivity extends AppCompatActivity {
    private static final String TAG = MovieDetailsActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movie_details);

        ImageView poster = (ImageView) findViewById(R.id.movie_details_poster);
        TextView title = (TextView) findViewById(R.id.movie_details_title);
        TextView year = (TextView) findViewById(R.id.movie_details_year);
        TextView rating = (TextView) findViewById(R.id.movie_details_rating);
        TextView synopsis = (TextView) findViewById(R.id.movie_details_synopsis);

        Bundle extras = getIntent().getExtras();
        Movie selectedMovie = (Movie) extras.get(PopularMoviesConstants.SELECTED_MNOVIE_ATTRIBUTE_NAME);

        if (selectedMovie == null) {
            Log.e(TAG, "No movie found, so cannot display movie details");
            return;
        }

        Picasso.with(this).load(selectedMovie.getPosterUri()).into(poster);
        title.setText(selectedMovie.getTitle());
        year.setText(generateFormattedYear(selectedMovie.getReleaseDate()));
        rating.setText(generateFormattedRating(selectedMovie.getVoteAverage()));
        synopsis.setText(selectedMovie.getSynopsis());
    }

    private String generateFormattedRating(Double rating) {
        if (rating == null) {
            return null;
        }

        return String.format("%.1f/%d", rating.doubleValue(), PopularMoviesConstants.MAX_RATING);
    }

    private String generateFormattedYear(String releaseDateStr) {
        if (releaseDateStr == null) {
            return null;
        }

        DateFormat dateFormat = new SimpleDateFormat(PopularMoviesConstants.MOVIE_RELEASE_DATE_FORMAT, Locale.US);
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
