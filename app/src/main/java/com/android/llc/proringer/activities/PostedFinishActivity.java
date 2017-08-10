package com.android.llc.proringer.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;

import com.android.llc.proringer.R;
import com.android.llc.proringer.utils.Logger;
import com.android.llc.proringer.viewsmod.textview.ProRegularTextView;

/**
 * Created by su on 7/14/17.
 */

public class PostedFinishActivity extends AppCompatActivity {
    ProRegularTextView mail_resent, contact;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_possted_finish);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        findViewById(R.id.home).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        contact = (ProRegularTextView) findViewById(R.id.contact);
        mail_resent = (ProRegularTextView) findViewById(R.id.mail_resent);
        contact.setMovementMethod(LinkMovementMethod.getInstance());
        mail_resent.setMovementMethod(LinkMovementMethod.getInstance());


        /**
         * Contact us spannable text with click listener
         */
        String contactTextOne = "If you already request your confirmation email to be reset and you have not received that email within 30 minutes and do not see it in your Spam/Junk folder please  ";
        String contactTextClick = "contact us ";
        String contactTextTwo = "and include a phone number.";

        Spannable word1 = new SpannableString(contactTextOne);

        word1.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.colorTextDark)), 0, contactTextOne.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        contact.setText(word1);

        Spannable word2 = new SpannableString(contactTextClick);

        ClickableSpan myClickableSpan = new ClickableSpan() {
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
        word2.setSpan(myClickableSpan, 0, contactTextClick.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        contact.append(word2);

        Spannable word3 = new SpannableString(contactTextTwo);

        word3.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.colorTextDark)), 0, contactTextTwo.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        contact.append(word3);


        /**
         * Mail resent spannable text with click listener
         */
        String mailResentPart1 = "If you do not received confirmation email within 30 minutes you can ";
        String mailResentPart2 = "request it to be resent.";

        Spannable resentWord = new SpannableString(mailResentPart1);

        resentWord.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.colorTextDark)), 0, mailResentPart1.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        mail_resent.setText(resentWord);

        Spannable resentWordClick = new SpannableString(mailResentPart2);

        ClickableSpan resentClickableSpan = new ClickableSpan() {
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
        resentWordClick.setSpan(resentClickableSpan, 0, mailResentPart2.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        mail_resent.append(resentWordClick);


        findViewById(R.id.confirm_login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(PostedFinishActivity.this, LogInActivity.class));
                finish();
            }
        });


    }
}
