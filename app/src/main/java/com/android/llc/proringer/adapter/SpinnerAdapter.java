package com.android.llc.proringer.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.android.llc.proringer.R;
import com.android.llc.proringer.viewsmod.textview.ProRegularTextView;

import java.util.ArrayList;

/**
 * Created by Bodhidipta on 6/20/16.
 */
public class SpinnerAdapter extends ArrayAdapter<String> {

    private Context context;
    private ProRegularTextView spinertitle;
    private ArrayList<String> option_list;


    public SpinnerAdapter(Context context, int resource, ArrayList<String> objects) {
        super(context, resource, objects);
        this.context = context;
        option_list = objects;


    }

    @Override
    public int getCount() {
        return option_list.size();
    }


    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater)
                    context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.spinner_text, null);
        }


        spinertitle = (ProRegularTextView) convertView.findViewById(R.id.txtspinemwnu);
        spinertitle.setText(option_list.get(position));


        return convertView;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater)
                    context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.actionmenuspiner, null);
        }
//        if (position == 0) {
//            spinertitle = (OpenSansLightTextView) convertView.findViewById(R.id.txtspinemwnu);
//            spinertitle.setVisibility(View.GONE);
//        } else {
        spinertitle = (ProRegularTextView) convertView.findViewById(R.id.txtspinemwnu);
        spinertitle.setVisibility(View.VISIBLE);
        spinertitle.setText(option_list.get(position));
        //   }


        return convertView;
    }


}
