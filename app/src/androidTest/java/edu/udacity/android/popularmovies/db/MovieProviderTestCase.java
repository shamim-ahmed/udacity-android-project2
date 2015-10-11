package edu.udacity.android.popularmovies.db;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.test.ProviderTestCase2;

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
        ContentValues values = TestUtilities.createMovieValues();
        Uri resultUri = provider.insert(MovieContract.CONTENT_URI, values);
        long movieId = MovieContract.MovieEntry.getMovieIdFromUri(resultUri);
        long expectedId = (Long) values.get("movie_id");
        assertTrue("movieId is different than expected", movieId == expectedId);

        int count = provider.delete(resultUri, null, null);
        assertTrue("inserted movie was not deleted successfully", count == 1);
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
