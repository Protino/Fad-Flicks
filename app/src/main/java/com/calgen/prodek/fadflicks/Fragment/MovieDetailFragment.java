package com.calgen.prodek.fadflicks.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.calgen.prodek.fadflicks.R;
import com.calgen.prodek.fadflicks.activity.MainActivity;
import com.calgen.prodek.fadflicks.activity.ReviewActivity;
import com.calgen.prodek.fadflicks.adapter.CreditsAdapter;
import com.calgen.prodek.fadflicks.adapter.ReviewAdapter;
import com.calgen.prodek.fadflicks.adapter.VideosAdapter;
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
import com.google.android.youtube.player.YouTubeThumbnailLoader;
import com.iarcuschin.simpleratingbar.SimpleRatingBar;
import com.squareup.picasso.Picasso;

import butterknife.BindDrawable;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Optional;
import icepick.Icepick;
import icepick.State;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A placeholder fragment containing a simple view.
 */
public class MovieDetailFragment extends Fragment {

    //@formatter:off
    private static final String TAG = MovieDetailFragment.class.getSimpleName();
    @State public byte jobsDone = 0;
    @State public MovieBundle movieBundle;
    @State public Movie movie;
    @State public boolean isFavourite;
    @State public String shareMessage;

    @BindView(R.id.title) TextView title;
    @BindView(R.id.release_date) TextView releaseDate;
    @BindView(R.id.runtime) TextView runtime;
    @BindView(R.id.movie_rating) SimpleRatingBar movieRating;
    @BindView(R.id.plot_text) TextView plot;
    @BindView(R.id.read_more_details) Button moreDetails;
    @BindView(R.id.cast_recycler) RecyclerView cardCast;
    @BindView(R.id.trailer_recycler) RecyclerView cardVideo;
    @BindView(R.id.review_recycler) RecyclerView cardReview;
    @BindView(R.id.all_reviews) Button allReviews;
    @BindView(R.id.no_review_msg) TextView noReviewsMessage;
    @Nullable @BindView(R.id.image_backdrop) ImageView backDropImage;
    @Nullable @BindView(R.id.share_fab) FloatingActionButton shareFab;
    @Nullable @BindView(R.id.fav_fab) FloatingActionButton favFab;
    @Nullable @BindView(R.id.fab_menu) FloatingActionMenu fabMenu;
    @Nullable @BindView(R.id.poster) ImageView poster;
    @Nullable @BindView(R.id.tagline) TextView tagLine;
    @Nullable @BindView(R.id.language) TextView language;

    @BindView(R.id.progressBarLayout) LinearLayout progressBarLayout;
    @BindView(R.id.content_detail_wide) FrameLayout detailContentLayout;

