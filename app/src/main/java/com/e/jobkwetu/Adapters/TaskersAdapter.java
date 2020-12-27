package com.e.jobkwetu.Adapters;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.e.jobkwetu.Home_Fragments.Jobber_Profile_Fragment;
import com.e.jobkwetu.Model.Taskers;
import com.e.jobkwetu.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class TaskersAdapter extends RecyclerView.Adapter<TaskersAdapter.ViewHolder> {

    private Context context;
    private List<Taskers> taskers;
    private String imageurl;
    private int user_id1;
    private static TaskersAdapter.OnItemClickListener mListener;
    private TaskersAdapterListener listener;

    public interface TaskersAdapterListener {
        void onClickSelected(Taskers data);
    }

    public TaskersAdapter(Context context, List<Taskers> taskers,TaskersAdapterListener listener) {
        this.context = context;
        this.taskers = taskers;
        this.listener=listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.taskers_row,parent,false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        holder.username.setText(taskers.get(position).getUsername());
        holder.skills.setText(taskers.get(position).getSkills());
        holder.description.setText(taskers.get(position).getDescription());
        holder.location.setText(taskers.get(position).getLocation());
        holder.date_joined.setText(taskers.get(position).getDate_joined());
        holder.rating.setRating(taskers.get(position).getRating());
        Picasso.get().load(taskers.get(position).getThumbnailUrl()).into(holder.imageView);
        imageurl=taskers.get(position).getThumbnailUrl();
        user_id1= taskers.get(position).getJobber_id();
    }



    @Override
    public int getItemCount() {
        return taskers.size();
    }

    public  class ViewHolder extends  RecyclerView.ViewHolder{

        public TextView username;
        public TextView skills;
        public TextView description;
        public TextView location;
        public TextView date_joined;
        public ImageView imageView;
        public RatingBar rating;
        public Button view;
        public Button select;

        public ViewHolder(View itemView) {
            super(itemView);
            username = (TextView) itemView.findViewById(R.id.taskers_name);
            imageView = (ImageView) itemView.findViewById(R.id.tasker_image);
            skills = (TextView) itemView.findViewById(R.id.taskers_skill);
            description = (TextView) itemView.findViewById(R.id.taskers_description);
            location = (TextView) itemView.findViewById(R.id.taskers_location);
            date_joined = (TextView) itemView.findViewById(R.id.taskers_date_joined);
            view=(Button) itemView.findViewById(R.id.taskers_more);
            select=(Button) itemView.findViewById(R.id.taskers_select_btn);
            rating=(RatingBar) itemView.findViewById(R.id.taskers_rating_bar);
            //view.setOnClickListener(this);
            //select.setOnClickListener(this);
            select.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION && mListener != null) {
                        mListener.onItemClick(position);
                    }
                    showPopupMenu(view,position);

                }
            });
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION && mListener != null) {
                        mListener.onItemClick(position);
                    }
                    Jobber_Profile_Fragment jobber = new Jobber_Profile_Fragment();
                    Bundle args = new Bundle();
                    args.putString("thurmnailUrl",imageurl);
                    args.putString("username",username.getText().toString());
                    args.putString("skills",skills.getText().toString());
                    args.putString("location",location.getText().toString());
                    args.putString("description",description.getText().toString());
                    args.putString("success",username.getText().toString());
                    args.putString("rating", String.valueOf(rating.getRating()));
                    args.putString("time",username.getText().toString());
                    args.putString("days",username.getText().toString());
                    args.putString("joining",date_joined.getText().toString());
                    jobber.setArguments(args);
                    ((FragmentActivity)context).getSupportFragmentManager().beginTransaction()
                            .replace(R.id.container,jobber)
                            .addToBackStack(null)
                            .commit();

                }
            });

        }

    }

    private void showPopupMenu(View view, int poaition) {
        PopupMenu popup = new PopupMenu(context, view);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_context, popup.getMenu());
        popup.setOnMenuItemClickListener(new MenuClickListener(poaition));
        popup.show();
    }


    class MenuClickListener implements PopupMenu.OnMenuItemClickListener {
        Integer pos;
        public MenuClickListener(int pos) {
            this.pos=pos;
        }

        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.action_favourite:
                    Toast.makeText(context, taskers.get(pos).getUsername()+" is added to favourite", Toast.LENGTH_SHORT).show();
                    return true;
                case R.id.action_select:
                    Taskers data=taskers.get(pos);
                    listener.onClickSelected(data);
                    //Toast.makeText(context, taskers.get(pos).getUser_id()+" is added to watchlist", Toast.LENGTH_SHORT).show();
                    return true;
                default:
            }
            return false;
        }
    }
    // inteface to send callbacks
    public interface OnItemClickListener {
        Void onItemClick(int position);

        Void onItemLongClick(int position);
    }

    public void setOnClickListener(TaskersAdapter.OnItemClickListener listener) {
        mListener = listener;

    }
}
