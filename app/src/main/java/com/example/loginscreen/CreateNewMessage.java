package com.example.loginscreen;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class CreateNewMessage extends AppCompatActivity {
    TextView newText;
    Button sendTextButton;
    public static String text;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_message);
        newText = (TextView) findViewById(R.id.txtNewText);
        sendTextButton = (Button) findViewById(R.id.btnSend);

        sendTextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendText(view);
            }
        });
    }

    public void sendText(View view) {
        text = newText.getText().toString();
        //sendTextButton.setText(text);
        //Toast.makeText(this, (CharSequence) text, Toast.LENGTH_SHORT).show();

        Intent intent = new Intent();
        intent.putExtra("TEXT", text);
        setResult(RESULT_OK, intent);
        finish();
//        if (home_page.sendNewText(text)){
//            //finish();
//        }
//        else{
//            Toast.makeText(this, "Message failed to send.", Toast.LENGTH_SHORT).show();
//        }

    }
}