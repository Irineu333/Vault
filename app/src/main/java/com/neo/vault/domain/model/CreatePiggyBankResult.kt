package com.neo.vault.domain.model

sealed class CreatePiggyBankResult {
    data class Success(
        val millis: Long
    ) : CreatePiggyBankResult()

    sealed class Error : CreatePiggyBankResult() {
        data class Generic(
            val t: Throwable
        ) : Error()

        object SameName : Error()
    }
}