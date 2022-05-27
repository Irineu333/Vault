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
    fun isToBreak() = dateToBreak?.let { it < System.currentTimeMillis() } ?: false

    enum class Type {
        PIGGY_BANK,
        GOAL
    }

    override fun equals(other: Any?): Boolean {
        return if (other is Vault) {
            other.id == id
        } else false
    }

    override fun hashCode(): Int {
        var result = id
        result = 31 * result + name.hashCode()
        result = 31 * result + (dateToBreak?.hashCode() ?: 0)
        result = 31 * result + currency.hashCode()
        result = 31 * result + type.hashCode()
        result = 31 * result + summation.hashCode()
        return result
    }
}