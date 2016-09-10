package com.calgen.prodek.fadflicks.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.calgen.prodek.fadflicks.R;

/**
 * Created by Gurupad on 29-Aug-16.
 * You asked me to change it for no reason.
 */
public class FavouriteFragment extends Fragment {
    public FavouriteFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_fav, container, false);
    }
}
