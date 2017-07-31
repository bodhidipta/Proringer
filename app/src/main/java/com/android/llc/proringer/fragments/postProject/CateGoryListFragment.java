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
import com.android.llc.proringer.activities.PostProjectActivity;
import com.android.llc.proringer.adapter.PostProjectCategoryGridAdapter;
import com.android.llc.proringer.adapter.PostProjectCategoryListAdapter;
import com.android.llc.proringer.helper.ProServiceApiHelper;
import com.android.llc.proringer.pojo.ProCategoryData;
import com.android.llc.proringer.utils.Logger;
import com.android.llc.proringer.viewsmod.textview.ProRegularTextView;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;

/**
 * Created by su on 7/13/17.
 */

public class CateGoryListFragment extends Fragment {
    RecyclerView category_listing;
    ProgressDialog pgDialog;
    PostProjectCategoryGridAdapter gridAdapter;
    PostProjectCategoryListAdapter listAdapter;
    LinkedList<String> proCategoryDatasSortedList;
    LinkedList<ProCategoryData> listdataMain;
    ProRegularTextView item_header;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.content_post_project_category_listing, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        category_listing = (RecyclerView) view.findViewById(R.id.category_listing);
        category_listing.setLayoutManager(new GridLayoutManager((PostProjectActivity)getActivity(), 3));

        item_header = (ProRegularTextView) view.findViewById(R.id.item_header);
        item_header = (ProRegularTextView) view.findViewById(R.id.item_header);

        ProServiceApiHelper.getInstance((PostProjectActivity)getActivity()).getCategoryList(new ProServiceApiHelper.onProCategoryListener() {
            @Override
            public void onComplete(LinkedList<ProCategoryData> listdata) {
                if (pgDialog != null && pgDialog.isShowing())
                    pgDialog.dismiss();


                listdataMain = listdata;

                proCategoryDatasSortedList = new LinkedList<String>();
                for (int d = 0; d < listdata.size(); d++) {
                    proCategoryDatasSortedList.add(listdata.get(d).getCategory_name());
                }


                addAlphabetHeader('A');
                addAlphabetHeader('B');
                addAlphabetHeader('C');
                addAlphabetHeader('D');
                addAlphabetHeader('E');
                addAlphabetHeader('F');
                addAlphabetHeader('G');
                addAlphabetHeader('H');
                addAlphabetHeader('I');
                addAlphabetHeader('J');
                addAlphabetHeader('K');
                addAlphabetHeader('L');
                addAlphabetHeader('M');
                addAlphabetHeader('N');
                addAlphabetHeader('O');
                addAlphabetHeader('P');
                addAlphabetHeader('Q');
                addAlphabetHeader('R');
                addAlphabetHeader('S');
                addAlphabetHeader('T');
                addAlphabetHeader('U');
                addAlphabetHeader('V');
                addAlphabetHeader('W');
                addAlphabetHeader('X');
                addAlphabetHeader('Y');
                addAlphabetHeader('Z');


                Collections.sort(proCategoryDatasSortedList, new Comparator<String>() {
                    @Override
                    public int compare(String text1, String text2) {
                        return text1.compareToIgnoreCase(text2);
                    }
                });


                gridAdapter = new PostProjectCategoryGridAdapter((PostProjectActivity)getActivity(), listdata, new PostProjectCategoryGridAdapter.onClickItem() {
                    @Override
                    public void onSelectItemClick(int position, ProCategoryData data) {
                        ((PostProjectActivity) getActivity()).selectedCategory = data;
                        ((PostProjectActivity) getActivity()).setHeaderCategory();
                        ((PostProjectActivity) getActivity()).increaseStep();
                        /**
                         * fragment calling
                         */
                        ((PostProjectActivity) getActivity()).changeFragmentNext(2);

                    }
                });
                category_listing.setAdapter(gridAdapter);

            }

            @Override
            public void onError(String error) {
                if (pgDialog != null && pgDialog.isShowing())
                    pgDialog.dismiss();

                new AlertDialog.Builder((PostProjectActivity)getActivity())
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
                pgDialog = new ProgressDialog((PostProjectActivity)getActivity());
                pgDialog.setTitle("Preparing category");
                pgDialog.setMessage("Getting category list.Please wait...");
                pgDialog.setCancelable(false);
                pgDialog.show();

            }
        });

        view.findViewById(R.id.see_all_categories).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Logger.printMessage("show list", "list vise");

//                if(((ProRegularTextView)view.findViewById(R.id.see_all_categories)).
//                        getText().toString().trim().equalsIgnoreCase("See All categories"))
//                {
                category_listing.setLayoutManager(new LinearLayoutManager((PostProjectActivity)getActivity()));

                listAdapter = new PostProjectCategoryListAdapter((PostProjectActivity)getActivity(), proCategoryDatasSortedList, new PostProjectCategoryListAdapter.onClickItem() {
                    @Override
                    public void onSelectItemClick(int position, String data) {

                        Logger.printMessage("data###", "" + data);
                        Logger.printMessage("listdataMain###", "" + listdataMain.size());

                        for (int p = 0; p < listdataMain.size(); p++) {
                            if (listdataMain.get(p).getCategory_name().trim().equalsIgnoreCase(data)) {
                                //true
                                Logger.printMessage("Category_name###", "" + listdataMain.get(p).getCategory_name());
                                Logger.printMessage("Id###", "" + listdataMain.get(p).getId());
                                Logger.printMessage("Category_image###", "" + listdataMain.get(p).getCategory_image());
                                Logger.printMessage("Parent_id###", "" + listdataMain.get(p).getParent_id());

                                ((PostProjectActivity) getActivity()).selectedCategory = listdataMain.get(p);
                                ((PostProjectActivity) getActivity()).setHeaderCategory();
                                ((PostProjectActivity) getActivity()).increaseStep();
                                /**
                                 * fragment calling
                                 */
                                ((PostProjectActivity) getActivity()).changeFragmentNext(2);
                                break;
                            }
                        }
                    }
                });
                category_listing.setAdapter(listAdapter);

