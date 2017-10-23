package com.android.llc.proringer.activities;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;

import com.android.llc.proringer.R;
import com.android.llc.proringer.adapter.ProsDetailsPortfolioImageAdapter;
import com.android.llc.proringer.helper.MyLoader;
import com.android.llc.proringer.helper.ProServiceApiHelper;
import com.android.llc.proringer.utils.Logger;
import com.android.llc.proringer.utils.MethodsUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by su on 10/23/17.
 */

public class ProsProjectGalleryActivity extends AppCompatActivity {
    MyLoader myLoader = null;
    RecyclerView rcv_portfolio;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pros_project_gallery);
        rcv_portfolio = (RecyclerView)findViewById(R.id.rcv_portfolio);
        rcv_portfolio.setLayoutManager(new GridLayoutManager(ProsProjectGalleryActivity.this,3));
        myLoader = new MyLoader(this);
        loadImagePortFolio(getIntent().getStringExtra("portfolio_id"));
        findViewById(R.id.img_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent backIntent = new Intent();
                setResult(RESULT_CANCELED, backIntent );
                finish();
            }
        });
    }

    public void loadImagePortFolio(String portfolio_id) {

        ProServiceApiHelper.getInstance(ProsProjectGalleryActivity.this).getProIndividualPortfolioImage(new ProServiceApiHelper.getApiProcessCallback() {
            @Override
            public void onStart() {
                myLoader.showLoader();
            }

            @Override
            public void onComplete(String message) {
                if (myLoader != null && myLoader.isMyLoaderShowing())
                    myLoader.dismissLoader();
                try {
                    JSONObject portfolioObj = new JSONObject(message);
                    JSONArray portfolioInfoArray = portfolioObj.getJSONArray("info_array");

                    Logger.printMessage("portfolioInfoArray-->",""+portfolioInfoArray);

                    ProsDetailsPortfolioImageAdapter prosDetailsPortfolioImageAdapter = new ProsDetailsPortfolioImageAdapter(ProsProjectGalleryActivity.this, portfolioInfoArray);
                    rcv_portfolio.setAdapter(prosDetailsPortfolioImageAdapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                    if (myLoader != null && myLoader.isMyLoaderShowing())
                        myLoader.dismissLoader();
                }

            }

            @Override
            public void onError(String error) {
                if (myLoader != null && myLoader.isMyLoaderShowing())
                    myLoader.dismissLoader();
            }
        }, portfolio_id);
    }

//    public void showImagePortFolioDialog(String portfolio_id) {
//
//        ProServiceApiHelper.getInstance(ProsProjectDetailsActivity.this).getProIndividualPortfolioImage(new ProServiceApiHelper.getApiProcessCallback() {
//            @Override
//            public void onStart() {
//                myLoader.showLoader();
//            }
//
//            @Override
//            public void onComplete(String message) {
//                if (myLoader != null && myLoader.isMyLoaderShowing())
//                    myLoader.dismissLoader();
//                try {
//                    JSONObject portfolioObj = new JSONObject(message);
//                    JSONArray portfolioInfoArray = portfolioObj.getJSONArray("info_array");
//
//
//                    final Dialog dialog = new Dialog(ProsProjectDetailsActivity.this);
//                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
////                    dialog.setCancelable(false);
//                    dialog.setContentView(R.layout.custom_dialogbox_portfolio);
//                    //dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
//                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
//
//                    dialog.setCanceledOnTouchOutside(false);
//                    dialog.setCancelable(false);
//
//
//                    dialog.findViewById(R.id.RLMain).getLayoutParams().width = (MethodsUtils.getScreenHeightAndWidth(ProsProjectDetailsActivity.this)[1] - 30);
//                    dialog.findViewById(R.id.RLMain).getLayoutParams().height = MethodsUtils.getScreenHeightAndWidth(ProsProjectDetailsActivity.this)[0] / 2;
//                    //        scrollView.getLayoutParams().height = (height-30)/2;
//
//                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
//
//                    RecyclerView rcv_portfolio = (RecyclerView) dialog.findViewById(R.id.rcv_portfolio);
//                    rcv_portfolio.setLayoutManager(new LinearLayoutManager(ProsProjectDetailsActivity.this, LinearLayoutManager.HORIZONTAL, false));
//
//                    ProsDetailsPortfolioImageAdapter prosDetailsPortfolioImageAdapter = new ProsDetailsPortfolioImageAdapter(ProsProjectDetailsActivity.this, portfolioInfoArray);
//                    rcv_portfolio.setAdapter(prosDetailsPortfolioImageAdapter);
//
//                    dialog.findViewById(R.id.img_cancel_dialog).setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//                            dialog.dismiss();
//                        }
//                    });
//
//
//                    dialog.show();
//
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                    if (myLoader != null && myLoader.isMyLoaderShowing())
//                        myLoader.dismissLoader();
//                }
//
//            }
//
//            @Override
//            public void onError(String error) {
//                if (myLoader != null && myLoader.isMyLoaderShowing())
//                    myLoader.dismissLoader();
//            }
//        }, portfolio_id);
//    }

}
