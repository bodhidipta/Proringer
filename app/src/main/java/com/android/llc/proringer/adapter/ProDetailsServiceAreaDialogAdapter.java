package com.android.llc.proringer.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.llc.proringer.R;
import com.android.llc.proringer.viewsmod.textview.ProRegularTextView;

import org.json.JSONArray;
import org.json.JSONException;

/**
 * Created by su on 8/3/17.
 */

public class ProDetailsServiceAreaDialogAdapter extends RecyclerView.Adapter<ProDetailsServiceAreaDialogAdapter.MyViewHolder> {
    JSONArray serviceAreaJsonArray;
    Context context;

    public ProDetailsServiceAreaDialogAdapter(Context context, JSONArray serviceJsonArray) {
        this.context = context;
        this.serviceAreaJsonArray = serviceJsonArray;
    }

    @Override
    public ProDetailsServiceAreaDialogAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_pro_details_service, parent, false);
        return new ProDetailsServiceAreaDialogAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ProDetailsServiceAreaDialogAdapter.MyViewHolder holder, final int position) {
        try {
            holder.tv_name.setText(serviceAreaJsonArray.getJSONObject(position).getString("city_services"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return serviceAreaJsonArray.length();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ProRegularTextView tv_name;

        public MyViewHolder(View itemView) {
            super(itemView);
            tv_name = (ProRegularTextView) itemView.findViewById(R.id.tv_name);
        }
    }
}
