package com.example.jewelrypurchase.models;

import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;

import androidx.core.widget.NestedScrollView;

public class ScrollViewHideHelper {
    private NestedScrollView scrollView;
    private View targetView;
    private int animationDuration = 300;
    private int scrollThreshold = 20;
    private int translateDistance = 0;
    private int direction;               // 动画方向 1（向下）-1（向上）
    private boolean isHidden = false;

    public ScrollViewHideHelper(NestedScrollView scrollView, View targetView, int direction) {
        this.scrollView = scrollView;
        this.targetView = targetView;
        this.direction = direction;
        setupScrollListener();
    }

    // 设置移动距离（单位：px）
    public void setTranslateDistance(int distance) {
        this.translateDistance = distance;
    }

    // 设置动画时长
    public void setAnimationDuration(int duration) {
        this.animationDuration = duration;
    }

    // 设置滑动阈值
    public void setScrollThreshold(int threshold) {
        this.scrollThreshold = threshold;
    }

    private void setupScrollListener() {
        scrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            private boolean isScrolling = false; // 防止重复触发动画
            private int lastScrollY = 0;

            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                int dy = scrollY - oldScrollY;
                lastScrollY = scrollY;

                // 防抖：避免频繁触发
                if (Math.abs(dy) < scrollThreshold || isScrolling) return;

                // 判断滚动方向
                boolean isScrollingDown = dy > 0;
                boolean isScrollingUp = dy < 0;

                // 边缘情况：如果已经在顶部或底部，则不处理
                if (isAtTop() && isScrollingUp) return;
                if (isAtBottom() && isScrollingDown) return;

                // 处理滚动逻辑
                if (isScrollingDown && !isHidden) {
                    hideView();
                } else if (isScrollingUp && isHidden) {
                    showView();
                }
            }

            private boolean isAtTop() {
                // 判断是否滚动到顶部（scrollY == 0）
                return lastScrollY == 0;
            }

            private boolean isAtBottom() {
                // 判断是否滚动到底部
                View child = scrollView.getChildAt(0);
                if (child == null) return false;
                return scrollView.getHeight() + scrollView.getScrollY() >= child.getHeight();
            }

        });
    }

    private int getEffectiveDistance() {
        return translateDistance != 0 ? translateDistance : targetView.getHeight();
    }

    private void hideView() {
        int distance = getEffectiveDistance();
        if (targetView.getTranslationY() == 0) {
            targetView.animate()
                    .translationY(direction * 2 * targetView.getHeight())
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

    // 手动控制方法
    public void show() {
        showView();
    }

    public void hide() {
        hideView();
    }
}
