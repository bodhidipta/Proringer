package com.android.llc.proringer.fragments.main_content;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.android.llc.proringer.R;
import com.android.llc.proringer.appconstant.ProApplication;
import com.android.llc.proringer.viewsmod.textview.ProRegularTextView;
import com.bumptech.glide.Glide;

/**
 * Created by su on 7/17/17.
 */

public class MyProjectDetails extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.my_project_detailed_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ProRegularTextView tv_posted_in = (ProRegularTextView) view.findViewById(R.id.tv_posted_in);
        ProRegularTextView tv_project = (ProRegularTextView) view.findViewById(R.id.tv_project);
        ProRegularTextView tv_service = (ProRegularTextView) view.findViewById(R.id.tv_service);
        ProRegularTextView tv_type = (ProRegularTextView) view.findViewById(R.id.tv_type);
        ProRegularTextView tv_property = (ProRegularTextView) view.findViewById(R.id.tv_property);
        ProRegularTextView tv_status = (ProRegularTextView) view.findViewById(R.id.tv_status);
        ProRegularTextView tv_type2 = (ProRegularTextView) view.findViewById(R.id.tv_type2);
        ProRegularTextView tv_start = (ProRegularTextView) view.findViewById(R.id.tv_start);
        ProRegularTextView img_description = (ProRegularTextView) view.findViewById(R.id.img_description);
        ImageView img_project = (ImageView) view.findViewById(R.id.img_project);

        tv_posted_in.setText(ProApplication.getInstance().getDataSelected().getDate_time());
        tv_project.setText(ProApplication.getInstance().getDataSelected().getProject_category_name());
        tv_service.setText(ProApplication.getInstance().getDataSelected().getProject_category_service_name());
        tv_type.setText(ProApplication.getInstance().getDataSelected().getProject_service_looktype());
        tv_property.setText(ProApplication.getInstance().getDataSelected().getProperty_type_name());
        tv_status.setText(ProApplication.getInstance().getDataSelected().getProject_stage());
        tv_start.setText(ProApplication.getInstance().getDataSelected().getProject_timeframe_name());
        img_description.setText(ProApplication.getInstance().getDataSelected().getProject_name());

        if (!ProApplication.getInstance().getDataSelected().getProject_image().equals(""))
            Glide.with(getActivity()).load(ProApplication.getInstance().getDataSelected().getProject_image()).centerCrop().into(img_project);

    }
}
