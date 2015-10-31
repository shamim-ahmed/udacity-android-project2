package edu.udacity.android.popularmovies.model;

import android.content.ContentValues;
import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

import edu.udacity.android.popularmovies.db.PopularMoviesContract;
import edu.udacity.android.popularmovies.util.StringUtils;

public class Review implements Parcelable {
    private final String reviewId;
    private final String author;
    private final String content;
    private final Long movieId;

    public Review(JSONObject jsonObject, Long mvId) throws JSONException {
        reviewId = jsonObject.getString("id");
        author = jsonObject.getString("author");
        content = jsonObject.getString("content");

        movieId = mvId;
    }

    public Review(Parcel parcel) {
        String[] values = new String[4];
        parcel.readStringArray(values);
        reviewId = values[0];
        author = values[1];
        content = values[2];
        String movieIdStr = values[3];

        if (!StringUtils.isBlank(movieIdStr)) {
            movieId = Long.parseLong(movieIdStr);
        } else {
            movieId = null;
        }
    }

    public Review(ContentValues values) {
        reviewId = values.getAsString(PopularMoviesContract.ReviewEntry.COLUMN_REVIEW_ID);
        author = values.getAsString(PopularMoviesContract.ReviewEntry.COLUMN_AUTHOR);
        content = values.getAsString(PopularMoviesContract.ReviewEntry.COLUMN_CONTENT);
        movieId = values.getAsLong(PopularMoviesContract.ReviewEntry.COLUMN_MOVIE_ID);
    }

    public String getReviewId() {
        return reviewId;
    }

    public String getAuthor() {
        return author;
    }

    public String getContent() {
        return content;
    }

    public Long getMovieId() {
        return movieId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeStringArray(new String[]{reviewId, author, content, Long.toString(movieId)});
    }

    @Override
    public String toString() {
        return String.format("&ldquo;%s&rdquo; - <b><i><u>%s</u></i></b>", content, author);
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator<Review>() {
        @Override
        public Review createFromParcel(Parcel parcel) {
            return new Review(parcel);
        }

        @Override
        public Review[] newArray(int size) {
            return new Review[size];
        }
    };
}
