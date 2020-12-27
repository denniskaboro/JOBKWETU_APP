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
import com.e.jobkwetu.Model.Popular_Model;
import com.e.jobkwetu.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class PopularAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Activity activity;
    private LayoutInflater inflater;
    private ArrayList<Popular_Model> popularArrayList;
    private static PopularAdapter.OnItemClickListener mListener;

    //ImageLoader imageLoader = MyApplication.getInstance().getImageLoader();

    public PopularAdapter(Activity activity, ArrayList<Popular_Model> popularArrayList) {
        this.activity = activity;
        this.popularArrayList = popularArrayList;
    }



    @Override
    public int getItemCount() {
        return popularArrayList.size();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;
            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.popular_row, parent, false);



        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        Popular_Model popmod = popularArrayList.get(position);

        ((ViewHolder) holder).title.setText(popmod.getTitle());
        // thumbnail image
        Picasso.get().load(popmod.getImage()).into(((ViewHolder) holder).thumbnail);
        //thumbNail.setImageUrl(m.getImage(), imageLoader);


    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        ImageView thumbnail;

        public ViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.popular_title);
            thumbnail = (ImageView) view.findViewById(R.id.popular_image);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String pop =title.getText().toString();
                    Intent intent = new Intent(view.getContext(), DetailedActivity.class);
                    view.getContext().startActivity(intent);
                   // Toast.makeText(view.getContext(), pop + "clicked", Toast.LENGTH_SHORT).show();
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
