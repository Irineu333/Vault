package com.neo.vault.util.extension

import android.view.View
import android.view.ViewGroup
import androidx.annotation.Px
import androidx.core.view.updatePadding

fun View.updateMargins(
    @Px left: Int = paddingLeft,
    @Px top: Int = paddingTop,
    @Px right: Int = paddingRight,
    @Px bottom: Int = paddingBottom
) {

    if (layoutParams == null) {
        layoutParams = ViewGroup.MarginLayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )

        when (layoutParams) {
            is ViewGroup.MarginLayoutParams -> {
                updatePadding(
                    left = left,
                    top = top,
                    right = right,
                    bottom = bottom
                )
            }

            else -> throw ClassCastException(
                "cannot cast ${layoutParams?.javaClass}" +
                        " to ${ViewGroup.MarginLayoutParams::class.java}"
            )
        }
    }
}