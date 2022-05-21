package com.neo.vault.utils

import com.neo.vault.domain.model.CurrencyCompat
import java.text.NumberFormat

object CurrencyUtil {

    fun formatter(value: Float, currency: CurrencyCompat): String {
        val format = NumberFormat.getCurrencyInstance().apply {
            setCurrency(currency.currency)
        }

        return format.format(value)
    }
}