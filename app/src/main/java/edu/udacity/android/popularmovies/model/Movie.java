package edu.udacity.android.popularmovies.model;

import android.content.ContentValues;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Movie implements Parcelable {
    private final Long movieId;
    private final String title;
    private final String releaseDate;
    private final Uri posterUri;
    private final Double voteAverage;
    private final String synopsis;

    // these fields are populated only when the details screen
    // for a particular movie is visited
    private final List<MovieTrailer> trailerList = new ArrayList<>();
    private final List<MovieReview> reviewList = new ArrayList<>();

    public static final Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>() {

        @Override
        public Movie createFromParcel(Parcel source) {
            return new Movie(source);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    public Movie(JSONObject movieData, Uri posterBaseUri) throws JSONException {
        movieId = movieData.getLong("id");
        title = movieData.getString("title");
        releaseDate = movieData.getString("release_date");

        String posterPath = movieData.getString("poster_path");
        posterUri = Uri.withAppendedPath(posterBaseUri, posterPath);

        String voteStr = movieData.getString("vote_average");
        voteAverage = voteStr != null ? Double.valueOf(voteStr) : null;

        synopsis = movieData.getString("overview");
    }

    public Movie(ContentValues values) {
        movieId = values.getAsLong("movie_id");
        title = values.getAsString("title");
        releaseDate = values.getAsString("release_date");

        String pUriStr = values.getAsString("poster_uri");
        posterUri = pUriStr != null ? Uri.parse(pUriStr) : null;
        voteAverage = values.getAsDouble("vote_average");
        synopsis = values.getAsString("synopsis");
    }

    @SuppressWarnings("unchecked")
    public Movie(Parcel in) {
        Object[] values = in.readArray(ClassLoader.getSystemClassLoader());
        movieId = (Long) values[0];
        title = (String) values[1];
        releaseDate = (String) values[2];
        posterUri = (Uri) values[3];
        voteAverage = (Double) values[4];
        synopsis = (String) values[5];
        setTrailerList((List<MovieTrailer>) values[6]);
        setReviewList((List<MovieReview>) values[7]);
    }

    public Long getMovieId() {
        return movieId;
    }

    public String getTitle() {
        return title;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public Uri getPosterUri() {
        return posterUri;
    }

    public Double getVoteAverage() {
        return voteAverage;
    }

    public String getSynopsis() {
        return synopsis;
    }

    public List<MovieTrailer> getTrailerList() {
        return trailerList;
    }

    public void setTrailerList(List<MovieTrailer> list) {
        if (list == null) {
            return;
        }

        trailerList.clear();
        trailerList.addAll(list);
    }

    public List<MovieReview> getReviewList() {
        return reviewList;
    }

    public void setReviewList(List<MovieReview> list) {
        if (list == null) {
            return;
        }

        reviewList.clear();
        reviewList.addAll(list);
    }

    @Override
    public String toString() {
        return title;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeArray(new Object[]{movieId, title, releaseDate, posterUri, voteAverage, synopsis, trailerList, reviewList});
    }
}
