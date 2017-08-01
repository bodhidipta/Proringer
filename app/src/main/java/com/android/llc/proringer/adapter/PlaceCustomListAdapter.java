package com.android.llc.proringer.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.android.llc.proringer.R;
import com.android.llc.proringer.activities.TakeGooglePlacePredictionActivity;
import com.android.llc.proringer.fragments.drawerNav.UserInformationFragment;
import com.android.llc.proringer.viewsmod.textview.ProRegularTextView;
import java.util.ArrayList;

/**
 * Created by su on 7/28/17.
 */

public class PlaceCustomListAdapter extends RecyclerView.Adapter<PlaceCustomListAdapter.MyViewHolder> {
    Context mContext;
    ArrayList<String> stringArrayList;
    TakeGooglePlacePredictionActivity.onOptionSelected callback;
    public PlaceCustomListAdapter(Context mContext, ArrayList<String> stringArrayList, TakeGooglePlacePredictionActivity.onOptionSelected callback){
        this.mContext=mContext;
        this.stringArrayList=stringArrayList;
        this.callback=callback;
    }

    @Override
    public PlaceCustomListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_dailog_google_place, parent, false);
        return new PlaceCustomListAdapter.MyViewHolder(itemView);    }

    @Override
    public void onBindViewHolder(PlaceCustomListAdapter.MyViewHolder holder, final int position) {

            holder.tv.setText(stringArrayList.get(position));

        holder.tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    callback.onItemPassed(position,stringArrayList);
            }
        });
    }

    @Override
    public int getItemCount() {
        return stringArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ProRegularTextView tv;
        public MyViewHolder(View itemView) {
            super(itemView);
            tv= (ProRegularTextView) itemView.findViewById(R.id.tv);
        }
    }
    public void setRefresh(ArrayList<String> stringArrayList){
        this.stringArrayList=stringArrayList;
        notifyDataSetChanged();
    }

}
