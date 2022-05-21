package com.neo.vault.presentation.ui.feature.createVault.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.neo.vault.core.Resource
import com.neo.vault.domain.model.CurrencyCompat
import com.neo.vault.domain.repository.VaultsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class CreateVaultViewModel @Inject constructor(
    private val vaultsRepository: VaultsRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(CreateVaultUiState())
    val uiState = _uiState.asStateFlow()

    private val _uiEffect = MutableSharedFlow<CreateVaultUiEffect>()
    val uiEffect = _uiEffect.asSharedFlow()

    val dateToBreak get() = uiState.value.dateToBreak

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

        val result = vaultsRepository.createPiggyBank(
            name = name,
            currency = currency,
            dateToBreak = dateToBreak
        )

        _uiState.update {
            it.copy(
                isLoading = false
            )
        }

        when (result) {
            is Resource.Error -> {
                _uiEffect.emit(CreateVaultUiEffect.Error(result.message))
            }
            is Resource.Success -> {
                _uiEffect.emit(CreateVaultUiEffect.Success)
            }
        }
    }

    suspend fun hasVaultWithName(name: String): Boolean {
        return vaultsRepository.getVaultByName(name) != null
    }
}