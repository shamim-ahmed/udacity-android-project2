package edu.udacity.android.popularmovies.model;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

public class Movie implements Parcelable {
    private final String title;
    private final String releaseDate;
    private final Uri posterUri;
    private final Double voteAverage;
    private final String synopsis;

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
        title = movieData.getString("title");
        releaseDate = movieData.getString("release_date");

        String posterPath = movieData.getString("poster_path");
        posterUri = Uri.withAppendedPath(posterBaseUri, posterPath);

        String voteStr = movieData.getString("vote_average");
        voteAverage = voteStr != null ? Double.valueOf(voteStr) : null;

        synopsis = movieData.getString("overview");
    }

    public Movie(Parcel in) {
        Object[] values = in.readArray(ClassLoader.getSystemClassLoader());
        title = (String) values[0];
        releaseDate = (String) values[1];
        posterUri = (Uri) values[2];
        voteAverage = (Double) values[3];
        synopsis = (String) values[4];
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
        dest.writeArray(new Object[]{title, releaseDate, posterUri, voteAverage, synopsis});
    }
}
