package com.neo.vault.domain.repository

import com.neo.vault.domain.model.Currency

interface VaultsRepository {
    fun createPiggyBank(
        name: String,
        currency: Currency,
        dateToBreak: Long?
    )
}