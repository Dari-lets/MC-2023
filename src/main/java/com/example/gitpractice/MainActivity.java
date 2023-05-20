package com.example.gitpractice;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    TextView output;
    Button btnProcess;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        output = (TextView) findViewById(R.id.txtOutput);
        btnProcess = (Button) findViewById(R.id.btnOK);

        btnProcess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Process();
            }
        });
    }

    public void Process(){
        // Smile.
        // Set this text to be your name/username
        output.setText("AisuZukinaOtoko");
    }
}