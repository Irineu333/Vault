package com.neo.vault.utils.extension

import com.neo.vault.utils.TimeUtils.dateFormatter
import com.neo.vault.utils.TimeUtils.dateTimeFormatter
import com.neo.vault.utils.TimeUtils.timeFormatter
import java.util.*

val Date.dateFormatted: String get() = dateFormatter.format(this)
val Date.dateTimeFormatted: String get() = dateTimeFormatter.format(this)
val Date.timeFormatted: String get() = timeFormatter.format(this)