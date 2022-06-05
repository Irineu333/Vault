package com.neo.vault.presentation.model

data class Session<T>(
    val date: Long,
    val items: List<T>
) {
    var expanded = true

    fun getItemsIfExpanded() = if (expanded) items else emptyList()

    val size get() = items.size
}