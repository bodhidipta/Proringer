package com.android.llc.proringer.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.android.llc.proringer.R;
import com.android.llc.proringer.helper.MyLoader;
import com.android.llc.proringer.helper.ProServiceApiHelper;
import com.android.llc.proringer.utils.Logger;
import com.android.llc.proringer.viewsmod.textview.ProRegularTextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by su on 8/23/17.
 */

public class SearchNearProActivity extends AppCompatActivity {

    private boolean outer_block_check = false;
    private MyLoader myLoader = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_near_pro);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((ProRegularTextView) findViewById(R.id.tv_title)).setText("Search Near");

        myLoader = new MyLoader(SearchNearProActivity.this);


        ((EditText) findViewById(R.id.edt_zip)).setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                    Logger.printMessage("edit", "Enter pressed");
                    ProServiceApiHelper.getInstance(SearchNearProActivity.this).setSearchZip(((EditText) findViewById(R.id.edt_zip)).getText().toString());
                    finish();
                }
                return false;
            }
        });

        findViewById(R.id.tv_current_location).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getCurrentLocationZip();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public void getCurrentLocationZip() {
        ProServiceApiHelper.getInstance(SearchNearProActivity.this).getZipCodeUsingGoogleApi(new ProServiceApiHelper.getApiProcessCallback() {
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
                                            Logger.printMessage("postal_code_get", "" + jsonArrayType.get(0));
                                            ProServiceApiHelper.getInstance(SearchNearProActivity.this).setSearchZip(jsonArrayAddressComponents.getJSONObject(j).getString("long_name"));
                                            outer_block_check = true;
                                            break;
                                        } else {
                                            Logger.printMessage("postal_code", "" + jsonArrayType.get(0));
                                        }
                                    }
                                }
                            }
                        }
                        finish();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Logger.printMessage("JSONException", "" + e.getMessage());
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


}
