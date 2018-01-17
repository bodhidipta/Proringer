package com.android.llc.proringer.fragments.bottomNav;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.llc.proringer.R;
import com.android.llc.proringer.activities.LandScreenActivity;
import com.android.llc.proringer.adapter.ProjectMessageAdapter;
import com.android.llc.proringer.appconstant.ProApplication;
import com.android.llc.proringer.helper.CustomAlert;
import com.android.llc.proringer.helper.MyCustomAlertListener;
import com.android.llc.proringer.helper.MyLoader;
import com.android.llc.proringer.helper.ProServiceApiHelper;
import com.android.llc.proringer.helper.SimpleDividerItemDecoration;
import com.android.llc.proringer.pojo.SetGetProjectMessage;
import com.android.llc.proringer.utils.Logger;
import com.android.llc.proringer.viewsmod.edittext.ProRegularEditText;
import com.android.llc.proringer.viewsmod.textview.ProRegularTextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by bodhidipta on 12/06/17.
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
 */

public class MessageFragment extends Fragment{
    private RecyclerView rcv;
    private ProjectMessageAdapter projectMessageAdapter;
    MyLoader myLoader = null;
    String list_search="";
    ArrayList<SetGetProjectMessage> setGetProjectMessageArrayList;
    private InputMethodManager keyboard;
    ImageView img_clear;
    ProRegularEditText edt_search;
    TextWatcher mySearchTextWatcher;
    LinearLayout LLMain, LLNetworkDisconnection;
    ProRegularTextView tv_empty_show;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.messages, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setGetProjectMessageArrayList = new ArrayList<>();
        myLoader = new MyLoader(getActivity());

        LLMain = (LinearLayout) view.findViewById(R.id.LLMain);
        LLNetworkDisconnection = (LinearLayout) view.findViewById(R.id.LLNetworkDisconnection);

        tv_empty_show=(ProRegularTextView)view.findViewById(R.id.tv_empty_show);
        tv_empty_show.setVisibility(View.GONE);

        rcv = (RecyclerView) view.findViewById(R.id.message_list);
        rcv.setLayoutManager(new LinearLayoutManager((LandScreenActivity) getActivity()));
        rcv.addItemDecoration(new SimpleDividerItemDecoration(getActivity()));

        keyboard = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);

        img_clear = (ImageView) view.findViewById(R.id.img_clear);
        img_clear.setVisibility(View.GONE);

        edt_search = (ProRegularEditText) view.findViewById(R.id.edt_search);

        loadList();


        edt_search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                    Logger.printMessage("search_category", edt_search.getText().toString());
                    closeKeypad();
                    loadList();
                } else if ((event != null && (actionId == KeyEvent.KEYCODE_DEL))) {
                    if (edt_search.getText().toString().equals("")) {
                        Logger.printMessage("search_category", edt_search.getText().toString());
                        closeKeypad();
                        loadList();
                    }
                }
                return false;
            }
        });

        img_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edt_search.setText("");
                list_search = "";
                loadList();
                closeKeypad();
            }
        });


        mySearchTextWatcher = new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // your logic here
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // your logic here
                list_search = s.toString().trim();
                if (list_search.length() > 0) {
                    img_clear.setVisibility(View.VISIBLE);
                } else {
                    img_clear.setVisibility(View.GONE);
                }
//                if(category_search.length()==0){
//                    closeKeypad();
//                    loadList();
//                }
                //loadCategoryList();
            }
        };

        edt_search.addTextChangedListener(mySearchTextWatcher);

    }

    public void loadList() {

        setGetProjectMessageArrayList.clear();

        ProServiceApiHelper.getInstance((LandScreenActivity) getActivity()).getUserMessageListAPI(new ProServiceApiHelper.getApiProcessCallback() {
            @Override
            public void onStart() {
                myLoader.showLoader();
            }

            @Override
            public void onComplete(String message) {
                if (myLoader != null && myLoader.isMyLoaderShowing())
                    myLoader.dismissLoader();
                try {
                    JSONObject jsonObject = new JSONObject(message);

                    if (jsonObject.has("info_array")) {

                        if (jsonObject.getJSONArray("info_array").length()>0) {

                            rcv.setVisibility(View.VISIBLE);
                            tv_empty_show.setVisibility(View.GONE);

                            for (int i = 0; i < jsonObject.getJSONArray("info_array").length(); i++) {
                                SetGetProjectMessage setGetProjectMessage = new SetGetProjectMessage();
                                setGetProjectMessage.setProj_id(jsonObject.getJSONArray("info_array").getJSONObject(i).getString("proj_id"));
                                setGetProjectMessage.setProj_image(jsonObject.getJSONArray("info_array").getJSONObject(i).getString("proj_image"));
                                setGetProjectMessage.setProj_name(jsonObject.getJSONArray("info_array").getJSONObject(i).getString("proj_name"));
                                setGetProjectMessage.setStatus(jsonObject.getJSONArray("info_array").getJSONObject(i).getString("status"));
                                setGetProjectMessage.setProject_date(jsonObject.getJSONArray("info_array").getJSONObject(i).getString("project_date"));
                                setGetProjectMessage.setNo_of_pros_user(jsonObject.getJSONArray("info_array").getJSONObject(i).getString("no_of_pro_user"));
                                setGetProjectMessage.setRead_status(jsonObject.getJSONArray("info_array").getJSONObject(i).getInt("read_status"));
                                setGetProjectMessageArrayList.add(setGetProjectMessage);
                            }


                            JSONArray info_array = jsonObject.getJSONArray("info_array");


                            Logger.printMessage("info_array", "" + info_array);

                            if (projectMessageAdapter == null) {
                                Logger.printMessage("projectMessageAdapter", "null");
                                projectMessageAdapter = new ProjectMessageAdapter((LandScreenActivity) getActivity(), setGetProjectMessageArrayList, new onOptionSelected() {
                                    @Override
                                    public void onItemPassed(int position, String project_id) {
                                        ((LandScreenActivity) getActivity()).toggleToolBar(true);
                                        ((LandScreenActivity) getActivity()).transactProjectMessaging(project_id);
                                    }
                                });

                                rcv.setAdapter(projectMessageAdapter);
                            } else {
                                Logger.printMessage("projectMessageAdapter", "not null");
                                projectMessageAdapter.notifyDataSetChanged();
                            }
                        }
                        else {
                            rcv.setVisibility(View.GONE);
                            tv_empty_show.setVisibility(View.VISIBLE);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(String error) {
                if (myLoader != null && myLoader.isMyLoaderShowing())
                    myLoader.dismissLoader();

                if (error.equalsIgnoreCase(getActivity().getResources().getString(R.string.no_internet_connection_found_Please_check_your_internet_connection))) {
                    LLMain.setVisibility(View.GONE);
                    LLNetworkDisconnection.setVisibility(View.VISIBLE);
                }
            }
        }, ProApplication.getInstance().getUserId(), "",list_search);

    }


    public interface onOptionSelected {
        void onItemPassed(int position, String value);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    public void closeKeypad() {
        try {
            keyboard.hideSoftInputFromWindow(getActivity().getWindow().getDecorView().getWindowToken(), 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
