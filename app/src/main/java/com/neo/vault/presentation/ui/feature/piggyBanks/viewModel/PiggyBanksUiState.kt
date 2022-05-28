package com.neo.vault.presentation.ui.feature.piggyBanks.viewModel

import com.neo.vault.domain.model.Vault
import com.neo.vault.presentation.model.Summation

data class PiggyBanksUiState(
    val toBreakPiggyBanks : List<Vault> = emptyList(),
    val joiningPiggyBanks : List<Vault> = emptyList(),
    val summations: List<Summation> = emptyList()
) {

    override fun hashCode(): Int {
        var result = toBreakPiggyBanks.hashCode()
        result = 31 * result + joiningPiggyBanks.hashCode()
        result = 31 * result + summations.hashCode()
        return result
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as PiggyBanksUiState

        if (toBreakPiggyBanks != other.toBreakPiggyBanks) return false
        if (joiningPiggyBanks != other.joiningPiggyBanks) return false
        if (summations != other.summations) return false

        return true
    }
}