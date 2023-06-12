package com.example.loginscreen;
import android.content.Context;
import android.util.Log;
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

public class VolleyRequestHelper {
    private static final String TAG = "VolleyRequestHelper";
    private static final String INSERT_URL = "https://lamp.ms.wits.ac.za/home/s2584540/save_data.php";

    private Context context;
    private RequestQueue requestQueue;

    public VolleyRequestHelper(Context context) {
        this.context = context;
        requestQueue = Volley.newRequestQueue(context);
    }

    public void insertData(final String user, final int avator, final String stu_num) {
        StringRequest request = new StringRequest(Request.Method.POST, INSERT_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String status = jsonObject.optString("status");
                            // Handle the response from the server
                            if (status.equals("success")) {

                                Toast.makeText(context, "Data inserted successfully", Toast.LENGTH_SHORT).show();

                            } else {

                                Toast.makeText(context, "Data insertion failed", Toast.LENGTH_SHORT).show();

                            }
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle the error
                        Toast.makeText(context, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.e(TAG, "Error: " + error.getMessage());
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("USERNAME", user);
                params.put("AVATAR", String.valueOf(avator));
                params.put("STU_NUM", stu_num);
                return params;
            }
        };

        requestQueue.add(request);
    }
}
