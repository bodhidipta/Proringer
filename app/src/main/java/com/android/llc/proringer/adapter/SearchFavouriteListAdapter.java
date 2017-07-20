package com.android.llc.proringer.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import com.android.llc.proringer.R;
import com.android.llc.proringer.activities.ProjectDetailsActivity;
import com.android.llc.proringer.appconstant.ProApplication;
import com.android.llc.proringer.helper.ProServiceApiHelper;
import com.android.llc.proringer.utils.Logger;
import com.android.llc.proringer.viewsmod.textview.ProRegularTextView;
import com.android.llc.proringer.viewsmod.textview.ProSemiBoldTextView;
import com.bumptech.glide.Glide;

import org.json.JSONArray;

/**
 * Created by su on 7/13/17.
 */

public class SearchFavouriteListAdapter extends RecyclerView.Adapter<SearchFavouriteListAdapter.ViewHolder> {
    private Context mcontext = null;
    JSONArray jsonInfoArray;
    ProgressDialog pgDia;
    public SearchFavouriteListAdapter(Context mcontext,JSONArray jsonInfoArray) {
        this.mcontext = mcontext;
        this.jsonInfoArray=jsonInfoArray;
        pgDia = new ProgressDialog(mcontext);
    }

    @Override
    public int getItemCount() {
        return jsonInfoArray.length();
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.ratingBar.setMax(5);
        try {
            holder.ratingBar.setRating(Float.valueOf(jsonInfoArray.getJSONObject(position).getString("pro_avg_review_rating")));
            holder.tv_pros_company_name.setText(jsonInfoArray.getJSONObject(position).getString("pros_company_name"));
            holder.tv_category_name.setText(jsonInfoArray.getJSONObject(position).getString("category_name"));
            holder.tv_address.setText(jsonInfoArray.getJSONObject(position).getString("city")
            +","+jsonInfoArray.getJSONObject(position).getString("state"));

            holder.tv_total_review.setText("("+jsonInfoArray.getJSONObject(position).getString("total_review")+")");

            holder.tv_description.setText(jsonInfoArray.getJSONObject(position).getString("description"));


            if (!jsonInfoArray.getJSONObject(position).getString("profile_img").equals(""))
                Glide.with(mcontext).load(jsonInfoArray.getJSONObject(position).getString("profile_img")).centerCrop().into(holder.img_project);


            if(jsonInfoArray.getJSONObject(position).getString("pros_verify").equalsIgnoreCase("Y")){
                holder.img_verify.setVisibility(View.VISIBLE);
                holder.img_verify_tick.setVisibility(View.VISIBLE);
                holder.tv_verify.setVisibility(View.VISIBLE);
                holder.linear_layout_border.setBackgroundResource(R.drawable.background_with_orange_border);
            }else {
                holder.img_verify.setVisibility(View.GONE);
                holder.img_verify_tick.setVisibility(View.GONE);
                holder.tv_verify.setVisibility(View.GONE);
            }


        }
        catch (Exception e)
        {
            e.printStackTrace();
        }




        holder.main_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(mcontext, ProjectDetailsActivity.class);
                mcontext.startActivity(intent);
            }
        });

        holder.img_favourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ///////////delete from favourite list
                try {
                ProServiceApiHelper.getInstance(mcontext).deleteFavouritePro(new ProServiceApiHelper.getApiProcessCallback() {
                                                                              @Override
                                                                              public void onStart() {

                                                                                  pgDia.setTitle("My Project");
                                                                                  pgDia.setMessage("It's deleting.Please wait....");
                                                                                  pgDia.setCancelable(false);
                                                                                  pgDia.show();

                                                                              }

                                                                              @Override
                                                                              public void onComplete(String message) {

                                                                                  jsonInfoArray.remove(position);
                                                                                  notifyItemRemoved(position);

                                                                                  if (pgDia != null && pgDia.isShowing())
                                                                                      pgDia.dismiss();


                                                                                  new AlertDialog.Builder(mcontext)
                                                                                          .setTitle("Delete Fav pros")
                                                                                          .setMessage("" + message)
                                                                                          .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                                                                              @Override
                                                                                              public void onClick(DialogInterface dialog, int which) {
                                                                                                  dialog.dismiss();
                                                                                              }
                                                                                          })
                                                                                          .setCancelable(false)
                                                                                          .show();
                                                                              }

                                                                              @Override
                                                                              public void onError(String error) {
                                                                                  if (pgDia != null && pgDia.isShowing())
                                                                                      pgDia.dismiss();

                                                                                  new AlertDialog.Builder(mcontext)
                                                                                          .setTitle("Delete Fav pros")
                                                                                          .setMessage("" + error)
                                                                                          .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                                                                              @Override
                                                                                              public void onClick(DialogInterface dialog, int which) {
                                                                                                  dialog.dismiss();
                                                                                              }
                                                                                          })
                                                                                          .setCancelable(false)
                                                                                          .show();

                                                                              }
                                                                          },
                        ProApplication.getInstance().getUserId(),
                        jsonInfoArray.getJSONObject(position).getString("pros_id")
                );
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }

            }
        });
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(mcontext).inflate(R.layout.search_pro_list_rowitem, parent, false));
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout linear_layout_border;
        RatingBar ratingBar;
        CardView main_container;
        ProSemiBoldTextView tv_pros_company_name;
        ProRegularTextView tv_category_name,tv_address,tv_verify,tv_total_review,tv_description,tv_more;
        ImageView img_project,img_verify,img_verify_tick,img_favourite;

        public ViewHolder(View itemView) {
            super(itemView);
            ratingBar = (RatingBar) itemView.findViewById(R.id.ratingPro);

            main_container= (CardView) itemView.findViewById(R.id.main_container);

            tv_pros_company_name= (ProSemiBoldTextView) itemView.findViewById(R.id.tv_pros_company_name);

            tv_category_name= (ProRegularTextView) itemView.findViewById(R.id.tv_category_name);
            tv_total_review= (ProRegularTextView) itemView.findViewById(R.id.tv_total_review);
            tv_address= (ProRegularTextView) itemView.findViewById(R.id.tv_address);
            tv_verify= (ProRegularTextView) itemView.findViewById(R.id.tv_verify);
            tv_description= (ProRegularTextView) itemView.findViewById(R.id.tv_description);
            tv_more= (ProRegularTextView) itemView.findViewById(R.id.tv_more);

            img_project= (ImageView) itemView.findViewById(R.id.img_project);
            img_verify= (ImageView) itemView.findViewById(R.id.img_verify);
            img_verify_tick= (ImageView) itemView.findViewById(R.id.img_verify_tick);
            img_favourite= (ImageView) itemView.findViewById(R.id.img_favourite);

            linear_layout_border= (LinearLayout) itemView.findViewById(R.id.linear_layout_border);

        }
    }
}
