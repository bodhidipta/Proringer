package com.android.llc.proringer.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.android.llc.proringer.R;
import com.android.llc.proringer.fragments.drawerNav.UserInformationFragment;
import com.android.llc.proringer.viewsmod.textview.ProRegularTextView;
import org.json.JSONArray;
import org.json.JSONException;

/**
 * Created by su on 7/28/17.
 */

public class PlaceCustomListAdapterDialog extends RecyclerView.Adapter<PlaceCustomListAdapterDialog.MyViewHolder> {
    Context mContext;
    JSONArray predictionsJsonArray;
    UserInformationFragment.onOptionSelected callback;
    public PlaceCustomListAdapterDialog(Context mContext,JSONArray predictionsJsonArray, UserInformationFragment.onOptionSelected callback){
        this.mContext=mContext;
        this.predictionsJsonArray=predictionsJsonArray;
        this.callback=callback;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_dailog_google_place, parent, false);
        return new MyViewHolder(itemView);    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        try {
            holder.tv.setText(predictionsJsonArray.getJSONObject(position).getString("description"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        holder.tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    callback.onItemPassed(position,predictionsJsonArray.getJSONObject(position));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return predictionsJsonArray.length();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ProRegularTextView tv;
        public MyViewHolder(View itemView) {
            super(itemView);
            tv= (ProRegularTextView) itemView.findViewById(R.id.tv);
        }
    }
    public void setRefresh(JSONArray predictionsJsonArray){
        this.predictionsJsonArray=predictionsJsonArray;
        notifyDataSetChanged();
    }
}
