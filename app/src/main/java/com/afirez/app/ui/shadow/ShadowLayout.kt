package com.afirez.app.ui.shadow

import android.content.Context
import android.content.res.TypedArray
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.util.AttributeSet
import android.widget.FrameLayout
import com.afirez.app.R

class ShadowLayout : FrameLayout {

    var shadowColor: Int = 0
    var shadowRadius: Float = 0.toFloat()
    var cornerRadius: Float = 0.toFloat()
    var dx: Float = 0.toFloat()
    var dy: Float = 0.toFloat()
    var theBackgroundColor: Int = 0

    private var invalidateShadowOnSizeChanged = true
    private var forceInvalidateShadow = false

    constructor(context: Context) : super(context) {
        initView(context, null)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        initView(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initView(context, attrs)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        if (w > 0 && h > 0 && (background == null || invalidateShadowOnSizeChanged || forceInvalidateShadow)) {
            forceInvalidateShadow = false
            setBackgroundCompat(w, h)
        }
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        if (forceInvalidateShadow) {
            forceInvalidateShadow = false
            setBackgroundCompat(right - left, bottom - top)
        }
    }

    fun setInvalidateShadowOnSizeChanged(invalidateShadowOnSizeChanged: Boolean) {
        this.invalidateShadowOnSizeChanged = invalidateShadowOnSizeChanged
    }

    fun invalidateShadow() {
        forceInvalidateShadow = true
        requestLayout()
        invalidate()
    }

    private fun initView(context: Context, attrs: AttributeSet?) {
        initAttributes(context, attrs)
        refreshPadding()
    }

    fun refreshPadding() {
        val xPadding = (shadowRadius + Math.abs(dx)).toInt()
        val yPadding = (shadowRadius + Math.abs(dy)).toInt()
        setPadding(xPadding, yPadding, xPadding, yPadding)
    }

    private fun setBackgroundCompat(w: Int, h: Int) {
        val bitmap = createShadowBitmap(w, h, cornerRadius, shadowRadius, dx, dy, shadowColor, Color.TRANSPARENT)
        val drawable = BitmapDrawable(resources, bitmap)
        background = drawable
    }


    private fun initAttributes(context: Context, attrs: AttributeSet?) {
        val attr = getTypedArray(context, attrs, R.styleable.ShadowLayout) ?: return
        try {
            cornerRadius = attr.getDimension(R.styleable.ShadowLayout_shadow_layout_cornerRadius, 0f)
            shadowRadius = attr.getDimension(R.styleable.ShadowLayout_shadow_layout_shadowRadius, 0f)
            dx = attr.getDimension(R.styleable.ShadowLayout_shadow_layout_dx, 0f)
            dy = attr.getDimension(R.styleable.ShadowLayout_shadow_layout_dy, 0f)
            shadowColor = attr.getColor(R.styleable.ShadowLayout_shadow_layout_shadowColor, Color.parseColor("#22000000"))
            theBackgroundColor = attr.getColor(R.styleable.ShadowLayout_shadow_layout_backgroundColor, Integer.MIN_VALUE)
        } finally {
            attr.recycle()
        }
    }

    private fun getTypedArray(context: Context, attributeSet: AttributeSet?, attr: IntArray): TypedArray? {
        return context.obtainStyledAttributes(attributeSet, attr, 0, 0)
    }

    private fun createShadowBitmap(shadowWidth: Int, shadowHeight: Int, cornerRadius: Float, shadowRadius: Float,
                                   dx: Float, dy: Float, shadowColor: Int, fillColor: Int): Bitmap {
        val output: Bitmap = Bitmap.createBitmap(shadowWidth, shadowHeight, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(output)

        val shadowRect = RectF(
            shadowRadius,
            shadowRadius,
            shadowWidth - shadowRadius,
            shadowHeight - shadowRadius)

        if (dy > 0) {
            shadowRect.top += dy
            shadowRect.bottom -= dy
        } else if (dy < 0) {
            shadowRect.top += Math.abs(dy)
            shadowRect.bottom -= Math.abs(dy)
        }

        if (dx > 0) {
            shadowRect.left += dx
            shadowRect.right -= dx
        } else if (dx < 0) {
            shadowRect.left += Math.abs(dx)
            shadowRect.right -= Math.abs(dx)
        }

        val paint = Paint()
        paint.isAntiAlias = true
        paint.color = fillColor
        paint.style = Paint.Style.FILL

        paint.setShadowLayer(shadowRadius, dx, dy, shadowColor)
        canvas.drawRoundRect(shadowRect, cornerRadius, cornerRadius, paint)
        if (theBackgroundColor != Integer.MIN_VALUE) {
            paint.clearShadowLayer()
            paint.color = theBackgroundColor
            val backgroundRect = RectF(paddingLeft.toFloat(), paddingTop.toFloat(), (width - paddingRight).toFloat(), (height - paddingBottom).toFloat())
            canvas.drawRoundRect(backgroundRect, cornerRadius, cornerRadius, paint)
        }

        return output
    }

}