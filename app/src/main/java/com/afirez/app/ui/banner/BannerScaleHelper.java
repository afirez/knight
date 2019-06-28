package com.afirez.app.ui.banner;

import android.content.Context;
import android.os.Build;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.*;
import android.view.View;
import android.view.ViewTreeObserver;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

/**
 * Created by jameson on 8/30/16.
 * changed by 二精-霁雪清虹 on 2017/11/19
 */
public class BannerScaleHelper {
    private BannerRecyclerView mRecyclerView;
    private Context mContext;

    private float mScale = 0.9f; // 两边视图scale
    private int mPagePadding = 15; // 卡片的padding, 卡片间的距离等于2倍的mPagePadding
    private int mShowLeftCardWidth = 15;   // 左边卡片显示大小

    private int mCardWidth; // 卡片宽度
    private int mOnePageWidth; // 滑动一页的距离
    private int mCardGalleryWidth;

    private int mFirstItemPos;
    private int mCurrentItemOffset;


    private boolean pagerLike = false;
    private SnapHelper mSnapHelper;
    private CanScrollOnSnap canScrollOnSnap;

    private int mLastPos;

    public boolean isPagerLike() {
        return pagerLike;
    }

    public void setPagerLike(boolean pagerLike) {
        this.pagerLike = pagerLike;
    }

