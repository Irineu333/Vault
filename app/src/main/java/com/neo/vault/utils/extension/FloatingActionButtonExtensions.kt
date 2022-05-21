package com.neo.vault.utils.extension

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton

fun ExtendedFloatingActionButton.hideAnimated() {

    isClickable = false

    animate()
        .setDuration(200)
        .alpha(0f)
        .translationX(absoluteWidth.toFloat())
        .start()
}

fun ExtendedFloatingActionButton.showAnimated() {

    isClickable = true

    animate()
        .setDuration(200)
        .alpha(1f)
        .translationX(0f)
        .start()
}