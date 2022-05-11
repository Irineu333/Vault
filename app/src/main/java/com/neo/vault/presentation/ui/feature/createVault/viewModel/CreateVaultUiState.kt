package com.neo.vault.presentation.ui.feature.createVault.viewModel

import java.util.*

data class CreateVaultUiState(
    val isLoading : Boolean = false,
    val dateToBreak : Date? = null
)