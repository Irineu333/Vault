package com.neo.vault.utils.extension

import com.neo.vault.utils.TimeUtils.formatter
import java.util.*

val Date.formatted: String get() = formatter.format(this)