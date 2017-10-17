package com.android.llc.proringer.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.llc.proringer.R;
import com.android.llc.proringer.helper.MyCustomAlertListener;

public class ContactProServiceActivity extends AppCompatActivity implements MyCustomAlertListener {

    ImageView img_cancle,dropdown;
    TextView tv_service,tv_terms_guidelines,et_btn_submit,tv_post_review,tv_title;
    CheckBox checkbox_term;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_proservice);
        img_cancle=(ImageView)findViewById(R.id.img_cancle);
        dropdown=(ImageView)findViewById(R.id.dropdown);
        tv_service=(TextView)findViewById(R.id.tv_service);
        tv_terms_guidelines=(TextView)findViewById(R.id.tv_terms_guidelines);
        et_btn_submit=(TextView)findViewById(R.id.et_btn_submit);
        tv_post_review=(TextView)findViewById(R.id.tv_post_review);
        tv_title=(TextView)findViewById(R.id.tv_title);
        checkbox_term=(CheckBox)findViewById(R.id.checkbox_term);
        String title=getIntent().getExtras().getString("pros_company_name");
        tv_title.setText(title);
        img_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        String termsTextOne = "By submitting Your request you agree to the Terms of Use,including consent to have ProRinger and our patterns contact you to discuss your project at the provided number/address.Your consent to making communication is  not required to make purchase.";
        String termsTextClick = "Learn More ";
        Spannable word1 = new SpannableString(termsTextOne);
        word1.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.colorTextDark)), 0, termsTextOne.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        tv_terms_guidelines.setText(word1);
        Spannable word2 = new SpannableString(termsTextClick);
        word2.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.colorAccent)), 0, termsTextClick.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        tv_terms_guidelines.append(word2);

    }

    @Override
    public void callbackForAlert(String result, int i) {

    }
}
