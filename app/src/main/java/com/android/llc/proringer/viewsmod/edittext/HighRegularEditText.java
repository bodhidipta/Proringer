package com.android.llc.proringer.viewsmod.edittext;

import android.content.Context;
import android.support.v7.widget.AppCompatEditText;
import android.util.AttributeSet;

import com.android.llc.proringer.viewsmod.FontCache;

/**
 * Created by Bodhidipta on 13/06/16.
 */
public class HighRegularEditText extends AppCompatEditText {

    public HighRegularEditText(Context context) {
        super(context);
        init(context);
    }

    public HighRegularEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    public HighRegularEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public void init(Context context) {
        super.setTypeface(FontCache.get("MYRIADPRO-REGULAR.OTF", context));
    }
}
