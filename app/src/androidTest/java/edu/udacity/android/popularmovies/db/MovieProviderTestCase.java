package edu.udacity.android.popularmovies.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.test.ProviderTestCase2;

import java.util.HashMap;
import java.util.List;

public class MovieProviderTestCase extends ProviderTestCase2<MovieProvider> {
    public MovieProviderTestCase() {
        super(MovieProvider.class, "edu.udacity.android.popularmovies");
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        clearMovieTable();
    }

    public void testInsertAndDelete() {
        MovieProvider provider = getProvider();
        List<ContentValues> movieDataList = TestUtilities.createMovieValues();
        assertTrue("no test data", movieDataList.size() > 0);

        ContentValues values = movieDataList.get(0);
        Uri resultUri = provider.insert(MovieContract.CONTENT_URI, values);
        String type = provider.getType(resultUri);
        assertEquals("Uri type is different than expected", MovieContract.CONTENT_ITEM_TYPE, type);

        long movieId = MovieContract.MovieEntry.getMovieIdFromUri(resultUri);
        long expectedId = (Long) values.get("movie_id");
        assertTrue("movieId is different than expected", movieId == expectedId);

        int count = provider.delete(resultUri, null, null);
        assertTrue("inserted movie was not deleted successfully", count == 1);
    }

    public void testQuery() {
        MovieProvider provider = getProvider();
        List<ContentValues> movieDataList = TestUtilities.createMovieValues();
        assertTrue("no test data", movieDataList.size() > 0);

        for (ContentValues values : movieDataList) {
            provider.insert(MovieContract.CONTENT_URI, values);
        }

        // retrieve individual movies
        for (ContentValues values : movieDataList) {
            long movieId = (Long) values.get("movie_id");
            Uri queryUri = MovieContract.MovieEntry.buildMovieUri(movieId);
            Cursor c = provider.query(queryUri, null, null, null, null);
            assertTrue("movie not found in database", c.moveToFirst());
            TestUtilities.validateCurrentRecord("movie values are different than expected", c, values);
            c.close();
        }

        // retrieve all movies
        HashMap<Long, ContentValues> movieDataMap = new HashMap<>();

        for (ContentValues values : movieDataList) {
            Long movieId = (Long) values.get("movie_id");
            movieDataMap.put(movieId, values);
        }

        Cursor cursor = provider.query(MovieContract.CONTENT_URI, null, null, null, null);

        while (cursor.moveToNext()) {
            int index = cursor.getColumnIndex("movie_id");
            Long movieId = cursor.getLong(index);
            ContentValues values = movieDataMap.remove(movieId);
            TestUtilities.validateCurrentRecord("movie data different than expected", cursor, values);
        }

        assertTrue("not all movies were retrieved", movieDataMap.isEmpty());

        cursor.close();
    }

    private void clearMovieTable() {
        MovieProvider provider = getProvider();
        MovieDbHelper dbHelper = provider.getMovieDbHelper();
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        try {
            db.execSQL("DELETE FROM " + MovieContract.MovieEntry.TABLE_NAME);
        } catch (Exception ex) {
            // ignore it
        }
    }
}
