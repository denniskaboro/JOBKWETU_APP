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
import com.e.jobkwetu.Model.Inspired_model;
import com.e.jobkwetu.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class InspiredAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Activity activity;
    private LayoutInflater inflater;
    private ArrayList<Inspired_model> InspiredArrayList;
    private static PopularAdapter.OnItemClickListener mListener;

    //ImageLoader imageLoader = MyApplication.getInstance().getImageLoader();

    public InspiredAdapter(Activity activity, ArrayList<Inspired_model> InspiredArrayList) {
        this.activity = activity;
        this.InspiredArrayList = InspiredArrayList;

    }


    @Override
    public int getItemCount() {
        return InspiredArrayList.size();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;
            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.inspired_row, parent, false);



        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        Inspired_model inspmod = InspiredArrayList.get(position);
        ((ViewHolder) holder).description.setText(inspmod.getDescroption());
        ((ViewHolder) holder).cost.setText(String.valueOf(inspmod.getCost()));
        ((ViewHolder) holder).ratting.setText(String.valueOf(inspmod.getRatting()));
        ((ViewHolder) holder).votes.setText(String.valueOf(inspmod.getVotes()));
        if (inspmod.getFavourite()){
            ((ViewHolder) holder).favorite.setImageResource(R.drawable.favorite_red);
        }else {
            ((ViewHolder) holder).favorite.setImageResource(R.drawable.favorite_grey);
        };
        Picasso.get().load(inspmod.getImage()).into(((ViewHolder) holder).image);


    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView description,ratting,votes,cost;
        ImageView image,favorite;

        public ViewHolder(View view) {
            super(view);
            description = (TextView) view.findViewById(R.id.inspired_description);
            ratting = (TextView) view.findViewById(R.id.inspired_ratting);
            votes = (TextView) view.findViewById(R.id.inspired_numbered);
            favorite = (ImageView) view.findViewById(R.id.inspired_like);
            cost = (TextView) view.findViewById(R.id.inspired_cost);
            image = (ImageView) view.findViewById(R.id.inspired_image);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String pop =cost.getText().toString();
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
