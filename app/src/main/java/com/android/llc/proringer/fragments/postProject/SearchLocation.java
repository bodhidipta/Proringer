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
import com.android.llc.proringer.R;
import com.android.llc.proringer.activities.ActivityPostProject;
import com.android.llc.proringer.adapter.LocationListAdapter;
import com.android.llc.proringer.helper.ProServiceApiHelper;
import com.android.llc.proringer.pojo.AddressData;
import com.android.llc.proringer.utils.Logger;
import com.android.llc.proringer.viewsmod.edittext.ProRegularEditText;
import java.util.List;

/**
 * Created by su on 7/13/17.
 */

public class SearchLocation extends Fragment {
    ProRegularEditText zip_code_text;
    private boolean zipSearchGoing = false;
    private LocationListAdapter zip_search_adapter = null;
    RecyclerView location_list;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.content_search_zip,container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        zip_code_text = (ProRegularEditText) view.findViewById(R.id.zip_code_text);
        location_list = (RecyclerView) view.findViewById(R.id.location_list);
        location_list.setLayoutManager(new LinearLayoutManager(getActivity()));



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
                zipSearchGoing = false;

                Logger.printMessage("listdata",""+listdata);

                if (zip_search_adapter == null) {
                    zip_search_adapter = new LocationListAdapter(getActivity(), listdata, new LocationListAdapter.onItemelcted() {
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

            @Override
            public void onError(String error) {

            }

            @Override
            public void onStartFetch() {

            }
        });
    }

}
