package com.neo.vault.utils

import com.neo.vault.domain.model.CurrencyCompat
import java.text.NumberFormat

object CurrencyUtil {

    fun formatter(value: Number, currency: CurrencyCompat): String {
        val format = NumberFormat.getCurrencyInstance().apply {
            setCurrency(currency.currency)
        }

        return format.format(value)
    }

    fun formatter(value: Number): String {
        val format = NumberFormat.getNumberInstance().apply {
            minimumFractionDigits = 2
            maximumFractionDigits = 10
        }

        return format.format(value)
    }
}