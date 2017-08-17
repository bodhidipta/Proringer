package com.android.llc.proringer.helper;

/**
 * Created by su on 8/17/17.
 */

public class ViewHelper {

    public static final String P_Left="left";
    public static final String P_Right="right";
    public static final String P_Center="center";
    public static final String P_Justify="justify";


    public static String SetParaAlign(String Text,String Align)
    {
        return "<html> <body  style=\"text-align:"+Align+";\">"+Text+"</body></html>";
    }



}
