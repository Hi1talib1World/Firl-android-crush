package com.Denzo.firl.feed;


import android.net.Uri;

import com.Denzo.firl.feed.pickImageBase.PickImageView;

public interface BaseCreatePostView extends PickImageView {
    void setDescriptionError(String error);

    void setTitleError(String error);

    String getTitleText();

    String getDescriptionText();

    void requestImageViewFocus();

    void onPostSavedSuccess();

    Uri getImageUri();
}