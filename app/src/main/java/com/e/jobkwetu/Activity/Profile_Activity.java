package com.e.jobkwetu.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.e.jobkwetu.App.EndPoints;
import com.e.jobkwetu.App.MyApplication;
import com.e.jobkwetu.R;
import com.e.jobkwetu.Register_User.RegisterActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class Profile_Activity extends AppCompatActivity {
    private String TAG = RegisterActivity.class.getSimpleName();
    private static final int CHOOSE_IMAGE = 95;
    private EditText username;
    private TextView phone2;
    private TextView email;
    private FirebaseAuth mAuth;
    //private DatabaseReference RootRef;
    private Uri uriprofilename;
    private String myurl = "";
    private String checker = "";
    private SharedPreferences sp;
    private CircleImageView imageView;
    private TextView update;
    private TextView cancel;
    private String displayName;
    Context context;
    ProgressDialog progressDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.profile_frag);

        username = findViewById(R.id.profile_username);
        phone2 = findViewById(R.id.profile_Phone1);
        email = findViewById(R.id.profile_email);
        imageView = findViewById(R.id.img_profile);
        update = findViewById(R.id.sett_update);
        cancel = findViewById(R.id.sett_close);
        mAuth = FirebaseAuth.getInstance();
        //RootRef = FirebaseDatabase.getInstance().getReference().child("Members");

        progressDialog = new ProgressDialog(Profile_Activity.this);
        progressDialog.setTitle("Update Profile");
        progressDialog.setMessage("Please wait ,while we are updating your account information");
        progressDialog.setCanceledOnTouchOutside(false);


        final String username1 = MyApplication.getInstance().getPrefManager().getUser().getName();
        final String Phone1 = MyApplication.getInstance().getPrefManager().getUser().getPhone();
        final String email1 = MyApplication.getInstance().getPrefManager().getUser().getEmail();

        username.setText(username1);
        phone2.setText(Phone1);
        email.setText(email1);

        phone2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(Profile_Activity.this, "Phone number cannot be edited", Toast.LENGTH_SHORT).show();
            }
        });
        email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(Profile_Activity.this, "Email cannot be edited", Toast.LENGTH_SHORT).show();
            }
        });


        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checker = "clicked";
                CropImage.activity(uriprofilename)
                        .setAspectRatio(1, 1)
                        .start(Profile_Activity.this);
                //showImageChooser();
            }
        });
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checker.equals("clicked")) {
                    progressDialog.show();
                    saveUsersInformation();
                    upLoadImageToDatabaseStorage();
                } else {
                    progressDialog.show();
                    //Toast.makeText(context, "Updated 1", Toast.LENGTH_SHORT).show();
                    saveUsersInformation();

                }


            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.hide();
                Intent intent = new Intent(Profile_Activity.this, HomeActivity.class);
                startActivity(intent);

            }
        });

    }

    private void upLoadImageToDatabaseStorage() {
       /* progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Update Profile");
        progressDialog.setMessage("Please wait ,while we are updating your account information");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        */
        upLoadImageToFirebaseStorage();

    }

    private void saveUsersInformation() {
        final String username5 = username.getText().toString();
        final String Token = MyApplication.getInstance().getPrefManager().getUser().getToken();
        final String userid = MyApplication.getInstance().getPrefManager().getUser().getId();
        StringRequest strReq = new StringRequest(Request.Method.POST,
                EndPoints.NAME, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e(TAG, "response: " + response);

                try {
                    JSONObject obj = new JSONObject(response);

                    // check for error flag
                    if(true == obj.getBoolean("success")) {
                        // user successfully logged in
                        progressDialog.dismiss();

                        JSONObject userObj = obj.getJSONObject("data");

                        //showAlartDialogue(R.layout.successful_dialogue);


                    } else {
                        // login error - simply toast the message
                        //showAlartDialogue2(R.layout.failed_dialogue);
                        progressDialog.dismiss();
                        Toast.makeText(Profile_Activity.this, "" + obj.getString("message"), Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException e) {
                   // showAlartDialogue2(R.layout.failed_dialogue);
                    progressDialog.dismiss();
                    Log.e(TAG, "json parsing error: " + e.getMessage());
                    Toast.makeText(Profile_Activity.this, "Json parse error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                String json = null;
                NetworkResponse networkResponse = error.networkResponse;
                //showAlartDialogue2(R.layout.failed_dialogue);
                progressDialog.dismiss();
                Log.e(TAG, "Volley error: " + error + ", code: " + networkResponse);
                Toast.makeText(getApplicationContext(), "Volley error: " + error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }

        }) {

            @Override
            protected Map<String, String > getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("name", username5);
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


        //Toast.makeText(this, "Yet to be updated", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            uriprofilename = result.getUri();
            imageView.setImageURI(uriprofilename);
        } else {
            Toast.makeText(this, "Error Try Again", Toast.LENGTH_SHORT).show();
            //startActivity(new Intent(Settings_Activity.this, Settings_Activity.class));
            //finish();
        }

    }

    private void upLoadImageToFirebaseStorage() {
        final String Token = MyApplication.getInstance().getPrefManager().getUser().getToken();
        final String userid = MyApplication.getInstance().getPrefManager().getUser().getId();

        final StorageReference profileImageRef = FirebaseStorage.getInstance().getReference("profile/" + System.currentTimeMillis() + ".jpg");
        if (uriprofilename != null) {
            //progressBar.setVisibility(View.VISIBLE);
            profileImageRef.putFile(uriprofilename)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            //progressDialog.dismiss();
                            //progressBar.setVisibility(View.GONE);
                            profileImageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    final String downloadUrl = uri.toString();
                                    progressDialog.dismiss();
                                    Toast.makeText(Profile_Activity.this, "Profile image stored to firebase database successfully.", Toast.LENGTH_SHORT).show();

                                    StringRequest strReq = new StringRequest(Request.Method.POST,
                                            EndPoints.PROFILE, new Response.Listener<String>() {

                                        @Override
                                        public void onResponse(String response) {
                                            Log.e(TAG, "response: " + response);

                                            try {
                                                JSONObject obj = new JSONObject(response);

                                                // check for error flag
                                                if (true == obj.getBoolean("success")) {
                                                    progressDialog.dismiss();
                                                    // user successfully logged in

                                                    JSONObject userObj = obj.getJSONObject("data");
                                                  /*  Register user = new Register(userObj.getString("user_id"),
                                                            userObj.getString("phone"),
                                                            userObj.getString("token"),
                                                            userObj.getString("email"),
                                                            userObj.getString("name"));


                                                    //Check on response params  TODO
                                                    //Deactivate error from mysql and php file and replace with rewritten errors TODO

                                                    // storing user in shared preference
                                                    Intent intent = new Intent(RegisterActivity.this, VerifyPhoneNoActivity.class);
                                                    MyApplication.getInstance().getPrefManager().storeUser2(user);
                                                    String phone2 = MyApplication.getInstance().getPrefManager().getUser2().getPhone();
                                                    intent.putExtra("mobile", phone);
                                                    // start main activity
                                                    startActivity(intent);
                                                    finish();

                                                   */

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
                                            Toast.makeText(Profile_Activity.this, "Connection error try again latter", Toast.LENGTH_SHORT).show();
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
                                                    case 500:
                                                        json = new String(networkResponse.data);
                                                        json = trimMessage(json, "message");
                                                        if (json != null) DisplayMessage(json);
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
                                            Toast.makeText(Profile_Activity.this, "Error" + json, Toast.LENGTH_SHORT).show();
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
                                    }) {
                                        @Override
                                        public Map<String, String> getHeaders() {
                                            Map<String, String> params2 = new HashMap<>();
                                            params2.put("Authorization", "Bearer "+Token);
                                            params2.put("Accept", "application/json");

                                            Log.e(TAG, "params2: " + params2.toString());
                                            return params2;
                                        }

                                        @Override
                                        protected Map<String, String> getParams() {
                                            Map<String, String> params = new HashMap<>();
                                            params.put("profile", downloadUrl);
                                            Log.e(TAG, "params: " + params.toString());
                                            return params;
                                        }
                                    };

                                    //Adding request to request queue
                                    MyApplication.getInstance().addToRequestQueue(strReq);

                                }
                            });

                        }
                    })

                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            //progressBar.setVisibility(View.GONE);
                            progressDialog.dismiss();
                            Toast.makeText(Profile_Activity.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                        }
                    });
        }
    }
}
