package com.example.loginscreen;

import static com.example.loginscreen.PasswordHasher.hashPassword;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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
import java.util.Vector;

public class SignupActivity extends AppCompatActivity {
    private static final String URL = "https://lamp.ms.wits.ac.za/home/s2549501/register.php";
    Button BackButton;
    Button SubmitButton;
    TextView Question1Text;
    TextView Question2Text;
    TextView Question3Text;
    TextView pwDontMatch;
    TextView PwText;
    TextView ConfirmPwText;
    TextView StudentNumberText;
    TextView AnswerText;
    TextView Answer1Text;
    TextView Answer2Text;

    EditText Answer;
    EditText Answer1;
    EditText Answer2;


    int count;
    int ans_index;

    //from sam
    Button SignUp;
    EditText stu_num;
    EditText PasswordEditText;
    EditText ConfirmPasswordEditText;
    LinearLayout LayoutQuestions;
    boolean submitted=false;



    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        SignUp = findViewById(R.id.sign_up_button);
        stu_num = findViewById(R.id.StudentNumber);
        PasswordEditText = findViewById(R.id.signup_password);
        ConfirmPasswordEditText = findViewById(R.id.signup_confirm_password);
        pwDontMatch = findViewById(R.id.ConfirmPwText);
        BackButton = findViewById(R.id.BackButton);
        SubmitButton = findViewById(R.id.SubmitButton);
        Question1Text = findViewById(R.id.Question1Text);
        Question2Text = findViewById(R.id.Question2Text);
        Question3Text = findViewById(R.id.Question3Text);
        Answer = findViewById(R.id.Answer);
        Answer1 = findViewById(R.id.Answer1);
        Answer2 = findViewById(R.id.Answer2);
        AnswerText = findViewById(R.id.AnswerText);
        Answer1Text = findViewById(R.id.Answer1Text);
        Answer2Text = findViewById(R.id.Answer2Text);
        PwText = findViewById(R.id.PwText);
        ConfirmPwText = findViewById(R.id.ConfirmPwText);
        StudentNumberText = findViewById(R.id.StudentNumberText);
        LayoutQuestions = findViewById(R.id.LayoutQuestions);
        count = 0;
        ans_index = 0;
        //questions
        String[] Questions = {"What is the name of your favourite teacher in high school?",
                "What is the name of the city where you were born?",
                "What is the name of your favourite first-year lecturer"};

        Question1Text.setText(Questions[0]);
        Question2Text.setText(Questions[1]);
        Question3Text.setText(Questions[2]);
        //make array to keep the answers
        String[] Answers = {"","",""};

        Vector<String> StuNums = new Vector<String>();
        BackButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                //move to the login page
                finish();
            }
        });

        SignUp.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                 boolean ValidInput = true;
                //strings for holding the input from the app user in the EditTexts
                String stu_number = stu_num.getText().toString();
                String password = PasswordEditText.getText().toString();
                String ConfirmPassword = ConfirmPasswordEditText.getText().toString();

                //checking if the input is empty
                if (stu_number.isEmpty()) {//don't process
                    StudentNumberText.setText("Required Field");
                    ValidInput = false;
                } else {
                    StudentNumberText.setText("");
                }
                if (password.isEmpty()) {
                    PwText.setText("Required Field");
                    ValidInput = false;
                } else {
                    PwText.setText("");
                }
                if (ConfirmPassword.isEmpty()) {
                    ConfirmPwText.setText("Required Field");
                    ValidInput = false;
                } else {
                    ConfirmPwText.setText("");
                }

                if (ValidInput && submitted) {//input and answers are valid
                    if (password.equals(ConfirmPassword)) { //passwords match
                        String url = "https://lamp.ms.wits.ac.za/home/s2549501/register.php";
                        String hashedPassword = hashPassword(password);
                        if (hashedPassword != null) {
                            //store input in db
                            registerUser(stu_number, hashedPassword);
                        } else {
                            //for us, for testing purposes, must be removed
                            Toast.makeText(SignupActivity.this, "Error hashing password", Toast.LENGTH_SHORT).show();
                        }
                        //switch to create profile page
                        Intent intent = new Intent(SignupActivity.this, CreateProfileActivity.class);
                        intent.putExtra("StudentNumber",stu_number);
                        startActivity(intent);
                        finish();
                    } else {//input ans answers are invalid, do not process
                        pwDontMatch.setText("Passwords Do Not Match!");
                    }

                }
            }
        });

        //store the answers in the array
        SubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean ValidAnswers = true;
                String Ans = Answer.getText().toString();
                String Ans1 = Answer1.getText().toString();
                String Ans2 = Answer2.getText().toString();
                String stu_number = stu_num.getText().toString();

                //checking the answers
                if(Ans.isEmpty()){
                    ValidAnswers=false;
                    AnswerText.setText("Required Field");
                } else{
                    AnswerText.setText("");
                }
                if(Ans1.isEmpty()){
                    ValidAnswers=false;
                    Answer1Text.setText("Required Field");
                } else{
                    Answer1Text.setText("");
                }
                if(Ans2.isEmpty()){
                    ValidAnswers=false;
                    Answer2Text.setText("Required Field");
                } else{
                    Answer2Text.setText("");
                }

                if(ValidAnswers){//if answers are valid, store them and remove that layout
                    //store answers in the array
                    Answers[0]=Ans;
                    Answers[1]=Ans1;
                    Answers[2]=Ans2;
                    //store answers in db
                    InsertAnswers(stu_number,Ans,Ans1,Ans2);
                    LayoutQuestions.setVisibility(View.GONE);
                    submitted =true;
                }
            }
        });


    }
    private void registerUser(String studentNumber, String password) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String status = jsonObject.optString("status");

                            if (status.equals("success")) {
                                Toast.makeText(SignupActivity.this, "Data Submitted Successfully", Toast.LENGTH_SHORT).show();
                                // Switch to login page or perform any other actions
                                //Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                                //startActivity(intent);
                            } else if (status.equals("error")) {
                                String errorMessage = jsonObject.optString("message");
                                Toast.makeText(SignupActivity.this, "Error: " + errorMessage, Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(SignupActivity.this, "Unknown response", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(SignupActivity.this, "Error parsing response", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(SignupActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("STU_NUM", studentNumber);
                params.put("PASSWORD", password);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    //Call this method on the EditTexts in the "submit" button

    private void InsertAnswers(String studentNumber, String Q1, String Q2, String Q3) {

        String url = "https://lamp.ms.wits.ac.za/home/s2549501/sq_post.php";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String status = jsonObject.optString("status");

                            if (status.equals("success")) {
                                Toast.makeText(SignupActivity.this, "Data Submitted Successfully", Toast.LENGTH_SHORT).show();
                                // Switch to login page or perform any other actions

                                //Intent intent = new Intent(Sign_up_page.this, MainActivity.class);
                                //startActivity(intent);

                            } else if (status.equals("error")) {
                                String errorMessage = jsonObject.optString("message");
                                Toast.makeText(SignupActivity.this, "Error: " + errorMessage, Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(SignupActivity.this, "Unknown response", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();

                            Toast.makeText(SignupActivity.this, "Error parsing response", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(SignupActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("STU_NUM", studentNumber);
                params.put("Q1", Q1);
                params.put("Q2", Q2);
                params.put("Q3", Q3);

                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

}