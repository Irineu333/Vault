package com.neo.vault.presentation.ui.feature.createVault.viewModel

import com.neo.vault.presentation.model.UiText

sealed class CreateVaultUiEffect {
    object Success : CreateVaultUiEffect()
}