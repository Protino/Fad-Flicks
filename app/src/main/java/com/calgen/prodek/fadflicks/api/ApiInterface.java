package com.calgen.prodek.fadflicks.api;

import com.calgen.prodek.fadflicks.model.MovieResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiInterface {
    @GET("discover/movie")
    Call<MovieResponse> getMovies(@Query("sort_by") String popularity, @Query("vote_count.gte") int voteCount);
}