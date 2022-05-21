package com.neo.vault.presentation.model

import com.neo.vault.domain.model.CurrencyCompat
import com.neo.vault.utils.extension.toRaw

typealias Action = () -> Unit

sealed class Summation {

    abstract val action: Action?
    abstract val title: UiText
    abstract val values: List<Value>

    data class Total(
        override val values: List<Value>,
        override val action: Action? = null,
        override val title: UiText = UiText.empty()
    ) : Summation()

    data class SubTotal(
        override val values: List<Value>,
        override val action: Action? = null,
        override val title: UiText = UiText.empty()
    ) : Summation()

    data class Value(
        val value: Float,
        val currency: CurrencyCompat
    )

    companion object {
        val default = Total(
            title = "Total".toRaw(),
            values =listOf(
                Value(
                    value = 0f,
                    currency = CurrencyCompat.default()
                )
            )
        )
    }
}