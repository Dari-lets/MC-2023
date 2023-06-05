package com.example.loginscreen;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MessageAdapterClass extends RecyclerView.Adapter<MessageAdapterClass.MyViewHolder>{

    private final RecyclerInterface recyclerInterface;
    Context context;
    ArrayList<MessageClass> texts;

    public MessageAdapterClass(Context context, ArrayList<MessageClass> texts, RecyclerInterface recyclerInterface){
        this.context = context;
        this.texts = texts;
        this.recyclerInterface = recyclerInterface;
    }

    @NonNull
    @Override
    public MessageAdapterClass.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Give the text its look
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.recycler_row, parent, false);
        return new MessageAdapterClass.MyViewHolder(view, recyclerInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageAdapterClass.MyViewHolder holder, int position) {
        // Give each message its value
        holder.username.setText(texts.get(position).getUsername());
        holder.text.setText(texts.get(position).getText());
        holder.avatar.setImageResource(texts.get(position).getAvatar());
        //
        int numVotes = Math.max(0, texts.get(position).getVotes());
        holder.votesText.setText(String.valueOf(numVotes));

        holder.upVoteButton.setChecked(texts.get(position).upVoted);
        holder.downVoteButton.setChecked(texts.get(position).downVoted);
    }

    @Override
    public int getItemCount() {
        // Total number of texts
        return texts.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        ImageView avatar;
        TextView username;
        TextView text;
        ImageButton replyButton;
        TextView votesText;
        ToggleButton upVoteButton;
        ToggleButton downVoteButton;

        public MyViewHolder(@NonNull View itemView, RecyclerInterface recyclerInterface) {
            super(itemView);
            avatar = itemView.findViewById(R.id.imageView);
            username = itemView.findViewById(R.id.userName);
            text = itemView.findViewById(R.id.mainText);
            replyButton = itemView.findViewById(R.id.btnReply);
            votesText = itemView.findViewById(R.id.txtVotes);
            upVoteButton = itemView.findViewById(R.id.btnUpvote);
            downVoteButton = itemView.findViewById(R.id.btnDownvote);




            // Clicking on the reply button
            replyButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (recyclerInterface != null){
                        int position = getAdapterPosition();

                        if (position != RecyclerView.NO_POSITION){
                            recyclerInterface.onReplyClick(position);
                        }
                    }
                }
            });

            // The message's upvote button is pressed
            upVoteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (recyclerInterface != null){
                        int position = getAdapterPosition();

                        if (position != RecyclerView.NO_POSITION){
                            recyclerInterface.onUpvote(position, upVoteButton.isChecked(), itemView);
                        }
                    }
                }
            });

            // The message's downvote button is pressed
            downVoteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (recyclerInterface != null){
                        int position = getAdapterPosition();

                        if (position != RecyclerView.NO_POSITION){
                            recyclerInterface.onDownVote(position, downVoteButton.isChecked(), itemView);
                        }
                    }
                }
            });

            // Clicking the message itself. Should take you to the replies
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (recyclerInterface != null){
                        int position = getAdapterPosition();

                        if (position != RecyclerView.NO_POSITION){
                            recyclerInterface.onTextClick(position);
                        }
                    }
                }
            });
        }
    }
}

