package com.Denzo.firl.feed;


import android.app.Activity;
import android.content.Context;

import com.Denzo.firl.ApplicationHelper;
import com.Denzo.firl.feed.listeners.OnCountChangedListener;
import com.Denzo.firl.feed.listeners.OnDataChangedListener;
import com.Denzo.firl.feed.listeners.OnObjectExistListener;
import com.Denzo.firl.feed.listeners.OnRequestComplete;
import com.Denzo.firl.feed.model.FollowingPost;
import com.Denzo.firl.feed.utils.LogUtil;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FollowInteractor {

    private static final String TAG = FollowInteractor.class.getSimpleName();
    private static FollowInteractor instance;

    private DatabaseHelper databaseHelper;
    private Context context;

    public static FollowInteractor getInstance(Context context) {
        if (instance == null) {
            instance = new FollowInteractor(context);
        }

        return instance;
    }

    private FollowInteractor(Context context) {
        this.context = context;
        databaseHelper = ApplicationHelper.getDatabaseHelper();
    }

    public void getFollowingPosts(String userId, OnDataChangedListener<FollowingPost> listener) {
        databaseHelper
                .getDatabaseReference()
                .child(DatabaseHelper.FOLLOWINGS_POSTS_DB_KEY)
                .child(userId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        List<FollowingPost> list = new ArrayList<>();
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            FollowingPost followingPost = snapshot.getValue(FollowingPost.class);
                            list.add(followingPost);
                        }

                        Collections.reverse(list);

                        listener.onListChanged(list);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        LogUtil.logDebug(TAG, "getFollowingPosts, onCancelled");
                    }
                });
    }

    private DatabaseReference getFollowersRef(String userId) {
        return databaseHelper
                .getDatabaseReference()
                .child(DatabaseHelper.FOLLOW_DB_KEY)
                .child(userId)
                .child(DatabaseHelper.FOLLOWERS_DB_KEY);
    }

    private DatabaseReference getFollowingsRef(String userId) {
        return databaseHelper
                .getDatabaseReference()
                .child(DatabaseHelper.FOLLOW_DB_KEY)
                .child(userId)
                .child(DatabaseHelper.FOLLOWINGS_DB_KEY);
    }

    public void getFollowersList(String targetUserId, OnDataChangedListener<String> onDataChangedListener) {
        getFollowersRef(targetUserId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<String> list = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Follower follower = snapshot.getValue(Follower.class);

                    if (follower != null) {
                        String profileId = follower.getProfileId();
                        list.add(profileId);
                    }

                }
                onDataChangedListener.onListChanged(list);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                LogUtil.logDebug(TAG, "getFollowersList, onCancelled");
            }
        });
    }

    public void getFollowingsList(String targetUserId, OnDataChangedListener<String> onDataChangedListener) {
        getFollowingsRef(targetUserId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<String> list = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Following following = snapshot.getValue(Following.class);

                    if (following != null) {
                        String profileId = following.getProfileId();
                        list.add(profileId);
                    }

                }
                onDataChangedListener.onListChanged(list);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                LogUtil.logDebug(TAG, "getFollowingsList, onCancelled");
            }
        });
    }

    public ValueEventListener getFollowingsCount(String targetUserId, OnCountChangedListener onCountChangedListener) {
        return getFollowingsRef(targetUserId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                onCountChangedListener.onCountChanged(dataSnapshot.getChildrenCount());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                LogUtil.logDebug(TAG, "getFollowingsCount, onCancelled");
            }
        });
    }

    public ValueEventListener getFollowersCount(String targetUserId, OnCountChangedListener onCountChangedListener) {
        return getFollowersRef(targetUserId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                onCountChangedListener.onCountChanged(dataSnapshot.getChildrenCount());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                LogUtil.logDebug(TAG, "getFollowersCount, onCancelled");
            }
        });
    }

    public void isFollowingExist(String followerUserId, String followingUserId, final OnObjectExistListener onObjectExistListener) {
        DatabaseReference followingRef = databaseHelper
                .getDatabaseReference()
                .child(DatabaseHelper.FOLLOW_DB_KEY)
                .child(followerUserId)
                .child(DatabaseHelper.FOLLOWINGS_DB_KEY)
                .child(followingUserId);

        DatabaseReference followerRef = databaseHelper
                .getDatabaseReference()
                .child(DatabaseHelper.FOLLOW_DB_KEY)
                .child(followingUserId)
                .child(DatabaseHelper.FOLLOWERS_DB_KEY)
                .child(followerUserId);


        followingRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    followerRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            onObjectExistListener.onDataChanged(dataSnapshot.exists());
                            LogUtil.logDebug(TAG, "isFollowerExist for" + followingUserId + ", success: " + dataSnapshot.exists());
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                } else {
                    onObjectExistListener.onDataChanged(false);
                    LogUtil.logDebug(TAG, "isFollowingExist for" + followerUserId + ", success: " + dataSnapshot.exists());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                LogUtil.logDebug(TAG, "isFollowingExist, onCancelled");
            }
        });
    }

    private Task<Void> addFollowing(String followerUserId, String followingUserId) {
        Following following = new Following(followingUserId);
        return databaseHelper
                .getDatabaseReference()
                .child(DatabaseHelper.FOLLOW_DB_KEY)
                .child(followerUserId)
                .child(DatabaseHelper.FOLLOWINGS_DB_KEY)
                .child(followingUserId)
                .setValue(following);
    }

    private Task<Void> addFollower(String followerUserId, String followingUserId) {
        Follower follower = new Follower(followerUserId);
        return databaseHelper
                .getDatabaseReference()
                .child(DatabaseHelper.FOLLOW_DB_KEY)
                .child(followingUserId)
                .child(DatabaseHelper.FOLLOWERS_DB_KEY)
                .child(followerUserId)
                .setValue(follower);
    }

    private Task<Void> removeFollowing(String followerUserId, String followingUserId) {
        return databaseHelper
                .getDatabaseReference()
                .child(DatabaseHelper.FOLLOW_DB_KEY)
                .child(followerUserId)
                .child(DatabaseHelper.FOLLOWINGS_DB_KEY)
                .child(followingUserId)
                .removeValue();
    }

    private Task<Void> removeFollower(String followerUserId, String followingUserId) {
        return databaseHelper
                .getDatabaseReference()
                .child(DatabaseHelper.FOLLOW_DB_KEY)
                .child(followingUserId)
                .child(DatabaseHelper.FOLLOWERS_DB_KEY)
                .child(followerUserId).removeValue();
    }

    public void unfollowUser(Activity activity, String followerUserId, String followingUserId, final OnRequestComplete onRequestComplete) {
        removeFollowing(followerUserId, followingUserId)
                .continueWithTask(task -> removeFollower(followerUserId, followingUserId))
                .addOnCompleteListener(activity, task -> {
                    onRequestComplete.onComplete(task.isSuccessful());
                    LogUtil.logDebug(TAG, "unfollowUser " + followingUserId + ", success: " + task.isSuccessful());
                });

    }

    public void followUser(Activity activity, String followerUserId, String followingUserId, final OnRequestComplete onRequestComplete) {
        addFollowing(followerUserId, followingUserId)
                .continueWithTask(task -> addFollower(followerUserId, followingUserId))
                .addOnCompleteListener(activity, task -> {
                    onRequestComplete.onComplete(task.isSuccessful());
                    LogUtil.logDebug(TAG, "followUser " + followingUserId + ", success: " + task.isSuccessful());
                });
    }
}