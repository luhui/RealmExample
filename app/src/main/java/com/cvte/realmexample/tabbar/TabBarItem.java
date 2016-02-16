package com.cvte.realmexample.tabbar;

import rx.Observable;

/**
 * Created by mluhui on 16/1/11.
 */
public class TabBarItem extends BarItem {

    public String contentFragmentIdentifier;
    public Observable<String> badgeObservable;

    public TabBarItem(int titleResource, int imageResource, int imageResourceFocus, String contentFragmentIdentifier) {
        super(titleResource, imageResource, imageResourceFocus);
        this.contentFragmentIdentifier = contentFragmentIdentifier;
    }
}
