package com.android.llc.proringer.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.android.llc.proringer.R;import com.bumptech.glide.Glide;

import org.json.JSONArray;

/**
 * Created by su on 8/3/17.
 */

public class ProsProjectDetailsAdapter extends RecyclerView.Adapter<ProsProjectDetailsAdapter.MyViewHolder> {
    Context context;
    JSONArray imagejsonArray;
    public ProsProjectDetailsAdapter(Context context,JSONArray imagejsonArray){
        this.context=context;
        this.imagejsonArray=imagejsonArray;
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_pro_details_images, parent, false);
        return new ProsProjectDetailsAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        try {
            if (!imagejsonArray.getJSONObject(position).getString("portfolio_image").equals(""))
                Glide.with(context).load(imagejsonArray.getJSONObject(position).getString("portfolio_image")).centerCrop().into(holder.img);
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return imagejsonArray.length();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView img;
        public MyViewHolder(View itemView) {
            super(itemView);
            img= (ImageView) itemView.findViewById(R.id.img);
        }
    }
}
