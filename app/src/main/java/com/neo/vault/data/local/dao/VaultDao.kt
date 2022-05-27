package com.neo.vault.data.local.dao

import androidx.room.*
import com.neo.vault.data.local.entity.VaultEntity
import com.neo.vault.domain.model.Vault

@Dao
interface VaultDao {
    @Insert
    suspend fun insertVault(vault: VaultEntity)

    @Update
    suspend fun updateVault(vaultEntity: VaultEntity)

    @Query("SELECT * FROM vault_tb WHERE id LIKE :id LIMIT 1")
    suspend fun findById(id: Int): Vault

    @Query("SELECT * FROM vault_tb WHERE name LIKE :name LIMIT 1")
    suspend fun findByName(name: String): VaultEntity?

    @Query("SELECT * FROM vault_tb WHERE type LIKE :type")
    suspend fun loadVaultsByType(type: Vault.Type): List<VaultEntity>

    @Delete
    suspend fun removeAll(vaults: List<VaultEntity>)
}