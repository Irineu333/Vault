package com.neo.vault.domain.model

data class Transaction(
    val id: Int,
    val value: Float,
    val date: Long
)