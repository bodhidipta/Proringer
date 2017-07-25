package com.android.llc.proringer.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.llc.proringer.R;
import com.android.llc.proringer.fragments.main_content.MyProjectRatePro;

/**
 * Created by su on 7/25/17.
 */

public class MyProjectRateProAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Context  mcontext;
    public MyProjectRateProAdapter(Context context){
        this.mcontext=context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType==0){
            return new ViewHolder0(LayoutInflater.from(mcontext).inflate(R.layout.adapter_pro_rate_first_view, parent, false));
        }
        else {
            return new ViewHolder0(LayoutInflater.from(mcontext).inflate(R.layout.adapter_pro_rate_second_view, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (holder.getItemViewType()) {
            case 0:
                ViewHolder0 viewHolder0 = (ViewHolder0) holder;
                break;

            case 2:
                ViewHolder2 viewHolder2 = (ViewHolder2) holder;
                break;
        }
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    class ViewHolder0 extends RecyclerView.ViewHolder {
        public ViewHolder0(View itemView) {
            super(itemView);
        }
    }

    class ViewHolder2 extends RecyclerView.ViewHolder {
        public ViewHolder2(View itemView) {
            super(itemView);
        }
    }

    @Override
    public int getItemViewType(int position) {
        // Just as an example, return 0 or 2 depending on position
        // Note that unlike in ListView adapters, types don't have to be contiguous
        return position % 2 * 2;
    }

}