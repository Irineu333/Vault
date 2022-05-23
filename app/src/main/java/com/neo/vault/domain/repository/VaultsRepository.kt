package com.neo.vault.domain.repository

import com.neo.vault.domain.model.CreatePiggyBankResult
import com.neo.vault.domain.model.CurrencyCompat
import com.neo.vault.domain.model.Vault

interface VaultsRepository {

    suspend fun createPiggyBank(
        name: String,
        currency: CurrencyCompat,
        dateToBreak: Long?
    ): CreatePiggyBankResult

    suspend fun getVaultByName(name: String): Vault?
    suspend fun loadPiggyBanks(): List<Vault>
    suspend fun removeAll(vaults : List<Vault>)
}