package com.android.llc.proringer.pojo;

/**
 * Created by bodhidipta on 24/06/17.
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

public class NotificationData {
    private String email_newsletter = "FALSE";
    private String email_chat_msg = "FALSE";
    private String email_tips_article = "FALSE";
    private String email_project_replies = "FALSE";
    private String mobile_newsletter = "FALSE";
    private String mobile_chat_msg = "FALSE";
    private String mobile_tips_article = "FALSE";
    private String mobile_project_replies = "FALSE";

    public NotificationData() {
    }

    public NotificationData(String email_newsletter, String email_chat_msg, String email_tips_article, String email_project_replies, String mobile_newsletter, String mobile_chat_msg, String mobile_tips_article, String mobile_project_replies) {
        this.email_newsletter = email_newsletter;
        this.email_chat_msg = email_chat_msg;
        this.email_tips_article = email_tips_article;
        this.email_project_replies = email_project_replies;
        this.mobile_newsletter = mobile_newsletter;
        this.mobile_chat_msg = mobile_chat_msg;
        this.mobile_tips_article = mobile_tips_article;
        this.mobile_project_replies = mobile_project_replies;
    }

    public String isEmail_newsletter() {
        return email_newsletter;
    }

    public void setEmail_newsletter(String email_newsletter) {
        this.email_newsletter = email_newsletter;
    }

    public String isEmail_chat_msg() {
        return email_chat_msg;
    }

    public void setEmail_chat_msg(String email_chat_msg) {
        this.email_chat_msg = email_chat_msg;
    }

    public String isEmail_tips_article() {
        return email_tips_article;
    }

    public void setEmail_tips_article(String email_tips_article) {
        this.email_tips_article = email_tips_article;
    }

    public String isEmail_project_replies() {
        return email_project_replies;
    }

    public void setEmail_project_replies(String email_project_replies) {
        this.email_project_replies = email_project_replies;
    }

    public String isMobile_newsletter() {
        return mobile_newsletter;
    }

    public void setMobile_newsletter(String mobile_newsletter) {
        this.mobile_newsletter = mobile_newsletter;
    }

    public String isMobile_chat_msg() {
        return mobile_chat_msg;
    }

    public void setMobile_chat_msg(String mobile_chat_msg) {
        this.mobile_chat_msg = mobile_chat_msg;
    }

    public String isMobile_tips_article() {
        return mobile_tips_article;
    }

    public void setMobile_tips_article(String mobile_tips_article) {
        this.mobile_tips_article = mobile_tips_article;
    }

    public String isMobile_project_replies() {
        return mobile_project_replies;
    }

    public void setMobile_project_replies(String mobile_project_replies) {
        this.mobile_project_replies = mobile_project_replies;
    }
}
