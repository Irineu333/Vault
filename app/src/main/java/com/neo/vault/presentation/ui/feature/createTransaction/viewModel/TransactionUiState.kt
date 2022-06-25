package com.neo.vault.presentation.ui.feature.createTransaction.viewModel

import com.neo.vault.utils.CurrencyUtil

internal data class TransactionUiState(
    val values: List<TransactionViewModel.Value> = mutableListOf(
        TransactionViewModel.Value.Literal()
    )
) {
    fun formatted(separator: String) = buildString {

        append("  ")

        for (value in values) {
            when (value) {
                is TransactionViewModel.Value.Literal -> {
                    append(
                        CurrencyUtil.formatter(
                            value.value.toMoney()
                        )
                    )
                }
                TransactionViewModel.Value.Operator.Divider -> {
                    append(" /$separator")
                }
                TransactionViewModel.Value.Operator.Minus -> {
                    append(" -$separator")
                }
                TransactionViewModel.Value.Operator.Plus -> {
                    append(" +$separator")
                }
                TransactionViewModel.Value.Operator.Times -> {
                    append(" *$separator")
                }
            }
        }

        append("  ")
    }

    fun last(): TransactionViewModel.Value {
        return values.last()
    }
}