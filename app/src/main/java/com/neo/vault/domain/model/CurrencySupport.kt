package com.neo.vault.domain.model

import java.util.*

enum class CurrencySupport {
    BRL,
    USD,
    EUR;

    val currency: Currency get() = Currency.getInstance(name)
}