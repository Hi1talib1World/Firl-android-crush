package com.Denzo.firl.Utils;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

public class PulsatorLayout extends RelativeLayout {

    private static final int DEFAULT_COUNT = 4;
    private static final int DEFAULT_DURATION = 3000;
    private static final int DEFAULT_REPEAT = -1;

    private final int count = DEFAULT_COUNT;
    private final int duration = DEFAULT_DURATION;
    private boolean isStarted = false;
    private final List<View> views = new ArrayList<>();
    private AnimatorSet animatorSet;

    public PulsatorLayout(Context context) {
        this(context, null);
    }

    public PulsatorLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PulsatorLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        for (int i = 0; i < count; i++) {
            View view = new View(getContext());
            view.setLayoutParams(params);
            view.setBackgroundResource(com.Denzo.firl.R.drawable.circle);
            view.setAlpha(0);
            addView(view);
            views.add(view);
        }
    }

    public void start() {
        if (isStarted) return;

        List<Animator> animators = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            ObjectAnimator scaleX = ObjectAnimator.ofFloat(views.get(i), "scaleX", 1.0f, 2.0f);
            scaleX.setRepeatCount(DEFAULT_REPEAT);
            scaleX.setRepeatMode(ObjectAnimator.RESTART);
            scaleX.setStartDelay((long) i * duration / count);

            ObjectAnimator scaleY = ObjectAnimator.ofFloat(views.get(i), "scaleY", 1.0f, 2.0f);
            scaleY.setRepeatCount(DEFAULT_REPEAT);
            scaleY.setRepeatMode(ObjectAnimator.RESTART);
            scaleY.setStartDelay((long) i * duration / count);

            ObjectAnimator alpha = ObjectAnimator.ofFloat(views.get(i), "alpha", 1.0f, 0.0f);
            alpha.setRepeatCount(DEFAULT_REPEAT);
            alpha.setRepeatMode(ObjectAnimator.RESTART);
            alpha.setStartDelay((long) i * duration / count);

            animators.add(scaleX);
            animators.add(scaleY);
            animators.add(alpha);
        }

        animatorSet = new AnimatorSet();
        animatorSet.playTogether(animators);
        animatorSet.setDuration(duration);
        animatorSet.setInterpolator(new AccelerateDecelerateInterpolator());
        animatorSet.start();

        isStarted = true;
    }

    public void stop() {
        if (animatorSet != null) {
            animatorSet.end();
        }
        isStarted = false;
    }
}
