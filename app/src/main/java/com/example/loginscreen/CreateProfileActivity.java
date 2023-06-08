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
import android.widget.ToggleButton;

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
    ToggleButton t1;
    ToggleButton t2;
    ToggleButton t3;
    ToggleButton t4;
    ToggleButton t5;
    ToggleButton t6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        String stu_num = getIntent().getStringExtra("StudentNumber");
        setContentView(R.layout.activity_create_profile);
        Button BackButton = findViewById(R.id.BackButton);
        Button FinishButton = findViewById(R.id.FinishButton);
        EditText user = findViewById(R.id.Username1);
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

        t1 = findViewById(R.id.esport_toggle);
        t2 = findViewById(R.id.diamond_toggle);
        t3 = findViewById(R.id.face_toggle);
        t4 = findViewById(R.id.peace_toggle);
        t5 = findViewById(R.id.moon_toggle);
        t6 = findViewById(R.id.pokemon_toggle);



        ToggleButton [] T_buttons = {t1,t2,t3,t4,t5,t6};

        cardView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                index = 0;
            }
        });
        t1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                index = 0;
                t2.setChecked(false);
                t3.setChecked(false);
                t4.setChecked(false);
                t5.setChecked(false);
                t6.setChecked(false);
            }

        });

        cardView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                index = 1;
            }
        });
        t2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                index = 1;
                t1.setChecked(false);
                t3.setChecked(false);
                t4.setChecked(false);
                t5.setChecked(false);
                t6.setChecked(false);
            }

        });
        cardView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                index = 2;
            }
        });
        t3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                index = 2;
                t1.setChecked(false);
                t2.setChecked(false);
                t4.setChecked(false);
                t5.setChecked(false);
                t6.setChecked(false);
            }

        });
        cardView4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                index = 3;
            }
        });
        t4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                index = 3;
                t1.setChecked(false);
                t2.setChecked(false);
                t3.setChecked(false);
                t5.setChecked(false);
                t6.setChecked(false);
            }

        });
        cardView5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                index = 4;
            }
        });
        t5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                index = 4;
                t1.setChecked(false);
                t2.setChecked(false);
                t3.setChecked(false);
                t4.setChecked(false);
                t6.setChecked(false);

            }

        });
        cardView6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                index = 5;
            }
        });
        t6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                index = 5;
                t2.setChecked(false);
                t3.setChecked(false);
                t4.setChecked(false);
                t5.setChecked(false);
                t1.setChecked(false);
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

        //this should take you to the homepage after choosing a username AND avatar
        FinishButton.setOnClickListener(new View.OnClickListener() {
            //first check if username is not empty and one of the buttons have been chosen

            @Override
            public void onClick(View view) {

                String UserName = user.getText().toString().trim();
                if (!UserName.isEmpty()) {
                    if (t1.isChecked() || t2.isChecked() || t3.isChecked() || t4.isChecked() || t5.isChecked() || t6.isChecked()){
                        VolleyRequestHelper volleyRequestHelper = new VolleyRequestHelper(CreateProfileActivity.this);
                        volleyRequestHelper.insertData(UserName, index, stu_num);
                        Intent intent = new Intent(CreateProfileActivity.this, HomePage.class);
                        intent.putExtra("STU_NUM", stu_num);
                        startActivity(intent);
                        finishAffinity();
                    }else {
                        Toast.makeText(CreateProfileActivity.this, "Please choose an avatar", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(CreateProfileActivity.this, "Please enter a username", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}

