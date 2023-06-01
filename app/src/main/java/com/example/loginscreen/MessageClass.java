package com.example.loginscreen;

import java.util.ArrayList;

public class MessageClass {
    // Main text message
    String username;
    String text;
    int avatar;
    int votes = 0;

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

    // all the replies
    ArrayList<ReplyClass> replies;

    public ArrayList<ReplyClass> getReplies() {
        return replies;
    }

    public void setReplies(ArrayList<ReplyClass> replies) {
        this.replies = replies;
    }


}

