package com.cvte.realmexample.tabbar;

import android.view.View;

/**
 * Created by mluhui on 16/1/11.
 */
public class BarItem {

    public int titleResource;
    public int imageResource;
    public int imageResourceFocus;
    public View customView;
    public OnItemClickListener onItemClickListener;

    public BarItem(View customView) {
        this.customView = customView;
    }

    public BarItem(int titleResource, int imageResource) {
        this.titleResource = titleResource;
        this.imageResource = imageResource;
    }

    public BarItem(int titleResource, int imageResource, int imageResourceFocus) {
        this.titleResource = titleResource;
        this.imageResource = imageResource;
        this.imageResourceFocus = imageResourceFocus;
    }

    public interface OnItemClickListener {

        void onItemClick(BarItem barItem);
    }
}
