package com.example.loginscreen;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class ReplyActivity extends AppCompatActivity implements ReplyRecyclerInterface{

    MessageClass originalMessage;
    ArrayList<ReplyClass> replies = new ArrayList<ReplyClass>();
    ReplyAdapterClass adapter;
    ImageView RmessageAvatar;
    TextView RmessageUsername;
    TextView RmessageContent;

    int [] avatars = {R.drawable.baseline_bedtime_24, R.drawable.baseline_snowmobile_24,
            R.drawable.baseline_umbrella_24};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reply);

        // Crashes the app
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        //this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //getSupportActionBar().hide();

        RmessageAvatar = (ImageView) findViewById(R.id.messageAvatar);
        RmessageUsername = (TextView) findViewById(R.id.messageUsername);
        RmessageContent = (TextView) findViewById(R.id.messageContent);

        // Initialise original message
        int index = getIntent().getIntExtra("MessageIndex", 0);
        originalMessage = HomePage.getMessage(index);
        RmessageAvatar.setImageResource(originalMessage.getAvatar());
        RmessageUsername.setText(originalMessage.getUsername());
        RmessageContent.setText(originalMessage.getText());

        // Create recycler view for the Reply page
        RecyclerView replyRecyclerView = findViewById(R.id.replyRecycler);
        // Set the values for each recycler view on the Reply page
        setTexts();
        // Finishing steps
        adapter = new ReplyAdapterClass(this, replies, this);
        replyRecyclerView.setAdapter(adapter);
        replyRecyclerView.setLayoutManager(new LinearLayoutManager(this));

    }

    private void setTexts(){
        for (int i = 0; i < 10; i++){
            replies.add(new ReplyClass("Bob" + i, "Hello there. I am replying to Bob", avatars[i % avatars.length]));
        }
    }


    public static boolean sendNewText(String text){
        return true;
    }

    public static boolean sendNewReply(String text){
        return true;
    }

    // Reply is clicked.
    @Override
    public void onTextClick(int position) {
        // TODO
        System.out.println("Reply pressed");
        RmessageContent.setText(100 + position);

    }

    // Reply's reply button is clicked
    @Override
    public void onReplyClick(Integer position) {
        // TODO
        System.out.println("Reply reply pressed " + position);
        RmessageContent.setText("yououo");
        //texts.get(position).setVotes(texts.get(position).getVotes() + 1);
        //adapter.notifyItemChanged(position);
    }

    public void createNewText(View view) {
        Intent intent = new Intent(this, CreateNewMessage.class);
        startActivityForResult(intent, 1);
        //adapter.notifyItemInserted(texts.size());
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if (requestCode == 1){
//            if (resultCode == RESULT_OK){
//                String result = data.getStringExtra("TEXT");
//                Toast.makeText(this, result, Toast.LENGTH_SHORT).show();
//                output.setText(result);
//                //texts.add(new TextClass("Bob New", result, 6));
//                //adapter.notifyItemInserted(texts.size());
//            }
//        }
//    }
}