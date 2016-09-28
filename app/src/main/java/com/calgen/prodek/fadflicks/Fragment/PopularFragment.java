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
import com.calgen.prodek.fadflicks.model.MovieResponse;
import com.calgen.prodek.fadflicks.utils.Cache;
import com.calgen.prodek.fadflicks.utils.Network;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import icepick.Icepick;
import icepick.State;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A placeholder fragment containing a simple view.
 */
public class PopularFragment extends Fragment {

    private static final String TAG = PopularFragment.class.getSimpleName();
    private static final int MIN_VOTE_COUNT = 1000;
    //@formatter:off
    @BindView(R.id.recycler_view) public RecyclerView recyclerView;
    @State public String sort_type;
    @State public ArrayList<Movie> movieList;
    private GridMovieAdapter movieAdapter;
    private Context context;
    //@formatter:on

    public PopularFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Icepick.restoreInstanceState(this, savedInstanceState);
        if (savedInstanceState == null) {
            sort_type = getString(R.string.sort_type_popular);
        }
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
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(context, 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(movieAdapter);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (savedInstanceState == null) updateMovieData();

    }

    private void updateMovieData() {
        //Check for network connection beforehand
        if (Network.isConnected(context)) {
            fetchData();
        } else {
        }
    }

    private void fetchData() {
        ApiClient apiClient = new ApiClient().setIsDebug(false);
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

    public void notifyChange() {
        initializeFavourites();
        movieAdapter.notifyDataSetChanged();
    }
}
