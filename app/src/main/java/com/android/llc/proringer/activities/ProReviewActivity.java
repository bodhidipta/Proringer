package com.android.llc.proringer.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;

import com.android.llc.proringer.R;
import com.android.llc.proringer.utils.Logger;
import com.android.llc.proringer.viewsmod.textview.ProRegularTextView;

/**
 * Created by su on 8/1/17.
 */

public class ProReviewActivity extends AppCompatActivity {
    ProRegularTextView tv_terms_guidelines;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.review_pro);
        tv_terms_guidelines= (ProRegularTextView) findViewById(R.id.tv_terms_guidelines);

        /**
         * Contact us spannable text with click listener
         */
        String termsTextOne = "By submitting a review,I acknowledge that I have appect the";
        String termsTextClick = " Terms of use ";
        String reviewGuidelineTextTwo = "and the";
        String reviewGuidelinesClick=" Review Guidelines.";


        Spannable word1 = new SpannableString(termsTextOne);
        word1.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.colorTextDark)), 0, termsTextOne.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        tv_terms_guidelines.setText(word1);


        Spannable word2 = new SpannableString(termsTextClick);
        ClickableSpan myTermsClickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                // There is the OnCLick. put your intent to Register class here
                widget.invalidate();
                Logger.printMessage("SpanHello", "click");
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                ds.setColor(getResources().getColor(R.color.colorAccent));
                ds.setUnderlineText(false);
            }
        };
        word2.setSpan(myTermsClickableSpan, 0, termsTextClick.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        tv_terms_guidelines.append(word2);


        Spannable word3 = new SpannableString(reviewGuidelineTextTwo);
        word3.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.colorTextDark)), 0, reviewGuidelineTextTwo.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        tv_terms_guidelines.append(word3);


        Spannable word4 = new SpannableString(termsTextClick);
        ClickableSpan myReviewGuidelinesClick = new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                // There is the OnCLick. put your intent to Register class here
                widget.invalidate();
                Logger.printMessage("SpanHello", "click");
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                ds.setColor(getResources().getColor(R.color.colorAccent));
                ds.setUnderlineText(false);
            }
        };
        word4.setSpan(myReviewGuidelinesClick, 0, termsTextClick.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        tv_terms_guidelines.append(word4);

    }
}
