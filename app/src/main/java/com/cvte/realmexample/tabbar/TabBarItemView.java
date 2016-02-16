package com.cvte.realmexample.tabbar;

import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cvte.realmexample.R;

import rx.android.schedulers.AndroidSchedulers;

/**
 * Created by mluhui on 16/1/11.
 */
public class TabBarItemView extends RelativeLayout {

    private static final int DefaultMargin = 4;
    private static final int TextSize = 12;

    private TextView mTextView;
    private TabBarItem mTabBarItem;
    private TextView mBadgeView;

    public TabBarItemView(Context context, TabBarItem tabBarItem) {
        super(context);
        initView(context);
        mTabBarItem = tabBarItem;

        mTextView.setText(tabBarItem.titleResource);
        mTextView.setCompoundDrawablesWithIntrinsicBounds(0, tabBarItem.imageResource, 0, 0);

        if (tabBarItem.badgeObservable != null) {
            tabBarItem
                    .badgeObservable
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(s -> {
                        if (s == null) {
                            hideBadge();
                        } else {
                            showBadge(s);
                        }
                    });
        }
    }

    public TabBarItemView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        initView(context);
    }

    private void initView(Context context) {

        mTextView = new TextView(context);
        mTextView.setPadding(0, 0, 0, UIUtils.dip2px(context, 2));
        mTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, TextSize);
        mTextView.setCompoundDrawablePadding(UIUtils.dip2px(context, DefaultMargin));
        LayoutParams textParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        textParams.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
        textParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);

        mTextView.setId(R.id.textView);
        addView(mTextView, textParams);

    }

    public TextView getTextView() {
        return mTextView;
    }

    public TabBarItem getTabBarItem() {
        return mTabBarItem;
    }

    public void setSelected(boolean selected) {
        Resources res = getContext().getResources();
        mTextView.setCompoundDrawablesWithIntrinsicBounds(0, selected ?
                mTabBarItem.imageResourceFocus : mTabBarItem.imageResource, 0, 0);
    }

    public void showBadge(String text) {
        mBadgeView.setVisibility(View.VISIBLE);
        mBadgeView.setText(text);
    }

    public void hideBadge() {
        mBadgeView.setVisibility(View.GONE);
    }
}
