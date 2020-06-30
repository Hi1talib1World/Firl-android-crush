package com.Denzo.firl.feed.utils;


import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import com.bumptech.glide.Glide;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.iid.FirebaseInstanceId;
import com.Denzo.firl.feed.ProfileInteractor;
import com.bumptech.glide.Glide;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.iid.FirebaseInstanceId;


public class LogoutHelper {

    private static final String TAG = LogoutHelper.class.getSimpleName();
    private static ClearImageCacheAsyncTask clearImageCacheAsyncTask;

    public static void signOut(GoogleApiClient mGoogleApiClient, FragmentActivity fragmentActivity) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            ProfileInteractor.getInstance(fragmentActivity.getApplicationContext())
                    .removeRegistrationToken(FirebaseInstanceId.getInstance().getToken(), user.getUid());

            for (UserInfo profile : user.getProviderData()) {
                String providerId = profile.getProviderId();
                logoutByProvider(providerId, mGoogleApiClient, fragmentActivity);
            }
            logoutFirebase(fragmentActivity.getApplicationContext());
        }

        if (clearImageCacheAsyncTask == null) {
            clearImageCacheAsyncTask = new ClearImageCacheAsyncTask(fragmentActivity.getApplicationContext());
            clearImageCacheAsyncTask.execute();
        }
    }

    private static void logoutByProvider(String providerId, GoogleApiClient mGoogleApiClient, FragmentActivity fragmentActivity) {
        switch (providerId) {
            case GoogleAuthProvider.PROVIDER_ID:
                logoutGoogle(mGoogleApiClient, fragmentActivity);
                break;

            case FacebookAuthProvider.PROVIDER_ID:
                logoutFacebook(fragmentActivity.getApplicationContext());
                break;
        }
    }

    private static void logoutFirebase(Context context) {
        FirebaseAuth.getInstance().signOut();
        PreferencesUtil.setProfileCreated(context, false);
    }

    private static void logoutFacebook(Context context) {
        FacebookSdk.sdkInitialize(context);
        LoginManager.getInstance().logOut();
    }

    private static void logoutGoogle(GoogleApiClient mGoogleApiClient, FragmentActivity fragmentActivity) {
        if (mGoogleApiClient == null) {
            mGoogleApiClient = GoogleApiHelper.createGoogleApiClient(fragmentActivity);
        }

        if (!mGoogleApiClient.isConnected()) {
            mGoogleApiClient.connect();
        }

        final GoogleApiClient finalMGoogleApiClient = mGoogleApiClient;
        mGoogleApiClient.registerConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
            @Override
            public void onConnected(@Nullable Bundle bundle) {
                if (finalMGoogleApiClient.isConnected()) {
                    Auth.GoogleSignInApi.signOut(finalMGoogleApiClient).setResultCallback(new ResultCallback<Status>() {
                        @Override
                        public void onResult(@NonNull Status status) {
                            if (status.isSuccess()) {
                                LogUtil.logDebug(TAG, "User Logged out from Google");
                            } else {
                                LogUtil.logDebug(TAG, "Error Logged out from Google");
                            }
                        }
                    });
                }
            }

            @Override
            public void onConnectionSuspended(int i) {
                LogUtil.logDebug(TAG, "Google API Client Connection Suspended");
            }
        });
    }

    private static class ClearImageCacheAsyncTask extends AsyncTask<Void, Void, Void> {
        private Context context;

        public ClearImageCacheAsyncTask(Context context) {
            this.context = context;
        }

        @Override
        protected Void doInBackground(Void... params) {
            Glide.get(context.getApplicationContext()).clearDiskCache();
            return null;
        }

        @Override
        protected void onPostExecute(Void o) {
            super.onPostExecute(o);
            clearImageCacheAsyncTask = null;
            Glide.get(context.getApplicationContext()).clearMemory();
        }
    }
}