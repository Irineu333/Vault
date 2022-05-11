package com.neo.vault.util.extension

import com.neo.vault.util.TimeUtils.formatter
import java.util.*

val Date.formatted: String get() = formatter.format(this)