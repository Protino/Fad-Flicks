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
import android.content.res.Configuration;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;

import com.calgen.prodek.fadflicks.R;
import com.calgen.prodek.fadflicks.adapter.VideosAdapter;
import com.calgen.prodek.fadflicks.fragment.MovieDetailFragment;
import com.calgen.prodek.fadflicks.model.Movie;
import com.calgen.prodek.fadflicks.utils.Cache;
import com.calgen.prodek.fadflicks.utils.Parser;
import com.calgen.prodek.fadflicks.utils.UI;
import com.google.android.youtube.player.YouTubeThumbnailLoader;
import com.squareup.picasso.Picasso;

import butterknife.BindDrawable;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import icepick.Icepick;
import icepick.State;

/**
 * Handles setup of detailFragment if not in two-pane UI mode.
 */
public class DetailActivity extends AppCompatActivity {

    //re-enable usage of vectors for devices(API 19 and lower)
    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    //@formatter:off
    @BindView(R.id.fav_fab) public FloatingActionButton favFab;
    @BindView(R.id.toolbar) public Toolbar toolbar;
    @BindView(R.id.image_backdrop) public ImageView backdropImage;
    @BindView(R.id.nestedScrollView) public NestedScrollView nestedScrollView;
    @BindDrawable(R.drawable.ic_favorite_border_white_24dp) public Drawable notFavouriteDrawable;
    @BindDrawable(R.drawable.ic_favorite_white_24dp) public Drawable favouriteDrawable;
    @State public boolean isFavourite;
    @State public boolean isFavouriteOriginal;
    @State public Movie movie;
    //@formatter:on

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Icepick.restoreInstanceState(this, savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);


        movie = (Movie) getIntent().getSerializableExtra(Intent.EXTRA_TEXT);
        Picasso.with(this)
                .load(Parser.formatImageUrl(movie.backdropPath, getString(R.string.image_size_large)))
                .placeholder(new ColorDrawable(0xFFFFFF))
                .into(backdropImage);

        if (savedInstanceState == null) {
            isFavourite = movie.isFavourite;
            isFavouriteOriginal = isFavourite;
        }

        Bundle arguments = new Bundle();
        arguments.putSerializable(Intent.EXTRA_TEXT, movie);
        MovieDetailFragment movieDetailFragment = new MovieDetailFragment();
        movieDetailFragment.setArguments(arguments);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.movie_detail_container, movieDetailFragment, MainActivity.MOVIE_DETAIL_FRAGMENT_TAG)
                .commit();
        setFavButtonDrawable();
        setUpLayoutMargins();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Icepick.saveInstanceState(this, outState);
    }

    /**
     * Changes margin of the content and {@link FloatingActionButton} according to the screen orientation.
     */
    private void setUpLayoutMargins() {
        CoordinatorLayout.LayoutParams scrollViewParams = (CoordinatorLayout.LayoutParams) nestedScrollView.getLayoutParams();

        AppBarLayout.ScrollingViewBehavior behavior = (AppBarLayout.ScrollingViewBehavior) scrollViewParams.getBehavior();
        CoordinatorLayout.LayoutParams fabParams = (CoordinatorLayout.LayoutParams) favFab.getLayoutParams();

        int overlayTopDimen = UI.dpToPx((int) getResources().getDimension(R.dimen.overlayTopDimen));
        int wideMargin = UI.dpToPx((int) getResources().getDimension(R.dimen.content_detail_wide_margin));
        int fabWideMargin = UI.dpToPx((int) getResources().getDimension(R.dimen.fab_margin));

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            behavior.setOverlayTop(overlayTopDimen);
            scrollViewParams.setMargins(wideMargin, 0, wideMargin, 0);
            fabParams.setMargins(fabWideMargin, fabWideMargin, fabWideMargin, fabWideMargin);
        } else {
            behavior.setOverlayTop(0);
            scrollViewParams.setMargins(0, 0, 0, 0);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    @OnClick(R.id.fav_fab)
    public void onFabClick() {
        isFavourite = !isFavourite;
        setFavButtonDrawable();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (isFavouriteOriginal != isFavourite) {
            Cache.setFavouriteMovie(this, movie.getId(), isFavourite);
            Intent intent = new Intent();
            intent.putExtra(getString(R.string.favourite_changed_key), true);
            intent.putExtra(getString(R.string.fav_movie_bool_key), isFavourite);
            intent.putExtra(getString(R.string.favourite_movie_id_key), movie.getId());
            setResult(RESULT_OK, intent);
            finish();
        }
        super.onBackPressed();
    }


    @Override
    public void onStop() {
        super.onStop();
        // Release loaders of YoutubeThumbnailView, otherwise Service connection leak occurs.
        if (VideosAdapter.viewLoaderMap == null) return;
        for (YouTubeThumbnailLoader loader : VideosAdapter.viewLoaderMap.values()) {
            loader.release();
        }
    }

    private void setFavButtonDrawable() {
        favFab.setImageDrawable((isFavourite) ? favouriteDrawable : notFavouriteDrawable);
    }

    public void setFabVisibility(int visibility) {
        favFab.setVisibility(visibility);
    }
}
