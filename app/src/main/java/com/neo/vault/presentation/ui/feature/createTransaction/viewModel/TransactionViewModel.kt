package com.neo.vault.presentation.ui.feature.createTransaction.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.neo.vault.core.Resource
import com.neo.vault.domain.model.Coin
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
                        values = it.values + operator + Value.Literal()
                    )
                }
            }

            is Value.Operator -> error("invalid values state ${value.values}")
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

    private fun getResolvedValue(): Resource<Value.Literal> {
        val values = uiState.value.values.toMutableList()

        fun getOperator(): Pair<Int, Value.Operator> {

            val result = values.firstWithIndex {
                it is Value.Operator.Times || it is Value.Operator.Divider
            } ?: values.firstWithIndex {
                it is Value.Operator.Plus || it is Value.Operator.Minus
            } ?: error("no operator found in $values")

            @Suppress("UNCHECKED_CAST")
            return result as Pair<Int, Value.Operator>
        }

        while (values.size > 1) {

            val (index, operator) = getOperator()

            val a = values[index - 1] as Value.Literal
            val b = values[index + 1] as Value.Literal

            runCatching {
                Value.Literal(
                    when (operator) {
                        Value.Operator.Times -> a.value * b.value
                        Value.Operator.Divider -> a.value / b.value
                        Value.Operator.Minus -> a.value - b.value
                        Value.Operator.Plus -> a.value + b.value
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

        if (values.isEmpty()) {
            error("invalid values state $values")
        }

        return Resource.Success(values[0] as Value.Literal)
    }

    fun toWithdrawTransaction() {
        createTransaction()
    }

    fun toDepositTransaction() {
        createTransaction()
    }

    fun createTransaction() {
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