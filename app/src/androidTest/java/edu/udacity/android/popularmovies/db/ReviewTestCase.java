package edu.udacity.android.popularmovies.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.test.ProviderTestCase2;

import java.util.ArrayList;
import java.util.List;

public class ReviewTestCase extends ProviderTestCase2<PopularMoviesProvider> {
    public ReviewTestCase() {
        super(PopularMoviesProvider.class, "edu.udacity.android.popularmovies");
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        clearTable(PopularMoviesContract.ReviewEntry.TABLE_NAME);
        clearTable(PopularMoviesContract.MovieEntry.TABLE_NAME);
    }

    public void testBulkInsert() {
        PopularMoviesProvider provider = getProvider();
        List<ContentValues> movieDataList = TestUtilities.createMovieValues();
        List<ContentValues> reviewDataList = TestUtilities.createReviewValues();
        assertTrue("no movie data", movieDataList.size() > 0);
        assertTrue("no review data", reviewDataList.size() > 0);

        // first insert movies so that foreign key constraint is satisfied
        int movieInsertCount = provider.bulkInsert(PopularMoviesContract.MovieEntry.CONTENT_URI, movieDataList.toArray(new ContentValues[movieDataList.size()]));
        assertEquals("movie bulk insertion failed", movieDataList.size(), movieInsertCount);

        int reviewInsertCount = provider.bulkInsert(PopularMoviesContract.ReviewEntry.CONTENT_URI, reviewDataList.toArray(new ContentValues[reviewDataList.size()]));
        assertEquals("review bulk insertion failed", reviewDataList.size(), reviewInsertCount);
    }

    public void testQueryReviewForMovie() {
        PopularMoviesProvider provider = getProvider();
        List<ContentValues> movieDataList = TestUtilities.createMovieValues();
        provider.bulkInsert(PopularMoviesContract.MovieEntry.CONTENT_URI, movieDataList.toArray(new ContentValues[movieDataList.size()]));

        List<ContentValues> reviewDataList = TestUtilities.createReviewValues();
        provider.bulkInsert(PopularMoviesContract.ReviewEntry.CONTENT_URI, reviewDataList.toArray(new ContentValues[reviewDataList.size()]));

        // retrieve all reviews associated with a movie
        for (ContentValues movieValues : movieDataList) {
            Long movieId = (Long) movieValues.get(PopularMoviesContract.MovieEntry.COLUMN_MOVIE_ID);

            List<ContentValues> expectedReviewDataList = filterReviewDataForMovie(reviewDataList, movieId);

            if (expectedReviewDataList.size() == 0) {
                continue;
            }

            Uri queryUri = PopularMoviesContract.ReviewEntry.buildReviewUriForMovie(movieId);
            Cursor c = provider.query(queryUri, null, null, null, null);
            assertEquals("review count is different than expected", expectedReviewDataList.size(), c.getCount());

            while (c.moveToNext()) {
                int index = c.getColumnIndex(PopularMoviesContract.ReviewEntry.COLUMN_REVIEW_ID);
                String reviewId = c.getString(index);
                ContentValues reviewValues = findReviewValues(reviewDataList, reviewId);
                assertNotNull("could not find review values at current cursor location", reviewValues);
                TestUtilities.validateCurrentRecord("review values are different than expected", c, reviewValues);
            }

            c.close();
        }
    }

    private List<ContentValues> filterReviewDataForMovie(List<ContentValues> reviewDataList, Long movieId) {
        List<ContentValues> resultList = new ArrayList<>();

        for (ContentValues values : reviewDataList) {
            Long mvId = (Long) values.get(PopularMoviesContract.ReviewEntry.COLUMN_MOVIE_ID);

            if (mvId.equals(movieId)) {
                resultList.add(values);
            }
        }

        return resultList;
    }

    private ContentValues findReviewValues(List<ContentValues> reviewDataList, String reviewId) {
        ContentValues result = null;

        for (ContentValues values : reviewDataList) {
            String tId = (String) values.get(PopularMoviesContract.ReviewEntry.COLUMN_REVIEW_ID);

            if (tId.equals(reviewId)) {
                result = values;
                break;
            }
        };

        return result;
    }

    private void clearTable(String tableName) {
        PopularMoviesProvider provider = getProvider();
        PopularMoviesDbHelper dbHelper = provider.getMovieDbHelper();
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        try {
            db.execSQL("DELETE FROM " + tableName);
        } catch (Exception ex) {
            // ignore it
        }
    }
}
