package com.neo.vault.presentation.ui.feature.createVault.viewModel

sealed class CreateVaultUiEffect {
    object Success : CreateVaultUiEffect()
    object Error : CreateVaultUiEffect()
}