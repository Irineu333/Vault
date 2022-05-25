package com.neo.vault.presentation.model

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class Selection<T> {

    private val selection = mutableSetOf<T>()
    private val _selectsState = MutableStateFlow(selection.toList())

    val selectsState get() = _selectsState.asStateFlow()
    val isActive: Boolean get() = selectsState.value.isNotEmpty()

    fun add(value: T) {
        selection.add(value)
        updated()
    }

    fun remove(value: T) {
        selection.remove(value)
        updated()
    }

    fun removeAll() {
        selection.clear()
        updated()
    }

    fun selected(value: T) = selection.contains(value)

    private fun updated() = CoroutineScope(Dispatchers.Main).launch {
        _selectsState.emit(selection.toList())
    }
}
