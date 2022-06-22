package com.neo.vault.presentation.ui.feature.createTransaction

import androidx.lifecycle.ViewModel
import com.neo.vault.presentation.model.Coin
import com.neo.vault.utils.CurrencyUtil
import com.neo.vault.utils.extension.firstWithIndex
import com.neo.vault.utils.extension.lastWithIndex
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.math.BigDecimal
import java.math.RoundingMode

class TransactionViewModel : ViewModel() {

    private val _uiState: MutableStateFlow<UiState> = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    fun insertNumber(number: Int) {
        val state = uiState.value
        val values = state.values

        val (index, last) = values.lastWithIndex()

        when (last) {
            is Value.Literal -> {

                val updated = last.updated(number)

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

                if (last.value.coin == 0) {
                    //error
                    return
                }

                _uiState.update {
                    it.copy(
                        values = it.values + operator + Value.Literal()
                    )
                }
            }

            is Value.Operator -> {
                //error
            }
        }
    }

    fun backSpace() {
        val state = uiState.value
        val values = state.values

        val (index, last) = values.lastWithIndex()

        when (last) {
            is Value.Literal -> {
                if (index > 1 && last.value.coin == 0) {
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
            } ?: throw IllegalStateException("no operator found")

            @Suppress("UNCHECKED_CAST")
            return result as Pair<Int, Value.Operator>
        }

        while (values.size >= 3) {

            val (index, operator) = getOperator()

            val a = values[index - 1] as Value.Literal
            val b = values[index + 1] as Value.Literal

            val result = when (operator) {
                Value.Operator.Times -> a.value * b.value
                Value.Operator.Divider -> a.value / b.value
                Value.Operator.Minus -> a.value - b.value
                Value.Operator.Plus -> a.value + b.value
            }

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
        fun formatted() = buildString {
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
            }
        }

        fun last(): Value {
            return values.last()
        }
    }

    sealed class Value {

        class Literal(
            val value: Coin = Coin(0)
        ) : Value() {

            fun updated(number: Int): Literal {

                val up = value.coin * 10L
                val coin = up + number

                if (coin > Int.MAX_VALUE) {
                    return this
                }

                return Literal(Coin(coin.toInt()))
            }

            fun backSpace(): Literal {
                val down = value.coin / 10

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