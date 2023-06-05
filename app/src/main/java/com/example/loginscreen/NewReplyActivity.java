package com.example.loginscreen;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class NewReplyActivity extends AppCompatActivity {
    MessageClass originalMessage;
    ReplyClass originalReply;
    int messageType;

    // Details of the message/reply you are replying to
    TextView username;
    TextView text;
    ImageView avatar;

    TextView newText;
    Button SubmitButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_reply);

        username = (TextView) findViewById(R.id.usernameReply);
        text = (TextView) findViewById(R.id.textReply);
        avatar = (ImageView) findViewById(R.id.avatarReply);

        newText = findViewById(R.id.txtNewReply);
        SubmitButton = findViewById(R.id.btnSendReply);

        messageType = getIntent().getIntExtra("MESSAGE_TYPE", HomePage.MESSAGE_TYPE);
        int messagePosition;
        int replyPosition;

        // Initialise the message/reply seen at the top of the screen
        if (messageType == HomePage.MESSAGE_TYPE){
            messagePosition = getIntent().getIntExtra("POSITION", 0);
            originalMessage = HomePage.getMessage(messagePosition);
            username.setText(originalMessage.getUsername());
            text.setText(originalMessage.getText());
            avatar.setImageResource(originalMessage.getAvatar());
        }
        else if (messageType == HomePage.REPLY_TYPE){
            replyPosition = getIntent().getIntExtra("POSITION", 0);
            messagePosition = getIntent().getIntExtra("ORIGINALKEY", 0);
            originalReply = ReplyActivity.getReply(replyPosition);
            username.setText(originalReply.getUsername());
            text.setText(originalReply.getReply());
            avatar.setImageResource(originalReply.getAvatar());

            String placeHolder = "@" + username.getText().toString() + " ";
            newText.setText(placeHolder);
        }

        SubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String reply = newText.getText().toString();
                if (text.length() > 300) {
                    Toast.makeText(NewReplyActivity.this, "Reply is too long.", Toast.LENGTH_SHORT).show();
                    return;
                }

                Intent intent = new Intent();
                intent.putExtra("REPLY", reply);
                setResult(RESULT_OK, intent);
                sendNewReply(reply);
            }
        });
    }

    private void sendNewReply(String text){
        String url = "https://lamp.ms.wits.ac.za/home/s2549501/newReply.php";

        HttpUrl.Builder urlBuilder = HttpUrl.parse(url).newBuilder();
        urlBuilder.addQueryParameter("USERNAME", HomePage.CURRENT_USER_USERNAME);
        urlBuilder.addQueryParameter("MESSAGE", text);
        urlBuilder.addQueryParameter("AVATAR", Integer.toString(HomePage.CURRENT_USER_AVATAR));
        if (messageType == HomePage.MESSAGE_TYPE)
            urlBuilder.addQueryParameter("MSG_ID", Integer.toString(originalMessage.getID()));
        else
            urlBuilder.addQueryParameter("MSG_ID", Integer.toString(originalReply.getOrignalMessageKey()));
        String url2 = urlBuilder.build().toString();

        Request request = new Request.Builder()
                .url(url2)
                .build();

        OkHttpClient client = new OkHttpClient();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                Toast.makeText(NewReplyActivity.this, "Reply failed.", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                NewReplyActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        finish();
                    }
                });
            }
        });
    }
}