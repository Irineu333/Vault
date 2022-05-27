package com.neo.vault.presentation.model

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class Selection<T> {

    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    private val selection = mutableSetOf<T>()
    private val _state = MutableStateFlow(selection.toList())

    val state get() = _state.asStateFlow()
    val isActive: Boolean get() = state.value.isNotEmpty()

    fun add(value: T) {
        if (!selection.contains(value))
            selection.add(value)
        updated()
    }

    fun remove(value: T) {
        selection.remove(value)
        updated()
    }

    fun disableActionMode() {
        selection.clear()
        updated()
    }

    fun selected(value: T) = selection.contains(value)

    private fun updated() = coroutineScope.launch {
        _state.emit(selection.toList())
    }
}
