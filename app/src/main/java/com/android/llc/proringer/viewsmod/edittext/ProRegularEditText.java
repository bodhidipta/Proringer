package com.android.llc.proringer.viewsmod.edittext;

import android.content.Context;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatEditText;
import android.util.AttributeSet;
import android.view.KeyEvent;


import com.android.llc.proringer.viewsmod.FontCache;

/**
 * Created by Bodhidipta on 13/06/16.
 */
public class ProRegularEditText extends AppCompatEditText {

    public ProRegularEditText(Context context) {
        super(context);
        init(context);
    }

    public ProRegularEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    public ProRegularEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public void init(Context context) {
        super.setTypeface(FontCache.get("OpenSans-Regular.ttf", context));
    }

    /**
     * Keyboard Listener
     */
    ProRegularEditText.KeyboardListener listener;

    public void setOnKeyboardListener(ProRegularEditText.KeyboardListener listener) {
        this.listener = listener;
    }

    public interface KeyboardListener {
        void onStateChanged(ProRegularEditText keyboardEditText, boolean showing);
    }

    @Override
    public boolean onKeyPreIme(int keyCode, @NonNull KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK
                && event.getAction() == KeyEvent.ACTION_UP) {
            if (listener != null)
                listener.onStateChanged(this, false);
        }
        return super.onKeyPreIme(keyCode, event);
    }

    @Override
    protected void onFocusChanged(boolean focused, int direction, Rect previouslyFocusedRect) {
        super.onFocusChanged(focused, direction, previouslyFocusedRect);
        if (listener != null)
            listener.onStateChanged(this, true);
    }
}
