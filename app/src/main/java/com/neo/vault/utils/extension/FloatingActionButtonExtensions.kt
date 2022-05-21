package com.neo.vault.utils.extension

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton

fun ExtendedFloatingActionButton.hideAnimated() {

    isEnabled = false

    animate()
        .setDuration(200)
        .alpha(0f)
        .translationX(absoluteWidth.toFloat())
        .start()
}

fun ExtendedFloatingActionButton.showAnimated() {

    isEnabled = true

    animate()
        .setDuration(200)
        .alpha(1f)
        .translationX(0f)
        .start()
}