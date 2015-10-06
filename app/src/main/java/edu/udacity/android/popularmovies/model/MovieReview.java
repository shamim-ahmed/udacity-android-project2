package edu.udacity.android.popularmovies.model;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

public class MovieReview implements Parcelable {
    private String id;
    private String author;
    private String content;
    private String link;

    public MovieReview(JSONObject jsonObject) throws JSONException {
        id = jsonObject.getString("id");
        author = jsonObject.getString("author");
        content = jsonObject.getString("content");
        link = jsonObject.getString("url");
    }

    public MovieReview(Parcel parcel) {
        String[] values = new String[4];
        parcel.readStringArray(values);
        id = values[0];
        author = values[1];
        content = values[2];
        link = values[3];
    }

    public String getId() {
        return id;
    }

    public String getAuthor() {
        return author;
    }

    public String getContent() {
        return content;
    }

    public String getLink() {
        return link;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeArray(new Object[]{id, author, content, link});
    }

    @Override
    public String toString() {
        return content;
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
