package com.android.llc.proringer.pojo;

/**
 * Created by bodhidipta on 13/06/17.
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

public class ChatPojo {
    private String date = "";
    private boolean isDateVisible = false;
    private String message = "";
    private boolean sender = false;
    private boolean ismessage = true;
    private String imageLink = "";

    public ChatPojo(String date, boolean isDateVisible, String message, boolean sender, boolean ismessage, String imageLink) {
        this.date = date;
        this.isDateVisible = isDateVisible;
        this.message = message;
        this.sender = sender;
        this.ismessage = ismessage;
        this.imageLink = imageLink;
    }

    public String getDate() {
        return date;
    }

    public boolean isDateVisible() {
        return isDateVisible;
    }

    public String getMessage() {
        return message;
    }

    public boolean isSender() {
        return sender;
    }

    public boolean ismessage() {
        return ismessage;
    }

    public String getImageLink() {
        return imageLink;
    }
}
