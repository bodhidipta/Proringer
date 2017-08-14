package com.android.llc.proringer.fragments.postProject;

import android.app.ProgressDialog;
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
import android.widget.Toast;
import com.android.llc.proringer.R;
import com.android.llc.proringer.activities.PostProjectActivity;
import com.android.llc.proringer.adapter.PostProjectLocationListAdapter;
import com.android.llc.proringer.appconstant.ProApplication;
import com.android.llc.proringer.database.DatabaseHandler;
import com.android.llc.proringer.helper.MyLoader;
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

public class SearchLocationFragment extends Fragment {
    List<AddressData> addressDataList;
    ProRegularEditText zip_code_text;

    private PostProjectLocationListAdapter zip_search_adapter = null;
    RecyclerView location_list;
    ProgressBar loading_progress;
    ImageView error_progress;
    public boolean outer_block_check = false;
    MyLoader myLoader=null;

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
        location_list.setLayoutManager(new LinearLayoutManager((PostProjectActivity) getActivity()));

        addressDataList = new ArrayList<>();

        myLoader=new MyLoader(getActivity());


        ((PostProjectActivity) getActivity()).selectedAddressData =null;

        if (!ProApplication.getInstance().getUserId().equals("")) {
            plotUserInformation();
        } else {
            //////////set current location zip code////
            Logger.printMessage("Lat", "" + ProServiceApiHelper.getInstance((PostProjectActivity) getActivity()).getCurrentLatLng()[0]);
            Logger.printMessage("Lng", "" + ProServiceApiHelper.getInstance((PostProjectActivity) getActivity()).getCurrentLatLng()[1]);
            getCurrentLocationZip();
        }

        zip_code_text.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus)
                    zip_code_text.setText("");
                else
                    zip_code_text.setText(ProApplication.getInstance().getZipCode());
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

                if(addressDataList!=null & addressDataList.size()>0) {

                    if (((PostProjectActivity) getActivity()).selectedAddressData == null) {
                        //zip_code_text.setError("Please enter a valid zip code to continue.");
                        Toast.makeText(getActivity(),"Please choose a location from list to continue.",Toast.LENGTH_SHORT).show();
                    } else {
                        ((PostProjectActivity) getActivity()).closeKeypad();
                        ((PostProjectActivity) getActivity()).increaseStep();
                        ((PostProjectActivity) getActivity()).changeFragmentNext(6);
                    }
                }
                else {
                    zip_code_text.setError("Please enter a valid zip code to continue.");
                }
            }
        });

    }

    private void searchLocationWithZip(String key) {
        ProServiceApiHelper.getInstance((PostProjectActivity) getActivity()).getSearchArea(new ProServiceApiHelper.onSearchZipCallback() {
            @Override
            public void onComplete(List<AddressData> listdata) {
                addressDataList = listdata;

                loading_progress.setVisibility(View.GONE);
                error_progress.setVisibility(View.GONE);

                Logger.printMessage("addressDataList", "" + addressDataList.size());

                if (addressDataList != null && addressDataList.size() > 0) {
                    if (zip_search_adapter == null) {
                        zip_search_adapter = new PostProjectLocationListAdapter(getActivity(), addressDataList, new PostProjectLocationListAdapter.onItemelcted() {
                            @Override
                            public void onSelect(int pos, AddressData data) {
                                ((PostProjectActivity) getActivity()).selectedAddressData = data;
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
        }, key);
    }

    public void getCurrentLocationZip() {
        ProServiceApiHelper.getInstance((PostProjectActivity) getActivity()).getZipCodeUsingGoogleApi(new ProServiceApiHelper.getApiProcessCallback() {
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
                    JSONArray jsonArrayResults = jsonObject.getJSONArray("results");
                    if (jsonArrayResults.length() > 0) {

                        for (int i = 0; i < jsonArrayResults.length(); i++) {

                            if (outer_block_check) {
                                break;
                            }

                            /**
                             * loop through address component
                             * for country and state
                             */
                            if (jsonArrayResults.getJSONObject(i).has("address_components") &&
                                    jsonArrayResults.getJSONObject(i).getJSONArray("address_components").length() > 0) {
                                JSONArray jsonArrayAddressComponents = jsonArrayResults.getJSONObject(i).getJSONArray("address_components");

                                for (int j = 0; j < jsonArrayAddressComponents.length(); j++) {

                                    if (jsonArrayAddressComponents.getJSONObject(j).has("types") &&
                                            jsonArrayAddressComponents.getJSONObject(j).getJSONArray("types").length() > 0
                                            ) {

                                        JSONArray jsonArrayType = jsonArrayAddressComponents.getJSONObject(j).getJSONArray("types");
                                        Logger.printMessage("types", "" + jsonArrayType.get(0));

                                        if (jsonArrayType.get(0).equals("postal_code")) {
                                            zip_code_text.setText(jsonArrayAddressComponents.getJSONObject(j).getString("long_name"));
                                            searchLocationWithZip(zip_code_text.getText().toString());
                                            outer_block_check = true;
                                            break;
                                        }
                                    }
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
                if (myLoader != null && myLoader.isMyLoaderShowing())
                    myLoader.dismissLoader();
            }
        });
    }

    private void plotUserInformation() {
        DatabaseHandler.getInstance((PostProjectActivity) getActivity()).getUserInfo(
                ProApplication.getInstance().getUserId(),
                new DatabaseHandler.onQueryCompleteListener() {
                    @Override
                    public void onSuccess(String... s) {
                        /**
                         * User data already found in database
                         */

                        Logger.printMessage("@dashBoard", "on database data exists");
                        try {
                            JSONObject mainObject = new JSONObject(s[0]);
                            JSONArray info_arr = mainObject.getJSONArray("info_array");
                            JSONObject innerObj = info_arr.getJSONObject(0);

                            Logger.printMessage("zipCode", "zipCode:-" + innerObj.getString("zipcode"));

                            if (innerObj.getString("zipcode").trim().equals("")) {
                                zip_code_text.setText("");
                            } else {
                                zip_code_text.setText(innerObj.getString("zipcode") + "");
                                searchLocationWithZip(zip_code_text.getText().toString());
                            }
                        } catch (JSONException jse) {
                            jse.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(String s) {
                        /**
                         * No user data found on database or something went wrong
                         */
                        Logger.printMessage("@dashBoard", "on database data not exists");

                    }
                });
    }
}
