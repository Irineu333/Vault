package com.neo.vault.presentation.ui.feature.createTransaction.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.neo.vault.core.Resource
import com.neo.vault.utils.extension.firstWithIndex
import com.neo.vault.utils.extension.lastWithIndex
import com.neo.vault.utils.extension.toRaw
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.math.BigInteger

internal class TransactionViewModel : ViewModel() {

    private val _uiState: MutableStateFlow<TransactionUiState> =
        MutableStateFlow(TransactionUiState())
    val uiState: StateFlow<TransactionUiState> = _uiState.asStateFlow()

    private val _uiEffect: Channel<TransactionUiEffect> = Channel()
    val uiEffect = _uiEffect.receiveAsFlow()

    fun insertNumber(number: Int) {
        val state = uiState.value
        val values = state.values

        val (index, last) = values.lastWithIndex()

        when (last) {
            is TransactionUiState.Value.Literal -> {

                val updated = runCatching {
                    last.updated(number)
                }.getOrNull() ?: return

                _uiState.update {
                    it.copy(
                        values = values.subList(0, index) + updated
                    )
                }
            }

            is TransactionUiState.Value.Operator -> {

                val newLiteral = TransactionUiState.Value.Literal().updated(number)

                _uiState.update {
                    it.copy(
                        values = values + newLiteral
                    )
                }
            }
        }
    }

    fun insertOperator(operator: TransactionUiState.Value.Operator) {
        val value = uiState.value

        when (val last = value.last()) {
            is TransactionUiState.Value.Literal -> {

                if (last.value.coin == BigInteger.ZERO) {
                    viewModelScope.launch {
                        _uiEffect.send(
                            TransactionUiEffect.Error.Notice(
                                "Digite um valor maior que zero".toRaw()
                            )
                        )
                    }
                    return
                }

                _uiState.update {
                    it.copy(
                        values = it.values + operator + TransactionUiState.Value.Literal()
                    )
                }
            }

            is TransactionUiState.Value.Operator -> error("invalid values state ${value.values}")
        }
    }

    fun toBackSpace() {
        val state = uiState.value
        val values = state.values

        val (index, last) = values.lastWithIndex()

        when (last) {
            is TransactionUiState.Value.Literal -> {
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

            is TransactionUiState.Value.Operator -> {
                _uiState.update {
                    it.copy(
                        values = values.subList(0, index)
                    )
                }
            }
        }
    }

    fun toClearAll() {
        _uiState.update {
            it.copy(
                values = listOf(TransactionUiState.Value.Literal())
            )
        }
    }

    fun toResolve() {
        when (val result = getResolvedValue()) {
            is Resource.Success -> {
                _uiState.update {
                    it.copy(
                        values = listOf(result.data)
                    )
                }
            }
            is Resource.Error -> {
                viewModelScope.launch {
                    _uiEffect.send(
                        TransactionUiEffect.Error.InvalidOperation
                    )
                }
            }
        }
    }

    private fun getResolvedValue(): Resource<TransactionUiState.Value.Literal> {
        val values = uiState.value.values.toMutableList()

        fun getOperator(): Pair<Int, TransactionUiState.Value.Operator> {

            val (index, operator) = values.firstWithIndex {
                it is TransactionUiState.Value.Operator.Times ||
                        it is TransactionUiState.Value.Operator.Divider
            } ?: values.firstWithIndex {
                it is TransactionUiState.Value.Operator.Plus ||
                        it is TransactionUiState.Value.Operator.Minus
            } ?: error("no operator found in $values")

            if (operator !is TransactionUiState.Value.Operator) {
                error("invalid operator")
            }

            return index to operator
        }

        while (values.size > 1) {

            val (index, operator) = getOperator()

            val a = values[index - 1] as TransactionUiState.Value.Literal
            val b = values[index + 1] as TransactionUiState.Value.Literal

            runCatching {
                TransactionUiState.Value.Literal(
                    when (operator) {
                        TransactionUiState.Value.Operator.Times -> a.value * b.value
                        TransactionUiState.Value.Operator.Divider -> a.value / b.value
                        TransactionUiState.Value.Operator.Minus -> a.value - b.value
                        TransactionUiState.Value.Operator.Plus -> a.value + b.value
                    }
                )
            }.onFailure {
                return Resource.Error(it)
            }.onSuccess {

                values[index] = it

                values.removeAt(index + 1)
                values.removeAt(index - 1)
            }
        }

        val result = values.first()

        if (result !is TransactionUiState.Value.Literal) {
            error("invalid value result $result")
        }

        return Resource.Success(result)
    }

    fun createTransaction() {
        when (val result = getResolvedValue()) {
            is Resource.Success -> {

            }
            is Resource.Error -> {
                viewModelScope.launch {
                    _uiEffect.send(
                        TransactionUiEffect.Error.InvalidOperation
                    )
                }
            }
        }
    }
}