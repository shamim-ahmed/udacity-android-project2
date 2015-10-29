package edu.udacity.android.popularmovies.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.test.ProviderTestCase2;

import java.util.ArrayList;
import java.util.List;

public class PosterTestCase extends ProviderTestCase2<PopularMoviesProvider> {
    public PosterTestCase() {
        super(PopularMoviesProvider.class, "edu.udacity.android.popularmovies");
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        clearTable(PopularMoviesContract.PosterEntry.TABLE_NAME);
        clearTable(PopularMoviesContract.MovieEntry.TABLE_NAME);
    }

    public void testBulkInsert() {
        PopularMoviesProvider provider = getProvider();
        List<ContentValues> movieDataList = TestUtilities.createMovieValues();
        List<ContentValues> posterDataList = TestUtilities.createPosterValues();
        assertTrue("no movie data", movieDataList.size() > 0);
        assertTrue("no poster data", posterDataList.size() > 0);

        // first insert movies so that foreign key constraint is satisfied
        int movieInsertCount = provider.bulkInsert(PopularMoviesContract.MovieEntry.CONTENT_URI, movieDataList.toArray(new ContentValues[movieDataList.size()]));
        assertEquals("movie bulk insertion failed", movieDataList.size(), movieInsertCount);

        int posterInsertCount = provider.bulkInsert(PopularMoviesContract.PosterEntry.CONTENT_URI, posterDataList.toArray(new ContentValues[posterDataList.size()]));
        assertEquals("poster bulk insertion failed", posterDataList.size(), posterInsertCount);
    }

    public void testQueryPosterForMovie() {
        PopularMoviesProvider provider = getProvider();
        List<ContentValues> movieDataList = TestUtilities.createMovieValues();
        provider.bulkInsert(PopularMoviesContract.MovieEntry.CONTENT_URI, movieDataList.toArray(new ContentValues[movieDataList.size()]));

        List<ContentValues> posterDataList = TestUtilities.createPosterValues();
        provider.bulkInsert(PopularMoviesContract.PosterEntry.CONTENT_URI, posterDataList.toArray(new ContentValues[posterDataList.size()]));

        // retrieve all posters associated with a movie
        for (ContentValues movieValues : movieDataList) {
            Long movieId = (Long) movieValues.get(PopularMoviesContract.MovieEntry.COLUMN_MOVIE_ID);

            List<ContentValues> expectedPosterDataList = filterPosterDataForMovie(posterDataList, movieId);

            if (expectedPosterDataList.size() == 0) {
                continue;
            }

            Uri queryUri = PopularMoviesContract.PosterEntry.buildPosterUriForMovie(movieId);
            Cursor c = provider.query(queryUri, null, null, null, null);
            assertEquals("poster count is different than expected", expectedPosterDataList.size(), c.getCount());

            while (c.moveToNext()) {
                int index = c.getColumnIndex(PopularMoviesContract.PosterEntry.COLUMN_POSTER_ID);
                String posterId = c.getString(index);
                ContentValues posterValues = findPosterValues(posterDataList, posterId);
                assertNotNull("could not find poster values at current cursor location", posterValues);
                TestUtilities.validateCurrentPosterRecord("poster values are different than expected", c, posterValues);
            }

            c.close();
        }
    }

    private List<ContentValues> filterPosterDataForMovie(List<ContentValues> posterDataList, Long movieId) {
        List<ContentValues> resultList = new ArrayList<>();

        for (ContentValues values : posterDataList) {
            Long mvId = (Long) values.get(PopularMoviesContract.PosterEntry.COLUMN_MOVIE_ID);

            if (mvId.equals(movieId)) {
                resultList.add(values);
            }
        }

        return resultList;
    }

    private ContentValues findPosterValues(List<ContentValues> posterDataValues, String posterId) {
        ContentValues result = null;

        for (ContentValues values : posterDataValues) {
            String tId = (String) values.get(PopularMoviesContract.PosterEntry.COLUMN_POSTER_ID);

            if (tId.equals(posterId)) {
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
