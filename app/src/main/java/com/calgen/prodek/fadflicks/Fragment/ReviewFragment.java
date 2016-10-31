package com.calgen.prodek.fadflicks.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.calgen.prodek.fadflicks.R;
import com.calgen.prodek.fadflicks.adapter.ReviewAdapter;
import com.calgen.prodek.fadflicks.model.Review;
import com.calgen.prodek.fadflicks.model.ReviewResponse;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnItemClick;
import icepick.Icepick;
import icepick.State;

/**
 * Created by Gurupad Mamadapur on 10/31/2016.
 */

public class ReviewFragment extends Fragment {

    //@formatter:off
    @BindView(R.id.listview_review) ListView reviewListView;
    @State int selectedItemPosition;
    //@formatter:on

    List<Review> reviewList;
    ReviewAdapter reviewAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        if (savedInstanceState == null) selectedItemPosition = -1;
        ReviewResponse reviewResponse = (ReviewResponse) getActivity().getIntent().getSerializableExtra(Intent.EXTRA_TEXT);
        reviewList = reviewResponse.getReviewResponses();
        Icepick.restoreInstanceState(this, savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Icepick.restoreInstanceState(this, outState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_review, container, false);
        ButterKnife.bind(this, rootView);
        reviewAdapter = new ReviewAdapter(getContext(), (ArrayList<Review>) reviewList, false);
        reviewListView.setAdapter(reviewAdapter);
        if (savedInstanceState != null && selectedItemPosition != -1) {
            reviewListView.smoothScrollToPosition(selectedItemPosition);
        }
        return rootView;
    }

    @OnItemClick(R.id.listview_review)
    public void onItemClick(int position) {
        selectedItemPosition = position;
        ((Callback) getActivity()).onItemSelected(reviewList.get(position));
    }

    public interface Callback {
        void onItemSelected(Review review);
    }
}
