package com.neo.vault.util.extension

import java.text.SimpleDateFormat
import java.util.*

val formatter by lazy { SimpleDateFormat.getDateInstance() }

val Calendar.formatted: String get() = formatter.format(time)