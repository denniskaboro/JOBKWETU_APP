package com.e.jobkwetu.Home_Fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.e.jobkwetu.Adapters.HistoryAdapter;
import com.e.jobkwetu.Adapters.TaskersAdapter;
import com.e.jobkwetu.App.EndPoints;
import com.e.jobkwetu.App.MyApplication;
import com.e.jobkwetu.Model.History_Model;
import com.e.jobkwetu.Model.Register;
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

public class Taskers_list_Fragment extends Fragment implements TaskersAdapter.TaskersAdapterListener {
    private SelectedWorker mcallback;
    private View view;
    private ProgressDialog pDialog;

    // Log tag
    private static final String TAG = Taskers_list_Fragment.class.getSimpleName();
    private List<Taskers> modelList = new ArrayList<>();
    private RecyclerView recyclerview;
    private TaskersAdapter adapter;

    public interface SelectedWorker {
        void workersselected(Taskers data);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            mcallback = (SelectedWorker) context;
        } catch (ClassCastException e){
            throw new ClassCastException(context.toString() + "implement fragment to activity");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.taskers_list_frag, container, false);
        }


        recyclerview = (RecyclerView) view.findViewById(R.id.taskers_list);
        adapter = new TaskersAdapter(getActivity(), modelList,this);

        GetTaskersList();

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerview.setLayoutManager(mLayoutManager);
        recyclerview.setItemAnimator(new DefaultItemAnimator());
        recyclerview.addItemDecoration(new MyDividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL, 16));
        recyclerview.setAdapter(adapter);


        pDialog = new ProgressDialog(getContext());
        // Showing progress dialog before making http request
        pDialog.setMessage("Loading...");
        pDialog.show();




        return view;
    }

    private void GetTaskersList() {
        // Creating volley request obj
        final String Token = MyApplication.getInstance().getPrefManager().getUser().getToken();
        StringRequest strReq = new StringRequest(Request.Method.GET,
                EndPoints.TASKERS_LIST, new Response.Listener<String>() {
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
                            Taskers model = new Taskers();
                            model.setUsername(obj2.getString("username"));
                            model.setDescription(obj2.getString("description"));
                            model.setThumbnailUrl(obj2.getString("thumbnailUrl"));
                            model.setDate_joined(obj2.getString("date_joined"));
                            model.setSkills(obj2.getString("skills"));
                            model.setJobber_id(obj2.getInt("jobber_id"));
                            //model.setRating((float) obj2.getDouble("ratting"));
                            modelList.add(model);
                            Log.e(TAG, "modellist: " + modelList);


                        }
                        // notifying list adapter about data changes so that it renders the list view with updated data
                        adapter.notifyDataSetChanged();

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
    @Override
    public void onClickSelected(Taskers data) {
        mcallback.workersselected(data);
        //Toast.makeText(getActivity(), "Selected"+data.getUsername(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDetach() {
        mcallback=null;
        super.onDetach();
    }
}
