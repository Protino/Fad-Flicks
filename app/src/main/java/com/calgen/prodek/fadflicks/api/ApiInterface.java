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

package com.calgen.prodek.fadflicks.api;

import com.calgen.prodek.fadflicks.model.Credits;
import com.calgen.prodek.fadflicks.model.MovieDetails;
import com.calgen.prodek.fadflicks.model.MovieResponse;
import com.calgen.prodek.fadflicks.model.ReviewResponse;
import com.calgen.prodek.fadflicks.model.VideoResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiInterface {

    @GET("movie/{id}")
    Call<MovieDetails> getMovieDetails(@Path("id") int movieId);

    @GET("movie/{id}/credits")
    Call<Credits> getCredits(@Path("id") int movieId);

    @GET("movie/{id}/videos")
    Call<VideoResponse> getVideos(@Path("id") int movieId);

    @GET("movie/{id}/reviews")
    Call<ReviewResponse> getReviews(@Path("id") int movieId);

    @GET("discover/movie")
    Call<MovieResponse> getMovies(@Query("sort_by") String popularity, @Query("vote_count.gte") int voteCount);
}