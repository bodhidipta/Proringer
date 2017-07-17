package com.android.llc.proringer.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.android.llc.proringer.R;
import com.android.llc.proringer.fragments.bottomNav.MyProjects;
import com.android.llc.proringer.pojo.ProjectPostedData;
import com.android.llc.proringer.viewsmod.textview.ProRegularTextView;

import java.util.List;

/**
 * Created by manishsethia on 12/07/17.
 */

public class ProjectListingAdapter extends RecyclerView.Adapter<ProjectListingAdapter.ViewHolder> {
    private Context mcontext = null;
    private List<ProjectPostedData> itemList;
    MyProjects.onOptionSelected callback;

    public ProjectListingAdapter(Context mcontext, List<ProjectPostedData> itemList,MyProjects.onOptionSelected callback) {
        this.mcontext = mcontext;
        this.itemList = itemList;
        this.callback=callback;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(mcontext).inflate(R.layout.content_my_project_row, parent, false));
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.job_status.setVisibility(View.INVISIBLE);
        holder.messages.setVisibility(View.INVISIBLE);
        holder.pro_hired.setVisibility(View.INVISIBLE);
        holder.no_pro_hired.setVisibility(View.INVISIBLE);
        holder.lebel_hireing_offr.setVisibility(View.INVISIBLE);
        holder.review_pro.setVisibility(View.INVISIBLE);
        holder.right_chevron.setVisibility(View.INVISIBLE);



        if (position==0){
         holder.start_project.setVisibility(View.VISIBLE);
        }else{
            holder.start_project.setVisibility(View.GONE);

        }

        if (itemList.get(position).isStatusActive()) {
            holder.job_status.setVisibility(View.VISIBLE);
            holder.job_status.setBackgroundResource(R.drawable.bg_oval_blue_text);
            holder.job_status.setText("ACTIVE");

            holder.right_chevron.setVisibility(View.VISIBLE);

        } else {
            holder.job_status.setVisibility(View.VISIBLE);
            holder.job_status.setBackgroundResource(R.drawable.bg_oval_grey_text);
            holder.job_status.setText("EXPIRED");

            if (!itemList.get(position).isAccepted()){
                holder.pro_hired.setVisibility(View.VISIBLE);
                holder.no_pro_hired.setVisibility(View.VISIBLE);

                holder.lebel_hireing_offr.setVisibility(View.VISIBLE);

            }
        }

        if (itemList.get(position).isMessages()){
            holder.messages.setVisibility(View.VISIBLE);
        }else {
            holder.messages.setVisibility(View.INVISIBLE);
        }

        if (itemList.get(position).isAccepted()){
            holder.job_status.setVisibility(View.VISIBLE);
            holder.job_status.setBackgroundResource(R.drawable.bg_oval_green_text);
            holder.job_status.setText("ACCEPTED");

            holder.review_pro.setVisibility(View.VISIBLE);
        }
        holder.totalView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callback.onItemPassed(position,"value shift to details page");
            }
        });



    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ProRegularTextView job_status, messages, pro_hired, no_pro_hired, lebel_hireing_offr, review_pro;
        ImageView right_chevron;
        LinearLayout start_project;
        View totalView;

        public ViewHolder(View itemView) {
            super(itemView);
            start_project=(LinearLayout)itemView.findViewById(R.id.start_project);
            job_status = (ProRegularTextView) itemView.findViewById(R.id.job_status);
            messages = (ProRegularTextView) itemView.findViewById(R.id.messages);
            pro_hired = (ProRegularTextView) itemView.findViewById(R.id.pro_hired);
            no_pro_hired = (ProRegularTextView) itemView.findViewById(R.id.no_pro_hired);
            lebel_hireing_offr = (ProRegularTextView) itemView.findViewById(R.id.lebel_hireing_offr);
            review_pro = (ProRegularTextView) itemView.findViewById(R.id.review_pro);
            right_chevron = (ImageView) itemView.findViewById(R.id.right_chevron);
            totalView=itemView;
        }
    }
}
