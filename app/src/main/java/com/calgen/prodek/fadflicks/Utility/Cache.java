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

    /**
     * @param c    Context needed to fetch DefaultSharedPreferences
     * @param data Data that is supposed to be cached.
     */
    public static void cacheMovieData(Context c, String data) {
        try {
            SharedPreferences sharedPreferences = getDefaultSharedPreferences(c);
            Editor sharedPreferencesEditor = sharedPreferences.edit();
            sharedPreferencesEditor.putString(c.getResources().getString(R.string.movie_data_prefs_key), data);
            if (!sharedPreferencesEditor.commit())
                Log.e(TAG, "cacheMovieData: Editor couldn't commit");
        } catch (NullPointerException e) {
            Log.e(TAG, "cacheMovieData: " + e.toString(), e);
        }
    }

    /**
     * @param context
     * @return String data is returned,{@code null} if not found.
     */
    public static String getMovieData(Context context) {
        String movieData = null;
        SharedPreferences sharedPreferences = getDefaultSharedPreferences(context);
        if (sharedPreferences.contains(context.getResources().getString(R.string.movie_data_prefs_key))) {
            movieData = sharedPreferences.getString(context.getResources().getString(R.string.movie_data_prefs_key), null);
        }
        return movieData;
    }
}
