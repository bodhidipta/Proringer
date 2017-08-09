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
import com.android.llc.proringer.pojo.ProjectMessage;
import com.chauthai.swipereveallayout.SwipeRevealLayout;
import com.chauthai.swipereveallayout.ViewBinderHelper;

import java.util.ArrayList;

/**
 * Created by bodhidipta on 12/06/17.
 * <!-- * Copyright (c) 2017, Proringer -->
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

public class ProjectMessageAdapter extends RecyclerView.Adapter<ProjectMessageAdapter.ViewHolder> {
    private Context mcontext;
    private onItemClick listener;
    ArrayList<ProjectMessage> projectMessageArrayList;
    private final ViewBinderHelper binderHelper = new ViewBinderHelper();

    public ProjectMessageAdapter(Context mcontext, ArrayList<ProjectMessage> projectMessageArrayList, onItemClick calback) {
        this.mcontext = mcontext;
        listener = calback;
        this.projectMessageArrayList = projectMessageArrayList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(mcontext).inflate(R.layout.message_row, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        if (projectMessageArrayList != null && 0 <= position && position < projectMessageArrayList.size()) {
            final String data = projectMessageArrayList.get(position).getTagName();

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

    @Override
    public int getItemCount() {
        if (projectMessageArrayList == null) {
            return 0;
        } else {
            return projectMessageArrayList.size();
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        View flag;
        SwipeRevealLayout swipe_layout;
        RelativeLayout main_container;
        LinearLayout delete_layout;

        public ViewHolder(View itemView) {
            super(itemView);
            flag = (View) itemView.findViewById(R.id.flag);
            swipe_layout = (SwipeRevealLayout) itemView.findViewById(R.id.swipe_layout);
            main_container = (RelativeLayout) itemView.findViewById(R.id.main_container);
            delete_layout = (LinearLayout) itemView.findViewById(R.id.delete_layout);
        }


        public void bind(String data) {
            delete_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    projectMessageArrayList.remove(getAdapterPosition());
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