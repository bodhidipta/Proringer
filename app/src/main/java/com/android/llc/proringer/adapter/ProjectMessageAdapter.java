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
import com.android.llc.proringer.fragments.bottomNav.MessageFragment;
import com.android.llc.proringer.pojo.ProjectMessage;
import com.android.llc.proringer.utils.MethodsUtils;
import com.android.llc.proringer.viewsmod.textview.ProLightTextView;
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
    MessageFragment.onOptionSelected callback;
    ArrayList<ProjectMessage> projectMessageArrayList;
    private final ViewBinderHelper binderHelper = new ViewBinderHelper();

    public ProjectMessageAdapter(Context mcontext, ArrayList<ProjectMessage> projectMessageArrayList, MessageFragment.onOptionSelected callback) {
        this.mcontext = mcontext;
        this.callback = callback;
        this.projectMessageArrayList = projectMessageArrayList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(mcontext).inflate(R.layout.message_row, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        if (position == projectMessageArrayList.size() - 1) {
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(0, 0, 0, new MethodsUtils().dpToPx(mcontext, 10));
            holder.swipe_layout.setLayoutParams(params);
        }

        if (projectMessageArrayList != null && 0 <= position && position < projectMessageArrayList.size()) {
            ProjectMessage projectMessage = projectMessageArrayList.get(position);

            // Use ViewBindHelper to restore and save the open/close state of the SwipeRevealView
            // put an unique string id as value, can be any string which uniquely define the data
            binderHelper.bind(holder.swipe_layout, projectMessage.getProj_id());

            // Bind your data here
            holder.bind(projectMessage, position);
        }

        if (position % 3 == 0) {
            holder.main_container.setBackground(mcontext.getResources().getDrawable(R.drawable.vertical_line_bg));
        } else {
            holder.main_container.setBackground(mcontext.getResources().getDrawable(R.color.colorBGblueShade));
        }

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
        ProSemiBoldTextView project_name, status;
        ProRegularTextView date;
        ProLightTextView name_convo;
        ImageView project_type_img;


        public ViewHolder(View itemView) {
            super(itemView);
            flag = (View) itemView.findViewById(R.id.flag);
            swipe_layout = (SwipeRevealLayout) itemView.findViewById(R.id.swipe_layout);
            main_container = (RelativeLayout) itemView.findViewById(R.id.main_container);
            delete_layout = (LinearLayout) itemView.findViewById(R.id.delete_layout);

            project_type_img = (ImageView) itemView.findViewById(R.id.project_type_img);
            project_name = (ProSemiBoldTextView) itemView.findViewById(R.id.project_name);
            status = (ProSemiBoldTextView) itemView.findViewById(R.id.status);
            date = (ProRegularTextView) itemView.findViewById(R.id.date);
            name_convo = (ProLightTextView) itemView.findViewById(R.id.name_convo);
        }


        void bind(final ProjectMessage projectMessage, final int position) {

            main_container.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    callback.onItemPassed(position, projectMessage.getProj_id());
                }
            });


            delete_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    projectMessageArrayList.remove(getAdapterPosition());
                    notifyItemRemoved(getAdapterPosition());
                }
            });

            Glide.with(mcontext).load(projectMessage.getProj_image()).fitCenter().placeholder(R.drawable.plumber).into(new GlideDrawableImageViewTarget(project_type_img) {
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
            project_name.setText(projectMessage.getProj_name());

            if (projectMessage.getNo_of_pros_user().equalsIgnoreCase("0")){
                name_convo.setText("No Conversions");
            }
            else {
                name_convo.setText(projectMessage.getNo_of_pros_user()+" "+"Conversions");
            }

            if (projectMessage.getStatus().equalsIgnoreCase("Y")){
                status.setVisibility(View.VISIBLE);
                status.setText("Active");
            }else {
                status.setVisibility(View.GONE);
            }


            date.setText(projectMessage.getProject_date());
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