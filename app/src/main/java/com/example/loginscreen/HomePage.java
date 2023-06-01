package com.example.loginscreen;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class HomePage extends AppCompatActivity implements RecyclerInterface{

    static ArrayList<MessageClass> texts = new ArrayList<MessageClass>();
    MessageAdapterClass adapter;

    static int [] avatars = {R.drawable.baseline_bedtime_24, R.drawable.baseline_snowmobile_24,
            R.drawable.baseline_umbrella_24};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        // Crashes the app
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        //this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //getSupportActionBar().hide();


        // Create recycler view for the home page
        RecyclerView recyclerView = findViewById(R.id.mRecycler);
        // Set the values for each recycler view on the home page
        setTexts();
        // Finishing steps
        adapter = new MessageAdapterClass(this, texts, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

    }

    private void setTexts(){
        for (int i = 0; i < 10; i++){
            texts.add(new MessageClass("Bob" + i, "Hello there. I am Bob" , avatars[i % avatars.length]));
        }
    }


    public static MessageClass getMessage(int position){
        return texts.get(position);
    }

//    public static int setImage(int position){
//        return avatars[position];
//    }

    public static boolean sendNewText(String text){
        return true;
    }

    public static boolean sendNewReply(String text){
        return true;
    }

    // Message is clicked.
    @Override
    public void onTextClick(int position) {
        // TODO
        //System.out.println("Message clicked");
        Intent intent = new Intent(this, ReplyActivity.class);
        intent.putExtra("MessageIndex", position);
        startActivity(intent);
    }

    // Message's reply button is clicked
    @Override
    public void onReplyClick(Integer position) {
        // TODO
        texts.get(position).setVotes(texts.get(position).getVotes() + 1);
        adapter.notifyItemChanged(position);
    }

    public void createNewText(View view) {
        Intent intent = new Intent(this, CreateNewMessage.class);
        startActivityForResult(intent, 1);
        //adapter.notifyItemInserted(texts.size());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1){
            if (resultCode == RESULT_OK){
                String result = data.getStringExtra("TEXT");
                Toast.makeText(this, result, Toast.LENGTH_SHORT).show();
                //texts.add(new TextClass("Bob New", result, 6));
                //adapter.notifyItemInserted(texts.size());
            }
        }
    }

}