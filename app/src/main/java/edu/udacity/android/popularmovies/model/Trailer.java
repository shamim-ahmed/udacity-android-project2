package edu.udacity.android.popularmovies.model;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

import edu.udacity.android.popularmovies.util.StringUtils;

public class Trailer implements Parcelable {
    private final String trailerId;
    private final String key;
    private final String name;
    private final String site;
    private final Long movieId;

    public Trailer(JSONObject jsonObject, Long mvId) throws JSONException {
        trailerId = jsonObject.getString("id");
        key = jsonObject.getString("key");
        name = jsonObject.getString("name");
        site = jsonObject.getString("site");

        movieId = mvId;
    }

    public Trailer(Parcel in) {
        String[] values = new String[5];
        in.readStringArray(values);
        trailerId = values[0];
        key = values[1];
        name = values[2];
        site = values[3];

        String movieIdStr = values[4];

        if (!StringUtils.isBlank(movieIdStr)) {
            movieId = Long.parseLong(values[4]);
        } else {
            movieId = null;
        }
    }

    public String getTrailerId() {
        return trailerId;
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

    public Long getMovieId() {
        return movieId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringArray(new String[]{trailerId, key, name, site, Long.toString(movieId)});
    }

    @Override
    public String toString() {
        return name;
    }

    public static final Parcelable.Creator<Trailer> CREATOR = new Parcelable.Creator<Trailer>() {

        @Override
        public Trailer createFromParcel(Parcel source) {
            return new Trailer(source);
        }

        @Override
        public Trailer[] newArray(int size) {
            return new Trailer[size];
        }
    };
}
