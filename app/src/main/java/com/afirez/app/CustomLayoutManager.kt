package com.afirez.app

import android.graphics.Rect
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.RecyclerView.LayoutParams
import android.view.View
import android.view.ViewGroup

class CustomLayoutManager : RecyclerView.LayoutManager() {

    override fun generateDefaultLayoutParams(): LayoutParams {
        return RecyclerView.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }

    override fun onLayoutChildren(recycler: RecyclerView.Recycler, state: RecyclerView.State) {
        super.onLayoutChildren(recycler, state)

        if (itemCount <= 0 || state.isPreLayout) {
            removeAndRecycleAllViews(recycler)
            return

        }

        detachAndScrapAttachedViews(recycler)

        fillChildren(recycler, state)
    }

//    var maxChildCount: Int = 5

    private val offsetX: Int = 0
    private val offsetY: Int = 0

    private fun fillChildren(recycler: RecyclerView.Recycler, state: RecyclerView.State) {
        val displayRect = Rect(
            offsetX,
            offsetY,
            offsetX + getHorizontalSpace(),
            offsetY + getVerticalSpace()
        )

        synchronized(this) {
            val count = this.itemCount - 1
            for (i in 0..count) {
                if (i >= itemCount) {
                    return
                }
                val scrap = recycler.getViewForPosition(i)
                measureChildWithMargins(scrap, 0, 0)
                addView(scrap)
                val width = getDecoratedMeasuredWidth(scrap)
                val height = getDecoratedMeasuredHeight(scrap)
                val layoutParams = scrap.layoutParams as RecyclerView.LayoutParams
                val left = layoutParams.leftMargin
                val top = layoutParams.topMargin
                val childRect = Rect(left - width, top, left, top + height)
                if (!Rect.intersects(displayRect, childRect)) {
                    removeAndRecycleView(scrap, recycler)
                } else {
                    layoutChild(scrap, childRect)
                }
            }
        }
    }

    private fun layoutChild(child: View, childRect: Rect) {
        layoutDecorated(child, childRect.left, childRect.top, childRect.right, childRect.bottom)
    }

    private fun getVerticalSpace(): Int {
        return height - paddingBottom - paddingTop
    }

    private fun getHorizontalSpace(): Int {
        return width - paddingLeft - paddingRight
    }
}
