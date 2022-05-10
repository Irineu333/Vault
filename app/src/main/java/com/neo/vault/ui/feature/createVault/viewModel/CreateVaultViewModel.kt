package com.neo.vault.ui.feature.createVault.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import java.text.SimpleDateFormat
import java.util.*

class CreateVaultViewModel : ViewModel() {

    private val _dateToBreak = MutableStateFlow<Calendar?>(null)
    val dateToBreak : StateFlow<Calendar?> = _dateToBreak

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
        dateToBreak: Long? = null
    ) {

    }

    fun hasVaultWithName(name: String): Boolean {
        return false
    }

    enum class Currency {
        BRL,
        USD,
        EUR
    }
}