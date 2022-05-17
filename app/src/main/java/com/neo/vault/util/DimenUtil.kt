package com.neo.vault.util

import com.neo.vault.R
import com.neo.vault.application

object DimenUtil {

    fun dpToPx(value: Number): Float {
        return value.toFloat() * application.resources.getDimension(R.dimen.dimen_1dp)
    }

    fun spToPx(value: Number): Float {
        return value.toFloat() * application.resources.getDimension(R.dimen.dimen_1sp)
    }
}