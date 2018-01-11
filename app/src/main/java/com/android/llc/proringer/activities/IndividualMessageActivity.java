package com.android.llc.proringer.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.llc.proringer.R;
import com.android.llc.proringer.adapter.IndevidualChatAdapter;
import com.android.llc.proringer.appconstant.ProApplication;
import com.android.llc.proringer.cropImagePackage.CropImage;
import com.android.llc.proringer.cropImagePackage.CropImageView;
import com.android.llc.proringer.helper.MyLoader;
import com.android.llc.proringer.helper.ProServiceApiHelper;
import com.android.llc.proringer.pojo.SetGetChatPojoData;
import com.android.llc.proringer.utils.Logger;
import com.android.llc.proringer.utils.PermissionController;
import com.android.llc.proringer.viewsmod.edittext.ProLightEditText;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
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
    MyLoader myLoader = null;
    JSONObject jsonObject;
    LinkedList<SetGetChatPojoData> chatList;
    IndevidualChatAdapter indevidualChatAdapter;
    ImageView img_camaragallery, im_send;
    ProLightEditText edt_sent_message;
    private String mCurrentPhotoPath = "";
    String project_id, id, pro_id;
    TextWatcher mySearchTextWatcher;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.individual_chat_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        chat_list = (RecyclerView) findViewById(R.id.chat_list);
        chat_list.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true));

        img_background = (ImageView) findViewById(R.id.img_background);
        img_camaragallery = (ImageView) findViewById(R.id.img_camaragallery);
        im_send = (ImageView) findViewById(R.id.im_send);

        img_camaragallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(IndividualMessageActivity.this, PermissionController.class);
                intent.setAction(PermissionController.ACTION_READ_STORAGE_PERMISSION);
                startActivityForResult(intent, 200);
            }
        });

        edt_sent_message = (ProLightEditText) findViewById(R.id.edt_sent_message);
        Glide.with(IndividualMessageActivity.this).load(R.drawable.chat_background).centerCrop().into(img_background);

        myLoader = new MyLoader(IndividualMessageActivity.this);

        chatList = new LinkedList<>();

        try {
            jsonObject = new JSONObject(getIntent().getStringExtra("infoarry"));

            id = jsonObject.getString("id");
            project_id = jsonObject.getString("project_id");
            pro_id = jsonObject.getString("pro_id");
            JSONArray message_list = jsonObject.getJSONArray("message_list");

            Logger.printMessage("jsonObject", "-->" + jsonObject);
            Logger.printMessage("project_id", "-->" + project_id);
            Logger.printMessage("id", "-->" + id);
            Logger.printMessage("pro_id", "-->" + pro_id);

            for (int i = 0; i < message_list.length(); i++) {

                for (int j = 0; j < message_list.getJSONObject(i).getJSONArray("info").length(); j++) {

                    SetGetChatPojoData chatPojo = new SetGetChatPojoData();
                    if (j == 0) {
                        chatPojo.setDate(message_list.getJSONObject(i).getString("date"));
                    } else {
                        chatPojo.setDate("");
                    }
                    chatPojo.setUser(message_list.getJSONObject(i).getJSONArray("info").getJSONObject(j).getString("user"));
                    chatPojo.setSender_id(message_list.getJSONObject(i).getJSONArray("info").getJSONObject(j).getString("sender_id"));
                    chatPojo.setReceiver_id(message_list.getJSONObject(i).getJSONArray("info").getJSONObject(j).getString("receiver_id"));
                    chatPojo.setMessage_id(message_list.getJSONObject(i).getJSONArray("info").getJSONObject(j).getString("message_id"));
                    chatPojo.setProject_id(message_list.getJSONObject(i).getJSONArray("info").getJSONObject(j).getString("project_id"));
                    chatPojo.setMessage_info(message_list.getJSONObject(i).getJSONArray("info").getJSONObject(j).getString("message_info"));
                    chatPojo.setOther_file_type(message_list.getJSONObject(i).getJSONArray("info").getJSONObject(j).getString("other_file_type"));
                    chatPojo.setMsg_attachment(message_list.getJSONObject(i).getJSONArray("info").getJSONObject(j).getString("msg_attachment"));
                    chatPojo.setTime_status(message_list.getJSONObject(i).getJSONArray("info").getJSONObject(j).getString("time_status"));
                    chatPojo.setTime_show(message_list.getJSONObject(i).getJSONArray("info").getJSONObject(j).getString("time_show"));
                    chatPojo.setCom_name(message_list.getJSONObject(i).getJSONArray("info").getJSONObject(j).getString("com_name"));
                    chatPojo.setUsersimage(message_list.getJSONObject(i).getJSONArray("info").getJSONObject(j).getString("usersimage"));

                    chatList.add(chatPojo);

                }
            }


            Collections.reverse(chatList);
            indevidualChatAdapter = new IndevidualChatAdapter(IndividualMessageActivity.this, chatList);

            chat_list.setAdapter(indevidualChatAdapter);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        im_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!edt_sent_message.getText().toString().trim().equals("")) {
                    mCurrentPhotoPath = "";
                    sendMessage();
                } else {
                    Toast.makeText(IndividualMessageActivity.this, "Enter Message", Toast.LENGTH_SHORT).show();
                }
            }
        });

