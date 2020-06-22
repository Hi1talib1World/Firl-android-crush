package com.Denzo.firl.Login;


import com.Denzo.firl.feed.BaseView;
import com.google.firebase.auth.AuthCredential;

public interface LoginView extends BaseView {
    void startCreateProfileActivity();

    void signInWithGoogle();

    void signInWithFacebook();

    void setProfilePhotoUrl(String url);

    void firebaseAuthWithCredentials(AuthCredential credential);
}