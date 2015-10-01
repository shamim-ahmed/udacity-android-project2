package edu.udacity.android.popularmovies;

import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.util.Log;

public class SortPreferenceActivity extends PreferenceActivity implements Preference.OnPreferenceChangeListener {
    private static final String TAG = SortPreferenceActivity.class.getSimpleName();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.sort_preference);
        bindPreferenceSummaryToValue(findPreference("sort_order"));
    }

    /**
     * Attaches a listener so the summary is always updated with the preference value. Also fires
     * the listener once, to initialize the summary (so it shows up before the value is changed.)
     */
    private void bindPreferenceSummaryToValue(Preference preference) {
        // Set the listener to watch for value changes.
        preference.setOnPreferenceChangeListener(this);

        // Trigger the listener immediately with the preference's
        // current value.
        onPreferenceChange(preference,
                PreferenceManager
                        .getDefaultSharedPreferences(preference.getContext())
                        .getString(preference.getKey(), ""));
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object value) {
        String chosenValue = value.toString();
        PopularMoviesApplication application = (PopularMoviesApplication) getApplication();
        String activeValue = application.getActiveSortPreference();

        if (!chosenValue.equals(activeValue)) {
            Log.i(TAG, String.format("Sort preference value changed to %s", chosenValue));
            application.setActiveSortPreference(chosenValue);
            application.setSortPreferenceChanged(true);
        }

        if (preference instanceof ListPreference) {
            // For list preferences, look up the correct display value in
            // the preference's 'entries' list (since they have separate labels/values).
            ListPreference listPreference = (ListPreference) preference;
            int prefIndex = listPreference.findIndexOfValue(chosenValue);
            if (prefIndex >= 0) {
                preference.setSummary(listPreference.getEntries()[prefIndex]);
            }
        } else {
            // For other preferences, set the summary to the value's simple string representation.
            preference.setSummary(chosenValue);
        }

        return true;
    }
}
