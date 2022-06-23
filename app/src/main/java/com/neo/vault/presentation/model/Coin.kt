package com.neo.vault.presentation.model

import kotlin.math.roundToLong

private fun Long.checkAndToInt(): Int {

    if (this > Int.MAX_VALUE) {
        throw IllegalStateException(
            "$this overflow to ${Int::class.java.simpleName}, max value is ${Int.MAX_VALUE}"
        )
    }

    if (this < Int.MIN_VALUE) {
        throw IllegalStateException(
            "$this underflow to ${Int::class.java.simpleName}, min value is ${Int.MIN_VALUE}"
        )
    }

    return toInt()
}

data class Coin(
    val coin: Int
) {

    constructor(coin: Long) : this(coin.checkAndToInt())

    @Suppress("UNUSED")
    constructor(money: Double) : this((money * 100).roundToLong())

    fun toMoney(): Double = coin / 100.0

    operator fun times(value: Coin): Coin {

        val result = (coin.toLong() * value.coin.toLong()) / 100L

        return Coin(result)
    }

    operator fun div(value: Coin): Coin {

        val result = (coin * 100L / value.coin.toLong())

        return Coin(result)
    }

    operator fun plus(value: Coin): Coin {

        val result = coin.toLong() + value.coin.toLong()

        return Coin(result)
    }

    operator fun minus(value: Coin): Coin {

        val result = coin - value.coin

        return Coin(result)
    }
}