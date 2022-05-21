package com.neo.vault.utils

import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

object TimeUtils {

    val formatter: DateFormat by lazy {
        SimpleDateFormat.getDateInstance()
    }

    val utcFormatter: DateFormat by lazy {
        SimpleDateFormat.getDateInstance().apply {
            timeZone = TimeZone.getTimeZone("UTC")
        }
    }

    fun utcToLocal(utcTimeMillis: Long): Long {
        val localDate = formatter.parse(
            utcFormatter.format(
                Date(utcTimeMillis)
            )
        )

        return localDate?.time
            ?: throw Exception(
                "cannot convert UTC millis " +
                        "$utcTimeMillis to local timeZone"
            )
    }

}