package com.kenwang.kenapps.data.model

import android.net.Uri

data class MapLocation(
    val timestamp: Long = 0,
    val title: String = "",
    val description: String = "",
    val longitude: Double = 0.0,
    val latitude: Double = 0.0,
    val pictureUri: Uri? = null
)
