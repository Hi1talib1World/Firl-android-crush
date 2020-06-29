package com.Denzo.firl.feed.pickImageBase;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.Denzo.firl.R;
import com.Denzo.firl.feed.Base.BasePresenter;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.File;

import static android.app.Activity.RESULT_OK;

public class PickImagePresenter<V extends PickImageView> extends BasePresenter<V> {
    protected static final int MAX_FILE_SIZE_IN_BYTES = 10485760;   //10 Mb

    public PickImagePresenter(Context context) {
        super(context);
    }

    public boolean isImageFileValid(Uri imageUri) {
        int message = R.string.error_general;
        boolean result = false;

        if (imageUri != null) {
            if (ValidationUtil.isImage(imageUri, context)) {
                File imageFile = new File(imageUri.getPath());
                if (imageFile.length() > MAX_FILE_SIZE_IN_BYTES) {
                    message = R.string.error_bigger_file;
                } else {
                    result = true;
                }
            } else {
                message = R.string.error_incorrect_file_type;
            }
        }

        if (!result) {
            int finalMessage = message;
            ifViewAttached(view -> {
                view.hideLocalProgress();
                view.showSnackBar(finalMessage);
            });

        }

        return result;
    }

    protected void handleCropImageResult(int requestCode, int resultCode, Intent data) {
        ifViewAttached(view -> {
            if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
                CropImage.ActivityResult result = CropImage.getActivityResult(data);
                if (resultCode == RESULT_OK) {
                    if (ValidationUtil.checkImageMinSize(result.getCropRect())) {
                        Uri imageUri = result.getUri();
                        view.loadImageToImageView(imageUri);
                    } else {
                        view.showSnackBar(R.string.error_smaller_image);
                    }
                } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                    LogUtil.logError(TAG, "crop image error", result.getError());
                    view.showSnackBar(R.string.error_fail_crop_image);
                }
            }
        });

    }
}