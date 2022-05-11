package com.neo.vault.presentation.ui.feature.createVault.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.neo.vault.domain.model.Currency
import com.neo.vault.domain.repository.VaultsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class CreateVaultViewModel @Inject constructor(
    private val vaultsRepository: VaultsRepository
) : ViewModel() {

    private val _dateToBreak = MutableStateFlow<Calendar?>(null)
    val dateToBreak: StateFlow<Calendar?> = _dateToBreak

    fun setDateToBreak(
        timeInMillis: Long
    ) {
        _dateToBreak.update {
            (it ?: Calendar.getInstance()).apply {
                this.timeInMillis = timeInMillis
            }
        }
    }

    fun createPiggyBank(
        name: String,
        currency: Currency,
        dateToBreak: Long? = this.dateToBreak.value?.timeInMillis
    ) = viewModelScope.launch {
        vaultsRepository.createPiggyBank(
            name = name,
            currency = currency,
            dateToBreak = dateToBreak
        )
    }

    suspend fun hasVaultWithName(name: String): Boolean {
        return vaultsRepository.getVaultByName(name) != null
    }
}