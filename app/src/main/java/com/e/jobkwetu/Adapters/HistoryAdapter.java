package com.e.jobkwetu.Adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.e.jobkwetu.Model.History_Model;
import com.e.jobkwetu.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class HistoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Activity activity;
    private LayoutInflater inflater;
    private ArrayList<History_Model> transArrayList;


    public HistoryAdapter(Activity activity, ArrayList<History_Model> transArrayList) {
        this.activity = activity;
        this.transArrayList = transArrayList;
    }


    @Override
    public int getItemCount() {
        return transArrayList.size();
    }


    @Override
    public long getItemId(int position) {
        return position;

    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;
        itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.history_row, parent, false);


        return new HistoryAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        // getting model data for the row
        History_Model m = transArrayList.get(position);
        // title
        ((ViewHolder) holder).title.setText("Title: " + m.getName());
        // category
        ((ViewHolder) holder).category.setText("Category: " + m.getDescription());
        // thumbnail image
        Picasso.get().load(m.getThumbnailUrl()).into(((ViewHolder) holder).thumbNail);
        ((ViewHolder) holder).cost.setText("Paid:" + m.getCost());
        ((ViewHolder) holder).date.setText("Paid:" + m.getDate());
        ((ViewHolder) holder).ratting.setRating(m.getRatting());


    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView category,cost,date,title;
        RatingBar ratting;
        ImageView thumbNail;
        public ViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.done_by);
            category = (TextView) view.findViewById(R.id.description);
            cost = (TextView) view.findViewById(R.id.cost_paid);
            thumbNail=(ImageView) view.findViewById(R.id.history_image);
            date = (TextView) view.findViewById(R.id.date_done);
            ratting = (RatingBar) view.findViewById(R.id.rating_bar);

        }
    }
}
