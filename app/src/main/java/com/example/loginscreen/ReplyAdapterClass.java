package com.example.loginscreen;
import android.content.Context;
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

public class ReplyAdapterClass extends RecyclerView.Adapter<ReplyAdapterClass.MyViewHolder>{

    private final ReplyRecyclerInterface replyRecyclerInterface;
    Context context;
    ArrayList<ReplyClass> texts;

    public ReplyAdapterClass(Context context, ArrayList<ReplyClass> texts, ReplyRecyclerInterface recyclerInterface){
        this.context = context;
        this.texts = texts;
        this.replyRecyclerInterface = recyclerInterface;
    }

    @NonNull
    @Override
    public ReplyAdapterClass.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Give the text its look
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.recycler_row, parent, false);
        return new ReplyAdapterClass.MyViewHolder(view, replyRecyclerInterface);
    }

    // Give each message its value
    @Override
    public void onBindViewHolder(@NonNull ReplyAdapterClass.MyViewHolder holder, int position) {
        holder.username.setText(texts.get(position).getUsername());
        holder.text.setText(texts.get(position).getReply());
        holder.avatar.setImageResource(texts.get(position).getAvatar());

        int numVotes = Math.max(0, texts.get(position).getVotes());
        holder.votesText.setText(String.valueOf(numVotes));

        holder.upVoteButton.setChecked(texts.get(position).upVoted);
        holder.downVoteButton.setChecked(texts.get(position).downVoted);
    }

    // Total number of texts
    @Override
    public int getItemCount() {
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

        public MyViewHolder(@NonNull View itemView, ReplyRecyclerInterface recyclerInterface) {
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
        }
    }
}

