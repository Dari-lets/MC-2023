package com.example.loginscreen;

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
import com.example.loginscreen.HomePage;
import com.example.loginscreen.R;
import com.example.loginscreen.SignupActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Vector;

public class LoginActivity extends AppCompatActivity {

    EditText name;
    EditText pass;

    EditText print;
    Button RegisterFromLogin;
    Button login;
    LinearLayout m;
    TextView LsOnly;

    EditText Checker;

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
        print = new EditText(this);

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
                String studentNumber = name.getText().toString();
                String password = pass.getText().toString();

                String url = "https://lamp.ms.wits.ac.za/home/s2541383/login.php";

                StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                        new Response.Listener<String>() {

                            @Override
                            public void onResponse(String response) {
                                try {
                                    Vector<String> stuNums = processJSONStu(response);
                                    Vector<String> stuPass = processJSONPas(response);
                                    m.setOrientation(m.VERTICAL);

                                    if (stuNums.size() == 0) {
                                        LsOnly.setText("You don't exist bish!");
                                        setContentView(m);
                                        m.addView(LsOnly);
                                    } else {
                                        boolean loginSuccessful = false;

                                        for (int i = 0; i < stuNums.size(); i++) {
                                            if (stuNums.get(i).equals(studentNumber) && stuPass.get(i).equals(password)) {
                                                loginSuccessful = true;
                                                break;
                                            }
                                        }

                                        if (loginSuccessful) {
                                            LsOnly.setText("Login Successful");
                                        } else {
                                            LsOnly.setText("Invalid credentials bish");
                                        }

                                        setContentView(m);
                                        m.addView(LsOnly);
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

    public void LoginSuccessful(){
        Intent intent = new Intent(this, HomePage.class);
        startActivity(intent);
    }
}


