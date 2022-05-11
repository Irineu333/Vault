package com.neo.vault.util

import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

object TimeUtils {

    val formatter: DateFormat by lazy {
        SimpleDateFormat.getDateInstance()
    }

    fun utcToLocal(utcTimeMillis: Long): Long {

        val formatter = SimpleDateFormat.getDateInstance()

        val utcDate = formatter.apply {
            timeZone = TimeZone.getTimeZone("UTC")
        }.format(Date(utcTimeMillis))

        return formatter.apply {
            timeZone = TimeZone.getDefault()
        }.parse(utcDate)!!.time
    }

}