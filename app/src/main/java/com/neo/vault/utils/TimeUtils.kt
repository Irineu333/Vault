package com.neo.vault.utils

import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

object TimeUtils {

    val dateFormatter: DateFormat by lazy {
        SimpleDateFormat.getDateInstance()
    }

    val dateTimeFormatter: DateFormat by lazy {
        SimpleDateFormat.getDateTimeInstance()
    }

    val utcDateFormatter: DateFormat by lazy {
        SimpleDateFormat.getDateInstance().apply {
            timeZone = TimeZone.getTimeZone("UTC")
        }
    }

    fun utcToLocal(utcTimeMillis: Long): Long {
        val localDate = dateFormatter.parse(
            utcDateFormatter.format(
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