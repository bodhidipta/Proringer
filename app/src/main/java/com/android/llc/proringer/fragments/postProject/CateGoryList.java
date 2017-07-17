package com.android.llc.proringer.fragments.postProject;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.android.llc.proringer.R;
import com.android.llc.proringer.activities.ActivityPostProject;
import com.android.llc.proringer.adapter.PostProjectCategoryGridAdapter;
import com.android.llc.proringer.adapter.PostProjectCategoryListAdapter;
import com.android.llc.proringer.helper.ProServiceApiHelper;
import com.android.llc.proringer.pojo.ProCategoryData;
import com.android.llc.proringer.utils.Logger;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;

/**
 * Created by su on 7/13/17.
 */

public class CateGoryList extends Fragment {
    RecyclerView category_listing;
    ProgressDialog pgDialog;
    PostProjectCategoryGridAdapter gridAdapter;
    PostProjectCategoryListAdapter listAdapter;
    LinkedList<String> proCategoryDatasSortedList;
    LinkedList<ProCategoryData> listdataMain;

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


                listdataMain=listdata;

                proCategoryDatasSortedList =new LinkedList<String>();
                for (int d=0;d<listdata.size();d++)
                {
                    proCategoryDatasSortedList.add(listdata.get(d).getCategory_name());
                }


                addAlbhabetHeader('A');
                addAlbhabetHeader('B');
                addAlbhabetHeader('C');
                addAlbhabetHeader('D');
                addAlbhabetHeader('E');
                addAlbhabetHeader('F');
                addAlbhabetHeader('G');
                addAlbhabetHeader('H');
                addAlbhabetHeader('I');
                addAlbhabetHeader('J');
                addAlbhabetHeader('K');
                addAlbhabetHeader('L');
                addAlbhabetHeader('M');
                addAlbhabetHeader('N');
                addAlbhabetHeader('O');
                addAlbhabetHeader('P');
                addAlbhabetHeader('Q');
                addAlbhabetHeader('R');
                addAlbhabetHeader('S');
                addAlbhabetHeader('T');
                addAlbhabetHeader('U');
                addAlbhabetHeader('V');
                addAlbhabetHeader('W');
                addAlbhabetHeader('X');
                addAlbhabetHeader('Y');
                addAlbhabetHeader('Z');



                Collections.sort(proCategoryDatasSortedList, new Comparator<String>()
                {
                    @Override
                    public int compare(String text1, String text2)
                    {
                        return text1.compareToIgnoreCase(text2);
                    }
                });



                gridAdapter = new PostProjectCategoryGridAdapter(getActivity(), listdata, new PostProjectCategoryGridAdapter.onClickItem() {
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

        view.findViewById(R.id.see_all_categories).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Logger.printMessage("show list","list vise");

                category_listing.setLayoutManager(new LinearLayoutManager(getActivity()));

                listAdapter=new PostProjectCategoryListAdapter(getActivity(), proCategoryDatasSortedList, new PostProjectCategoryListAdapter.onClickItem() {
                    @Override
                    public void onSelectItemClick(int position, String data) {

                        Logger.printMessage("data###",""+data);
                        Logger.printMessage("listdataMain###",""+listdataMain.size());

                        for (int p=0;p<listdataMain.size();p++){
                            if (listdataMain.get(p).getCategory_name().trim().equalsIgnoreCase(data)){
                                //true
                                Logger.printMessage("Category_name###",""+listdataMain.get(p).getCategory_name());
                                Logger.printMessage("Id###",""+listdataMain.get(p).getId());
                                Logger.printMessage("Category_image###",""+listdataMain.get(p).getCategory_image());
                                Logger.printMessage("Parent_id###",""+listdataMain.get(p).getParent_id());

                                ((ActivityPostProject) getActivity()).selectedCategory = listdataMain.get(p);
                                ((ActivityPostProject) getActivity()).setHeaderCategory();
                                ((ActivityPostProject) getActivity()).increaseStep();
                                /**
                                 * fragment calling
                                 */
                                ((ActivityPostProject) getActivity()).changeFragmentNext(2);
                                break;
                            }
                        }
                    }
                });
                category_listing.setAdapter(listAdapter);
            }
        });
    }
    public void addAlbhabetHeader(char a){
        for (int p=0;p<proCategoryDatasSortedList.size();p++){
            if(proCategoryDatasSortedList.get(p).toUpperCase().charAt(0)==a){
                proCategoryDatasSortedList.add(""+a);
                break;
            }
        }
    }
}
