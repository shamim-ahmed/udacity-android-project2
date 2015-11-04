package edu.udacity.android.popularmovies.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import edu.udacity.android.popularmovies.PopularMoviesApplication;
import edu.udacity.android.popularmovies.R;
import edu.udacity.android.popularmovies.model.Movie;
import edu.udacity.android.popularmovies.task.db.PosterQueryTask;
import edu.udacity.android.popularmovies.util.Constants;

public class MovieGridAdapter extends ArrayAdapter<Movie> {
    private final Activity activity;

    public MovieGridAdapter(Activity activity) {
        super(activity.getApplicationContext(), R.layout.movie_poster);
        this.activity = activity;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Context context = activity.getApplicationContext();
        Movie movie = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.movie_poster, parent, false);
        }

        ImageView imageView = (ImageView) convertView.findViewById(R.id.movie_poster);
        PopularMoviesApplication application = (PopularMoviesApplication) activity.getApplication();
        String sortPreference = application.getActiveSortPreference();

        if (Constants.SORT_FAVORITE.equals(sortPreference)) {
            PosterQueryTask task = new PosterQueryTask(movie, activity, imageView);
            task.execute();
        } else {
            Picasso.with(context)
                    .load(movie.getPosterUri())
                    .noFade()
                    .placeholder(R.drawable.movie_placeholder)
                    .fit()
                    .centerInside()
                    .into(imageView);
        }

        return convertView;
    }
}
