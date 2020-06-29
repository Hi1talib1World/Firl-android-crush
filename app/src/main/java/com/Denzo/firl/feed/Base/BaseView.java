package com.Denzo.firl.feed.Base;


import android.content.DialogInterface;
import android.view.View;

import androidx.annotation.StringRes;

import com.hannesdorfmann.mosby3.mvp.MvpView;

public interface BaseView extends MvpView {

    void showProgress();

    void showProgress(int message);

    void hideProgress();

    void showSnackBar(String message);

    void showSnackBar(int message);

    void showSnackBar(View view, int messageId);

    void showToast(@StringRes int messageId);

    void showToast(String message);

    void showWarningDialog(int messageId);

    void showWarningDialog(String message);

    void showNotCancelableWarningDialog(String message);

    void showWarningDialog(@StringRes int messageId, DialogInterface.OnClickListener listener);

    void showWarningDialog(String message, DialogInterface.OnClickListener listener);

    void startLoginActivity();

    void hideKeyboard();

    void finish();
}