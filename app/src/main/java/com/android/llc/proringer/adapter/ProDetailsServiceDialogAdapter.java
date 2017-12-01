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
 * Created by su on 9/21/17.
 */

public class ProDetailsServiceDialogAdapter extends RecyclerView.Adapter<ProDetailsServiceDialogAdapter.MyViewHolder> {
    JSONArray serviceAreaJsonArray;
    Context context;

    public ProDetailsServiceDialogAdapter(Context context, JSONArray serviceJsonArray) {
        this.context = context;
        this.serviceAreaJsonArray = serviceJsonArray;
    }

    @Override
    public ProDetailsServiceDialogAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_pro_details_service, parent, false);
        return new ProDetailsServiceDialogAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ProDetailsServiceDialogAdapter.MyViewHolder holder, final int position) {
        try {
            holder.tv_name.setText(serviceAreaJsonArray.getJSONObject(position).getString("service_name"));
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
