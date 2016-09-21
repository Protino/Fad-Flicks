package com.calgen.prodek.fadflicks.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.calgen.prodek.fadflicks.R;
import com.calgen.prodek.fadflicks.adapter.ReviewAdapter;
import com.calgen.prodek.fadflicks.model.MovieBundle;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Gurupad on 11-Sep-16.
 * You asked me to change it for no reason.
 */
public class ReviewActivity extends AppCompatActivity {
    private static final String TAG = ReviewActivity.class.getSimpleName();
    //@formatter:off
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.all_reviews_recycler) RecyclerView recyclerView;
    //@formatter:on
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);
        ButterKnife.bind(this);

        MovieBundle movieBundle = (MovieBundle) getIntent().getSerializableExtra(Intent.EXTRA_TEXT);
        toolbar.setTitle(movieBundle.movie.getTitle());
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ReviewAdapter reviewAdapter = new ReviewAdapter(this, movieBundle.reviewResponse, false);

        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(reviewAdapter);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }
}
