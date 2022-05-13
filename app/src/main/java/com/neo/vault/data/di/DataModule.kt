package com.neo.vault.data.di

import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import com.neo.vault.data.local.AppDatabase
import com.neo.vault.data.local.dao.VaultDao
import com.neo.vault.data.repository.VaultsRepositoryIml
import com.neo.vault.domain.repository.VaultsRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DataBindsModule {
    @Singleton
    @Binds
    abstract fun bindsVaultsRepository(
        repository: VaultsRepositoryIml
    ): VaultsRepository
}

@Module
@InstallIn(SingletonComponent::class)
class DataProvidesModule {

    @Provides
    @Singleton
    fun providesAppDatabase(
        @ApplicationContext applicationContext: Context
    ): AppDatabase {
        return Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "vault_db"
        ).build()
    }

    @Provides
    @Singleton
    fun providesVaultsDao(
        appDatabase: AppDatabase
    ): VaultDao {
        return appDatabase.vaultsDao()
    }

    @Singleton
    @Provides
    fun provideSharedPreference(
        @ApplicationContext context: Context
    ): SharedPreferences {
        return context.getSharedPreferences(
            context.packageName + "_preferences",
            Context.MODE_PRIVATE
        )
    }
}