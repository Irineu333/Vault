package com.neo.vault.presentation.model

import com.neo.vault.domain.model.CurrencySupport

typealias Action = () -> Unit

sealed class Value {

    abstract val value: Float
    abstract val action: Action?
    abstract val currency : CurrencySupport
    abstract val title: UiText

    data class Total(
        override val value: Float,
        override val currency: CurrencySupport,
        override val action: Action? = null,
        override val title: UiText = UiText.empty()
    ) : Value()

    data class SubTotal(
        override val value: Float,
        override val currency: CurrencySupport,
        override val action: Action? = null,
        override val title: UiText = UiText.empty()
    ) : Value()
}