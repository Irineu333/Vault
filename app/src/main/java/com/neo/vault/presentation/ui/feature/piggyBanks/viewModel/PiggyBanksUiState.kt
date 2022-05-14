package com.neo.vault.presentation.ui.feature.piggyBanks.viewModel

import com.neo.vault.domain.model.Vault
import com.neo.vault.presentation.model.Summation

data class PiggyBanksUiState(
    val piggyBanks : List<Vault> = emptyList(),
    val summations: List<Summation> = emptyList()
)