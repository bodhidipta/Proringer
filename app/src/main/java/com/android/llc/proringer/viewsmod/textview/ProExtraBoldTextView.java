package com.android.llc.proringer.viewsmod.textview;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

import com.android.llc.proringer.viewsmod.FontCache;

/**
 * Created by Bodhidipta on 13/06/16.
 */
public class ProExtraBoldTextView extends AppCompatTextView {

    public ProExtraBoldTextView(Context context) {
        super(context);
        init(context);
    }

    public ProExtraBoldTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    public ProExtraBoldTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public void init(Context context) {
        super.setTypeface(FontCache.get("OpenSans-ExtraBold.ttf", context));
    }
}