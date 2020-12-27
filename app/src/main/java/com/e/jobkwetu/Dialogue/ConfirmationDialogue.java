package com.e.jobkwetu.Dialogue;

import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.app.DialogCompat;
import androidx.fragment.app.DialogFragment;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.e.jobkwetu.App.EndPoints;
import com.e.jobkwetu.App.MyApplication;
import com.e.jobkwetu.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static com.e.jobkwetu.App.MyApplication.TAG;

public class ConfirmationDialogue extends DialogFragment {

    private CardView cancelbtn,acceptbtn;
    private TextView tasks,time,day,subcounty,location,description;
    Button mcancel,maccept;
    AlertDialog alertDialog;


    public ConfirmationDialogue(){
    }

    public static ConfirmationDialogue newInstance(String title) {
        ConfirmationDialogue frag = new ConfirmationDialogue();
        Bundle args = new Bundle();
        args.putString("title", title);
        frag.setArguments(args);
        return frag;
    }

    @Override

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.confirmation_dialogue, container);




    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tasks= view.findViewById(R.id.dialoguetask);
        time= view.findViewById(R.id.dialoguetime);
        day= view.findViewById(R.id.dialogueday);
        subcounty= view.findViewById(R.id.dialoguesubcounty);
        location= view.findViewById(R.id.dialoguelocation);
        description= view.findViewById(R.id.dialoguedescription);
        mcancel = view.findViewById(R.id.cancel);
        maccept = view.findViewById(R.id.accept);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        Bundle mArgs = getArguments();
        final String tasks1 = mArgs.getString("tasks");
        final String time1= mArgs.getString("time1");
        final String time2= mArgs.getString("time2");
        final String day1= mArgs.getString("day1");
        final String day2= mArgs.getString("day2");
        final String location1 = mArgs.getString("location");
        final String subcounty1= mArgs.getString("subcounty");
        final String description1= mArgs.getString("description");

        tasks.setText(tasks1);
        time.setText("From " + time1 + "\nto " + time2);
        day.setText("From " + day1 + "\nto " + day2);
        subcounty.setText(subcounty1);
        location.setText(location1);
        description.setText(description1);

        //confirm.setText("Am from\n" + id +"Doing\n\n"+category);

        mcancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDialog().dismiss();
            }
        });
        maccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String username = MyApplication.getInstance().getPrefManager().getUser().getName();
                final String phone1 =MyApplication.getInstance().getPrefManager().getUser().getPhone();
                final String Token = MyApplication.getInstance().getPrefManager().getUser().getToken();
                final String userid = MyApplication.getInstance().getPrefManager().getUser().getId();
                StringRequest strReq = new StringRequest(Request.Method.POST,
                        EndPoints.JOBBER, new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.e(TAG, "response: " + response);

                        try {
                            JSONObject obj = new JSONObject(response);

                            // check for error flag
                            if(true == obj.getBoolean("success")) {
                                // user successfully logged in

                                JSONObject userObj = obj.getJSONObject("data");

                                showAlartDialogue(R.layout.successful_dialogue);


                            } else {
                                // login error - simply toast the message
                                showAlartDialogue2(R.layout.failed_dialogue);
                                Toast.makeText(getContext(), "" + obj.getString("message"), Toast.LENGTH_LONG).show();
                            }

                        } catch (JSONException e) {
                            showAlartDialogue2(R.layout.failed_dialogue);
                            Log.e(TAG, "json parsing error: " + e.getMessage());
                            Toast.makeText(getContext(), "Json parse error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        String json = null;
                        NetworkResponse networkResponse = error.networkResponse;
                        showAlartDialogue2(R.layout.failed_dialogue);
                        Log.e(TAG, "Volley error: " + error + ", code: " + networkResponse);
                        //Toast.makeText(getApplicationContext(), "Volley error: " + error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }

                }) {

                    @Override
                    protected Map<String, String > getParams() {
                        Map<String, String> params = new HashMap<>();
                        params.put("user_id", userid);
                        params.put("user_phone", phone1);
                        params.put("tasks", tasks1);
                        params.put("time_start", time1);
                        params.put("time_stop", time2);
                        params.put("day_start", day1);
                        params.put("day_stop", day2);
                        params.put("subcounty", subcounty1);
                        params.put("address", location1);
                        params.put("description", description1);
                        Log.e(TAG, "params: " + params.toString());
                        return params;
                    }
                    @Override
                    public Map<String, String> getHeaders() {
                        Map<String, String> params2 = new HashMap<>();
                        params2.put("Authorization", "Bearer "+Token);
                        params2.put("Accept", "application/json");

                        Log.e(TAG, "params2: " + params2.toString());
                        return params2;
                    }
                };

                //Adding request to request queue
                MyApplication.getInstance().addToRequestQueue(strReq);


            }
        });


    }
    private void showAlartDialogue2(int failed_dialogue) {
        AlertDialog.Builder dialogueBuilder = new AlertDialog.Builder(getContext());
        View layoutview = getLayoutInflater().inflate(failed_dialogue,null);
        Button dialogButton = layoutview.findViewById(R.id.btndialog_failed);
        dialogueBuilder.setView(layoutview);
        alertDialog = dialogueBuilder.create();
        alertDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.show();
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });
    }

    private void showAlartDialogue(int successful_dialogue) {
        AlertDialog.Builder dialogueBuilder = new AlertDialog.Builder(getContext());
        View layoutview = getLayoutInflater().inflate(successful_dialogue,null);
        Button dialogButton = layoutview.findViewById(R.id.btndialog_success);
        dialogueBuilder.setView(layoutview);
        alertDialog = dialogueBuilder.create();
        alertDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.show();
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });
    }
}
