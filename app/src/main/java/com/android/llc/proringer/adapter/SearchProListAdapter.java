package com.android.llc.proringer.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import com.android.llc.proringer.R;
import com.android.llc.proringer.activities.ProProjectDetailsActivity;
import com.android.llc.proringer.fragments.drawerNav.SearchLocalProFragment;
import com.android.llc.proringer.utils.Logger;
import com.android.llc.proringer.viewsmod.textview.ProRegularTextView;
import com.android.llc.proringer.viewsmod.textview.ProSemiBoldTextView;
import com.bumptech.glide.Glide;
import org.json.JSONArray;
import org.json.JSONException;

/**
 * Created by bodhidipta on 21/06/17.
 * <!-- * Copyright (c) 2017, The Proringer-->
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

public class SearchProListAdapter extends RecyclerView.Adapter<SearchProListAdapter.ViewHolder> {
    private Context mcontext = null;
    JSONArray jsonInfoArray;
    SearchLocalProFragment.onOptionSelected callback;


    public SearchProListAdapter(Context mcontext, JSONArray jsonInfoArray,SearchLocalProFragment.onOptionSelected callback ) {
        this.mcontext = mcontext;
        this.jsonInfoArray = jsonInfoArray;
        this.callback=callback;
    }

    @Override
    public int getItemCount() {
        return jsonInfoArray.length();
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.ratingBar.setMax(5);
        try {
            holder.ratingBar.setRating(Float.valueOf(jsonInfoArray.getJSONObject(position).getString("pro_avg_review_rating")));
            holder.tv_pros_company_name.setText(jsonInfoArray.getJSONObject(position).getString("pros_company_name"));
            holder.tv_category_name.setText(jsonInfoArray.getJSONObject(position).getString("category_name"));
            holder.tv_address.setText(jsonInfoArray.getJSONObject(position).getString("city")
                    + "," + jsonInfoArray.getJSONObject(position).getString("state"));

            holder.tv_total_review.setText("(" + jsonInfoArray.getJSONObject(position).getString("total_review") + ")");


            /**
             * Read more spannable text with click listener
             */
            String contactTextOne = jsonInfoArray.getJSONObject(position).getString("description");
            Logger.printMessage("contactTextOne", "" + contactTextOne);
            String contactTextClick = " Read more..";


            Spannable word1 = new SpannableString(contactTextOne);
            word1.setSpan(new ForegroundColorSpan(mcontext.getResources().getColor(R.color.colorTextDark)), 0, contactTextOne.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            if (word1.length() > 80) {
                holder.tv_description.setText(word1.subSequence(1, 60));

                Spannable word2 = new SpannableString(contactTextClick);
                ClickableSpan myClickableSpan = new ClickableSpan() {
                    @Override
                    public void onClick(View widget) {
                        // There is the OnCLick. put your intent to Register class here
                        widget.invalidate();
                        Logger.printMessage("SpanHello", "click");
                    }

                    @Override
                    public void updateDrawState(TextPaint ds) {
                        ds.setColor(mcontext.getResources().getColor(R.color.colorAccent));
                        ds.setUnderlineText(false);
                    }
                };
                word2.setSpan(myClickableSpan, 0, contactTextClick.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                holder.tv_description.append(word2);
            }

            if (!jsonInfoArray.getJSONObject(position).getString("profile_img").equals(""))
                Glide.with(mcontext).load(jsonInfoArray.getJSONObject(position).getString("profile_img")).centerCrop().into(holder.img_project);


            if (jsonInfoArray.getJSONObject(position).getString("pros_verify").equalsIgnoreCase("Y")) {
                holder.img_verify.setVisibility(View.VISIBLE);
                holder.img_verify_tick.setVisibility(View.VISIBLE);
                holder.tv_verify.setVisibility(View.VISIBLE);
                holder.linear_layout_border.setBackgroundResource(R.drawable.background_with_orange_border);
            } else {
                holder.img_verify.setVisibility(View.GONE);
                holder.img_verify_tick.setVisibility(View.GONE);
                holder.tv_verify.setVisibility(View.GONE);
                holder.linear_layout_border.setBackgroundResource(R.drawable.background_with_white_border);
            }

            if (jsonInfoArray.getJSONObject(position).getString("pro_status").equalsIgnoreCase("Y")) {
                holder.img_favorite.setImageResource(R.drawable.ic_favorite);
            } else {
                holder.img_favorite.setImageResource(R.drawable.ic_un_favorite);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


        holder.main_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mcontext, ProProjectDetailsActivity.class);
                mcontext.startActivity(intent);
            }
        });

        holder.img_favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    callback.onItemPassed(jsonInfoArray.getJSONObject(position).getString("pros_id"),jsonInfoArray.getJSONObject(position).getString("pro_status"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(mcontext).inflate(R.layout.search_pro_list_rowitem, parent, false));
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout linear_layout_border;
        RatingBar ratingBar;
        CardView main_container;
        ProSemiBoldTextView tv_pros_company_name;
        ProRegularTextView tv_category_name, tv_address, tv_verify, tv_total_review, tv_description;
        ImageView img_project, img_verify, img_verify_tick, img_favorite;

        public ViewHolder(View itemView) {
            super(itemView);
            ratingBar = (RatingBar) itemView.findViewById(R.id.ratingPro);

            main_container = (CardView) itemView.findViewById(R.id.main_container);

            tv_pros_company_name = (ProSemiBoldTextView) itemView.findViewById(R.id.tv_pros_company_name);

            tv_category_name = (ProRegularTextView) itemView.findViewById(R.id.tv_category_name);
            tv_total_review = (ProRegularTextView) itemView.findViewById(R.id.tv_total_review);
            tv_address = (ProRegularTextView) itemView.findViewById(R.id.tv_address);
            tv_verify = (ProRegularTextView) itemView.findViewById(R.id.tv_verify);

            tv_description = (ProRegularTextView) itemView.findViewById(R.id.tv_description);
            tv_description.setMovementMethod(LinkMovementMethod.getInstance());


            img_project = (ImageView) itemView.findViewById(R.id.img_project);
            img_verify = (ImageView) itemView.findViewById(R.id.img_verify);
            img_verify_tick = (ImageView) itemView.findViewById(R.id.img_verify_tick);
            img_favorite = (ImageView) itemView.findViewById(R.id.img_favorite);

            linear_layout_border = (LinearLayout) itemView.findViewById(R.id.linear_layout_border);
        }
    }

    public void refreshData(JSONArray jsonInfoArray){
        this.jsonInfoArray=jsonInfoArray;
        notifyDataSetChanged();
    }
}
