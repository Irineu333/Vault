package com.neo.vault.utils.extension

import com.neo.vault.utils.TimeUtils.dateFormatter
import com.neo.vault.utils.TimeUtils.dateTimeFormatter
import java.util.*

val Date.dateFormatted: String get() = dateFormatter.format(this)
val Date.dateTimeFormatted: String get() = dateTimeFormatter.format(this)