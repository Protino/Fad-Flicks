package com.calgen.prodek.fadflicks.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.calgen.prodek.fadflicks.Activity.DetailActivity;
import com.calgen.prodek.fadflicks.Adapter.DetailMovieAdapter;
import com.calgen.prodek.fadflicks.R;
import com.calgen.prodek.fadflicks.Utility.ApplicationConstants;
import com.calgen.prodek.fadflicks.Utility.Network;
import com.calgen.prodek.fadflicks.api.ApiClient;
import com.calgen.prodek.fadflicks.model.Credits;
import com.calgen.prodek.fadflicks.model.Movie;
import com.calgen.prodek.fadflicks.model.MovieBundle;
import com.calgen.prodek.fadflicks.model.MovieDetails;
import com.calgen.prodek.fadflicks.model.ReviewResponse;
import com.calgen.prodek.fadflicks.model.VideoResponse;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A placeholder fragment containing a simple view.
 */
public class MovieDetailFragment extends Fragment {

    private static final String TAG = DetailActivity.class.getSimpleName();
    public byte jobsDone = 0;
    public MovieBundle movieBundle;
    public Movie movie;
    public ReviewResponse reviewResponse;
    public VideoResponse videoResponse;
    public Credits credits;
    public MovieDetails movieDetails;
    @BindView(R.id.detail_recycler_view)
    RecyclerView baseRecyclerView;
    private DetailMovieAdapter detailMovieAdapter;

    public MovieDetailFragment() {
        jobsDone = 0;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);
        ButterKnife.bind(this, rootView);

        movie = (Movie) getActivity().getIntent().getSerializableExtra(Intent.EXTRA_TEXT);
        movieBundle = new MovieBundle();
        jobsDone = 0;

        detailMovieAdapter = new DetailMovieAdapter(getContext(), movieBundle);
        RecyclerView.LayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        baseRecyclerView.setLayoutManager(linearLayoutManager);
        baseRecyclerView.setNestedScrollingEnabled(false);
        baseRecyclerView.setItemAnimator(new DefaultItemAnimator());
        baseRecyclerView.setAdapter(detailMovieAdapter);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        fetchData();
    }

    private void fetchData() {
        //handle network connection
        if (Network.isConnected(getContext())) {
            //fetch extra details about the movie by id

            ApiClient apiClient = new ApiClient().setIsDebug(ApplicationConstants.DEBUG);

            //reviews
            Call<ReviewResponse> reviewResponseCall = apiClient.movieInterface().getReviews(movie.getId());
            reviewResponseCall.enqueue(new Callback<ReviewResponse>() {
                @Override
                public void onResponse(Call<ReviewResponse> call, Response<ReviewResponse> response) {
                    reviewResponse = response.body();
                    jobsDone++;
                    notifyDataSetChanged();
                }

                @Override
                public void onFailure(Call<ReviewResponse> call, Throwable t) {
                    Log.e(TAG, "onFailure: ", t);
                }
            });

            //trailers
            Call<VideoResponse> videoResponseCall = apiClient.movieInterface().getVideos(movie.getId());
            videoResponseCall.enqueue(new Callback<VideoResponse>() {
                @Override
                public void onResponse(Call<VideoResponse> call, Response<VideoResponse> response) {
                    videoResponse = response.body();
                    jobsDone++;
                    notifyDataSetChanged();
                }

                @Override
                public void onFailure(Call<VideoResponse> call, Throwable t) {
                    Log.e(TAG, "onFailure: ", t);
                }
            });

            //credits
            Call<Credits> creditsCall = apiClient.movieInterface().getCredits(movie.getId());
            creditsCall.enqueue(new Callback<Credits>() {
                @Override
                public void onResponse(Call<Credits> call, Response<Credits> response) {
                    credits = response.body();
                    jobsDone++;
                    notifyDataSetChanged();
                }

                @Override
                public void onFailure(Call<Credits> call, Throwable t) {
                    Log.e(TAG, "onFailure: ", t);
                }
            });

            //extra details : such as duration, website,
            Call<MovieDetails> movieDetailsCall = apiClient.movieInterface().getMovieDetails(movie.getId());
            movieDetailsCall.enqueue(new Callback<MovieDetails>() {
                @Override
                public void onResponse(Call<MovieDetails> call, Response<MovieDetails> response) {
                    movieDetails = response.body();
                    jobsDone++;
                    notifyDataSetChanged();
                }

                @Override
                public void onFailure(Call<MovieDetails> call, Throwable t) {
                    Log.d(TAG, "onFailure: ", t);
                }
            });

        } else {
            //snackbar
        }
    }

    private void notifyDataSetChanged() {
        if (jobsDone == 4) {
            movieBundle.movie = movie;
            movieBundle.reviewResponse = reviewResponse;
            movieBundle.credits = credits;
            movieBundle.videoResponse = videoResponse;
            movieBundle.movieDetails = movieDetails;
            detailMovieAdapter.notifyDataSetChanged();
        }
    }
}
