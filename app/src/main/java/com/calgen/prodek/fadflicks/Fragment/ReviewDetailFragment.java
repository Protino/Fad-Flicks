package com.calgen.prodek.fadflicks.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.calgen.prodek.fadflicks.R;
import com.calgen.prodek.fadflicks.model.Review;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Gurupad Mamadapur on 10/30/2016.
 */

public class ReviewDetailFragment extends Fragment {
    //@formatter:off
    @BindView(R.id.review_author) TextView reviewAuthor;
    @BindView(R.id.review_text) TextView reviewText;
    //@formatter:on
    Review review;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        review = (Review) getArguments().getSerializable(Intent.EXTRA_TEXT);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.review_detail, container, false);
        ButterKnife.bind(this, rootView);
        reviewAuthor.setText(review.getAuthor());
        reviewText.setText(review.getContent());
        Linkify.addLinks(reviewText, Linkify.ALL);
        return rootView;
    }
}
