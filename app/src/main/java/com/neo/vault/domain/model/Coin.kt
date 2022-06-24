package com.neo.vault.domain.model

import java.math.BigDecimal
import java.math.BigInteger

data class Coin(
    val coin: BigInteger = BigInteger.ZERO
) {

    fun toMoney(): BigDecimal = BigDecimal(coin).divide(BigDecimal.valueOf(100))

    operator fun times(value: Coin): Coin {

        val result = (coin * value.coin) / BigInteger.valueOf(100)

        return Coin(result)
    }

    operator fun div(value: Coin): Coin {

        val result = (coin * BigInteger.valueOf(100) / value.coin)

        return Coin(result)
    }

    operator fun plus(value: Coin): Coin {

        val result = coin + value.coin

        return Coin(result)
    }

    operator fun minus(value: Coin): Coin {

        val result = coin - value.coin

        return Coin(result)
    }
}