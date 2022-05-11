package com.neo.vault.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.neo.vault.data.local.entity.VaultEntity

@Dao
interface VaultDao {
    @Insert
    suspend fun insertVault(vault: VaultEntity)

    @Query("SELECT * FROM vault_tb WHERE name LIKE :name")
    suspend fun findByName(name: String): VaultEntity?
}