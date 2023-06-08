package com.example.loginscreen;

import static com.example.loginscreen.PasswordHasher.hashPassword;

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

public class UpdatePasswordActivity extends AppCompatActivity {

    Button Confirm;
    String STU_NUM;
    EditText password;
    EditText confirmPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_password);

        Confirm = findViewById(R.id.ConfirmButton);
        STU_NUM = getIntent().getStringExtra("STU_NUM");
        password = (EditText) findViewById(R.id.NewPassword);
        confirmPassword = (EditText) findViewById(R.id.ConfirmNewPassword);


        //user has changed their password and are taken to the login page
        Confirm.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                String updatedPassword = password.getText().toString();

                // make sure passwords match
                if (updatedPassword.equals(confirmPassword.getText().toString())) {
                    String hashedPassword = hashPassword(updatedPassword);
                    String url = "https://lamp.ms.wits.ac.za/home/s2549501/updatePassword.php";

                    HttpUrl.Builder urlBuilder = HttpUrl.parse(url).newBuilder();
                    urlBuilder.addQueryParameter("STU_NUM", STU_NUM);
                    urlBuilder.addQueryParameter("PASSWORD", hashedPassword);
                    String url2 = urlBuilder.build().toString();

                    Request request = new Request.Builder()
                            .url(url2)
                            .build();

                    OkHttpClient client = new OkHttpClient();

                    client.newCall(request).enqueue(new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            e.printStackTrace();
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            UpdatePasswordActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Intent intent = new Intent(UpdatePasswordActivity.this, LoginActivity.class);
                                    startActivity(intent);
                                    finishAffinity(); // end all parent activities
                                }
                            });

                        }
                    });
                } // end of if statement
                else {
                    Toast.makeText(UpdatePasswordActivity.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                 }
            }
        });
    }
}


