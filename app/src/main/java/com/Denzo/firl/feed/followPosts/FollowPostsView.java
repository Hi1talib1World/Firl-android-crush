package com.Denzo.firl.feed.followPosts;


import android.view.View;

import com.Denzo.firl.feed.Base.BaseView;
import com.Denzo.firl.feed.model.FollowingPost;

import java.util.List;

public interface FollowPostsView extends BaseView {
    void openPostDetailsActivity(String postId, View v);

    void openProfileActivity(String userId, View view);

    void onFollowingPostsLoaded(List<FollowingPost> list);

    void showLocalProgress();

    void hideLocalProgress();

    void showEmptyListMessage(boolean show);
}