package com.example.mcproject;

public class ReplyClass {
    String reply;
    String username;
    int avatar;

    public ReplyClass(String reply, String username, int avatar) {
        this.reply = reply;
        this.username = username;
        this.avatar = avatar;
    }


    public String getReply() {
        return reply;
    }

    public String getUsername() {
        return username;
    }

    public int getAvatar() {
        return avatar;
    }
}
