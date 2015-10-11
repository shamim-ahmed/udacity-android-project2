package edu.udacity.android.popularmovies.db;

import android.content.ContentValues;
import android.database.Cursor;

import java.util.Map;
import java.util.Set;

import static junit.framework.Assert.*;

public class TestUtilities {

    public static ContentValues createMovieValues() {
        ContentValues values = new ContentValues();
        values.put(MovieContract.MovieEntry.COLUMN_MOVIE_ID, 27205L);
        values.put(MovieContract.MovieEntry.COLUMN_TITLE, "Inception");
        values.put(MovieContract.MovieEntry.COLUMN_POSTER_URI, "http://image.tmdb.org/t/p/w185/qmDpIHrmpJINaRKAfWQfftjCdyi.jpg");
        values.put(MovieContract.MovieEntry.COLUMN_RELEASE_DATE, "2010-07-16");
        values.put(MovieContract.MovieEntry.COLUMN_SYNOPSIS, "Cobb, a skilled thief who commits corporate espionage by infiltrating the subconscious of his targets is offered a chance to regain his old life as payment for a task considered to be impossible");

        return values;
    }

    public static void validateCursor(String error, Cursor valueCursor, ContentValues expectedValues) {
        assertTrue("Empty cursor returned. " + error, valueCursor.moveToFirst());
        validateCurrentRecord(error, valueCursor, expectedValues);
        valueCursor.close();
    }

    private static void validateCurrentRecord(String error, Cursor valueCursor, ContentValues expectedValues) {
        Set<Map.Entry<String, Object>> valueSet = expectedValues.valueSet();
        for (Map.Entry<String, Object> entry : valueSet) {
            String columnName = entry.getKey();
            int idx = valueCursor.getColumnIndex(columnName);
            assertFalse("Column '" + columnName + "' not found. " + error, idx == -1);
            String expectedValue = entry.getValue().toString();
            assertEquals("Value '" + entry.getValue().toString() +
                    "' did not match the expected value '" +
                    expectedValue + "'. " + error, expectedValue, valueCursor.getString(idx));
        }
    }


    private TestUtilities() {
    }
}
