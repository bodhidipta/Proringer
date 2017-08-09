package com.android.llc.proringer.fragments.drawerNav;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ScrollingView;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.android.llc.proringer.R;
import com.android.llc.proringer.activities.LandScreenActivity;
import com.android.llc.proringer.appconstant.ProApplication;
import com.android.llc.proringer.helper.ProServiceApiHelper;
import com.android.llc.proringer.utils.Logger;
import com.android.llc.proringer.viewsmod.SwitchHelper;

/**
 * Created by bodhidipta on 22/06/17.
 * <!-- * Copyright (c) 2017, The Proringer-->
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

public class NotificationsFragment extends Fragment {
    private SwitchHelper email_newsletter, email_chat_msg, email_tips_artcl, email_prjct_rspnse, mobile_newsletter, mobile_chat_msg, mobile_tips_artcl, mobile_prjct_rspnse;
    ProgressDialog pgDialog;
    ScrollView ScrollViewMAin;
    LinearLayout LLNetworkDisconnection;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_notifications, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ScrollViewMAin= (ScrollView) view.findViewById(R.id.ScrollViewMAin);
        LLNetworkDisconnection= (LinearLayout) view.findViewById(R.id.LLNetworkDisconnection);;

        email_newsletter = (SwitchHelper) view.findViewById(R.id.email_newsletter);
        email_chat_msg = (SwitchHelper) view.findViewById(R.id.email_chat_msg);
        email_tips_artcl = (SwitchHelper) view.findViewById(R.id.email_tips_artcl);
        email_prjct_rspnse = (SwitchHelper) view.findViewById(R.id.email_prjct_rspnse);
        mobile_newsletter = (SwitchHelper) view.findViewById(R.id.mobile_newsletter);
        mobile_chat_msg = (SwitchHelper) view.findViewById(R.id.mobile_chat_msg);
        mobile_tips_artcl = (SwitchHelper) view.findViewById(R.id.mobile_tips_artcl);
        mobile_prjct_rspnse = (SwitchHelper) view.findViewById(R.id.mobile_prjct_rspnse);

        email_newsletter.setState(ProApplication.getInstance().getUserNotification().isEmail_newsletter());
        email_chat_msg.setState(ProApplication.getInstance().getUserNotification().isEmail_chat_msg());
        email_tips_artcl.setState(ProApplication.getInstance().getUserNotification().isEmail_tips_article());
        email_prjct_rspnse.setState(ProApplication.getInstance().getUserNotification().isEmail_project_replies());
        mobile_newsletter.setState(ProApplication.getInstance().getUserNotification().isMobile_newsletter());
        mobile_chat_msg.setState(ProApplication.getInstance().getUserNotification().isMobile_chat_msg());
        mobile_tips_artcl.setState(ProApplication.getInstance().getUserNotification().isMobile_tips_article());
        mobile_prjct_rspnse.setState(ProApplication.getInstance().getUserNotification().isMobile_project_replies());

        getNotificationState();

        email_newsletter.setOnclickCallback(new SwitchHelper.onClickListener() {
            @Override
            public void onClick(boolean state) {
                Logger.printMessage("customswitch", "email_newsletter : " + state);
                setChangeNotification();
            }
        });
        email_chat_msg.setOnclickCallback(new SwitchHelper.onClickListener() {
            @Override
            public void onClick(boolean state) {
                Logger.printMessage("customswitch", "email_chat_msg : " + state);
                setChangeNotification();
            }
        });
        email_tips_artcl.setOnclickCallback(new SwitchHelper.onClickListener() {
            @Override
            public void onClick(boolean state) {
                Logger.printMessage("customswitch", "email_tips_artcl : " + state);
                setChangeNotification();
            }
        });
        email_prjct_rspnse.setOnclickCallback(new SwitchHelper.onClickListener() {
            @Override
            public void onClick(boolean state) {
                Logger.printMessage("customswitch", "email_prjct_rspnse : " + state);
                setChangeNotification();
            }
        });
        mobile_newsletter.setOnclickCallback(new SwitchHelper.onClickListener() {
            @Override
            public void onClick(boolean state) {
                Logger.printMessage("customswitch", "mobile_newsletter : " + state);
                setChangeNotification();
            }
        });
        mobile_chat_msg.setOnclickCallback(new SwitchHelper.onClickListener() {
            @Override
            public void onClick(boolean state) {
                Logger.printMessage("customswitch", "mobile_chat_msg : " + state);
                setChangeNotification();
            }
        });
        mobile_tips_artcl.setOnclickCallback(new SwitchHelper.onClickListener() {
            @Override
            public void onClick(boolean state) {
                Logger.printMessage("customswitch", "mobile_tips_artcl : " + state);
                setChangeNotification();
            }
        });
        mobile_prjct_rspnse.setOnclickCallback(new SwitchHelper.onClickListener() {
            @Override
            public void onClick(boolean state) {
                Logger.printMessage("customswitch", "mobile_prjct_rspnse : " + state);
                setChangeNotification();
            }
        });

    }

    private void getNotificationState() {

        ScrollViewMAin.setVisibility(View.VISIBLE);
        LLNetworkDisconnection.setVisibility(View.GONE);

        ProServiceApiHelper.getInstance((LandScreenActivity)getActivity()).getUserNotification(
                new ProServiceApiHelper.getApiProcessCallback() {
                    @Override
                    public void onStart() {
                        pgDialog = new ProgressDialog((LandScreenActivity)getActivity());
                        pgDialog.setTitle("Notification");
                        pgDialog.setCancelable(false);
                        pgDialog.setMessage("Getting Notification data.Please wait...");
                        pgDialog.show();
                    }

                    @Override
                    public void onComplete(String message) {
                        if (pgDialog != null && pgDialog.isShowing())
                            pgDialog.dismiss();

                        email_newsletter.setState(ProApplication.getInstance().getUserNotification().isEmail_newsletter());
                        email_chat_msg.setState(ProApplication.getInstance().getUserNotification().isEmail_chat_msg());
                        email_tips_artcl.setState(ProApplication.getInstance().getUserNotification().isEmail_tips_article());
                        email_prjct_rspnse.setState(ProApplication.getInstance().getUserNotification().isEmail_project_replies());
                        mobile_newsletter.setState(ProApplication.getInstance().getUserNotification().isMobile_newsletter());
                        mobile_chat_msg.setState(ProApplication.getInstance().getUserNotification().isMobile_chat_msg());
                        mobile_tips_artcl.setState(ProApplication.getInstance().getUserNotification().isMobile_tips_article());
                        mobile_prjct_rspnse.setState(ProApplication.getInstance().getUserNotification().isMobile_project_replies());

                    }

                    @Override
                    public void onError(String error) {

                        if (pgDialog != null && pgDialog.isShowing())
                            pgDialog.dismiss();


                        if(error.equalsIgnoreCase("No internet connection found. Please check your internet connection.")){
                            ScrollViewMAin.setVisibility(View.GONE);
                            LLNetworkDisconnection.setVisibility(View.VISIBLE);
                        }

                        new AlertDialog.Builder(getActivity())
                                .setTitle("Notification Load Error")
                                .setMessage("" + error)
                                .setPositiveButton("retry", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                        getNotificationState();

                                    }
                                })
                                .setNegativeButton("abort", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });

                    }
                });
    }

    private void setChangeNotification() {
        ProServiceApiHelper.getInstance((LandScreenActivity)getActivity()).updateUserNotification(
                new ProServiceApiHelper.getApiProcessCallback() {
                    @Override
                    public void onStart() {

                    }

                    @Override
                    public void onComplete(String message) {

                    }

                    @Override
                    public void onError(String error) {

                    }
                },
                email_newsletter.getState(),
                email_chat_msg.getState(),
                email_tips_artcl.getState(),
                email_prjct_rspnse.getState(),
                mobile_newsletter.getState(),
                mobile_chat_msg.getState(),
                mobile_tips_artcl.getState(),
                mobile_prjct_rspnse.getState()

        );
    }


}
