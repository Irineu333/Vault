package com.neo.vault.domain.repository

import com.neo.vault.domain.model.CurrencySupport
import com.neo.vault.domain.model.Vault

interface VaultsRepository {
    suspend fun createPiggyBank(
        name: String,
        currency: CurrencySupport,
        dateToBreak: Long?
    ): Boolean

    suspend fun getVaultByName(name: String): Vault?
    suspend fun loadPiggyBanks(): List<Vault>
}