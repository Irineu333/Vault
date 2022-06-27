package com.neo.vault.presentation.ui.feature.createTransaction.viewModel

import com.neo.vault.presentation.model.UiText

internal sealed class TransactionUiEffect {
    sealed class Error : TransactionUiEffect() {
        object InvalidOperation : Error()
        class Notice(val message: UiText) : Error()
    }
}