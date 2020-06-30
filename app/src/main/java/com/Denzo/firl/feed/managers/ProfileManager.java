package com.Denzo.firl.feed.managers;

import android.content.Context;
import android.net.Uri;
import com.google.firebase.database.ValueEventListener;

import com.Denzo.firl.feed.model.Profile;
import com.Denzo.firl.feed.ProfileInteractor;
import com.Denzo.firl.feed.enums.ProfileStatus;
import com.Denzo.firl.feed.listeners.OnDataChangedListener;
import com.Denzo.firl.feed.listeners.OnObjectChangedListener;
import com.Denzo.firl.feed.listeners.OnObjectExistListener;
import com.Denzo.firl.feed.listeners.OnProfileCreatedListener;
import com.Denzo.firl.feed.utils.PreferencesUtil;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ProfileManager  extends FirebaseListenersManager {

    private static final String TAG = ProfileManager.class.getSimpleName();
    private static ProfileManager instance;

    private Context context;
    private ProfileInteractor profileInteractor;


    public static ProfileManager getInstance(Context context) {
        if (instance == null) {
            instance = new ProfileManager(context);
        }

        return instance;
    }

    private ProfileManager(Context context) {
        this.context = context;
        profileInteractor = ProfileInteractor.getInstance(context);
    }

    public Profile buildProfile(FirebaseUser firebaseUser, String largeAvatarURL) {
        Profile profile = new Profile(firebaseUser.getUid());
        profile.setEmail(firebaseUser.getEmail());
        profile.setUsername(firebaseUser.getDisplayName());
        profile.setPhotoUrl(largeAvatarURL != null ? largeAvatarURL : firebaseUser.getPhotoUrl().toString());
        return profile;
    }

    public void isProfileExist(String id, final OnObjectExistListener<Profile> onObjectExistListener) {
        profileInteractor.isProfileExist(id, onObjectExistListener);
    }

    public void createOrUpdateProfile(Profile profile, OnProfileCreatedListener onProfileCreatedListener) {
        createOrUpdateProfile(profile, null, onProfileCreatedListener);
    }

    public void createOrUpdateProfile(final Profile profile, Uri imageUri, final OnProfileCreatedListener onProfileCreatedListener) {
        if (imageUri == null) {
            profileInteractor.createOrUpdateProfile(profile, onProfileCreatedListener);
        } else {
            profileInteractor.createOrUpdateProfileWithImage(profile, imageUri, onProfileCreatedListener);
        }
    }

    public void getProfileValue(Context activityContext, String id, final OnObjectChangedListener<Profile> listener) {
        ValueEventListener valueEventListener = profileInteractor.getProfile(id, listener);
        addListenerToMap(activityContext, valueEventListener);
    }

    public void getProfileSingleValue(String id, final OnObjectChangedListener<Profile> listener) {
        profileInteractor.getProfileSingleValue(id, listener);
    }

    public ProfileStatus checkProfile() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user == null) {
            return ProfileStatus.NOT_AUTHORIZED;
        } else if (!PreferencesUtil.isProfileCreated(context)) {
            return ProfileStatus.NO_PROFILE;
        } else {
            return ProfileStatus.PROFILE_CREATED;
        }
    }

    public void search(String searchText, OnDataChangedListener<Profile> onDataChangedListener) {
        closeListeners(context);
        ValueEventListener valueEventListener = profileInteractor.searchProfiles(searchText, onDataChangedListener);
        addListenerToMap(context, valueEventListener);
    }

    public void addRegistrationToken(String token, String userId) {
        profileInteractor.addRegistrationToken(token, userId);
    }
}
