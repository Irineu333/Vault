package com.neo.vault.util

import androidx.core.os.ConfigurationCompat
import com.neo.vault.app
import java.util.*
import java.util.regex.Pattern

object CurrencyUtil {

    val locale get() = ConfigurationCompat.getLocales(app.resources.configuration)[0]
    val currency get() = Currency.getInstance(locale)

    fun toCurrency(value: Double, currency: Currency = this.currency): String {
        val format = "${currency.symbol} %.${currency.defaultFractionDigits}f"
        val formatted = String.format(format, value)

        val config = when (currency.currencyCode) {
            "BRL" -> CurrencyConfig.BRL
            "USD" -> CurrencyConfig.USD
            else -> null
        }

        return config?.let { addSeparator(formatted, config) } ?: formatted
    }

    private fun addSeparator(value: String, config: CurrencyConfig): String {
        val builder = StringBuilder(value)

        val regex = Pattern.compile("\\d+(?=\\d{${config.groupCount}})")
        var matcher = regex.matcher(builder)

        while (matcher.find()) {
            builder.insert(matcher.end(), config.separator)
            matcher = regex.matcher(builder)
        }

        return builder.toString()
    }

    sealed class CurrencyConfig(
        val groupCount: Int,
        val separator: String?
    ) {
        object BRL : CurrencyConfig(
            groupCount = 3,
            separator = "."
        )

        object USD : CurrencyConfig(
            groupCount = 3,
            separator = ","
        )
    }
}