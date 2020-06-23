package com.Denzo.firl.feed.listeners;

public interface OnObjectChangedListener<T> {

    void onObjectChanged(T obj);

    void onError(String errorText);
}