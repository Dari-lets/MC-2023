package com.example.loginscreen;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import android.widget.ToggleButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class HomePage extends AppCompatActivity implements RecyclerInterface{

    private Toolbar toolbar;
    ImageView logOutButton;

    static ArrayList<MessageClass> texts = new ArrayList<MessageClass>();

    MessageAdapterClass adapter;
    RecyclerView recyclerView;

    static String CURRENT_USER_STU_NUM = "DEFAULT";
    static String CURRENT_USER_USERNAME = "DEFAULT";
    static int CURRENT_USER_AVATAR = 0;

    // The types of messages. Either original or reply
    static int MESSAGE_TYPE = 0;
    static int REPLY_TYPE = 1;

    int[] avatars = {R.drawable.baseline_sports_esports_24, R.drawable.baseline_diamond_24,
            R.drawable.face, R.drawable.baseline_self_improvement_24, R.drawable.moon,
            R.drawable.pokemon};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        texts.clear();
        CURRENT_USER_USERNAME = getIntent().getStringExtra("USERNAME");
        CURRENT_USER_AVATAR = getIntent().getIntExtra("AVATAR", CURRENT_USER_AVATAR);

        LoginActivity.manageSession.setLoggedIn(true);
        LoginActivity.manageSession.setAvatar(CURRENT_USER_AVATAR);
        LoginActivity.manageSession.setUsername(CURRENT_USER_USERNAME);

        // Create recycler view for the home page
        recyclerView = findViewById(R.id.mRecycler);
        recyclerView.setItemAnimator(null);

        loadMessages();
        // Finishing steps
        adapter = new MessageAdapterClass(this, texts, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        logOutButton = findViewById(R.id.LogOut);
        logOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onLogOutClicked();
            }
        });

    }

    private void onLogOutClicked(){
        LoginActivity.manageSession.LogOut();
        Intent intent = new Intent(HomePage.this, LoginActivity.class);
        startActivity(intent);
        finishAffinity();
    }

    private void loadMessages(){
        texts.clear();
        String url = "https://lamp.ms.wits.ac.za/home/s2584540/loadMessages.php";

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()){

                    String myResponse = response.body().string();
                    try{
                        JSONArray responseArray = new JSONArray(myResponse);

                        for (int i = 0; i < responseArray.length(); i++) {
                            JSONObject arrayObject = responseArray.getJSONObject(i);
                            String username = arrayObject.getString("USERNAME");
                            String text = arrayObject.getString("MESSAGE");
                            int avatar = arrayObject.getInt("AVATAR");
                            int votes = arrayObject.getInt("UPVOTES");
                            MessageClass temp = new MessageClass(username, text, avatars[avatar]);
                            temp.setID(arrayObject.getInt("MSG_ID"));
                            temp.setVotes(votes);
                            texts.add(temp);
                        }


                            HomePage.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    loadVotes();
                                    texts.sort(Comparator.comparing(MessageClass::getVotes).reversed());
                                    adapter.notifyDataSetChanged();
                                }
                            });

                    } catch (JSONException e){
                        e.printStackTrace();
                    }

                }
            }
        });

    }

    private void loadVotes(){
        String url = "https://lamp.ms.wits.ac.za/home/s2584540/msgVotes.php";

        HttpUrl.Builder urlBuilder = HttpUrl.parse(url).newBuilder();
        urlBuilder.addQueryParameter("USERNAME", CURRENT_USER_USERNAME);
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
                if (response.isSuccessful()){

                    String myResponse = response.body().string();
                    try{
                        JSONArray responseArray = new JSONArray(myResponse);

                        for (int i = 0; i < responseArray.length(); i++) {
                            JSONObject arrayObject = responseArray.getJSONObject(i);
                            int msgID = arrayObject.getInt("MSG_ID");
                            int msgVote = arrayObject.getInt("VOTE");
                            for(MessageClass message : texts){
                                if (message.getID() == msgID){
                                    if (msgVote == 1)
                                        message.upVoted = true;
                                    else if (msgVote == -1)
                                        message.downVoted = true;
                                }
                            }

                        }
                        HomePage.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                adapter.notifyDataSetChanged();
                            }
                        });

                    } catch (JSONException e){
                        e.printStackTrace();
                    }

                }
            }
        });

    }

    public static MessageClass getMessage(int position){
        return texts.get(position);
    }

    private void sendNewText(String text){
        String url = "https://lamp.ms.wits.ac.za/home/s2584540/newMessage.php";

        HttpUrl.Builder urlBuilder = HttpUrl.parse(url).newBuilder();
        urlBuilder.addQueryParameter("USERNAME", CURRENT_USER_USERNAME);
        urlBuilder.addQueryParameter("MESSAGE", text);
        urlBuilder.addQueryParameter("AVATAR", Integer.toString(CURRENT_USER_AVATAR));
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
                HomePage.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        loadMessages();
                    }
                });
            }
        });
    }

    // Message is clicked.
    @Override
    public void onTextClick(int position) {
        Intent intent = new Intent(this, ReplyActivity.class);
        intent.putExtra("MessageIndex", position);
        startActivity(intent);
    }

    // Message's reply button is clicked
    @Override
    public void onReplyClick(int position) {
        Intent intent = new Intent(this, NewReplyActivity.class);
        intent.putExtra("MESSAGE_TYPE", MESSAGE_TYPE);
        intent.putExtra("POSITION", position);
        startActivityForResult(intent, 2);
    }

    // Message is upVoted. True if pressed. False if unpressed
    @Override
    public void onUpvote(int position, boolean press, View view) {
        ToggleButton downVoteButton = view.findViewById(R.id.btnDownvote);
        if (downVoteButton.isChecked()){
            updateVotes(position, 2);
            texts.get(position).downVoted = false;
            texts.get(position).upVoted = true;
        }
        else if (press){
            updateVotes(position, 1);
            texts.get(position).upVoted = true;
        }
        else{
            updateVotes(position, -1);
            texts.get(position).upVoted = false;
        }
        downVoteButton.setChecked(false);
    }


    // Message is downVoted. True if pressed. False if unpressed
    @Override
    public void onDownVote(int position, boolean press, View view) {
        ToggleButton upVoteButton = view.findViewById(R.id.btnUpvote);
        if (upVoteButton.isChecked()){
            updateVotes(position, -2);
            texts.get(position).upVoted = false;
            texts.get(position).downVoted = true;
        }
        else if (press){
            updateVotes(position, -1);
            texts.get(position).downVoted = true;
        }
        else{
            updateVotes(position, 1);
            texts.get(position).downVoted = false;
        }
        upVoteButton.setChecked(false);
    }

    // Update the message's votes in the database
    private void updateVotes(int position, int updateScore){
        texts.get(position).setVotes(texts.get(position).getVotes() + updateScore);
        adapter.notifyItemChanged(position);
        String url = "https://lamp.ms.wits.ac.za/home/s2584540/newMsgVote.php";

        HttpUrl.Builder urlBuilder = HttpUrl.parse(url).newBuilder();
        urlBuilder.addQueryParameter("USERNAME", CURRENT_USER_USERNAME);
        urlBuilder.addQueryParameter("VALUE", Integer.toString(updateScore));
        urlBuilder.addQueryParameter("MSG_ID", Integer.toString(texts.get(position).getID()));
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
            }
        });

    }

    public void createNewText(View view) {
        Intent intent = new Intent(this, CreateNewMessage.class);
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1){
            if (resultCode == RESULT_OK){
                String result = data.getStringExtra("TEXT");
                texts.add(new MessageClass(CURRENT_USER_USERNAME, result, avatars[CURRENT_USER_AVATAR]));
                adapter.notifyItemInserted(texts.size() - 1);
                sendNewText(result);
            }
        }
        else if (requestCode == 2){ // Perhaps not needed
            if (resultCode == RESULT_OK){
                String result = data.getStringExtra("REPLY");
            }
        }
    }

}