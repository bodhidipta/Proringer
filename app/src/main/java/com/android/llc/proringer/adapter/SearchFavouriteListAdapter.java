package com.android.llc.proringer.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import com.android.llc.proringer.R;
import com.android.llc.proringer.activities.ProjectDetailsActivity;
import com.android.llc.proringer.appconstant.ProApplication;
import com.android.llc.proringer.viewsmod.textview.ProRegularTextView;
import com.android.llc.proringer.viewsmod.textview.ProSemiBoldTextView;
import com.bumptech.glide.Glide;

import org.json.JSONArray;

/**
 * Created by su on 7/13/17.
 */

public class SearchFavouriteListAdapter extends RecyclerView.Adapter<SearchFavouriteListAdapter.ViewHolder> {
    private Context mcontext = null;
    JSONArray jsonInfoArray;

    public SearchFavouriteListAdapter(Context mcontext,JSONArray jsonInfoArray) {
        this.mcontext = mcontext;
        this.jsonInfoArray=jsonInfoArray;
    }

    @Override
    public int getItemCount() {
        return jsonInfoArray.length();
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.ratingBar.setMax(5);
        try {
            holder.ratingBar.setRating(Float.valueOf(jsonInfoArray.getJSONObject(position).getString("pro_avg_review_rating")));
            holder.tv_pros_company_name.setText(jsonInfoArray.getJSONObject(position).getString("pros_company_name"));
            holder.tv_category_name.setText(jsonInfoArray.getJSONObject(position).getString("category_name"));
            holder.tv_address.setText(jsonInfoArray.getJSONObject(position).getString("city")
            +","+jsonInfoArray.getJSONObject(position).getString("state"));

            holder.tv_total_review.setText("("+jsonInfoArray.getJSONObject(position).getString("total_review")+")");

            if (!jsonInfoArray.getJSONObject(position).getString("profile_img").equals(""))
                Glide.with(mcontext).load(jsonInfoArray.getJSONObject(position).getString("profile_img")).centerCrop().into(holder.img_project);


            if(jsonInfoArray.getJSONObject(position).getString("pros_verify").equalsIgnoreCase("Y")){
                holder.img_verify.setVisibility(View.VISIBLE);
                holder.img_verify_tick.setVisibility(View.VISIBLE);
                holder.tv_verify.setVisibility(View.VISIBLE);
            }else {
                holder.img_verify.setVisibility(View.GONE);
                holder.img_verify_tick.setVisibility(View.GONE);
                holder.tv_verify.setVisibility(View.GONE);
            }


        }
        catch (Exception e)
        {
            e.printStackTrace();
        }




        holder.main_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(mcontext, ProjectDetailsActivity.class);
                mcontext.startActivity(intent);
            }
        });

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(mcontext).inflate(R.layout.search_pro_list_rowitem, parent, false));
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        RatingBar ratingBar;
        CardView main_container;
        ProSemiBoldTextView tv_pros_company_name;
        ProRegularTextView tv_category_name,tv_address,tv_verify,tv_total_review;
        ImageView img_project,img_verify,img_verify_tick;

        public ViewHolder(View itemView) {
            super(itemView);
            ratingBar = (RatingBar) itemView.findViewById(R.id.ratingPro);

            main_container= (CardView) itemView.findViewById(R.id.main_container);

            tv_pros_company_name= (ProSemiBoldTextView) itemView.findViewById(R.id.tv_pros_company_name);

            tv_category_name= (ProRegularTextView) itemView.findViewById(R.id.tv_category_name);
            tv_total_review= (ProRegularTextView) itemView.findViewById(R.id.tv_total_review);
            tv_address= (ProRegularTextView) itemView.findViewById(R.id.tv_address);
            tv_verify= (ProRegularTextView) itemView.findViewById(R.id.tv_verify);

            img_project= (ImageView) itemView.findViewById(R.id.img_project);
            img_verify= (ImageView) itemView.findViewById(R.id.img_verify);
            img_verify_tick= (ImageView) itemView.findViewById(R.id.img_verify_tick);

        }
    }
}