    public void attachToRecyclerView(final BannerRecyclerView mRecyclerView) {
        if (mRecyclerView == null) {
            return;
        }
        this.mRecyclerView = mRecyclerView;
        mContext = mRecyclerView.getContext();
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                // Log.e("TAG", "RecyclerView.OnScrollListener onScrollStateChanged");
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    canScrollOnSnap.canScrollOnSnap(getCurrentItem() != 0 &&
                            getCurrentItem() != mRecyclerView.getAdapter().getItemCount() - 2);
                    int[] finalSnapDistance = canScrollOnSnap.finalSnapDistance();
                    if (finalSnapDistance[0] == 0
                            && finalSnapDistance[1] == 0) {
                        mCurrentItemOffset = 0;
                        mLastPos = getCurrentItem();
                        //认为是一次滑动停止 这里可以写滑动停止回调
                        mRecyclerView.dispatchOnPageSelected(mLastPos);
                        //Log.e("TAG", "滑动停止后最终位置为" + getCurrentItem());
                    }
                } else {
                    canScrollOnSnap.canScrollOnSnap(true);
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                //Log.e("TAG", String.format("onScrolled dx=%s, dy=%s", dx, dy));
                super.onScrolled(recyclerView, dx, dy);
                // dx>0则表示右滑, dx<0表示左滑, dy<0表示上滑, dy>0表示下滑
                mCurrentItemOffset += dx;
                onScrolledChangedCallback();
            }
        });
        initWidth();

        if (isPagerLike()) {
            CardPagerSnapHelper cardPagerSnapHelper = new CardPagerSnapHelper();
            mSnapHelper = cardPagerSnapHelper;
            canScrollOnSnap = cardPagerSnapHelper;
        } else {
            CardLinearSnapHelper cardLinearSnapHelper = new CardLinearSnapHelper();
            mSnapHelper = cardLinearSnapHelper;
            canScrollOnSnap = cardLinearSnapHelper;
        }

        mSnapHelper.attachToRecyclerView(mRecyclerView);
    }

    /**
     * 初始化卡片宽度
     */
    private void initWidth() {
        mRecyclerView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (Build.VERSION.SDK_INT > 16) {
                    mRecyclerView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                } else {
                    mRecyclerView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                }
                mCardGalleryWidth = mRecyclerView.getWidth();
                mCardWidth = mCardGalleryWidth - 2 * (mPagePadding + mShowLeftCardWidth);
                mOnePageWidth = mCardWidth;
                scrollToPosition(mFirstItemPos);
            }
        });
    }

    public void setCurrentItem(int item) {
        setCurrentItem(item, false);
    }

    public void setCurrentItem(int item, boolean smoothScroll) {
        if (mRecyclerView == null) {
            return;
        }
        if (smoothScroll) {
            mRecyclerView.smoothScrollToPosition(item);
        } else {
            scrollToPosition(item);
        }
    }

    public void scrollToPosition(int pos) {
        if (mRecyclerView == null) {
            return;
        }
        //mRecyclerView.getLayoutManager()).scrollToPositionWithOffset 方法不会回调  RecyclerView.OnScrollListener 的onScrollStateChanged方法,是瞬间跳到指定位置
        //mRecyclerView.smoothScrollToPosition 方法会回调  RecyclerView.OnScrollListener 的onScrollStateChanged方法 并且是自动居中，有滚动过程的滑动到指定位置
        ((LinearLayoutManager) mRecyclerView.getLayoutManager()).
                scrollToPositionWithOffset(pos, mPagePadding + mShowLeftCardWidth);
        mCurrentItemOffset = 0;
        mLastPos = pos;
        //认为是一次滑动停止 这里可以写滑动停止回调
        mRecyclerView.dispatchOnPageSelected(mLastPos);
        //onScrolledChangedCallback();
        mRecyclerView.post(new Runnable() {
            @Override
            public void run() {
                onScrolledChangedCallback();
            }
        });
    }

    public void setFirstItemPosition(int firstItemPos) {
        this.mFirstItemPos = firstItemPos;
    }


    /**
     * RecyclerView位移事件监听, view大小随位移事件变化
     */
    private void onScrolledChangedCallback() {
        if (mOnePageWidth == 0) {
            return;
        }
        int currentItemPos = getCurrentItem();
        int offset = mCurrentItemOffset - (currentItemPos - mLastPos) * mOnePageWidth;
        float percent = (float) Math.max(Math.abs(offset) * 1.0 / mOnePageWidth, 0.0001);

        //Log.e("TAG",String.format("offset=%s, percent=%s", offset, percent));
        View leftView = null;
        View currentView;
        View rightView = null;
        if (currentItemPos > 0) {
            leftView = mRecyclerView.getLayoutManager().findViewByPosition(currentItemPos - 1);
        }
        currentView = mRecyclerView.getLayoutManager().findViewByPosition(currentItemPos);
        if (currentItemPos < mRecyclerView.getAdapter().getItemCount() - 1) {
            rightView = mRecyclerView.getLayoutManager().findViewByPosition(currentItemPos + 1);
        }

        if (leftView != null) {
            // y = (1 - mScale)x + mScale
            leftView.setScaleY((1 - mScale) * percent + mScale);
        }
        if (currentView != null) {
            // y = (mScale - 1)x + 1
            currentView.setScaleY((mScale - 1) * percent + 1);
        }
        if (rightView != null) {
            // y = (1 - mScale)x + mScale
            rightView.setScaleY((1 - mScale) * percent + mScale);
        }
    }

    public int getCurrentItem() {
        //return ((LinearLayoutManager) mRecyclerView.getLayoutManager()).findLastVisibleItemPosition() - 1;
        return mRecyclerView.getLayoutManager().getPosition(mSnapHelper.findSnapView(mRecyclerView.getLayoutManager()));
    }

    public void setScale(float scale) {
        mScale = scale;
    }

    public void setPagePadding(int pagePadding) {
        mPagePadding = pagePadding;
    }

    public void setShowLeftCardWidth(int showLeftCardWidth) {
        mShowLeftCardWidth = showLeftCardWidth;
    }

    interface CanScrollOnSnap {
        boolean canScrollOnSnap();

        void canScrollOnSnap(boolean canScroll);

        int[] finalSnapDistance();
    }

    /**
     * 防止卡片在第一页和最后一页因无法"居中"而一直循环调用onScrollStateChanged-->SnapHelper.snapToTargetExistingView-->onScrollStateChanged
     * Created by jameson on 9/3/16.
     */
    private static class CardLinearSnapHelper extends LinearSnapHelper implements CanScrollOnSnap {
        public boolean canScrollOnSnap = true;
        public int[] finalSnapDistance = {0, 0};

        @Override
        public int[] calculateDistanceToFinalSnap(@NonNull RecyclerView.LayoutManager layoutManager, @NonNull View targetView) {
            //Log.e("TAG", "calculateDistanceToFinalSnap");
            if (!canScrollOnSnap) {
                finalSnapDistance[0] = 0;
                finalSnapDistance[1] = 0;
            } else {
                finalSnapDistance = super.calculateDistanceToFinalSnap(layoutManager, targetView);
            }
            return finalSnapDistance;
        }

        @Override
        public boolean canScrollOnSnap() {
            return canScrollOnSnap;
        }

        @Override
        public void canScrollOnSnap(boolean canScroll) {
            canScrollOnSnap = canScroll;
        }

        @Override
        public int[] finalSnapDistance() {
            return finalSnapDistance;
        }
    }

    private static class CardPagerSnapHelper extends PagerSnapHelper implements CanScrollOnSnap {
        public int[] finalSnapDistance = {0, 0};

        @Override
        public int[] calculateDistanceToFinalSnap(@NonNull RecyclerView.LayoutManager layoutManager, @NonNull View targetView) {
            //Log.e("TAG", "calculateDistanceToFinalSnap");

            finalSnapDistance = super.calculateDistanceToFinalSnap(layoutManager, targetView);

            return finalSnapDistance;
        }

        @Override
        public boolean canScrollOnSnap() {
            return true;
        }

        @Override
        public void canScrollOnSnap(boolean canScroll) {

        }

        @Override
        public int[] finalSnapDistance() {
            return finalSnapDistance;
        }
    }
}
