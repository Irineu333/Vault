package com.neo.vault.presentation.ui.feature.createTransaction

import androidx.lifecycle.ViewModel
import com.neo.vault.utils.CurrencyUtil
import com.neo.vault.utils.extension.lastWithIndex
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class TransactionViewModel : ViewModel() {

    private val _uiState: MutableStateFlow<UiState> = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    fun insertNumber(number: Int) {
        val state = uiState.value
        val values = state.values

        val (index, last) = values.lastWithIndex()

        when (last) {
            is Value.Literal -> {

                val newLiteral = Value.Literal("${last.value}$number")

                _uiState.update {
                    it.copy(
                        values = values.subList(0, index) + newLiteral
                    )
                }
            }

            is Value.Operator -> {

                val newLiteral = Value.Literal(number.toString())

                _uiState.update {
                    it.copy(
                        values = values + newLiteral
                    )
                }
            }
        }
    }

    fun insertComma() {
        val state = uiState.value
        val values = state.values

        val (index, last) = values.lastWithIndex()

        when (last) {
            is Value.Literal -> {

                val newLiteral = Value.Literal("${last.value}.")

                _uiState.update {
                    it.copy(
                        values = values.subList(0, index) + newLiteral
                    )
                }
            }

            is Value.Operator -> {

                val newLiteral = Value.Literal("0.")

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
                        values = it.values + operator
                    )
                }
            }

            is Value.Operator -> {
                //error
            }
        }
    }
}

data class UiState(
    val values: List<Value> = mutableListOf(Value.Literal("0"))
) {
    fun formatted() = buildString {
        for (value in values) {
            when (value) {
                is Value.Literal -> {
                    append(
                        CurrencyUtil.formatter(
                            value.value.toFloat()
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

    fun lastWithIndex(): Pair<Int, Value> {
        return values.lastWithIndex()
    }
}

sealed class Value {

    class Literal(
        val value: String
    ) : Value()

    sealed class Operator : Value() {
        object Plus : Operator()
        object Minus : Operator()
        object Times : Operator()
        object Divider : Operator()
    }
}