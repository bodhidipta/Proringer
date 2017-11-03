package com.android.llc.proringer.fragments.editPros;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.llc.proringer.R;
import com.android.llc.proringer.activities.AddEditProsActivity;
import com.android.llc.proringer.viewsmod.edittext.ProRegularEditText;
import com.android.llc.proringer.viewsmod.textview.ProRegularTextView;

/**
 * Created by su on 2/11/17.
 */

public class EditProsDetailsFragment extends Fragment {
    ProRegularEditText project_description_text;
    ProRegularTextView selected_text;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.contain_post_project_description, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        project_description_text = (ProRegularEditText) view.findViewById(R.id.project_description_text);
        selected_text=(ProRegularTextView)view.findViewById(R.id.selected_text);
        selected_text.setText("Add details about the project");

        view.findViewById(R.id.continue_project_describing).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /**
                 * fragment calling
                 */

                if (project_description_text.getText().toString().trim().equals("")) {
                    project_description_text.setError("Please enter project description");
                } else {
                    ((AddEditProsActivity) getActivity()).closeKeypad();
                    ((AddEditProsActivity) getActivity()).increaseStep();
                    ((AddEditProsActivity) getActivity()).project_description_text = project_description_text.getText().toString().trim();
                    ((AddEditProsActivity) getActivity()).changeFragmentNext(2);
                }
            }
        });
    }
}