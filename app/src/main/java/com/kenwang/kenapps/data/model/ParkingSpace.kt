package com.kenwang.kenapps.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ParkingSpaceBean(
    @SerialName("行政區") val area: String = "",
    @SerialName("型式") val type: String = "",
    @SerialName("場名") val name: String = "",
    @SerialName("位置") val address: String = "",
    @SerialName("大車") val largeCarCount: Int = 0,
    @SerialName("小車") val normalCarCount: Int = 0,
    @SerialName("機車") val bicycleCount: Int = 0,
    @SerialName("收費標準") val charges: String = "",
    @SerialName("管理業者") val information: String = ""
)

@Serializable
data class ParkingSpace(
    val area: String = "",
    val type: String = "",
    val name: String = "",
    val address: String = "",
    val longitude: Double = 0.0,
    val latitude: Double = 0.0,
    val largeCarCount: Int = 0,
    val normalCarCount: Int = 0,
    val bicycleCount: Int = 0,
    val charges: String = "",
    val information: String = ""
)
