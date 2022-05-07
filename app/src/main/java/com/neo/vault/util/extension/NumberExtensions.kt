package com.neo.vault.util.extension

import com.neo.vault.util.DimenUtil

fun Number.dpToPx(): Float {
    return DimenUtil.dpToPx(this)
}

fun Number.spToPx(): Float {
    return DimenUtil.spToPx(this)
}