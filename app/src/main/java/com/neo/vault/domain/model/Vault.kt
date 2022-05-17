package com.neo.vault.domain.model

import com.neo.vault.util.CurrencyUtil

data class Vault(
    val name: String,
    val dateToBreak: Long? = null,
    val currency: CurrencySupport,
    val type: Type,
    val summation: Float
) {
    fun isToBreak() = dateToBreak?.let { it < System.currentTimeMillis() } ?: false

    enum class Type {
        PIGGY_BANK,
        GOAL
    }
}