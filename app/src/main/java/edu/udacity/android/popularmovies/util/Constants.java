package edu.udacity.android.popularmovies.util;

public class Constants {
    public static final String SORT_PREFERENCE_KEY = "sort_order";
    public static final String SORT_PREFERENCE_DEFAULT_VALUE = "popularity.desc";
    public static final String API_KEY_QUERY_PARAM_NAME = "api_key";
    public static final String SORT_BY_QUERY_PARAM_NAME = "sort_by";
    public static final String SELECTED_MOVIE_ATTRIBUTE_NAME = "selectedMovie";
    public static final String MOVIE_RELEASE_DATE_FORMAT = "yyyy-MM-dd";
    public static final String NULL_AS_STR = "null";
    public static final int MAX_RATING = 10;
    public static final int MOBILE_COLUMN_COUNT = 2;
    public static final int TABLET_COLUMN_COUNT = 3;
    public static final String MOVIE_ARRAY_ATTRIBUTE_NAME = "movieArray";


    // private constructor to prevent instantiation
    private Constants() {
    }
}
