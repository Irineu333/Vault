package com.neo.vault.presentation.ui.feature.createVault.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.neo.vault.domain.model.CreatePiggyBankResult
import com.neo.vault.domain.model.CurrencyCompat
import com.neo.vault.domain.repository.VaultsRepository
import com.neo.vault.utils.extension.toRaw
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class CreateVaultViewModel @Inject constructor(
    private val vaultsRepository: VaultsRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(CreateVaultUiState())
    val uiState = _uiState.asStateFlow()

    private val _uiEffect = Channel<CreateVaultUiEffect>()
    val uiEffect = _uiEffect.receiveAsFlow()

    val dateToBreak get() = uiState.value.dateToBreak

    var editVaultId: Int? = null

    fun setDateToBreak(
        timeInMillis: Long
    ) {
        _uiState.update {
            it.copy(
                dateToBreak = Date(timeInMillis)
            )
        }
    }

    fun createPiggyBank(
        name: String,
        currency: CurrencyCompat,
        dateToBreak: Long? = this.dateToBreak?.time
    ) = viewModelScope.launch {

        _uiState.update {
            it.copy(
                isLoading = true
            )
        }

        val editVaultId = editVaultId

        val result = if (editVaultId != null) {
            vaultsRepository.editPiggyBank(
                id = editVaultId,
                name = name,
                currency = currency,
                dateToBreak = dateToBreak
            )
        } else {
            vaultsRepository.createPiggyBank(
                name = name,
                currency = currency,
                dateToBreak = dateToBreak
            )
        }

        _uiState.update {
            it.copy(
                isLoading = false
            )
        }

        when (result) {

            is CreatePiggyBankResult.Success -> {
                _uiEffect.send(CreateVaultUiEffect.Success)
            }

            CreatePiggyBankResult.Error.SameName -> {
                _uiEffect.send(CreateVaultUiEffect.Error("Número já existe".toRaw()))
            }

            CreatePiggyBankResult.Error.NotFound -> {
                _uiEffect.send(CreateVaultUiEffect.Error("Cofrinho não encontrado".toRaw()))
            }

            is CreatePiggyBankResult.Error.Generic -> {
                _uiEffect.send(CreateVaultUiEffect.Error("${result.t.message}".toRaw()))
            }
        }
    }

    suspend fun hasVaultWithName(name: String): Boolean {
        return vaultsRepository.getVaultByName(name) != null
    }
}