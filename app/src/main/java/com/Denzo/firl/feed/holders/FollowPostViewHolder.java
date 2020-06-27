package com.Denzo.firl.feed.holders;


import android.content.DialogInterface;
import android.view.View;

import com.Denzo.firl.feed.BaseActivity;
import com.Denzo.firl.feed.listeners.OnPostChangedListener;
import com.Denzo.firl.feed.model.FollowingPost;
import com.Denzo.firl.feed.model.Post;
import com.Denzo.firl.feed.utils.LogUtil;

import static com.Denzo.firl.feed.PostsAdapter.TAG;

/**
 * Created by Alexey on 22.05.18.
 */
public class FollowPostViewHolder extends PostViewHolder {


    public FollowPostViewHolder(View view, DialogInterface.OnClickListener onClickListener, BaseActivity activity) {
        super(view, onClickListener, activity);
    }

    public FollowPostViewHolder(View view, DialogInterface.OnClickListener onClickListener, BaseActivity activity, boolean isAuthorNeeded) {
        super(view, onClickListener, activity, isAuthorNeeded);
    }

    public void bindData(FollowingPost followingPost) {
        postManager.getSinglePostValue(followingPost.getPostId(), new OnPostChangedListener() {
            @Override
            public void onObjectChanged(Post obj) {
                bindData(obj);
            }

            @Override
            public void onError(String errorText) {
                LogUtil.logError(TAG, "bindData", new RuntimeException(errorText));
            }
        });
    }

}