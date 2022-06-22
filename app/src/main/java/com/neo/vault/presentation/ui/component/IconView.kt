package com.neo.vault.presentation.ui.component

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import com.neo.vault.R
import com.neo.vault.databinding.CustomAdjustIconBinding

class IconView(
    context: Context,
    attr: AttributeSet? = null
) : FrameLayout(context, attr) {

    private val binding =
        CustomAdjustIconBinding.inflate(
            LayoutInflater.from(context),
            this,
            true
        )

    init {
        setBackgroundResource(R.drawable.bg_key_button)
        isClickable = true
        setColorFilter(ContextCompat.getColor(context, R.color.onSurface))
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec)
    }

    fun setImageResource(@DrawableRes resId: Int) {
        binding.icon.setImageResource(resId)
    }

    @Suppress("unused")
    fun setColorFilter(@ColorInt color: Int) {
        binding.icon.setColorFilter(color)
    }
}
