package com.e.jobkwetu.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.e.jobkwetu.App.EndPoints;
import com.e.jobkwetu.App.MyApplication;
import com.e.jobkwetu.Dialogue.ConfirmationDialogue2;
import com.e.jobkwetu.Helper.DatabaseHelper;
import com.e.jobkwetu.Home_Fragments.Taskers_list_Fragment;
import com.e.jobkwetu.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FillTaskFormActivity extends AppCompatActivity  implements AdapterView.OnItemSelectedListener {
    private static final String TAG = FillTaskFormActivity.class.getSimpleName();
    DatabaseHelper dbhelper;
    HashMap<String, String> queryValues;
    private TextView category,service,worker,Date;
    private EditText address,description,title;
    private Spinner spinner1,spinner2,spinner3;
    private CardView submit;
    private DatabaseHelper db;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private Integer txt;
    private String jobber_name;
    private int jobber_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.task_form_frag);

        dbhelper = new DatabaseHelper(this);
        spinner1 = (Spinner) findViewById(R.id.spn_category);
        spinner2 = (Spinner) findViewById(R.id.spn_service);
        spinner3 = (Spinner) findViewById(R.id.spn_subcounty);
        worker = (TextView) findViewById(R.id.txt_worker);
        Date = (TextView) findViewById(R.id.work_date);
        submit = (CardView) findViewById(R.id.submit);
        address = (EditText) findViewById(R.id.locat2);
        description =(EditText) findViewById(R.id.descr2);
        title =(EditText) findViewById(R.id.titl2);

        Intent intent = getIntent();
        if (intent.getExtras() != null) {
            jobber_name= intent.getStringExtra("data");
            jobber_id = intent.getIntExtra("data2",0);
            worker.setText(jobber_name);
        }

        worker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFragment(new Taskers_list_Fragment());
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(getContext(), "Array" +notesList, Toast.LENGTH_SHORT).show();
                if(!ValidateLocation()) {
                    return;
                }
                if(!ValidateDescription()) {
                    return;
                }
                showEditDialog();

            }
        });
        Date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        FillTaskFormActivity.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mDateSetListener,
                        year, month, day);

                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }

        });
        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                Log.d(TAG, "onDateSet: dd/mm/yyy: " + day + "/" + month + "/" + year);

                String date = day + "/" + month + "/" + year;
                txt = month;
                Date.setText(date);
            }
        };



        spinner1.setOnItemSelectedListener(this);
        spinner2.setOnItemSelectedListener(this);
        spinner3.setOnItemSelectedListener(this);


        //loading spinner data from database
        load_spinner_data_category();
        load_spinner_data_service();
        load_spinner_data_subcounty();


        //calling method sync sqlite
        syncSQLiteMySQLDBCategory();
        syncSQLiteMySQLDBService();
        syncSQLiteMySQLDBSubcounty();


    }

    private void syncSQLiteMySQLDBSubcounty() {
        final String Token = MyApplication.getInstance().getPrefManager().getUser().getToken();

        // Creating volley request obj
        StringRequest strReq = new StringRequest(Request.Method.GET,
                EndPoints.SUBCOUNTY, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e(TAG, "response: " + response);
                // hidePDialog();
                //   Toast.makeText(getContext(), "Response"+response, Toast.LENGTH_SHORT).show();

                // Parsing json
                UpdateSqliteSubcounty(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                //  Toast.makeText(getContext(), "Error"+error.getMessage(), Toast.LENGTH_SHORT).show();
                //hidePDialog();


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

    private void load_spinner_data_subcounty() {
        //database handler
        db = new DatabaseHelper(this);


        //spinner drop down elements
        List<String> labels = db.getAllSubcounty();

        //Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, labels);

        //drop down layout style - list view with radio button
        dataAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //attaching data adapter to spinner
        spinner3.setAdapter(dataAdapter);

    }

    public void openFragment(Fragment fragment) {
        FragmentTransaction transaction =this.getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, fragment);
        transaction.addToBackStack(null);
        //activefragment.add(fragment);
        transaction.commit();
    }

    // Method to Sync MySQL to SQLite DB
    private void UpdateSqliteWorker(String response) {
        //ArrayList<HashMap<String, String>> categorysynclist;
        //categorysynclist = new ArrayList<HashMap<String, String>>();
        // Create GSON object
        //Gson gson = new GsonBuilder().create();
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

                //System.out.println(arr.length());
                // If no of array elements is not zero
                // if(arr.length() != 0){
                // Loop through each array element, get JSON object which has userid and username
                for (int i = 0; i < userObj.length(); i++) {
                    // Get JSON object
                    JSONObject obj2 = (JSONObject) userObj.get(i);
                    System.out.println(obj2.get("id"));
                    System.out.println(obj2.get("category"));
                    // DB QueryValues Object to insert into SQLite
                    queryValues = new HashMap<String, String>();
                    // Add userID extracted from Object
                    queryValues.put("id", obj2.get("id").toString());
                    // Add userName extracted from Object
                    queryValues.put("category", obj2.get("category").toString());
                    // Insert User into SQLite DB
                    dbhelper.insertCategory(queryValues);
                    HashMap<String, String> map = new HashMap<String, String>();
                    // Add status for each User in Hashmap
                    map.put("id", obj2.get("id").toString());
                    map.put("status", "1");
                    // categorysynclist.add(map);
                    Log.e(TAG, "response:3 " + queryValues);

                }




                // Inform Remote MySQL DB about the completion of Sync activity by passing Sync status of Users
                // updateMySQLSyncSts(gson.toJson(categorysynclist));
                // Reload the Main Activity
                //reloadActivity();
            }


        } catch (JSONException e) {
            Log.e(TAG, "ERROR " + e);
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    // Inform Remote MySQL DB about the completion of Sync activity by passing Sync status of Users
    private void updateMySQLSyncSts(String toJson) {

    }

    private void UpdateSqliteSubcounty(String response) {
        //ArrayList<HashMap<String, String>> categorysynclist;
        //categorysynclist = new ArrayList<HashMap<String, String>>();
        // Create GSON object
        //Gson gson = new GsonBuilder().create();
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

                //System.out.println(arr.length());
                // If no of array elements is not zero
                // if(arr.length() != 0){
                // Loop through each array element, get JSON object which has userid and username
                for (int i = 0; i < userObj.length(); i++) {
                    // Get JSON object
                    JSONObject obj2 = (JSONObject) userObj.get(i);
                    System.out.println(obj2.get("id"));
                    System.out.println(obj2.get("subcounty"));
                    // DB QueryValues Object to insert into SQLite
                    queryValues = new HashMap<String, String>();
                    // Add userID extracted from Object
                    queryValues.put("id", obj2.get("id").toString());
                    // Add userName extracted from Object
                    queryValues.put("subcounty", obj2.get("subcounty").toString());
                    // Insert User into SQLite DB
                    dbhelper.insertSubcounty(queryValues);
                    HashMap<String, String> map = new HashMap<String, String>();
                    // Add status for each User in Hashmap
                    map.put("id", obj2.get("id").toString());
                    map.put("status", "1");
                    // categorysynclist.add(map);
                    Log.e(TAG, "response:3 " + queryValues);

                }




                // Inform Remote MySQL DB about the completion of Sync activity by passing Sync status of Users
                // updateMySQLSyncSts(gson.toJson(categorysynclist));
                // Reload the Main Activity
                //reloadActivity();
            }


        } catch (JSONException e) {
            Log.e(TAG, "ERROR " + e);
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }


    // Method to Sync MySQL to SQLite DB
    private void syncSQLiteMySQLDBService() {
        final String Token = MyApplication.getInstance().getPrefManager().getUser().getToken();

        // Creating volley request obj
        StringRequest strReq = new StringRequest(Request.Method.GET,
                EndPoints.TASKER, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e(TAG, "response: " + response);
                // hidePDialog();
                // Toast.makeText(getContext(), "Response"+response, Toast.LENGTH_SHORT).show();

                // Parsing json
                UpdateSqliteTasker(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                //  Toast.makeText(getContext(), "Error"+error.getMessage(), Toast.LENGTH_SHORT).show();
                //hidePDialog();


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

    private void UpdateSqliteTasker(String response) {
        //ArrayList<HashMap<String, String>> categorysynclist;
        //categorysynclist = new ArrayList<HashMap<String, String>>();
        // Create GSON object
        //Gson gson = new GsonBuilder().create();
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

                //System.out.println(arr.length());
                // If no of array elements is not zero
                // if(arr.length() != 0){
                // Loop through each array element, get JSON object which has userid and username
                for (int i = 0; i < userObj.length(); i++) {
                    // Get JSON object
                    JSONObject obj2 = (JSONObject) userObj.get(i);
                    System.out.println(obj2.get("id"));
                    System.out.println(obj2.get("tasker"));
                    // DB QueryValues Object to insert into SQLite
                    queryValues = new HashMap<String, String>();
                    // Add userID extracted from Object
                    queryValues.put("id", obj2.get("id").toString());
                    // Add userName extracted from Object
                    queryValues.put("tasker", obj2.get("tasker").toString());
                    // Insert User into SQLite DB
                    dbhelper.insertTasks(queryValues);
                    HashMap<String, String> map = new HashMap<String, String>();
                    // Add status for each User in Hashmap
                    map.put("id", obj2.get("id").toString());
                    map.put("status", "1");
                    // categorysynclist.add(map);
                    Log.e(TAG, "response:3 " + queryValues);

                }




                // Inform Remote MySQL DB about the completion of Sync activity by passing Sync status of Users
                // updateMySQLSyncSts(gson.toJson(categorysynclist));
                // Reload the Main Activity
                //reloadActivity();
            }


        } catch (JSONException e) {
            Log.e(TAG, "ERROR " + e);
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }



    // Method to Sync MySQL to SQLite DB
    private void syncSQLiteMySQLDBCategory() {

        final String Token = MyApplication.getInstance().getPrefManager().getUser().getToken();

        // Creating volley request obj
        StringRequest strReq = new StringRequest(Request.Method.GET,
                EndPoints.CATEGORY, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e(TAG, "response: " + response);
                // hidePDialog();
                // Toast.makeText(getContext(), "Response"+response, Toast.LENGTH_SHORT).show();

                // Parsing json
                UpdateSqliteWorker(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                //  Toast.makeText(getContext(), "Error"+error.getMessage(), Toast.LENGTH_SHORT).show();
                //hidePDialog();


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



    private void load_spinner_data_service() {
        //database handler
        db = new DatabaseHelper(this);

        //spinner drop down elements
        List<String> labels = db.getAllTasks();

        //Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, labels);

        //drop down layout style - list view with radio button
        dataAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //attaching data adapter to spinner
        spinner2.setAdapter(dataAdapter);

    }

    private void load_spinner_data_category() {
        //database handler
        db = new DatabaseHelper(this);


        //spinner drop down elements
        List<String> labels = db.getAllNotes();

        //Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, labels);

        //drop down layout style - list view with radio button
        dataAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //attaching data adapter to spinner
        spinner1.setAdapter(dataAdapter);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
    private boolean ValidateLocation() {
        final String Location = address.getText().toString().trim();
        if (TextUtils.isEmpty(Location)) {
            address.requestFocus();
            address.setError("Enter Location");
            //Phone is empty
            Toast.makeText(this, "please enter location", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private boolean ValidateDescription() {
        final String Description = description.getText().toString().trim();
        if (TextUtils.isEmpty(Description)) {
            description.requestFocus();
            description.setError("Enter Description");
            //Phone is empty
            Toast.makeText(this, "please enter description", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
    private void showEditDialog() {
        Bundle bundle = new Bundle();
        bundle.putString("tasks", spinner2.getSelectedItem().toString());
        bundle.putString("jobber",worker.getText().toString());
        bundle.putString("date",Date.getText().toString());
        bundle.putString("subcounty",spinner3.getSelectedItem().toString());
        bundle.putString("location",address.getText().toString());
        bundle.putString("description",description.getText().toString());
        bundle.putString("title",title.getText().toString());
        bundle.putInt("jobber_id",jobber_id);
        bundle.putString("jobber_name",jobber_name);

        FragmentManager fm = this.getSupportFragmentManager();
        ConfirmationDialogue2 confirmationDialogFragment = ConfirmationDialogue2.newInstance("Some Title");
        confirmationDialogFragment.setArguments(bundle);
        confirmationDialogFragment.show(fm, "fragment_edit_name");

    }
}

