package com.Denzo.firl.postDetails;


import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;

import com.Denzo.firl.R;
import com.Denzo.firl.feed.Base.BaseActivity;
import com.Denzo.firl.feed.PostManager;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;

public class ImageDetailActivity extends BaseActivity<ImageDetailView, ImageDetailPresenter> implements ImageDetailView {

    private static final String TAG = ImageDetailActivity.class.getSimpleName();

    public static final String IMAGE_TITLE_EXTRA_KEY = "ImageDetailActivity.IMAGE_TITLE_EXTRA_KEY";
    private ViewGroup viewGroup;
    private TouchImageView touchImageView;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_detail);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        touchImageView = findViewById(R.id.touchImageView);
        progressBar = findViewById(R.id.progressBar);
        viewGroup = findViewById(R.id.image_detail_container);

        initActionBar();

        String imageTitle = getIntent().getStringExtra(IMAGE_TITLE_EXTRA_KEY);
        loadImage(imageTitle);

        touchImageView.setOnClickListener(v -> {
            final int vis = viewGroup.getSystemUiVisibility();
            if ((vis & View.SYSTEM_UI_FLAG_LOW_PROFILE) != 0) {
                viewGroup.setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
            } else {
                viewGroup.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE);
            }
        });
    }

    @NonNull
    @Override
    public ImageDetailPresenter createPresenter() {
        if (presenter == null) {
            return new ImageDetailPresenter(this);
        }
        return presenter;
    }

    private void initActionBar() {
        actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setDisplayHomeAsUpEnabled(true);

            viewGroup.setOnSystemUiVisibilityChangeListener(
                    vis -> {
                        if ((vis & View.SYSTEM_UI_FLAG_LOW_PROFILE) != 0) {
                            actionBar.hide();
                        } else {
                            actionBar.show();
                        }
                    });

            // Start low profile mode and hide ActionBar
            viewGroup.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE);
            actionBar.hide();
        }
    }

    private void loadImage(String imageTitle) {
        int maxImageSide = presenter.calcMaxImageSide();

        ImageUtil.loadImageWithSimpleTarget(GlideApp.with(this),
                PostManager.getInstance(this.getApplicationContext()).getOriginImageStorageRef(imageTitle),
                new SimpleTarget<Bitmap>(maxImageSide, maxImageSide) {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        progressBar.setVisibility(View.GONE);
                        touchImageView.setImageBitmap(resource);
                    }

                    @Override
                    public void onLoadFailed(@Nullable Drawable errorDrawable) {
                        super.onLoadFailed(errorDrawable);
                        progressBar.setVisibility(View.GONE);
                        touchImageView.setImageResource(R.drawable.ic_stub);
                    }
                });
    }
}