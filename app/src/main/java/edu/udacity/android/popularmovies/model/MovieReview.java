package edu.udacity.android.popularmovies.model;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

import edu.udacity.android.popularmovies.util.StringUtils;

public class MovieReview implements Parcelable {
    private final String reviewId;
    private final String author;
    private final String content;
    private final Long movieId;

    public MovieReview(JSONObject jsonObject, Long mvId) throws JSONException {
        reviewId = jsonObject.getString("id");
        author = jsonObject.getString("author");
        content = jsonObject.getString("content");

        movieId = mvId;
    }

    public MovieReview(Parcel parcel) {
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

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator<MovieReview>() {
        @Override
        public MovieReview createFromParcel(Parcel parcel) {
            return new MovieReview(parcel);
        }

        @Override
        public MovieReview[] newArray(int size) {
            return new MovieReview[size];
        }
    };
}
