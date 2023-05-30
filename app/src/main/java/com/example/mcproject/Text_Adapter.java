package com.example.mcproject;
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

public class Text_Adapter extends RecyclerView.Adapter<Text_Adapter.MyViewHolder>{

    private final RecyclerInterface recyclerInterface;
    Context context;
    ArrayList<TextClass> texts;

    public Text_Adapter(Context context, ArrayList<TextClass> texts, RecyclerInterface recyclerInterface){
        this.context = context;
        this.texts = texts;
        this.recyclerInterface = recyclerInterface;
    }

    @NonNull
    @Override
    public Text_Adapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Give the text its look
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.recycler_row, parent, false);
        return new Text_Adapter.MyViewHolder(view, recyclerInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull Text_Adapter.MyViewHolder holder, int position) {
        // Give each message its value
        holder.username.setText(texts.get(position).getUsername());
        holder.text.setText(texts.get(position).getText());
        holder.avatar.setImageResource(texts.get(position).getAvatar());
        //
        holder.votesText.setText(String.valueOf(texts.get(position).getVotes()));
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
        Button replyButton;
        TextView votesText;

        public MyViewHolder(@NonNull View itemView, RecyclerInterface recyclerInterface) {
            super(itemView);
            avatar = itemView.findViewById(R.id.imageView);
            username = itemView.findViewById(R.id.userName);
            text = itemView.findViewById(R.id.mainText);
            replyButton = itemView.findViewById(R.id.btnReply);
            votesText = itemView.findViewById(R.id.txtVotes);

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
