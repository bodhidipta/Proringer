package com.android.llc.proringer.adapter;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.android.llc.proringer.R;
import com.android.llc.proringer.activities.ProsProjectDetailsActivity;
import com.android.llc.proringer.activities.ProsProjectGalleryActivity;
import com.bumptech.glide.Glide;
import org.json.JSONArray;
import org.json.JSONException;

/**
 * Created by su on 8/3/17.
 */

public class ProsDetailsPortfolioImageAdapter extends RecyclerView.Adapter<ProsDetailsPortfolioImageAdapter.MyViewHolder> {
    Context context;
    JSONArray portfolioInfoArray;
    ProsProjectGalleryActivity.onOptionSelected callback;
    int x;

    public ProsDetailsPortfolioImageAdapter(Context context, JSONArray portfolioInfoArray,ProsProjectGalleryActivity.onOptionSelected callback,int x) {
        this.context = context;
        this.portfolioInfoArray = portfolioInfoArray;
        this.callback = callback;
        this.x=x;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_pro_details_images, parent, false);
        GridLayoutManager.LayoutParams params = (GridLayoutManager.LayoutParams) itemView.getLayoutParams();
        params.height = x;
        itemView.setLayoutParams(params);
        return new ProsDetailsPortfolioImageAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        try {
            if (!portfolioInfoArray.getJSONObject(position).getString("portfolio_img").equals(""))
                Glide.with(context).load(portfolioInfoArray.getJSONObject(position).getString("portfolio_img")).override(600, 200) // resizes the image to these dimensions (in pixel)
                        .centerCrop().into(holder.img);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        holder.img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    callback.onItemPassed(position, portfolioInfoArray.getJSONObject(position).getString("portfolio_img"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return portfolioInfoArray.length();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView img;

        public MyViewHolder(View itemView) {
            super(itemView);
            img = (ImageView) itemView.findViewById(R.id.img);
        }
    }
}
