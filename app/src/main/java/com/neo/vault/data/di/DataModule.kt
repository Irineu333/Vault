package com.neo.vault.data.di

import com.neo.vault.data.repository.VaultsRepositoryIml
import com.neo.vault.domain.repository.VaultsRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
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