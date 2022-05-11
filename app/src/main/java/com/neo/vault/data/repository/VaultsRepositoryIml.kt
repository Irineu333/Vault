package com.neo.vault.data.repository

import com.neo.vault.domain.model.Currency
import com.neo.vault.domain.repository.VaultsRepository
import javax.inject.Inject

class VaultsRepositoryIml @Inject constructor() : VaultsRepository {
    override fun createPiggyBank(
        name: String,
        currency: Currency,
        dateToBreak: Long?
    ) {

    }
}