package com.calgen.prodek.fadflicks.RetrofitTests;

import android.test.AndroidTestCase;

import com.calgen.prodek.fadflicks.Utility.ApplicationConstants;
import com.calgen.prodek.fadflicks.api.ApiClient;
import com.calgen.prodek.fadflicks.model.MovieResponse;

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

        Call<MovieResponse> topRatedList = apiClient.movieInterface().getMovies(TestData.SORT_BY, TestData.VOTE_COUNt);
        Response<MovieResponse> movieResponse = topRatedList.execute();

        assertEquals(movieResponse.code(), 200);
        assertEquals(true, movieResponse.isSuccessful());

    }


    public static class TestData {
        public static final String SORT_BY = "popularity.desc";
        public static final int VOTE_COUNt = 1000;
    }
}
