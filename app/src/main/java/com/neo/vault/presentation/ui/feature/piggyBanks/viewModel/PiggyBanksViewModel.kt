package com.neo.vault.presentation.ui.feature.piggyBanks.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.neo.vault.domain.model.CurrencyCompat
import com.neo.vault.domain.repository.VaultsRepository
import com.neo.vault.presentation.model.Summation
import com.neo.vault.utils.extension.summation
import com.neo.vault.utils.extension.toRaw
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PiggyBanksViewModel @Inject constructor(
    private val repository: VaultsRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(PiggyBanksUiState())
    val uiState = _uiState.asStateFlow()

    fun loadPiggyBanks() = viewModelScope.launch {

        val piggyBanks = repository.loadPiggyBanks()

        val totalValues = mutableListOf<Summation.Value>()
        val toBreakValues = mutableListOf<Summation.Value>()

        for (currency in CurrencyCompat.values()) {
            val filtered = piggyBanks.filter {
                it.currency == currency
            }
            if (filtered.isNotEmpty()) {
                totalValues.add(
                    Summation.Value(
                        value = filtered.summation { piggyBank ->
                            piggyBank.summation.toDouble()
                        }.toFloat(),
                        currency = currency
                    )
                )

                val filteredToBreak = filtered.filter { it.isToBreak() }

                val summation = filteredToBreak.summation { piggyBank ->
                    piggyBank.summation.toDouble()
                }

                if (summation > 0) {
                    toBreakValues.add(
                        Summation.Value(
                            value = summation.toFloat(),
                            currency = currency
                        )
                    )
                }
            }
        }

        val (toBreak, joining) = piggyBanks.partition { it.isToBreak() }

        val summations = mutableListOf<Summation>(
            Summation.Total(
                title = "Total".toRaw(),
                values = totalValues.ifEmpty {
                    Summation.default.values
                }
            )
        )

        if (toBreakValues.isNotEmpty()) {
            summations.add(
                Summation.SubTotal(
                    title = "Para quebrar".toRaw(),
                    values = toBreakValues
                )
            )
        }

        _uiState.update {

            it.copy(
                toBreakPiggyBanks = toBreak,
                joiningPiggyBanks = joining,
                summations = summations
            )
        }
    }
}