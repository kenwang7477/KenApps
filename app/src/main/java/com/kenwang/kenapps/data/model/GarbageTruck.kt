package com.kenwang.kenapps.data.model

import kotlinx.serialization.Serializable

@Serializable
data class GarbageTruck(
    val linId: String = "",
    val car: String = "",
    val time: String = "",
    val location: String = "",
    val longitude: Double = 0.0,
    val latitude: Double = 0.0
)
