package com.e.jobkwetu.Adapters;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.e.jobkwetu.Activity.DetailedActivity;
import com.e.jobkwetu.Model.newly_joined_model;
import com.e.jobkwetu.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class NewlyJoinedAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Activity activity;
    private LayoutInflater inflater;
    private ArrayList<newly_joined_model> newlyArrayList;
    private static PopularAdapter.OnItemClickListener mListener;

    public NewlyJoinedAdapter(Activity activity, ArrayList<newly_joined_model> newlyArraylist) {
        this.activity = activity;
        this.newlyArrayList = newlyArraylist;

    }


    @Override
    public int getItemCount() {
        return newlyArrayList.size();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;
            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.newly_joined_row, parent, false);



        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        newly_joined_model newmodel = newlyArrayList.get(position);

        ((ViewHolder) holder).username.setText(newmodel.getUsername());
        ((ViewHolder) holder).category.setText(newmodel.getCategory());
        ((ViewHolder) holder).location.setText(newmodel.getLocation());
        ((ViewHolder) holder).date.setText(newmodel.getDate());
        Picasso.get().load(newmodel.getImage()).into(((ViewHolder) holder).imageView);
        //thumbNail.setImageUrl(m.getImage(), imageLoader);


    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView username,category,location,date;
        ImageView imageView;

        public ViewHolder(View view) {
            super(view);
            username = (TextView) view.findViewById(R.id.newly_username);
            category = (TextView) view.findViewById(R.id.newly_category);
            location = (TextView) view.findViewById(R.id.newly_loaction);
            date = (TextView) view.findViewById(R.id.newly_date);
            imageView = (ImageView) view.findViewById(R.id.newly_image);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String pop =username.getText().toString();
                    Intent intent = new Intent(view.getContext(), DetailedActivity.class);
                    view.getContext().startActivity(intent);
                    //Toast.makeText(view.getContext(), pop + "clicked", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
    // inteface to send callbacks
    public interface OnItemClickListener {
        Void onItemClick(int position);

        Void onItemLongClick(int position);
    }

    public void setOnClickListener(PopularAdapter.OnItemClickListener listener) {
        mListener = listener;

    }
}
