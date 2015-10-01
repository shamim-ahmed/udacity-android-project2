package edu.udacity.android.popularmovies.util;

public class MathUtils {
    public static boolean isInteger(double value) {
        return ((value == Math.floor(value)) && !Double.isInfinite(value));
    }

    // private constructor to prevent instantiation
    private MathUtils() {
    }
}
