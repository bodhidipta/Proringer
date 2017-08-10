package com.android.llc.proringer.adapter;

import android.content.Context;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.llc.proringer.R;
import com.android.llc.proringer.helper.BottomSheetGlobalList;
import com.android.llc.proringer.viewsmod.textview.ProRegularTextView;

import java.util.List;

/**
 * Created by su on 7/11/17.
 */

public class HomeReminderBottomSheetAdapter extends RecyclerView.Adapter<HomeReminderBottomSheetAdapter.MyViewHolder> {
    Context context;
    List<String> arrayList;
    private BottomSheetGlobalList.onOptionSelected callback;
    BottomSheetDialog dialog;

    public HomeReminderBottomSheetAdapter(Context context, List<String> arrayList, BottomSheetDialog dialog, BottomSheetGlobalList.onOptionSelected callback) {
        this.context = context;
        this.arrayList = arrayList;
        this.dialog = dialog;
        this.callback = callback;
    }

    @Override
    public HomeReminderBottomSheetAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.actionmenuspiner, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(HomeReminderBottomSheetAdapter.MyViewHolder holder, final int position) {
        holder.txtspinemwnu.setText(arrayList.get(position));
        holder.txtspinemwnu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callback.onItemPassed(position, arrayList.get(position));
                dialog.dismiss();
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ProRegularTextView txtspinemwnu;

        public MyViewHolder(View itemView) {
            super(itemView);
            txtspinemwnu = (ProRegularTextView) itemView.findViewById(R.id.txtspinemwnu);
        }
    }
}
