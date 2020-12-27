package com.e.jobkwetu.Activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.e.jobkwetu.R;
import com.squareup.picasso.Picasso;

public class OrderActivity extends AppCompatActivity {
    private ImageView imageView;
    private TextView description3,category3,location3,cost3,description4;
    @SuppressLint("CutPasteId")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_reveiw_activity);
        Intent intent =getIntent();
        String image = intent.getStringExtra("image2");
        String description = intent.getStringExtra("description2");
        String category = intent.getStringExtra("category2");
        String location = intent.getStringExtra("location2");
        String cost = intent.getStringExtra("cost2");

        imageView=(ImageView) findViewById(R.id.order_image);
        description3 =(TextView) findViewById(R.id.order_description);
        description4 =(TextView) findViewById(R.id.order_description2);
        category3 =(TextView) findViewById(R.id.order_category);
        location3 =(TextView) findViewById(R.id.order_location);
        cost3 =(TextView) findViewById(R.id.order_cost);

        Picasso.get().load(image).into(imageView);
        description3.setText(description);
        description4.setText(description);
        category3.setText(category);
        location3.setText(location);
        cost3.setText(cost);

    }
}
