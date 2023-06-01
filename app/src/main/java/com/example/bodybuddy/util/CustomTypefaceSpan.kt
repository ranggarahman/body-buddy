package com.example.bodybuddy.util

import android.graphics.Paint
import android.graphics.Typeface
import android.text.TextPaint
import android.text.style.TypefaceSpan

class CustomTypefaceSpan(private val typeface: Typeface) : TypefaceSpan("") {
    override fun updateDrawState(ds: TextPaint) {
        applyCustomTypeface(ds)
    }

    override fun updateMeasureState(paint: TextPaint) {
        applyCustomTypeface(paint)
    }

    private fun applyCustomTypeface(paint: Paint) {
        val oldStyle: Int
        val old = paint.typeface
        oldStyle = old?.style ?: 0

        val customTypeface = Typeface.create(typeface, oldStyle)
        paint.typeface = customTypeface
    }
}
