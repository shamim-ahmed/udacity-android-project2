package edu.udacity.android.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import edu.udacity.android.popularmovies.model.Movie;
import edu.udacity.android.popularmovies.util.Constants;
import edu.udacity.android.popularmovies.util.MathUtils;
import edu.udacity.android.popularmovies.util.StringUtils;

public class MovieDetailsActivity extends AppCompatActivity {
    private static final String TAG = MovieDetailsActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movie_details);

        ImageView posterView = (ImageView) findViewById(R.id.movie_details_poster);
        TextView titleView = (TextView) findViewById(R.id.movie_details_title);
        TextView yearView = (TextView) findViewById(R.id.movie_details_year);
        TextView ratingView = (TextView) findViewById(R.id.movie_details_rating);
        TextView synopsisView = (TextView) findViewById(R.id.movie_details_synopsis);

        Bundle extras = getIntent().getExtras();
        Movie selectedMovie = (Movie) extras.get(Constants.SELECTED_MOVIE_ATTRIBUTE_NAME);

        if (selectedMovie == null) {
            Log.e(TAG, "No movie found, so cannot display movie details");
            return;
        }

        Picasso.with(this)
                .load(selectedMovie.getPosterUri())
                .noFade()
                .placeholder(R.drawable.movie_placeholder)
                .into(posterView);

        titleView.setText(selectedMovie.getTitle());
        yearView.setText(generateFormattedYear(selectedMovie.getReleaseDate()));
        ratingView.setText(generateFormattedRating(selectedMovie.getVoteAverage()));

        String synopsis = selectedMovie.getSynopsis();

        if (StringUtils.isBlank(synopsis)) {
            synopsis = getResources().getString(R.string.unknown_synopsis_message);
        }

        synopsisView.setText(synopsis);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, SortPreferenceActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
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
