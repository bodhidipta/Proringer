package com.android.llc.proringer.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import com.android.llc.proringer.R;
import com.android.llc.proringer.activities.ProjectDetailsActivity;

/**
 * Created by su on 7/13/17.
 */

public class SearchFavouriteListAdapter extends RecyclerView.Adapter<SearchFavouriteListAdapter.ViewHolder> {
    private Context mcontext = null;

    public SearchFavouriteListAdapter(Context mcontext) {
        this.mcontext = mcontext;
    }

    @Override
    public int getItemCount() {
        return 15;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.ratingBar.setMax(5);
        holder.ratingBar.setRating(4);

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

        public ViewHolder(View itemView) {
            super(itemView);
            ratingBar = (RatingBar) itemView.findViewById(R.id.ratingPro);
            main_container= (CardView) itemView.findViewById(R.id.main_container);

        }
    }
}
