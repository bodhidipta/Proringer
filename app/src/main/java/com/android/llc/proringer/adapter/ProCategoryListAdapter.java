package com.android.llc.proringer.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.llc.proringer.R;
import com.android.llc.proringer.fragments.drawerNav.SearchLocalProFragment;
import com.android.llc.proringer.pojo.ProCategoryData;
import com.android.llc.proringer.viewsmod.textview.ProRegularTextView;

import java.util.LinkedList;

/**
 * Created by su on 8/25/17.
 */

public class ProCategoryListAdapter extends RecyclerView.Adapter<ProCategoryListAdapter.MyViewHolder> {
    Context mContext;
    LinkedList<ProCategoryData> listdata;
    SearchLocalProFragment.onOptionSelectedCategory callback;

    public ProCategoryListAdapter(Context mContext, LinkedList<ProCategoryData> listdata, SearchLocalProFragment.onOptionSelectedCategory callback) {
        this.mContext = mContext;
        this.listdata = listdata;
        this.callback = callback;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_category_dailog, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        holder.tv.setText(listdata.get(position).getCategory_name());

        holder.tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callback.onItemPassed(position, listdata.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return listdata.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ProRegularTextView tv;

        public MyViewHolder(View itemView) {
            super(itemView);
            tv = (ProRegularTextView) itemView.findViewById(R.id.tv);
        }
    }

    public void setRefresh(LinkedList<ProCategoryData> listdata) {
        this.listdata = listdata;
        notifyDataSetChanged();
    }
}