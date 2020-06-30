package com.Denzo.firl.feed.utils;


import androidx.fragment.app.FragmentActivity;

import com.Denzo.firl.R;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApiClient;

public class GoogleApiHelper {

    public static GoogleApiClient createGoogleApiClient(FragmentActivity fragmentActivity) {
        GoogleApiClient.OnConnectionFailedListener failedListener;

        if (fragmentActivity instanceof GoogleApiClient.OnConnectionFailedListener) {
            failedListener = (GoogleApiClient.OnConnectionFailedListener) fragmentActivity;
        } else {
            throw new IllegalArgumentException(fragmentActivity.getClass().getSimpleName() + " should implement OnConnectionFailedListener");
        }

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(fragmentActivity.getResources().getString(R.string.google_web_client_id))
                .requestEmail()
                .build();

        return new GoogleApiClient.Builder(fragmentActivity)
                .enableAutoManage(fragmentActivity, failedListener)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
    }
}