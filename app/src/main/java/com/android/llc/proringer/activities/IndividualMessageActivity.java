package com.android.llc.proringer.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;

import com.android.llc.proringer.R;
import com.android.llc.proringer.adapter.IndevidualChatAdapter;
import com.android.llc.proringer.pojo.SetGetChatPojoData;
import com.android.llc.proringer.utils.Logger;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.Collections;
import java.util.LinkedList;

/**
 * Created by bodhidipta on 20/06/17.
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

public class IndividualMessageActivity extends AppCompatActivity {
    RecyclerView chat_list;
    ImageView img_background;
    IndevidualChatAdapter indevidualChatAdapter=null;
    JSONArray jsonArray;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.individual_chat_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        try {
            jsonArray=new JSONArray(getIntent().getStringExtra("message_list"));
            Logger.printMessage("message_list","-->"+jsonArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        chat_list = (RecyclerView) findViewById(R.id.chat_list);
        chat_list.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true));
        LinkedList<SetGetChatPojoData> chatList = new LinkedList<>();

        img_background = (ImageView) findViewById(R.id.img_background);

        Glide.with(IndividualMessageActivity.this).load(R.drawable.chat_background).centerCrop().into(img_background);



        try {

            for (int i = 0; i < jsonArray.length(); i++) {

                for (int j = 0; j < jsonArray.getJSONObject(i).getJSONArray("info").length();j++){

                    SetGetChatPojoData setGetChatPojoData=new SetGetChatPojoData();
                    if (j==0){
                        setGetChatPojoData.setDate(jsonArray.getJSONObject(i).getString("date"));
                    }else {
                        setGetChatPojoData.setDate("");
                    }
                    setGetChatPojoData.setUser(jsonArray.getJSONObject(i).getJSONArray("info").getJSONObject(j).getString("user"));
                    setGetChatPojoData.setSender_id(jsonArray.getJSONObject(i).getJSONArray("info").getJSONObject(j).getString("sender_id"));
                    setGetChatPojoData.setReceiver_id(jsonArray.getJSONObject(i).getJSONArray("info").getJSONObject(j).getString("receiver_id"));
                    setGetChatPojoData.setMessage_id(jsonArray.getJSONObject(i).getJSONArray("info").getJSONObject(j).getString("message_id"));
                    setGetChatPojoData.setProject_id(jsonArray.getJSONObject(i).getJSONArray("info").getJSONObject(j).getString("project_id"));
                    setGetChatPojoData.setMessage_info(jsonArray.getJSONObject(i).getJSONArray("info").getJSONObject(j).getString("message_info"));
                    setGetChatPojoData.setOther_file_type(jsonArray.getJSONObject(i).getJSONArray("info").getJSONObject(j).getString("other_file_type"));
                    setGetChatPojoData.setMsg_attachment(jsonArray.getJSONObject(i).getJSONArray("info").getJSONObject(j).getString("msg_attachment"));
                    setGetChatPojoData.setTime_status(jsonArray.getJSONObject(i).getJSONArray("info").getJSONObject(j).getString("time_status"));
                    setGetChatPojoData.setTime_show(jsonArray.getJSONObject(i).getJSONArray("info").getJSONObject(j).getString("time_show"));
                    setGetChatPojoData.setCom_name(jsonArray.getJSONObject(i).getJSONArray("info").getJSONObject(j).getString("com_name"));
                    setGetChatPojoData.setUsersimage(jsonArray.getJSONObject(i).getJSONArray("info").getJSONObject(j).getString("usersimage"));

                    chatList.add(setGetChatPojoData);
                }
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }

        Collections.reverse(chatList);
        indevidualChatAdapter=new IndevidualChatAdapter(this, chatList);
        chat_list.setAdapter(indevidualChatAdapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            onBackPressed();

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
