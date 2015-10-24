package edu.udacity.android.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (findViewById(R.id.movie_detail_container) != null) {
            twoPaneRenderMode = true;
        } else {
            twoPaneRenderMode = false;
        }

        Log.i(TAG, "is two pane render mode enabled ? " + twoPaneRenderMode);

        MovieGridFragment movieGridFragment = (MovieGridFragment) getSupportFragmentManager().findFragmentById(R.id.movie_grid_fragment);
        movieGridFragment.setTowPaneRederMode(twoPaneRenderMode);
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
}
