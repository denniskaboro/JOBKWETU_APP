package com.e.jobkwetu.Home_Fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.e.jobkwetu.Adapters.TaskAdapter;
import com.e.jobkwetu.App.EndPoints;
import com.e.jobkwetu.App.MyApplication;
import com.e.jobkwetu.Model.TasksList;
import com.e.jobkwetu.R;
import com.e.jobkwetu.Utilities.MyDividerItemDecoration;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Tasks_Fragment extends Fragment {

    private TaskAdapter mAdapter;
    private List<TasksList> notesList = new ArrayList<>();
    private RecyclerView recyclerView;
    private TextView noNotesView;
    private ProgressDialog pDialog;
    // Log tag
    private static final String TAG = Tasks_Fragment.class.getSimpleName();


    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.tasks_frag, container, false);
        }


        //coordinatorLayout = findViewById(R.id.coordinator_layout);
        recyclerView = view.findViewById(R.id.task_recycler_view);
        noNotesView = view.findViewById(R.id.empty_notes_view);


        mAdapter = new TaskAdapter(getActivity(), notesList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new MyDividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL, 16));
        recyclerView.setAdapter(mAdapter);

        toggleEmptyNotes();

        pDialog = new ProgressDialog(getContext());
        // Showing progress dialog before making http request
        pDialog.setMessage("Loading...");
        pDialog.show();
        GetTaskersList();

        return view;
    }

    /**
     *
     * Get data from database and render it on recyclerview
     */
    private void GetTaskersList() {
        // Creating volley request obj
        final String Token = MyApplication.getInstance().getPrefManager().getUser().getToken();
        StringRequest strReq = new StringRequest(Request.Method.GET,
                EndPoints.TASKS_LIST, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e(TAG, "response: " + response);
                pDialog.dismiss();

                Log.e(TAG, "response:1 " );
                try {
                    // Extract JSON array from the response
                    //JSONArray arr = new JSONArray(response);
                    Log.e(TAG, "response:2 " );
                    JSONObject obj = new JSONObject(response);
                    // check for error flag
                    if(obj.getBoolean("success")) {
                        // user successfully logged in
                        //JSONObject userObj = obj.getJSONObject("data");
                        //JSONObject userObj = obj.getJSONObject("data");
                        JSONArray userObj = obj.getJSONArray("data");
                        Log.e(TAG, "response:2 2" );
                        // Loop through each array element, get JSON object which has userid and username
                        for (int i = 0; i < userObj.length(); i++) {
                            // Get JSON object
                            JSONObject obj2 = (JSONObject) userObj.get(i);
                            TasksList model = new TasksList();
                            model.setTitle(obj2.getString("title"));
                            model.setDescription(obj2.getString("description"));
                            model.setSkills(obj2.getString("skills"));
                            model.setStart_date(obj2.getString("start_date"));
                            model.setSubcounty(obj2.getString("subcounty"));
                            notesList.add(model);
                            Log.e(TAG, "modellist: " + notesList);

                        }
                        // notifying list adapter about data changes so that it renders the list view with updated data
                        mAdapter.notifyDataSetChanged();

                    }


                } catch (JSONException e) {
                    Log.e(TAG, "ERROR " + e);
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                pDialog.dismiss();

            }
        }){
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params2 = new HashMap<>();
                params2.put("Authorization", "Bearer "+Token);
                params2.put("Accept", "application/json");

                Log.e(TAG, "params2: " + params2.toString());
                return params2;
            }
        };

// Adding request to request queue
        MyApplication.getInstance().addToRequestQueue(strReq);
    }


    /**
     * Toggling list and empty notes view
     */
    private void toggleEmptyNotes() {
        // you can check notesList.size() > 0

        if (mAdapter.getItemCount()> 0) {
            noNotesView.setVisibility(View.VISIBLE);
        } else {
            noNotesView.setVisibility(View.GONE);
        }


    }

}
