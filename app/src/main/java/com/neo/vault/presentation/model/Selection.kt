package com.neo.vault.presentation.model

class Selection<T>(
    val updated: Selection<T>.() -> Unit
) {

    private val selection = mutableSetOf<T>()

    val active get() = selection.isNotEmpty()

    fun add(value: T) {
        selection.add(value)
        updated()
    }

    fun remove(value: T) {
        selection.remove(value)
        updated()
    }

    fun contains(value: T) = selection.contains(value)
}
