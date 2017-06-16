package com.android.llc.proringer.viewsmod.textview;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.widget.TextView;

import com.android.llc.proringer.viewsmod.FontCache;

/**
 * Created by Bodhidipta on 13/06/16.
 */
public class ProLightTextView extends AppCompatTextView {

    public ProLightTextView(Context context) {
        super(context);
        init(context);
    }

    public ProLightTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    public ProLightTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public void init(Context context) {
        super.setTypeface(FontCache.get("OpenSans-Light.ttf", context));
    }
}