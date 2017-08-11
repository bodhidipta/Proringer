package com.android.llc.proringer.viewsmod;

import android.graphics.Color;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.android.llc.proringer.R;
import com.android.llc.proringer.viewsmod.textview.ProBoldTextView;

/**
 * Created by bodhidipta on 09/06/17.
 * <!-- * Copyright (c) 2017, Proringer-->
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
 * -->
 */

public class NavigationHandler {
    private View inflatedView = null;
    private OnHandleInput listener = null;
    private static NavigationHandler instance = null;

    private LinearLayout find_local_pros_cont, account_cont, support_cont, about_cont;
    private ImageView find_local_pros_img, account_img, support_img, about_img;
    private ProBoldTextView find_local_pros_text, account_text, support_text, about_text;

    private RelativeLayout userInformation, login_settings, notification, home_scheduler, invite_friend, log_out,RLEmailSupport;

    public static final String FIND_LOCAL_PROS = "find_local_pros",
            ACCOUNT = "account",
            USER_INFORMATION = "user_info",
            LOGIN_SETTINGS = "login_sett",
            NOTIFICATION = "noti",
            HOME_SCHEDUL = "home_sche",
            INVITE_FRIEND = "invite_fr",
            SUPPORT = "support",
            ABOUT = "abt",
            Email_Support = "email_support",
            LOGOUT = "logout";

    public static NavigationHandler getInstance() {
        if (instance == null)
            instance = new NavigationHandler();
        return instance;
    }

