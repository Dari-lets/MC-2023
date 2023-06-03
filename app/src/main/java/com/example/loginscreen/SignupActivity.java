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

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

public class SignupActivity extends AppCompatActivity {

    Button BackButton;
    Button SkipButton;
    Button SubmitButton;
    TextView QuestionText;
    TextView pwDontMatch;
    TextView PwText;
    TextView ConfirmPwText;
    TextView StudentNumberText;
    TextView AnswerText;

    EditText Answer;


    int count;
    int ans_index;

    //from sam
    Button SignUp;
    EditText stu_num;
    EditText PasswordEditText;
    EditText ConfirmPasswordEditText;
    LinearLayout LayoutQuestions;


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
        SkipButton = findViewById(R.id.SkipButton);
        SubmitButton = findViewById(R.id.SubmitButton);
        QuestionText = findViewById(R.id.QuestionText);
        Answer = findViewById(R.id.Answer);
        AnswerText = findViewById(R.id.AnswerText);
        PwText = findViewById(R.id.PwText);
        ConfirmPwText = findViewById(R.id.ConfirmPwText);
        StudentNumberText = findViewById(R.id.StudentNumberText);
        LayoutQuestions = findViewById(R.id.LayoutQuestions);
        count = 0;
        ans_index=0;

        Vector<String> StuNums = new Vector<String>();

        SignUp.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                boolean validInput = true;
                //strings for holding the input from the app user in the EditTexts
                String stu_number = stu_num.getText().toString();
                String password = PasswordEditText.getText().toString();
                String ConfirmPassword = ConfirmPasswordEditText.getText().toString();
                //if any of the edit texts are empty
                if(stu_number.isEmpty()){//dont process
                    StudentNumberText.setText("Required Field");
                    validInput = false;
                }
                else{
                    StudentNumberText.setText("");
                }
                if(password.isEmpty()){
                    PwText.setText("Required Field");
                    validInput = false;
                }
                else{
                    PwText.setText("");
                }
                if(ConfirmPassword.isEmpty()){
                    ConfirmPwText.setText("Required Field");
                    validInput = false;
                }
                else{
                    ConfirmPwText.setText("");
                }

                if (validInput){
                    if (password.equals(ConfirmPassword)) { //passwords match
                        String url = "https://lamp.ms.wits.ac.za/home/s2541383/register.php";

                        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        Toast.makeText(SignupActivity.this, response.trim(), Toast.LENGTH_SHORT).show();
                                    }
                                },
                                //ErrorLister just displays that there is an error when the onResponse function above isn't working for some reason
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        Toast.makeText(SignupActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                                    }
                                }) {
                            @Override
                            protected Map<String, String> getParams() /*throws AuthFailureError */ {
                                Map<String, String> params = new HashMap<String, String>();
                                //Here is where you indicate what you want to input
                                params.put("student_number", stu_number); //"student_number" is the column from my register table, stu_number is the string created above
                                params.put("passwordd", password);
                                // example: params.put("column_name", String_input);

                                //You can add more if you want to insert into more columns like how its done up here, as long as its accounted for in the php file

                                return params;
                            }
                        };
                        RequestQueue requestQueue = Volley.newRequestQueue(SignupActivity.this);
                        requestQueue.add(stringRequest);
                        //switching back to login page
                        Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                        startActivity(intent);
                    } else {
                        pwDontMatch.setText("Passwords Do Not Match!");
                    }

                }
            }
        });

        String[] Questions ={"What is the name of your favourite teacher in high school?",
                "What is the name of the city where you were born?",
                "What is the middle name of your oldest sibling?",
                "What is the name of the street you grew up on?",
                "What was the name of your first-grade teacher?",
                "What is the name of the first boy/girl you kissed?"};

        QuestionText.setText(Questions[0]);
        //make array to keep track of the indices of the questions they answered (only when they click submit)
        int[] Indices = {-1, -1, -1};
        //make array to keep the answers
        String[] Answers= new String[3];

        //changes the questions shown as the user clicks skip
        SkipButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AnswerText.setText("");
                count +=1;

                if(count==6){
                    count=0;
                }
                    for (int i = 0; i < 3; i++){
                        if(count==5){
                            continue;
                        }
                        else if (Indices[i] == count){
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
                //get answer
                String ans = Answer.getText().toString();
                if(ans.isEmpty()){//didn't answer but pressed submit
                    AnswerText.setText("Field Required!");
                }
                else{//answered, continue processing
                    //get question index(count) and put it in the Indices array
                    Indices[ans_index]=count;
                    //put answer in the matching index in the Answers array
                    Answers[ans_index]=ans;

                    //clear edittext
                    Answer.setText("");
                    //change question
                    count+=1;
                    if(count==6){
                        count=0;}
                    for (int i = 0; i < 3; i++){
                        if(count==5){
                            continue;
                        }
                        else if (Indices[i] == count){
                            count++;
                        }
                    }
                    QuestionText.setText(Questions[count]);
                    ans_index+=1;
                    if(ans_index==3){
                        //theyve already answered three questions
                        //remove the texts??
                        LayoutQuestions.setVisibility(View.GONE);
                    }
                }
            }
        });

    }

    /*public boolean ValidateInput(){
        if(stu_number.isEmpty() || password.isEmpty() || ConfirmPassword.isEmpty()){
            return false;
        }
        else{
            return true;
        }
    }*/

}