package com.neo.vault.util

import com.neo.vault.domain.model.CurrencySupport
import java.text.NumberFormat

object CurrencyUtil {

    fun formatter(value: Float, currency: CurrencySupport): String {
        val format = NumberFormat.getCurrencyInstance().apply {
            setCurrency(currency.currency)
        }

        return format.format(value)
    }
}