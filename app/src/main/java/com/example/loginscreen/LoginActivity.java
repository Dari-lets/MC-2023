package com.example.loginscreen;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.*;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.*;


public class LoginActivity extends AppCompatActivity {


    EditText username;
    EditText password;

    EditText print;
    Button SignUpButton;
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


        username = (EditText)findViewById(R.id.username);
        password = (EditText)findViewById(R.id.password);
        SignUpButton = findViewById(R.id.SignUpButton);
        login = findViewById(R.id.LoginButton);

        LsOnly = new TextView(this);
        print = new EditText(this);

        SignUpButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {

                Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(intent);

            }
        });

        login.setOnClickListener(new View.OnClickListener(){


            @Override
            public void onClick(View view) {

                String StudentNumber = username.getText().toString();
                String password = LoginActivity.this.password.getText().toString();


                String url = "https://lamp.ms.wits.ac.za/home/s2541383/login.php";

                StringRequest stringRequest = new StringRequest(Request.Method.GET,url,
                        new Response.Listener<String>() {

                            @Override
                            public void onResponse(String response) {

                                try {
                                    Vector<String> StuNums = processJSONStu(response);
                                    Vector<String> StuPass = processJSONPas(response);
                                    m.setOrientation(m.VERTICAL);
                                    //when nobody has signed up, stuNums thing is empty
                                    if (StuNums.size() == 0){
                                        LsOnly.setText("You don't exist bishh! - Monare");
                                        setContentView(m);
                                        m.addView(LsOnly);

                                    }else {
                                        for (int i = 0; i < StuNums.size(); i++) {

                                            if (StuNums.get(i).equals(StudentNumber)) {
                                                if (StuPass.get(i).equals(password)) {
                                                    LsOnly.setText("Login Successful");
                                                    setContentView(m);
                                                    m.addView(LsOnly);
                                                } else {
                                                    LsOnly.setText("Password Wrong bishh");
                                                    setContentView(m);
                                                    m.addView(LsOnly);
                                                }

                                            } else {
                                                LsOnly.setText("You don't exist bishh! - Monare");
                                                setContentView(m);
                                                m.addView(LsOnly);
                                            }
                                        }
                                    }


                                } catch (JSONException e){
                                    throw new RuntimeException(e);
                                }

                            }

                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error){
                        //something is not working the way its supposed to work in the OnResponse
                        LsOnly.setText("Shit's busted");
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
        Vector <String> VecStuNums = new Vector<String>();

        for (int i = 0; i < q.length(); i++){
            JSONObject job = q.getJSONObject(i);
            String Brandy = job.getString("student_number");
            VecStuNums.add(Brandy);
        }
        return VecStuNums;

    }

    public Vector<String> processJSONPas(String jsonyy) throws JSONException {

        JSONArray q = new JSONArray(jsonyy);
        Vector <String> VecStuPass = new Vector<String>();

        for (int i = 0; i < q.length(); i++){
            JSONObject job = q.getJSONObject(i);
            String Brandy = job.getString("passwordd");
            VecStuPass.add(Brandy);
        }
        return VecStuPass;

    }
}