package com.calgen.prodek.fadflicks.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
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
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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
import com.calgen.prodek.fadflicks.model.Review;
import com.calgen.prodek.fadflicks.model.ReviewResponse;
import com.calgen.prodek.fadflicks.model.VideoResponse;
import com.calgen.prodek.fadflicks.utils.ApplicationConstants;
import com.calgen.prodek.fadflicks.utils.Cache;
import com.calgen.prodek.fadflicks.utils.Network;
import com.calgen.prodek.fadflicks.utils.Parser;
import com.calgen.prodek.fadflicks.utils.UI;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.google.android.youtube.player.YouTubeThumbnailLoader;
import com.iarcuschin.simpleratingbar.SimpleRatingBar;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

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

public class MovieDetailFragment extends Fragment implements VideosAdapter.Callback {

    //@formatter:off
    private static final String TAG = MovieDetailFragment.class.getSimpleName();
    @State public byte jobsDone = 0;
    @State public MovieBundle movieBundle;
    @State public Movie movie;
    @State public boolean isFavourite;
    @State public String shareMessage;

    @BindView(R.id.title) public TextView title;
    @BindView(R.id.release_date) public TextView releaseDate;
    @BindView(R.id.runtime) public TextView runtime;
    @BindView(R.id.movie_rating) public SimpleRatingBar movieRating;
    @BindView(R.id.plot_text) public TextView plot;
    @BindView(R.id.read_more_details) public Button moreDetails;
    @BindView(R.id.cast_recycler) public RecyclerView cardCast;
    @BindView(R.id.trailer_recycler) public RecyclerView cardVideo;
    @BindView(R.id.review_listView) public ListView reviewListView;
    @BindView(R.id.all_reviews_button) public Button allReviews;
    @BindView(R.id.no_review_msg) public TextView noReviewsMessage;
    @Nullable @BindView(R.id.image_backdrop) public ImageView backDropImage;
    @Nullable @BindView(R.id.share_fab) public FloatingActionButton shareFab;
    @Nullable @BindView(R.id.fav_fab) public FloatingActionButton favFab;
    @Nullable @BindView(R.id.fab_menu) public FloatingActionMenu fabMenu;
    @Nullable @BindView(R.id.poster) public ImageView poster;
    @Nullable @BindView(R.id.tagline) public TextView tagLine;
    @Nullable @BindView(R.id.language) public TextView language;

    @BindView(R.id.progressBarLayout) public LinearLayout progressBarLayout;
    @BindView(R.id.content_detail_wide) public FrameLayout detailContentLayout;

    @BindDrawable(R.drawable.ic_favorite_border_white_24dp) public Drawable notFavouriteDrawable;
    @BindDrawable(R.drawable.ic_favorite_white_24dp) public Drawable favouriteDrawable;
    private Context context;
    //@formatter:on

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        if (savedInstanceState == null) jobsDone = 0;
        Icepick.restoreInstanceState(this, savedInstanceState);
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
        Bundle arguments = getArguments();
        context = getActivity();

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
        if (movie == null) return;
        if (savedInstanceState == null) {
            fetchData();
        } else if (movieBundle != null) {
            bindViews();
            hideLoadingLayout();
        }
    }

    private void bindViews() {
        title.setText(movie.getTitle());
        releaseDate.setText(Parser.formatReleaseDate(movie.getReleaseDate()));

        runtime.setText(String.format(getString(R.string.runtime),movieBundle.movieDetails.getRuntime()));

        if (!MainActivity.twoPane) {
            Picasso.with(context).load(Parser.formatImageUrl(movie.getPosterPath(), context.getString(R.string.image_size_small)))
                    .into(poster);
        } else {
            language.setText(movie.getOriginalLanguage());
            tagLine.setText(movieBundle.movieDetails.getTagline());
            if (movieBundle.movieDetails.getTagline().isEmpty()) {
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

        VideosAdapter videosAdapter = new VideosAdapter(context, movieBundle.videoResponse,this);
        cardVideo.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        cardVideo.setHasFixedSize(true);
        cardVideo.setNestedScrollingEnabled(false);
        cardVideo.setAdapter(videosAdapter);

        int reviewCount = movieBundle.reviewResponse.getTotalResults();
        if (reviewCount == 0) {
            allReviews.setVisibility(View.GONE);
            reviewListView.setVisibility(View.GONE);
            noReviewsMessage.setVisibility(View.VISIBLE);
        } else {
            ReviewAdapter reviewAdapter = new ReviewAdapter(context, (ArrayList<Review>) movieBundle.reviewResponse.getReviewResponses(), true);
            reviewListView.setAdapter(reviewAdapter);
            reviewListView.setSelector(new ColorDrawable(Color.TRANSPARENT));
            reviewListView.setCacheColorHint(Color.TRANSPARENT);
            UI.setListViewHeightBasedOnChildren(reviewListView);
        }
    }

    private void fetchData() {
        showLoadingLayout();
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
            return;
        }
        //handle network connection
        if (Network.isConnected(context)) {
            fetchDataFromInternet();
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

    private void fetchDataFromInternet() {
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
                progressBarLayout.setVisibility(View.GONE);
            }
        });
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
        String website = movieBundle.movieDetails.getHomepage();

        String greetingMessage = String.format(context.getString(R.string.share_welcome_message),movie.getTitle());
        String movieInfo = String.format(context.getString(R.string.share_movie_info),
                movie.getReleaseDate(),cast1.getName(),cast2.getName(),website);

        shareMessage = greetingMessage+movieInfo;
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
                isFavourite = !isFavourite;
                notifyFavouriteChange();
                setFavButtonDrawable();
                break;
            case R.id.share_fab:
                sendShareIntent();
                break;
            default:
                break;
        }
        fabMenu.close(true);
    }

    @OnClick({R.id.read_more_details, R.id.all_reviews_button})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.read_more_details:
                showDialog();
                break;
            case R.id.all_reviews_button:
                Intent intent = new Intent(context, ReviewActivity.class);
                intent.putExtra(Intent.EXTRA_TEXT, movieBundle.reviewResponse);
                context.startActivity(intent);
                break;
            default:
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

    @Override
    public void createAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(getString(R.string.youtube_error_dialog_message))
                .setTitle(getString(R.string.youtube_error_dialog_title))
                .setIcon(getResources().getDrawable(R.drawable.ic_warning_black_24dp))
                .setPositiveButton(getString(R.string.continue_installation), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        try {
                            Intent intent = new Intent(Intent.ACTION_VIEW);
                            intent.setData(Uri.parse(getString(R.string.youtube_package_id)));
                            startActivity(intent);
                        } catch (Exception e) {
                            Toast.makeText(context, R.string.play_store_app_error_message, Toast.LENGTH_LONG).show();
                        }
                    }
                })
                .setNegativeButton("Cancel",new DialogInterface.OnClickListener(){

                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .create().show();
    }
}
