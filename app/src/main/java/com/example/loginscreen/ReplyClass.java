package com.example.loginscreen;

public class ReplyClass {
    String reply;
    String username;
    int avatar;

    boolean upVote = false;
    boolean downVote = false;

    public int getReplyKey() {
        return replyKey;
    }

    public void setReplyKey(int replyKey) {
        this.replyKey = replyKey;
    }

    int replyKey;

    public int getOrignalMessageKey() {
        return orignalMessageKey;
    }

    public void setOrignalMessageKey(int orignalMessageKey) {
        this.orignalMessageKey = orignalMessageKey;
    }

    int orignalMessageKey;

    boolean upVoted = false;
    boolean downVoted = false;

    public int getVotes() {
        return votes;
    }

    public void setVotes(int votes) {
        this.votes = votes;
    }

    int votes = 0;

    public ReplyClass(String username, String reply, int avatar) {
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