//        mySearchTextWatcher = new TextWatcher() {
//            @Override
//            public void afterTextChanged(Editable s) {
//
//            }
//
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//                // your logic here
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                if (s.toString().trim().length() > 0) {
//                    im_send.setVisibility(View.VISIBLE);
//                } else {
//                    im_send.setVisibility(View.GONE);
//                }
//            }
//        };

//        edt_sent_message.addTextChangedListener(mySearchTextWatcher);


    }

    /*  @Override
      public void onError(String error) {

          Log.d("onError","onError");
      }*/
    //}, ProApplication.getInstance().getUserId(), project_id);
    public void onActivityResult(final int requestCode, int resultCode, final Intent data) {
//

        // handle result of CropImageActivity
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {

                mCurrentPhotoPath = result.getUri().toString();

//                      Glide.with(IndividualMessageActivity).load(result.getUri()).fitCenter().into(new GlideDrawableImageViewTarget(image_pager) {
//                          /**
//                           * {@inheritDoc}
//                           * If no {@link GlideAnimation} is given or if the animation does not set the
//                           * {@link Drawable} on the view, the drawable is set using
//                           * {@link ImageView#setImageDrawable(Drawable)}.
//                           *
//                           * @param resource  {@inheritDoc}
//                           * @param animation {@inheritDoc}
//                           */
//                          @Override
//                          public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> animation) {
//                              super.onResourceReady(resource, animation);
//                          }
//                      });

                Log.i("path-->", mCurrentPhotoPath);
                sendMessage();

                Toast.makeText(IndividualMessageActivity.this, "Cropping successful, Sample: " + result.getSampleSize(), Toast.LENGTH_LONG).show();
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Toast.makeText(IndividualMessageActivity.this, "Cropping failed: " + result.getError(), Toast.LENGTH_LONG).show();
            }
        } else if (requestCode == 200 && resultCode == RESULT_OK) {
//               // showImagePickerOption();
            startCropImageActivity(null);
        }
    }

    private void startCropImageActivity(Uri imageUri) {
        Intent intent = CropImage.activity(imageUri)
                .setGuidelines(CropImageView.Guidelines.ON)
                .setMultiTouchEnabled(false)
                .setAspectRatio(4, 3)
                .getIntent(IndividualMessageActivity.this);
        startActivityForResult(intent, CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE);
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

    public void sendMessage() {
        try {
            ProServiceApiHelper.getInstance(IndividualMessageActivity.this).sendMessageAPI(new ProServiceApiHelper.getApiProcessCallback() {
                @Override
                public void onStart() {
                    Logger.printMessage("start", "start");
                }

                @Override
                public void onComplete(String message) {
                    Logger.printMessage("complete", message);
                    updateMessage();
                    Toast.makeText(IndividualMessageActivity.this, message, Toast.LENGTH_SHORT).show();
                    edt_sent_message.setText("");
                }

                @Override
                public void onError(String error) {

                }
            }, project_id, pro_id, URLEncoder.encode(edt_sent_message.getText().toString().trim(), "utf-8"), mCurrentPhotoPath);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public void updateMessage() {
        chatList.clear();
        Log.d("againcall", "yes");
        ProServiceApiHelper.getInstance(IndividualMessageActivity.this).getUserMessageListAPI(new ProServiceApiHelper.getApiProcessCallback() {
            @Override
            public void onStart() {
                Logger.printMessage("start", "start");
            }

            @Override
            public void onComplete(String message) {
                Logger.printMessage("messagesdysy", message);

                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(message);
                    final JSONArray info_array = jsonObject.getJSONArray("info_array");

                    if (info_array.getJSONObject(0).has("all_pro_user_list")) {

                        for (int c = 0; c < info_array.getJSONObject(0).getJSONArray("all_pro_user_list").length(); c++) {

                            if (info_array.getJSONObject(0).getJSONArray("all_pro_user_list").getJSONObject(c).getString("id").trim().equals(id.trim())) {

                                JSONArray message_list = info_array.getJSONObject(0).getJSONArray("all_pro_user_list").getJSONObject(c).getJSONArray("message_list");
                                for (int i = 0; i < message_list.length(); i++) {

                                    for (int j = 0; j < message_list.getJSONObject(i).getJSONArray("info").length(); j++) {

                                        SetGetChatPojoData chatPojo = new SetGetChatPojoData();
                                        if (j == 0) {
                                            chatPojo.setDate(message_list.getJSONObject(i).getString("date"));
                                        } else {
                                            chatPojo.setDate("");
                                        }
                                        chatPojo.setUser(message_list.getJSONObject(i).getJSONArray("info").getJSONObject(j).getString("user"));
                                        chatPojo.setSender_id(message_list.getJSONObject(i).getJSONArray("info").getJSONObject(j).getString("sender_id"));
                                        chatPojo.setReceiver_id(message_list.getJSONObject(i).getJSONArray("info").getJSONObject(j).getString("receiver_id"));
                                        chatPojo.setMessage_id(message_list.getJSONObject(i).getJSONArray("info").getJSONObject(j).getString("message_id"));
                                        chatPojo.setProject_id(message_list.getJSONObject(i).getJSONArray("info").getJSONObject(j).getString("project_id"));
                                        chatPojo.setMessage_info(message_list.getJSONObject(i).getJSONArray("info").getJSONObject(j).getString("message_info"));
                                        chatPojo.setOther_file_type(message_list.getJSONObject(i).getJSONArray("info").getJSONObject(j).getString("other_file_type"));
                                        chatPojo.setMsg_attachment(message_list.getJSONObject(i).getJSONArray("info").getJSONObject(j).getString("msg_attachment"));
                                        chatPojo.setTime_status(message_list.getJSONObject(i).getJSONArray("info").getJSONObject(j).getString("time_status"));
                                        chatPojo.setTime_show(message_list.getJSONObject(i).getJSONArray("info").getJSONObject(j).getString("time_show"));
                                        chatPojo.setCom_name(message_list.getJSONObject(i).getJSONArray("info").getJSONObject(j).getString("com_name"));
                                        chatPojo.setUsersimage(message_list.getJSONObject(i).getJSONArray("info").getJSONObject(j).getString("usersimage"));

                                        chatList.add(chatPojo);
                                    }
                                }
                                Collections.reverse(chatList);
                                indevidualChatAdapter.notifyDataSetChanged();
                            }
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(String error) {
                Logger.printMessage("Error", "-->" + error);
            }
        }, ProApplication.getInstance().getUserId(), project_id);
    }

}

