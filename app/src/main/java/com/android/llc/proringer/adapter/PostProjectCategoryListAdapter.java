package com.android.llc.proringer.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.android.llc.proringer.R;
import com.android.llc.proringer.pojo.ProCategoryData;
import com.android.llc.proringer.utils.Logger;
import com.android.llc.proringer.viewsmod.textview.ProRegularTextView;
import java.util.LinkedList;

/**
 * Created by su on 7/17/17.
 */

public class PostProjectCategoryListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mcontext;
    LinkedList<String> proCategoryDatasSortedList;
    private onClickItem listener;

    public PostProjectCategoryListAdapter(Context mcontext, LinkedList<String> proCategoryDatasSortedList,onClickItem listener) {
        this.mcontext = mcontext;
        this.proCategoryDatasSortedList = proCategoryDatasSortedList;
        this.listener = listener;

    }

    class ViewHolder1 extends RecyclerView.ViewHolder {
        ProRegularTextView item_;

        public ViewHolder1(View itemView) {
            super(itemView);
            item_ = (ProRegularTextView) itemView.findViewById(R.id.item_);
        }
    }

    class ViewHolder2 extends RecyclerView.ViewHolder {
        ProRegularTextView item_;

        public ViewHolder2(View itemView) {
            super(itemView);
            item_ = (ProRegularTextView) itemView.findViewById(R.id.item_);
        }
    }

    @Override
    public int getItemViewType(int position) {
        // Just as an example, return 0 or 2 depending on position
        // Note that unlike in ListView adapters, types don't have to be contiguous
        if (proCategoryDatasSortedList.get(position).equalsIgnoreCase("A")
                ||proCategoryDatasSortedList.get(position).equalsIgnoreCase("B")
                ||proCategoryDatasSortedList.get(position).equalsIgnoreCase("C")
                ||proCategoryDatasSortedList.get(position).equalsIgnoreCase("D")
                ||proCategoryDatasSortedList.get(position).equalsIgnoreCase("E")
                ||proCategoryDatasSortedList.get(position).equalsIgnoreCase("F")
                ||proCategoryDatasSortedList.get(position).equalsIgnoreCase("G")
                ||proCategoryDatasSortedList.get(position).equalsIgnoreCase("H")
                ||proCategoryDatasSortedList.get(position).equalsIgnoreCase("I")
                ||proCategoryDatasSortedList.get(position).equalsIgnoreCase("J")
                ||proCategoryDatasSortedList.get(position).equalsIgnoreCase("K")
                ||proCategoryDatasSortedList.get(position).equalsIgnoreCase("L")
                ||proCategoryDatasSortedList.get(position).equalsIgnoreCase("M")
                ||proCategoryDatasSortedList.get(position).equalsIgnoreCase("N")
                ||proCategoryDatasSortedList.get(position).equalsIgnoreCase("O")
                ||proCategoryDatasSortedList.get(position).equalsIgnoreCase("P")
                ||proCategoryDatasSortedList.get(position).equalsIgnoreCase("Q")
                ||proCategoryDatasSortedList.get(position).equalsIgnoreCase("R")
                ||proCategoryDatasSortedList.get(position).equalsIgnoreCase("S")
                ||proCategoryDatasSortedList.get(position).equalsIgnoreCase("T")
                ||proCategoryDatasSortedList.get(position).equalsIgnoreCase("U")
                ||proCategoryDatasSortedList.get(position).equalsIgnoreCase("V")
                ||proCategoryDatasSortedList.get(position).equalsIgnoreCase("W")
                ||proCategoryDatasSortedList.get(position).equalsIgnoreCase("X")
                ||proCategoryDatasSortedList.get(position).equalsIgnoreCase("Y")
                ||proCategoryDatasSortedList.get(position).equalsIgnoreCase("Z")) {
            return 1;
        } else {
            return 2;
        }
    }

    @Override
    public int getItemCount() {
        return proCategoryDatasSortedList.size();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == 1) {
            return new ViewHolder1(LayoutInflater.from(mcontext).inflate(R.layout.post_project_category_row_accent_color_background, parent, false));
        } else {
            return new ViewHolder2(LayoutInflater.from(mcontext).inflate(R.layout.post_project_category_row, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        switch (holder.getItemViewType()) {
            case 1:
                ViewHolder1 viewHolder1 = (ViewHolder1) holder;
                viewHolder1.item_.setText(proCategoryDatasSortedList.get(position));
                viewHolder1.item_.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        /////////Nothing Happened here////////////////
                        Logger.printMessage("position", "" + proCategoryDatasSortedList.get(position));
                    }
                });
                break;

            case 2:
                ViewHolder2 viewHolder2 = (ViewHolder2) holder;
                viewHolder2.item_.setText(proCategoryDatasSortedList.get(position));
                viewHolder2.item_.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        listener.onSelectItemClick(position, proCategoryDatasSortedList.get(position));
                    }
                });
                break;
        }
    }

    public interface onClickItem {
        void onSelectItemClick(int position, String data);
    }


}