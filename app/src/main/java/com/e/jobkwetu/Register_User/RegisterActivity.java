package com.e.jobkwetu.Register_User;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
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
import com.e.jobkwetu.App.EndPoints;
import com.e.jobkwetu.App.MyApplication;
import com.e.jobkwetu.Model.Register;
import com.e.jobkwetu.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.PendingDynamicLinkData;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {
    private String TAG = RegisterActivity.class.getSimpleName();
    EditText editEmail, editName, editPassword, editPhone,edit_c_password;
    CardView btnSignIn, btnSignUp;
    TextView ResetPass;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        editEmail = (EditText) findViewById(R.id.emailsu);
        editName = (EditText) findViewById(R.id.usernamesu);
        editPassword = (EditText) findViewById(R.id.passwordsu);
        edit_c_password = (EditText) findViewById(R.id.c_passwordsu);
        editPhone = (EditText) findViewById(R.id.phonesu);
        ResetPass = findViewById(R.id.resetpasssu);

        btnSignIn = (CardView) findViewById(R.id.signinsu);
        btnSignUp = (CardView) findViewById(R.id.signupsu);

        progressDialog = new ProgressDialog(this);

        RetriveReferral();


        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);

            }
        });
        ResetPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegisterActivity.this, ResetPasswordActivity.class);
                startActivity(intent);

            }
        });


        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!ValidateUsername()) {
                    return;
                }
                if (!ValidateEmail()) {
                    return;
                }

                if (!ValidatePhone()) {
                    return;
                }
                if (!ValidatePassword()) {
                    return;
                }
                if (!Validatec_Password()) {
                    return;
                }
                progressDialog.show();



                final String phone = editPhone.getText().toString();
                final String password = editPassword.getText().toString();
                final String c_password = edit_c_password.getText().toString();
                final String email = editEmail.getText().toString();
                final String username = editName.getText().toString();


                StringRequest strReq = new StringRequest(Request.Method.POST,
                        EndPoints.REGISTER, new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.e(TAG, "response: " + response);

                        try {
                            JSONObject obj = new JSONObject(response);

                            // check for error flag
                            if(true == obj.getBoolean("success")) {
                                progressDialog.dismiss();
                                // user successfully logged in

                                JSONObject userObj = obj.getJSONObject("data");
                                Register user = new Register(userObj.getString("user_id"),
                                        userObj.getString("phone"),
                                        userObj.getString("token"),
                                        userObj.getString("email"),
                                        userObj.getString("name"));



                                //Check on response params  TODO
                                //Deactivate error from mysql and php file and replace with rewritten errors TODO

                                // storing user in shared preference
                                Intent intent = new Intent(RegisterActivity.this,VerifyPhoneNoActivity.class);
                                MyApplication.getInstance().getPrefManager().storeUser2(user);
                               String phone2 = MyApplication.getInstance().getPrefManager().getUser2().getPhone();
                                intent.putExtra("mobile",phone);
                                // start main activity
                                startActivity(intent);
                                finish();

                            } else {
                                // login error - simply toast the message
                                progressDialog.dismiss();
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
                        progressDialog.dismiss();
                        String json = null;
                        NetworkResponse networkResponse = error.networkResponse;
                        Toast.makeText(RegisterActivity.this, "Connection error try again latter", Toast.LENGTH_SHORT).show();
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
                                case 500:
                                    json = new String(networkResponse.data);
                                    json = trimMessage(json,"message");
                                    if (json !=null) DisplayMessage(json);
                                    //Log.e(TAG, "onErrorResponse: "+json );
                                    break;
                                //Add cases here
                            }

                        }
                        //Log.e(TAG, "Volley error: " + error + ", code: " + networkResponse);
                        //Toast.makeText(getApplicationContext(), "Volley error: " + error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }
                    //Somewhere that hass access to a context

                    private void DisplayMessage(String json) {
                        Toast.makeText(RegisterActivity.this, "Error"  + json, Toast.LENGTH_SHORT).show();
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
                        params.put("email", email);
                        params.put("name", username);
                        params.put("password", password);
                        params.put("c_password", c_password);
                        Log.e(TAG, "params: " + params.toString());
                        return params;
                    }
                };

                //Adding request to request queue
                MyApplication.getInstance().addToRequestQueue(strReq);

            }
        });
    }

    private void RetriveReferral() {
        FirebaseDynamicLinks.getInstance()
                .getDynamicLink(getIntent())
                .addOnSuccessListener(this, new OnSuccessListener<PendingDynamicLinkData>() {
                    @Override
                    public void onSuccess(PendingDynamicLinkData pendingDynamicLinkData) {
                        // Get deep link from result (may be null if no link is found)
                        Uri deepLink = null;
                        if (pendingDynamicLinkData != null) {
                            deepLink = pendingDynamicLinkData.getLink();
                        }
                        //
                        // If the user isn't signed in and the pending Dynamic Link is
                        // an invitation, sign in the user anonymously, and record the
                        // referrer's UID.
                        //
                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        if (user == null
                                && deepLink != null
                                && deepLink.getBooleanQueryParameter("invitedby", false)) {
                            String referrerUid = deepLink.getQueryParameter("invitedby");
                            createAnonymousAccountWithReferrerInfo(referrerUid);
                        }
                    }
                });
    }

    private void createAnonymousAccountWithReferrerInfo(final String referrerUid) {
        FirebaseAuth.getInstance()
                .signInAnonymously()
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        // Keep track of the referrer in the RTDB. Database calls
                        // will depend on the structure of your app's RTDB.
                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        DatabaseReference userRecord =
                                FirebaseDatabase.getInstance().getReference()
                                        .child("users")
                                        .child(user.getUid());
                        userRecord.child("referred_by").setValue(referrerUid);
                    }
                });

    }

    private boolean Validatec_Password() {
        final String c_Password = edit_c_password.getText().toString().trim();
        final String password = editPassword.getText().toString().trim();
        if (TextUtils.isEmpty(c_Password)) {
            edit_c_password.setError("Conf password is empty!!");
            //Password is empty
            Toast.makeText(RegisterActivity.this, "please enter Confirmation Password", Toast.LENGTH_SHORT).show();
            // stops fuction from executing further
            return false;
        }
        if (c_Password.length() < 6) {
            edit_c_password.setError("Minimum length of password should be 6");
            edit_c_password.requestFocus();
            return false;
        }
        if (!c_Password.equals(password)) {
            edit_c_password.setError("Conf pass does not match");
            edit_c_password.requestFocus();
            return false;
        }
        return true;
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    private boolean ValidateUsername() {
        final String username = editName.getText().toString().trim();
        if (TextUtils.isEmpty(username)) {
            editName.setError("Username is empty!!");
            editName.requestFocus();
            //Username is empty
            Toast.makeText(RegisterActivity.this, "please enter Username", Toast.LENGTH_SHORT).show();
            // stops fuction from executing further
            return false;
        }
        if (username.length() < 5) {
            editName.setError("Minimum length of Username should be 5");
            editName.requestFocus();
            return false;
        }
        return true;

    }

    private boolean ValidateEmail() {
        final String email = editEmail.getText().toString().trim();
        if (TextUtils.isEmpty(email)) {
            editEmail.setError("Email is empty");
            editEmail.requestFocus();
            //Email is empty
            Toast.makeText(RegisterActivity.this, "please enter Email", Toast.LENGTH_SHORT).show();
            // stops fuction from executing further
            return false;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editEmail.setError("Please enter a valid Email.");
            editEmail.requestFocus();
            return false;
        }
        return true;

    }

    private boolean ValidatePhone() {
        final String Phone = editPhone.getText().toString().trim();
        if (TextUtils.isEmpty(Phone)) {
            editPhone.setError("Phone No is empty!!");
            editPhone.requestFocus();
            //Phone is empty
            Toast.makeText(RegisterActivity.this, "please enter Phone Number", Toast.LENGTH_SHORT).show();
            // stops fuction from executing further
            return false;
        }
        if (Phone.length() < 12) {
            editPhone.setError("Minimum length of Phone Number should be 12");
            editPhone.requestFocus();
            return false;
        }
        return true;
    }

    //Validate Password
    private Boolean ValidatePassword() {
        final String Password = editPassword.getText().toString().trim();
        if (TextUtils.isEmpty(Password)) {
            editPassword.setError("Password is empty!!");
            editPassword.requestFocus();
            //Password is empty
            Toast.makeText(RegisterActivity.this, "please enter Password", Toast.LENGTH_SHORT).show();
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
    //progressDialog.setMessage("Registering User...");
    //progressDialog.show();
}
