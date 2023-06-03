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

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

public class SignupActivity extends AppCompatActivity {

    Button BackButton;
    Button SkipButton;
    Button SubmitButton;
    TextView QuestionText;
    TextView pwDontMatch;

    EditText Answer;
    EditText PasswordEditText;
    EditText ConfirmPasswordEditText;
    int count;
    int ans_index;
    String password = PasswordEditText.getText().toString();
    String confirmPassword = ConfirmPasswordEditText.getText().toString();
    //from sam
    Button SignUp;
    EditText stu_num;
    EditText passs;
    EditText conpass;
    LinearLayout m;
    EditText LsOnly;

    TextView Confirm;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_signup);

                BackButton = findViewById(R.id.BackButton);
                SkipButton = findViewById(R.id.SkipButton);
                SubmitButton = findViewById(R.id.SubmitButton);
                QuestionText = findViewById(R.id.QuestionText);
                pwDontMatch = findViewById(R.id.pwDifferentText);
                Answer = findViewById(R.id.Answer);
                PasswordEditText = findViewById(R.id.signup_password);
                ConfirmPasswordEditText = findViewById(R.id.signup_confirm_password);
                count = 0;
                ans_index=0;

                String[] Questions ={"What is the name of your favourite teacher in high school?",
                        "What is the name of the hospital where you were born?",
                        "What is the middle name of your oldest sibling?",
                        "What is the name of the street you grew up on?",
                        "What was the name of your first-grade teacher?",
                        "What is the name of the first boy/girl you kissed?"};
                QuestionText.setText(Questions[0]);

                //make array to keep track of the indices of the questions they answered (only when they click submit)
                //make array to keep the answers
                String[] Answers= new String[3];
                int[] Indices = {-1, -1, -1};


                //checks if the passwords match
                String message = "Passwords don't match!";

                if (!validatePassword()){
                    pwDontMatch.setText(message);
                    System.out.println("they dont match....");
                }




                //changes the questions shown as the user clicks skip
                SkipButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        count +=1;

                        if(count==6){
                            count=0;
                        }
                        for (int i = 0; i < 3; i++){
                            if (Indices[i] == count){
                                count++;
                            }
                        }
                        QuestionText.setText(Questions[count]);
                    }
                });
                //when user clicks submit, save their answer and the question index to the respective arrays
                SubmitButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //get question index(count) and put it in the Indices array
                        Indices[ans_index]=count;
                        //get answer and put it in the matching index in the Answers array
                        String ans = Answer.getText().toString();
                        Answers[ans_index]=ans;
                        //clear edittext
                        Answer.setText("");
                        //change question
                        count+=1;
                        QuestionText.setText(Questions[count]);
                        ans_index+=1;
                        if(ans_index==3){
                            //take to home page after input validation
                            //still must "fix"
                            Intent intent = new Intent(SignupActivity.this, HomePage.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                });

                //moving back to login page
                BackButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
            }
        });

    }
    private boolean validatePassword(){
        System.out.println("GETS HERE");
        if(password.equals(confirmPassword)) {
            //passwords match
            return true;
        }
        else{
            //passwords don't match
            return false;
        }
    }
}