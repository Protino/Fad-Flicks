package com.calgen.prodek.fadflicks.Utility;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

import com.calgen.prodek.fadflicks.R;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;

/**
 * Created by Gurupad on 06-Jul-16.
 */
public class Cache {
    private static final String TAG = "Cache";

    public static void cacheMovieData(Context c, String data) {
        SharedPreferences sharedPreferences = getDefaultSharedPreferences(c);
        Editor sharedPreferencesEditor = sharedPreferences.edit();
        sharedPreferencesEditor.putString(c.getResources().getString(R.string.movie_data_prefs_key), data);
        if (!sharedPreferencesEditor.commit())
            Log.e(TAG, "cacheMovieData: Editor couldn't commit");
        else
            Log.d(TAG, "cacheMovieData: committed");
    }

    public static String getMovieData(Context context) {
        String movieData = null;
        SharedPreferences sharedPreferences = getDefaultSharedPreferences(context);
        if (sharedPreferences.contains(context.getResources().getString(R.string.movie_data_prefs_key))) {
            movieData = sharedPreferences.getString(context.getResources().getString(R.string.movie_data_prefs_key), null);
        }
        return movieData;
    }


    public static String getSortType(Context context) {
        String defaultSortType = context.getResources().getString(R.string.pref_sort_type_default);
        SharedPreferences sharedPreferences = getDefaultSharedPreferences(context);
        if (sharedPreferences.contains(context.getResources().getString(R.string.pref_sort_type_key))) {
            return sharedPreferences.getString(context.getResources().getString(R.string.pref_sort_type_key)
                    , defaultSortType);
        } else
            return defaultSortType;
    }
}
