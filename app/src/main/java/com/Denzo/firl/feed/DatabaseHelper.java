package com.Denzo.firl.feed;


import android.content.Context;
import android.net.Uri;

import com.Denzo.firl.Constants;
import com.Denzo.firl.R;
import android.util.Log;

import com.Denzo.firl.feed.utils.LogUtil;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;

public class DatabaseHelper {

    public static final String TAG = DatabaseHelper.class.getSimpleName();

    private static DatabaseHelper instance;

    public static final String POSTS_DB_KEY = "posts";
    public static final String PROFILES_DB_KEY = "profiles";
    public static final String POST_COMMENTS_DB_KEY = "post-comments";
    public static final String POST_LIKES_DB_KEY = "post-likes";
    public static final String FOLLOW_DB_KEY = "follow";
    public static final String FOLLOWINGS_DB_KEY = "followings";
    public static final String FOLLOWINGS_POSTS_DB_KEY = "followingPostsIds";
    public static final String FOLLOWERS_DB_KEY = "followers";
    public static final String IMAGES_STORAGE_KEY = "images";
    public static final String IMAGES_MEDIUM_KEY = "medium";
    public static final String IMAGES_SMALL_KEY = "small";

    private Context context;
    private FirebaseDatabase database;
    FirebaseStorage storage;
    private Map<ValueEventListener, DatabaseReference> activeListeners = new HashMap<>();

    public static DatabaseHelper getInstance(Context context) {
        if (instance == null) {
            instance = new DatabaseHelper(context);
        }

        return instance;
    }

    private DatabaseHelper(Context context) {
        this.context = context;
    }

    public void init() {
        database = FirebaseDatabase.getInstance();
        database.setPersistenceEnabled(true);
        storage = FirebaseStorage.getInstance();

//        Sets the maximum time to retry upload operations if a failure occurs.
        storage.setMaxUploadRetryTimeMillis(Constants.Database.MAX_UPLOAD_RETRY_MILLIS);
    }

    public StorageReference getStorageReference() {
        return storage.getReferenceFromUrl(context.getResources().getString(R.string.storage_link));
    }

    public DatabaseReference getDatabaseReference() {
        return database.getReference();
    }

    public void closeListener(ValueEventListener listener) {
        if (activeListeners.containsKey(listener)) {
            DatabaseReference reference = activeListeners.get(listener);
            reference.removeEventListener(listener);
            activeListeners.remove(listener);
            LogUtil.logDebug(TAG, "closeListener(), listener was removed: " + listener);
        } else {
            LogUtil.logDebug(TAG, "closeListener(), listener not found :" + listener);
        }
    }

    public void closeAllActiveListeners() {
        for (ValueEventListener listener : activeListeners.keySet()) {
            DatabaseReference reference = activeListeners.get(listener);
            reference.removeEventListener(listener);
        }

        activeListeners.clear();
    }

    public void addActiveListener(ValueEventListener listener, DatabaseReference reference) {
        activeListeners.put(listener, reference);
    }

    public Task<Void> removeImage(String imageTitle) {
        StorageReference desertRef = getStorageReference().child(IMAGES_STORAGE_KEY + "/" + imageTitle);
        return desertRef.delete();
    }

    public StorageReference getOriginImageStorageRef(String imageTitle) {
        return getStorageReference().child(IMAGES_STORAGE_KEY).child(imageTitle);
    }

    public StorageReference getMediumImageStorageRef(String imageTitle) {
        return getStorageReference().child(IMAGES_STORAGE_KEY).child(IMAGES_MEDIUM_KEY).child(imageTitle);
    }

    public StorageReference getSmallImageStorageRef(String imageTitle) {
        return getStorageReference().child(IMAGES_STORAGE_KEY).child(IMAGES_SMALL_KEY).child(imageTitle);
    }

    public UploadTask uploadImage(Uri uri, String imageTitle) {
        StorageReference riversRef = getStorageReference().child(IMAGES_STORAGE_KEY + "/" + imageTitle);
        // Create file metadata including the content type
        StorageMetadata metadata = new StorageMetadata.Builder()
                .setCacheControl("max-age=7776000, Expires=7776000, public, must-revalidate")
                .build();

        return riversRef.putFile(uri, metadata);
    }

}
