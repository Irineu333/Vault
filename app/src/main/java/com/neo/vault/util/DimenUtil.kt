package com.neo.vault.util

import com.neo.vault.R
import com.neo.vault.app

@Suppress("UNCHECKED_CAST")
object DimenUtil {

    fun dpToPx(value: Number): Float {
        return value.toFloat() * app.resources.getDimension(R.dimen.dimen_1dp)
    }

    fun spToPx(value: Number): Float {
        return value.toFloat() * app.resources.getDimension(R.dimen.dimen_1sp)
    }
}