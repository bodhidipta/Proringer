package com.android.llc.proringer.viewsmod;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

import com.android.llc.proringer.R;
import com.android.llc.proringer.viewsmod.textview.ProBoldTextView;

/**
 * Created by bodhidipta on 22/06/17.
 * <!-- * Copyright (c) 2017, The Proringer-->
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * <p>
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

public class SwitchHelper extends RelativeLayout {
    private Context mcontext = null;
    private View viewinf;
    private ProBoldTextView lebel = null;
    private RelativeLayout switch_off = null;
    boolean isEnable = false;
    private onClickListener listener;
    public static final String ON = "TRUE";
    public static final String OFF = "FALSE";


    /**
     * Simple constructor to use when creating a view from code.
     *
     * @param context The Context the view is running in, through which it can
     *                access the current theme, resources, etc.
     */
    public SwitchHelper(Context context) {
        super(context);
        mcontext = context;
        intiView();
    }

    public SwitchHelper(Context context, AttributeSet attrs) {
        super(context, attrs);
        mcontext = context;
        intiView();
    }

    public void setState(String isOn) {

        if (isOn.equals(OFF)) {
            isEnable = false;
            lebel.setText("OFF");
            switch_off.setVisibility(VISIBLE);
            lebel.setTextColor(Color.parseColor("#000000"));
            if (listener != null)
                listener.onClick(false);
        } else {
            isEnable = true;
            lebel.setText("ON");
            switch_off.setVisibility(GONE);
            lebel.setTextColor(Color.parseColor("#f1592a"));
            if (listener != null)
                listener.onClick(true);
        }

    }

    public String getState() {
        return isEnable ? "TRUE" : "FALSE";
    }


    public void setOnclickCallback(onClickListener callback) {
        listener = callback;
    }

    private void intiView() {
        final LayoutInflater li = (LayoutInflater) mcontext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        viewinf = li.inflate(R.layout.switch_view, this);
        lebel = (ProBoldTextView) viewinf.findViewById(R.id.text_state);
        switch_off = (RelativeLayout) viewinf.findViewById(R.id.switch_off);


        viewinf.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isEnable) {
                    isEnable = false;
                    lebel.setText("OFF");
                    switch_off.setVisibility(VISIBLE);
                    lebel.setTextColor(Color.parseColor("#000000"));
                    if (listener != null)
                        listener.onClick(false);
                } else {
                    isEnable = true;
                    lebel.setText("ON");
                    switch_off.setVisibility(GONE);
                    lebel.setTextColor(Color.parseColor("#f1592a"));
                    if (listener != null)
                        listener.onClick(true);
                }
            }
        });
    }

    public interface onClickListener {
        void onClick(boolean state);
    }

}
