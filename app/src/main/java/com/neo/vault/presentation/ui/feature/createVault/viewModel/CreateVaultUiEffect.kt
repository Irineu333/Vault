package com.neo.vault.presentation.ui.feature.createVault.viewModel

import com.neo.vault.presentation.model.UiText

sealed class CreateVaultUiEffect {
    data class Message(
        val message: UiText
    ) : CreateVaultUiEffect()

    object Success : CreateVaultUiEffect()
}