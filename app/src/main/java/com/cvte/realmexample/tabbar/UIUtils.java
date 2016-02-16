package com.cvte.realmexample.tabbar;

import android.content.Context;
import android.graphics.Paint;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

/**
 * Created by zhengyuji on 9/4/14.
 */
public class UIUtils {

    public static int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    public static int px2dip(Context context, float pxValue) {
        float density = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / density + 0.5f);
    }

    public static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    public static int getScreenWidth(Context context) {
        DisplayMetrics metrics = new DisplayMetrics();
        ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay()
                .getMetrics(metrics);
        return metrics.widthPixels;
    }

    public static int getScreenHeight(Context context) {
        DisplayMetrics metrics = new DisplayMetrics();
        ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay()
                .getMetrics(metrics);
        return metrics.heightPixels;
    }

    public static float getTextWidth(String text) {
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        return paint.measureText(text);
    }

    public static int setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return 0;
        }

        int totalHeight = 0;
        if (listAdapter instanceof BaseAdapter) {
            BaseAdapter adapter = (BaseAdapter) listAdapter;
            adapter.notifyDataSetChanged();
        }
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        return totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
    }
}
