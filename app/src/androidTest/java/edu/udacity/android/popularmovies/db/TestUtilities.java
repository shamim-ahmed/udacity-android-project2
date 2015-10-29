package edu.udacity.android.popularmovies.db;

import android.content.ContentValues;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static junit.framework.Assert.*;

public class TestUtilities {

    public static List<ContentValues> createMovieValues() {
        List<ContentValues> contentList = new ArrayList<>();

        ContentValues inceptionValues = new ContentValues();
        inceptionValues.put(PopularMoviesContract.MovieEntry.COLUMN_MOVIE_ID, 27205L);
        inceptionValues.put(PopularMoviesContract.MovieEntry.COLUMN_TITLE, "Inception");
        inceptionValues.put(PopularMoviesContract.MovieEntry.COLUMN_POSTER_URI, "http://image.tmdb.org/t/p/w185/qmDpIHrmpJINaRKAfWQfftjCdyi.jpg");
        inceptionValues.put(PopularMoviesContract.MovieEntry.COLUMN_RELEASE_DATE, "2010-07-16");
        inceptionValues.put(PopularMoviesContract.MovieEntry.COLUMN_SYNOPSIS, "Cobb, a skilled thief who commits corporate espionage by infiltrating the subconscious of his targets is offered a chance to regain his old life as payment for a task considered to be impossible");
        contentList.add(inceptionValues);

        ContentValues jurassicWorldValues = new ContentValues();
        jurassicWorldValues.put(PopularMoviesContract.MovieEntry.COLUMN_MOVIE_ID, 135397L);
        jurassicWorldValues.put(PopularMoviesContract.MovieEntry.COLUMN_TITLE, "Jurassic World");
        jurassicWorldValues.put(PopularMoviesContract.MovieEntry.COLUMN_POSTER_URI, "http://image.tmdb.org/t/p/w185/jjBgi2r5cRt36xF6iNUEhzscEcb.jpg");
        jurassicWorldValues.put(PopularMoviesContract.MovieEntry.COLUMN_RELEASE_DATE, "2015-06-12");
        jurassicWorldValues.put(PopularMoviesContract.MovieEntry.COLUMN_SYNOPSIS, "Twenty-two years after the events of Jurassic Park, Isla Nublar now features a fully functioning dinosaur theme park, Jurassic World, as originally envisioned by John Hammond.");
        contentList.add(jurassicWorldValues);

        return contentList;
    }

    public static List<ContentValues> createTrailerValues() {
        List<ContentValues> contentList = new ArrayList<>();

        ContentValues inceptionTrailerValues = new ContentValues();
        inceptionTrailerValues.put(PopularMoviesContract.TrailerEntry.COLUMN_TRAILER_ID, "abcd1234");
        inceptionTrailerValues.put(PopularMoviesContract.TrailerEntry.COLUMN_TRAILER_NAME, "Inception official trailer");
        inceptionTrailerValues.put(PopularMoviesContract.TrailerEntry.COLUMN_TRAILER_SITE, "https://www.youtube.com");
        inceptionTrailerValues.put(PopularMoviesContract.TrailerEntry.COLUMN_MOVIE_ID, 27205L);

        ContentValues jurassicFirstTrailerValues = new ContentValues();
        jurassicFirstTrailerValues.put(PopularMoviesContract.TrailerEntry.COLUMN_TRAILER_ID, "efgh5678");
        jurassicFirstTrailerValues.put(PopularMoviesContract.TrailerEntry.COLUMN_TRAILER_NAME, "US Official Trailer");
        jurassicFirstTrailerValues.put(PopularMoviesContract.TrailerEntry.COLUMN_TRAILER_SITE, "https://www.youtube.com");
        jurassicFirstTrailerValues.put(PopularMoviesContract.TrailerEntry.COLUMN_MOVIE_ID, 135397L);

        ContentValues jurassicSecondTrailerValues = new ContentValues();
        jurassicSecondTrailerValues.put(PopularMoviesContract.TrailerEntry.COLUMN_TRAILER_ID, "efgh91011");
        jurassicSecondTrailerValues.put(PopularMoviesContract.TrailerEntry.COLUMN_TRAILER_NAME, "World Official Trailer");
        jurassicSecondTrailerValues.put(PopularMoviesContract.TrailerEntry.COLUMN_TRAILER_SITE, "https://www.youtube.com");
        jurassicSecondTrailerValues.put(PopularMoviesContract.TrailerEntry.COLUMN_MOVIE_ID, 135397L);

        contentList.addAll(Arrays.asList(inceptionTrailerValues, jurassicFirstTrailerValues, jurassicSecondTrailerValues));
        return contentList;
    }

    public static List<ContentValues> createReviewValues() {
        List<ContentValues> contentList = new ArrayList<>();
        ContentValues inceptionReviewValues = new ContentValues();
        inceptionReviewValues.put(PopularMoviesContract.ReviewEntry.COLUMN_REVIEW_ID, "abcd1234");
        inceptionReviewValues.put(PopularMoviesContract.ReviewEntry.COLUMN_AUTHOR, "shamim");
        inceptionReviewValues.put(PopularMoviesContract.ReviewEntry.COLUMN_CONTENT, "What an amazing movie !");
        inceptionReviewValues.put(PopularMoviesContract.ReviewEntry.COLUMN_MOVIE_ID, 27205L);

        ContentValues jurassicFirstReviewValues = new ContentValues();
        jurassicFirstReviewValues.put(PopularMoviesContract.ReviewEntry.COLUMN_REVIEW_ID, "efgh5678");
        jurassicFirstReviewValues.put(PopularMoviesContract.ReviewEntry.COLUMN_AUTHOR, "batman");
        jurassicFirstReviewValues.put(PopularMoviesContract.ReviewEntry.COLUMN_CONTENT, "Why did we clone dinosaurs again ?");
        jurassicFirstReviewValues.put(PopularMoviesContract.ReviewEntry.COLUMN_MOVIE_ID, 135397L);

        ContentValues jurassicSecondReviewValues = new ContentValues();
        jurassicSecondReviewValues.put(PopularMoviesContract.ReviewEntry.COLUMN_REVIEW_ID, "ijkl9102");
        jurassicSecondReviewValues.put(PopularMoviesContract.ReviewEntry.COLUMN_AUTHOR, "robin");
        jurassicSecondReviewValues.put(PopularMoviesContract.ReviewEntry.COLUMN_CONTENT, "Is it legal to keep a dinosaur as pet ?");
        jurassicSecondReviewValues.put(PopularMoviesContract.ReviewEntry.COLUMN_MOVIE_ID, 135397L);

        contentList.addAll(Arrays.asList(inceptionReviewValues, jurassicFirstReviewValues, jurassicSecondReviewValues));

        return contentList;
    }

    public static void validateCurrentRecord(String error, Cursor valueCursor, ContentValues expectedValues) {
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
