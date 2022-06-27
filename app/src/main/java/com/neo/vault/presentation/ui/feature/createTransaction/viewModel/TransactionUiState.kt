package com.neo.vault.presentation.ui.feature.createTransaction.viewModel

import com.neo.vault.utils.CurrencyUtil

internal data class TransactionUiState(
    val values: List<TransactionViewModel.Value> = mutableListOf(
        TransactionViewModel.Value.Literal()
    )
) {
    fun formatted(horizontal: Boolean = false) = buildString {

        append("  ")

        if (horizontal) {
            append("\n")
        }

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
                    append(" / ")
                }
                TransactionViewModel.Value.Operator.Minus -> {
                    append(" - ")
                }
                TransactionViewModel.Value.Operator.Plus -> {
                    append(" + ")
                }
                TransactionViewModel.Value.Operator.Times -> {
                    append(" * ")
                }
            }

            if (horizontal) {
                append("\n")
            }
        }


        append("  ")
    }

    fun last(): TransactionViewModel.Value {
        return values.last()
    }
}