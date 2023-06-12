package com.example.loginscreen;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Comparator;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ForgotPasswordActivity extends AppCompatActivity {

    Button Submit;

    String [] answers = new String[3];
    EditText q1;
    EditText q2;
    EditText q3;
    EditText stuNum;
    String ans1;
    String ans2;
    String ans3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        Submit = findViewById(R.id.SubmitButton);

        q1 = (EditText) findViewById(R.id.answer1);
        q2 = (EditText) findViewById(R.id.answer2);
        q3 = (EditText) findViewById(R.id.answer3);
        stuNum = (EditText) findViewById(R.id.StudentNumber);

        ans1 = q1.getText().toString().toLowerCase();
        ans2 = q2.getText().toString().toLowerCase();
        ans3 = q3.getText().toString().toLowerCase();


        //after submitting and VERIFYING the answers to the security questions, go to update password
        Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadAnswers(stuNum.getText().toString());
            }
        });
    }

    public void loadAnswers(String stu_Num){
        String url = "https://lamp.ms.wits.ac.za/home/s2584540/loadSecurityAnswers.php";

        HttpUrl.Builder urlBuilder = HttpUrl.parse(url).newBuilder();
        urlBuilder.addQueryParameter("STU_NUM", stu_Num);
        String url2 = urlBuilder.build().toString();

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(url2)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()){

                    String myResponse = response.body().string();
                    try{
                        JSONArray responseArray = new JSONArray(myResponse);

                        JSONObject arrayObject = responseArray.getJSONObject(0);
                        answers[0] = arrayObject.getString("Q1");
                        answers[1] = arrayObject.getString("Q2");
                        answers[2] = arrayObject.getString("Q3");

                        ForgotPasswordActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (validateAnswers()){
                                    Intent intent= new Intent(ForgotPasswordActivity.this, UpdatePasswordActivity.class);
                                    intent.putExtra("STU_NUM", stu_Num);
                                    startActivity(intent);
                                    finish();
                                }
                            }
                        });

                    } catch (JSONException e){
                        e.printStackTrace();
                    }

                }
            }
        });
    }

    public boolean validateAnswers(){
        // Making answers lowercase for leniency
        ans1 = q1.getText().toString().toLowerCase();
        ans2 = q2.getText().toString().toLowerCase();
        ans3 = q3.getText().toString().toLowerCase();

        if (!ans1.equals(answers[0].toLowerCase())){
            Toast.makeText(ForgotPasswordActivity.this, "Question 1 incorrect", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!ans2.equals(answers[1].toLowerCase())){
            Toast.makeText(ForgotPasswordActivity.this, "Question 2 incorrect", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!ans3.equals(answers[2].toLowerCase())){
            Toast.makeText(ForgotPasswordActivity.this, "Question 3 incorrect", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }
}