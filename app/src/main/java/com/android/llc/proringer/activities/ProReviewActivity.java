package com.android.llc.proringer.activities;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.llc.proringer.R;
import com.android.llc.proringer.appconstant.ProApplication;
import com.android.llc.proringer.helper.ProServiceApiHelper;
import com.android.llc.proringer.utils.Logger;
import com.android.llc.proringer.viewsmod.edittext.ProRegularEditText;
import com.android.llc.proringer.viewsmod.textview.ProRegularTextView;

/**
 * Created by su on 8/1/17.
 */

public class ProReviewActivity extends AppCompatActivity {
    TextView tv_terms_guidelines;
    RatingBar ratBar_review;
    String pros_id="",review_rate="0";
    ProRegularEditText project_description_text;
    ProgressDialog pgDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.review_pro);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        project_description_text= (ProRegularEditText) findViewById(R.id.project_description_text);


        if (getIntent().getExtras() != null) {
            pros_id = getIntent().getExtras().getString("pros_id");
        }

        tv_terms_guidelines= (TextView) findViewById(R.id.tv_terms_guidelines);
        tv_terms_guidelines.setMovementMethod(LinkMovementMethod.getInstance());

//        ratBar_review= (RatingBar) findViewById(R.id.ratBar_review);
//
//        LayerDrawable starst = (LayerDrawable) ratBar_review.getProgressDrawable();
//        ProApplication.SetRatingColor(starst);

        ratBar_review=(RatingBar)findViewById(R.id.ratBar_review);

        ratBar_review.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener(){

            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating,
                                        boolean fromUser) {
                // TODO Auto-generated method stub
                Toast.makeText(getApplicationContext(),Float.toString(rating),Toast.LENGTH_LONG).show();
                review_rate=Float.toString(rating);
            }

        });


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


        Spannable word4 = new SpannableString(reviewGuidelinesClick);
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
        word4.setSpan(myReviewGuidelinesClick, 0, reviewGuidelinesClick.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        tv_terms_guidelines.append(word4);

        findViewById(R.id.tv_post_review).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(review_rate.equals("0")){
                    Toast.makeText(ProReviewActivity.this,"Please select rat",Toast.LENGTH_LONG).show();
                }
                else {
                    if(project_description_text.getText().toString().trim().equals("")){
                        project_description_text.setError("Please enter project description");
                    }
                    else {
                        postReview();
                    }
                }
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

    public void postReview(){
        ProServiceApiHelper.getInstance(ProReviewActivity.this).addReview(new ProServiceApiHelper.getApiProcessCallback() {
            @Override
            public void onStart() {
                pgDialog = new ProgressDialog(ProReviewActivity.this);
                pgDialog.setTitle("Add Review");
                pgDialog.setCancelable(false);
                pgDialog.setMessage("Adding Review.Please wait...");
                pgDialog.show();
            }

            @Override
            public void onComplete(String message) {
                if (pgDialog != null && pgDialog.isShowing())
                    pgDialog.dismiss();

                new AlertDialog.Builder(ProReviewActivity.this)
                        .setTitle("Add Review")
                        .setMessage("" + message)
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        })
                        .show();
            }

            @Override
            public void onError(String error) {
                if (pgDialog != null && pgDialog.isShowing())
                    pgDialog.dismiss();

                new AlertDialog.Builder(ProReviewActivity.this)
                        .setTitle("Add Review")
                        .setMessage("" + error)
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        })
                        .show();
            }
        },ProApplication.getInstance().getUserId(),pros_id,review_rate,project_description_text.getText().toString().trim());
    }
}