    public void init(View view, OnHandleInput listener) {
        inflatedView = view;

        find_local_pros_cont = (LinearLayout) view.findViewById(R.id.find_local_pros_cont);
        find_local_pros_img = (ImageView) view.findViewById(R.id.find_local_pros_img);
        find_local_pros_text = (ProBoldTextView) view.findViewById(R.id.find_local_pros_text);

        account_cont = (LinearLayout) view.findViewById(R.id.account_cont);
        account_img = (ImageView) view.findViewById(R.id.account_img);
        account_text = (ProBoldTextView) view.findViewById(R.id.account_text);

        support_cont = (LinearLayout) view.findViewById(R.id.support_cont);
        support_img = (ImageView) view.findViewById(R.id.support_img);
        support_text = (ProBoldTextView) view.findViewById(R.id.support_text);

        about_cont = (LinearLayout) view.findViewById(R.id.about_cont);
        about_img = (ImageView) view.findViewById(R.id.about_img);
        about_text = (ProBoldTextView) view.findViewById(R.id.about_text);

        RLEmailSupport = (RelativeLayout) view.findViewById(R.id.RLEmailSupport);
        login_settings = (RelativeLayout) view.findViewById(R.id.login_settings);
        notification = (RelativeLayout) view.findViewById(R.id.notification);
        home_scheduler = (RelativeLayout) view.findViewById(R.id.home_scheduler);
        invite_friend = (RelativeLayout) view.findViewById(R.id.invite_friend);
        log_out = (RelativeLayout) view.findViewById(R.id.log_out);
        RLEmailSupport = (RelativeLayout) view.findViewById(R.id.RLEmailSupport);

        find_local_pros_cont.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                highlightTag(FIND_LOCAL_PROS);
            }
        });
        account_cont.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                highlightTag(ACCOUNT);
            }
        });
        support_cont.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                highlightTag(SUPPORT);
            }
        });
        about_cont.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                highlightTag(ABOUT);
            }
        });
        userInformation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                highlightTag(USER_INFORMATION);
            }
        });

        RLEmailSupport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                highlightTag(Email_Support);
            }
        });
        login_settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                highlightTag(LOGIN_SETTINGS);
            }
        });
        notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                highlightTag(NOTIFICATION);
            }
        });
        home_scheduler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                highlightTag(HOME_SCHEDUL);
            }
        });
        invite_friend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                highlightTag(INVITE_FRIEND);
            }
        });
        log_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                highlightTag(LOGOUT);
            }
        });


        handleViewInput(listener);
    }

    private NavigationHandler() {
    }

    private void handleViewInput(OnHandleInput callback) {
        listener = callback;
    }

    public void highlightTag(String tag) {
        switch (tag) {
            case FIND_LOCAL_PROS:

                listener.onClickItem(FIND_LOCAL_PROS);

                find_local_pros_cont.setBackgroundColor(Color.parseColor("#656565"));
                find_local_pros_img.setBackgroundResource(R.drawable.ic_search_pro_white);
                find_local_pros_text.setTextColor(Color.WHITE);

                account_cont.setBackgroundColor(Color.TRANSPARENT);
                account_img.setBackgroundResource(R.drawable.ic_settings);
                account_text.setTextColor(Color.parseColor("#505050"));


                support_cont.setBackgroundColor(Color.TRANSPARENT);
                support_img.setBackgroundResource(R.drawable.ic_support);
                support_text.setTextColor(Color.parseColor("#505050"));

                about_cont.setBackgroundColor(Color.TRANSPARENT);
                about_img.setBackgroundResource(R.drawable.ic_about);
                about_text.setTextColor(Color.parseColor("#505050"));

                userInformation.setBackgroundColor(Color.TRANSPARENT);
                login_settings.setBackgroundColor(Color.TRANSPARENT);
                notification.setBackgroundColor(Color.TRANSPARENT);
                home_scheduler.setBackgroundColor(Color.TRANSPARENT);
                invite_friend.setBackgroundColor(Color.TRANSPARENT);
                RLEmailSupport.setBackgroundColor(Color.TRANSPARENT);
                log_out.setBackgroundColor(Color.TRANSPARENT);

                break;

            case ACCOUNT:
                listener.onClickItem(ACCOUNT);

                userInformation.setBackgroundColor(Color.parseColor("#656565"));

                find_local_pros_cont.setBackgroundColor(Color.TRANSPARENT);
                find_local_pros_img.setBackgroundResource(R.drawable.ic_search_pro);
                find_local_pros_text.setTextColor(Color.parseColor("#505050"));

                account_cont.setBackgroundColor(Color.parseColor("#656565"));
                account_img.setBackgroundResource(R.drawable.ic_settings_white);
                account_text.setTextColor(Color.WHITE);

                support_cont.setBackgroundColor(Color.TRANSPARENT);
                support_img.setBackgroundResource(R.drawable.ic_support);
                support_text.setTextColor(Color.parseColor("#505050"));

                find_local_pros_cont.setBackgroundColor(Color.TRANSPARENT);
                find_local_pros_img.setBackgroundResource(R.drawable.ic_search_pro);
                find_local_pros_text.setTextColor(Color.parseColor("#505050"));

                login_settings.setBackgroundColor(Color.TRANSPARENT);
                notification.setBackgroundColor(Color.TRANSPARENT);
                home_scheduler.setBackgroundColor(Color.TRANSPARENT);
                invite_friend.setBackgroundColor(Color.TRANSPARENT);
                RLEmailSupport.setBackgroundColor(Color.TRANSPARENT);
                log_out.setBackgroundColor(Color.TRANSPARENT);
                break;

            case USER_INFORMATION:
                listener.onClickItem(USER_INFORMATION);
                userInformation.setBackgroundColor(Color.parseColor("#656565"));

                find_local_pros_cont.setBackgroundColor(Color.TRANSPARENT);
                find_local_pros_img.setBackgroundResource(R.drawable.ic_search_pro);
                find_local_pros_text.setTextColor(Color.parseColor("#505050"));

                account_cont.setBackgroundColor(Color.TRANSPARENT);
                account_img.setBackgroundResource(R.drawable.ic_settings);
                account_text.setTextColor(Color.parseColor("#505050"));


                support_cont.setBackgroundColor(Color.TRANSPARENT);
                support_img.setBackgroundResource(R.drawable.ic_support);
                support_text.setTextColor(Color.parseColor("#505050"));

                find_local_pros_cont.setBackgroundColor(Color.TRANSPARENT);
                find_local_pros_img.setBackgroundResource(R.drawable.ic_search_pro);
                find_local_pros_text.setTextColor(Color.parseColor("#505050"));

                login_settings.setBackgroundColor(Color.TRANSPARENT);
                notification.setBackgroundColor(Color.TRANSPARENT);
                home_scheduler.setBackgroundColor(Color.TRANSPARENT);
                invite_friend.setBackgroundColor(Color.TRANSPARENT);
                RLEmailSupport.setBackgroundColor(Color.TRANSPARENT);
                log_out.setBackgroundColor(Color.TRANSPARENT);
                break;
            case LOGIN_SETTINGS:
                listener.onClickItem(LOGIN_SETTINGS);
                login_settings.setBackgroundColor(Color.parseColor("#656565"));

                find_local_pros_cont.setBackgroundColor(Color.TRANSPARENT);
                find_local_pros_img.setBackgroundResource(R.drawable.ic_search_pro);
                find_local_pros_text.setTextColor(Color.parseColor("#505050"));

                account_cont.setBackgroundColor(Color.TRANSPARENT);
                account_img.setBackgroundResource(R.drawable.ic_settings);
                account_text.setTextColor(Color.parseColor("#505050"));


                support_cont.setBackgroundColor(Color.TRANSPARENT);
                support_img.setBackgroundResource(R.drawable.ic_support);
                support_text.setTextColor(Color.parseColor("#505050"));

                find_local_pros_cont.setBackgroundColor(Color.TRANSPARENT);
                find_local_pros_img.setBackgroundResource(R.drawable.ic_search_pro);
                find_local_pros_text.setTextColor(Color.parseColor("#505050"));

                userInformation.setBackgroundColor(Color.TRANSPARENT);
                notification.setBackgroundColor(Color.TRANSPARENT);
                home_scheduler.setBackgroundColor(Color.TRANSPARENT);
                invite_friend.setBackgroundColor(Color.TRANSPARENT);
                RLEmailSupport.setBackgroundColor(Color.TRANSPARENT);
                log_out.setBackgroundColor(Color.TRANSPARENT);
                break;
            case NOTIFICATION:
                listener.onClickItem(NOTIFICATION);
                notification.setBackgroundColor(Color.parseColor("#656565"));

                find_local_pros_cont.setBackgroundColor(Color.TRANSPARENT);
                find_local_pros_img.setBackgroundResource(R.drawable.ic_search_pro);
                find_local_pros_text.setTextColor(Color.parseColor("#505050"));

                account_cont.setBackgroundColor(Color.TRANSPARENT);
                account_img.setBackgroundResource(R.drawable.ic_settings);
                account_text.setTextColor(Color.parseColor("#505050"));


                support_cont.setBackgroundColor(Color.TRANSPARENT);
                support_img.setBackgroundResource(R.drawable.ic_support);
                support_text.setTextColor(Color.parseColor("#505050"));

                find_local_pros_cont.setBackgroundColor(Color.TRANSPARENT);
                find_local_pros_img.setBackgroundResource(R.drawable.ic_search_pro);
                find_local_pros_text.setTextColor(Color.parseColor("#505050"));

                userInformation.setBackgroundColor(Color.TRANSPARENT);
                login_settings.setBackgroundColor(Color.TRANSPARENT);
                home_scheduler.setBackgroundColor(Color.TRANSPARENT);
                invite_friend.setBackgroundColor(Color.TRANSPARENT);
                RLEmailSupport.setBackgroundColor(Color.TRANSPARENT);
                log_out.setBackgroundColor(Color.TRANSPARENT);
                break;
            case HOME_SCHEDUL:
                listener.onClickItem(HOME_SCHEDUL);
                home_scheduler.setBackgroundColor(Color.parseColor("#656565"));

                find_local_pros_cont.setBackgroundColor(Color.TRANSPARENT);
                find_local_pros_img.setBackgroundResource(R.drawable.ic_search_pro);
                find_local_pros_text.setTextColor(Color.parseColor("#505050"));

                account_cont.setBackgroundColor(Color.TRANSPARENT);
                account_img.setBackgroundResource(R.drawable.ic_settings);
                account_text.setTextColor(Color.parseColor("#505050"));

                support_cont.setBackgroundColor(Color.TRANSPARENT);
                support_img.setBackgroundResource(R.drawable.ic_support);
                support_text.setTextColor(Color.parseColor("#505050"));

                find_local_pros_cont.setBackgroundColor(Color.TRANSPARENT);
                find_local_pros_img.setBackgroundResource(R.drawable.ic_search_pro);
                find_local_pros_text.setTextColor(Color.parseColor("#505050"));

                userInformation.setBackgroundColor(Color.TRANSPARENT);
                login_settings.setBackgroundColor(Color.TRANSPARENT);
                notification.setBackgroundColor(Color.TRANSPARENT);
                invite_friend.setBackgroundColor(Color.TRANSPARENT);
                RLEmailSupport.setBackgroundColor(Color.TRANSPARENT);
                log_out.setBackgroundColor(Color.TRANSPARENT);
                break;
            case INVITE_FRIEND:
                listener.onClickItem(INVITE_FRIEND);
                invite_friend.setBackgroundColor(Color.parseColor("#656565"));

                find_local_pros_cont.setBackgroundColor(Color.TRANSPARENT);
                find_local_pros_img.setBackgroundResource(R.drawable.ic_search_pro);
                find_local_pros_text.setTextColor(Color.parseColor("#505050"));

                support_cont.setBackgroundColor(Color.TRANSPARENT);
                support_img.setBackgroundResource(R.drawable.ic_support);
                support_text.setTextColor(Color.parseColor("#505050"));


                account_cont.setBackgroundColor(Color.TRANSPARENT);
                account_img.setBackgroundResource(R.drawable.ic_settings);
                account_text.setTextColor(Color.parseColor("#505050"));

                find_local_pros_cont.setBackgroundColor(Color.TRANSPARENT);
                find_local_pros_img.setBackgroundResource(R.drawable.ic_search_pro);
                find_local_pros_text.setTextColor(Color.parseColor("#505050"));

                userInformation.setBackgroundColor(Color.TRANSPARENT);
                login_settings.setBackgroundColor(Color.TRANSPARENT);
                notification.setBackgroundColor(Color.TRANSPARENT);
                home_scheduler.setBackgroundColor(Color.TRANSPARENT);
                RLEmailSupport.setBackgroundColor(Color.TRANSPARENT);
                log_out.setBackgroundColor(Color.TRANSPARENT);
                break;
            case SUPPORT:
                listener.onClickItem(SUPPORT);

                account_cont.setBackgroundColor(Color.TRANSPARENT);
                account_img.setBackgroundResource(R.drawable.ic_settings);
                account_text.setTextColor(Color.parseColor("#505050"));

                support_cont.setBackgroundColor(Color.parseColor("#656565"));
                support_img.setBackgroundResource(R.drawable.ic_support_white);
                support_text.setTextColor(Color.WHITE);

                find_local_pros_cont.setBackgroundColor(Color.TRANSPARENT);
                find_local_pros_img.setBackgroundResource(R.drawable.ic_search_pro);
                find_local_pros_text.setTextColor(Color.parseColor("#505050"));

                about_cont.setBackgroundColor(Color.TRANSPARENT);
                about_img.setBackgroundResource(R.drawable.ic_about);
                about_text.setTextColor(Color.parseColor("#505050"));

                userInformation.setBackgroundColor(Color.TRANSPARENT);
                login_settings.setBackgroundColor(Color.TRANSPARENT);
                notification.setBackgroundColor(Color.TRANSPARENT);
                home_scheduler.setBackgroundColor(Color.TRANSPARENT);
                invite_friend.setBackgroundColor(Color.TRANSPARENT);
                RLEmailSupport.setBackgroundColor(Color.TRANSPARENT);
                log_out.setBackgroundColor(Color.TRANSPARENT);
                break;
            case ABOUT:
                listener.onClickItem(ABOUT);

                about_cont.setBackgroundColor(Color.parseColor("#656565"));
                about_img.setBackgroundResource(R.drawable.ic_about_white);
                about_text.setTextColor(Color.WHITE);

                account_cont.setBackgroundColor(Color.TRANSPARENT);
                account_img.setBackgroundResource(R.drawable.ic_settings);
                account_text.setTextColor(Color.parseColor("#505050"));

                support_cont.setBackgroundColor(Color.TRANSPARENT);
                support_img.setBackgroundResource(R.drawable.ic_support);
                support_text.setTextColor(Color.parseColor("#505050"));

                find_local_pros_cont.setBackgroundColor(Color.TRANSPARENT);
                find_local_pros_img.setBackgroundResource(R.drawable.ic_search_pro);
                find_local_pros_text.setTextColor(Color.parseColor("#505050"));

                userInformation.setBackgroundColor(Color.TRANSPARENT);
                login_settings.setBackgroundColor(Color.TRANSPARENT);
                notification.setBackgroundColor(Color.TRANSPARENT);
                home_scheduler.setBackgroundColor(Color.TRANSPARENT);
                invite_friend.setBackgroundColor(Color.TRANSPARENT);
                RLEmailSupport.setBackgroundColor(Color.TRANSPARENT);
                log_out.setBackgroundColor(Color.TRANSPARENT);
                break;
            case LOGOUT:
                listener.onClickItem(LOGOUT);
                log_out.setBackgroundColor(Color.parseColor("#656565"));

                find_local_pros_cont.setBackgroundColor(Color.TRANSPARENT);
                find_local_pros_img.setBackgroundResource(R.drawable.ic_search_pro);
                find_local_pros_text.setTextColor(Color.parseColor("#505050"));

                account_cont.setBackgroundColor(Color.TRANSPARENT);
                account_img.setBackgroundResource(R.drawable.ic_settings);
                account_text.setTextColor(Color.parseColor("#505050"));

                support_cont.setBackgroundColor(Color.TRANSPARENT);
                support_img.setBackgroundResource(R.drawable.ic_support);
                support_text.setTextColor(Color.parseColor("#505050"));

                find_local_pros_cont.setBackgroundColor(Color.TRANSPARENT);
                find_local_pros_img.setBackgroundResource(R.drawable.ic_search_pro);
                find_local_pros_text.setTextColor(Color.parseColor("#505050"));

                userInformation.setBackgroundColor(Color.TRANSPARENT);
                login_settings.setBackgroundColor(Color.TRANSPARENT);
                notification.setBackgroundColor(Color.TRANSPARENT);
                home_scheduler.setBackgroundColor(Color.TRANSPARENT);
                RLEmailSupport.setBackgroundColor(Color.TRANSPARENT);
                invite_friend.setBackgroundColor(Color.TRANSPARENT);
                break;

            case Email_Support:

                listener.onClickItem(LOGOUT);
                RLEmailSupport.setBackgroundColor(Color.parseColor("#656565"));

                find_local_pros_cont.setBackgroundColor(Color.TRANSPARENT);
                find_local_pros_img.setBackgroundResource(R.drawable.ic_search_pro);
                find_local_pros_text.setTextColor(Color.parseColor("#505050"));

                account_cont.setBackgroundColor(Color.TRANSPARENT);
                account_img.setBackgroundResource(R.drawable.ic_settings);
                account_text.setTextColor(Color.parseColor("#505050"));

                support_cont.setBackgroundColor(Color.TRANSPARENT);
                support_img.setBackgroundResource(R.drawable.ic_support);
                support_text.setTextColor(Color.parseColor("#505050"));

                find_local_pros_cont.setBackgroundColor(Color.TRANSPARENT);
                find_local_pros_img.setBackgroundResource(R.drawable.ic_search_pro);
                find_local_pros_text.setTextColor(Color.parseColor("#505050"));

                userInformation.setBackgroundColor(Color.TRANSPARENT);
                login_settings.setBackgroundColor(Color.TRANSPARENT);
                notification.setBackgroundColor(Color.TRANSPARENT);
                home_scheduler.setBackgroundColor(Color.TRANSPARENT);
                invite_friend.setBackgroundColor(Color.TRANSPARENT);
                log_out.setBackgroundColor(Color.TRANSPARENT);

                break;

            default:

        }
    }


    public interface OnHandleInput {
        void onClickItem(String tag);
    }
}
