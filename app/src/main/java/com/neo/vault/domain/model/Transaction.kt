package com.neo.vault.domain.model

data class Transaction(
    val id: Int,
    val value: Float,
    val date: Long,
    val summation: Float
) {
    val oldSummation: Float get() = summation - value
    val isPositive get() = value > 0
}