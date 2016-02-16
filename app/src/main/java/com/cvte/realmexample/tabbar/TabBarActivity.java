package com.cvte.realmexample.tabbar;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;

import com.cvte.realmexample.R;

import rx.Observable;
import rx.subjects.BehaviorSubject;

/**
 * Created by mluhui on 16/1/11.
 */
public abstract class TabBarActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener {

    private TabBarView mTabBarView;
    private ViewPager mViewPager;
    private ViewGroup mContentView;
    private TabBarItem[] tabBarItems;
    private TabBarAdapter mAdapter;
    private BehaviorSubject<Integer> mSelectedIndexSubject = BehaviorSubject.create();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_tabbar);
        mTabBarView = (TabBarView) super.findViewById(R.id.tabBarView);
        mTabBarView.setOnTabBarItemClickListener(this::onItemSelected);
        mViewPager = (ViewPager) super.findViewById(R.id.viewPager_tabBarContent);
        mContentView = (ViewGroup) super.findViewById(R.id.relativeLayout_tabBarContainer);
        mAdapter = new TabBarAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mAdapter);
        mViewPager.addOnPageChangeListener(this);
        mViewPager.setOffscreenPageLimit(4);

        getSupportFragmentManager().addOnBackStackChangedListener(() -> {
            if (getSupportFragmentManager().getBackStackEntryCount() == 0 && mTabBarView.getVisibility() != View.VISIBLE) {
                mTabBarView.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void setContentView(View view) {
        mContentView.addView(view);
    }

    @Override
    public void setContentView(int layoutResID) {
        getLayoutInflater().inflate(layoutResID, mContentView, true);
    }

    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params) {
        mContentView.addView(view, params);
    }

    @Override
    public View findViewById(int id) {
        return mContentView.findViewById(id);
    }

    public TabBarItem[] getTabBarItems() {
        return tabBarItems;
    }

    public void setTabBarItems(TabBarItem[] tabBarItems) {
        this.tabBarItems = tabBarItems;
        mTabBarView.setTabBarItems(tabBarItems);
        mAdapter.notifyDataSetChanged();
        mTabBarView.setSelectedIndex(0);
    }

    public int getSelectedIndex() {
        return mViewPager.getCurrentItem();
    }

    public Observable<Integer> getSelectedIndexObservable() {
        return mSelectedIndexSubject.distinctUntilChanged();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        mSelectedIndexSubject.onNext(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    public void onItemSelected(TabBarItem item, int index) {
        mViewPager.setCurrentItem(index, false);
    }

    private Fragment getFragment(int index) {
        TabBarItem tabBarItem = tabBarItems[index];
        String contentFragmentIdentifier = tabBarItem.contentFragmentIdentifier;
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(contentFragmentIdentifier);
        if (fragment == null) {
            try {
                fragment = (Fragment) Class.forName(contentFragmentIdentifier).newInstance();
                onCreateContentFragment(fragment, index);
            } catch (InstantiationException | IllegalAccessException | ClassNotFoundException | ClassCastException e) {
                e.printStackTrace();
            }
        }
        return fragment;
    }

    protected void onCreateContentFragment(Fragment fragment, int index) {
    }

    private class TabBarAdapter extends FragmentPagerAdapter {

        public TabBarAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return getFragment(position);
        }

        @Override
        public int getCount() {
            if (tabBarItems != null) {
                return tabBarItems.length;
            } else {
                return 0;
            }
        }
    }
}
