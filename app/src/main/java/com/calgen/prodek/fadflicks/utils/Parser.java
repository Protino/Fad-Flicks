package com.calgen.prodek.fadflicks.utils;

import android.graphics.Typeface;
import android.net.Uri;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.util.Log;

import com.calgen.prodek.fadflicks.model.Cast;
import com.calgen.prodek.fadflicks.model.Credits;
import com.calgen.prodek.fadflicks.model.Crew;
import com.calgen.prodek.fadflicks.model.Genre;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Gurupad on 05-Jul-16.
 */
public class Parser {

    public static final String BASE_IMAGE_URL = "http://image.tmdb.org/t/p/";
    public static final String IMAGE_SIZE = "w185";
    private static final String TAG = Parser.class.getSimpleName();

    /**
     * @param movieData a JSON formatted string data fetched from movieDb.
     * @return Array of strings containing formatted poster URLs. null value if movieData is null.
     */
    public static String[] getAllMoviePosterUrls(String movieData) {
        JSONObject jsonObject;
        String[] posterUrls = null;
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
            Log.e(TAG, "getAllMoviePosterUrls: JSONException", e);
        }
        return posterUrls;
    }

    /**
     * @param movieData JSON formatted string data fetched from movieDb.
     * @param posterUrl Search key based on which details of a particular movie are fetched from {@code movieData}
     * @return JSONObject containing details of the movie searched. {@code null} if not found.
     */
    public static JSONObject getMovieDetailsByUrl(String movieData, String posterUrl) {
        JSONObject movieDetails;
        try {
            JSONObject jsonObject = new JSONObject(movieData);
            JSONArray jsonArray = jsonObject.getJSONArray("results");
            for (int i = 0; i < jsonArray.length(); i++) {
                movieDetails = jsonArray.getJSONObject(i);
                if (posterUrl.endsWith(movieDetails.getString("poster_path"))) {         //because posterUrl is not a relative path
                    return movieDetails;
                }
            }
        } catch (JSONException e) {
            Log.e(TAG, "getAllMoviePosterUrls: JSONException", e);
        }
        return null;
    }


    /**
     * @param imageUrl  Relative image path that is supposed to be appended.
     * @param imageSize Size of the image according to tmdb.org
     * @return Complete Url to locate the image resource.
     */
    public static String formatImageUrl(String imageUrl, String imageSize) {
        Uri uri = Uri.parse(BASE_IMAGE_URL)
                .buildUpon()
                .appendEncodedPath(imageSize)
                .appendEncodedPath(imageUrl)
                .build();
        return uri.toString();
    }

    /**
     * @param formatString the string that is supposed to be formatted
     * @param start        index from where to start
     * @param end          index where to end
     * @return formatted {@code SpannableString}
     */
    public static SpannableString formatIntoSpannableString(String formatString, int start, int end) {
        SpannableString spannableString = new SpannableString(formatString);
        spannableString.setSpan(new RelativeSizeSpan(1f), start, end, 0);
        spannableString.setSpan(new StyleSpan(Typeface.BOLD), start, end, 0);
        return spannableString;
    }

    /**
     * @param releaseDate date in the form yyyy-mm-dd
     * @return date in friendly form like JUN 2015
     */
    public static String formatReleaseDate(String releaseDate) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-mm-dd");
        Date date;
        try {
            date = formatter.parse(releaseDate);
            formatter = new SimpleDateFormat("MMM yyyy");
            return formatter.format(date);
        } catch (ParseException e) {
            Log.e(TAG, "formatReleaseDate:", e);
        }
        return releaseDate;
    }

    public static String getActors(Credits credits) {
        String actors = "";
        List<Cast> castList = credits.getCast();
        int listSize = castList.size();

        for (int i = 0; i < listSize - 2; i++) {
            actors += castList.get(i).getName() + ", ";
        }
        actors += castList.get(listSize - 2).getName() + " and " + castList.get(listSize - 1).getName();
        return actors;
    }

    public static String getDirectors(Credits credits) {
        List<String> directors = new ArrayList<>();
        for (Crew crew : credits.getCrew()) {
            if (crew.getJob().toLowerCase().contains("director"))
                directors.add(crew.getName());
        }
        return TextUtils.join(", ", directors);
    }


    public static String getProducers(Credits credits) {
        List<String> producers = new ArrayList<>();
        for (Crew crew : credits.getCrew()) {
            if (crew.getJob().toLowerCase().contains("producer"))
                producers.add(crew.getName());
        }
        return TextUtils.join(", ", producers);
    }


    public static String getMusicians(Credits credits) {
        List<String> musicians = new ArrayList<>();
        for (Crew crew : credits.getCrew()) {
            if (crew.getJob().toLowerCase().contains("music"))
                musicians.add(crew.getName());
        }
        return TextUtils.join(", ", musicians);
    }

    public static String getWriters(Credits credits) {
        List<String> writers = new ArrayList<>();
        for (Crew crew : credits.getCrew()) {
            String job = crew.getJob().toLowerCase();
            if (job.contains("writer") || job.contains("script") || job.contains("screenplay"))
                writers.add(crew.getName());
        }
        return TextUtils.join(", ", writers);
    }

    public static String getGenre(List<Genre> genreList) {
        String genres = "";
        for (Genre genre : genreList) {
            genres += genre.getName().toUpperCase() + "  ";
        }
        return genres;
    }
}
