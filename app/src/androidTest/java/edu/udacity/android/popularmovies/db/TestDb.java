package edu.udacity.android.popularmovies.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;

import java.util.HashSet;
import java.util.List;

public class TestDb extends AndroidTestCase {
    @Override
    public void setUp() throws Exception {
        super.setUp();
        deleteDataFromTable();
    }

    private void deleteDataFromTable() {
        MovieDbHelper dbHelper = new MovieDbHelper(mContext);
        SQLiteDatabase db =  dbHelper.getWritableDatabase();

        try {
            db.execSQL("DELETE FROM " + MovieContract.MovieEntry.TABLE_NAME);
        } catch (Exception ex) {
            // ignore it
        }
    }

    public void testCreateTable() throws Throwable {
        MovieDbHelper dbHelper = new MovieDbHelper(mContext);
        SQLiteDatabase db =  dbHelper.getWritableDatabase();
        assertEquals(true, db.isOpen());

        // have we created the tables we want?
        Cursor c = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);

        assertTrue("Error: This means that the database has not been created correctly", c.moveToFirst());

        boolean tableFound = false;

        do {
            String tableName = c.getString(0);

            if (MovieContract.MovieEntry.TABLE_NAME.equals(tableName)) {
                tableFound = true;
                break;
            }
        } while (c.moveToNext());

        assertTrue("Error : Movie table was not created successfully", tableFound);

        // now, do our table contains the correct columns?
        c = db.rawQuery("PRAGMA table_info(" + MovieContract.MovieEntry.TABLE_NAME + ")", null);

        assertTrue("Error: This means that we were unable to query the database for table information.", c.moveToFirst());

        // Build a HashSet of all of the column names we want to look for
        final HashSet<String> movieColumnHashSet = new HashSet<String>();
        movieColumnHashSet.add(MovieContract.MovieEntry._ID);
        movieColumnHashSet.add(MovieContract.MovieEntry.COLUMN_MOVIE_ID);
        movieColumnHashSet.add(MovieContract.MovieEntry.COLUMN_TITLE);
        movieColumnHashSet.add(MovieContract.MovieEntry.COLUMN_POSTER_URI);
        movieColumnHashSet.add(MovieContract.MovieEntry.COLUMN_RELEASE_DATE);
        movieColumnHashSet.add(MovieContract.MovieEntry.COLUMN_SYNOPSIS);
        movieColumnHashSet.add(MovieContract.MovieEntry.COLUMN_VOTE_AVERAGE);

        int columnNameIndex = c.getColumnIndex("name");
        do {
            String columnName = c.getString(columnNameIndex);
            movieColumnHashSet.remove(columnName);
        } while(c.moveToNext());

        // if this fails, it means that your database doesn't contain all of the required location
        // entry columns
        assertTrue("Error: The database doesn't contain all of the required movie entry columns",
                movieColumnHashSet.isEmpty());
        c.close();
        db.close();
    }

    public void testMovieTable() {
        MovieDbHelper dbHelper = new MovieDbHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        assertTrue("database is not open", db.isOpen());

        List<ContentValues> movieDataList = TestUtilities.createMovieValues();
        assertTrue("no movie data", movieDataList.size() > 0);

        ContentValues values = movieDataList.get(0);
        long _id = db.insert(MovieContract.MovieEntry.TABLE_NAME, null, values);
        assertTrue("insert in Movie table failed", _id != -1);

        Cursor c = db.query(MovieContract.MovieEntry.TABLE_NAME, null, "_ID = ?", new String[] {Long.toString(_id)}, null, null, null, null);
        assertTrue("the row from Movie table could not be retrieved", c.moveToFirst());
        TestUtilities.validateCurrentRecord("retrieved values do not match inserted values", c, values);

        c.close();
        db.close();
    }
}