    @BindDrawable(R.drawable.ic_favorite_border_white_24dp) Drawable notFavouriteDrawable;
    @BindDrawable(R.drawable.ic_favorite_white_24dp) Drawable favouriteDrawable;
    private Context context;
    private Bundle arguments;
    //@formatter:on

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        jobsDone = 0;
        Icepick.restoreInstanceState(this, savedInstanceState);
        setRetainInstance(true);
        if (!MainActivity.twoPane)
            setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_detail, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_share) {
            sendShareIntent();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Icepick.saveInstanceState(this, outState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);
        ButterKnife.bind(this, rootView);
        arguments = getArguments();
        context = getContext();

        if (savedInstanceState == null)
            movieBundle = new MovieBundle();

        if (arguments != null) {
            movie = (Movie) arguments.getSerializable(Intent.EXTRA_TEXT);
            if (MainActivity.twoPane) {
                Picasso.with(context)
                        .load(Parser.formatImageUrl(movie.backdropPath, context.getString(R.string.image_size_large)))
                        .into(backDropImage);

                isFavourite = movie.isFavourite;
                setFavButtonDrawable();
            }
            movieBundle.movie = movie;
        }
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (savedInstanceState == null && movie != null) {
            fetchData();
        } else if (jobsDone == 4) {
            bindViews();
            hideLoadingLayout();
        }
    }

    private void bindViews() {
        title.setText(movie.getTitle());
        releaseDate.setText(movie.getReleaseDate());

        // TODO: 09-Sep-16 Make use of string formats, like it was done in sunshine
        runtime.setText(movieBundle.movieDetails.getRuntime() + " " + context.getString(R.string.minutes));

        if (!MainActivity.twoPane) {
            Picasso.with(context).load(Parser.formatImageUrl(movie.getPosterPath(), context.getString(R.string.image_size_small)))
                    .into(poster);
        } else {
            language.setText(movie.getOriginalLanguage());
            tagLine.setText(movieBundle.movieDetails.getTagline());
            if (movieBundle.movieDetails.getTagline().isEmpty()){
                tagLine.setHeight(0);
            }
        }
        movieRating.setRating((float) (movie.getVoteAverage() / 2));

        plot.setText(movie.getOverview());

        // Now initialize recycler views and assign them new adapters
        CreditsAdapter creditsAdapter = new CreditsAdapter(context, movieBundle.credits);
        cardCast.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        cardCast.setHasFixedSize(true);
        cardCast.setNestedScrollingEnabled(false);
        cardCast.setAdapter(creditsAdapter);

        VideosAdapter videosAdapter = new VideosAdapter(context, movieBundle.videoResponse);
        cardVideo.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        cardVideo.setHasFixedSize(true);
        cardVideo.setNestedScrollingEnabled(false);
        cardVideo.setAdapter(videosAdapter);

        int reviewCount = movieBundle.reviewResponse.getTotalResults();
        if (reviewCount == 0) {
            allReviews.setVisibility(View.GONE);
            cardReview.setVisibility(View.GONE);
            noReviewsMessage.setVisibility(View.VISIBLE);
        } else {
            ReviewAdapter reviewAdapter = new ReviewAdapter(context, movieBundle.reviewResponse, true);
            cardReview.setLayoutManager(new LinearLayoutManager(context));
            cardReview.setNestedScrollingEnabled(false);
            cardReview.setAdapter(reviewAdapter);
        }
    }

    private void fetchData() {
        showLoadingLayout();
        //handle network connection
        if (Network.isConnected(context)) {
            //fetch extra details about the movie by id
            ApiClient apiClient = new ApiClient().setIsDebug(ApplicationConstants.DEBUG);

            //reviews
            Call<ReviewResponse> reviewResponseCall = apiClient.movieInterface().getReviews(movie.getId());
            reviewResponseCall.enqueue(new Callback<ReviewResponse>() {
                @Override
                public void onResponse(Call<ReviewResponse> call, Response<ReviewResponse> response) {
                    movieBundle.reviewResponse = response.body();
                    jobsDone++;
                    notifyDataSetChanged();
                }

                @Override
                public void onFailure(Call<ReviewResponse> call, Throwable t) {
                    Log.e(TAG, "onFailure: ReviewResponseCall", t);
                    progressBarLayout.setVisibility(View.GONE);
                }
            });

            //trailers
            Call<VideoResponse> videoResponseCall = apiClient.movieInterface().getVideos(movie.getId());
            videoResponseCall.enqueue(new Callback<VideoResponse>() {
                @Override
                public void onResponse(Call<VideoResponse> call, Response<VideoResponse> response) {
                    movieBundle.videoResponse = response.body();
                    jobsDone++;
                    notifyDataSetChanged();
                }

                @Override
                public void onFailure(Call<VideoResponse> call, Throwable t) {
                    Log.e(TAG, "onFailure: videoResponseCall", t);
                    progressBarLayout.setVisibility(View.GONE);
                }
            });

            //credits
            Call<Credits> creditsCall = apiClient.movieInterface().getCredits(movie.getId());
            creditsCall.enqueue(new Callback<Credits>() {
                @Override
                public void onResponse(Call<Credits> call, Response<Credits> response) {
                    movieBundle.credits = response.body();
                    jobsDone++;
                    notifyDataSetChanged();
                }

                @Override
                public void onFailure(Call<Credits> call, Throwable t) {
                    Log.e(TAG, "onFailure credits call: ", t);
                    progressBarLayout.setVisibility(View.GONE);
                }
            });

            //extra details : such as duration, website,
            Call<MovieDetails> movieDetailsCall = apiClient.movieInterface().getMovieDetails(movie.getId());
            movieDetailsCall.enqueue(new Callback<MovieDetails>() {
                @Override
                public void onResponse(Call<MovieDetails> call, Response<MovieDetails> response) {
                    MovieDetails movieDetails;
                    movieDetails = response.body();
                    movieBundle.movieDetails = movieDetails;
                    jobsDone++;
                    notifyDataSetChanged();
                }

                @Override
                public void onFailure(Call<MovieDetails> call, Throwable t) {
                    Log.d(TAG, "onFailure: movieDetails call", t);
                    progressBarLayout.setVisibility(View.GONE);
                }
            });

        } else {
            //check if data is present in the cache to load
            MovieBundle cacheData = Cache.getMovieData(context, movie.getId());

            if (cacheData != null) {
                movieBundle.movie = cacheData.movie;
                movieBundle.reviewResponse = cacheData.reviewResponse;
                movieBundle.credits = cacheData.credits;
                movieBundle.videoResponse = cacheData.videoResponse;
                movieBundle.movieDetails = cacheData.movieDetails;
                bindViews();
                hideLoadingLayout();
                setShareMessage();
            } else {
                Snackbar snackbar = Snackbar.make(detailContentLayout,
                        getString(R.string.internet_error_message),
                        Snackbar.LENGTH_INDEFINITE);
                snackbar.setAction(getString(R.string.try_again), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        fetchData();
                    }
                });
                snackbar.setActionTextColor(context.getResources().getColor(R.color.error));
                snackbar.show();
            }
        }
    }

    private void sendShareIntent() {
        if (shareMessage != null) {
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
            sendIntent.setType("text/plain");
            context.startActivity(sendIntent);
        }
    }

    private void notifyDataSetChanged() {
        if (jobsDone == 4) {
            bindViews();
            hideLoadingLayout();
            setShareMessage();
            Cache.cacheMovieData(context, movieBundle);
        }
    }

    private void setShareMessage() {
        Cast cast1 = movieBundle.credits.getCast().get(0);
        Cast cast2 = movieBundle.credits.getCast().get(1);

        // TODO: 05-Oct-16 More friendly release data message
        String releaseMessage = "Release date :" + movie.getReleaseDate();

        shareMessage = "Hey, "
                + movie.getTitle()
                + " is awesome.It has actors like "
                + cast1.getName() + ","
                + cast2.getName() + "..."
                + releaseMessage;
    }

    // TODO: 05-Oct-16 replace with event bus
    private void notifyFavouriteChange() {
        Cache.setFavouriteMovie(context, movie.getId(), isFavourite);
        ((MainActivity) getActivity()).notifyFavouriteChange(movie.getId(), isFavourite);
    }

    //Only in sw600dp
    @Optional
    @OnClick({R.id.fav_fab, R.id.share_fab})
    public void onFabClick(View view) {
        switch (view.getId()) {
            case R.id.fav_fab:
                Log.d(TAG, "onFabClick: fav fab clicked");
                isFavourite = !isFavourite;
                notifyFavouriteChange();
                setFavButtonDrawable();
                break;
            case R.id.share_fab:
                sendShareIntent();
                break;
        }
        fabMenu.close(true);
    }

    @OnClick({R.id.read_more_details, R.id.all_reviews, R.id.review_recycler})
    public void OnClick(View v) {
        switch (v.getId()) {
            case R.id.read_more_details:
                showDialog();
                break;
            case R.id.all_reviews:
            case R.id.review_recycler:
                Intent intent = new Intent(context, ReviewActivity.class);
                intent.putExtra(Intent.EXTRA_TEXT, movieBundle);
                context.startActivity(intent);
                break;
        }
    }

    private void showDialog() {
        FragmentManager fragmentManager = ((FragmentActivity) context).getSupportFragmentManager();
        ReadMoreDialog newFragment = new ReadMoreDialog();
        Bundle bundle = new Bundle();
        bundle.putSerializable(Intent.EXTRA_TEXT, movieBundle);
        newFragment.setArguments(bundle);

        if (context.getResources().getBoolean(R.bool.large_layout)) {
            newFragment.show(fragmentManager, ReadMoreDialog.TAG);
        } else {
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            transaction.add(android.R.id.content, newFragment)
                    .addToBackStack(null).commit();
        }
    }

    private void showLoadingLayout() {
        progressBarLayout.setVisibility(View.VISIBLE);
        detailContentLayout.setVisibility(View.GONE);
    }

    private void hideLoadingLayout() {
        progressBarLayout.setVisibility(View.GONE);
        detailContentLayout.setVisibility(View.VISIBLE);
    }

    private void setFavButtonDrawable() {
        favFab.setImageDrawable((isFavourite) ? favouriteDrawable : notFavouriteDrawable);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (VideosAdapter.viewLoaderMap == null)
            return;
        for (YouTubeThumbnailLoader loader : VideosAdapter.viewLoaderMap.values()) {
            loader.release();
        }
    }
}
