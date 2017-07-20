package com.android.llc.proringer.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;

import com.android.llc.proringer.R;
import com.android.llc.proringer.viewsmod.textview.ProRegularTextView;

/**
 * Created by su on 7/14/17.
 */

public class PostedFinishActivity extends AppCompatActivity {
    ProRegularTextView mail_resent,contact;
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

        mail_resent= (ProRegularTextView) findViewById(R.id.mail_resent);
        contact= (ProRegularTextView) findViewById(R.id.contact);


        mail_resent.setText(Html.fromHtml(getString(R.string.posted_finish_resent)));
        contact.setText(Html.fromHtml(getString(R.string.posted_finish_contact_us)));


        mail_resent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(PostedFinishActivity.this,ContactUsActivity.class));
            }
        });

        findViewById(R.id.confirm_login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(PostedFinishActivity.this,LogIn.class));
                finish();
            }
        });


    }
}
