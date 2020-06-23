package com.Denzo.firl.feed.listeners;

public interface OnPostListChangedListener<Post> {

    public void onListChanged(PostListResult result);

    void onCanceled(String message);
}