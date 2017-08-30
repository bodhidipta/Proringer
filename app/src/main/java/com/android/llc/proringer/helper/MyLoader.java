package com.android.llc.proringer.helper;

import android.app.ProgressDialog;
import android.content.Context;

/**
 * Created by su on 8/12/17.
 */

public class MyLoader {

    ProgressDialog pgDialog;
    Context context;
    static String title = "Loading";
    static String msg = "Please wait...";

    public MyLoader(Context context) {

        this.context = context;

        pgDialog = new ProgressDialog(context);
        pgDialog.setTitle(title);
        pgDialog.setCancelable(false);
        pgDialog.setCanceledOnTouchOutside(false);
        pgDialog.setMessage(msg);
    }

    public void showLoader() {
        pgDialog.show();
    }

    public void dismissLoader() {
        pgDialog.dismiss();
    }

    public boolean isMyLoaderShowing() {
        if (pgDialog.isShowing()) {
            return true;
        } else {
            return false;
        }
    }
}
