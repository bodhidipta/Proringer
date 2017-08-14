package com.android.llc.proringer.helper;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

/**
 * Created by su on 4/10/17.
 */

public class CustomAlertForBackground {

    MyCustomAlertListener ml;
    MyCustomAlertBackgroundListener mlg;
    Context context;
    String title,message;

    public CustomAlertForBackground(Context context, String title, String message, MyCustomAlertBackgroundListener mlg){
        this.context=context;
        this.title=title;
        this.message=message;
        this.mlg = mlg;
    }


    public void getListenerOKCancelFromNormalAlertBackground(){
        new AlertDialog.Builder(context)
                .setCancelable(false)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        /////////take listener event///////////////
                        mlg.callbackForBackgroundAlert("ok");
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                        /////////take listener event///////////////
                        mlg.callbackForBackgroundAlert("cancel");
                    }
                })
                .create()
                .show();
    }
}
