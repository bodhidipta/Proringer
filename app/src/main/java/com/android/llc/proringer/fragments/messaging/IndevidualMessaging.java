package com.android.llc.proringer.fragments.messaging;

import android.icu.util.Calendar;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.llc.proringer.R;
import com.android.llc.proringer.adapter.IndevidualChatAdapter;
import com.android.llc.proringer.adapter.ProjectDetailedMessageAdapter;
import com.android.llc.proringer.pojo.ChatPojo;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;

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

public class IndevidualMessaging extends Fragment {
    RecyclerView chat_list;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.individual_chat_details, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        chat_list = (RecyclerView) view.findViewById(R.id.chat_list);
        chat_list.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, true));
        LinkedList<ChatPojo> chatList = new LinkedList<>();

        java.util.Calendar calendar = java.util.Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(java.util.Calendar.DAY_OF_MONTH, -3);

        ChatPojo pojo1 = new ChatPojo(
                new SimpleDateFormat("dd-MM-yyyy").format(calendar.getTime()),
                true,
                "Hello- world ",
                true,
                true,
                "");
        chatList.add(pojo1);
        ChatPojo pojo2 = new ChatPojo(
                new SimpleDateFormat("dd-MM-yyyy").format(calendar.getTime()),
                false,
                "HI there - world ",
                false,
                true,
                "");
        chatList.add(pojo2);
        calendar.add(java.util.Calendar.DAY_OF_MONTH, 1);
        ChatPojo pojo3 = new ChatPojo(
                new SimpleDateFormat("dd-MM-yyyy").format(calendar.getTime()),
                true,
                "Blahh... blahhh ... blahhhhhhhhh..... ",
                false,
                true,
                "");
        chatList.add(pojo3);
        ChatPojo pojo4 = new ChatPojo(
                new SimpleDateFormat("dd-MM-yyyy").format(calendar.getTime()),
                false,
                "",
                false,
                false,
                "http://visuallightbox.com/images/demo/macro1/data/images1/1.jpg");
        chatList.add(pojo4);
        calendar.add(java.util.Calendar.DAY_OF_MONTH, 1);
        ChatPojo pojo5 = new ChatPojo(
                new SimpleDateFormat("dd-MM-yyyy").format(calendar.getTime()),
                true,
                "This is latest",
                true,
                true,
                "");
        chatList.add(pojo5);
        ChatPojo pojo6 = new ChatPojo(
                new SimpleDateFormat("dd-MM-yyyy").format(calendar.getTime()),
                false,
                "Indeed",
                false,
                true,
                "");
        chatList.add(pojo6);
        calendar.add(java.util.Calendar.DAY_OF_MONTH, 1);
        ChatPojo pojo7 = new ChatPojo(
                new SimpleDateFormat("dd-MM-yyyy").format(calendar.getTime()),
                true,
                "This is latest",
                true,
                true,
                "");
        chatList.add(pojo7);
        ChatPojo pojo8 = new ChatPojo(
                new SimpleDateFormat("dd-MM-yyyy").format(calendar.getTime()),
                false,
                "Indeed",
                false,
                true,
                "");
        chatList.add(pojo8);

        Collections.reverse(chatList);
        chat_list.setAdapter(new IndevidualChatAdapter(getActivity(), chatList));

    }
}
