package com.e.jobkwetu.Adapters;

import android.content.Context;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import androidx.recyclerview.widget.RecyclerView;


import com.e.jobkwetu.Model.SelectedCategory;
import com.e.jobkwetu.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.MyViewHolder> {

    private Context context;
    private List<SelectedCategory> categoriesList;
    private static CategoryAdapter.OnItemClickListener mListener;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView categories;
        public TextView dot;
        public TextView timestamp;

        public MyViewHolder(View view) {
            super(view);
            categories = view.findViewById(R.id.note);
            dot = view.findViewById(R.id.dot);
            timestamp = view.findViewById(R.id.timestamp);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION && mListener != null) {
                        mListener.onItemClick(position);
                    }

               }
            });
        }
    }


    public CategoryAdapter(Context context, List<SelectedCategory> categoriesList) {
        this.context = context;
        this.categoriesList = categoriesList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.category_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        SelectedCategory category = categoriesList.get(position);

        holder.categories.setText(category.getNote());

        // Displaying dot from HTML character code
        holder.dot.setText(Html.fromHtml("&#8226;"));

        // Formatting and displaying timestamp
        holder.timestamp.setText(formatDate(category.getTimestamp()));
    }

    @Override
    public int getItemCount() {
        return categoriesList.size();
    }

    /**
     * Formatting timestamp to `MMM d` format
     * Input: 2018-02-21 00:15:42
     * Output: Feb 21
     */
    private String formatDate(String dateStr) {
        try {
            SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = fmt.parse(dateStr);
            SimpleDateFormat fmtOut = new SimpleDateFormat("MMM d");
            return fmtOut.format(date);
        } catch (ParseException e) {

        }

        return "";
    }
    // inteface to send callbacks
    public interface OnItemClickListener {
        Void onItemClick(int position);

        Void onItemLongClick(int position);
    }

    public void setOnClickListener(CategoryAdapter.OnItemClickListener listener) {
        mListener = listener;

    }
}
