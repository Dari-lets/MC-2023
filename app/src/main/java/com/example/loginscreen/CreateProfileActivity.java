package com.example.loginscreen;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
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

public class CreateProfileActivity extends AppCompatActivity {
    //   private static final String URL = "https://lamp.ms.wits.ac.za/home/s2541383/hala.php";
    private int index = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_create_profile);
        Button BackButton = findViewById(R.id.BackButton);
        Button FinishButton = findViewById(R.id.FinishButton);
        EditText user = findViewById(R.id.Username1);

        String pass = "lldaphrich@";
        //take text from
        int[] avatars = {R.drawable.baseline_sports_esports_24, R.drawable.baseline_diamond_24,
                R.drawable.face, R.drawable.baseline_self_improvement_24, R.drawable.moon,
                R.drawable.pokemon};


        CardView cardView1 = findViewById(R.id.esport);
        CardView cardView2 = findViewById(R.id.esport1);
        CardView cardView3 = findViewById(R.id.esport2);
        CardView cardView4 = findViewById(R.id.esport3);
        CardView cardView5 = findViewById(R.id.esport4);
        CardView cardView6 = findViewById(R.id.esport5);

        cardView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                index = 0;
            }
        });
        cardView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                index = 1;
            }
        });
        cardView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                index = 2;
            }
        });
        cardView4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                index = 3;
            }
        });
        cardView5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                index = 4;
            }
        });
        cardView6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                index = 5;
            }
        });


        //go back to sign up
        BackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CreateProfileActivity.this, SignupActivity.class);
                startActivity(intent);
            }
        });
        //  VolleyRequestHelper volleyRequestHelper = new VolleyRequestHelper(CreateProfileActivity.this);
        FinishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String UserName = user.getText().toString().trim();
                if (!UserName.isEmpty()) {
                    VolleyRequestHelper volleyRequestHelper = new VolleyRequestHelper(CreateProfileActivity.this);
                    volleyRequestHelper.insertData(UserName, index);
                    Intent intent = new Intent(CreateProfileActivity.this, LoginActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(CreateProfileActivity.this, "Invalid input", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}

