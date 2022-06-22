package com.neo.vault.presentation.ui.feature.createTransaction

import androidx.lifecycle.ViewModel
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

                if (updated.value > MAX_VALUE) {
                    //error
                    return
                }

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

                if (last.value == 0.00) {
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
                if (index > 1 && last.value == 0.00) {
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

            val aBigDecimal = a.value.toBigDecimal()
            val bBigDecimal = b.value.toBigDecimal()

            val result = when (operator) {
                Value.Operator.Divider -> aBigDecimal.divide(bBigDecimal)
                Value.Operator.Minus -> aBigDecimal.subtract(bBigDecimal)
                Value.Operator.Plus -> aBigDecimal.add(bBigDecimal)
                Value.Operator.Times -> aBigDecimal.multiply(bBigDecimal)
            }

            val beforeValues = values.subList(0, index - 1)
            val afterValues = values.subList(index + 2, values.size)

            val newLiteral = Value.Literal(result.roundedFloor().toDouble())

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
                                value.value
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
            val value: Double = 0.0
        ) : Value() {

            fun updated(number: Int): Literal {

                val up = value.toBigDecimal().multiply(BigDecimal(10.0))
                val decimal = number.toBigDecimal().divide(BigDecimal(100.0))
                val insert = up.add(decimal)

                return Literal(insert.roundedFloor().toDouble())
            }

            fun backSpace(): Literal {
                val down = value.toBigDecimal().divide(BigDecimal(10.0))

                return Literal(down.roundedFloor().toDouble())
            }
        }

        sealed class Operator : Value() {
            object Plus : Operator()
            object Minus : Operator()
            object Times : Operator()
            object Divider : Operator()
        }
    }

    companion object {
        const val MAX_VALUE = 999_999_999.99
    }
}

private fun BigDecimal.roundedFloor(scale: Int = 2): BigDecimal {
    return setScale(scale, RoundingMode.FLOOR)
}
