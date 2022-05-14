package com.neo.vault.domain.model

data class Vault(
    val name: String,
    val dateToBreak: Long? = null,
    val currency: CurrencySupport,
    val type: Type,
    val summation: Float
)