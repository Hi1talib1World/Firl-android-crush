package com.Denzo.firl.feed.listeners;

import com.Denzo.firl.feed.model.Post;

public interface OnPostChangedListener {
    public void onObjectChanged(Post obj);

    public void onError(String errorText);
}