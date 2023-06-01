package com.example.loginscreen;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
public class SignupActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_signup);

                //moving back to login page
                Button BackButton = findViewById(R.id.BackButton);
                BackButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }
}
/////////////////

//package com.example.loginscreen;
//
//        import android.content.Intent;
//        import android.os.Bundle;
//        import android.view.View;
//        import android.widget.Button;
//        import android.widget.EditText;
//        import android.widget.Toast;
//
//        import androidx.appcompat.app.AppCompatActivity;
//
//        import com.android.volley.Request;
//        import com.android.volley.RequestQueue;
//        import com.android.volley.Response;
//        import com.android.volley.VolleyError;
//        import com.android.volley.toolbox.StringRequest;
//        import com.android.volley.toolbox.Volley;
//
//        import org.json.JSONException;
//        import org.json.JSONObject;
//
//        import java.util.HashMap;
//        import java.util.Map;
//
//public class SignupActivity extends AppCompatActivity {
//
//    private static final String URL = "https://lamp.ms.wits.ac.za/home/s2541383/registernew.php";
//
//    private EditText stuNumEditText;
//    private EditText passwordEditText;
//    private EditText confirmPassEditText;
//    private Button signUpButton = findViewById(R.id.button12);
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_signup);
//
//        stuNumEditText = findViewById(R.id.StudentNumber);
//        passwordEditText = findViewById(R.id.signup_password);
//        confirmPassEditText = findViewById(R.id.signup_confirm_password);
//
//        signUpButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                String studentNumber = stuNumEditText.getText().toString().trim();
//                String password = passwordEditText.getText().toString().trim();
//                String confirmPassword = confirmPassEditText.getText().toString().trim();
//
//                if (!studentNumber.isEmpty() && !password.isEmpty() && password.equals(confirmPassword)) {
//                    registerUser(studentNumber, password);
//                } else {
//                    Toast.makeText(SignupActivity.this, "Invalid input or passwords do not match", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
//    }
//
//    private void registerUser(String studentNumber, String password) {
//        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        try {
//                            JSONObject jsonObject = new JSONObject(response);
//                            String status = jsonObject.optString("status");
//
//                            if (status.equals("success")) {
//                                Toast.makeText(SignupActivity.this, "Data Submitted Successfully", Toast.LENGTH_SHORT).show();
//                                // Switch to login page or perform any other actions
//                                Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
//                                startActivity(intent);
//                            } else if (status.equals("error")) {
//                                String errorMessage = jsonObject.optString("message");
//                                Toast.makeText(SignupActivity.this, "Error: " + errorMessage, Toast.LENGTH_SHORT).show();
//                            } else {
//                                Toast.makeText(SignupActivity.this, "Unknown response", Toast.LENGTH_SHORT).show();
//                            }
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                            Toast.makeText(SignupActivity.this, "Error parsing response", Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        Toast.makeText(SignupActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
//                    }
//                }) {
//            @Override
//            protected Map<String, String> getParams() {
//                Map<String, String> params = new HashMap<>();
//                params.put("student_number", studentNumber);
//                params.put("passwordd", password);
//                return params;
//            }
//        };
//
//        RequestQueue requestQueue = Volley.newRequestQueue(this);
//        requestQueue.add(stringRequest);
//    }
//}
