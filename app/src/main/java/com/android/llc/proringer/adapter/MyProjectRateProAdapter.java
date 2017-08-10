package com.android.llc.proringer.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.android.llc.proringer.R;
import com.android.llc.proringer.viewsmod.textview.ProRegularTextView;

/**
 * Created by su on 7/25/17.
 */

public class MyProjectRateProAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Context mcontext;
    boolean check_divider;

    public MyProjectRateProAdapter(Context context) {
        this.mcontext = context;
        check_divider = true;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == 0) {
            return new ViewHolder0(LayoutInflater.from(mcontext).inflate(R.layout.adapter_pro_rate_first_view, parent, false));
        } else {
            return new ViewHolder2(LayoutInflater.from(mcontext).inflate(R.layout.adapter_pro_rate_second_view, parent, false));
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
                if (check_divider) {
                    viewHolder2.View_SplitHorLine.setVisibility(View.VISIBLE);
                    check_divider = false;
                }
                break;
        }
    }

    @Override
    public int getItemCount() {
        return 4;
    }

    class ViewHolder0 extends RecyclerView.ViewHolder {
        ProRegularTextView tv_name, tv_description;
        ImageView img_project;

        public ViewHolder0(View itemView) {
            super(itemView);
            img_project = (ImageView) itemView.findViewById(R.id.img_project);
            tv_name = (ProRegularTextView) itemView.findViewById(R.id.tv_name);
            tv_description = (ProRegularTextView) itemView.findViewById(R.id.tv_description);
        }
    }

    class ViewHolder2 extends RecyclerView.ViewHolder {
        ProRegularTextView tv_name, tv_description;
        View View_SplitHorLine;

        public ViewHolder2(View itemView) {
            super(itemView);
            tv_name = (ProRegularTextView) itemView.findViewById(R.id.tv_name);
            tv_description = (ProRegularTextView) itemView.findViewById(R.id.tv_description);
            View_SplitHorLine = itemView.findViewById(R.id.View_SplitHorLine);
        }
    }

    @Override
    public int getItemViewType(int position) {
        // Just as an example, return 0 or 2 depending on position
        // Note that unlike in ListView adapters, types don't have to be contiguous
        if (position < 2)
            return 0;
        else
            return 2;
    }

}