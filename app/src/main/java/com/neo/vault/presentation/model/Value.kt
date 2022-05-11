package com.neo.vault.presentation.model

sealed class Value {

    abstract val value: Double

    data class Total(
        override val value: Double,
    ) : Value()

    data class SubTotal(
        override val value: Double,
        val title : String
    ) : Value()
}