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

package com.calgen.prodek.fadflicks.retrofitTests;

import android.test.AndroidTestCase;

import com.calgen.prodek.fadflicks.api.ApiClient;
import com.calgen.prodek.fadflicks.model.Credits;
import com.calgen.prodek.fadflicks.model.MovieDetails;
import com.calgen.prodek.fadflicks.model.MovieResponse;
import com.calgen.prodek.fadflicks.model.ReviewResponse;
import com.calgen.prodek.fadflicks.model.VideoResponse;
import com.calgen.prodek.fadflicks.utils.ApplicationConstants;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by Gurupad on 01-Sep-16.
 */
public class TestMovieDBApi extends AndroidTestCase {

    private static ApiClient apiClient;

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

    public void testMovieDetailsEndpoint() throws IOException {
        Call<MovieDetails> call = apiClient.movieInterface().getMovieDetails(TestData.MOVIE_ID);
        Response<MovieDetails> response = call.execute();

        assertEquals(response.code(), 200);
        assertEquals(true, response.isSuccessful());
    }


    public static class TestData {
        public static final String SORT_BY = "popularity.desc";
        public static final int VOTE_COUNt = 1000;
        public static final int MOVIE_ID = 209112; // Dark Knight Rises
    }
}
