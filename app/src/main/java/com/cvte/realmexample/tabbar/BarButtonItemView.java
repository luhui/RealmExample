package com.cvte.realmexample.tabbar;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cvte.realmexample.R;

/**
 * Created by mluhui on 16/1/11.
 */
public class BarButtonItemView extends LinearLayout {

    private ImageButton mImageButton;

    public BarButtonItemView(Context context, BarItem barItem) {
        super(context);
        setOrientation(HORIZONTAL);
        setGravity(Gravity.CENTER_VERTICAL);
        Resources res = context.getResources();

        if (barItem.imageResource > 0) {
            int[] attrs = new int[]{android.R.attr.selectableItemBackground};
            TypedArray typedArray = context.obtainStyledAttributes(attrs);
            Drawable background = typedArray.getDrawable(0);
            typedArray.recycle();
            mImageButton = new ImageButton(context);
            mImageButton.setBackground(background);
            mImageButton.setImageResource(barItem.imageResource);
            int size = res.getDimensionPixelSize(R.dimen.DP_56);
            LayoutParams imageButtonParams = new LayoutParams(size, size);
            addView(mImageButton, imageButtonParams);
        }

        if (barItem.titleResource > 0) {
            TextView textView = new TextView(context);
            textView.setText(barItem.titleResource);
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
            textView.setTextColor(Color.BLACK);
            LayoutParams textParams = new LayoutParams(
                    LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            textParams.gravity = Gravity.CENTER_VERTICAL;
            int margin = (barItem.imageResource > 0) ? 0 :
                    res.getDimensionPixelSize(R.dimen.DP_16);
            textParams.setMargins(margin, 0, 0, 0);
            addView(textView, textParams);
        }
    }

    @Override
    public void setOnClickListener(OnClickListener l) {
        if (mImageButton != null) {
            mImageButton.setOnClickListener(l);
        } else {
            super.setOnClickListener(l);
        }
    }
}
