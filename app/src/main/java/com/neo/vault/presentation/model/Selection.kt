package com.neo.vault.presentation.model

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class Selection<T> {

    private val selection = mutableSetOf<T>()

    private val _state = MutableStateFlow(false)
    val state get() = _state.asStateFlow()

    fun add(value: T) {
        selection.add(value)
        updated()
    }

    fun remove(value: T) {
        selection.remove(value)
        updated()
    }

    private fun updated() = CoroutineScope(Dispatchers.Main).launch {
        _state.emit(selection.isNotEmpty())
    }

    fun selected(value: T) = selection.contains(value)
}
