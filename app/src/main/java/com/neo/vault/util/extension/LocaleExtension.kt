package com.neo.vault.util.extension

import java.util.*

val Locale.currency get() = Currency.getInstance(this)