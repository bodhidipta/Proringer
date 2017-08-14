package com.android.llc.proringer.helper;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;


/**
 * Created by su on 4/10/17.
 */

public class CustomAlert {

    MyCustomAlertListener ml;
    Context context;
    String title,message;


    // constructor
    public CustomAlert(Context context,String title,String message,MyCustomAlertListener ml){
        this.context=context;
        this.title=title;
        this.message=message;
        this.ml = ml;
    }

    public void createNormalAlert(final String positiveTitle,final int checkValue){
        new AlertDialog.Builder(context)
                .setCancelable(false)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(positiveTitle, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        ml.callbackForAlert(positiveTitle,checkValue);
                    }
                })
                .create()
                .show();
    }



    public void getListenerRetryCancelFromNormalAlert(final String positiveTitle,final String negativeTitle,final int checkValue){
        new AlertDialog.Builder(context)
                .setCancelable(false)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(positiveTitle, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        /////////take listener event///////////////
                        ml.callbackForAlert(positiveTitle,checkValue);
                    }
                })
                .setNegativeButton(negativeTitle, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                        /////////take listener event///////////////
                        ml.callbackForAlert(negativeTitle,checkValue);
                    }
                })
                .create()
                .show();
    }

}
