package com.neo.vault.data.repository

import com.neo.vault.data.local.dao.VaultDao
import com.neo.vault.data.local.entity.VaultEntity
import com.neo.vault.data.local.entity.toModel
import com.neo.vault.domain.model.Currency
import com.neo.vault.domain.model.Type
import com.neo.vault.domain.model.Vault
import com.neo.vault.domain.repository.VaultsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class VaultsRepositoryIml @Inject constructor(
    private val vaultDao: VaultDao
) : VaultsRepository {
    override suspend fun createPiggyBank(
        name: String,
        currency: Currency,
        dateToBreak: Long?
    ) {
       withContext(Dispatchers.IO) {
           vaultDao.insertVault(
               VaultEntity(
                   name = name,
                   currency = currency,
                   dateToBreak = dateToBreak,
                   type = Type.PIGGY_BANK
               )
           )
       }
    }

    override suspend fun getVaultByName(name: String): Vault? {
        return withContext(Dispatchers.IO){
            vaultDao.findByName(
                name = name
            )
        }?.toModel()
    }
}