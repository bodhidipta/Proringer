package com.android.llc.proringer.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.android.llc.proringer.R;
import com.android.llc.proringer.helper.onItemClick;
import com.android.llc.proringer.pojo.ProjectMessageDetails;
import com.chauthai.swipereveallayout.SwipeRevealLayout;
import com.chauthai.swipereveallayout.ViewBinderHelper;

import java.util.ArrayList;

/**
 * Created by bodhidipta on 13/06/17.
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

public class ProjectDetailedMessageAdapter extends RecyclerView.Adapter<ProjectDetailedMessageAdapter.ViewHolder> {
    Context mcontext = null;
    private onItemClick listener;
    ArrayList<ProjectMessageDetails> projectMessageDetailsArrayList;
    private final ViewBinderHelper binderHelper = new ViewBinderHelper();

    public ProjectDetailedMessageAdapter(Context mcontext, ArrayList<ProjectMessageDetails> projectMessageDetailsArrayList, onItemClick callback) {
        this.mcontext = mcontext;
        this.projectMessageDetailsArrayList = projectMessageDetailsArrayList;
        listener = callback;
    }

    @Override
    public int getItemCount() {
        return projectMessageDetailsArrayList.size();
    }

    @Override
    public ProjectDetailedMessageAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(mcontext).inflate(R.layout.project_detailed_messge_row, parent, false));
    }

    @Override
    public void onBindViewHolder(ProjectDetailedMessageAdapter.ViewHolder holder, final int position) {

        if (projectMessageDetailsArrayList != null && 0 <= position && position < projectMessageDetailsArrayList.size()) {
            final String data = projectMessageDetailsArrayList.get(position).getTagName();

            // Use ViewBindHelper to restore and save the open/close state of the SwipeRevealView
            // put an unique string id as value, can be any string which uniquely define the data
            binderHelper.bind(holder.swipe_layout, data);

            // Bind your data here
            holder.bind(data);
        }


        if (position % 3 == 0) {
            holder.flag.setVisibility(View.VISIBLE);
        } else {
            holder.flag.setVisibility(View.INVISIBLE);
        }
        holder.main_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(position);

            }
        });
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        View flag;
        SwipeRevealLayout swipe_layout;
        RelativeLayout main_container;
        LinearLayout LLDelete, LLMore;

        public ViewHolder(View itemView) {
            super(itemView);
            flag = (View) itemView.findViewById(R.id.flag);
            LLMore = (LinearLayout) itemView.findViewById(R.id.LLMore);
            swipe_layout = (SwipeRevealLayout) itemView.findViewById(R.id.swipe_layout);
            main_container = (RelativeLayout) itemView.findViewById(R.id.main_container);
            LLDelete = (LinearLayout) itemView.findViewById(R.id.LLDelete);
        }

        public void bind(String data) {
            LLDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    projectMessageDetailsArrayList.remove(getAdapterPosition());
                    notifyItemRemoved(getAdapterPosition());
                }
            });

            //textView.setText(data);
        }
    }


    /**
     * Only if you need to restore open/close state when the orientation is changed.
     * Call this method in {@link android.app.Activity#onSaveInstanceState(Bundle)}
     */
    public void saveStates(Bundle outState) {
        binderHelper.saveStates(outState);
    }

    /**
     * Only if you need to restore open/close state when the orientation is changed.
     * Call this method in {@link android.app.Activity#onRestoreInstanceState(Bundle)}
     */
    public void restoreStates(Bundle inState) {
        binderHelper.restoreStates(inState);
    }
}