package com.neo.vault.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.neo.vault.data.local.dao.VaultDao
import com.neo.vault.data.local.entity.VaultEntity

@Database(entities = [VaultEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun vaultsDao(): VaultDao
}