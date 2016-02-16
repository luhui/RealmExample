package com.cvte.realmexample.tabbar;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.cvte.realmexample.R;


/**
 * Created by mluhui on 16/1/11.
 */
public class TabBarView extends RelativeLayout {

    private int mSelectedIndex = -1;
    private TabBarItemView mSelectedTabBarItemView;
    private OnTabBarBarItemClickListener mOnTabBarBarItemClickListener;

    public TabBarView(Context context, TabBarItem[] barItems) {
        super(context);
        init(context);
        setTabBarItems(barItems);
    }

    public TabBarView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init(context);
    }

    private void init(Context context) {
        setGravity(Gravity.CENTER);
        setBackgroundColor(Color.WHITE);
    }

    private void setDivider() {
        View divider = new View(getContext());
        divider.setBackgroundResource(R.color.tertiary);
        LayoutParams dividerParams = new LayoutParams(LayoutParams.MATCH_PARENT, 1);
        addView(divider, dividerParams);
    }

    public int getSelectedIndex() {
        return mSelectedIndex;
    }

    public void setSelectedIndex(int selectedIndex) {
        TabBarItemView tabBarItemView = (TabBarItemView) findViewById(getIDFromIndex(selectedIndex));
        tabBarItemView.performClick();
    }

    protected void setTabBarItems(TabBarItem[] barItems) {
        removeAllViews();
        setDivider();
        LinearLayout container = new LinearLayout(getContext());
        LayoutParams containerParams = new LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT);
        addView(container, containerParams);
        for (int i = 0; i < barItems.length; i++) {
            TabBarItem barItem = barItems[i];
            TabBarItemView tabBarItemView = new TabBarItemView(getContext(), barItem);
            addTabBarItemView(container, tabBarItemView, i);
        }
    }

    protected void setTabBarItemViews(TabBarItemView[] tabBarItemViews) {
        removeAllViews();
        setDivider();
        LinearLayout container = new LinearLayout(getContext());
        LayoutParams containerParams = new LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT);
        addView(container, containerParams);
        for (int i = 0; i < tabBarItemViews.length; i++) {
            TabBarItemView tabBarItemView = tabBarItemViews[i];
            addTabBarItemView(container, tabBarItemView, i);
        }
    }

    protected void setOnTabBarItemClickListener(OnTabBarBarItemClickListener onTabBarItemClickListener) {
        mOnTabBarBarItemClickListener = onTabBarItemClickListener;
    }

    private void addTabBarItemView(LinearLayout container, TabBarItemView tabBarItemView, int index) {
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(0,
                LinearLayout.LayoutParams.MATCH_PARENT);
        layoutParams.weight = 1;
        tabBarItemView.setId(getIDFromIndex(index));
        container.addView(tabBarItemView, layoutParams);
        tabBarItemView.setOnClickListener(v -> {
            if (mSelectedIndex == index) {
                return;
            }
            if (mSelectedTabBarItemView != null) {
                mSelectedTabBarItemView.setSelected(false);
            }
            mSelectedIndex = index;
            mSelectedTabBarItemView = (TabBarItemView) v;
            v.setSelected(true);
            TabBarItem tabBarItem = tabBarItemView.getTabBarItem();
            if (mOnTabBarBarItemClickListener != null) {
                mOnTabBarBarItemClickListener.onTabBarItemClick(tabBarItem, index);
            }
            if (tabBarItem.onItemClickListener != null) {
                tabBarItem.onItemClickListener.onItemClick(tabBarItem);
            }
        });
    }

    private int getIDFromIndex(int i) {
        return i + 1;
    }

    public interface OnTabBarBarItemClickListener {

        void onTabBarItemClick(TabBarItem tabBarItem, int index);
    }
}
