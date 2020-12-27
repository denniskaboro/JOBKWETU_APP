package com.e.jobkwetu.Register_User;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

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
import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {
    private String TAG = LoginActivity.class.getSimpleName();
    EditText editPassword, editPhone;
    CardView btnSignIn, btnSignUp;
    TextView TxSignin, TxSignUp;
    TextView ResetPass;
    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /**
         * Check for login session. It user is already logged in
         * redirect him to main activity
         * */
        if (MyApplication.getInstance().getPrefManager().getUser() != null && FirebaseAuth.getInstance().getCurrentUser() != null) {
            startActivity(new Intent(this, HomeActivity.class));
            finish();
        }
        setContentView(R.layout.activity_login);

        editPassword = (EditText) findViewById(R.id.passwordsi);
        editPhone = (EditText) findViewById(R.id.phonesi);
        ResetPass = findViewById(R.id.resetpasssi);

        btnSignIn = (CardView) findViewById(R.id.signinsi);
        btnSignUp = (CardView) findViewById(R.id.signupsi);


        progressDialog = new ProgressDialog(this);

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!ValidatePhone()) {
                    return;
                }
                if (!ValidatePassword()) {
                    return;
                }
                Login_User();
                progressDialog.show();
            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);

            }
        });
        ResetPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, ResetPasswordActivity.class);
                startActivity(intent);

            }
        });
    }

    private void Login_User() {

        final String phone = editPhone.getText().toString();
        final String password = editPassword.getText().toString();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                EndPoints.LOGIN, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e(TAG, "response: " + response);


                try {
                    JSONObject obj = new JSONObject(response);

                    // check for error flag
                    if (obj.getBoolean("success")) {
                        progressDialog.dismiss();
                        // user successfully logged in

                        JSONObject userObj = obj.getJSONObject("data");
                        User user = new User(userObj.getString("user_id"),
                                userObj.getString("phone"),
                                userObj.getString("name"),
                                userObj.getString("token"),
                                userObj.getString("profile"),
                                userObj.getString("email"));

                        // storing user in shared preferences
                        MyApplication.getInstance().getPrefManager().storeUser(user);
                        // start main activity
                        Intent intent = new Intent(LoginActivity.this,VerifyPhoneNoActivity.class);
                        String phone2 =  MyApplication.getInstance().getPrefManager().getUser().getPhone();
                        intent.putExtra("mobile",phone);
                        // start main activity
                        startActivity(intent);
                        finish();

                    } else {
                        progressDialog.dismiss();
                        // login error - simply toast the message
                        Toast.makeText(getApplicationContext(), "" + obj.getString("message"), Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException e) {
                    progressDialog.dismiss();
                    Log.e(TAG, "json parsing error: " + e.getMessage());
                    Toast.makeText(getApplicationContext(), "Json parse error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                String json = null;
                NetworkResponse networkResponse = error.networkResponse;
                progressDialog.dismiss();
                if (networkResponse !=null && networkResponse.data !=null){
                    switch (networkResponse.statusCode){
                        case 400:
                            json = new String(networkResponse.data);
                            json = trimMessage(json,"message");
                            if (json !=null) DisplayMessage(json);
                            //Log.e(TAG, "onErrorResponse: "+json );
                            break;

                        case 404:
                            json = new String(networkResponse.data);
                            json = trimMessage(json,"message");
                            if (json !=null) DisplayMessage(json);
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
                Toast.makeText(LoginActivity.this, "Error"  + json, Toast.LENGTH_SHORT).show();
            }

            private String trimMessage(String json, String key) {
                String trimmedString = null;
                try {
                    JSONObject obj = new JSONObject(json);
                    trimmedString = obj.getString(key);
                    //Log.e(TAG, "trimMessage: "+trimmedString );
                } catch (JSONException e){
                    e.printStackTrace();
                    return null;
                }
                return trimmedString;
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("phone", phone);
                params.put("password", password);

                Log.e(TAG, "params: " + params.toString());
                return params;
            }
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params2 = new HashMap<>();
                params2.put("Authorization", "Bearer .$accessToken");
                params2.put("Accept", "application/json");

                Log.e(TAG, "params2: " + params2.toString());
                return params2;
            }

        };


        //Adding request to request queue
        MyApplication.getInstance().addToRequestQueue(strReq);
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    //Validate Password
    private Boolean ValidatePassword() {
        final String Password = editPassword.getText().toString().trim();
        if (TextUtils.isEmpty(Password)) {
            //Password is empty
            Toast.makeText(LoginActivity.this, "please enter Password", Toast.LENGTH_SHORT).show();
            // stops fuction from executing further
            return false;
        }
        if (Password.length() < 6) {
            editPassword.setError("Minimum length of password should be 6");
            editPassword.requestFocus();
            return false;
        }
        return true;
    }
    //Validate Phone
    private Boolean ValidatePhone() {
        final String Phone = editPhone.getText().toString().trim();
        if (TextUtils.isEmpty(Phone)) {
            //Phone is empty
            Toast.makeText(LoginActivity.this, "please enter Phone Number", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (Phone.length() > 12) {
            editPhone.setError("Maximum length of Phone Number should be 12");
            editPhone.requestFocus();
            return false;
        }
        if (Phone.length() < 12) {
            editPhone.setError("Minimum length of Phone Number should be 12");
            editPhone.requestFocus();
            return false;
        }
        return true;
    }
}

