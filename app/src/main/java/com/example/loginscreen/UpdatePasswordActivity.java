package com.example.loginscreen;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class UpdatePasswordActivity extends AppCompatActivity {

    Button Confirm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_password);

        Confirm = findViewById(R.id.ConfirmButton);

        //user has changed their password and are taken to the login page
        Confirm.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view){
                Intent intent = new Intent(UpdatePasswordActivity.this, LoginActivity.class);
                startActivity(intent);
            }

        });
    }

}