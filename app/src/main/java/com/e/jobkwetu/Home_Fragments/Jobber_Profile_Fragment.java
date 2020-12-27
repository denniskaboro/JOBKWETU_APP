package com.e.jobkwetu.Home_Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.e.jobkwetu.R;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class Jobber_Profile_Fragment extends Fragment {
    private View view;
    private CircleImageView imageView;
    private TextView username,skill,location,description,success,time,days,joining;
    private RatingBar ratting;
    private RecyclerView recyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.jobber_profile_frag, container, false);
        }
        imageView=(CircleImageView)view.findViewById(R.id.jobber_image);
        username=(TextView)view.findViewById(R.id.jobber_username);
        skill=(TextView)view.findViewById(R.id.jobber_skills);
        location=(TextView)view.findViewById(R.id.jobber_location);
        description=(TextView)view.findViewById(R.id.jobber_description);
        success=(TextView)view.findViewById(R.id.jobber_success);
        time=(TextView)view.findViewById(R.id.jobber_time);
        days=(TextView)view.findViewById(R.id.jobber_days);
        joining=(TextView)view.findViewById(R.id.jobber_joining);
        ratting=(RatingBar) view.findViewById(R.id.rating_bar);
        recyclerView=(RecyclerView) view.findViewById(R.id.jobber_reviews);

        String imageurl1 = getArguments().getString("thurmnailUrl");
        String username1 = getArguments().getString("username");
        String skill1 = getArguments().getString("skills");
        String location1 = getArguments().getString("location");
        String description1 = getArguments().getString("description");
        String success1 = getArguments().getString("success");
        String time1 = getArguments().getString("time");
        String days1 = getArguments().getString("days");
        String joining1 = getArguments().getString("joining");
        Float ratting1 = Float.valueOf(getArguments().getString("rating"));

        Picasso.get().load(imageurl1).into(imageView);
        username.setText(username1);
        skill.setText(skill1);
        location.setText(location1);
        description.setText(description1);
       // success.setText(success1);
        //time.setText(time1);
        //days.setText(days1);
        joining.setText(joining1);
        //ratting.setRating(ratting1);
        return view;
    }
}
