package com.neo.vault.util.extension

import android.view.View
import android.view.ViewGroup
import androidx.annotation.Px
import androidx.core.view.marginBottom
import androidx.core.view.marginTop
import androidx.core.view.updateMargins
import androidx.core.view.updatePadding
import androidx.viewbinding.ViewBinding
import com.google.android.material.snackbar.Snackbar
import com.neo.vault.presentation.model.UiText

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

        when (val params = layoutParams) {
            is ViewGroup.MarginLayoutParams -> {
                params.updateMargins(
                    left = left,
                    top = top,
                    right = right,
                    bottom = bottom
                )
            }

            else -> throw ClassCastException(
                "cannot cast ${params?.javaClass}" +
                        " to ${ViewGroup.MarginLayoutParams::class.java}"
            )
        }
    }
}

fun View.showSnackbar(
    message: UiText,
    length: Int = Snackbar.LENGTH_LONG
): Snackbar {
    return Snackbar.make(
        this,
        message.resolve(context),
        length
    ).apply {
        show()
    }
}

fun View.removeFromParent() {
    (this.parent as? ViewGroup)?.removeView(this)
}

val View.totalHeight get() = marginTop + marginBottom + measuredHeight