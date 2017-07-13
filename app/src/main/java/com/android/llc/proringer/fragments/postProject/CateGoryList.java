package com.android.llc.proringer.fragments.postProject;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.llc.proringer.R;
import com.android.llc.proringer.activities.ActivityPostProject;
import com.android.llc.proringer.adapter.PostProjectGridAdapter;
import com.android.llc.proringer.helper.ProServiceApiHelper;
import com.android.llc.proringer.pojo.ProCategoryData;
import com.android.llc.proringer.utils.Logger;

import java.util.LinkedList;

/**
 * Created by su on 7/13/17.
 */

public class CateGoryList extends Fragment {
    RecyclerView category_listing;
    ProgressDialog pgDialog;
    PostProjectGridAdapter gridAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.content_post_project_category_listing, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        category_listing = (RecyclerView) view.findViewById(R.id.category_listing);
        category_listing.setLayoutManager(new GridLayoutManager(getActivity(), 3));

        ProServiceApiHelper.getInstance(getActivity()).getCategoryList(new ProServiceApiHelper.onProCategoryListener() {
            @Override
            public void onComplete(LinkedList<ProCategoryData> listdata) {
                if (pgDialog != null && pgDialog.isShowing())
                    pgDialog.dismiss();

                gridAdapter = new PostProjectGridAdapter(getActivity(), listdata, new PostProjectGridAdapter.onClickItem() {
                    @Override
                    public void onSelectItemClick(int position, ProCategoryData data) {
                        ((ActivityPostProject) getActivity()).selectedCategory = data;
                        ((ActivityPostProject) getActivity()).setHeaderCategory();
                        ((ActivityPostProject) getActivity()).increaseStep();
                        /**
                         * fragment calling
                         */
                        ((ActivityPostProject) getActivity()).changeFragmentNext(2);

                    }
                });
                category_listing.setAdapter(gridAdapter);

            }

            @Override
            public void onError(String error) {
                if (pgDialog != null && pgDialog.isShowing())
                    pgDialog.dismiss();

                new AlertDialog.Builder(getActivity())
                        .setTitle("Error")
                        .setMessage("" + error)
                        .setCancelable(false)
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .show();

            }

            @Override
            public void onStartFetch() {
                pgDialog = new ProgressDialog(getActivity());
                pgDialog.setTitle("Preparing category");
                pgDialog.setMessage("Please wait while preparing category list.");
                pgDialog.setCancelable(false);
                pgDialog.show();

            }
        });


    }

}
