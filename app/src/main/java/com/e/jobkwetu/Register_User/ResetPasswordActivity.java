package com.e.jobkwetu.Register_User;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.e.jobkwetu.Activity.HomeActivity;
import com.e.jobkwetu.App.EndPoints;
import com.e.jobkwetu.App.MyApplication;
import com.e.jobkwetu.Model.User;
import com.e.jobkwetu.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ResetPasswordActivity extends AppCompatActivity {
    private String TAG = ResetPasswordActivity.class.getSimpleName();
    EditText email;
    CardView Resetpass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        email = findViewById(R.id.emailRe);
        Resetpass = findViewById(R.id.resetpass);


        Resetpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String email1 = email.getText().toString();

                //final String email = editEmail.getText().toString().trim();
                if (TextUtils.isEmpty(email1)) {
                    email.setError("Email is empty");
                    email.requestFocus();
                    //Email is empty
                    Toast.makeText(ResetPasswordActivity.this, "please enter Email", Toast.LENGTH_SHORT).show();
                    // stops fuction from executing further
                    return;
                }
                if (!Patterns.EMAIL_ADDRESS.matcher(email1).matches()) {
                    email.setError("Please enter a valid Email.");
                    email.requestFocus();
                    return;
                }


                StringRequest strReq = new StringRequest(Request.Method.POST,
                        EndPoints.RESET_PASSWORD, MyRequestSuccessListener(),
                        MyRequestErrorListener()) {
                    //new Response.Listener<String>()


                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        Map<String, String> params2 = new HashMap<>();
                        params2.put("X-Requested-With", "XMLHttpRequest");

                        return params2;
                    }

                    @Override
                    public byte[] getBody() throws com.android.volley.AuthFailureError {
                        String str = "{\"email\":\"" + email1 + "\"}";
                        Log.e(TAG, "getBody: " + str);
                        return str.getBytes();
                    }

                    public String getBodyContentType() {
                        return "application/json";
                    }
                };
                //Adding request to request queue
                MyApplication.getInstance().addToRequestQueue(strReq);
            }


            private Response.Listener<String> MyRequestSuccessListener() {
                return new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e(TAG, "response: " + response);

                        try {
                            JSONObject obj = new JSONObject(response);

                            // check for error flag
                            if (true == obj.getBoolean("success")) {


                                // start main activity
                                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                                finish();

                            } else {
                                // login error - simply toast the message
                                Toast.makeText(getApplicationContext(), "" + obj.getString("message"), Toast.LENGTH_LONG).show();
                            }

                        } catch (JSONException e) {
                            Log.e(TAG, "json parsing error: " + e.getMessage());
                            Toast.makeText(getApplicationContext(), "Json parse error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                };
            }

            private Response.ErrorListener MyRequestErrorListener() {
                return new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        String json = null;
                        NetworkResponse networkResponse = error.networkResponse;
                        if (networkResponse != null && networkResponse.data != null) {
                            switch (networkResponse.statusCode) {
                                case 400:
                                    json = new String(networkResponse.data);
                                    json = trimMessage(json, "message");
                                    if (json != null) DisplayMessage(json);
                                    //Log.e(TAG, "onErrorResponse: "+json );
                                    break;

                                case 404:
                                    json = new String(networkResponse.data);
                                    json = trimMessage(json, "message");
                                    if (json != null) DisplayMessage(json);
                                    //Log.e(TAG, "onErrorResponse: "+json );
                                    break;
                                //Add cases here
                            }

                        }
                        Log.e(TAG, "Volley error: " + error + ", code: " + networkResponse);
                        //Toast.makeText(getApplicationContext(), "Volley error: " + error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }
                    //Somewhere that hass access to a context

                    private void DisplayMessage(String json) {
                        Toast.makeText(ResetPasswordActivity.this, "Error" + json, Toast.LENGTH_SHORT).show();
                    }

                    private String trimMessage(String json, String key) {
                        String trimmedString = null;
                        try {
                            JSONObject obj = new JSONObject(json);
                            trimmedString = obj.getString(key);
                            //Log.e(TAG, "trimMessage: "+trimmedString );
                        } catch (JSONException e) {
                            e.printStackTrace();
                            return null;
                        }
                        return trimmedString;
                    }
                };

                    /*@Override
                    protected Map<String, String> getParams() {
                        Map<String, String> params = new HashMap<>();
                        params.put("email", email1);

                        Log.e(TAG, "params: " + params.toString());
                        return params;
                    }
                    @Override
                    public Map<String, String> getHeaders() {
                        Map<String, String> params2 = new HashMap<>();
                        params2.put("Content-Type", "application/json");
                        params2.put("X-Requested-With", "XMLHttpRequest");

                        Log.e(TAG, "params2: " + params2.toString());
                        return params2;
                    }

                };

                     */



            }
        });
    }
}

