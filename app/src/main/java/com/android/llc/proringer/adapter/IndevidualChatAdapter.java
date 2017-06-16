package com.android.llc.proringer.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.android.llc.proringer.R;
import com.android.llc.proringer.pojo.ChatPojo;
import com.android.llc.proringer.viewsmod.textview.ProRegularTextView;
import com.bumptech.glide.Glide;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;

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

public class IndevidualChatAdapter extends RecyclerView.Adapter<IndevidualChatAdapter.ViewHolder> {
    private Context mcontext;
    private LinkedList<ChatPojo> dataList;

    public IndevidualChatAdapter(Context mcontext, LinkedList<ChatPojo> list) {
        this.mcontext = mcontext;
        dataList = list;
    }

    @Override
    public IndevidualChatAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(mcontext).inflate(R.layout.indevidual_list_row, parent, false));
    }

    @Override
    public void onBindViewHolder(IndevidualChatAdapter.ViewHolder holder, int position) {
        if (dataList.get(position).isDateVisible()) {
            holder.date_header.setVisibility(View.VISIBLE);

            if (dataList.get(position).getDate().equals(new SimpleDateFormat("dd-MM-yyyy").format(new Date()))) {
                holder.date_header.setText("Today");
            } else {
                holder.date_header.setText(dataList.get(position).getDate());
            }
        } else {
            holder.date_header.setVisibility(View.GONE);
        }

        if (dataList.get(position).isSender()) {
            holder.receiver_image_cont.setVisibility(View.GONE);
            holder.receiver_message.setVisibility(View.GONE);


            if (dataList.get(position).ismessage()) {
                holder.sender_message.setVisibility(View.VISIBLE);
                holder.sender_image_cont.setVisibility(View.GONE);
            } else {
                holder.sender_image_cont.setVisibility(View.VISIBLE);
                holder.sender_message.setVisibility(View.GONE);
                Glide.with(mcontext).load(dataList.get(position).getImageLink()).fitCenter()
                        .into(holder.sender_image);
            }
        } else {
            holder.sender_image_cont.setVisibility(View.GONE);
            holder.sender_message.setVisibility(View.GONE);


            if (dataList.get(position).ismessage()) {
                holder.receiver_message.setVisibility(View.VISIBLE);
                holder.receiver_image_cont.setVisibility(View.GONE);
            } else {
                holder.receiver_image_cont.setVisibility(View.VISIBLE);
                holder.receiver_message.setVisibility(View.GONE);
            }
            Glide.with(mcontext).load(dataList.get(position).getImageLink()).centerCrop().into(holder.receiver_image);
        }

    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ProRegularTextView date_header, receiver_message, sender_message, receiver_image_date, sender_image_date;
        ImageView receiver_image, sender_image;
        RelativeLayout receiver_image_cont, sender_image_cont;

        public ViewHolder(View itemView) {
            super(itemView);
            date_header = (ProRegularTextView) itemView.findViewById(R.id.date_header);
            receiver_message = (ProRegularTextView) itemView.findViewById(R.id.receiver_message);
            sender_message = (ProRegularTextView) itemView.findViewById(R.id.sender_message);
            receiver_image_date = (ProRegularTextView) itemView.findViewById(R.id.receiver_image_date);
            sender_image_date = (ProRegularTextView) itemView.findViewById(R.id.sender_image_date);
            receiver_image = (ImageView) itemView.findViewById(R.id.receiver_image);
            sender_image = (ImageView) itemView.findViewById(R.id.sender_image);
            receiver_image_cont = (RelativeLayout) itemView.findViewById(R.id.receiver_image_cont);
            sender_image_cont = (RelativeLayout) itemView.findViewById(R.id.sender_image_cont);


        }
    }
}
