package com.neo.vault.presentation.model

import android.content.Context
import androidx.annotation.StringRes

sealed class UiText {

    companion object {
        fun to(value: String) : Raw {
            return Raw(value = value)
        }

        fun to(@StringRes stringResId: Int) : Res {
            return Res(stringResId = stringResId)
        }
    }

    fun resolve(context: Context): String {
        return when (this) {
            is Raw -> value
            is Res -> context.getString(stringResId)
        }
    }

    data class Raw(
        val value: String
    ) : UiText()

    data class Res(
        @StringRes
        val stringResId: Int
    ) : UiText()
}
