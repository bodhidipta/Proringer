package com.android.llc.proringer.adapter;

import android.content.Context;
import android.location.Address;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.llc.proringer.R;
import com.android.llc.proringer.viewsmod.textview.ProRegularTextView;

import java.util.LinkedList;
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

public class LocationListAdapter extends RecyclerView.Adapter<LocationListAdapter.ViewHolder> {
    private Context mcontext;
    private List<Address> addressList;
    private onItemelcted listener;

    public LocationListAdapter(Context mcontext, List<Address> addressList, onItemelcted call) {
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
        return new ViewHolder(LayoutInflater.from(mcontext).inflate(R.layout.actionmenuspiner, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.txtspinemwnu.setText(addressList.get(position).getLocality());
        if (addressList.get(position).getSubLocality() != null) {
            holder.txtspinemwnu.append("," + addressList.get(position).getSubLocality());
        }
        if (addressList.get(position).getSubAdminArea() != null) {
            holder.txtspinemwnu.append("," + addressList.get(position).getSubAdminArea());
        }
        if (addressList.get(position).getAdminArea() != null) {
            holder.txtspinemwnu.append("," + addressList.get(position).getAdminArea());
        }

        if (addressList.get(position).getCountryName() != null) {
            holder.txtspinemwnu.append("," + addressList.get(position).getCountryName());
        }
        if (addressList.get(position).getPostalCode() != null) {
            holder.txtspinemwnu.append("," + addressList.get(position).getPostalCode());
        }

        holder.txtspinemwnu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onSelect(position, addressList.get(position));
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
        void onSelect(int pos, Address data);
    }
}
