package com.neo.vault.utils.extension

import android.graphics.Color
import androidx.annotation.ColorInt
import androidx.annotation.IntRange

@ColorInt
fun @receiver:ColorInt Int.modify(
    @IntRange(from = 0, to = 255) alpha: Int = Color.alpha(this),
    @IntRange(from = 0, to = 255) red: Int = Color.red(this),
    @IntRange(from = 0, to = 255) green : Int = Color.green(this),
    @IntRange(from = 0, to = 255) blue : Int = Color.blue(this)
): Int {
    return Color.argb(alpha, red, green, blue)
}