package com.neo.vault.presentation.ui.sync

import kotlinx.coroutines.flow.MutableSharedFlow

object Sync {
    val piggyBanks = MutableSharedFlow<Updated>()

    sealed class Updated {
        object PiggyBanks : Updated()
    }
}