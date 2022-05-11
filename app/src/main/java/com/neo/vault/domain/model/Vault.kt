package com.neo.vault.domain.model

import androidx.room.ColumnInfo
import androidx.room.PrimaryKey

data class Vault(
    val name: String,
    val dateToBreak: Long? = null,
    val currency: Currency,
    val type: Type
)