                category_listing.addOnScrollListener(new RecyclerView.OnScrollListener() {
                    @Override
                    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                        super.onScrollStateChanged(recyclerView, newState);
                    }

                    @Override
                    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                        super.onScrolled(recyclerView, dx, dy);

                        LinearLayoutManager llm = (LinearLayoutManager) category_listing.getLayoutManager();


                        int displayedposition = llm.findFirstVisibleItemPosition();
                        Logger.printMessage("psition",""+displayedposition);


                        if (proCategoryDatasSortedList.get(displayedposition).startsWith("A")){
                            item_header.setText("A");
                        }
                        else if(proCategoryDatasSortedList.get(displayedposition).startsWith("B")){
                            item_header.setText("B");
                        } else if(proCategoryDatasSortedList.get(displayedposition).startsWith("C")){
                            item_header.setText("C");
                        } else if(proCategoryDatasSortedList.get(displayedposition).startsWith("D")){
                            item_header.setText("D");
                        } else if(proCategoryDatasSortedList.get(displayedposition).startsWith("E")){
                            item_header.setText("E");
                        } else if(proCategoryDatasSortedList.get(displayedposition).startsWith("F")){
                            item_header.setText("F");
                        } else if(proCategoryDatasSortedList.get(displayedposition).startsWith("G")){
                            item_header.setText("G");
                        } else if(proCategoryDatasSortedList.get(displayedposition).startsWith("H")){
                            item_header.setText("H");
                        } else if(proCategoryDatasSortedList.get(displayedposition).startsWith("I")){
                            item_header.setText("I");
                        } else if(proCategoryDatasSortedList.get(displayedposition).startsWith("J")){
                            item_header.setText("J");
                        } else if(proCategoryDatasSortedList.get(displayedposition).startsWith("K")){
                            item_header.setText("K");
                        } else if(proCategoryDatasSortedList.get(displayedposition).startsWith("L")){
                            item_header.setText("L");
                        } else if(proCategoryDatasSortedList.get(displayedposition).startsWith("M")){
                            item_header.setText("M");
                        }else if(proCategoryDatasSortedList.get(displayedposition).startsWith("N")){
                            item_header.setText("N");
                        }else if(proCategoryDatasSortedList.get(displayedposition).startsWith("O")){
                            item_header.setText("O");
                        }else if(proCategoryDatasSortedList.get(displayedposition).startsWith("P")){
                            item_header.setText("P");
                        }else if(proCategoryDatasSortedList.get(displayedposition).startsWith("Q")){
                            item_header.setText("Q");
                        }else if(proCategoryDatasSortedList.get(displayedposition).startsWith("R")){
                            item_header.setText("R");
                        }else if(proCategoryDatasSortedList.get(displayedposition).startsWith("S")){
                            item_header.setText("S");
                        }
                        else if(proCategoryDatasSortedList.get(displayedposition).startsWith("T")){
                            item_header.setText("T");
                        }
                        else if(proCategoryDatasSortedList.get(displayedposition).startsWith("U")){
                            item_header.setText("U");
                        }
                        else if(proCategoryDatasSortedList.get(displayedposition).startsWith("V")){
                            item_header.setText("V");
                        }
                        else if(proCategoryDatasSortedList.get(displayedposition).startsWith("W")){
                            item_header.setText("W");
                        }else if(proCategoryDatasSortedList.get(displayedposition).startsWith("X")){
                            item_header.setText("X");
                        }else if(proCategoryDatasSortedList.get(displayedposition).startsWith("Y")){
                            item_header.setText("Y");
                        }else if(proCategoryDatasSortedList.get(displayedposition).startsWith("Z")){
                            item_header.setText("Z");
                        }
                    }
                });


                ((ProRegularTextView) view.findViewById(R.id.see_all_categories)).setVisibility(View.GONE);
                item_header.setVisibility(View.VISIBLE);

//                    ((ProRegularTextView)view.findViewById(R.id.see_all_categories)).
//                            setText("See All categories grid");

//                }else {
//                    category_listing.setLayoutManager(new GridLayoutManager(getActivity(), 3));
//                    gridAdapter = new PostProjectCategoryGridAdapter(getActivity(), listdataMain, new PostProjectCategoryGridAdapter.onClickItem() {
//                        @Override
//                        public void onSelectItemClick(int position, ProCategoryData data) {
//                            ((PostProjectActivity) getActivity()).selectedCategory = data;
//                            ((PostProjectActivity) getActivity()).setHeaderCategory();
//                            ((PostProjectActivity) getActivity()).increaseStep();
//                            /**
//                             * fragment calling
//                             */
//                            ((PostProjectActivity) getActivity()).changeFragmentNext(2);
//
//                        }
//                    });
//                    category_listing.setAdapter(gridAdapter);
//
//                    ((ProRegularTextView)view.findViewById(R.id.see_all_categories)).
//                            setText("See All categories");
//                }

            }
        });
    }

    public void addAlphabetHeader(char a) {
        for (int p = 0; p < proCategoryDatasSortedList.size(); p++) {
            if (proCategoryDatasSortedList.get(p).toUpperCase().charAt(0) == a) {
                proCategoryDatasSortedList.add("" + a);
                break;
            }
        }
    }
}
