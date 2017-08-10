package com.android.llc.proringer.fragments.main_content;

import android.app.ProgressDialog;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.android.llc.proringer.R;
import com.android.llc.proringer.helper.ProServiceApiHelper;
import com.android.llc.proringer.utils.Logger;
import com.android.llc.proringer.viewsmod.textview.ProRegularTextView;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by su on 7/19/17.
 */

public class FaqFragment extends Fragment {
    LinearLayout linear_main_container;
    ProgressDialog pgDialog;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_faq, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setTitle("");
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        linear_main_container = (LinearLayout) view.findViewById(R.id.linear_main_container);

        ProServiceApiHelper.getInstance(getActivity()).getFaqInformation(new ProServiceApiHelper.faqCallback() {
            @Override
            public void onStart() {
                pgDialog = new ProgressDialog(getActivity());
                pgDialog.setTitle("Faq");
                pgDialog.setMessage("Fag page loading Please wait...");
                pgDialog.setCancelable(false);
                pgDialog.show();
            }

            @Override
            public void onComplete(String s) {
                if (pgDialog != null && pgDialog.isShowing())
                    pgDialog.dismiss();

                try {
                    JSONObject jsonObject = new JSONObject(s);

                    if (jsonObject.has("faq")) {

                        JSONArray faqArray = jsonObject.getJSONArray("faq");
                        Logger.printMessage("faq", "" + faqArray);

                        for (int i = 0; i < faqArray.length(); i++) {
                            LinearLayout.LayoutParams lparams1 = new LinearLayout.LayoutParams(
                                    LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                            ProRegularTextView tv1 = new ProRegularTextView(getActivity());
                            tv1.setLayoutParams(lparams1);
                            tv1.setText(faqArray.getJSONObject(i).getString("question"));

                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                tv1.setTextColor(getResources().getColor(R.color.colorAccent, null));
                            } else {
                                tv1.setTextColor(getResources().getColor(R.color.colorAccent));
                            }


                            LinearLayout.LayoutParams lparams2 = new LinearLayout.LayoutParams(
                                    LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                            lparams2.setMargins(0, 20, 0, 0);
                            ProRegularTextView tv2 = new ProRegularTextView(getActivity());
                            tv2.setLayoutParams(lparams2);
                            tv2.setText(faqArray.getJSONObject(i).getString("answer"));

                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                tv2.setTextColor(getResources().getColor(R.color.colorTextBlack, null));
                            } else {
                                tv1.setTextColor(getResources().getColor(R.color.colorTextBlack));
                            }

                            linear_main_container.addView(tv1);
                            linear_main_container.addView(tv2);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(String error) {
                if (pgDialog != null && pgDialog.isShowing())
                    pgDialog.dismiss();
            }
        });

        linear_main_container = (LinearLayout) view.findViewById(R.id.linear_main_container);

    }
}
