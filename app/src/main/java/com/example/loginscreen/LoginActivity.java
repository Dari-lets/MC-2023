package com.example.loginscreen;

import static com.example.loginscreen.PasswordHasher.hashPassword;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Vector;

public class LoginActivity extends AppCompatActivity {

    EditText name;
    EditText pass;
    Button RegisterFromLogin;
    Button login;

    //These below are for testing
    LinearLayout m;
    TextView LsOnly;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        m = new LinearLayout(this);

        name = findViewById(R.id.username);
        pass = findViewById(R.id.password);
        RegisterFromLogin = findViewById(R.id.SignUpButton);
        login = findViewById(R.id.LoginButton);

        LsOnly = new TextView(this);

        RegisterFromLogin.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(intent);
            }
        });

        login.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                String username = name.getText().toString();
                String password = pass.getText().toString();
                String HashedPassword = hashPassword(password);

                String url = "https://lamp.ms.wits.ac.za/home/s2541383/login.php";

                StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                        new Response.Listener<String>() {

                            @SuppressLint("SetTextI18n")
                            @Override
                            public void onResponse(String response) {
                                try {
                                    Vector<String> stuNums = processJSONStu(response);
                                    Vector<String> stuPass = processJSONPas(response);
                                    m.setOrientation(m.VERTICAL);

                                    if (stuNums.size() == 0) {
                                        LsOnly.setText("Database empty, register now");
                                        setContentView(m);
                                        m.addView(LsOnly);
                                    } else {

                                        boolean found = false; // Flag to track if a match is found

                                        for (int i = 0; i < stuNums.size(); i++) {
                                            String currentStuNum = stuNums.get(i);
                                            String currentStuPass = stuPass.get(i);

                                            if (currentStuNum.equals(username) && currentStuPass.equals(password)) {
                                                // Login Successful
                                                found = true; // Set the flag to true
                                                LoginActivity.this.runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        Intent intent = new Intent(LoginActivity.this, HomePage.class);
                                                        intent.putExtra("USERNAME", username);
                                                        startActivity(intent);
                                                        finish();
                                                    }
                                                });
                                            }
                                        }

                                        if (!found) {
                                            // No match found
                                            LsOnly.setText("Invalid Username or Password");
                                            setContentView(m);
                                            m.addView(LsOnly);
                                        }



                                    }
                                } catch (JSONException e) {
                                    Log.e("JSONException", e.getMessage()); // Log the error
                                    LsOnly.setText("Error occurred");
                                    setContentView(m);
                                    m.addView(LsOnly);
                                }

                            }

                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("VolleyError", error.getMessage()); // Log the error
                        LsOnly.setText("Error occurred");
                        setContentView(m);
                        m.addView(LsOnly);
                    }
                });

                RequestQueue requestQueue = Volley.newRequestQueue(LoginActivity.this);
                requestQueue.add(stringRequest);
            }
        });

    }

    public Vector<String> processJSONStu(String jsonyy) throws JSONException {
        JSONArray q = new JSONArray(jsonyy);
        Vector<String> vecStuNums = new Vector<>();

        for (int i = 0; i < q.length(); i++) {
            JSONObject job = q.getJSONObject(i);
            String studentNumber = job.getString("student_number");
            vecStuNums.add(studentNumber);
        }
        return vecStuNums;
    }

    public Vector<String> processJSONPas(String jsonyy) throws JSONException {
        JSONArray q = new JSONArray(jsonyy);
        Vector<String> vecStuPass = new Vector<>();

        for (int i = 0; i < q.length(); i++) {
            JSONObject job = q.getJSONObject(i);
            String password = job.getString("passwordd");
            vecStuPass.add(password);
        }
        return vecStuPass;
    }
}

