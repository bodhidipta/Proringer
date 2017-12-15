package com.android.llc.proringer.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.android.llc.proringer.R;
import com.android.llc.proringer.activities.FacebookActivity;
import com.android.llc.proringer.fragments.main_content.ProjectMessagingFragment;
import com.android.llc.proringer.helper.CustomAlert;
import com.android.llc.proringer.helper.MyCustomAlertListener;
import com.android.llc.proringer.pojo.ProjectMessageDetails;
import com.android.llc.proringer.viewsmod.textview.ProRegularTextView;
import com.android.llc.proringer.viewsmod.textview.ProSemiBoldTextView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
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

public class ProjectDetailedMessageAdapter extends RecyclerView.Adapter<ProjectDetailedMessageAdapter.ViewHolder> implements MyCustomAlertListener{
    Context mcontext = null;
    ProjectMessagingFragment.onOptionSelected callback;
    ArrayList<ProjectMessageDetails> projectMessageDetailsArrayList;
    private final ViewBinderHelper binderHelper = new ViewBinderHelper();
    int deletePos=0;

    public ProjectDetailedMessageAdapter(Context mcontext, ArrayList<ProjectMessageDetails> projectMessageDetailsArrayList, ProjectMessagingFragment.onOptionSelected callback) {
        this.mcontext = mcontext;
        this.projectMessageDetailsArrayList = projectMessageDetailsArrayList;
        this.callback = callback;
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

            ProjectMessageDetails projectMessageDetails = projectMessageDetailsArrayList.get(position);

            // Use ViewBindHelper to restore and save the open/close state of the SwipeRevealView
            // put an unique string id as value, can be any string which uniquely define the data
            binderHelper.bind(holder.swipe_layout, projectMessageDetails.getId());

            // Bind your data here
            holder.bind(projectMessageDetails);
        }


        if (projectMessageDetailsArrayList.get(position).getRead_status()==0) {
            holder.main_container.setBackground(mcontext.getResources().getDrawable(R.drawable.vertical_line_bg));
        } else {
        holder.main_container.setBackground(mcontext.getResources().getDrawable(R.color.colorBGblueShade));
        }

        holder.img_attached.setVisibility(View.GONE);

        holder.main_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (projectMessageDetailsArrayList.get(position).getNo_of_msg() > 0) {
                    callback.onItemPassed(position, "");
                }
            }
        });
    }

    @Override
    public void callbackForAlert(String result, int i) {
        if (result.equalsIgnoreCase("ok")){
            if (i==0){
                projectMessageDetailsArrayList.remove(deletePos);
                notifyItemRemoved(deletePos);
            }
        }
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        View flag;
        SwipeRevealLayout swipe_layout;
        RelativeLayout main_container;
        LinearLayout LLDelete, LLMore;
        ImageView img_attached, prof_img;
        ProRegularTextView tv_description, tv_date;
        ProSemiBoldTextView tv_name;

        public ViewHolder(View itemView) {
            super(itemView);
            flag = (View) itemView.findViewById(R.id.flag);
            LLMore = (LinearLayout) itemView.findViewById(R.id.LLMore);
            swipe_layout = (SwipeRevealLayout) itemView.findViewById(R.id.swipe_layout);
            main_container = (RelativeLayout) itemView.findViewById(R.id.main_container);
            LLDelete = (LinearLayout) itemView.findViewById(R.id.LLDelete);
            prof_img = (ImageView) itemView.findViewById(R.id.prof_img);
            img_attached = (ImageView) itemView.findViewById(R.id.img_attached);
            tv_description = (ProRegularTextView) itemView.findViewById(R.id.tv_description);
            tv_date = (ProRegularTextView) itemView.findViewById(R.id.tv_date);
            tv_name = (ProSemiBoldTextView) itemView.findViewById(R.id.tv_name);
        }

        public void bind(ProjectMessageDetails projectMessageDetails) {


            LLDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CustomAlert customAlert = new CustomAlert(mcontext, "Delete", "Are you sure you want to delete this conversation?",ProjectDetailedMessageAdapter.this );
                    customAlert.createNormalAlert("ok", 1);
                    deletePos=getAdapterPosition();
                }
            });

            tv_name.setText(projectMessageDetails.getPro_com_nm());
            tv_date.setText(projectMessageDetails.getPro_time_status());
            tv_description.setText(projectMessageDetails.getMessage_info());

            Glide.with(mcontext).load(projectMessageDetails.getPro_img())
                    .placeholder(R.drawable.plumber)
                    .into(new GlideDrawableImageViewTarget(prof_img) {
                /**
                 * {@inheritDoc}
                 * If no {@link GlideAnimation} is given or if the animation does not set the
                 * {@link Drawable} on the view, the drawable is set using
                 * {@link ImageView#setImageDrawable(Drawable)}.
                 *
                 * @param resource  {@inheritDoc}
                 * @param animation {@inheritDoc}
                 */
                @Override
                public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> animation) {
                    super.onResourceReady(resource, animation);
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