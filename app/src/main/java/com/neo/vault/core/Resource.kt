package com.neo.vault.core

sealed class Resource<out T> {
    data class Success<T>(
        val data: T
    ) : Resource<T>()

    data class Error(
        val message: Throwable
    ) : Resource<Nothing>()
}