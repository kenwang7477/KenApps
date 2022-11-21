package com.kenwang.kenapps.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object TimeUtil {

    fun timestampToDate(timestamp: Long, pattern: String = "yyyy/MM/dd HH:mm"): String {
        val dateFormat = SimpleDateFormat(pattern, Locale.getDefault())
        val date = Date(timestamp)
        return dateFormat.format(date)
    }
}
