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

/**
 * Created by su on 8/3/17.
 */

public class ProsDetailsPortfolioImageAdapter extends RecyclerView.Adapter<ProsDetailsPortfolioImageAdapter.MyViewHolder> {
    Context context;
    JSONArray portfolioInfoArray;
    int height;
    int width;

    public ProsDetailsPortfolioImageAdapter(Context context, JSONArray portfolioInfoArray) {
        this.context = context;
        this.portfolioInfoArray = portfolioInfoArray;


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
        return new ProsDetailsPortfolioImageAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.img.getLayoutParams().width = (width - 30);
        holder.img.getLayoutParams().height = (height - 30) / 2;
        try {
            if (!portfolioInfoArray.getJSONObject(position).getString("portfolio_img").equals(""))
                Glide.with(context).load(portfolioInfoArray.getJSONObject(position).getString("portfolio_img")).centerCrop().into(holder.img);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
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
