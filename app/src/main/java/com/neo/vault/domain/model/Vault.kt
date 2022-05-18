package com.neo.vault.domain.model

data class Vault(
    val name: String,
    val dateToBreak: Long? = null,
    val currency: CurrencyCompat,
    val type: Type,
    val summation: Float
) {
    fun isToBreak() = dateToBreak?.let { it < System.currentTimeMillis() } ?: false

    enum class Type {
        PIGGY_BANK,
        GOAL
    }
}