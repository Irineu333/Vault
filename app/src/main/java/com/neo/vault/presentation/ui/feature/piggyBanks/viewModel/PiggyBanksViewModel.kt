package com.neo.vault.presentation.ui.feature.piggyBanks.viewModel

import androidx.lifecycle.ViewModel
import com.neo.vault.domain.repository.VaultsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PiggyBanksViewModel @Inject constructor(
    val repository: VaultsRepository
) : ViewModel() {

    fun loadPiggyBanks() {

    }
}