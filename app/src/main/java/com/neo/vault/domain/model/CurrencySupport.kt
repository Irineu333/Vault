package com.neo.vault.domain.model

import com.neo.vault.app
import com.neo.vault.util.extension.currency
import com.neo.vault.util.extension.locale
import java.util.*

enum class CurrencySupport {
    BRL,
    USD,
    EUR;

    val currency: Currency get() = Currency.getInstance(name)

    companion object {
        fun default(): CurrencySupport {
            return runCatching {
                valueOf(app.locale.currency.currencyCode)
            }.getOrElse {
                BRL
            }
        }
    }
}