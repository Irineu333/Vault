package com.neo.vault.utils.extension

import com.neo.vault.utils.DimenUtil

fun Number.dpToPx(): Float {
    return DimenUtil.dpToPx(this)
}

fun Number.spToPx(): Float {
    return DimenUtil.spToPx(this)
}