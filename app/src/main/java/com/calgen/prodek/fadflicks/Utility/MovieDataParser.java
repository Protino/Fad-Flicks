package com.calgen.prodek.fadflicks.Utility;

import android.net.Uri;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Gurupad on 05-Jul-16.
 */
public class MovieDataParser {

    private static final String LOG_TAG = MovieDataParser.class.getSimpleName();

    /**
     *
     * @param movieData Represents JSON formatted string data fetched from moviedb.
     * @return Array of strings containing formatted poster URLs. null value if movieData is null.
     */
    public static String[] getAllMoviePosterUrls(String movieData) {
        JSONObject jsonObject;
        String[] posterUrls = null;
        String BASE_IMAGE_URL = "http://image.tmdb.org/t/p/";
        String IMAGE_SIZE = "w185/";
        if (movieData == null)
            return null;
        try {
            jsonObject = new JSONObject(movieData);
            JSONArray jsonArray = jsonObject.getJSONArray("results");
            posterUrls = new String[jsonArray.length()];
            for (int i = 0; i < jsonArray.length(); i++) {

                //To get complete URL relative path must be suffixed to the base path
                //along with necessary parameters,if any.
                String relativePath = posterUrls[i] = jsonArray.getJSONObject(i).getString("poster_path");

                Uri uri = Uri.parse(BASE_IMAGE_URL)
                        .buildUpon()
                        .appendEncodedPath(IMAGE_SIZE)
                        .appendEncodedPath(relativePath)
                        .build();

                posterUrls[i] = uri.toString();
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e(LOG_TAG, "JSON parsing error :", e);
        }
        return posterUrls;
    }
}
