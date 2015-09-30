package edu.udacity.android.popularmovies.util;

import android.util.Log;

import java.io.Closeable;

public class IOUtils {
    private static final String TAG = IOUtils.class.getSimpleName();

    public static void close(Closeable closeable) {
        if (closeable == null) {
            return;
        }

        try {
            closeable.close();
        } catch (Exception ex) {
            // nothing to do. ignore
            Log.w(TAG, "I/O resource not closed properly");
        }
    }

    // private constructor to prevent instantiation
    private IOUtils() {
    }
}
