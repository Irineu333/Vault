package com.neo.vault.presentation.model

typealias Action = () -> Unit

sealed class Value {

    abstract val value: Double
    abstract val action: Action?

    data class Total(
        override val value: Double,
        override val action: Action? = null,
    ) : Value()

    data class SubTotal(
        override val value: Double,
        override val action: Action? = null,
        val title: String
    ) : Value()
}