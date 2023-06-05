package com.example.loginscreen;

import android.view.View;

public interface ReplyRecyclerInterface {
    void onTextClick(int position);
    void onReplyClick(int position);
    void onUpvote(int position, boolean press, View view);
    void onDownVote(int position, boolean press, View view);
}

