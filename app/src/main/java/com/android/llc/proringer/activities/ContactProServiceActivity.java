package com.android.llc.proringer.activities;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import com.android.llc.proringer.R;
import com.android.llc.proringer.adapter.CustomListAdapterDialog;
import com.android.llc.proringer.helper.MyCustomAlertListener;
import com.android.llc.proringer.utils.Logger;
import com.android.llc.proringer.viewsmod.textview.ProRegularTextView;
import com.android.llc.proringer.viewsmod.textview.ProSemiBoldTextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ContactProServiceActivity extends AppCompatActivity implements MyCustomAlertListener {

    ImageView img_cancle,dropdown;
    CheckBox checkbox_term;
    PopupWindow popupWindow;
    CustomListAdapterDialog customListAdapterDialog = null;
    String services;
    ProRegularTextView tv_service,tv_terms_guidelines,tv_post_review,tv_title;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_proservice);
        img_cancle=(ImageView)findViewById(R.id.img_cancle);
        dropdown=(ImageView)findViewById(R.id.dropdown);
        tv_service=(ProRegularTextView)findViewById(R.id.tv_service);
        tv_terms_guidelines=(ProRegularTextView)findViewById(R.id.tv_terms_guidelines);
        tv_post_review=(ProRegularTextView)findViewById(R.id.tv_post_review);
        tv_title=(ProRegularTextView)findViewById(R.id.tv_title);
        checkbox_term=(CheckBox)findViewById(R.id.checkbox_term);


        String title=getIntent().getExtras().getString("pros_company_name");
        services=getIntent().getExtras().getString("services");

        Logger.printMessage("services",services);

        tv_title.setText(title);
        img_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        String termsTextOne = "By submitting Your request you agree to the Terms of Use,including your consent to have ProRinger and our patterns contact you to discuss your project at the provided number/address.";
        //String termsTextClick = "Learn More ";
        Spannable word1 = new SpannableString(termsTextOne);
        word1.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.colorTextDark)), 0, termsTextOne.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        tv_terms_guidelines.setText(word1);
        //Spannable word2 = new SpannableString(termsTextClick);
        //word2.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.colorAccent)), 0, termsTextClick.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
       // tv_terms_guidelines.append(word2);

        findViewById(R.id.relative_dropdown).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    JSONArray jsonArrayServices=new JSONArray(services);
                    showDialogServies(view,jsonArrayServices);
                }catch (Exception ex){
                    ex.printStackTrace();
                }
            }
        });
    }

    @Override
    public void callbackForAlert(String result, int i) {

    }

    private void showDialogServies(View v, JSONArray PredictionsJsonArray) {

            popupWindow = new PopupWindow(ContactProServiceActivity.this);
            // Closes the popup window when touch outside.
            popupWindow.setOutsideTouchable(true);
            // Removes default background.
            popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

            View dailogView = getLayoutInflater().inflate(R.layout.dialog_show_place, null);

            RecyclerView rcv_ = (RecyclerView) dailogView.findViewById(R.id.rcv_);
            rcv_.setLayoutManager(new LinearLayoutManager(ContactProServiceActivity.this));



            customListAdapterDialog = new CustomListAdapterDialog(ContactProServiceActivity.this, PredictionsJsonArray, new onOptionSelected() {
                @Override
                public void onItemPassed(int position, JSONObject value) {
                    popupWindow.dismiss();
                    Logger.printMessage("value", "" + value);

                    try {
                        tv_service.setText(value.getString("service_name"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });

            rcv_.setAdapter(customListAdapterDialog);
            // some other visual settings
            popupWindow.setFocusable(false);
            popupWindow.setWidth(WindowManager.LayoutParams.MATCH_PARENT);
            popupWindow.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);

            // set the list view as pop up window content
            popupWindow.setContentView(dailogView);
            popupWindow.showAsDropDown(v, -5, 0);

    }

    public interface onOptionSelected {
        void onItemPassed(int position, JSONObject value);
    }
}
