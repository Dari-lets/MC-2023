package com.example.loginscreen;

import static com.example.loginscreen.PasswordHasher.hashPassword;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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

    EditText username;
    EditText password;

    EditText print;
    Button RegisterFromLogin;
    Button login;
    LinearLayout m;
    Button ForgotPasswordButton;


    TextView LsOnly;
    TextView UsernameText;
    TextView PasswordText;

    EditText Checker;

    static ManageSession manageSession;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        m = new LinearLayout(this);

        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        RegisterFromLogin = findViewById(R.id.SignUpButton);
        login = findViewById(R.id.LoginButton);
        ForgotPasswordButton = findViewById(R.id.ForgotPasswordButton);
        UsernameText = findViewById(R.id.UsernameText);
        PasswordText = findViewById(R.id.PasswordText);

        manageSession = new ManageSession(LoginActivity.this);

        LsOnly = new TextView(this);
        print = new EditText(this);

        //manageSession.setLoggedIn(false);
        // If it doesn't want to logout, just un-comment this to manually set status to false

        //returns a boolean that checks if the person is Logged in or not. If they are, switch to home page. See in the Login.ClickListener how the status was set to true
        if (manageSession.isLoggedIn()) {
            // User is logged in, Go to the Home Page
            Intent intent = new Intent(LoginActivity.this, HomePage.class);
            intent.putExtra("USERNAME", manageSession.getUsername());
            intent.putExtra("AVATAR", manageSession.getAvatar());
            startActivity(intent);
            finish();
        }

        //when user presses sign up button
        RegisterFromLogin.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(intent);
            }
        });
        //when user presses forgot password, go to forgot password page

        ForgotPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
                startActivity(intent);
            }
        });

        login.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                String studentNumber = username.getText().toString();
                String password1 = LoginActivity.this.password.getText().toString();
                String HashedPassword = hashPassword(password1);

                boolean ValidInput = true;
                //strings to hold the input
                String name = username.getText().toString();
                String pass = password.getText().toString();
                if (name.isEmpty()) {
                    ValidInput = false;
                    UsernameText.setText("Required Field");
                } else {
                    UsernameText.setText("");
                }
                if (pass.isEmpty()) {
                    ValidInput = false;
                    PasswordText.setText("Required Field");
                } else {
                    PasswordText.setText("");
                }
                if (ValidInput) {
                    String url = "https://lamp.ms.wits.ac.za/home/s2549501/login.php";

                    StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                            new Response.Listener<String>() {

                                @Override
                                public void onResponse(String response) {
                                    try {
                                        Vector<String> stuNums = processJSONStu(response);
                                        Vector<String> stuPass = processJSONPas(response);
                                        Vector<String> stuUsernames = processJSONUsernames(response);
                                        Vector<Integer> stuAvatars = processJSONAvatars(response);
                                        m.setOrientation(m.VERTICAL);

                                        if (stuNums.size() == 0) {
                                            LsOnly.setText("Database empty, register now!");
                                            setContentView(m);
                                            m.addView(LsOnly);
                                        } else {

                                            boolean found = false;

                                            for (int i = 0; i < stuNums.size(); i++) {
                                                String currentStuNum = stuNums.get(i);
                                                String currentStuPass = stuPass.get(i);
                                                String currentStuUsername = stuUsernames.get(i);
                                                int currentStuAvatar = stuAvatars.get(i);

                                                if (currentStuNum.equals(studentNumber) && currentStuPass.equals(HashedPassword)) {
                                                    //Login successful, go to homepage
                                                    Intent intent = new Intent(LoginActivity.this, HomePage.class);
                                                    intent.putExtra("USERNAME", currentStuUsername);
                                                    intent.putExtra("AVATAR", currentStuAvatar);
                                                    startActivity(intent);
                                                    finish();
                                                    //if the person's credentials are correct, set the LoggedIn status to true, meaning app will stay logged in
                                                    manageSession.setLoggedIn(true);
                                                    manageSession.setUsername(currentStuUsername);
                                                    manageSession.setAvatar(currentStuAvatar);

                                                    //check the bottom of this page for short explanation on Logging out
                                                    found = true; // set flag to true
                                                    break;


                                                }
                                            }

                                            if (!found) {//invalid credentials
                                                Toast toast = Toast.makeText(LoginActivity.this, "Invalid Credentials", Toast.LENGTH_SHORT);
                                                toast.setGravity(Gravity.TOP, 0, 0);
                                                toast.show();
                                            }

                                        }
                                    } catch (JSONException e) {
                                        //this is also for us, for testing
                                        Log.e("JSONException", e.getMessage()); // Log the error
                                        LsOnly.setText("Something bad has happened...");
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
            }
        });


    }

    public Vector<String> processJSONStu(String jsonyy) throws JSONException {
        JSONArray q = new JSONArray(jsonyy);
        Vector<String> vecStuNums = new Vector<>();

        for (int i = 0; i < q.length(); i++) {
            JSONObject job = q.getJSONObject(i);
            String studentNumber = job.getString("STU_NUM");
            vecStuNums.add(studentNumber);
        }
        return vecStuNums;
    }

    public Vector<String> processJSONPas(String jsonyy) throws JSONException {
        JSONArray q = new JSONArray(jsonyy);
        Vector<String> vecStuPass = new Vector<>();

        for (int i = 0; i < q.length(); i++) {
            JSONObject job = q.getJSONObject(i);
            String password = job.getString("PASSWORD");
            vecStuPass.add(password);
        }
        return vecStuPass;
    }

    // NEW processes the student usernames
    public Vector<String> processJSONUsernames(String jsonyy) throws JSONException {
        JSONArray q = new JSONArray(jsonyy);
        Vector<String> vecStuUsernames = new Vector<>();

        for (int i = 0; i < q.length(); i++) {
            JSONObject job = q.getJSONObject(i);
            String password = job.getString("USERNAME");
            vecStuUsernames.add(password);
        }
        return vecStuUsernames;
    }

    public Vector<Integer> processJSONAvatars(String jsonyy) throws JSONException {
        JSONArray q = new JSONArray(jsonyy);
        Vector<Integer> vecStuAvatars = new Vector<>();

        for (int i = 0; i < q.length(); i++) {
            JSONObject job = q.getJSONObject(i);
            Integer avatar = job.getInt("AVATAR");
            vecStuAvatars.add(avatar);
        }
        return vecStuAvatars;
    }

    public void LoginSuccessful(){
        Intent intent = new Intent(this, HomePage.class);
        startActivity(intent);
        finish();
    }

    /*So if you wanted to implement a logout button in another page, you can just set the status to false in the clickListener of a button e.g.
     *
     * Logout.OnClickListener(new View.OnClickListener()  {
     *
     *   @Override
     *   public void OnClick(View V) {
     *
     *       manageSession.setLoggedIn(false);
     *       "End Current Page Activity"
     *
     *   }
     *
     * });
     *
     * The class should handle all the other stuff ^_^ */

}


