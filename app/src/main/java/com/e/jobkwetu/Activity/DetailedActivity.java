package com.e.jobkwetu.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.e.jobkwetu.Adapters.InspiredAdapter;
import com.e.jobkwetu.Adapters.PopularAdapter;
import com.e.jobkwetu.App.EndPoints;
import com.e.jobkwetu.App.MyApplication;
import com.e.jobkwetu.Model.Detailed;
import com.e.jobkwetu.Model.Inspired_model;
import com.e.jobkwetu.Model.Popular_Model;
import com.e.jobkwetu.R;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DetailedActivity extends AppCompatActivity {
    private static final String TAG = "DetailedActivity";
    private RecyclerView insprecycler;
    private ArrayList<Inspired_model> InspArrayList;
    private InspiredAdapter mAdapter4;
    private PopularAdapter mAdapter2;
    private RecyclerView poprecycler;
    private ArrayList<Popular_Model> popularArrayList;
    private Detailed detailed;
    private TextView name;
    private TextView level;
    private TextView category;
    private TextView description;
    private TextView ratting;
    private RatingBar ratingbar;
    private ImageView thumbNail;
    private String image3;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detailed_fragment);

        insprecycler = findViewById(R.id.detailed_review_rec2);
        InspArrayList = new ArrayList<>();
        mAdapter4 = new InspiredAdapter(this, InspArrayList);
        LinearLayoutManager layoutManager3 = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,false);
        insprecycler.setLayoutManager(layoutManager3);
        insprecycler.setItemAnimator(new DefaultItemAnimator());
        insprecycler.setAdapter(mAdapter4);
        fetchIspiredThread();

        poprecycler = findViewById(R.id.detailed_review_rec);
        popularArrayList = new ArrayList<>();
        mAdapter2 = new PopularAdapter(this, popularArrayList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,false);
        poprecycler.setLayoutManager(layoutManager);
        poprecycler.setItemAnimator(new DefaultItemAnimator());
        poprecycler.setAdapter(mAdapter2);
        fetchPopularThread();

        fetchDetailedThread();
        name = (TextView) findViewById(R.id.detailed_name);
        level = (TextView) findViewById(R.id.detailed_level);
        category = (TextView) findViewById(R.id.detailed_category);
        description = (TextView)findViewById(R.id.detailed_description);
        ratting = (TextView) findViewById(R.id.detailed_rate);
        ratingbar =(RatingBar) findViewById(R.id.detailed_ratting);
        thumbNail = (ImageView) findViewById(R.id.detailed_image);
        final Button next =(Button) findViewById(R.id.detailed_continue);

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String description1 = description.getText().toString();
                String category1 = category.getText().toString();
                String location1= "Emali";
                String cost1= "$10";
                Intent intent =new Intent(getApplicationContext(),OrderActivity.class);
                intent.putExtra("image2",image3);
                intent.putExtra("description2",description1);
                intent.putExtra("category2",category1);
                intent.putExtra("location2",location1);
                intent.putExtra("cost2",cost1);
                startActivity(intent);
                finish();
            }
        });


    }

    private void fetchPopularThread() {

        String endPoint = EndPoints.POPULAR;
        Log.e(TAG, "endPoint: " + endPoint);

        StringRequest strReq = new StringRequest(Request.Method.GET,
                endPoint, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e(TAG, "response: " + response);

                try {
                    JSONObject obj = new JSONObject(response);

                    // check for error
                    if(obj.getBoolean("success")) {
                        JSONArray userObj = obj.getJSONArray("data");

                        for (int i = 0; i < userObj.length(); i++) {
                            JSONObject popularObj = (JSONObject) userObj.get(i);

                            String populartitle = popularObj.getString("title");

                            String popularimage = popularObj.getString("image");
                            //String createdAt = popularObj.getString("created_at");

                            //JSONObject userObj = commentObj.getJSONObject("user");
                            //String userId = userObj.getString("user_id");
                            //String userName = userObj.getString("username");
                            //User user = new User(userId, userName, null);

                            Popular_Model popular= new Popular_Model(populartitle,popularimage);
                            //String populaertitle2=popular.getTitle();
                            //popular.setId(commentId);

                            popularArrayList.add(popular);
                        }

                        mAdapter2.notifyDataSetChanged();
                        if (mAdapter2.getItemCount() > 1) {
                            poprecycler.getLayoutManager().smoothScrollToPosition(poprecycler, null, mAdapter2.getItemCount() + 1);
                        }

                    } else {
                        Toast.makeText(getApplicationContext(), "" + obj.getJSONObject("error").getString("message"), Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException e) {
                    Log.e(TAG, "json parsing error: " + e.getMessage());
                    Toast.makeText(getApplicationContext(), "json parse error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                NetworkResponse networkResponse = error.networkResponse;
                Log.e(TAG, "Volley error: " + error.getMessage() + ", code: " + networkResponse);
                Toast.makeText(getApplicationContext(), "Volley error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() {
                final String Token = MyApplication.getInstance().getPrefManager().getUser().getToken();

                Map<String, String> params2 = new HashMap<>();
                params2.put("Authorization", "Bearer " + Token);
                params2.put("Accept", "application/json");

                Log.e(TAG, "params2: " + params2.toString());
                return params2;
            }
        };

        //Adding request to request queue
        MyApplication.getInstance().addToRequestQueue(strReq);
    }

    private void fetchIspiredThread() {

        String endPoint = EndPoints.INSPIRED;
        Log.e(TAG, "endPoint: " + endPoint);

        StringRequest strReq = new StringRequest(Request.Method.GET,
                endPoint, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e(TAG, "response: " + response);

                try {
                    JSONObject obj = new JSONObject(response);

                    // check for error
                    if (obj.getBoolean("success")) {
                        JSONArray userObj = obj.getJSONArray("data");

                        for (int i = 0; i < userObj.length(); i++) {
                            JSONObject popularObj = (JSONObject) userObj.get(i);

                            String description = popularObj.getString("description");
                            Double ratting = popularObj.getDouble("ratting");
                            Integer votes = popularObj.getInt("votes");
                            Integer cost = popularObj.getInt("cost");
                            Boolean favorite = popularObj.getBoolean("favorite");
                            String image = popularObj.getString("image");
                            Inspired_model inspmodel = new Inspired_model(image, description, favorite, cost, votes, ratting);
                            InspArrayList.add(inspmodel);
                        }

                        mAdapter4.notifyDataSetChanged();
                        if (mAdapter4.getItemCount() > 1) {
                            insprecycler.getLayoutManager().smoothScrollToPosition(insprecycler, null, mAdapter4.getItemCount() + 1);
                        }

                    } else {
                        Toast.makeText(getApplicationContext(), "" + obj.getJSONObject("error").getString("message"), Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException e) {
                    Log.e(TAG, "json parsing error: " + e.getMessage());
                    Toast.makeText(getApplicationContext(), "json parse error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                NetworkResponse networkResponse = error.networkResponse;
                Log.e(TAG, "Volley error: " + error.getMessage() + ", code: " + networkResponse);
                Toast.makeText(getApplicationContext(), "Volley error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() {
                final String Token = MyApplication.getInstance().getPrefManager().getUser().getToken();

                Map<String, String> params2 = new HashMap<>();
                params2.put("Authorization", "Bearer " + Token);
                params2.put("Accept", "application/json");

                Log.e(TAG, "params2: " + params2.toString());
                return params2;
            }
        };

        //Adding request to request queue
        MyApplication.getInstance().addToRequestQueue(strReq);
    }
    private void fetchDetailedThread() {

        String endPoint = EndPoints.DETAILED;
        Log.e(TAG, "endPoint: " + endPoint);

        StringRequest strReq = new StringRequest(Request.Method.GET,
                endPoint, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e(TAG, "response: " + response);

                try {
                    JSONObject obj = new JSONObject(response);

                    // check for error
                    if(obj.getBoolean("success")) {
                        JSONArray userObj = obj.getJSONArray("data");

                        for (int i = 0; i < userObj.length(); i++) {
                            JSONObject popularObj = (JSONObject) userObj.get(i);

                            String user_id = popularObj.getString("user_id");
                            String image = popularObj.getString("image");
                            String name = popularObj.getString("name");
                            Integer level = popularObj.getInt("level");
                            String description = popularObj.getString("description");
                            String title = popularObj.getString("title");
                            Double ratting = popularObj.getDouble("ratting");
                            String date_joined = popularObj.getString("date_joined");
                            image3=image;
                            detailed = new Detailed(user_id, image, name, description, title, date_joined, level, ratting);
                            //DetailedArrayList.add(detailed);
                            AddData();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "" + obj.getJSONObject("error").getString("message"), Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException e) {
                    Log.e(TAG, "json parsing error: " + e.getMessage());
                    Toast.makeText(getApplicationContext(), "json parse error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                NetworkResponse networkResponse = error.networkResponse;
                Log.e(TAG, "Volley error: " + error.getMessage() + ", code: " + networkResponse);
                Toast.makeText(getApplicationContext(), "Volley error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() {
                final String Token = MyApplication.getInstance().getPrefManager().getUser().getToken();

                Map<String, String> params2 = new HashMap<>();
                params2.put("Authorization", "Bearer " + Token);
                params2.put("Accept", "application/json");

                Log.e(TAG, "params2: " + params2.toString());
                return params2;
            }
        };

        //Adding request to request queue
        MyApplication.getInstance().addToRequestQueue(strReq);
    }

    private void AddData() {
        //name
        name.setText("Name: " + detailed.getName());
        // category
        category.setText("Category: " + detailed.getTitle());
        // thumbnail image
        Picasso.get().load(detailed.getImage()).into(thumbNail);
        //thumbNail.setImageUrl(detailed.getImage(), imageLoader);
        level.setText("LEVEL: "+String.valueOf(detailed.getLevel()));
        description.setText(detailed.getDescription());
        ratting.setText(String.valueOf(detailed.getRatting()));
        ratingbar.setRating(detailed.getRatting().floatValue());

    }
}