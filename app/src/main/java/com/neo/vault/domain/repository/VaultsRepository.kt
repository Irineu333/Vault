package com.neo.vault.domain.repository

import com.neo.vault.core.Resource
import com.neo.vault.data.local.entity.VaultEntity
import com.neo.vault.domain.model.CurrencyCompat
import com.neo.vault.domain.model.Vault

interface VaultsRepository {

    suspend fun createPiggyBank(
        name: String,
        currency: CurrencyCompat,
        dateToBreak: Long?
    ): Resource<Unit>

    suspend fun getVaultByName(name: String): Vault?
    suspend fun loadPiggyBanks(): List<Vault>
}