package com.neo.vault.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.neo.vault.domain.model.CurrencyCompat
import com.neo.vault.domain.model.Vault

@Entity(tableName = "vault_tb")
data class VaultEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Int? = null,
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "dateToBreak")
    val dateToBreak: Long? = null,
    @ColumnInfo(name = "currency")
    val currency: CurrencyCompat,
    @ColumnInfo(name = "type")
    val type: Vault.Type,
    @ColumnInfo(name = "summation")
    val summation: Float = 0f
)

fun VaultEntity.toModel() = Vault(
    id = id ?: throw NullPointerException("id cannot be null"),
    name = name,
    dateToBreak = dateToBreak,
    currency = currency,
    type = type,
    summation = summation
)

fun List<VaultEntity>.toModel() = map { it.toModel() }

fun Vault.toEntity() = VaultEntity(
    id = id,
    name = name,
    dateToBreak = dateToBreak,
    currency = currency,
    type = type,
    summation = summation
)

fun List<Vault>.toEntity() = map { it.toEntity() }