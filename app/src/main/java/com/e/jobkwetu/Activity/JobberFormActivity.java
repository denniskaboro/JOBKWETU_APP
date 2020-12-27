package com.e.jobkwetu.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.e.jobkwetu.Adapters.CategoryAdapter;
import com.e.jobkwetu.App.EndPoints;
import com.e.jobkwetu.App.MyApplication;
import com.e.jobkwetu.Dialogue.ConfirmationDialogue;
import com.e.jobkwetu.Helper.DatabaseHelper;
import com.e.jobkwetu.Model.SelectedCategory;
import com.e.jobkwetu.R;
import com.e.jobkwetu.Utilities.MyDividerItemDecoration;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JobberFormActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private static final String TAG = JobberFormActivity.class.getSimpleName();
    DatabaseHelper dbhelper;
    HashMap<String, String> queryValuesS;
    HashMap<String, String> queryValuesC;
    HashMap<String, String> queryValuesT;
    private DatabaseHelper db;
    private Spinner spinner1,spinner2,spinner3,spinner4,spinner5,spinner6,spinner7;
    private EditText locationEt,descriptionEt;
    private FloatingActionButton ADDBTN;
    private RecyclerView tasks;
    private CardView submitbtn;
    private CategoryAdapter mAdapter;
    private List<SelectedCategory> notesList = new ArrayList<>();
    private List<String> notesList2 = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.jobber_form_frag);

        dbhelper = new DatabaseHelper(this);
        spinner1 = (Spinner) findViewById(R.id.jobber_spinner);
        spinner2 = (Spinner) findViewById(R.id.task_spinner);
        spinner3 = (Spinner) findViewById(R.id.spn_tme1);
        spinner4 = (Spinner) findViewById(R.id.spn_tme2);
        spinner5 = (Spinner) findViewById(R.id.spn_day1);
        spinner6 = (Spinner) findViewById(R.id.spn_day2);
        spinner7 = (Spinner) findViewById(R.id.sub_county_spinner);
        tasks = (RecyclerView) findViewById(R.id.tasks_list);

        ADDBTN =(FloatingActionButton) findViewById(R.id.floatbtn);
        submitbtn=(CardView) findViewById(R.id.submit);
        locationEt=(EditText) findViewById(R.id.location_et);
        descriptionEt=(EditText) findViewById(R.id.description_et);

        db = new DatabaseHelper(this);
        notesList.addAll(db.getAllSELECTEDCATEGORY());

        notesList2.addAll(db.getAllSelected());

        //Toast.makeText(getContext(), "Array" +labels, Toast.LENGTH_SHORT).show();

        //RecyclerView add data
        mAdapter = new CategoryAdapter(this, notesList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this.getApplicationContext());
        tasks.setLayoutManager(mLayoutManager);
        tasks.setItemAnimator(new DefaultItemAnimator());
        tasks.addItemDecoration(new MyDividerItemDecoration(this, LinearLayoutManager.VERTICAL, 16));
        tasks.setAdapter(mAdapter);



        //Available time spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.hours, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner3.setAdapter(adapter);
        spinner4.setAdapter(adapter);
        spinner3.setOnItemSelectedListener(this);
        spinner4.setOnItemSelectedListener(this);

        //Available days spinner
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this, R.array.days, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner5.setAdapter(adapter2);
        spinner6.setAdapter(adapter2);
        spinner5.setOnItemSelectedListener(this);
        spinner6.setOnItemSelectedListener(this);


        //category spinner
        spinner1.setOnItemSelectedListener(this);
        //task spinner
        spinner2.setOnItemSelectedListener(this);
        //Sub county spinner
        spinner7.setOnItemSelectedListener(this);

        //loading spinner data from database
        load_spinner_data_category();
        load_spinner_data_service();
        //loading spinner data from database
        load_spinner_data_subcounty();


        //calling method sync sqlite
        syncSQLiteMySQLDBCategory();
        syncSQLiteMySQLDBService();
        syncSQLiteMySQLDBSubcounty();

        ADDBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String note = spinner2.getSelectedItem().toString();
                createNote(note);
            }
        });

        submitbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(getContext(), "Array" +notesList, Toast.LENGTH_SHORT).show();
                if(!ValidateLocation()) {
                    return;
                }
                if(!ValidateDescription()) {
                    return;
                }
                if(!ValidateTasks()) {
                    return;
                }
                if(!ValidateSubcounty()) {
                    return;
                }
                if(!Validateday()) {
                    return;
                }
                if(!ValidateTime()) {
                    return;
                }
                showEditDialog();

            }
        });

    }

    private boolean ValidateTasks() {
        final String Location = String.valueOf(notesList2);
        if (Location ==null) {
            //spinner3.requestFocus();
            Toast.makeText(this, "please select tasks", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private boolean ValidateSubcounty() {
        final String Location = spinner7.getSelectedItem().toString().trim();
        if (TextUtils.isEmpty(Location)) {
            spinner7.requestFocus();
            Toast.makeText(this, "please select subcounty", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private boolean Validateday() {
        final String Location = spinner5.getSelectedItem().toString().trim();
        final String Location1 = spinner6.getSelectedItem().toString().trim();
        if (TextUtils.isEmpty(Location)) {
            spinner5.requestFocus();
            Toast.makeText(this, "please select Days available", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (TextUtils.isEmpty(Location1)) {
            spinner6.requestFocus();
            Toast.makeText(this, "please select Days available", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private boolean ValidateTime() {
        final String Location = spinner3.getSelectedItem().toString().trim();
        final String Location1 = spinner4.getSelectedItem().toString().trim();
        if (TextUtils.isEmpty(Location)) {
            spinner3.requestFocus();
            Toast.makeText(this, "please select Time available", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (TextUtils.isEmpty(Location1)) {
            spinner4.requestFocus();
            Toast.makeText(this, "please select Time available", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void createNote(String note) {
        // inserting note in db and getting
        // newly inserted note id
        long id = db.insertSelectedCategory(note);

        // get the newly inserted note from db
        SelectedCategory n = db.getSelectedCategory(id);

        if (n != null) {
            // adding new note to array list at 0 position
            notesList.add(0,n);

            // refreshing the list
            mAdapter.notifyDataSetChanged();
        }
    }

    private boolean ValidateLocation() {
        final String Location = locationEt.getText().toString().trim();
        if (TextUtils.isEmpty(Location)) {
            locationEt.requestFocus();
            locationEt.setError("Enter Location");
            //Phone is empty
            Toast.makeText(this, "please enter location", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private boolean ValidateDescription() {
        final String Description = descriptionEt.getText().toString().trim();
        if (TextUtils.isEmpty(Description)) {
            descriptionEt.requestFocus();
            descriptionEt.setError("Enter Description");
            //Phone is empty
            Toast.makeText(this, "please enter description", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
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
                    queryValuesT = new HashMap<String, String>();
                    // Add userID extracted from Object
                    queryValuesT.put("id", obj2.get("id").toString());
                    // Add userName extracted from Object
                    queryValuesT.put("category", obj2.get("category").toString());
                    // Insert User into SQLite DB
                    dbhelper.insertCategory(queryValuesT);
                    HashMap<String, String> map = new HashMap<String, String>();
                    // Add status for each User in Hashmap
                    map.put("id", obj2.get("id").toString());
                    map.put("status", "1");
                    // categorysynclist.add(map);
                    Log.e(TAG, "response:3 " + queryValuesT);

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
                    queryValuesS = new HashMap<String, String>();
                    // Add userID extracted from Object
                    queryValuesS.put("id", obj2.get("id").toString());
                    // Add userName extracted from Object
                    queryValuesS.put("subcounty", obj2.get("subcounty").toString());
                    // Insert User into SQLite DB
                    dbhelper.insertSubcounty(queryValuesS);
                    HashMap<String, String> map = new HashMap<String, String>();
                    // Add status for each User in Hashmap
                    map.put("id", obj2.get("id").toString());
                    map.put("status", "1");
                    // categorysynclist.add(map);
                    Log.e(TAG, "response:3 " + queryValuesS);

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
                    queryValuesC = new HashMap<String, String>();
                    // Add userID extracted from Object
                    queryValuesC.put("id", obj2.get("id").toString());
                    // Add userName extracted from Object
                    queryValuesC.put("tasker", obj2.get("tasker").toString());
                    // Insert User into SQLite DB
                    dbhelper.insertTasks(queryValuesC);
                    HashMap<String, String> map = new HashMap<String, String>();
                    // Add status for each User in Hashmap
                    map.put("id", obj2.get("id").toString());
                    map.put("status", "1");
                    // categorysynclist.add(map);
                    Log.e(TAG, "response:3 " + queryValuesC);

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
                Toast.makeText(JobberFormActivity.this, "Response"+response, Toast.LENGTH_SHORT).show();

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
                Toast.makeText(JobberFormActivity.this, "Response"+response, Toast.LENGTH_SHORT).show();

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
    // Method to Sync MySQL to SQLite DB
    private void syncSQLiteMySQLDBSubcounty() {
        final String Token = MyApplication.getInstance().getPrefManager().getUser().getToken();

        // Creating volley request obj
        StringRequest strReq = new StringRequest(Request.Method.GET,
                EndPoints.SUBCOUNTY, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e(TAG, "response: " + response);
                // hidePDialog();
                Toast.makeText(JobberFormActivity.this, "Response"+response, Toast.LENGTH_SHORT).show();

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
        spinner7.setAdapter(dataAdapter);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
    //Show confirmation dialog
    private void showEditDialog() {
        Bundle bundle = new Bundle();
        bundle.putString("tasks", String.valueOf(notesList2));
        bundle.putString("time1",spinner3.getSelectedItem().toString());
        bundle.putString("time2",spinner4.getSelectedItem().toString());
        bundle.putString("day1",spinner5.getSelectedItem().toString());
        bundle.putString("day2",spinner6.getSelectedItem().toString());
        bundle.putString("subcounty",spinner7.getSelectedItem().toString());
        bundle.putString("location",locationEt.getText().toString());
        bundle.putString("description",descriptionEt.getText().toString());

        FragmentManager fm = getSupportFragmentManager();
        ConfirmationDialogue confirmationDialogFragment = ConfirmationDialogue.newInstance("Some Title");
        confirmationDialogFragment.setArguments(bundle);
        confirmationDialogFragment.show(fm, "fragment_edit_name");

    }
}

