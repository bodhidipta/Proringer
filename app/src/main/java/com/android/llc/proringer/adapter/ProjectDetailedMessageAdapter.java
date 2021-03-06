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
import android.widget.Toast;

import com.android.llc.proringer.R;
import com.android.llc.proringer.activities.LandScreenActivity;
import com.android.llc.proringer.appconstant.ProApplication;
import com.android.llc.proringer.fragments.mainContent.ProjectMessagingFragment;
import com.android.llc.proringer.helper.CustomAlert;
import com.android.llc.proringer.helper.MyCustomAlertListener;
import com.android.llc.proringer.helper.MyLoader;
import com.android.llc.proringer.helper.ProServiceApiHelper;
import com.android.llc.proringer.pojo.SetGetProjectMessageDetailsData;
import com.android.llc.proringer.utils.Logger;
import com.android.llc.proringer.viewsmod.textview.ProRegularTextView;
import com.android.llc.proringer.viewsmod.textview.ProSemiBoldTextView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.chauthai.swipereveallayout.SwipeRevealLayout;
import com.chauthai.swipereveallayout.ViewBinderHelper;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

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

public class ProjectDetailedMessageAdapter extends RecyclerView.Adapter<ProjectDetailedMessageAdapter.ViewHolder> implements MyCustomAlertListener {
    Context mcontext = null;
    ProjectMessagingFragment.onOptionSelected callback;
    ArrayList<SetGetProjectMessageDetailsData> setGetProjectMessageDetailsDataArrayList;
    private final ViewBinderHelper binderHelper = new ViewBinderHelper();
    int delete_position = 0;
    MyLoader myLoader;

    public ProjectDetailedMessageAdapter(Context mcontext, ArrayList<SetGetProjectMessageDetailsData> setGetProjectMessageDetailsDataArrayList, ProjectMessagingFragment.onOptionSelected callback) {
        this.mcontext = mcontext;
        this.setGetProjectMessageDetailsDataArrayList = setGetProjectMessageDetailsDataArrayList;
        this.callback = callback;
        myLoader = new MyLoader(mcontext);
    }

    @Override
    public int getItemCount() {
        return setGetProjectMessageDetailsDataArrayList.size();
    }

    @Override
    public ProjectDetailedMessageAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(mcontext).inflate(R.layout.project_detailed_messge_row, parent, false));
    }

    @Override
    public void onBindViewHolder(ProjectDetailedMessageAdapter.ViewHolder holder, final int position) {

        if (setGetProjectMessageDetailsDataArrayList != null && 0 <= position && position < setGetProjectMessageDetailsDataArrayList.size()) {

            SetGetProjectMessageDetailsData setGetProjectMessageDetailsData = setGetProjectMessageDetailsDataArrayList.get(position);

            // Use ViewBindHelper to restore and save the open/close state of the SwipeRevealView
            // put an unique string id as value, can be any string which uniquely define the data
            binderHelper.bind(holder.swipe_layout, setGetProjectMessageDetailsData.getId());

            // Bind your data here
            holder.bind(setGetProjectMessageDetailsData);
        }


        if (setGetProjectMessageDetailsDataArrayList.get(position).getRead_status() == 1) {
            holder.main_container.setBackground(mcontext.getResources().getDrawable(R.drawable.vertical_line_bg));
        } else {
            holder.main_container.setBackground(mcontext.getResources().getDrawable(R.color.colorBGblueShade));
        }

        holder.img_attached.setVisibility(View.GONE);

        holder.main_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (setGetProjectMessageDetailsDataArrayList.get(position).getNo_of_msg() > 0) {
                    callback.onItemPassed(position, "");
                }
            }
        });
    }

    @Override
    public void callbackForAlert(String result, int i) {

        Logger.printMessage("result-->", result);
        Logger.printMessage("i-->", String.valueOf(i));
        if (result.equalsIgnoreCase("Ok") && i == 1) {

            ProServiceApiHelper.getInstance((LandScreenActivity) mcontext).deleteMessageListAPI(new ProServiceApiHelper.getApiProcessCallback() {
                @Override
                public void onStart() {
                    myLoader.showLoader();
                }

                @Override
                public void onComplete(String message) {
                    try {
                        JSONObject jsonObject = new JSONObject(message);
                        Toast.makeText(mcontext, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();

                        setGetProjectMessageDetailsDataArrayList.remove(delete_position);
                        notifyItemRemoved(delete_position);
                        myLoader.dismissLoader();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onError(String error) {
                    myLoader.dismissLoader();
                }
            }, ProApplication.getInstance().getUserId(), setGetProjectMessageDetailsDataArrayList.get(delete_position).getProject_id(), setGetProjectMessageDetailsDataArrayList.get(delete_position).getPro_id());

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

        public void bind(SetGetProjectMessageDetailsData setGetProjectMessageDetailsData) {


            LLDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CustomAlert customAlert = new CustomAlert(mcontext, "Delete", "Are you sure you want to delete this conversation?", ProjectDetailedMessageAdapter.this);
                    customAlert.createNormalAlert("ok", 1);
                    delete_position = getAdapterPosition();
                }
            });

            tv_name.setText(setGetProjectMessageDetailsData.getPro_com_nm());
            tv_date.setText(setGetProjectMessageDetailsData.getPro_time_status());
            tv_description.setText(setGetProjectMessageDetailsData.getMessage_info());

//            Glide.with(mcontext).load(setGetProjectMessageDetailsData.getPro_img())
//                    .placeholder(R.drawable.plumber)
//                    .into(new GlideDrawableImageViewTarget(prof_img) {
//                        /**
//                         * {@inheritDoc}
//                         * If no {@link GlideAnimation} is given or if the animation does not set the
//                         * {@link Drawable} on the view, the drawable is set using
//                         * {@link ImageView#setImageDrawable(Drawable)}.
//                         *
//                         * @param resource  {@inheritDoc}
//                         * @param animation {@inheritDoc}
//                         */
//                        @Override
//                        public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> animation) {
//                            super.onResourceReady(resource, animation);
//                        }
//                    });

            Picasso.with(mcontext).load(setGetProjectMessageDetailsData.getPro_img()).into(prof_img);

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