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

    private List<AddressData> addressDataList;
    private ProRegularEditText zip_code_text;
    private PostProjectLocationListAdapter zip_search_adapter = null;
    private RecyclerView location_list;
    private ProgressBar loading_progress;
    private ImageView error_progress;
    private boolean outer_block_check = false;
    private MyLoader myLoader = null;
    TextWatcher textWatcher = null;

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
        myLoader = new MyLoader(getActivity());
        zip_search_adapter = null;
        ((PostProjectActivity) getActivity()).selectedAddressData = null;
        Logger.printMessage("onCreate", "onCreate");

        textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length() > 4) {
                    Logger.printMessage("GreaterThan4", "" + s.toString().length());
                    searchLocationUsingZip(s.toString());
                }
            }
        };
        zip_code_text.addTextChangedListener(textWatcher);

        if (!ProApplication.getInstance().getUserId().equals("")) {
            plotUserInformation();
        } else {
            //////////set current location zip code////
            Logger.printMessage("not login", "not login");
            Logger.printMessage("Lat", "" + ProServiceApiHelper.getInstance((PostProjectActivity) getActivity()).getCurrentLatLng()[0]);
            Logger.printMessage("Lng", "" + ProServiceApiHelper.getInstance((PostProjectActivity) getActivity()).getCurrentLatLng()[1]);
            getCurrentLocationZip();
        }

        zip_code_text.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    zip_code_text.setText("");
                    ((PostProjectActivity) getActivity()).selectedAddressData = null;
                    addressDataList.clear();
                }
//                else {
//                    zip_code_text.setText(ProApplication.getInstance().getZipCode());
//                }
            }
        });


        view.findViewById(R.id.continue_location_section).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /**
                 * fragment calling
                 */

                if (addressDataList != null & addressDataList.size() > 0) {
                    ((PostProjectActivity) getActivity()).closeKeypad();
                    ((PostProjectActivity) getActivity()).increaseStep();
                    ((PostProjectActivity) getActivity()).changeFragmentNext(6);
                    addressDataList.clear();
                } else {
                    zip_code_text.setError("Please enter a valid zip code to continue.");
                }
            }
        });

    }


    private void searchLocationUsingZipFirstTime(String key) {
        Logger.printMessage("key",""+key);
        ProServiceApiHelper.getInstance(getActivity()).getSearchArea(new ProServiceApiHelper.onSearchZipCallback() {
            @Override
            public void onComplete(List<AddressData> listdata) {
                try {
                    addressDataList = listdata;
                    Logger.printMessage("addressDataList", "" + addressDataList.size());

                    if (addressDataList != null && addressDataList.size() > 0) {
                        if (addressDataList.get(0).getCountry_code().equals("US") ||
                                addressDataList.get(0).getCountry_code().equals("CA")) {
                            ((PostProjectActivity) getActivity()).selectedAddressData = addressDataList.get(0);
                        }
                    }
                }catch (Exception e){
                    Logger.printMessage("Exception",""+e.getMessage());
                    addressDataList.clear();
                }
            }

            @Override
            public void onError(String error) {
                Logger.printMessage("error", "" + error);
                addressDataList.clear();
            }

            @Override
            public void onStartFetch() {
                addressDataList.clear();
            }
        }, key);
    }

    private void searchLocationUsingZip(String key) {
        ProServiceApiHelper.getInstance((PostProjectActivity) getActivity()).getSearchArea(new ProServiceApiHelper.onSearchZipCallback() {
            @Override
            public void onComplete(List<AddressData> listdata) {

                try {
                    addressDataList = listdata;
                    Logger.printMessage("addressDataList", "" + addressDataList.size());

                    loading_progress.setVisibility(View.GONE);
                    error_progress.setVisibility(View.GONE);

                    if (addressDataList != null && addressDataList.size() > 0) {
                        if (addressDataList.get(0).getCountry_code().equals("US") ||
                                addressDataList.get(0).getCountry_code().equals("CA")) {
                            ((PostProjectActivity) getActivity()).selectedAddressData = addressDataList.get(0);

                            if (zip_search_adapter == null) {
                                zip_search_adapter = new PostProjectLocationListAdapter(getActivity(), addressDataList, new PostProjectLocationListAdapter.onItemelcted() {
                                    @Override
                                    public void onSelect(int pos, AddressData data) {
                                        //((PostProjectActivity) getActivity()).selectedAddressData = data;
                                    }
                                });
                                location_list.setAdapter(zip_search_adapter);
                            } else {
                                zip_search_adapter.updateData(listdata);
                            }
                        }
                    }
                }catch (Exception e){
                    Logger.printMessage("Exception",""+e.getMessage());
                }
            }

            @Override
            public void onError(String error) {
                Logger.printMessage("error", "" + error);
                addressDataList.clear();
                loading_progress.setVisibility(View.GONE);
                error_progress.setVisibility(View.VISIBLE);
            }

            @Override
            public void onStartFetch() {
                addressDataList.clear();
                loading_progress.setVisibility(View.VISIBLE);
                error_progress.setVisibility(View.GONE);
            }
        }, key);
    }

    ////////////////////If the user offline then this function called first for current zip/////////////////////
    public void getCurrentLocationZip() {
        ProServiceApiHelper.getInstance(getActivity()).getZipCodeUsingGoogleApi(new ProServiceApiHelper.getApiProcessCallback() {
            @Override
            public void onStart() {
                myLoader.showLoader();
            }

            @Override
            public void onComplete(String message) {
                //Logger.printMessage("message",message);
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
                                            Logger.printMessage("postal_code_get",""+jsonArrayType.get(0));
                                            zip_code_text.setText(jsonArrayAddressComponents.getJSONObject(j).getString("long_name"));
                                            searchLocationUsingZipFirstTime(zip_code_text.getText().toString());
                                            outer_block_check = true;
                                            break;
                                        }else {
                                            Logger.printMessage("postal_code",""+jsonArrayType.get(0));
                                        }
                                    }
                                }
                            }
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Logger.printMessage("JSONException",""+e.getMessage());
                    if (myLoader != null && myLoader.isMyLoaderShowing())
                        myLoader.dismissLoader();
                }
            }

            @Override
            public void onError(String error) {
                Logger.printMessage("error", "" + error);
                if (myLoader != null && myLoader.isMyLoaderShowing())
                    myLoader.dismissLoader();
            }
        });
    }

    private void plotUserInformation() {
        DatabaseHandler.getInstance(getActivity()).getUserInfo(
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
                                Logger.printMessage("key_in","key_in");
                                searchLocationUsingZipFirstTime(zip_code_text.getText().toString());
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
