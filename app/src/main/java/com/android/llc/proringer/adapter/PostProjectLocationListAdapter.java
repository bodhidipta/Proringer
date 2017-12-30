package com.android.llc.proringer.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.llc.proringer.R;
import com.android.llc.proringer.pojo.SetGetAddressData;
import com.android.llc.proringer.viewsmod.textview.ProRegularTextView;

import java.util.List;

/**
 * Created by bodhidipta on 29/06/17.
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

public class PostProjectLocationListAdapter extends RecyclerView.Adapter<PostProjectLocationListAdapter.ViewHolder> {
    private Context mcontext;
    private List<SetGetAddressData> addressList;
    private onItemelcted listener;


    public PostProjectLocationListAdapter(Context mcontext, List<SetGetAddressData> addressList, onItemelcted call) {
        this.mcontext = mcontext;
        this.addressList = addressList;
        listener = call;
    }

    @Override
    public int getItemCount() {
        return addressList.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(mcontext).inflate(R.layout.place_child_list, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
//        if (selected_address.equals("")) {
//            holder.txtspinemwnu.setBackgroundColor(Color.parseColor("#ffffff"));
//            holder.txtspinemwnu.setTextColor(Color.parseColor("#333333"));
//        } else {
//            holder.txtspinemwnu.setBackgroundColor(Color.parseColor("#505050"));
//            holder.txtspinemwnu.setTextColor(Color.parseColor("#ffffff"));
//        }

        holder.txtspinemwnu.setBackgroundColor(Color.parseColor("#ffffff"));
        holder.txtspinemwnu.setTextColor(Color.parseColor("#333333"));

        holder.txtspinemwnu.setText(addressList.get(position).getFormatted_address());

        holder.txtspinemwnu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (addressList.get(position).getCountry_code().equals("US") ||
                        addressList.get(position).getCountry_code().equals("CA")) {
                    //selected_address = addressList.get(position).getFormatted_address();
                    listener.onSelect(position, addressList.get(position));
                    notifyDataSetChanged();
                } else {
                    Toast.makeText(mcontext, "Please select zip code for country US or Canada.", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ProRegularTextView txtspinemwnu;

        public ViewHolder(View itemView) {
            super(itemView);
            txtspinemwnu = (ProRegularTextView) itemView.findViewById(R.id.txtspinemwnu);
        }
    }

    public interface onItemelcted {
        void onSelect(int pos, SetGetAddressData data);
    }

    public void updateData(List<SetGetAddressData> list) {
        addressList = list;
        //selected_address = "";
        notifyDataSetChanged();
    }
}
