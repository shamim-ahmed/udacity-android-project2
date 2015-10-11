package edu.udacity.android.popularmovies.db;

import android.content.ContentValues;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static junit.framework.Assert.*;

public class TestUtilities {

    public static List<ContentValues> createMovieValues() {
        List<ContentValues> contentList = new ArrayList<>();

        ContentValues inceptionValues = new ContentValues();
        inceptionValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_ID, 27205L);
        inceptionValues.put(MovieContract.MovieEntry.COLUMN_TITLE, "Inception");
        inceptionValues.put(MovieContract.MovieEntry.COLUMN_POSTER_URI, "http://image.tmdb.org/t/p/w185/qmDpIHrmpJINaRKAfWQfftjCdyi.jpg");
        inceptionValues.put(MovieContract.MovieEntry.COLUMN_RELEASE_DATE, "2010-07-16");
        inceptionValues.put(MovieContract.MovieEntry.COLUMN_SYNOPSIS, "Cobb, a skilled thief who commits corporate espionage by infiltrating the subconscious of his targets is offered a chance to regain his old life as payment for a task considered to be impossible");
        contentList.add(inceptionValues);

        ContentValues jurassicWorldValues = new ContentValues();
        jurassicWorldValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_ID, 135397L);
        jurassicWorldValues.put(MovieContract.MovieEntry.COLUMN_TITLE, "Jurassic World");
        jurassicWorldValues.put(MovieContract.MovieEntry.COLUMN_POSTER_URI, "http://image.tmdb.org/t/p/w185/jjBgi2r5cRt36xF6iNUEhzscEcb.jpg");
        jurassicWorldValues.put(MovieContract.MovieEntry.COLUMN_RELEASE_DATE, "2015-06-12");
        jurassicWorldValues.put(MovieContract.MovieEntry.COLUMN_SYNOPSIS, "Twenty-two years after the events of Jurassic Park, Isla Nublar now features a fully functioning dinosaur theme park, Jurassic World, as originally envisioned by John Hammond.");
        contentList.add(jurassicWorldValues);

        return contentList;
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
