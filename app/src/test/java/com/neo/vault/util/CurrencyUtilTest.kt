package com.neo.vault.util

import junit.framework.TestCase

class CurrencyUtilTest : TestCase() {

    fun `test toMoney`() {
        assertEquals("R$ 1.000,00", CurrencyUtil.toCurrency(1000.00))
        assertEquals("R$ 1.000.000,00", CurrencyUtil.toCurrency(1000000.00))
    }
}