package edu.udacity.android.popularmovies;

import android.app.Application;

public class PopularMoviesApplication extends Application {
    private String currentSortOrder;

    public PopularMoviesApplication() {
        currentSortOrder = "popularity.desc";
    }

    public synchronized String getCurrentSortOrder() {
        return currentSortOrder;
    }

    public synchronized void setCurrentSortOrder(String currentSortOrder) {
        this.currentSortOrder = currentSortOrder;
    }
}
