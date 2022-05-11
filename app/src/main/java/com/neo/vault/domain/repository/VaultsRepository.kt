package com.neo.vault.domain.repository

import com.neo.vault.domain.model.Currency
import com.neo.vault.domain.model.Vault

interface VaultsRepository {
    suspend fun createPiggyBank(
        name: String,
        currency: Currency,
        dateToBreak: Long?
    )

    suspend fun getVaultByName(name: String): Vault?
}