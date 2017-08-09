package com.android.llc.proringer.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import com.android.llc.proringer.R;
import com.android.llc.proringer.helper.onItemClick;
import com.android.llc.proringer.pojo.ProjectMessage;
import com.chauthai.swipereveallayout.SwipeRevealLayout;
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

    public ProjectMessageAdapter(Context mcontext,ArrayList<ProjectMessage> projectMessageArrayList, onItemClick calback) {
        this.mcontext = mcontext;
        listener = calback;
        this.projectMessageArrayList=projectMessageArrayList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(mcontext).inflate(R.layout.message_row, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
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
        return projectMessageArrayList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        View flag;
        SwipeRevealLayout swipe_layout;
        RelativeLayout main_container;

        public ViewHolder(View itemView) {
            super(itemView);
            flag = (View) itemView.findViewById(R.id.flag);
            swipe_layout = (SwipeRevealLayout) itemView.findViewById(R.id.swipe_layout);
            main_container = (RelativeLayout) itemView.findViewById(R.id.main_container);
        }
    }
}