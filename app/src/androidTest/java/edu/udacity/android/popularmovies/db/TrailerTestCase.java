package edu.udacity.android.popularmovies.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.test.ProviderTestCase2;

import java.util.ArrayList;
import java.util.List;

public class TrailerTestCase extends ProviderTestCase2<PopularMoviesProvider> {
    public TrailerTestCase() {
        super(PopularMoviesProvider.class, "edu.udacity.android.popularmovies");
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        clearTable(PopularMoviesContract.TrailerEntry.TABLE_NAME);
        clearTable(PopularMoviesContract.MovieEntry.TABLE_NAME);
    }

    public void testBulkInsert() {
        PopularMoviesProvider provider = getProvider();
        List<ContentValues> movieDataList = TestUtilities.createMovieValues();
        List<ContentValues> trailerDataList = TestUtilities.createTrailerValues();
        assertTrue("no movie data", movieDataList.size() > 0);
        assertTrue("no trailer data", trailerDataList.size() > 0);

        // first insert movies so that foreign key constraint is satisfied
        int movieInsertCount = provider.bulkInsert(PopularMoviesContract.MovieEntry.CONTENT_URI, movieDataList.toArray(new ContentValues[movieDataList.size()]));
        assertEquals("movie bulk insertion failed", movieDataList.size(), movieInsertCount);

        int trailerInsertCount = provider.bulkInsert(PopularMoviesContract.TrailerEntry.CONTENT_URI, trailerDataList.toArray(new ContentValues[trailerDataList.size()]));
        assertEquals("trailer bulk insertion failed", trailerDataList.size(), trailerInsertCount);
    }

    public void testQueryTrailerForMovie() {
        PopularMoviesProvider provider = getProvider();
        List<ContentValues> movieDataList = TestUtilities.createMovieValues();
        provider.bulkInsert(PopularMoviesContract.MovieEntry.CONTENT_URI, movieDataList.toArray(new ContentValues[movieDataList.size()]));

        List<ContentValues> trailerDataList = TestUtilities.createTrailerValues();
        provider.bulkInsert(PopularMoviesContract.TrailerEntry.CONTENT_URI, trailerDataList.toArray(new ContentValues[trailerDataList.size()]));

        // retrieve all trailers associated with a movie
        for (ContentValues movieValues : movieDataList) {
            Long movieId = (Long) movieValues.get(PopularMoviesContract.MovieEntry.COLUMN_MOVIE_ID);

            List<ContentValues> expectedTrailerDataList = filterTrailerDataForMovie(trailerDataList, movieId);

            if (expectedTrailerDataList.size() == 0) {
                continue;
            }

            Uri queryUri = PopularMoviesContract.TrailerEntry.buildTrailerUriForMovie(movieId);
            Cursor c = provider.query(queryUri, null, null, null, null);
            assertEquals("trailer count is different than expected", expectedTrailerDataList.size(), c.getCount());

            while (c.moveToNext()) {
                int index = c.getColumnIndex(PopularMoviesContract.TrailerEntry.COLUMN_TRAILER_ID);
                String trailerId = c.getString(index);
                ContentValues trailerValues = findTrailerValues(trailerDataList, trailerId);
                assertNotNull("could not find trailer values at current cursor location", trailerValues);
                TestUtilities.validateCurrentRecord("trailer values are different than expected", c, trailerValues);
            }

            c.close();
        }
    }

    private List<ContentValues> filterTrailerDataForMovie(List<ContentValues> trailerDataList, Long movieId) {
        List<ContentValues> resultList = new ArrayList<>();

        for (ContentValues values : trailerDataList) {
            Long mvId = (Long) values.get(PopularMoviesContract.TrailerEntry.COLUMN_MOVIE_ID);

            if (mvId.equals(movieId)) {
                resultList.add(values);
            }
        }

        return resultList;
    }

    private ContentValues findTrailerValues(List<ContentValues> trailerDataList, String trailerId) {
        ContentValues result = null;

        for (ContentValues values : trailerDataList) {
            String tId = (String) values.get(PopularMoviesContract.TrailerEntry.COLUMN_TRAILER_ID);

            if (tId.equals(trailerId)) {
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
