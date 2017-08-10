package com.android.llc.proringer.helper;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.android.llc.proringer.R;
import com.android.llc.proringer.adapter.HomeReminderBottomSheetAdapter;

import java.util.List;

/**
 * Created by su on 7/11/17.
 */

public class BottomSheetGlobalList {
    View view = null;
    private static Context dialogContext;
    onOptionSelected listener;
    RecyclerView rcv;
    private static BottomSheetGlobalList instatnce = null;

    public static BottomSheetGlobalList getInstatnce(Context context) {
        dialogContext = context;

        if (instatnce == null)
            instatnce = new BottomSheetGlobalList();

        return instatnce;
    }

    private BottomSheetGlobalList() {
    }

    public void showSiteSelectionDialog(final List<String> itemValueList, onOptionSelected callback) {
        listener = callback;

        view = LayoutInflater.from(dialogContext).inflate(R.layout.dialog_bottom_sheet, null);

        LinearLayout containlay = new LinearLayout(dialogContext);
        containlay.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        containlay.addView(view);
        containlay.requestLayout();

        LinearLayout mainLay = new LinearLayout(dialogContext);

        final BottomSheetDialog dialog = new BottomSheetDialog(dialogContext, R.style.BottomSheetDialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setGravity(Gravity.CENTER_HORIZONTAL);


        mainLay.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        mainLay.setGravity(Gravity.CENTER_HORIZONTAL);
        mainLay.addView(containlay);
        mainLay.requestLayout();


        dialog.setContentView(mainLay, new ViewGroup.LayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)));

        dialog.show();

        rcv = (RecyclerView) dialog.findViewById(R.id.rcv);
        rcv.setLayoutManager(new LinearLayoutManager(dialogContext));

        HomeReminderBottomSheetAdapter homeReminderBottomSheetAdapter = new HomeReminderBottomSheetAdapter(dialogContext, itemValueList, dialog, listener);
        rcv.setAdapter(homeReminderBottomSheetAdapter);

    }


    public interface onOptionSelected {
        void onItemPassed(int position, String value);
    }

}