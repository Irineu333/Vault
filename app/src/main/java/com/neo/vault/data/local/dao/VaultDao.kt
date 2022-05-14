package com.neo.vault.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.neo.vault.data.local.entity.VaultEntity
import com.neo.vault.domain.model.Type

@Dao
interface VaultDao {
    @Insert
    suspend fun insertVault(vault: VaultEntity)

    @Query("SELECT * FROM vault_tb WHERE name LIKE :name LIMIT 1")
    suspend fun findByName(name: String): VaultEntity?

    @Query("SELECT * FROM vault_tb WHERE type LIKE :type")
    suspend fun loadVaultsByType(type: Type): List<VaultEntity>
}