package com.e.jobkwetu.Home_Fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.e.jobkwetu.Adapters.TaskersAdapter;
import com.e.jobkwetu.App.EndPoints;
import com.e.jobkwetu.App.MyApplication;
import com.e.jobkwetu.Model.Taskers;
import com.e.jobkwetu.R;
import com.e.jobkwetu.Utilities.MyDividerItemDecoration;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Task_list_Fragment extends Fragment {
    private View view;
    private ProgressDialog pDialog;

    // Log tag
    private static final String TAG = Task_list_Fragment.class.getSimpleName();
    private List<Taskers> notesList = new ArrayList<>();
    private RecyclerView recyclerView;
    private TaskersAdapter adapter;
    private TextView noNotesView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.task_list_frag, container, false);
        }



        //coordinatorLayout = findViewById(R.id.coordinator_layout);
        recyclerView = view.findViewById(R.id.taskers_list);
        noNotesView = view.findViewById(R.id.empty_notes_view);

        //adapter = new TaskersAdapter(getActivity(), notesList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new MyDividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL, 16));
        recyclerView.setAdapter(adapter);

        //toggleEmptyNotes();

        pDialog = new ProgressDialog(getContext());
        // Showing progress dialog before making http request
        pDialog.setMessage("Loading...");
        pDialog.show();
        GetTaskersList();



        return view;
    }

    private void GetTaskersList() {
        // Creating volley request obj
        StringRequest strReq = new StringRequest(Request.Method.GET,
                EndPoints.TASKERS_LIST, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e(TAG, "response: " + response);
                pDialog.dismiss();

                try {
                    JSONObject obj = new JSONObject(response);

                    // check for error
                    if(obj.getBoolean("success")) {
                        JSONArray userObj = obj.getJSONArray("data");

                        for (int i = 0; i < userObj.length(); i++) {
                            JSONObject popularObj = (JSONObject) userObj.get(i);
                            //JSONObject obj = userObj.getJSONObject(String.valueOf(i));
                            Taskers model = new Taskers();
                            model.setUsername(popularObj.getString("name"));
                            model.setDescription(popularObj.getString("description"));
                            model.setThumbnailUrl(popularObj.getString("image"));
                            model.setDate_joined(popularObj.getString("cost"));
                            model.setSkills(popularObj.getString("date"));
                            model.setRating((float) popularObj.getDouble("ratting"));

                            // newlymodel= new newly_joined_model(username,image,category,location,date);
                         //   transArrayList.add(model);
                         //   hidePDialog();
                        }
                        adapter.notifyDataSetChanged();
                        //Toast.makeText(TransactionsActivity.this, , Toast.LENGTH_SHORT).show();
                        if (adapter.getItemCount() > 1) {
                            //hidePDialog();
                           // listView.getLayoutManager().smoothScrollToPosition(listView, null, adapter.getItemCount() + 1);
                        }//else {notrans.setVisibility(View.VISIBLE);}

                    } else {
                       // notrans.setVisibility(View.VISIBLE);
                        Toast.makeText(getActivity(), "" + obj.getJSONObject("error").getString("message"), Toast.LENGTH_LONG).show();
                      //  hidePDialog();
                    }

                } catch (JSONException e) {
                    //notrans.setVisibility(View.VISIBLE);
                    Log.e(TAG, "json parsing error: " + e.getMessage());
                    Toast.makeText(getActivity(), "json parse error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                   // hidePDialog();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                NetworkResponse networkResponse = error.networkResponse;
               // notrans.setVisibility(View.VISIBLE);
                Log.e(TAG, "Volley error: " + error.getMessage() + ", code: " + networkResponse);
                Toast.makeText(getActivity(), "Volley error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            //    hidePDialog();
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
// Adding request to request queue
        MyApplication.getInstance().addToRequestQueue(strReq);
    }
}
