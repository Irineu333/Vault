package com.neo.vault.utils.extension

import java.util.*

val Locale.currency get() = Currency.getInstance(this)