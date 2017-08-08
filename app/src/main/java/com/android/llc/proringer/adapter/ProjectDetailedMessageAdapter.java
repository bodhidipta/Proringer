package com.android.llc.proringer.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.llc.proringer.R;
import com.android.llc.proringer.helper.OnSwipeTouchListener;
import com.android.llc.proringer.helper.onItemClick;
import com.android.llc.proringer.pojo.ProjectMessageDetails;

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
    ArrayList<ProjectMessageDetails> projectMessageDetailsArrayList;
    private onItemClick listener;
    DisplayMetrics displayMetrics;

    public ProjectDetailedMessageAdapter(Context mcontext,ArrayList<ProjectMessageDetails> projectMessageDetailsArrayList, onItemClick callback) {
        this.mcontext = mcontext;
        this.projectMessageDetailsArrayList=projectMessageDetailsArrayList;
        listener = callback;
        displayMetrics = mcontext.getResources().getDisplayMetrics();
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

        holder.RLMain_container.getLayoutParams().width =displayMetrics.widthPixels;

        if (position % 3 == 0) {
            holder.flag.setVisibility(View.VISIBLE);
        } else {
            holder.flag.setVisibility(View.INVISIBLE);
        }
        holder.RLMain_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(position);
            }
        });

        if(!projectMessageDetailsArrayList.get(position).isOpen()){

            //holder.horizontalScrollView.invalidate();
            holder.horizontalScrollView.fullScroll(HorizontalScrollView.FOCUS_LEFT);
        }
        else {
            holder.horizontalScrollView.fullScroll(HorizontalScrollView.FOCUS_RIGHT);
        }


        holder.horizontalScrollView.setOnTouchListener(new OnSwipeTouchListener((mcontext)) {
            public void onSwipeTop() {
                Toast.makeText(mcontext, "top", Toast.LENGTH_SHORT).show();
            }
            public void onSwipeRight() {
                Toast.makeText(mcontext, "right", Toast.LENGTH_SHORT).show();
            }
            public void onSwipeLeft() {
                Toast.makeText(mcontext, "left", Toast.LENGTH_SHORT).show();
            }
            public void onSwipeBottom() {
                Toast.makeText(mcontext, "bottom", Toast.LENGTH_SHORT).show();
            }

        });
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout more;
        RelativeLayout RLMain_container;
        LinearLayout LLDelete;
        View flag;
        HorizontalScrollView horizontalScrollView;

        public ViewHolder(View itemView) {
            super(itemView);
            flag = (View) itemView.findViewById(R.id.flag);
            more = (LinearLayout) itemView.findViewById(R.id.more);
            RLMain_container = (RelativeLayout) itemView.findViewById(R.id.RLMain_container);
            LLDelete = (LinearLayout) itemView.findViewById(R.id.LLDelete);
            horizontalScrollView= (HorizontalScrollView) itemView.findViewById(R.id.scrollview);
        }
    }
}
