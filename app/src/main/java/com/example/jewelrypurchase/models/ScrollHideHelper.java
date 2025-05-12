package com.example.jewelrypurchase.models;

import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ScrollHideHelper {
    private RecyclerView recyclerView;
    private View targetView;
    private int animationDuration = 400;  // 默认动画时长
    private int scrollThreshold = 3;     // 默认滑动阈值
    private int direction;               // 动画方向 1（向下）-1（向上）
    private boolean isHidden = false;

    public ScrollHideHelper(RecyclerView recyclerView, View targetView, int direction) {
        this.recyclerView = recyclerView;
        this.targetView = targetView;
        this.direction = direction;
        setupScrollListener();
    }

    // 可选：自定义动画时长
    public void setAnimationDuration(int duration) {
        this.animationDuration = duration;
    }

    // 可选：自定义滑动阈值
    public void setScrollThreshold(int threshold) {
        this.scrollThreshold = threshold;
    }

    private void setupScrollListener() {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            private int totalScrolled = 0;

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                totalScrolled += dy;

                if (Math.abs(dy) > scrollThreshold) {
                    if (dy > 0 && !isHidden) {
                        hideView();
                    } else if (dy < 0 && isHidden) {
                        showView();
                    }
                }
            }
        });
    }

    private void hideView() {
        if (targetView.getTranslationY() == 0) {
            targetView.animate()
                    .translationY(direction*2*targetView.getHeight())
                    .setInterpolator(new AccelerateInterpolator())
                    .setDuration(animationDuration)
                    .withEndAction(() -> isHidden = true)
                    .start();
        }
    }

    private void showView() {
        if (targetView.getTranslationY() != 0) {
            targetView.animate()
                    .translationY(0)
                    .setInterpolator(new DecelerateInterpolator())
                    .setDuration(animationDuration)
                    .withEndAction(() -> isHidden = false)
                    .start();
        }
    }

    // 手动显示/隐藏方法
    public void show() {
        showView();
    }

    public void hide() {
        hideView();
    }
}