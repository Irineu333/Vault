package com.neo.vault.presentation.ui.feature.createTransaction

import androidx.lifecycle.ViewModel
import com.neo.vault.domain.model.Coin
import com.neo.vault.utils.CurrencyUtil
import com.neo.vault.utils.extension.firstWithIndex
import com.neo.vault.utils.extension.lastWithIndex
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.math.BigInteger

class TransactionViewModel : ViewModel() {

    private val _uiState: MutableStateFlow<UiState> = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    fun insertNumber(number: Int) {
        val state = uiState.value
        val values = state.values

        val (index, last) = values.lastWithIndex()

        when (last) {
            is Value.Literal -> {

                val updated = runCatching {
                    last.updated(number)
                }.getOrNull() ?: return

                _uiState.update {
                    it.copy(
                        values = values.subList(0, index) + updated
                    )
                }
            }

            is Value.Operator -> {

                val newLiteral = Value.Literal().updated(number)

                _uiState.update {
                    it.copy(
                        values = values + newLiteral
                    )
                }
            }
        }
    }

    fun insertPlus() = insertOperator(Value.Operator.Plus)

    fun insertTimes() = insertOperator(Value.Operator.Times)

    fun insertMinus() = insertOperator(Value.Operator.Minus)

    fun insertDivider() = insertOperator(Value.Operator.Divider)

    private fun insertOperator(operator: Value.Operator) {
        val value = uiState.value

        when (val last = value.last()) {
            is Value.Literal -> {

                if (last.value.coin == BigInteger.ZERO) {
                    // error
                    return
                }

                _uiState.update {
                    it.copy(
                        values = it.values + operator + Value.Literal()
                    )
                }
            }

            is Value.Operator -> {
                // error
            }
        }
    }

    fun backSpace() {
        val state = uiState.value
        val values = state.values

        val (index, last) = values.lastWithIndex()

        when (last) {
            is Value.Literal -> {
                if (index > 1 && last.value.coin == BigInteger.ZERO) {
                    _uiState.update {
                        it.copy(
                            values = values.subList(0, index - 1)
                        )
                    }
                    return
                }

                _uiState.update {
                    it.copy(
                        values = values.subList(0, index) + last.backSpace()
                    )
                }
            }

            is Value.Operator -> {
                _uiState.update {
                    it.copy(
                        values = values.subList(0, index)
                    )
                }
            }
        }
    }

    fun clearAll() {
        _uiState.update {
            it.copy(
                values = listOf(Value.Literal())
            )
        }
    }

    fun resolve() {
        val state = uiState.value
        var values = state.values

        fun getOperator(): Pair<Int, Value.Operator> {

            val result = values.firstWithIndex {
                it is Value.Operator.Times || it is Value.Operator.Divider
            } ?: values.firstWithIndex {
                it is Value.Operator.Plus || it is Value.Operator.Minus
            } ?: error("no operator found")

            @Suppress("UNCHECKED_CAST")
            return result as Pair<Int, Value.Operator>
        }

        while (values.size >= 3) {

            val (index, operator) = getOperator()

            val a = values[index - 1] as Value.Literal
            val b = values[index + 1] as Value.Literal

            val result = runCatching {
                when (operator) {
                    Value.Operator.Times -> a.value * b.value
                    Value.Operator.Divider -> a.value / b.value
                    Value.Operator.Minus -> a.value - b.value
                    Value.Operator.Plus -> a.value + b.value
                }
            }.getOrNull() ?: break

            val beforeValues = values.subList(0, index - 1)
            val afterValues = values.subList(index + 2, values.size)

            val newLiteral = Value.Literal(result)

            values = beforeValues + newLiteral + afterValues

            _uiState.update { it.copy(values = values) }
        }
    }

    data class UiState(
        val values: List<Value> = mutableListOf(Value.Literal())
    ) {
        fun formatted(separator : String) = buildString {

            append("  ")

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
                        append(" /$separator")
                    }
                    Value.Operator.Minus -> {
                        append(" -$separator")
                    }
                    Value.Operator.Plus -> {
                        append(" +$separator")
                    }
                    Value.Operator.Times -> {
                        append(" *$separator")
                    }
                }
            }

            append("  ")
        }

        fun last(): Value {
            return values.last()
        }
    }

    sealed class Value {

        class Literal(
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
