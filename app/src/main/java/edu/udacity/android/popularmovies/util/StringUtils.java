package edu.udacity.android.popularmovies.util;

public class StringUtils {
    public static boolean isBlank(String str) {
        return str == null || str.trim().equals("") || str.equals(Constants.NULL_AS_STR);
    }

    // private constructor to prevent instantiation
    private StringUtils() {
    }
}
