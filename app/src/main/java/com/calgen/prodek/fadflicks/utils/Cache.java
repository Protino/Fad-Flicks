package com.calgen.prodek.fadflicks.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.calgen.prodek.fadflicks.R;
import com.calgen.prodek.fadflicks.model.MovieBundle;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;

/**
 * Created by Gurupad on 06-Jul-16.
 * You asked me to change it for no reason.
 */
public class Cache {
    private static final String TAG = "Cache";

    public Cache() {
    }

    /**
     * @param context     Context needed to fetch DefaultSharedPreferences
     * @param movieBundle Data that is supposed to be cached.
     */
    public static void cacheMovieData(Context context, MovieBundle movieBundle) {
        SharedPreferences sharedPreferences = getDefaultSharedPreferences(context);
        Editor sharedPreferencesEditor = sharedPreferences.edit();
        Gson gson = new Gson();
        String data = gson.toJson(movieBundle);
        String key = context.getResources().getString(R.string.fav_movie_base_key) + movieBundle.movie.getId();
        sharedPreferencesEditor.putString(key, data);
        sharedPreferencesEditor.apply();
    }

    /**
     * @param context Context needed to fetch DefaultSharedPreferences
     * @return String data is returned,{@code null} if not found.
     */
    public static MovieBundle getMovieData(Context context, Integer movieId) {
        MovieBundle movieBundle;
        SharedPreferences sharedPreferences = getDefaultSharedPreferences(context);
        String key = context.getResources().getString(R.string.fav_movie_base_key) + movieId;
        if (sharedPreferences.contains(key)) {
            String jsonStr = sharedPreferences.getString(key, null);
            movieBundle = new Gson().fromJson(jsonStr, MovieBundle.class);
            return movieBundle;
        }
        return null;
    }

    public static List<MovieBundle> bulkReadMovieData(Context context, List<Integer> movieIds) {
        List<MovieBundle> movieBundleList = new ArrayList<>();
        SharedPreferences sharedPreferences = getDefaultSharedPreferences(context);
        String baseKey = context.getResources().getString(R.string.fav_movie_base_key);
        for (Integer id : movieIds) {
            String key = baseKey + id;
            if (sharedPreferences.contains(key)) {
                String jsonStr = sharedPreferences.getString(key, null);
                movieBundleList.add(new Gson().fromJson(jsonStr, MovieBundle.class));
            }
        }
        return movieBundleList;
    }

    public static void setFavouriteMovie(Context context, int movieId, boolean isFavourite) {
        SharedPreferences sharedPreferences = getDefaultSharedPreferences(context);
        Editor sharedPreferencesEditor = sharedPreferences.edit();
        HashMap<Integer, Boolean> map = getFavouriteMovies(context);
        if (map == null) {
            map = new HashMap<>();
        }
        map.put(movieId, isFavourite);
        Gson gson = new Gson();
        String data = gson.toJson(map);
        sharedPreferencesEditor.putString(context.getResources().getString(R.string.fav_movies_prefs_key), data);
        sharedPreferencesEditor.apply();
    }

    public static HashMap<Integer, Boolean> getFavouriteMovies(Context context) {
        String data = null;
        SharedPreferences sharedPreferences = getDefaultSharedPreferences(context);
        if (sharedPreferences.contains(context.getResources().getString(R.string.fav_movies_prefs_key))) {
            data = sharedPreferences.getString(context.getResources().getString(R.string.fav_movies_prefs_key), null);
            if (data == null)
                return null;
            Gson gson = new Gson();
            Type type = new TypeToken<HashMap<Integer, Boolean>>() {
            }.getType();
            return gson.fromJson(data, type);
        } else {
            return null;
        }
    }

    public static void bulkInsertFavouriteMovies(Context context, HashMap<Integer, Boolean> map) {
        SharedPreferences sharedPreferences = getDefaultSharedPreferences(context);
        Editor sharedPreferencesEditor = sharedPreferences.edit();
        HashMap<Integer, Boolean> cachedMap = getFavouriteMovies(context);
        if (map == null) {
            cachedMap = new HashMap<>();
        }
        for (HashMap.Entry<Integer, Boolean> entry : map.entrySet()) {
            cachedMap.put(entry.getKey(), entry.getValue());
        }
        Gson gson = new Gson();
        String data = gson.toJson(cachedMap);
        sharedPreferencesEditor.putString(context.getResources().getString(R.string.fav_movies_prefs_key), data);
        sharedPreferencesEditor.apply();
    }

    public static void clearFavouriteMovies(Context context) {
        SharedPreferences sharedPreferences = getDefaultSharedPreferences(context);
        Editor sharedPreferencesEditor = sharedPreferences.edit();
        HashMap<Integer, Boolean> map = new HashMap<>();
        Gson gson = new Gson();
        String data = gson.toJson(map);
        sharedPreferencesEditor.putString(context.getResources().getString(R.string.fav_movies_prefs_key), data);
        sharedPreferencesEditor.apply();
    }
}
