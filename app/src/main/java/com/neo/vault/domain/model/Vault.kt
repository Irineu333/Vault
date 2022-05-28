package com.neo.vault.domain.model

import java.io.Serializable

data class Vault(
    val id: Int,
    val name: String,
    val dateToBreak: Long? = null,
    val currency: CurrencyCompat,
    val type: Type,
    val summation: Float
) : Serializable {

    fun isToBreak() = dateToBreak?.let {
        it < System.currentTimeMillis()
    } ?: false

    enum class Type {
        PIGGY_BANK,
        GOAL
    }
}