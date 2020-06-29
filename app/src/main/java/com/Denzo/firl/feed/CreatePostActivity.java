package com.Denzo.firl.feed;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.annotation.NonNull;

import com.Denzo.firl.R;
import com.Denzo.firl.feed.Base.BaseCreatePostActivity;

public class CreatePostActivity extends BaseCreatePostActivity<CreatePostView, CreatePostPresenter> implements CreatePostView {
    public static final int CREATE_NEW_POST_REQUEST = 11;

    @NonNull
    @Override
    public CreatePostPresenter createPresenter() {
        if (presenter == null) {
            return new CreatePostPresenter(this);
        }
        return presenter;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.create_post_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.post:
                presenter.doSavePost(imageUri);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}