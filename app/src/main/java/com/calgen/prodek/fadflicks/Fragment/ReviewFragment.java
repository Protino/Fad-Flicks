/*
 *    Copyright 2016 Gurupad Mamadapur
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

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
import com.calgen.prodek.fadflicks.activity.MainActivity;
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
 * Placeholder fragment to display list of reviews of a movie.
 */

public class ReviewFragment extends Fragment {

    //@formatter:off
    @BindView(R.id.listview_review) public ListView reviewListView;
    @State public int selectedItemPosition;
    //@formatter:on

    public List<Review> reviewList;
    public ReviewAdapter reviewAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        if (savedInstanceState == null) selectedItemPosition = 0;
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
        if (savedInstanceState != null) {
            reviewListView.smoothScrollToPosition(selectedItemPosition);
        } else {
            if (MainActivity.twoPane) {
                reviewListView.performItemClick(
                        reviewListView.getAdapter().getView(selectedItemPosition, null, null),
                        selectedItemPosition,
                        selectedItemPosition
                );
            }
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
