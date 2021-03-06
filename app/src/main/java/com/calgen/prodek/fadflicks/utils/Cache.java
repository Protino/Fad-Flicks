/*
 *    Copyright 2016 Gurupad Mamadapur
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

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
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.TimeZone;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;

/**
 * Handles management of cache data of the application stored as defaultSharedPreferences.
 */
public class Cache {

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
        sharedPreferencesEditor.commit();
    }

    /**
     * @param context {@link Context} needed to fetch DefaultSharedPreferences
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

    /**
     * Read movie details of all movies whose id's are specified as {@code List<Integer> movieIds}
     * from cache.
     * @param context {@link Context} needed to fetch DefaultSharedPreferences.
     * @param movieIds list of movieIds whose data is to be fetched.
     * @return {@code List<MovieBundle>} movieDetails list.
     */
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

    /**
     * @param context {@link Context} needed to fetch DefaultSharedPreferences.
     * @param movieId id of movie whose favourite needs to be cached.
     * @param isFavourite favourite value.
     */
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
        sharedPreferencesEditor.commit();
    }

    /**
     * @param context {@link Context} needed to fetch DefaultSharedPreferences
     * @return {@code HashMap<Integer, Boolean>} of favourite movies.
     */
    public static HashMap<Integer, Boolean> getFavouriteMovies(Context context) {
        String data;
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

    /**
     * @param context {@link Context} needed to fetch DefaultSharedPreferences
     * @param map map of movie's which need to be cached.
     */
    public static void bulkInsertFavouriteMovies(Context context, HashMap<Integer, Boolean> map) {
        SharedPreferences sharedPreferences = getDefaultSharedPreferences(context);
        Editor sharedPreferencesEditor = sharedPreferences.edit();
        HashMap<Integer, Boolean> cachedMap = getFavouriteMovies(context);
        if (map == null) {
            cachedMap = new HashMap<>();
        }
        if (map != null) {
            for (HashMap.Entry<Integer, Boolean> entry : map.entrySet()) {
                if (cachedMap != null) {
                    cachedMap.put(entry.getKey(), entry.getValue());
                }
            }
        }
        Gson gson = new Gson();
        String data = gson.toJson(cachedMap);
        sharedPreferencesEditor.putString(context.getResources().getString(R.string.fav_movies_prefs_key), data);
        sharedPreferencesEditor.apply();
    }

    /**
     * Clears {@code defaultSharedPreferences}.
     * @param context {@link Context} needed to fetch DefaultSharedPreferences
     */
    public static void purgeCache(Context context) {
        SharedPreferences sharedPreferences = getDefaultSharedPreferences(context);
        Editor sharedPreferencesEditor = sharedPreferences.edit();
        sharedPreferencesEditor.clear();
        sharedPreferencesEditor.apply();
    }

    /**
     * Determine whether cache clearance is required based on how long it has been since the last
     * usage of application.
     * @param context {@link Context} needed to fetch DefaultSharedPreferences
     * @return
     */
    public static boolean isPurgeRequired(Context context) {
        long currentDate = new Date().getTime();
        long lastUsageDate = readTimeOfLastUsage(context);
        return (currentDate - lastUsageDate > ApplicationConstants.CACHE_PURGE_PERIOD);
    }

    /**
     * Store the current time including timezone
     * @param context {@link Context} needed to fetch DefaultSharedPreferences
     */
    public static void cacheTimeOfLastUsage(Context context) {
        SharedPreferences sharedPreferences = getDefaultSharedPreferences(context);
        Editor sharedPreferencesEditor = sharedPreferences.edit();
        Date date = new Date();
        long currentTimeMillis = date.getTime();
        sharedPreferencesEditor.putLong(context.getString(R.string.time_of_last_usage_key), currentTimeMillis);
        sharedPreferencesEditor.putString(context.getString(R.string.last_usage_timezone), TimeZone.getDefault().getID());
        sharedPreferencesEditor.apply();
    }

    /**
     * @param context {@link Context} needed to fetch DefaultSharedPreferences
     * @return time when the application was last used.
     */
    public static Long readTimeOfLastUsage(Context context) {
        SharedPreferences sharedPreferences = getDefaultSharedPreferences(context);
        if (sharedPreferences.contains(context.getString(R.string.time_of_last_usage_key))) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(sharedPreferences.getLong(context.getString(R.string.time_of_last_usage_key), 0));
            calendar.setTimeZone(TimeZone.getTimeZone(sharedPreferences.getString(context.getString(R.string.last_usage_timezone), TimeZone.getDefault().getID())));
            return calendar.getTime().getTime();

        } else {
            cacheTimeOfLastUsage(context);
            return readTimeOfLastUsage(context);
        }
    }
}
