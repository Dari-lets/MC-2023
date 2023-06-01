package com.example.loginscreen;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class CreateProfileActivity extends AppCompatActivity {
    private static final String URL = "https://lamp.ms.wits.ac.za/home/s2541383/hala.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_create_profile);
        Button BackButton = findViewById(R.id.BackButton);
        Button FinishButton = findViewById(R.id.FinishButton);
        EditText user = findViewById(R.id.Username1);
        String pass = "yeah";
        //take text from
//        int [] avatars = {R.drawable.baseline_bedtime_24, R.drawable.baseline_snowmobile_24,
//                R.drawable.baseline_umbrella_24, R.drawable.baseline_umbrella_24, R.drawable.baseline_umbrella_24,
//                R.drawable.baseline_umbrella_24};
        //go back to sign up
        BackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CreateProfileActivity.this, SignupActivity.class);
                startActivity(intent);
            }
        });
        FinishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String UserName = user.getText().toString().trim();
                if (!UserName.isEmpty()) {
                    add_user_info(UserName, pass);
                } else {
                    Toast.makeText(CreateProfileActivity.this, "Invalid input", Toast.LENGTH_SHORT).show();
                }
            }
        });

//        FinishButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(CreateProfileActivity.this, LoginActivity.class);
//                startActivity(intent);
//            }
//        });
    }

    private void add_user_info(String UserName, String pass) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String status = jsonObject.optString("status");

                            if (status.equals("success")) {
                                Toast.makeText(CreateProfileActivity.this, "Data Submitted Successfully", Toast.LENGTH_SHORT).show();
                                // Switch to login page or perform any other actions
                                Intent intent = new Intent(CreateProfileActivity.this, LoginActivity.class);
                                startActivity(intent);
                            } else if (status.equals("error")) {
                                String errorMessage = jsonObject.optString("message");
                                Toast.makeText(CreateProfileActivity.this, "Error: " + errorMessage, Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(CreateProfileActivity.this, "Unknown response", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(CreateProfileActivity.this, "Error parsing response", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(CreateProfileActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("students_number", UserName);
                params.put("passwordd", pass);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}