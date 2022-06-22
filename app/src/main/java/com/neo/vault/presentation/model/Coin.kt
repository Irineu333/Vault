package com.neo.vault.presentation.model

import kotlin.math.roundToInt

data class Coin(
    val coin: Int
) {
    @Suppress("UNUSED")
    constructor(money: Double) : this((money * 100).roundToInt())

    fun toMoney(): Double = coin / 100.0

    operator fun times(value: Coin) = Coin((coin * value.coin) / 100)
    operator fun div(value: Coin) = Coin((coin * 100 / value.coin))
    operator fun plus(value: Coin) = Coin(coin + value.coin)
    operator fun minus(value: Coin) = Coin(coin - value.coin)

}