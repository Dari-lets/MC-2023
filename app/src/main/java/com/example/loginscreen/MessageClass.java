package com.example.loginscreen;

import java.util.ArrayList;

public class MessageClass {
    // Main text message
    String username = "";
    String text = "";
    int avatar = 0;
    int votes = 0;

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    int ID = 0;

    boolean upVoted = false;
    boolean downVoted = false;

    public int getVotes() {
        return votes;
    }

    public void setVotes(int votes) {
        this.votes = votes;
    }


    public MessageClass(String username, String text, int avatar) {
        this.username = username;
        this.text = text;
        this.avatar = avatar;
    }

    public String getUsername() {
        return username;
    }

    public String getText() {
        return text;
    }

    public int getAvatar() {
        return avatar;
    }

    public ReplyClass getReply(int position){
        return replies.get(position);
    }

    // all the replies
    ArrayList<ReplyClass> replies;

    public ArrayList<ReplyClass> getReplies() {
        return replies;
    }

    public void setReplies(ArrayList<ReplyClass> replies) {
        this.replies = replies;
    }


}

