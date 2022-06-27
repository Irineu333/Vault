package com.neo.vault.presentation.ui.feature.createTransaction.viewModel

import com.neo.vault.domain.model.Coin
import com.neo.vault.utils.CurrencyUtil
import java.math.BigInteger

internal data class TransactionUiState(
    val values: List<Value> = mutableListOf(
        Value.Literal()
    )
) {
    fun formatted(horizontal: Boolean = false) = buildString {

        append("  ")

        if (horizontal) {
            append("\n")
        }

        for (value in values) {
            when (value) {
                is Value.Literal -> {
                    append(
                        CurrencyUtil.formatter(
                            value.value.toMoney()
                        )
                    )
                }
                Value.Operator.Divider -> {
                    append(" / ")
                }
                Value.Operator.Minus -> {
                    append(" - ")
                }
                Value.Operator.Plus -> {
                    append(" + ")
                }
                Value.Operator.Times -> {
                    append(" * ")
                }
            }

            if (horizontal) {
                append("\n")
            }
        }


        append("  ")
    }

    fun last(): Value {
        return values.last()
    }


    sealed class Value {

        data class Literal(
            val value: Coin = Coin()
        ) : Value() {

            fun updated(number: Int): Literal {

                val up = value.coin * BigInteger.TEN
                val coin = up + BigInteger("$number")

                return Literal(Coin(coin))
            }

            fun backSpace(): Literal {
                val down = value.coin / BigInteger.TEN

                return Literal(Coin(down))
            }
        }

        sealed class Operator : Value() {
            object Plus : Operator()
            object Minus : Operator()
            object Times : Operator()
            object Divider : Operator()
        }
    }
}