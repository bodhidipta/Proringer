package com.android.llc.proringer.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.android.llc.proringer.R;
import com.android.llc.proringer.helper.DownloaderAndShowFile;
import com.android.llc.proringer.pojo.SetGetChatPojoData;
import com.android.llc.proringer.viewsmod.textview.ProRegularTextView;
import com.bumptech.glide.Glide;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

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

public class IndevidualChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mcontext;
    private List<SetGetChatPojoData> dataList;

    public IndevidualChatAdapter(Context mcontext, List<SetGetChatPojoData> list) {
        this.mcontext = mcontext;
        dataList = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ////change////////
        if (viewType == 1) {
            return new ViewHolderReceiver(LayoutInflater.from(mcontext).inflate(R.layout.indevidual_list_row_receiver, parent, false));

        } else {
            return new ViewHolderSender(LayoutInflater.from(mcontext).inflate(R.layout.indevidual_list_row_sender, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {



        switch (holder.getItemViewType()) {
            case 1:
                ViewHolderReceiver viewHolderReceiver = (ViewHolderReceiver) holder;
                // ProjectMessageSendAndRecv ggg=new ProjectMessageSendAndRecv(date);

                if (!dataList.get(position).getDate().trim().equals(""))
                {
                    viewHolderReceiver.date_header.setVisibility(View.VISIBLE);
                    if (dataList.get(position).getDate().equals(new SimpleDateFormat("yyyy-MM-dd").format(new Date()))) {
                        viewHolderReceiver.date_header.setText("Today");
                    } else {
                        viewHolderReceiver.date_header.setText(dataList.get(position).getDate());
                    }
                }else {
                    viewHolderReceiver.date_header.setVisibility(View.GONE);
                }



                /*else
                {
                    viewHolderReceiver.date_header.setVisibility(View.GONE);
                }*/
                if (dataList.get(position).getMsg_attachment().equals(""))
                {
                    viewHolderReceiver.receiver_image_cont.setVisibility(View.GONE);

                    viewHolderReceiver.receiver_message.setVisibility(View.VISIBLE);
                    viewHolderReceiver.receiver_message.setText(dataList.get(position).getMessage_info());

                }else {
                    viewHolderReceiver.receiver_message.setVisibility(View.GONE);
                    viewHolderReceiver.receiver_image_cont.setVisibility(View.VISIBLE);
                    Glide.with(mcontext).load(dataList.get(position).getMsg_attachment()).centerCrop().into(viewHolderReceiver.receiver_image);

                }

                viewHolderReceiver.receiver_image_download.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DownloaderAndShowFile.downloadAndOpenPDFOrImage(mcontext,dataList.get(position).getMsg_attachment());
                    }
                });

               /* if (dataList.get(position).isDateVisible()) {
                    viewHolderReceiver.date_header.setVisibility(View.VISIBLE);

                    if (dataList.get(position).getDate().equals(new SimpleDateFormat("dd-MM-yyyy").format(new Date()))) {
                        viewHolderReceiver.date_header.setText("Today");
                    } else {
                        viewHolderReceiver.date_header.setText(dataList.get(position).getDate());
                    }
                } else {
                    viewHolderReceiver.date_header.setVisibility(View.GONE);
                }

                if (dataList.get(position).ismessage()) {
                    viewHolderReceiver.receiver_message.setVisibility(View.VISIBLE);
                    viewHolderReceiver.receiver_image_cont.setVisibility(View.GONE);
                } else {
                    viewHolderReceiver.receiver_image_cont.setVisibility(View.VISIBLE);
                    viewHolderReceiver.receiver_message.setVisibility(View.GONE);
                }
                Glide.with(mcontext).load(dataList.get(position).getImageLink()).centerCrop().into(viewHolderReceiver.receiver_image);*/
                break;

            case 2:
                ViewHolderSender viewHolderSender = (ViewHolderSender) holder;

                // ProjectMessageSendAndRecv ggg1=new ProjectMessageSendAndRecv(date);

                if (!dataList.get(position).getDate().trim().equals("")){
                    viewHolderSender.date_header.setVisibility(View.VISIBLE);
                    if (dataList.get(position).getDate().equals(new SimpleDateFormat("yyyy-MM-dd").format(new Date()))) {
                        viewHolderSender.date_header.setText("Today");
                    } else {
                        viewHolderSender.date_header.setText(dataList.get(position).getDate());
                    }
                }else {
                    viewHolderSender.date_header.setVisibility(View.GONE);
                }


                    /*else
                    { viewHolderSender.date_header.setVisibility(View.GONE);

                    }*/



                if (dataList.get(position).getMsg_attachment().equals(""))
                {
                    viewHolderSender.sender_message.setVisibility(View.VISIBLE);
                    viewHolderSender.sender_message.setText(dataList.get(position).getMessage_info());
                    viewHolderSender.sender_image_cont.setVisibility(View.GONE);
                }else {
                    viewHolderSender.sender_image_cont.setVisibility(View.VISIBLE);
                    viewHolderSender.sender_message.setVisibility(View.GONE);
                    Glide.with(mcontext).load(dataList.get(position).getMsg_attachment()).fitCenter().into(viewHolderSender.sender_image);
                }

                viewHolderSender.sender_image_download.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DownloaderAndShowFile.downloadAndOpenPDFOrImage(mcontext,dataList.get(position).getMsg_attachment());
                    }
                });


              /*  if (dataList.get(position).isDateVisible()) {
                    viewHolderSender.date_header.setVisibility(View.VISIBLE);

                    if (dataList.get(position).getDate().equals(new SimpleDateFormat("dd-MM-yyyy").format(new Date()))) {
                        viewHolderSender.date_header.setText("Today");
                    } else {
                        viewHolderSender.date_header.setText(dataList.get(position).getDate());
                    }
                } else {
                    viewHolderSender.date_header.setVisibility(View.GONE);
                }


                if (dataList.get(position).ismessage()) {
                    viewHolderSender.sender_message.setVisibility(View.VISIBLE);
                    viewHolderSender.sender_image_cont.setVisibility(View.GONE);
                } else {
                    viewHolderSender.sender_image_cont.setVisibility(View.VISIBLE);
                    viewHolderSender.sender_message.setVisibility(View.GONE);
                    Glide.with(mcontext).load(dataList.get(position).getImageLink()).fitCenter()
                            .into(viewHolderSender.sender_image);
                }*/
                break;
        }
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    class ViewHolderReceiver extends RecyclerView.ViewHolder {
        ProRegularTextView date_header, receiver_message, receiver_image_date;
        RelativeLayout receiver_image_cont;
        ImageView receiver_image,receiver_image_download;

        public ViewHolderReceiver(View itemView) {
            super(itemView);
            date_header = (ProRegularTextView) itemView.findViewById(R.id.date_header);
            receiver_message = (ProRegularTextView) itemView.findViewById(R.id.receiver_message);
            receiver_image_date = (ProRegularTextView) itemView.findViewById(R.id.receiver_image_date);
            receiver_image_cont = (RelativeLayout) itemView.findViewById(R.id.receiver_image_cont);
            receiver_image = (ImageView) itemView.findViewById(R.id.receiver_image);
            receiver_image_download = (ImageView) itemView.findViewById(R.id.receiver_image_download);
        }
    }

    class ViewHolderSender extends RecyclerView.ViewHolder {
        ProRegularTextView date_header, sender_message, sender_image_date;
        ImageView sender_image,sender_image_download;
        RelativeLayout sender_image_cont;

        public ViewHolderSender(View itemView) {
            super(itemView);
            date_header = (ProRegularTextView) itemView.findViewById(R.id.date_header);
            sender_message = (ProRegularTextView) itemView.findViewById(R.id.sender_message);
            sender_image_date = (ProRegularTextView) itemView.findViewById(R.id.sender_image_date);
            sender_image = (ImageView) itemView.findViewById(R.id.sender_image);
            sender_image_download = (ImageView) itemView.findViewById(R.id.sender_image_download);
            sender_image_cont = (RelativeLayout) itemView.findViewById(R.id.sender_image_cont);

        }
    }


    @Override
    public int getItemViewType(int position) {
        if (dataList.get(position).getUser().equals("S")) {
            return 2;
        } else {
            return 1;
        }
    }
}