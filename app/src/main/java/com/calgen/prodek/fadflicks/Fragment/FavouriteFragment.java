package com.calgen.prodek.fadflicks.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.calgen.prodek.fadflicks.R;
import com.calgen.prodek.fadflicks.adapter.GridMovieAdapter;
import com.calgen.prodek.fadflicks.model.Movie;
import com.calgen.prodek.fadflicks.model.MovieBundle;
import com.calgen.prodek.fadflicks.utils.Cache;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import icepick.Icepick;
import icepick.State;

/**
 * Created by Gurupad on 29-Aug-16.
 * You asked me to change it for no reason.
 */
public class FavouriteFragment extends Fragment {

    private static final String TAG = PopularFragment.class.getSimpleName();
    private static final int MIN_VOTE_COUNT = 1000;
    //@formatter:off
    @BindView(R.id.recycler_view) public RecyclerView recyclerView;
    @State public ArrayList<Movie> movieList;
    private GridMovieAdapter adapter;
    private Context context;

    public FavouriteFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Icepick.restoreInstanceState(this, savedInstanceState);
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
        View rootView = inflater.inflate(R.layout.fragment_fav, container, false);
        context = getContext();

        ButterKnife.bind(this, rootView);

        if (savedInstanceState == null) {
            movieList = new ArrayList<>();
        }
        adapter = new GridMovieAdapter(context, movieList);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(context, 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        fetchData();
    }

    private void fetchData() {
        // get favourite movies HashMap from sharedPrefs
        HashMap<Integer,Boolean> map = Cache.getFavouriteMovies(context);
        List<Integer> favouriteMovieIdList = new ArrayList<>();
        for(HashMap.Entry<Integer,Boolean> entry: map.entrySet()){
            if(entry.getValue()){
                favouriteMovieIdList.add(entry.getKey());
            }
        }
        List<MovieBundle> movieBundleList = new ArrayList<>();
        movieBundleList = Cache.bulkReadMovieData(context,favouriteMovieIdList);
        for(MovieBundle movieBundle:movieBundleList){
            movieList.add(movieBundle.movie);
        }
        adapter.notifyDataSetChanged();
    }
}
