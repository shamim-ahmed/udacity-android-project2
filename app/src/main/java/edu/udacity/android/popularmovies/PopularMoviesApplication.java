package edu.udacity.android.popularmovies;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.LruCache;

import java.util.Properties;

import edu.udacity.android.popularmovies.util.AppUtils;
import edu.udacity.android.popularmovies.util.Constants;

public class PopularMoviesApplication extends Application {
    private String activeSortPreference;
    private boolean reloadFlag;
    private int numberOfColumnsInGrid;
    private Properties configProperties;

    // We found that the getView method of MovieGridAdapter is often called multiple times
    // for the same position. This results in multiple queries for the same poster. In order
    // to avoid this scenario, we have introduced an in-memory cache for posters. Only
    // posters that are retrieved from database will be cached.
    // NOTE : the LruCache related code was copied from android official documentation
    final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
    final int cacheSize = maxMemory / 8;
    private LruCache<String, Bitmap> posterCache = new LruCache<String, Bitmap>(cacheSize) {
        @Override
        protected int sizeOf(String key, Bitmap bitmap) {
            // The cache size will be measured in kilobytes rather than number of items.
            return bitmap.getByteCount() / 1024;
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();

        Context context = getApplicationContext();

        if (AppUtils.isTablet(context)) {
            numberOfColumnsInGrid = Constants.TABLET_COLUMN_COUNT;
        } else {
            numberOfColumnsInGrid = Constants.MOBILE_COLUMN_COUNT;
        }

        activeSortPreference = AppUtils.readSortOrderFromPreference(context);
        configProperties = AppUtils.readConfiguration(context);
    }

    public synchronized String getActiveSortPreference() {
        return activeSortPreference;
    }

    public synchronized void setActiveSortPreference(String activeSortPreference) {
        this.activeSortPreference = activeSortPreference;
    }

    public synchronized boolean isReloadFlag() {
        return reloadFlag;
    }

    public synchronized void setReloadFlag(boolean reloadFlag) {
        this.reloadFlag = reloadFlag;
    }

    public synchronized void clearSortPreferenceChanged() {
        setReloadFlag(false);
    }

    public synchronized int getNumberOfColumnsInGrid() {
        return numberOfColumnsInGrid;
    }

    public String getConfigurationProperty(String key, String... substitutes) {
        String configValue = configProperties.getProperty(key);

        for (int i = 0; i < substitutes.length; i++) {
            configValue = configValue.replaceAll(String.format("\\{%d\\}", i), substitutes[i]);
        }

        return configValue;
    }

    public void addBitmapToMemoryCache(String posterId, Bitmap bitmap) {
        if (getBitmapFromMemCache(posterId) == null) {
            posterCache.put(posterId, bitmap);
        }
    }

    public Bitmap getBitmapFromMemCache(String posterId) {
        return posterCache.get(posterId);
    }
}
