package com.android.llc.proringer.helper;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import com.android.llc.proringer.R;
import com.android.llc.proringer.viewsmod.textview.ProRegularTextView;

/**
 * Created by su on 8/14/17.
 */

public class ShowMyDialog {

    Context context;

    public ShowMyDialog(Context context){
        this.context=context;
    }

    public void showDescribetionDialog(String title, String describetion) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//                    dialog.setCancelable(false);
        dialog.setContentView(R.layout.custom_dialogbox_pro_review_describetion);
        //dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));

        LinearLayout LLMain = (LinearLayout) dialog.findViewById(R.id.LLMain);

        ProRegularTextView tv_tittle = (ProRegularTextView) dialog.findViewById(R.id.tv_tittle);
        ProRegularTextView tv_show_describetion = (ProRegularTextView) dialog.findViewById(R.id.tv_show_describetion);


        DisplayMetrics displayMetrics = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(displayMetrics);

        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;

        LLMain.getLayoutParams().width = (width - 30);
        LLMain.getLayoutParams().height = (width - 30);
//        scrollView.getLayoutParams().height = (height-30)/2;

        dialog.findViewById(R.id.img_cancel_dialog).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        tv_tittle.setText(title);
        tv_show_describetion.setText(describetion);
        dialog.show();
    }

}