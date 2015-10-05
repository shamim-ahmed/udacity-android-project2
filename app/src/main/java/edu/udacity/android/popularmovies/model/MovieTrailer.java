package edu.udacity.android.popularmovies.model;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

public class MovieTrailer implements Parcelable {
    private final String id;
    private final String key;
    private final String name;
    private final String site;

    public MovieTrailer(JSONObject jsonObject) throws JSONException {
        id = jsonObject.getString("id");
        key = jsonObject.getString("key");
        name = jsonObject.getString("name");
        site = jsonObject.getString("site");
    }

    public MovieTrailer(Parcel in) {
        String[] values = new String[4];
        in.readStringArray(values);
        id = values[0];
        key = values[1];
        name = values[2];
        site = values[3];
    }

    public String getId() {
        return id;
    }

    public String getKey() {
        return key;
    }

    public String getName() {
        return name;
    }

    public String getSite() {
        return site;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringArray(new String[]{id, key, name, site});
    }

    public static final Parcelable.Creator<MovieTrailer> CREATOR = new Parcelable.Creator<MovieTrailer>() {

        @Override
        public MovieTrailer createFromParcel(Parcel source) {
            return new MovieTrailer(source);
        }

        @Override
        public MovieTrailer[] newArray(int size) {
            return new MovieTrailer[size];
        }
    };
}
