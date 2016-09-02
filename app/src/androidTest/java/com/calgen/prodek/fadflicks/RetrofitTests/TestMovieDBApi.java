package com.calgen.prodek.fadflicks.RetrofitTests;

import android.test.AndroidTestCase;

import com.calgen.prodek.fadflicks.Utility.ApplicationConstants;
import com.calgen.prodek.fadflicks.api.ApiClient;
import com.calgen.prodek.fadflicks.model.Credits;
import com.calgen.prodek.fadflicks.model.Genres;
import com.calgen.prodek.fadflicks.model.MovieResponse;
import com.calgen.prodek.fadflicks.model.ReviewResponse;
import com.calgen.prodek.fadflicks.model.VideoResponse;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by Gurupad on 01-Sep-16.
 * You asked me to change it for no reason.
 */
public class TestMovieDBApi extends AndroidTestCase {

    public static ApiClient apiClient;

    public TestMovieDBApi() {
        apiClient = new ApiClient();
        apiClient.setIsDebug(ApplicationConstants.DEBUG);
    }

    public void testMoviesEndpoint() throws IOException {

        Call<MovieResponse> call = apiClient.movieInterface().getMovies(TestData.SORT_BY, TestData.VOTE_COUNt);
        Response<MovieResponse> response = call.execute();

        assertEquals(response.code(), 200);
        assertEquals(true, response.isSuccessful());

    }

    public void testGenresEndpoint() throws IOException {
        Call<Genres> call = apiClient.movieInterface().getGenres();
        Response<Genres> response = call.execute();

        assertEquals(response.code(), 200);
        assertEquals(true, response.isSuccessful());
    }

    public void testVideosEndpoint() throws IOException {
        Call<VideoResponse> call = apiClient.movieInterface().getVideos(TestData.MOVIE_ID);
        Response<VideoResponse> response = call.execute();

        assertEquals(response.code(), 200);
        assertEquals(true, response.isSuccessful());

    }

    public void testReviewsEndpoint() throws IOException {
        Call<ReviewResponse> call = apiClient.movieInterface().getReviews(TestData.MOVIE_ID);
        Response<ReviewResponse> response = call.execute();

        assertEquals(response.code(), 200);
        assertEquals(true, response.isSuccessful());
    }

    public void testCreditsEndpoint() throws IOException {
        Call<Credits> call = apiClient.movieInterface().getCredits(TestData.MOVIE_ID);
        Response<Credits> response = call.execute();

        assertEquals(response.code(), 200);
        assertEquals(true, response.isSuccessful());
    }


    public static class TestData {
        public static final String SORT_BY = "popularity.desc";
        public static final int VOTE_COUNt = 1000;
        public static final int MOVIE_ID = 209112; // Dark Knight Rises
    }
}
