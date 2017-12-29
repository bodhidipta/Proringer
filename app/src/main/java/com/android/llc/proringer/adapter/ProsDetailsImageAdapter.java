package com.android.llc.proringer.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.android.llc.proringer.R;
import com.android.llc.proringer.activities.ProsProjectDetailsActivity;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;

/**
 * Created by su on 8/3/17.
 */

public class ProsDetailsImageAdapter extends RecyclerView.Adapter<ProsDetailsImageAdapter.MyViewHolder> {
    Context context;
    JSONArray imageJsonArray;
    int height;
    int width;
    ProsProjectDetailsActivity.onOptionSelected callback;

    public ProsDetailsImageAdapter(Context context, JSONArray imageJsonArray, ProsProjectDetailsActivity.onOptionSelected callback) {
        this.context = context;
        this.imageJsonArray = imageJsonArray;
        this.callback = callback;

        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((ProsProjectDetailsActivity) context).getWindowManager()
                .getDefaultDisplay()
                .getMetrics(displayMetrics);

        height = displayMetrics.heightPixels;
        width = displayMetrics.widthPixels;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_pro_details_images, parent, false);
        return new ProsDetailsImageAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        holder.img.getLayoutParams().width = (width - 30) / 5;
        holder.img.getLayoutParams().height = (int)3*(width - 30) / (5*4);
        try {
            if (!imageJsonArray.getJSONObject(position).getString("portfolio_image").equals(""))
                Glide.with(context).load(imageJsonArray.getJSONObject(position).getString("portfolio_image")).centerCrop().into(holder.img);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        holder.img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    callback.onItemPassed(position, imageJsonArray.getJSONObject(position).getString("portfolio_id"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return imageJsonArray.length();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView img;

        public MyViewHolder(View itemView) {
            super(itemView);
            img = (ImageView) itemView.findViewById(R.id.img);
        }
    }
}
