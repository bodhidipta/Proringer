package com.android.llc.proringer.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.RatingBar;

import com.android.llc.proringer.R;
import com.android.llc.proringer.activities.ProsReportAbuseActivity;
import com.android.llc.proringer.activities.ProsReviewAllListActivity;
import com.android.llc.proringer.helper.CustomAlert;
import com.android.llc.proringer.helper.MyCustomAlertListener;
import com.android.llc.proringer.helper.ShowMyDialog;
import com.android.llc.proringer.utils.Logger;
import com.android.llc.proringer.viewsmod.textview.ProRegularTextView;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;

/**
 * Created by su on 8/4/17.
 */

public class ProsReviewAllAdapter extends RecyclerView.Adapter<ProsReviewAllAdapter.MyViewHolder> implements MyCustomAlertListener {
    Context context;
    JSONArray jsonInfoArray;

    public ProsReviewAllAdapter(Context context, JSONArray jsonInfoArray) {
        this.context = context;
        this.jsonInfoArray = jsonInfoArray;

    }

    @Override
    public ProsReviewAllAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_pros_review_all, parent, false);
        return new ProsReviewAllAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ProsReviewAllAdapter.MyViewHolder holder, final int position) {
        try {

            if (!jsonInfoArray.getJSONObject(position).getString("profile_img").equals(""))
                Glide.with(context).load(jsonInfoArray.getJSONObject(position).getString("profile_img")).centerCrop().into(holder.img_profile);

            holder.rbar.setRating(Float.parseFloat(jsonInfoArray.getJSONObject(position).getString("avg_rating")));

            holder.tv_name.setText(jsonInfoArray.getJSONObject(position).getString("homeowner_name"));
            holder.tv_review_date.setText(jsonInfoArray.getJSONObject(position).getString("date_time"));

            if (jsonInfoArray.getJSONObject(position).getString("review_report_status").trim().equals("0")) {
                holder.tv_report.setVisibility(View.VISIBLE);
            } else {
                holder.tv_report.setVisibility(View.GONE);
            }

            holder.tv_report.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        if (jsonInfoArray.getJSONObject(position).getString("review_report_status").trim().equals("0")) {
                            Intent intent = new Intent(context, ProsReportAbuseActivity.class);
                            intent.putExtra("review_report_id", jsonInfoArray.getJSONObject(position).getString("id"));
                            context.startActivity(intent);
                        } else {

                            CustomAlert customAlert = new CustomAlert(context, "Contact Us", "Sorry! You have already added a report for this review", ProsReviewAllAdapter.this);
                            customAlert.createNormalAlert("ok", 1);
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            });
            holder.tv_review_comment.setText(jsonInfoArray.getJSONObject(position).getString("rater_description").trim());

            holder.tv_review_comment.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    holder.tv_review_comment.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    int lineCount = holder.tv_review_comment.getLineCount();
                    Log.d("count", "Number of lines is " + lineCount);

                    if (lineCount > 1) {

                        /**
                         * Contact us spannable text with click listener
                         */
                        String contactTextOne = null;
                        try {
                            if (jsonInfoArray.getJSONObject(position).getString("rater_description").trim().length() >= 60) {
                                contactTextOne = jsonInfoArray.getJSONObject(position).getString("rater_description").trim().substring(0, 60) + "....";
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        String contactTextClick = "Read Full Review";


                        Spannable word1 = new SpannableString(contactTextOne);

                        word1.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.colorTextDark)), 0, contactTextOne.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                        holder.tv_review_comment.setText(word1);

                        Spannable word2 = new SpannableString(contactTextClick);

                        ClickableSpan myClickableSpan = new ClickableSpan() {
                            @Override
                            public void onClick(View widget) {
                                // There is the OnCLick. put your intent to Register class here
                                widget.invalidate();
                                Logger.printMessage("SpanHello", "click");


                                try {
                                    new ShowMyDialog(context).showDescribetionDialog("Review", jsonInfoArray.getJSONObject(position).getString("rater_description").trim());
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void updateDrawState(TextPaint ds) {
                                ds.setColor(context.getResources().getColor(R.color.colorAccent));
                                ds.setUnderlineText(false);
                            }
                        };
                        word2.setSpan(myClickableSpan, 0, contactTextClick.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        holder.tv_review_comment.append(word2);
                    }
                }
            });

            if (jsonInfoArray.getJSONObject(position).getInt("review_reply") == 0) {
                holder.CardViewReply.setVisibility(View.GONE);
            } else {
                holder.CardViewReply.setVisibility(View.VISIBLE);
                holder.tv_review_reply_by.setText(((ProsReviewAllListActivity) context).pros_company_name);
                holder.tv_review_reply_on_date.setText("responded on " + jsonInfoArray.getJSONObject(position).getString("review_reply_date"));
                holder.tv_review_reply.setText(jsonInfoArray.getJSONObject(position).getString("review_reply_content"));


                holder.tv_review_reply.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        holder.tv_review_reply.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                        int lineCount = holder.tv_review_reply.getLineCount();
                        Log.d("count", "Number of lines is " + lineCount);

                        if (lineCount > 1) {

                            /**
                             * Contact us spannable text with click listener
                             */
                            String contactTextOne = null;
                            try {
                                if (jsonInfoArray.getJSONObject(position).getString("review_reply_content").trim().length() >= 60) {
                                    contactTextOne = jsonInfoArray.getJSONObject(position).getString("review_reply_content").substring(0, 60) + "....";
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            String contactTextClick = "Read Full Response";


                            Spannable word1 = new SpannableString(contactTextOne);

                            word1.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.colorTextDark)), 0, contactTextOne.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                            holder.tv_review_reply.setText(word1);

                            Spannable word2 = new SpannableString(contactTextClick);

                            ClickableSpan myClickableSpan = new ClickableSpan() {
                                @Override
                                public void onClick(View widget) {
                                    // There is the OnCLick. put your intent to Register class here
                                    widget.invalidate();
                                    Logger.printMessage("SpanHello", "click");
                                    try {
                                        new ShowMyDialog(context).showDescribetionDialog("Reply", jsonInfoArray.getJSONObject(position).getString("review_reply_content").trim());
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                }

                                @Override
                                public void updateDrawState(TextPaint ds) {
                                    ds.setColor(context.getResources().getColor(R.color.colorAccent));
                                    ds.setUnderlineText(false);
                                }
                            };
                            word2.setSpan(myClickableSpan, 0, contactTextClick.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                            holder.tv_review_reply.append(word2);
                        }
                    }
                });


            }

            if (position == jsonInfoArray.length() - 1) {
                ((ProsReviewAllListActivity) context).loadReviewList(jsonInfoArray.length(), 10);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return jsonInfoArray.length();
    }

    @Override
    public void callbackForAlert(String result, int i) {
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ProRegularTextView tv_name, tv_review_date, tv_report, tv_review_comment, tv_review_reply, tv_review_reply_by, tv_review_reply_on_date;
        RatingBar rbar;
        ImageView img_profile;
        CardView CardViewReply;

        public MyViewHolder(View itemView) {
            super(itemView);

            img_profile = (ImageView) itemView.findViewById(R.id.img_profile);

            tv_name = (ProRegularTextView) itemView.findViewById(R.id.tv_name);
            tv_review_date = (ProRegularTextView) itemView.findViewById(R.id.tv_review_date);
            tv_report = (ProRegularTextView) itemView.findViewById(R.id.tv_report);

            tv_review_comment = (ProRegularTextView) itemView.findViewById(R.id.tv_review_comment);
            tv_review_comment.setMovementMethod(LinkMovementMethod.getInstance());

            CardViewReply = (CardView) itemView.findViewById(R.id.CardViewReply);

            tv_review_reply = (ProRegularTextView) itemView.findViewById(R.id.tv_review_reply);
            tv_review_reply.setMovementMethod(LinkMovementMethod.getInstance());

            tv_review_reply_by = (ProRegularTextView) itemView.findViewById(R.id.tv_review_reply_by);
            tv_review_reply_on_date = (ProRegularTextView) itemView.findViewById(R.id.tv_review_reply_on_date);

            rbar = (RatingBar) itemView.findViewById(R.id.rbar);
        }
    }

    public void NotifyMeInLazyLoad(JSONArray jsonInfoArray) {
        this.jsonInfoArray = jsonInfoArray;
        notifyDataSetChanged();
    }
}
