package com.calgen.prodek.fadflicks.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.calgen.prodek.fadflicks.fragment.FavouriteFragment;
import com.calgen.prodek.fadflicks.fragment.PopularFragment;
import com.calgen.prodek.fadflicks.fragment.TopRatedFragment;
import com.calgen.prodek.fadflicks.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import icepick.Icepick;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    //@formatter:off
    @BindView(R.id.toolbar) public Toolbar toolbar;
    @BindView(R.id.tabs) public TabLayout tabLayout;
    @BindView(R.id.viewpager) public ViewPager viewPager;
    //@formatter:on

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Icepick.saveInstanceState(this, outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Icepick.restoreInstanceState(this, savedInstanceState);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        setupViewPager();
        tabLayout.setupWithViewPager(viewPager);
    }

    private void setupViewPager() {
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFragment(new PopularFragment(), getString(R.string.popular_tab_title));
        viewPagerAdapter.addFragment(new TopRatedFragment(), getString(R.string.top_rated_tab_title));
        viewPagerAdapter.addFragment(new FavouriteFragment(), getString(R.string.favourites_tab_title));
        viewPager.setAdapter(viewPagerAdapter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    public class ViewPagerAdapter extends FragmentPagerAdapter {

        private final List<Fragment> fragmentList = new ArrayList<>();
        private final List<String> fragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
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
    }
}
