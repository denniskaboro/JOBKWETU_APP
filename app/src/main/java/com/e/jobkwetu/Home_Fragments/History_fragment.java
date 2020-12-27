package com.e.jobkwetu.Home_Fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.e.jobkwetu.Adapters.HistoryAdapter;
import com.e.jobkwetu.App.EndPoints;
import com.e.jobkwetu.App.MyApplication;
import com.e.jobkwetu.Model.History_Model;
import com.e.jobkwetu.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class History_fragment extends Fragment {
    private ProgressDialog pDialog;

    // Log tag
    private static final String TAG = History_fragment.class.getSimpleName();
    private TextView notrans;

    private ArrayList<History_Model> transArrayList;
    private RecyclerView listView;
    private HistoryAdapter adapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.history_frag, container, false);

        notrans=view.findViewById(R.id.empty_notes_view2);
        notrans.setVisibility(View.GONE);

        listView = (RecyclerView) view.findViewById(R.id.history_recycler);
        transArrayList = new ArrayList<>();
        adapter = new HistoryAdapter(getActivity(), transArrayList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL,false);
        listView.setLayoutManager(layoutManager);
        listView.setItemAnimator(new DefaultItemAnimator());
        listView.setAdapter(adapter);
        pDialog = new ProgressDialog(getContext());
        // Showing progress dialog before making http request
        pDialog.setMessage("Loading...");
        pDialog.show();


        Fetchdata();
        return view;
    }

    private void Fetchdata() {
        StringRequest strReq = new StringRequest(Request.Method.GET,
                EndPoints.HISTORY, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e(TAG, "response: " + response);
                hidePDialog();
                try {
                    JSONObject obj = new JSONObject(response);
                    if(obj.getBoolean("success")) {
                        JSONArray userObj = obj.getJSONArray("data");
                        for (int i = 0; i < userObj.length(); i++) {
                            // Get JSON object
                            JSONObject histObj = (JSONObject) userObj.get(i);

                            History_Model model = new History_Model();
                            model.setName(histObj.getString("name"));
                            model.setDescription(histObj.getString("description"));
                            model.setThumbnailUrl(histObj.getString("image"));
                            model.setCost(histObj.getString("cost"));
                            model.setDate(histObj.getString("date"));
                            model.setRatting((float) histObj.getDouble("ratting"));

                            // newlymodel= new newly_joined_model(username,image,category,location,date);
                            transArrayList.add(model);
                            hidePDialog();
                        }
                        adapter.notifyDataSetChanged();
                        //Toast.makeText(TransactionsActivity.this, , Toast.LENGTH_SHORT).show();
                        if (adapter.getItemCount() > 1) {
                            //hidePDialog();
                            listView.getLayoutManager().smoothScrollToPosition(listView, null, adapter.getItemCount() + 1);
                        }else {notrans.setVisibility(View.VISIBLE);}

                    } else {
                        notrans.setVisibility(View.VISIBLE);
                        Toast.makeText(getActivity(), "" + obj.getJSONObject("error").getString("message"), Toast.LENGTH_LONG).show();
                        hidePDialog();
                    }

                } catch (JSONException e) {
                    notrans.setVisibility(View.VISIBLE);
                    Log.e(TAG, "json parsing error: " + e.getMessage());
                    Toast.makeText(getActivity(), "json parse error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    hidePDialog();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                NetworkResponse networkResponse = error.networkResponse;
                notrans.setVisibility(View.VISIBLE);
                Log.e(TAG, "Volley error: " + error.getMessage() + ", code: " + networkResponse);
                Toast.makeText(getActivity(), "Volley error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                hidePDialog();
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


    @Override
    public void onDestroy() {
        super.onDestroy();
        hidePDialog();
    }

    private void hidePDialog() {
        if (pDialog != null) {
            pDialog.dismiss();
            pDialog = null;
        }
    }


}
