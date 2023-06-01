package com.example.loginscreen;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

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

//    @Override
//    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
//
//    }

    // Give each message its value
    @Override
    public void onBindViewHolder(@NonNull ReplyAdapterClass.MyViewHolder holder, int position) {
        holder.username.setText(texts.get(position).getUsername());
        holder.text.setText(texts.get(position).getReply());
        holder.avatar.setImageResource(texts.get(position).getAvatar());
        holder.votesText.setText(String.valueOf(texts.get(position).getVotes()));
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
        Button replyButton;
        TextView votesText;

        public MyViewHolder(@NonNull View itemView, ReplyRecyclerInterface recyclerInterface) {
            super(itemView);
            avatar = itemView.findViewById(R.id.imageView);
            username = itemView.findViewById(R.id.userName);
            text = itemView.findViewById(R.id.mainText);
            replyButton = itemView.findViewById(R.id.btnReply);
            votesText = itemView.findViewById(R.id.txtVotes);


            // Clicking on the reply button
            replyButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (recyclerInterface != null){
                        int position = getAdapterPosition();
                        System.out.println("HIIIIIIII");
                        if (position != RecyclerView.NO_POSITION){
                            recyclerInterface.onReplyClick(position);
                        }
                    }
                }
            });

            // Clicking the message itself. Should do nothing.
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (recyclerInterface != null){
                        int position = getAdapterPosition();

                        if (position != RecyclerView.NO_POSITION){
                            //recyclerInterface.onTextClick(position);

                        }
                    }
                }
            });
        }
    }
}

