package com.kenwang.kenapps.data.repository.garbagetruck

import com.google.gson.annotations.SerializedName

data class GarbageTruckResponse(
    @SerializedName("contentType") val contentType: String,
    @SerializedName("isImage") val isImage: Boolean,
    @SerializedName("data") val trucks: List<GarbageTruckBean>
)

data class GarbageTruckBean(
    @SerializedName("linid") val linId: String?,
    @SerializedName("car") val car: String?,
    @SerializedName("time") val time: String?,
    @SerializedName("location") val location: String?,
    @SerializedName("x") val longitude: Double?,
    @SerializedName("y") val latitude: Double?
)
