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
 * Created by su on 8/2/17.
 */

public class ProsDetailsServiceAdapter extends RecyclerView.Adapter<ProsDetailsServiceAdapter.MyViewHolder> {
    JSONArray serviceJsonArray;
    Context context;

    public ProsDetailsServiceAdapter(Context context, JSONArray serviceJsonArray){
        this.context=context;
        this.serviceJsonArray=serviceJsonArray;
    }

    @Override
    public ProsDetailsServiceAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_pro_details_service, parent, false);
        return new ProsDetailsServiceAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ProsDetailsServiceAdapter.MyViewHolder holder, int position) {
        try {
            holder.tv_name.setText(serviceJsonArray.getJSONObject(position).getString("service_name"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return serviceJsonArray.length();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ProRegularTextView tv_name;
        public MyViewHolder(View itemView) {
            super(itemView);
            tv_name= (ProRegularTextView) itemView.findViewById(R.id.tv_name);
        }
    }
}
