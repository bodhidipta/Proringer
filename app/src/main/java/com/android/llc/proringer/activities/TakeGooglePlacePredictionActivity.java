package com.android.llc.proringer.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import com.android.llc.proringer.R;
import com.android.llc.proringer.adapter.PlaceCustomListAdapter;
import com.android.llc.proringer.helper.ProServiceApiHelper;
import com.android.llc.proringer.utils.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

/**
 * Created by su on 8/1/17.
 */

public class TakeGooglePlacePredictionActivity extends AppCompatActivity{

    ImageView Erase;
    RecyclerView rcv_location_suggestion;
    EditText locationText;
    String selectedPlace = "",city="",state="",zip_code="";
    boolean AfterPrressNoCallTextChangerListener=false;
    private PlaceCustomListAdapter placeCustomListAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_place);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(""+getResources().getString(R.string.select_address));

        rcv_location_suggestion = (RecyclerView) findViewById(R.id.rcv_location_suggestion);
        rcv_location_suggestion.setLayoutManager(new LinearLayoutManager(TakeGooglePlacePredictionActivity.this));


        locationText = (EditText) findViewById(R.id.edt_location);

        Erase=(ImageView)findViewById(R.id.Erase);
        Erase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                locationText.setText("");
                Erase.setVisibility(View.GONE);
                fetch_LocationSuggession("");
                rcv_location_suggestion.setVisibility(View.GONE);
            }
        });
        locationText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!AfterPrressNoCallTextChangerListener) {
                    fetch_LocationSuggession(s.toString().trim());
                }else {
                    AfterPrressNoCallTextChangerListener=false;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

                if(locationText.getText().length()>0)
                {
                    Erase.setVisibility(View.VISIBLE);
                }else {
                    Erase.setVisibility(View.GONE);
                }
            }
        });
    }


    public void fetch_LocationSuggession(String place) {

        ProServiceApiHelper.getInstance(TakeGooglePlacePredictionActivity.this).getSearchCountriesByPlacesFilter(new ProServiceApiHelper.onSearchPlacesNameCallback() {

            @Override
            public void onComplete(ArrayList<String> listData) {

                if (listData.size()>0){
                    placeCustomListAdapter=new PlaceCustomListAdapter(TakeGooglePlacePredictionActivity.this, listData, new TakeGooglePlacePredictionActivity.onOptionSelected() {
                        @Override
                        public void onItemPassed(int position, ArrayList<String> stringArrayList) {

                            AfterPrressNoCallTextChangerListener=true;

                            locationText.setText(stringArrayList.get(position));
                            selectedPlace=stringArrayList.get(position);
                            rcv_location_suggestion.setVisibility(View.GONE);
                            getZipCityState();

                        }
                    });
                    rcv_location_suggestion.setVisibility(View.VISIBLE);
                    rcv_location_suggestion.setAdapter(placeCustomListAdapter);
                }else {
                    rcv_location_suggestion.setVisibility(View.GONE);
                }

                ViewGroup.LayoutParams params = rcv_location_suggestion.getLayoutParams();
                rcv_location_suggestion.setLayoutParams(params);
                rcv_location_suggestion.requestLayout();
            }

            @Override
            public void onError(String error) {

            }

            @Override
            public void onStartFetch() {

            }
        },place);
    }

    public void getZipCityState(){
        ProServiceApiHelper.getInstance(TakeGooglePlacePredictionActivity.this).getZipLocationStateAPI(new ProServiceApiHelper.getApiProcessCallback() {
            @Override
            public void onStart() {

            }

            @Override
            public void onComplete(String message) {
                try {
                    JSONObject mainRes = new JSONObject(message);

                    if (mainRes.getString("status").equalsIgnoreCase("OK") &&
                            mainRes.has("results") &&
                            mainRes.getJSONArray("results").length() > 0) {

                        JSONArray results = mainRes.getJSONArray("results");

                        JSONObject innerIncer = results.getJSONObject(0);

                        /**
                         * loop through address component
                         * for country and state
                         */
                        if (innerIncer.has("address_components") &&
                                innerIncer.getJSONArray("address_components").length() > 0) {

                            Logger.printMessage("address_components",""+innerIncer.getJSONArray("address_components"));

                            JSONArray address_components = innerIncer.getJSONArray("address_components");

                            for (int j = 0; j < address_components.length(); j++) {

                                if (address_components.getJSONObject(j).has("types") &&
                                        address_components.getJSONObject(j).getJSONArray("types").length() > 0
                                        ) {

                                    JSONArray types = address_components.getJSONObject(j).getJSONArray("types");

                                    for (int k = 0; k < types.length(); k++) {
                                        if (types.getString(k).equals("administrative_area_level_2")) {
                                            city=address_components.getJSONObject(j).getString("short_name");
                                            Logger.printMessage("city","-->"+city);
                                        }
                                        if (types.getString(k).equals("administrative_area_level_1")) {
                                            state=address_components.getJSONObject(j).getString("short_name");
                                            Logger.printMessage("state","-->"+state);
                                        }

                                        if (types.getString(k).equals("postal_code")) {
                                            zip_code=address_components.getJSONObject(j).getString("short_name");
                                            Logger.printMessage("zip_code","-->"+zip_code);
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

            }
        },selectedPlace);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_location_finder, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            Intent i = new Intent();
            i.putExtra("data", selectedPlace);
            setResult(0, i);
            try {
                InputMethodManager inputMethodManager = (InputMethodManager) this.getSystemService(Activity.INPUT_METHOD_SERVICE);
                View view = this.getCurrentFocus();
                if (view == null) {
                    view = new View(TakeGooglePlacePredictionActivity.this);
                }
                inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
            } catch (Exception e) {
                e.printStackTrace();
            }
            finish();
            return true;
        } else if (id == R.id.action_Done) {

            Intent i = new Intent();
            Bundle BD = new Bundle();

            BD.putString("selectedPlace", selectedPlace);
            BD.putString("zip", zip_code);
            BD.putString("city", city);
            BD.putString("state", state);

            i.putExtra("data", BD);
            setResult(Activity.RESULT_OK,i);

            //setResult(-1, i);
            try {
                InputMethodManager inputMethodManager = (InputMethodManager) this.getSystemService(Activity.INPUT_METHOD_SERVICE);
                View view = this.getCurrentFocus();
                if (view == null) {
                    view = new View(TakeGooglePlacePredictionActivity.this);
                }
                inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
            } catch (Exception e) {
                e.printStackTrace();
            }
            onBackPressed();
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public interface onOptionSelected {
        void onItemPassed(int position,ArrayList<String> stringArrayList);
    }

}
