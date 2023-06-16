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
import android.widget.ToggleButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ReplyActivity extends AppCompatActivity implements ReplyRecyclerInterface{

    static MessageClass originalMessage;
    static ArrayList<ReplyClass> replies = new ArrayList<ReplyClass>();
    ReplyAdapterClass adapter;
    ImageView RmessageAvatar;
    TextView RmessageUsername;
    TextView RmessageContent;

    int originalMessageIndex;

    int[] avatars = {R.drawable.baseline_sports_esports_24, R.drawable.baseline_diamond_24,
            R.drawable.face, R.drawable.baseline_self_improvement_24, R.drawable.moon,
            R.drawable.pokemon};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reply);

        replies.clear();

        // Crashes the app
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        //this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //getSupportActionBar().hide();

        RmessageAvatar = (ImageView) findViewById(R.id.messageAvatar);
        RmessageUsername = (TextView) findViewById(R.id.messageUsername);
        RmessageContent = (TextView) findViewById(R.id.messageContent);

        // Initialise original message
        originalMessageIndex = getIntent().getIntExtra("MessageIndex", 0);
        originalMessage = HomePage.getMessage(originalMessageIndex);
        RmessageAvatar.setImageResource(originalMessage.getAvatar());
        RmessageUsername.setText(originalMessage.getUsername());
        RmessageContent.setText(originalMessage.getText());

        // Create recycler view for the Reply page
        RecyclerView replyRecyclerView = findViewById(R.id.replyRecycler);
        replyRecyclerView.setItemAnimator(null);

        loadReplies();
        // Finishing steps
        adapter = new ReplyAdapterClass(this, replies, this);
        replyRecyclerView.setAdapter(adapter);
        replyRecyclerView.setLayoutManager(new LinearLayoutManager(this));

    }

    private void loadReplies(){
        replies.clear();
        String url = "https://lamp.ms.wits.ac.za/home/s2549501/loadReplies.php";
        HttpUrl.Builder urlBuilder = HttpUrl.parse(url).newBuilder();
        urlBuilder.addQueryParameter("MSG_ID", Integer.toString(originalMessage.getID()));
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
                            String username = arrayObject.getString("USERNAME");
                            String text = arrayObject.getString("MESSAGE");
                            int avatar = arrayObject.getInt("AVATAR");
                            int votes = arrayObject.getInt("UPVOTES");
                            ReplyClass newReply = new ReplyClass(username, text, avatars[avatar]);
                            newReply.setReplyKey(arrayObject.getInt("REPLY_ID"));
                            newReply.setOrignalMessageKey(originalMessage.getID());
                            newReply.setVotes(votes);
                            replies.add(newReply);
                        }
                        ReplyActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                loadReplyVotes();
                                adapter.notifyDataSetChanged();
                            }
                        });
                        //};

                    } catch (JSONException e){
                        e.printStackTrace();
                    }

                }
            }
        });

    }

    private void loadReplyVotes(){
        String url = "https://lamp.ms.wits.ac.za/home/s2549501/replyVotes.php";

        HttpUrl.Builder urlBuilder = HttpUrl.parse(url).newBuilder();
        urlBuilder.addQueryParameter("USERNAME", HomePage.CURRENT_USER_USERNAME);
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
                            int replyKey = arrayObject.getInt("REPLY_ID");
                            int replyVote = arrayObject.getInt("VOTE");
                            for(ReplyClass reply : replies){
                                if (reply.getReplyKey() == replyKey){
                                    if (replyVote == 1)
                                        reply.upVoted = true;
                                    else if (replyVote == -1)
                                        reply.downVoted = true;
                                }
                            }

                        }
                        ReplyActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                adapter.notifyDataSetChanged();
                            }
                        });
                        //};

                    } catch (JSONException e){
                        e.printStackTrace();
                    }

                }
            }
        });
    }

    public static ReplyClass getReply(int position){
        return replies.get(position);
    }

    // Reply is clicked.
    @Override
    public void onTextClick(int position) {
    }

    // Reply's reply button is clicked
    @Override
    public void onReplyClick(int position) {
        Intent intent = new Intent(this, NewReplyActivity.class);
        intent.putExtra("MESSAGE_TYPE", HomePage.REPLY_TYPE);
        intent.putExtra("POSITION", position);
        intent.putExtra("ORIGINALKEY", originalMessageIndex);
        startActivityForResult(intent, 2);
    }

    // Reply is upVoted. True if pressed. False if unpressed
    @Override
    public void onUpvote(int position, boolean press, View view) {
        ToggleButton downVoteButton = view.findViewById(R.id.btnDownvote);
        if (downVoteButton.isChecked()){
            updateVotes(position, 2);
            replies.get(position).downVoted = false;
            replies.get(position).upVoted = true;
        }
        else if (press){
            updateVotes(position, 1);
            replies.get(position).upVoted = true;
        }
        else{
            updateVotes(position, -1);
            replies.get(position).upVoted = false;
        }
        downVoteButton.setChecked(false);
    }

    // Reply is downVoted. True if pressed. False if unpressed
    @Override
    public void onDownVote(int position, boolean press, View view) {
        ToggleButton upVoteButton = view.findViewById(R.id.btnUpvote);
        if (upVoteButton.isChecked()){
            updateVotes(position, -2);
            replies.get(position).upVoted = false;
            replies.get(position).downVoted = true;
        }
        else if (press){
            updateVotes(position, -1);
            replies.get(position).downVoted = true;
        }
        else{
            updateVotes(position, 1);
            replies.get(position).downVoted = false;
        }
        upVoteButton.setChecked(false);
    }

    // Update the reply's votes in the database
    private void updateVotes(int position, int updateScore){

        replies.get(position).setVotes(replies.get(position).getVotes() + updateScore);
        adapter.notifyItemChanged(position);
        String url = "https://lamp.ms.wits.ac.za/home/s2549501/newReplyVote.php ";

        HttpUrl.Builder urlBuilder = HttpUrl.parse(url).newBuilder();
        urlBuilder.addQueryParameter("USERNAME", HomePage.CURRENT_USER_USERNAME);
        urlBuilder.addQueryParameter("VALUE", Integer.toString(updateScore));
        urlBuilder.addQueryParameter("REPLY_ID", Integer.toString(replies.get(position).getReplyKey()));
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

    public void createNewReply(View view) {
        Intent intent = new Intent(this, NewReplyActivity.class);
        intent.putExtra("MESSAGE_TYPE", HomePage.MESSAGE_TYPE);
        intent.putExtra("POSITION", originalMessageIndex);
        startActivityForResult(intent, 2);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 2){
            if (resultCode == RESULT_OK){
                String result = data.getStringExtra("REPLY");
                Toast.makeText(this, result, Toast.LENGTH_SHORT).show();
                ReplyClass newReply = new ReplyClass(HomePage.CURRENT_USER_USERNAME, result, avatars[HomePage.CURRENT_USER_AVATAR]);
                newReply.setOrignalMessageKey(originalMessage.getID());
                replies.add(newReply);

                adapter.notifyItemInserted(replies.size() - 1);
                loadReplies();
            }
        }
    }
}