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

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.view.ViewGroup;

import com.calgen.prodek.fadflicks.R;
import com.calgen.prodek.fadflicks.adapter.GridMovieAdapter;
import com.calgen.prodek.fadflicks.fragment.GridFragment;
import com.calgen.prodek.fadflicks.fragment.MovieDetailFragment;
import com.calgen.prodek.fadflicks.utils.Cache;
import com.calgen.prodek.fadflicks.utils.CustomBundler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import icepick.Icepick;
import icepick.State;

/**
 * Handles set up of viewpager, where fragments are loaded which display
 * movie information. It also handles two-pane UI.
 */
public class MainActivity extends AppCompatActivity implements GridFragment.ReloadCallback {

    //re-enable usage of vectors for devices(API 19 and lower)
    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    public static final String MOVIE_DETAIL_FRAGMENT_TAG = "M_D_F_TAG";
    private static final int FAVOURITE_FRAGMENT_POSITION = 2;
    private static final int TOP_RATED_FRAGMENT_POSITION = 1;
    private static final int POPULAR_FRAGMENT_POSITION = 0;
    private static final int PAGE_LIMIT = 2;
    public static boolean twoPane;

    //@formatter:off
    @BindView(R.id.toolbar) public Toolbar toolbar;
    @BindView(R.id.tabs) public TabLayout tabLayout;
    @BindView(R.id.viewpager) public ViewPager viewPager;
    @State public int currentItemPosition;
    @State(CustomBundler.class) public Map<Integer, String> fragmentTags = new HashMap<>();
    private ViewPagerAdapter viewPagerAdapter;
    //@formatter:on

    @Override
    protected void onStart() {
        super.onStart();
        Cache.getFavouriteMovies(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        if (findViewById(R.id.movie_detail_container) != null) {
            twoPane = true;
            if (savedInstanceState == null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.movie_detail_container, new MovieDetailFragment(), MOVIE_DETAIL_FRAGMENT_TAG)
                        .commit();
            }
            toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        } else {
            twoPane = false;
        }
        setupViewPager();
        tabLayout.setupWithViewPager(viewPager);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.primary_dark));
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        currentItemPosition = viewPager.getCurrentItem();
        Icepick.saveInstanceState(this, outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Icepick.restoreInstanceState(this, savedInstanceState);
        viewPager.setCurrentItem(currentItemPosition);
    }

    private void setupViewPager() {
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());

        GridFragment popularFragment = new GridFragment();
        Bundle bundle = new Bundle();
        bundle.putString(getString(R.string.fragment_data_key), getString(R.string.popularData));
        popularFragment.setArguments(bundle);
        viewPagerAdapter.addFragment(popularFragment, getString(R.string.popular_tab_title));


        GridFragment topRatedFragment = new GridFragment();
        bundle = new Bundle();
        bundle.putString(getString(R.string.fragment_data_key), getString(R.string.topRatedData));
        topRatedFragment.setArguments(bundle);
        viewPagerAdapter.addFragment(topRatedFragment, getString(R.string.top_rated_tab_title));

        GridFragment favouriteFragment = new GridFragment();
        bundle = new Bundle();
        bundle.putString(getString(R.string.fragment_data_key), getString(R.string.favouritesData));
        favouriteFragment.setArguments(bundle);

        viewPagerAdapter.addFragment(favouriteFragment, getString(R.string.favourites_tab_title));

        viewPager.setAdapter(viewPagerAdapter);
        viewPager.setOffscreenPageLimit(PAGE_LIMIT);
    }

    @Override
    protected void onPause() {
        super.onPause();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GridMovieAdapter.FAV_REQUEST_CODE && resultCode == Activity.RESULT_OK &&
                data.getBooleanExtra(getString(R.string.favourite_changed_key), true)) {
            int movieId = data.getIntExtra(getString(R.string.favourite_movie_id_key), -1);
            boolean isFavourite;
            if (movieId == -1)
                return;
            if (data.hasExtra(getString(R.string.fav_movie_bool_key)))
                isFavourite = data.getBooleanExtra(getString(R.string.fav_movie_bool_key), false);
            else
                return;

            notifyFavouriteChange(movieId, isFavourite);
        }
    }

    /**
     * Notifies respective fragments about the change in the favourite value of the
     * movie with if {@code movieId}.
     *
     * @param movieId     id of the movie whose favourite value is changed
     * @param isFavourite value of the favourite
     */
    public void notifyFavouriteChange(int movieId, boolean isFavourite) {
        GridFragment gridFragment;
        currentItemPosition = viewPager.getCurrentItem();
        switch (currentItemPosition) {
            case 0:
            case 1:
                gridFragment = (GridFragment) viewPagerAdapter.getFragment(currentItemPosition);
                gridFragment.notifyChange(movieId, isFavourite);
                break;
            case 2:
                gridFragment = (GridFragment) viewPagerAdapter.getFragment(FAVOURITE_FRAGMENT_POSITION);
                gridFragment.notifyChange(movieId, isFavourite);

                gridFragment = (GridFragment) viewPagerAdapter.getFragment(TOP_RATED_FRAGMENT_POSITION);
                gridFragment.notifyChange(movieId, isFavourite);

                gridFragment = (GridFragment) viewPagerAdapter.getFragment(POPULAR_FRAGMENT_POSITION);
                gridFragment.notifyChange(movieId, isFavourite);
                return;
            default:
                break;
        }
        gridFragment = (GridFragment) viewPagerAdapter.getFragment(FAVOURITE_FRAGMENT_POSITION);
        gridFragment.notifyChange(movieId, isFavourite);

    }

    /**
     * Force viewpager to update views
     */
    @Override
    public void reloadData() {
        viewPager.getAdapter().notifyDataSetChanged();
    }

    public class ViewPagerAdapter extends FragmentPagerAdapter {

        private final List<Fragment> fragmentList = new ArrayList<>();
        private final List<String> fragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }


        @Override
        public CharSequence getPageTitle(int position) {
            return fragmentTitleList.get(position);
        }

        public void addFragment(Fragment fragment, String title) {
            fragmentList.add(fragment);
            fragmentTitleList.add(title);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            Object obj = super.instantiateItem(container, position);
            if (obj instanceof Fragment) {
                Fragment fragment = (Fragment) obj;
                String tag = fragment.getTag();
                fragmentTags.put(position, tag);
            }
            return obj;
        }

        public Fragment getFragment(int position) {
            String tag = fragmentTags.get(position);
            if (tag == null)
                return null;
            return getSupportFragmentManager().findFragmentByTag(tag);
        }
    }
}
