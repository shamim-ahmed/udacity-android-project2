package edu.udacity.android.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import edu.udacity.android.popularmovies.model.Movie;
import edu.udacity.android.popularmovies.util.Constants;

public class MainActivity extends AppCompatActivity implements MovieGridFragment.Callback {
    private boolean twoPaneRenderMode;

    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            PopularMoviesApplication application = (PopularMoviesApplication) getApplication();

            if (application.isReloadFlag()) {
                recreate();
            }

            return true;
        }
    });

    @Override
    public void recreate() {
        super.recreate();

        if (twoPaneRenderMode) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            MovieDetailsFragment detailsFragment = (MovieDetailsFragment) fragmentManager.findFragmentByTag(Constants.MOVIE_DETAILS_FRAGMENT_TAG);

            if (detailsFragment != null) {
                fragmentManager.beginTransaction().remove(detailsFragment).commit();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (findViewById(R.id.movie_detail_container) != null) {
            twoPaneRenderMode = true;
        } else {
            twoPaneRenderMode = false;
        }
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

    @Override
    public void onResume() {
        super.onResume();
        handler.sendEmptyMessageDelayed(1, 100);
    }

    @Override
    public void onItemSelected(Movie selectedMovie) {
        if (twoPaneRenderMode) {
            MovieDetailsFragment detailsFragment = new MovieDetailsFragment();
            Bundle arguments = new Bundle();
            arguments.putParcelable(Constants.SELECTED_MOVIE_ATTRIBUTE_NAME, selectedMovie);
            detailsFragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction().replace(R.id.movie_detail_container, detailsFragment, Constants.MOVIE_DETAILS_FRAGMENT_TAG).commit();
        } else {
            Intent intent = new Intent(this, MovieDetailsActivity.class);
            intent.putExtra(Constants.SELECTED_MOVIE_ATTRIBUTE_NAME, selectedMovie);
            startActivity(intent);
        }
    }
}
