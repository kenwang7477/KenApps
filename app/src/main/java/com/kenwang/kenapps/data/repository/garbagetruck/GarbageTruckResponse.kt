package com.kenwang.kenapps.data.repository.garbagetruck

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GarbageTruckResponse(
    @SerialName("contentType") val contentType: String,
    @SerialName("isImage") val isImage: Boolean,
    @SerialName("data") val trucks: List<GarbageTruckBean>
)

@Serializable
data class GarbageTruckBean(
    @SerialName("linid") val linId: String?,
    @SerialName("car") val car: String?,
    @SerialName("time") val time: String?,
    @SerialName("location") val location: String?,
    @SerialName("x") val longitude: Double?,
    @SerialName("y") val latitude: Double?
)
