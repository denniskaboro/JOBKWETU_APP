package com.e.jobkwetu.Dialogue;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.e.jobkwetu.App.EndPoints;
import com.e.jobkwetu.App.MyApplication;
import com.e.jobkwetu.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.e.jobkwetu.App.MyApplication.TAG;

public class MpesaConfirmation extends DialogFragment {
    private TextView deposit;
    private EditText mpesa;
    private Button pay;
    private String Amount2;

    public MpesaConfirmation() {
    }
    public static MpesaConfirmation newInstance(String title) {
        MpesaConfirmation frag = new MpesaConfirmation();
        Bundle args = new Bundle();
        args.putString("title", title);
        frag.setArguments(args);
        return frag;
    }
    @Override

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.mpesa_confirmation, container);

        deposit = view.findViewById(R.id.deposit_tx);
        mpesa = view.findViewById(R.id.mpesa_ed);
        pay = view.findViewById(R.id.pay_bt);

        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        Bundle mArgs = getArguments();
        final String username = MyApplication.getInstance().getPrefManager().getUser().getName();
        final String phone1 = MyApplication.getInstance().getPrefManager().getUser().getPhone();
        final String Token = MyApplication.getInstance().getPrefManager().getUser().getToken();
        final String userid = MyApplication.getInstance().getPrefManager().getUser().getId();

        deposit.setText("Deposit From:" + "(Mpesa" + phone1 + ")");

        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StringRequest strReq = new StringRequest(Request.Method.POST,
                        EndPoints.MPESA, new Response.Listener<String>() {


                    @Override
                    public void onResponse(String response) {
                        Log.e(TAG, "response: " + response);

                        try {
                            JSONObject obj = new JSONObject(response);

                            // check for error flag
                            if (true == obj.getBoolean("success")) {
                                // user successfully logged in

                                JSONObject userObj = obj.getJSONObject("data");

                                //WHAT TODO


                            } else {
                                // login error - simply toast the message
                                Toast.makeText(getContext(), "" + obj.getString("message"), Toast.LENGTH_LONG).show();
                            }

                        } catch (JSONException e) {
                            Log.e(TAG, "json parsing error: " + e.getMessage());
                            Toast.makeText(getContext(), "Json parse error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        String json = null;
                        NetworkResponse networkResponse = error.networkResponse;
                        Log.e(TAG, "Volley error: " + error + ", code: " + networkResponse);
                        Toast.makeText(getContext(), "Volley error: " + error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }

                }) {

                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> params = new HashMap<>();
                        final String Amount3 = mpesa.getText().toString();
                        params.put("Amount", Amount3);
                        params.put("Phone", phone1);
                        //params.put("tasks", tasks1);


                        Log.e(TAG, "params: " + params.toString());
                        return params;
                    }

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

        });
return view;
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }
}
