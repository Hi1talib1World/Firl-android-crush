package com.Denzo.firl.feed.pickImageBase;


import android.net.Uri;

import com.Denzo.firl.feed.Base.BaseView;

public interface PickImageView extends BaseView {
    void hideLocalProgress();

    void loadImageToImageView(Uri imageUri);
}