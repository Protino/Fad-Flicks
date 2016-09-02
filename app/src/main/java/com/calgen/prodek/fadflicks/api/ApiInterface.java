package com.calgen.prodek.fadflicks.api;

import com.calgen.prodek.fadflicks.model.Credits;
import com.calgen.prodek.fadflicks.model.Genres;
import com.calgen.prodek.fadflicks.model.MovieResponse;
import com.calgen.prodek.fadflicks.model.ReviewResponse;
import com.calgen.prodek.fadflicks.model.VideoResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiInterface {


    @GET("genre/movie/list")
    Call<Genres> getGenres();

    @GET("movie/{id}/credits")
    Call<Credits> getCredits(@Path("id") int movieId);

    @GET("movie/{id}/videos")
    Call<VideoResponse> getVideos(@Path("id") int movieId);

    @GET("movie/{id}/reviews")
    Call<ReviewResponse> getReviews(@Path("id") int movieId);

    @GET("discover/movie")
    Call<MovieResponse> getMovies(@Query("sort_by") String popularity, @Query("vote_count.gte") int voteCount);
}