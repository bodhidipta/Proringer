package com.android.llc.proringer.fragments.main_content;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.android.llc.proringer.R;
import com.android.llc.proringer.appconstant.ProApplication;
import com.android.llc.proringer.helper.ProServiceApiHelper;
import com.android.llc.proringer.viewsmod.textview.ProRegularTextView;
import com.bumptech.glide.Glide;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by su on 7/17/17.
 */

public class MyProjectDetails extends Fragment {
    ProgressDialog dialog;
    ProRegularTextView tv_posted_in,tv_project,tv_service,tv_type,tv_property,tv_status,tv_start,img_description;
    ImageView img_project;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.my_project_detailed_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

         tv_posted_in = (ProRegularTextView) view.findViewById(R.id.tv_posted_in);
         tv_project = (ProRegularTextView) view.findViewById(R.id.tv_project);
         tv_service = (ProRegularTextView) view.findViewById(R.id.tv_service);
         tv_type = (ProRegularTextView) view.findViewById(R.id.tv_type);
         tv_property = (ProRegularTextView) view.findViewById(R.id.tv_property);
         tv_status = (ProRegularTextView) view.findViewById(R.id.tv_status);
//        ProRegularTextView tv_type2 = (ProRegularTextView) view.findViewById(R.id.tv_type2);
         tv_start = (ProRegularTextView) view.findViewById(R.id.tv_start);
         img_description = (ProRegularTextView) view.findViewById(R.id.img_description);
         img_project = (ImageView) view.findViewById(R.id.img_project);

        ProServiceApiHelper.getInstance(getActivity()).getMyProjectDetails(new ProServiceApiHelper.getApiProcessCallback() {
            @Override
            public void onStart() {
                dialog = new ProgressDialog(getActivity());
                dialog.setTitle("My Projects");
                dialog.setCancelable(false);
                dialog.setMessage("Getting MyProject details. Please wait.");
                dialog.show();
            }

            @Override
            public void onComplete(String message) {
                if (dialog != null && dialog.isShowing())
                    dialog.dismiss();

                try {
                    JSONObject jsonObject = new JSONObject(message);

                    if (jsonObject.has("info_array")) {

                        JSONArray info_array=jsonObject.getJSONArray("info_array");

                        tv_posted_in.setText(ProApplication.getInstance().getDataSelected().getDate_time());
                        tv_project.setText(ProApplication.getInstance().getDataSelected().getProject_category_name());
                        tv_service.setText(ProApplication.getInstance().getDataSelected().getProject_category_service_name());
                        tv_type.setText(ProApplication.getInstance().getDataSelected().getProject_service_looktype());
                        tv_property.setText(ProApplication.getInstance().getDataSelected().getProperty_type_name());
                        tv_status.setText(ProApplication.getInstance().getDataSelected().getProject_stage());
                        tv_start.setText(ProApplication.getInstance().getDataSelected().getProject_timeframe_name());
                        img_description.setText(ProApplication.getInstance().getDataSelected().getProject_name());

                        if (!ProApplication.getInstance().getDataSelected().getProject_image().equals(""))
                            Glide.with(getActivity()).load(info_array.getJSONObject(0).getString("project_image")).centerCrop().into(img_project);

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(String error) {
                if (dialog != null && dialog.isShowing())
                    dialog.dismiss();

                new AlertDialog.Builder(getActivity())
                        .setTitle("MyProjects Error")
                        .setMessage("" + error)
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        })
                        .show();
            }
        }, ProApplication.getInstance().getDataSelected().getId());

    }
}
