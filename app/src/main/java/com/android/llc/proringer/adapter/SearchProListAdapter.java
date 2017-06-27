package com.android.llc.proringer.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;

import com.android.llc.proringer.R;

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

    public SearchProListAdapter(Context mcontext) {
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

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(mcontext).inflate(R.layout.search_pro_list_rowitem, parent, false));
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        RatingBar ratingBar;

        public ViewHolder(View itemView) {
            super(itemView);
            ratingBar = (RatingBar) itemView.findViewById(R.id.ratingPro);

        }
    }
}
