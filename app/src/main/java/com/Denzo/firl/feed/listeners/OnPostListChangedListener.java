package com.Denzo.firl.feed.listeners;

import com.Denzo.firl.feed.model.PostListResult;

public interface OnPostListChangedListener<Post> {

    public void onListChanged(PostListResult result);

    void onCanceled(String message);
}