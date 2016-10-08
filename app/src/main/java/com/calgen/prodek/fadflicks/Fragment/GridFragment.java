package com.calgen.prodek.fadflicks.fragment;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.calgen.prodek.fadflicks.R;
import com.calgen.prodek.fadflicks.activity.MainActivity;
import com.calgen.prodek.fadflicks.adapter.GridMovieAdapter;
import com.calgen.prodek.fadflicks.api.ApiClient;
import com.calgen.prodek.fadflicks.model.Movie;
import com.calgen.prodek.fadflicks.model.MovieBundle;
import com.calgen.prodek.fadflicks.model.MovieResponse;
import com.calgen.prodek.fadflicks.utils.ApplicationConstants;
import com.calgen.prodek.fadflicks.utils.Cache;
import com.calgen.prodek.fadflicks.utils.Network;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import icepick.Icepick;
import icepick.State;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Gurupad on 28-Sep-16.
 */
public class GridFragment extends Fragment implements SearchView.OnQueryTextListener{

    private static final String TAG = GridFragment.class.getSimpleName();
    private static final int MIN_VOTE_COUNT = 100;
    public boolean firstLaunch = true;
    //@formatter:off
    @BindView(R.id.grid_recycler_view) public RecyclerView recyclerView;
    @BindView(R.id.try_again) public Button tryAgainButton;
    @BindView(R.id.internet_error_layout) public LinearLayout internetErrorLayout;
    @State public String sort_type;
    @State public ArrayList<Movie> movieList;
    @BindString(R.string.topRatedData) public String topRatedData;
    @BindString(R.string.popularData) public String popularData;
    @BindString(R.string.favouritesData) public String favouritesData;
    @State public String fragmentDataType;
    @BindView(R.id.empty_favourites_layout) LinearLayout emptyFavouritesLayout;
    private GridMovieAdapter movieAdapter;
    private Context context;
    //@formatter:on

    public GridFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        setHasOptionsMenu(true);
        Icepick.restoreInstanceState(this, savedInstanceState);
        fragmentDataType = getArguments().getString(getContext().getString(R.string.fragment_data_key), null);
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_main, menu);

        final MenuItem searchItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setQueryHint(context.getString(R.string.search_hint));
        searchView.setOnQueryTextListener(this);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Icepick.saveInstanceState(this, outState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_grid, container, false);

        ButterKnife.bind(this, rootView);
        context = getContext();
        if (savedInstanceState == null) {
            movieList = new ArrayList<>();
        }
        movieAdapter = new GridMovieAdapter(context, movieList);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(context, getSpanCount());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(movieAdapter);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (fragmentDataType.equals(popularData)) {
            sort_type = context.getString(R.string.sort_type_popular);
            updateMovieData();
        } else if (fragmentDataType.equals(topRatedData)) {
            sort_type = context.getString(R.string.sort_type_top_rated);
            updateMovieData();
        } else {
            fetchCacheData();
        }
    }

    private void fetchCacheData() {
        HashMap<Integer, Boolean> map = Cache.getFavouriteMovies(context);
        if (map == null)
            return;
        List<Integer> favouriteMovieIdList = new ArrayList<>();
        for (HashMap.Entry<Integer, Boolean> entry : map.entrySet()) {
            if (entry.getValue()) {
                favouriteMovieIdList.add(entry.getKey());
            }
        }
        List<MovieBundle> movieBundleList;
        movieList.clear();
        movieBundleList = Cache.bulkReadMovieData(context, favouriteMovieIdList);
        for (MovieBundle movieBundle : movieBundleList) {
            movieList.add(movieBundle.movie);
        }
        if (movieList.isEmpty())
            showEmptyFavouritesLayout();
        else
            hideEmptyFavouritesLayout();
        initializeFavourites();
        movieAdapter.notifyDataSetChanged();
    }

    private void showEmptyFavouritesLayout() {
        recyclerView.setVisibility(View.GONE);
        emptyFavouritesLayout.setVisibility(View.VISIBLE);
    }

    private void hideEmptyFavouritesLayout() {
        recyclerView.setVisibility(View.VISIBLE);
        emptyFavouritesLayout.setVisibility(View.GONE);
    }


    private void updateMovieData() {
        //Check for network connection beforehand
        if (Network.isConnected(context)) {
            fetchData();
        } else {
            showInternetErrorLayout();
        }
    }

    private void showInternetErrorLayout() {
        internetErrorLayout.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
    }

    @OnClick(R.id.try_again)
    public void onClick() {
        getActivity().recreate();
    }

    private void hideInternetErrorLayout() {
        internetErrorLayout.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
    }

    private void fetchData() {
        ApiClient apiClient = new ApiClient().setIsDebug(ApplicationConstants.DEBUG);
        Call<MovieResponse> call = apiClient.movieInterface().getMovies(sort_type, MIN_VOTE_COUNT);

        call.enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                hideInternetErrorLayout();
                movieList.clear();
                List<Movie> movies = response.body().getMovies();
                for (Movie movie : movies) {
                    movieList.add(movie);
                }
                initializeFavourites();
                movieAdapter.notifyDataSetChanged();
                if (MainActivity.twoPane && firstLaunch && fragmentDataType == popularData) {
                    clickFirstItem();
                    firstLaunch = false;
                }
            }

            @Override
            public void onFailure(Call<MovieResponse> call, Throwable t) {
                Log.e(TAG, "onFailure: ", t);
                showInternetErrorLayout();
            }
        });
    }

    private void clickFirstItem() {
        final int delay = 10;
        final int position = 0;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                GridMovieAdapter.MovieViewHolder viewHolder = (GridMovieAdapter.MovieViewHolder)
                        recyclerView.findViewHolderForAdapterPosition(position);
                viewHolder.onClick();
            }
        }, delay);
    }

    public void initializeFavourites() {
        HashMap<Integer, Boolean> map = Cache.getFavouriteMovies(context);
        if (map == null) {
            for (Movie movie : movieList) {
                movie.setFavourite(false);
            }
        } else {
            if (movieList != null) {
                for (Movie movie : movieList) {
                    if (map.containsKey(movie.getId())) {
                        movie.setFavourite(map.get(movie.getId()));
                    }
                }
            }
        }
    }

    public void notifyChange(Integer movieId, boolean isFavourite) {
        if (movieList != null) {
            if (fragmentDataType.equals(favouritesData)) {
                if (!isFavourite) {
                    for (Movie movie : movieList) {
                        if (movie.getId().equals(movieId)) {
                            movieList.remove(movie);
                            if (movieList.isEmpty())
                                showEmptyFavouritesLayout();
                            initializeFavourites();
                            movieAdapter.notifyDataSetChanged();
                            return;
                        }
                    }
                } else {
                    MovieBundle movieBundle = Cache.getMovieData(context, movieId);
                    if (movieBundle != null && !movieList.contains(movieBundle.movie)) {
                        movieList.add(movieBundle.movie);
                        initializeFavourites();
                        movieAdapter.notifyDataSetChanged();
                        hideEmptyFavouritesLayout();
                    }
                }

            } else {
                for (Movie movie : movieList) {
                    if (movie.getId().equals(movieId)) {
                        movie.setFavourite(isFavourite);
                        initializeFavourites();
                        movieAdapter.notifyDataSetChanged();
                        break;
                    }
                }
            }
        }
    }

    public int getSpanCount() {
        if (getActivity().getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
            return 3;
        else
            return 2;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        movieAdapter.getFilter().filter(newText);
        return false;
    }
}
