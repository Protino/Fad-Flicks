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

package com.calgen.prodek.fadflicks.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.calgen.prodek.fadflicks.R;
import com.calgen.prodek.fadflicks.fragment.ReviewDetailFragment;
import com.calgen.prodek.fadflicks.model.Review;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Gurupad Mamadapur on 10/31/2016.
 */
public class ReviewDetailActivity extends AppCompatActivity {

    //@formatter:off
    @BindView(R.id.toolbar) public Toolbar toolbar;
    //@formatter:on

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_detail);
        ButterKnife.bind(this);

        Review review = (Review) getIntent().getSerializableExtra(Intent.EXTRA_TEXT);
        toolbar.setTitle(getIntent().getStringExtra(getString(R.string.movie_title_key)));
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ReviewDetailFragment reviewDetailFragment = new ReviewDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(Intent.EXTRA_TEXT, review);
        reviewDetailFragment.setArguments(bundle);

        getSupportFragmentManager().beginTransaction()
                .add(R.id.review_detail_container, reviewDetailFragment)
                .commit();
    }

    @Override
    protected void onPause() {
        super.onPause();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }
}
