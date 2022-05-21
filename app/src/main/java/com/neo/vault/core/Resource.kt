package com.neo.vault.core

import com.neo.vault.presentation.model.UiText

sealed class Resource<out T> {
    data class Success<T>(
        val data: T
    ) : Resource<T>()

    data class Error(
        val message: UiText
    ) : Resource<Nothing>()
}