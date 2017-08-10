package com.android.llc.proringer.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.llc.proringer.R;
import com.android.llc.proringer.viewsmod.textview.ProRegularTextView;

import org.json.JSONArray;

/**
 * Created by su on 8/3/17.
 */

public class ProsDetailsLicenseAdapter extends RecyclerView.Adapter<ProsDetailsLicenseAdapter.MyViewHolder> {
    Context context;
    JSONArray licenseJsonArray;

    public ProsDetailsLicenseAdapter(Context context, JSONArray licenseJsonArray) {
        this.context = context;
        this.licenseJsonArray = licenseJsonArray;
    }

    @Override
    public ProsDetailsLicenseAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_pro_details_license, parent, false);
        return new ProsDetailsLicenseAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ProsDetailsLicenseAdapter.MyViewHolder holder, int position) {

        try {
            holder.tv_category_name.setText(licenseJsonArray.getJSONObject(position).getString("category_name"));
            holder.tv_licence_issuer.setText(licenseJsonArray.getJSONObject(position).getString("licence_issuer"));
            holder.tv_licence_no.setText(licenseJsonArray.getJSONObject(position).getString("licence_no"));
            holder.tv_license_expire.setText(licenseJsonArray.getJSONObject(position).getString("license_expire"));
            holder.tv_view_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        return licenseJsonArray.length();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ProRegularTextView tv_category_name, tv_licence_issuer, tv_licence_no, tv_license_expire, tv_view_btn;

        public MyViewHolder(View itemView) {
            super(itemView);
            tv_category_name = (ProRegularTextView) itemView.findViewById(R.id.tv_category_name);
            tv_licence_issuer = (ProRegularTextView) itemView.findViewById(R.id.tv_licence_issuer);
            tv_licence_no = (ProRegularTextView) itemView.findViewById(R.id.tv_licence_no);
            tv_license_expire = (ProRegularTextView) itemView.findViewById(R.id.tv_license_expire);
            tv_view_btn = (ProRegularTextView) itemView.findViewById(R.id.tv_view_btn);
        }
    }
}
