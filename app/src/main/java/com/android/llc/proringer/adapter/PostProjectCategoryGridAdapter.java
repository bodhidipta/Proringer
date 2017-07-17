package com.android.llc.proringer.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.android.llc.proringer.R;
import com.android.llc.proringer.pojo.ProCategoryData;
import com.android.llc.proringer.viewsmod.textview.ProRegularTextView;
import com.bumptech.glide.Glide;

import java.util.LinkedList;

/**
 * Created by bodhidipta on 15/06/17.
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

public class PostProjectCategoryGridAdapter extends RecyclerView.Adapter<PostProjectCategoryGridAdapter.ViewHolder> {
    private Context mcontext;
    private LinkedList<ProCategoryData> itemList;
    private onClickItem listener;


    public PostProjectCategoryGridAdapter(Context mcontext, LinkedList<ProCategoryData> itemList, onClickItem listener) {
        this.mcontext = mcontext;
        this.itemList = itemList;
        this.listener = listener;

    }

    @Override
    public PostProjectCategoryGridAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new ViewHolder(LayoutInflater.from(mcontext).inflate(R.layout.post_project_category_grid_row, parent, false));
    }

    @Override
    public void onBindViewHolder(PostProjectCategoryGridAdapter.ViewHolder holder, final int position) {


        holder.item_.setText(itemList.get(position).getCategory_name());
        Glide.with(mcontext).load(itemList.get(position).getCategory_image()).centerCrop().into(holder.image_);
        holder.image_.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onSelectItemClick(position, itemList.get(position));
            }
        });


    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ProRegularTextView item_;
        ImageView image_;

        public ViewHolder(View itemView) {
            super(itemView);
            item_ = (ProRegularTextView) itemView.findViewById(R.id.item_);
            image_ = (ImageView) itemView.findViewById(R.id.image);
        }
    }

    public void updateList(LinkedList<ProCategoryData> list) {
        itemList = list;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public interface onClickItem {
        void onSelectItemClick(int position, ProCategoryData data);
    }
}
