package com.neo.vault.presentation.ui.component

import android.content.Context
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import com.neo.vault.R

class KeyView(
    context: Context,
    attr: AttributeSet? = null
) : AppCompatTextView(context, attr) {

    private var textPercentSize = 0.50f

    init {
        setBackgroundResource(R.drawable.bg_key_button)
        isClickable = true

        text = "A"
        setTextColor(ContextCompat.getColor(context, R.color.onSurface))
        gravity = Gravity.CENTER
        includeFontPadding = false
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec)

        setTextSize(TypedValue.COMPLEX_UNIT_PX,measuredWidth * textPercentSize)
    }
}