package com.android.llc.proringer.fragments.postProject;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.android.llc.proringer.R;
import com.android.llc.proringer.activities.ActivityPostProject;
import com.android.llc.proringer.activities.GetStarted;
import com.android.llc.proringer.adapter.PostProjectLocationListAdapter;
import com.android.llc.proringer.appconstant.ProApplication;
import com.android.llc.proringer.helper.ProServiceApiHelper;
import com.android.llc.proringer.pojo.AddressData;
import com.android.llc.proringer.utils.Logger;
import com.android.llc.proringer.viewsmod.edittext.ProRegularEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by su on 7/13/17.
 */

public class SearchLocation extends Fragment {
    List<AddressData> addressDataList;
    ProRegularEditText zip_code_text;
    private boolean zipSearchGoing = false;
    private PostProjectLocationListAdapter zip_search_adapter = null;
    RecyclerView location_list;
    ProgressBar loading_progress;
    ImageView error_progress;
    public boolean outer_block_check=false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.content_search_zip, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        zip_code_text = (ProRegularEditText) view.findViewById(R.id.zip_code_text);
        location_list = (RecyclerView) view.findViewById(R.id.location_list);
        loading_progress = (ProgressBar) view.findViewById(R.id.loading_progress);
        error_progress = (ImageView) view.findViewById(R.id.error_progress);
        location_list.setLayoutManager(new LinearLayoutManager(getActivity()));

        addressDataList = new ArrayList<>();

        if (!ProApplication.getInstance().getUserId().equals("")) {
            zip_code_text.setHint(ProApplication.getInstance().getZipCode());
        } else {

            //////////set current location zip code////
            Logger.printMessage("Lat", "" + ProServiceApiHelper.getInstance(getActivity()).getCurrentLatLng()[0]);
            Logger.printMessage("Lng", "" + ProServiceApiHelper.getInstance(getActivity()).getCurrentLatLng()[1]);
            getCurrentLocationZip();
        }


        zip_code_text.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus)
                    zip_code_text.setHint("");
                else
                    zip_code_text.setHint(ProApplication.getInstance().getZipCode());
            }
        });


        zip_code_text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                searchLocationWithZip(s.toString());
            }
        });

        view.findViewById(R.id.continue_location_section).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                /**
                 * fragment calling
                 */

                if (((ActivityPostProject) getActivity()).selectedAddressData == null) {
                    zip_code_text.setError("Please enter a valid zip code to continue.");
                } else {
                    ((ActivityPostProject) getActivity()).closeKeypad();
                    ((ActivityPostProject) getActivity()).increaseStep();
                    ((ActivityPostProject) getActivity()).changeFragmentNext(6);
                }
            }
        });

    }

    private void searchLocationWithZip(String key) {
        ProServiceApiHelper.getInstance(getActivity()).getSearchArea(key, new ProServiceApiHelper.onSearchZipCallback() {
            @Override
            public void onComplete(List<AddressData> listdata) {
                addressDataList = listdata;

                zipSearchGoing = false;
                loading_progress.setVisibility(View.GONE);
                error_progress.setVisibility(View.GONE);

                Logger.printMessage("addressDataList", "" + addressDataList);

                if (addressDataList != null && addressDataList.size() > 0) {
                    if (zip_search_adapter == null) {
                        zip_search_adapter = new PostProjectLocationListAdapter(getActivity(), addressDataList, new PostProjectLocationListAdapter.onItemelcted() {
                            @Override
                            public void onSelect(int pos, AddressData data) {
                                ((ActivityPostProject) getActivity()).selectedAddressData = data;
                            }
                        });

                        location_list.setAdapter(zip_search_adapter);
                    } else {
                        zip_search_adapter.updateData(listdata);
                    }
                }
            }

            @Override
            public void onError(String error) {

                if (zip_search_adapter != null) {
                    addressDataList.clear();
                    zip_search_adapter.updateData(addressDataList);
                }
                loading_progress.setVisibility(View.GONE);
                error_progress.setVisibility(View.VISIBLE);
            }

            @Override
            public void onStartFetch() {
                loading_progress.setVisibility(View.VISIBLE);
                error_progress.setVisibility(View.GONE);
            }
        });
    }

    public void getCurrentLocationZip() {
        ProServiceApiHelper.getInstance(getActivity()).getZipCodeUsingGoogleApi(new ProServiceApiHelper.getApiProcessCallback() {
            @Override
            public void onStart() {

            }
            @Override
            public void onComplete(String message) {

                try {
                    JSONObject jsonObject = new JSONObject(message);
                    JSONArray jsonArrayResults = jsonObject.getJSONArray("results");
                    if (jsonArrayResults.length() > 0) {

                        for (int i = 0; i < jsonArrayResults.length(); i++) {

                            if (outer_block_check)
                            {
                                break;
                            }
                            JSONArray jsonArrayAddressComponents = jsonArrayResults.getJSONObject(i).getJSONArray("address_components");

                            for (int j = 0; j < jsonArrayAddressComponents.length(); j++) {

                                JSONObject addressObj = jsonArrayAddressComponents.getJSONObject(j);
                                JSONArray jsonArrayType=addressObj.getJSONArray("types");
                                Logger.printMessage("types", "" + jsonArrayType.get(0));

                                if (jsonArrayType.get(0).equals("postal_code"))
                                {
                                    zip_code_text.setHint(addressObj.getString("long_name"));
                                    outer_block_check=true;
                                    break;
                                }
                            }
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onError(String error) {

            }
        });
    }
}
