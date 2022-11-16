package com.kenwang.kenapps.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object TimeUtil {

    fun timestampToDate(timestamp: Long): String {
        val dateFormat = SimpleDateFormat("yyyy/MM/dd HH:mm", Locale.getDefault())
        val date = Date(timestamp)
        return dateFormat.format(date)
    }
}
