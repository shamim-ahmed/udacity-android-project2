package edu.udacity.android.popularmovies.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import edu.udacity.android.popularmovies.R;
import edu.udacity.android.popularmovies.model.Movie;

public class MovieGridAdapter extends ArrayAdapter<Movie> {
    private final Context context;

    public MovieGridAdapter(Context context) {
        super(context, R.layout.movie_poster);
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Movie movie = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.movie_poster, parent, false);
        }

        ImageView imageView = (ImageView) convertView.findViewById(R.id.movie_poster);
        Picasso.with(context)
                .load(movie.getPosterUri())
                .noFade()
                .placeholder(R.drawable.movie_placeholder)
                .fit()
                .into(imageView);

        return convertView;
    }
}
