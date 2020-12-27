package com.e.jobkwetu.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.e.jobkwetu.Adapters.TransactionAdapter;
import com.e.jobkwetu.App.EndPoints;
import com.e.jobkwetu.App.MyApplication;
import com.e.jobkwetu.Dialogue.MpesaConfirmation;
import com.e.jobkwetu.Model.trans;
import com.e.jobkwetu.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.e.jobkwetu.App.MyApplication.TAG;


public class TransactionsActivity extends AppCompatActivity {

    private ProgressDialog pDialog;
    private Button deposit;
    private TextView balance;

    private TextView notrans;
    private ArrayList<trans> transArrayList;
    private RecyclerView listView;
    private TransactionAdapter adapter;

    public static final String MyPREFERENCES = "MyPrefs" ;
    public static final String Name = "nameKey";
    SharedPreferences sharedpreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.settings_activity);
        setContentView(R.layout.activity_transaction);

        deposit=findViewById(R.id.tv_deposit);
        balance=findViewById(R.id.tv_price);
        listView=findViewById(R.id.transaction_list);
        notrans=findViewById(R.id.empty_notes_view2);
        notrans.setVisibility(View.GONE);

        transArrayList = new ArrayList<>();
        adapter = new TransactionAdapter(this, transArrayList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false);
        listView.setLayoutManager(layoutManager);
        listView.setItemAnimator(new DefaultItemAnimator());
        listView.setAdapter(adapter);
        pDialog = new ProgressDialog(this);
        // Showing progress dialog before making http request
        pDialog.setMessage("Loading...");
        pDialog.show();

        CheckTransationlist();

        deposit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                FragmentManager fm = getSupportFragmentManager();
                MpesaConfirmation MpesaConfirmationFragment = MpesaConfirmation.newInstance("Some Title");
                MpesaConfirmationFragment.setArguments(bundle);
                MpesaConfirmationFragment.show(fm, "fragment_edit_name");
            }
        });
        CheckBalance();
        sharedpreferences = getSharedPreferences(MyPREFERENCES,
                Context.MODE_PRIVATE);

        if (sharedpreferences.contains(Name)) {
            balance.setText(sharedpreferences.getString(Name, ""));
        }


        /*mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("Processing your request");
        mProgressDialog.setTitle("Please Wait...");
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.show();
        */





    }

    private void CheckTransationlist() {
        // Creating volley request obj
        StringRequest strReq = new StringRequest(Request.Method.GET,
                EndPoints.TRANSATION, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e(TAG, "response: " + response);
                hidePDialog();

                try {
                    JSONObject obj = new JSONObject(response);

                    // check for error
                    if(obj.getBoolean("success")) {
                        JSONArray userObj = obj.getJSONArray("data");

                        for (int i = 0; i < userObj.length(); i++) {
                            JSONObject popularObj = (JSONObject) userObj.get(i);
                            //JSONObject obj = userObj.getJSONObject(String.valueOf(i));
                            trans model = new trans();
                            model.setId(String.valueOf(i));
                            model.setDate(popularObj.getString("TransTime"));
                            model.setTransaction(popularObj.getString("TransID"));
                            model.setAmount(popularObj.getString("TransAmount"));
                            model.setStatus(popularObj.getString("BillRefNumber"));
                            // newlymodel= new newly_joined_model(username,image,category,location,date);
                            transArrayList.add(model);
                            Toast.makeText(TransactionsActivity.this, model.getId(), Toast.LENGTH_SHORT).show();
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
                            Toast.makeText(getApplicationContext(), "" + obj.getJSONObject("error").getString("message"), Toast.LENGTH_LONG).show();
                        hidePDialog();
                        }

                    } catch (JSONException e) {
                    notrans.setVisibility(View.VISIBLE);
                        Log.e(TAG, "json parsing error: " + e.getMessage());
                        Toast.makeText(getApplicationContext(), "json parse error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    hidePDialog();
                    }
                }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                NetworkResponse networkResponse = error.networkResponse;
                notrans.setVisibility(View.VISIBLE);
                Log.e(TAG, "Volley error: " + error.getMessage() + ", code: " + networkResponse);
                Toast.makeText(getApplicationContext(), "Volley error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
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

    private void CheckBalance() {
        final String Token = MyApplication.getInstance().getPrefManager().getUser().getToken();
        final String userid = MyApplication.getInstance().getPrefManager().getUser().getId();
        StringRequest strReq = new StringRequest(Request.Method.GET,
                EndPoints.MPESABALANCE, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e(TAG, "response: " + response);

                try {
                    JSONObject obj = new JSONObject(response);

                    // check for error flag
                    if (true == obj.getBoolean("success")) {
                        // user successfully logged in

                        JSONObject userObj = obj.getJSONObject("data");


                            String n = userObj.getString("balance");
                        //Toast.makeText(TransactionsActivity.this, n, Toast.LENGTH_SHORT).show();


                            sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedpreferences.edit();
                            editor.putString(Name, n);
                            editor.commit();
                            //final String balance22 = MyApplication.getInstance().getPrefManager().getUser().getToken();
                            sharedpreferences = getSharedPreferences(MyPREFERENCES,
                                    Context.MODE_PRIVATE);

                            if (sharedpreferences.contains(Name)) {
                                balance.setText("KSH: "+sharedpreferences.getString(Name, ""));
                            }


                        //WHAT TODO


                    } else {
                        // login error - simply toast the message
                        Toast.makeText(TransactionsActivity.this, "" + obj.getString("message"), Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException e) {
                    Log.e(TAG, "json parsing error: " + e.getMessage());
                    Toast.makeText(TransactionsActivity.this, "Json parse error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                String json = null;
                NetworkResponse networkResponse = error.networkResponse;
                Log.e(TAG, "Volley error: " + error + ", code: " + networkResponse);
                Toast.makeText(TransactionsActivity.this, "Volley error: " + error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }

        }) {



            @Override
            public Map<String, String> getHeaders() {
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
