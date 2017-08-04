package com.android.llc.proringer.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import com.android.llc.proringer.R;
import com.android.llc.proringer.activities.ProsReviewAllListActivity;
import com.android.llc.proringer.viewsmod.textview.ProRegularTextView;
import com.bumptech.glide.Glide;
import org.json.JSONArray;
import org.json.JSONException;

/**
 * Created by su on 8/4/17.
 */

public class ProsReviewAllAdapter extends RecyclerView.Adapter<ProsReviewAllAdapter.MyViewHolder> {
    Context context;
    JSONArray jsonInfoArray;

    public ProsReviewAllAdapter(Context context,JSONArray jsonInfoArray){
        this.context=context;
        this.jsonInfoArray=jsonInfoArray;

    }
    @Override
    public ProsReviewAllAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_pros_review_all, parent, false);
        return new ProsReviewAllAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ProsReviewAllAdapter.MyViewHolder holder, int position) {
        try {

            if (!jsonInfoArray.getJSONObject(position).getString("profile_img").equals(""))
                Glide.with(context).load(jsonInfoArray.getJSONObject(position).getString("profile_image")).centerCrop().into(holder.img_profile);

            holder.rbar.setRating(Float.parseFloat(jsonInfoArray.getJSONObject(position).getString("avg_rating")));

            holder.tv_name.setText(jsonInfoArray.getJSONObject(position).getString("homeowner_name"));
            holder.tv_review_date.setText(jsonInfoArray.getJSONObject(position).getString("date_time"));

            holder.tv_report.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });
            holder.tv_review_comment.setText(jsonInfoArray.getJSONObject(position).getString("rater_description"));


            if (jsonInfoArray.getJSONObject(position).getInt("review_reply")==0){
                holder.CardViewReply.setVisibility(View.GONE);
            }
            else {
                holder.CardViewReply.setVisibility(View.VISIBLE);
                holder.tv_review_reply_by.setText("hbfdjnfdjnkdf");
                holder.tv_review_reply_on_date.setText(jsonInfoArray.getJSONObject(position).getString("review_reply_date"));
                holder.tv_review_reply.setText(jsonInfoArray.getJSONObject(position).getString("review_reply_content"));
            }

            if(position==jsonInfoArray.length()-1){
                ((ProsReviewAllListActivity)context).loadReviewList(jsonInfoArray.length(),10);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return jsonInfoArray.length();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ProRegularTextView tv_name,tv_review_date,tv_report,tv_review_comment,tv_review_reply,tv_review_reply_by,tv_review_reply_on_date;
        RatingBar rbar;
        ImageView img_profile;
        CardView CardViewReply;
        public MyViewHolder(View itemView) {
            super(itemView);

            img_profile= (ImageView) itemView.findViewById(R.id.img_profile);

            tv_name= (ProRegularTextView) itemView.findViewById(R.id.tv_name);
            tv_review_date= (ProRegularTextView) itemView.findViewById(R.id.tv_review_date);
            tv_report= (ProRegularTextView) itemView.findViewById(R.id.tv_report);
            tv_review_comment= (ProRegularTextView) itemView.findViewById(R.id.tv_review_comment);

            CardViewReply= (CardView) itemView.findViewById(R.id.CardViewReply);

            tv_review_reply= (ProRegularTextView) itemView.findViewById(R.id.tv_review_reply);
            tv_review_reply_by= (ProRegularTextView) itemView.findViewById(R.id.tv_review_reply_by);
            tv_review_reply_on_date= (ProRegularTextView) itemView.findViewById(R.id.tv_review_reply_on_date);

            rbar= (RatingBar) itemView.findViewById(R.id.rbar);
        }
    }

    public void  NotifyMeInLazzyLoad(JSONArray jsonInfoArray){
        this.jsonInfoArray=jsonInfoArray;
        notifyDataSetChanged();
    }
}
