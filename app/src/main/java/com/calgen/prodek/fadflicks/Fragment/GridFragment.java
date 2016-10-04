package com.calgen.prodek.fadflicks.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.calgen.prodek.fadflicks.R;
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
import icepick.Icepick;
import icepick.State;
import mehdi.sakout.dynamicbox.DynamicBox;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Gurupad on 28-Sep-16.
 */
public class GridFragment extends Fragment {

    private static final String TAG = GridFragment.class.getSimpleName();
    private static final int MIN_VOTE_COUNT = 1000;
    //@formatter:off
    @BindView(R.id.recycler_view) public RecyclerView recyclerView;
    @State public String sort_type;
    @State public ArrayList<Movie> movieList;
    @BindString(R.string.topRatedData) public String topRatedData;
    @BindString(R.string.popularData) public String popularData;
    @BindString(R.string.favouritesData) public String favouritesData;

    @State public String fragmentDataType;

    private GridMovieAdapter movieAdapter;
    private Context context;
    private DynamicBox dynamicBox;
    //@formatter:on

    public GridFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Icepick.restoreInstanceState(this, savedInstanceState);
        fragmentDataType = getArguments().getString(getContext().getString(R.string.fragment_data_key), null);
        Log.d(TAG, "onCreate: " + fragmentDataType);
        setRetainInstance(true);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Icepick.saveInstanceState(this, outState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_popular, container, false);

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
        dynamicBox = new DynamicBox(context, recyclerView);
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
        if (map == null) // TODO: 28-Sep-16  Inflate a drawable that displays empty favourites or something
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
        initializeFavourites();
        movieAdapter.notifyDataSetChanged();
    }

    private void updateMovieData() {
        //Check for network connection beforehand
        if (Network.isConnected(context)) {
            fetchData();
        } else {
        }
    }

    private void fetchData() {
        ApiClient apiClient = new ApiClient().setIsDebug(ApplicationConstants.DEBUG);
        Call<MovieResponse> call = apiClient.movieInterface().getMovies(sort_type, MIN_VOTE_COUNT);

        call.enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                movieList.clear();
                List<Movie> movies = response.body().getMovies();
                for (Movie movie : movies) {
                    movieList.add(movie);
                }
                initializeFavourites();
                movieAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<MovieResponse> call, Throwable t) {
                Log.e(TAG, "onFailure: ", t);
            }
        });
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
        return (context.getResources().getBoolean(R.bool.large_layout)) ? 3 : 2;
    }
}
