package com.android.llc.proringer.fragments.main_content;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.android.llc.proringer.R;
import com.android.llc.proringer.activities.LandScreenActivity;
import com.android.llc.proringer.adapter.MyProjectRateProAdapter;

/**
 * Created by su on 7/25/17.
 */

public class MyProjectRatePro extends Fragment {
    RecyclerView recyclerView;
    MyProjectRateProAdapter myProjectRateProAdapter;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.my_project_rate_pro, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView= (RecyclerView) view.findViewById(R.id.rcv_);
        recyclerView.setLayoutManager(new LinearLayoutManager((LandScreenActivity)getActivity()));

        myProjectRateProAdapter=new MyProjectRateProAdapter((LandScreenActivity)getActivity());
        recyclerView.setAdapter(myProjectRateProAdapter);
    }
}
