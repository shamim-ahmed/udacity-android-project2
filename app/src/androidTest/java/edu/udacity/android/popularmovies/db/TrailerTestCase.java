package edu.udacity.android.popularmovies.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.test.ProviderTestCase2;

import java.util.HashMap;
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

    public void testBulkInsertAndDelete() {
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

        int trailerDeleteCount = provider.delete(PopularMoviesContract.TrailerEntry.CONTENT_URI, null, null);
        assertEquals("trailer deletion failed", trailerDataList.size(), trailerDeleteCount);

        int movieDeleteCount = provider.delete(PopularMoviesContract.MovieEntry.CONTENT_URI, null, null);
        assertEquals("movie deletion failed", movieDataList.size(), movieDeleteCount);
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
