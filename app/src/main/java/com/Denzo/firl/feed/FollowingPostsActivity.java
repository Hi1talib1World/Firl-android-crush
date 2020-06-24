package com.Denzo.firl.feed;

import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.Denzo.firl.Profil.ProfilActivity;
import com.Denzo.firl.R;
import com.Denzo.firl.postDetails.PostDetailsActivity;

class FollowingPostsActivity extends BaseActivity<FollowPostsView, FollowingPostsPresenter> implements FollowPostsView {

    private FollowPostsAdapter postsAdapter;
    private RecyclerView recyclerView;

    private ProgressBar progressBar;
    private SwipeRefreshLayout swipeContainer;
    private TextView message_following_posts_empty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_follow_posts);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        initContentView();

        presenter.loadFollowingPosts();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == PostDetailsActivity.UPDATE_POST_REQUEST) {
            postsAdapter.updateSelectedItem();
        }
    }

    @NonNull
    @Override
    public FollowingPostsPresenter createPresenter() {
        if (presenter == null) {
            return new FollowingPostsPresenter(this);
        }
        return presenter;
    }

    @Override
    public void onFollowingPostsLoaded(List<FollowingPost> list) {
        postsAdapter.setList(list);
    }

    @Override
    public void showLocalProgress() {
        if (!swipeContainer.isRefreshing()) {
            progressBar.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void hideLocalProgress() {
        swipeContainer.setRefreshing(false);
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void showEmptyListMessage(boolean show) {
        message_following_posts_empty.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    private void initContentView() {
        if (recyclerView == null) {
            progressBar = findViewById(R.id.progressBar);
            message_following_posts_empty = findViewById(R.id.message_following_posts_empty);
            swipeContainer = findViewById(R.id.swipeContainer);
            swipeContainer.setOnRefreshListener(() -> presenter.onRefresh());

            initPostListRecyclerView();
        }
    }

    private void initPostListRecyclerView() {
        recyclerView = findViewById(R.id.recyclerView);
        postsAdapter = new FollowPostsAdapter(this);
        postsAdapter.setCallBack(new FollowPostsAdapter.CallBack() {
            @Override
            public void onItemClick(FollowingPost followingPost, View view) {
                presenter.onPostClicked(followingPost.getPostId(), view);
            }

            @Override
            public void onAuthorClick(int position, View view) {
                String postId = postsAdapter.getItemByPosition(position).getPostId();
                presenter.onAuthorClick(postId, view);
            }
        });
        ((SimpleItemAnimator) recyclerView.getItemAnimator()).setSupportsChangeAnimations(false);
        recyclerView.setAdapter(postsAdapter);
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void openPostDetailsActivity(String postId, View v) {
        Intent intent = new Intent(FollowingPostsActivity.this, PostDetailsActivity.class);
        intent.putExtra(PostDetailsActivity.POST_ID_EXTRA_KEY, postId);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            View imageView = v.findViewById(R.id.postImageView);
            View authorImageView = v.findViewById(R.id.authorImageView);

            ActivityOptions options = ActivityOptions.
                    makeSceneTransitionAnimation(FollowingPostsActivity.this,
                            new android.util.Pair<>(imageView, getString(R.string.post_image_transition_name)),
                            new android.util.Pair<>(authorImageView, getString(R.string.post_author_image_transition_name))
                    );
            startActivityForResult(intent, PostDetailsActivity.UPDATE_POST_REQUEST, options.toBundle());
        } else {
            startActivityForResult(intent, PostDetailsActivity.UPDATE_POST_REQUEST);
        }
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void openProfileActivity(String userId, View view) {
        Intent intent = new Intent(FollowingPostsActivity.this, ProfilActivity.class);
        intent.putExtra(ProfilActivity.USER_ID_EXTRA_KEY, userId);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && view != null) {

            View authorImageView = view.findViewById(R.id.authorImageView);

            ActivityOptions options = ActivityOptions.
                    makeSceneTransitionAnimation(FollowingPostsActivity.this,
                            new android.util.Pair<>(authorImageView, getString(R.string.post_author_image_transition_name)));
            startActivityForResult(intent, ProfilActivity.CREATE_POST_FROM_PROFILE_REQUEST, options.toBundle());
        } else {
            startActivityForResult(intent, ProfilActivity.CREATE_POST_FROM_PROFILE_REQUEST);
        }
    }
}