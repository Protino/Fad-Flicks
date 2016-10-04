package com.calgen.prodek.fadflicks.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
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
import android.widget.ImageView;

import com.calgen.prodek.fadflicks.R;
import com.calgen.prodek.fadflicks.activity.DetailActivity;
import com.calgen.prodek.fadflicks.activity.MainActivity;
import com.calgen.prodek.fadflicks.adapter.DetailMovieAdapter;
import com.calgen.prodek.fadflicks.api.ApiClient;
import com.calgen.prodek.fadflicks.model.Cast;
import com.calgen.prodek.fadflicks.model.Credits;
import com.calgen.prodek.fadflicks.model.Movie;
import com.calgen.prodek.fadflicks.model.MovieBundle;
import com.calgen.prodek.fadflicks.model.MovieDetails;
import com.calgen.prodek.fadflicks.model.ReviewResponse;
import com.calgen.prodek.fadflicks.model.VideoResponse;
import com.calgen.prodek.fadflicks.utils.ApplicationConstants;
import com.calgen.prodek.fadflicks.utils.Cache;
import com.calgen.prodek.fadflicks.utils.Network;
import com.calgen.prodek.fadflicks.utils.Parser;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.squareup.picasso.Picasso;

import butterknife.BindDrawable;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A placeholder fragment containing a simple view.
 */
public class MovieDetailFragment extends Fragment {

    //@formatter:off
    private static final String TAG = DetailActivity.class.getSimpleName();
    public byte jobsDone = 0;
    public MovieBundle movieBundle;
    public Movie movie;
    public ReviewResponse reviewResponse;
    public VideoResponse videoResponse;
    public Credits credits;
    public MovieDetails movieDetails;
    @BindView(R.id.detail_recycler_view) RecyclerView baseRecyclerView;
    @BindView(R.id.image_backdrop) ImageView backDropImage;
    @BindView(R.id.share_fab) FloatingActionButton shareFab;
    @BindView(R.id.fav_fab) FloatingActionButton favFab;
    @BindView(R.id.fab_menu) FloatingActionMenu fabMenu;
    @BindDrawable(R.drawable.ic_favorite_border_white_24dp) Drawable notFavouriteDrawable;
    @BindDrawable(R.drawable.ic_favorite_white_24dp) Drawable favouriteDrawable;
    private DetailMovieAdapter detailMovieAdapter;
    private Context context;
    private boolean isFavourite;
    private String shareMessage;
    //@formatter:on

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        jobsDone = 0;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);
        ButterKnife.bind(this, rootView);
        Bundle arguments = getArguments();
        context = getContext();

        if (arguments != null) {
            movie = (Movie) arguments.getSerializable(Intent.EXTRA_TEXT);

            Picasso.with(context)
                    .load(Parser.formatImageUrl(movie.backdropPath, context.getString(R.string.image_size_large)))
                    .into(backDropImage);

            isFavourite = movie.isFavourite;
            setFavButtonDrawable();
        }

        movieBundle = new MovieBundle();

        detailMovieAdapter = new DetailMovieAdapter(context, movieBundle);
        RecyclerView.LayoutManager linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        baseRecyclerView.setLayoutManager(linearLayoutManager);
        baseRecyclerView.setNestedScrollingEnabled(false);
        baseRecyclerView.setItemAnimator(new DefaultItemAnimator());
        baseRecyclerView.setAdapter(detailMovieAdapter);
        return rootView;
    }

    private void setFavButtonDrawable() {
        favFab.setImageDrawable((isFavourite) ? favouriteDrawable : notFavouriteDrawable);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (savedInstanceState == null && movie != null) {
            fetchData();
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    private void fetchData() {
        //handle network connection
        if (Network.isConnected(context)) {
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

    @OnClick({R.id.fav_fab, R.id.share_fab})
    public void onFabClick(View view) {
        switch (view.getId()) {
            case R.id.fav_fab:
                isFavourite = !isFavourite;
                notifyFavouriteChange();
                setFavButtonDrawable();
                break;
            case R.id.share_fab:
                if (shareMessage != null) {
                    Intent sendIntent = new Intent();
                    sendIntent.setAction(Intent.ACTION_SEND);
                    sendIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                    sendIntent.setType("text/plain");
                    context.startActivity(sendIntent);
                }
                break;
        }
        fabMenu.close(true);
    }

    private void notifyDataSetChanged() {
        if (jobsDone == 4) {
            movieBundle.movie = movie;
            movieBundle.reviewResponse = reviewResponse;
            movieBundle.credits = credits;
            movieBundle.videoResponse = videoResponse;
            movieBundle.movieDetails = movieDetails;
            detailMovieAdapter.notifyDataSetChanged();
            setShareMessage();
            Cache.cacheMovieData(context, movieBundle);
        }
    }

    private void setShareMessage() {
        /*
        Hey, "movie_name" is awesome.
        It has actor1 actor2 and director is directing.
        We need to check this out.


        If (release > cur_data diff 4 months)
         releasing on date.

         */
        Cast cast1 = movieBundle.credits.getCast().get(0);
        Cast cast2 = movieBundle.credits.getCast().get(1);

        // TODO: 05-Oct-16 More friendly release data message
        String releaseMessage = "Release date :" + movie.getReleaseDate();

        shareMessage = "Hey,"
                + movie.getTitle()
                + "is awesome.It has actors like "
                + cast1.getName() + ","
                + cast2.getName() + "..."
                + releaseMessage;
    }

    private void notifyFavouriteChange() {
        Cache.setFavouriteMovie(context, movie.getId(), isFavourite);
        ((MainActivity) getActivity()).notifyFavouriteChange(movie.getId(), isFavourite);
    }
}
