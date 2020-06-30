package com.Denzo.firl.feed;


import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import androidx.annotation.Nullable;

import com.Denzo.firl.R;
import com.Denzo.firl.feed.utils.Utils;
import com.Denzo.firl.feed.listeners.OnDataChangedListener;
import com.Denzo.firl.feed.listeners.OnObjectExistListener;
import com.Denzo.firl.feed.listeners.OnPostChangedListener;
import com.Denzo.firl.feed.listeners.OnPostCreatedListener;
import com.Denzo.firl.feed.listeners.OnPostListChangedListener;
import com.Denzo.firl.feed.listeners.OnTaskCompleteListener;
import com.Denzo.firl.feed.model.FollowingPost;
import com.Denzo.firl.feed.model.Like;
import com.Denzo.firl.feed.model.Post;
import com.Denzo.firl.feed.utils.ImageUtil;
import com.Denzo.firl.managers.FirebaseListenersManager;
import com.Denzo.firl.feed.utils.*;


public class PostManager extends FirebaseListenersManager {

    private static final String TAG = PostManager.class.getSimpleName();
    private static PostManager instance;
    private int newPostsCounter = 0;
    private PostCounterWatcher postCounterWatcher;
    private PostInteractor postInteractor;

    private Context context;

    public static PostManager getInstance(Context context) {
        if (instance == null) {
            instance = new PostManager(context);
        }

        return instance;
    }

    private PostManager(Context context) {
        this.context = context;
        postInteractor = PostInteractor.getInstance(context);
    }

    public void createOrUpdatePost(Post post) {
        try {
            postInteractor.createOrUpdatePost(post);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }

    public void getPostsList(OnPostListChangedListener<Post> onDataChangedListener, long date) {
        postInteractor.getPostList(onDataChangedListener, date);
    }

    public void getPostsListByUser(OnDataChangedListener<Post> onDataChangedListener, String userId) {
        postInteractor.getPostListByUser(onDataChangedListener, userId);
    }

    public void getPost(Context context, String postId, OnPostChangedListener onPostChangedListener) {
        ValueEventListener valueEventListener = postInteractor.getPost(postId, onPostChangedListener);
        addListenerToMap(context, valueEventListener);
    }

    public void getSinglePostValue(String postId, OnPostChangedListener onPostChangedListener) {
        postInteractor.getSinglePost(postId, onPostChangedListener);
    }

    public void createOrUpdatePostWithImage(Uri imageUri, final OnPostCreatedListener onPostCreatedListener, final Post post) {
        postInteractor.createOrUpdatePostWithImage(imageUri, onPostCreatedListener, post);
    }

    public void removePost(final Post post, final OnTaskCompleteListener onTaskCompleteListener) {
        postInteractor.removePost(post, onTaskCompleteListener);
    }

    public void addComplain(Post post) {
        postInteractor.addComplainToPost(post);
    }

    public void hasCurrentUserLike(Context activityContext, String postId, String userId, final OnObjectExistListener<Like> onObjectExistListener) {
        ValueEventListener valueEventListener = postInteractor.hasCurrentUserLike(postId, userId, onObjectExistListener);
        addListenerToMap(activityContext, valueEventListener);
    }

    public void hasCurrentUserLikeSingleValue(String postId, String userId, final OnObjectExistListener<Like> onObjectExistListener) {
        postInteractor.hasCurrentUserLikeSingleValue(postId, userId, onObjectExistListener);
    }

    public void isPostExistSingleValue(String postId, final OnObjectExistListener<Post> onObjectExistListener) {
        postInteractor.isPostExistSingleValue(postId, onObjectExistListener);
    }

    public void incrementWatchersCount(String postId) {
        postInteractor.incrementWatchersCount(postId);
    }

    public void incrementNewPostsCounter() {
        newPostsCounter++;
        notifyPostCounterWatcher();
    }

    public void clearNewPostsCounter() {
        newPostsCounter = 0;
        notifyPostCounterWatcher();
    }

    public int getNewPostsCounter() {
        return newPostsCounter;
    }

    public void setPostCounterWatcher(PostCounterWatcher postCounterWatcher) {
        this.postCounterWatcher = postCounterWatcher;
    }

    private void notifyPostCounterWatcher() {
        if (postCounterWatcher != null) {
            postCounterWatcher.onPostCounterChanged(newPostsCounter);
        }
    }

    public void getFollowingPosts(String userId, OnDataChangedListener<FollowingPost> listener) {
        FollowInteractor.getInstance(context).getFollowingPosts(userId, listener);
    }

    public void searchByTitle(String searchText, OnDataChangedListener<Post> onDataChangedListener) {
        closeListeners(context);
        ValueEventListener valueEventListener = postInteractor.searchPostsByTitle(searchText, onDataChangedListener);
        addListenerToMap(context, valueEventListener);
    }

    public void filterByLikes(int limit, OnDataChangedListener<Post> onDataChangedListener) {
        closeListeners(context);
        ValueEventListener valueEventListener = postInteractor.filterPostsByLikes(limit, onDataChangedListener);
        addListenerToMap(context, valueEventListener);
    }

    public void loadImageMediumSize(GlideRequests request, String imageTitle, ImageView imageView, @Nullable OnImageRequestListener onImageRequestListener) {
        int width = Utils.getDisplayWidth(context);
        int height = (int) context.getResources().getDimension(R.dimen.post_detail_image_height);

        StorageReference mediumStorageRef = getMediumImageStorageRef(imageTitle);
        StorageReference originalStorageRef = getOriginImageStorageRef(imageTitle);

        ImageUtil.loadMediumImageCenterCrop(request, mediumStorageRef, originalStorageRef, imageView, width, height, new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                if (onImageRequestListener != null) {
                    onImageRequestListener.onImageRequestFinished();
                }
                return false;
            }

            @Override
            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                if (onImageRequestListener != null) {
                    onImageRequestListener.onImageRequestFinished();
                }
                return false;
            }
        });

    }

    public void loadImageMediumSize(GlideRequests request, String imageTitle, ImageView imageView) {
        loadImageMediumSize(request, imageTitle, imageView, null);
    }

    private StorageReference getMediumImageStorageRef(String imageTitle) {
        return postInteractor.getMediumImageStorageRef(imageTitle);
    }

    public StorageReference getSmallImageStorageRef(String imageTitle) {
        return postInteractor.getSmallImageStorageRef(imageTitle);
    }

    public StorageReference getOriginImageStorageRef(String imageTitle) {
        return postInteractor.getOriginImageStorageRef(imageTitle);
    }

    public interface PostCounterWatcher {
        void onPostCounterChanged(int newValue);
    }

    public interface OnImageRequestListener {
        void onImageRequestFinished();
    }
}