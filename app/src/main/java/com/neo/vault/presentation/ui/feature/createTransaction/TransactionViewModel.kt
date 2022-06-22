package com.neo.vault.presentation.ui.feature.createTransaction

import androidx.lifecycle.ViewModel
import com.neo.vault.utils.CurrencyUtil
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

                val newLiteral = Value.Literal(0.00).updated(number)

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
        when (uiState.value.last()) {
            is Value.Literal -> {
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

    data class UiState(
        val values: List<Value> = mutableListOf(Value.Literal(0.00))
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
                val toBigDecimal = number.toBigDecimal()
                val bigDecimal = toBigDecimal.divide(BigDecimal(100.0))
                val insert = up + bigDecimal

                return Literal(insert.setScale(2, RoundingMode.FLOOR).toDouble())
            }

            fun backSpace(): Literal {
                val down = value.toBigDecimal().divide(BigDecimal(10.0))

                return Literal(down.setScale(2, RoundingMode.FLOOR).toDouble())
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