package com.example.loginscreen;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ForgotPasswordActivity extends AppCompatActivity {

    Button Submit;
    Button GoBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        Submit = findViewById(R.id.SubmitButton);
        GoBack = findViewById(R.id.BackButton);

        //clicking back button, go to login
        GoBack.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view){
                Intent intent = new Intent(ForgotPasswordActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        //after submitting and VERIFYING the answers to the security questions, go to update password
        Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(ForgotPasswordActivity.this, UpdatePasswordActivity.class);
                startActivity(intent);
            }
        });
    }
}