package edu.udacity.android.popularmovies.util;

import android.content.ContentValues;

import java.util.ArrayList;
import java.util.List;

import edu.udacity.android.popularmovies.db.PopularMoviesContract;
import edu.udacity.android.popularmovies.model.Movie;
import edu.udacity.android.popularmovies.model.Review;
import edu.udacity.android.popularmovies.model.Trailer;

public class DbUtils {
    public static ContentValues convertMovie(Movie movie) {
        ContentValues values = new ContentValues();
        values.put(PopularMoviesContract.MovieEntry.COLUMN_MOVIE_ID, movie.getMovieId());
        values.put(PopularMoviesContract.MovieEntry.COLUMN_TITLE, movie.getTitle());
        values.put(PopularMoviesContract.MovieEntry.COLUMN_POSTER_URI, movie.getPosterUri().toString());
        values.put(PopularMoviesContract.MovieEntry.COLUMN_RELEASE_DATE, movie.getReleaseDate());
        values.put(PopularMoviesContract.MovieEntry.COLUMN_VOTE_AVERAGE, movie.getVoteAverage());
        values.put(PopularMoviesContract.MovieEntry.COLUMN_SYNOPSIS, movie.getSynopsis());

        return values;
    }

    public static ContentValues convertPoster(String posterId, byte[] posterData, Long movieId) {
        ContentValues values = new ContentValues();
        values.put(PopularMoviesContract.PosterEntry.COLUMN_POSTER_ID, posterId);
        values.put(PopularMoviesContract.PosterEntry.COLUMN_CONTENT, posterData);
        values.put(PopularMoviesContract.PosterEntry.COLUMN_MOVIE_ID, movieId);

        return values;
    }

    public static ContentValues[] convertTrailers(List<Trailer> trailerList) {
        List<ContentValues> resultList = new ArrayList<>();

        for (Trailer trailer : trailerList) {
            ContentValues values = new ContentValues();
            values.put(PopularMoviesContract.TrailerEntry.COLUMN_TRAILER_ID, trailer.getTrailerId());
            values.put(PopularMoviesContract.TrailerEntry.COLUMN_TRAILER_NAME, trailer.getName());
            values.put(PopularMoviesContract.TrailerEntry.COLUMN_TRAILER_SITE, trailer.getSite());
            values.put(PopularMoviesContract.TrailerEntry.COLUMN_MOVIE_ID, trailer.getMovieId());
            resultList.add(values);
        }

        return resultList.toArray(new ContentValues[resultList.size()]);
    }

    public static ContentValues[] convertReviews(List<Review> reviewList) {
        List<ContentValues> resultList = new ArrayList<>();

        for (Review review : reviewList) {
            ContentValues values = new ContentValues();
            values.put(PopularMoviesContract.ReviewEntry.COLUMN_REVIEW_ID, review.getReviewId());
            values.put(PopularMoviesContract.ReviewEntry.COLUMN_AUTHOR, review.getAuthor());
            values.put(PopularMoviesContract.ReviewEntry.COLUMN_CONTENT, review.getContent());
            values.put(PopularMoviesContract.ReviewEntry.COLUMN_MOVIE_ID, review.getMovieId());
            resultList.add(values);
        }

        return resultList.toArray(new ContentValues[resultList.size()]);
    }

    private DbUtils() {
    }
}
