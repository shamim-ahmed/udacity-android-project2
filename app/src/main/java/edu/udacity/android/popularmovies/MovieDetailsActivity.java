package edu.udacity.android.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import edu.udacity.android.popularmovies.model.Movie;
import edu.udacity.android.popularmovies.util.Constants;

public class MovieDetailsActivity extends AppCompatActivity {
    private static final String TAG = MovieDetailsActivity.class.getSimpleName();

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        if (savedInstanceState != null) {
            return;
        }

        Bundle extras = getIntent().getExtras();
        Movie selectedMovie = (Movie) extras.get(Constants.SELECTED_MOVIE_ATTRIBUTE_NAME);

        Bundle arguments = new Bundle();
        arguments.putParcelable(Constants.SELECTED_MOVIE_ATTRIBUTE_NAME, selectedMovie);

        MovieDetailsFragment detailsFragment = new MovieDetailsFragment();
        detailsFragment.setArguments(arguments);

        getSupportFragmentManager().beginTransaction()
                .add(R.id.movie_detail_container, detailsFragment, Constants.MOVIE_DETAILS_FRAGMENT_TAG)
                .commit();
    }
}
