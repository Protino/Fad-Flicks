package com.calgen.prodek.fadflicks.Fragment;

import android.content.Intent;
import android.os.Bundle;
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
    public static byte jobsDone = 0;
    public MovieDetails movieDetails;
    public Movie movie;
    @BindView(R.id.detail_recycler_view)
    RecyclerView baseRecyclerView;
    private DetailMovieAdapter detailMovieAdapter;

    public MovieDetailFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);
        ButterKnife.bind(this, rootView);

        movie = (Movie) getActivity().getIntent().getSerializableExtra(Intent.EXTRA_TEXT);

        detailMovieAdapter = new DetailMovieAdapter(getContext(), movieDetails);
        fetchData();

        RecyclerView.LayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        baseRecyclerView.setLayoutManager(linearLayoutManager);
        baseRecyclerView.setItemAnimator(new DefaultItemAnimator());
        baseRecyclerView.setAdapter(detailMovieAdapter);
        return rootView;
    }

    private void fetchData() {
        //handle network connection
        if (Network.isConnected(getContext())) {
            //fetch extra details about the movie by id

            ApiClient apiClient = new ApiClient().setIsDebug(ApplicationConstants.DEBUG);

            //basic info from movie object
            movieDetails.movie = movie;

            //reviews
            Call<ReviewResponse> reviewResponseCall = apiClient.movieInterface().getReviews(movie.getId());
            reviewResponseCall.enqueue(new Callback<ReviewResponse>() {
                @Override
                public void onResponse(Call<ReviewResponse> call, Response<ReviewResponse> response) {
                    movieDetails.reviewResponse = response.body();
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
                    movieDetails.videoResponse = response.body();
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
                    movieDetails.credits = response.body();
                    jobsDone++;
                    notifyDataSetChanged();
                }

                @Override
                public void onFailure(Call<Credits> call, Throwable t) {
                    Log.e(TAG, "onFailure: ", t);
                }
            });

        } else {
            //snackbar
        }
    }

    private void notifyDataSetChanged() {
        if (jobsDone == 3) {
            detailMovieAdapter.notifyDataSetChanged();
        }
    }
}
