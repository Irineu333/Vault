package com.neo.vault.presentation.model

import androidx.annotation.StringRes

sealed class UiText {
    data class Raw(
        val value: String
    ) : UiText()

    data class Res(
        @StringRes val stringResId: Int
    ) : UiText()
}
