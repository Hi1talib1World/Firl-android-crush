package com.Denzo.firl.postDetails;


import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import com.Denzo.firl.feed.Base.BasePresenter;

class ImageDetailPresenter extends BasePresenter<ImageDetailView> {

    ImageDetailPresenter(Context context) {
        super(context);
    }

    public int calcMaxImageSide() {
        DisplayMetrics displaymetrics = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(displaymetrics);

        int width = displaymetrics.widthPixels;
        int height = displaymetrics.heightPixels;

        return width > height ? width : height;
    }
}