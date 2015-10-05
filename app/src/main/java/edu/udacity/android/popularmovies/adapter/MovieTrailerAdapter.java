package edu.udacity.android.popularmovies.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import edu.udacity.android.popularmovies.R;
import edu.udacity.android.popularmovies.model.MovieTrailer;

public class MovieTrailerAdapter extends ArrayAdapter<MovieTrailer> {
    private final Activity activity;

    public MovieTrailerAdapter(Activity activity) {
        super(activity, R.layout.movie_trailer);
        this.activity = activity;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        MovieTrailer trailer = getItem(position);

        if (view == null) {
            LayoutInflater inflater = activity.getLayoutInflater();
            view = inflater.inflate(R.layout.movie_trailer, parent, false);
        }

        TextView textView = (TextView) view.findViewById(R.id.movie_trailer_name);
        textView.setText(trailer.getName());
        return view;
    }
}
